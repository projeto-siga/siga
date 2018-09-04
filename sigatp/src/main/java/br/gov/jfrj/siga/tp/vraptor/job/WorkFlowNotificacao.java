package br.gov.jfrj.siga.tp.vraptor.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.tasks.Task;
import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.tp.model.Andamento;
import br.gov.jfrj.siga.tp.model.EstadoRequisicao;
import br.gov.jfrj.siga.tp.model.Parametro;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.util.FormatarTextoHtml;
import br.gov.jfrj.siga.tp.util.VerificadorEnvioEmail;

@ApplicationScoped
public class WorkFlowNotificacao implements Task {

	private static final String espacosHtml = "&nbsp;&nbsp;";

	private static final String CRON_EXECUTAWORKFLOW = "cron.executaw";
	private static final String CRON_LISTAEMAIL = "cron.listaEmailw";
	private static final String SIGATP_WORKFLOW = "sigatp.workflow";
	private static final String CRON_ENVIARPARALISTA = "cron.enviarParaListaW";
	private static final String CAMINHO_HOSTNAME_STANDALONE = "caminhoHostnameStandalone";

	@Override
	public void execute() {
		try {
			CustomScheduler.criaEntityManager();

			if(!VerificadorEnvioEmail.possoEnviarEmail()) {
				return;
			}
			boolean executa = Boolean.parseBoolean(Parametro.buscarConfigSistemaEmVigor(CRON_EXECUTAWORKFLOW));
			
			if (executa) {
				try {
					notificarAndamentos();
				} catch (Exception ex) {
					Logger.getLogger(SIGATP_WORKFLOW).info("Erro no Servico de Notificacao do WorkFlow " + ex.getMessage());
				}
			} else {
				Logger.getLogger(SIGATP_WORKFLOW).info("Servico de Notificacao do WorkFlow desligado");
			}
			Logger.getLogger(SIGATP_WORKFLOW).info("Servico de Notificacao do WorkFlow finalizado");
		} catch (Exception e) {
			Logger.getLogger(SIGATP_WORKFLOW).info("Erro ao criar Entity: " + e.getMessage());
		} finally {
			CustomScheduler.fecharEntityManager();
		}
	}

	private static void notificarAndamentos() throws Exception {
		String titulo = "Notifica\u00E7\u00F5es do Andamento de Requisi\u00E7\u00F5es de Transporte";
		List<Andamento> andamentos = Andamento.listarPorDataNotificacaoWorkFlow();

		if (andamentos != null && andamentos.size() > 0) {
			for (Andamento item : andamentos) {
				Set<DpPessoa> lstPessoas = null;
				boolean notificar = false;

				if (item.getEstadoRequisicao().equals(EstadoRequisicao.ABERTA)) {
					lstPessoas = new HashSet<DpPessoa>(retornarConfiguracaoDpPessoa(item));
					notificar = true;
				}

				else if (item.getEstadoRequisicao().equals(EstadoRequisicao.REJEITADA) || item.getEstadoRequisicao().equals(EstadoRequisicao.AUTORIZADA)
						|| item.getEstadoRequisicao().equals(EstadoRequisicao.PROGRAMADA)) {

					lstPessoas = new HashSet<DpPessoa>();
					lstPessoas.add(item.getResponsavel());

					if (!item.getResponsavel().getId().equals(item.getRequisicaoTransporte().getSolicitante().getId())) {
						lstPessoas.add(item.getRequisicaoTransporte().getSolicitante());
					}
					notificar = true;
				}

				if (notificar) {
					SimpleDateFormat fr = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					String sequencia = item.getEstadoRequisicao().getDescricao() + " " + item.getRequisicaoTransporte().buscarSequence() + " " + item.getRequisicaoTransporte().getId() + " "
							+ fr.format(item.getRequisicaoTransporte().getDataHoraSaidaPrevista().getTime());

					if (enviarEmail(titulo, lstPessoas, sequencia)) {
						try {
							TpDao.iniciarTransacao();
							Andamento.gravarDataNotificacaoWorkFlow(item.getId());
							TpDao.commitTransacao();
						} catch (Exception ex) {
							Logger.getLogger(SIGATP_WORKFLOW).info("Falha ao gravar notifica\u00E7\u00E3o: " + ex.getMessage());
							TpDao.rollbackTransacao();
						}
					}
				}
			}
		}
	}

	private static Set<DpPessoa> retornarConfiguracaoDpPessoa(Andamento andamento) {
		List<CpConfiguracao> configuracoes = new ArrayList<CpConfiguracao>();
		Set<DpPessoa> setDpPessoa = new HashSet<DpPessoa>();
		List<DpPessoa> lstDpPessoa = new ArrayList<DpPessoa>();
		Object[] parametros = { "SIGA-TP-APR", "SIGA-TP-ADM", CpSituacaoConfiguracao.SITUACAO_PODE, andamento.getRequisicaoTransporte().getCpOrgaoUsuario().getIdOrgaoUsu() };

		configuracoes = CpConfiguracao.AR.find("cpServico.siglaServico in (?, ?) and cpSituacaoConfiguracao.idSitConfiguracao = ? and " + "hisDtFim is null and dpPessoa.orgaoUsuario.idOrgaoUsu = ? ",
				parametros).fetch();

		for (CpConfiguracao cpConfiguracao : configuracoes) {
			if (cpConfiguracao.getDpPessoa() != null) {
				setDpPessoa.add(cpConfiguracao.getDpPessoa());
			} else if (cpConfiguracao.getLotacao() != null) {
				lstDpPessoa = DpPessoa.AR.find("lotacao.idLotacao = ? and dataFimPessoa is null ", cpConfiguracao.getLotacao().getIdLotacao()).fetch();
				setDpPessoa.addAll(lstDpPessoa);
			}
		}

		return setDpPessoa;
	}

	private static Boolean enviarEmail(String titulo, Set<?> objeto, String linha) throws Exception {
		String hostName = System.getProperty(Parametro.buscarConfigSistemaEmVigor(CAMINHO_HOSTNAME_STANDALONE));
		final String finalMensagem = "<b>Mensagem autom&aacute;tica enviada pelo M&oacute;dulo de Transportes do Siga. Favor n&atilde;o responder.</b><br>";
		Boolean emailEnviado = false;
		String[] itensLinha = linha.split(" ");
		List<String> parametros = new ArrayList<String>();

		String conteudoHTML = "<html> <p> A requisi&ccedil;&atilde;o de Transporte" + espacosHtml + itensLinha[1] + espacosHtml + "com sa&iacute;da solicitada para" + espacosHtml + itensLinha[3]
				+ espacosHtml + "&agrave;s" + espacosHtml + itensLinha[4];

		if (itensLinha[0].equals(EstadoRequisicao.ABERTA.toString())) {
			conteudoHTML += espacosHtml + "est&aacute; pendente para an&aacute;lise de APROVA&Ccedil;&Atilde;O/REJEI&Ccedil;&Atilde;O."
					+ "<br>Para aprovar/rejeitar a requisi&ccedil;&atilde;o, clique em:" + espacosHtml;
			parametros.add("id," + itensLinha[2] + ",sigatp/app/andamento/autorizar,Autorizar");
			parametros.add("id," + itensLinha[2] + ",sigatp/app/andamento/rejeitar,Rejeitar");

			for (String parametro : parametros) {
				String[] itens = parametro.split(",");
				Map<String, Object> param = new HashMap<String, Object>();
				param.put(itens[0], itens[1]);
				String caminhoUrl = "/" + itens[2] + "/" + itens[1];
				conteudoHTML += "<a href='" + hostName + caminhoUrl + "'>" + itens[3] + "</a>" + (itens[2].contains("autorizar") ? " ou " : "");
			}
		} else if (itensLinha[0].equals(EstadoRequisicao.AUTORIZADA.toString()) || itensLinha[0].equals(EstadoRequisicao.REJEITADA.toString())
				|| itensLinha[0].equals(EstadoRequisicao.PROGRAMADA.toString())) {
			conteudoHTML += espacosHtml + "foi" + espacosHtml + itensLinha[0];
		}

		String remetente = SigaBaseProperties.getString("servidor.smtp.usuario.remetente");
		String email = "";

		boolean flagEnviarEmailParaLista = Boolean.parseBoolean(Parametro.buscarConfigSistemaEmVigor(CRON_ENVIARPARALISTA));

		if (!flagEnviarEmailParaLista) {
			for (Object pessoa : objeto) {
				if (pessoa.getClass().equals(DpPessoa.class)) {
					email = ((DpPessoa) pessoa).getEmailPessoa() + ",";
				}
			}
			email = email.substring(0, email.length() - 1);			
		} else {
			email = Parametro.buscarConfigSistemaEmVigor(CRON_LISTAEMAIL);			
		}

		conteudoHTML += ".</p>" + finalMensagem + "</html>";
		String conteudo = FormatarTextoHtml.retirarTagsHtml(conteudoHTML, espacosHtml);

		try {
			Correio.enviar(remetente, email.split(","), titulo, conteudo, conteudoHTML);
			emailEnviado = true;
			SimpleDateFormat fr = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			Logger.getLogger(SIGATP_WORKFLOW).info(fr.format(calendar.getTime()) + " - Email enviado para " + email + ", assunto: " + titulo);
		} catch (Exception ex) {
			Logger.getLogger(SIGATP_WORKFLOW).info("Falha ao enviar email:" + ex.getMessage());
		}

		return emailEnviado;
	}
}