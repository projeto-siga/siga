package br.gov.jfrj.siga.tp.vraptor.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.tasks.Task;
import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.tp.model.Condutor;
import br.gov.jfrj.siga.tp.model.EstadoMissao;
import br.gov.jfrj.siga.tp.model.Missao;
import br.gov.jfrj.siga.tp.model.Parametro;
import br.gov.jfrj.siga.tp.util.FormatarTextoHtml;

@ApplicationScoped
public class EmailNotificacao implements Task {
	private static final String espacosHtml = "&nbsp;&nbsp;&nbsp;&nbsp;";

	private static final String CRON_EXECUTA = "cron.executa";
	private static final String CRON_LISTAEMAIL = "cron.listaEmail";
	private static final String CRON_ENVIARPARALISTA = "cron.enviarParaLista";
	private static final String SIGATP_EMAIL = "sigatp.email";
	private static final String CAMINHO_HOSTNAME_STANDALONE = "caminhoHostnameStandalone";
	private static final String CRON_EXECUTA_NOTIFICARMISSOESPROGRAMADAS = "cron.executa.notificarMissoesProgramadas";

	@Override
	public void execute() {
		try {
			CustomScheduler.criaEntityManager();

			boolean executa = Boolean.parseBoolean(Parametro.buscarConfigSistemaEmVigor(CRON_EXECUTA));
			boolean notificarMissoesProgramadas = Boolean.parseBoolean(Parametro.buscarConfigSistemaEmVigor(CRON_EXECUTA_NOTIFICARMISSOESPROGRAMADAS));

			if (executa) {
				verificarVencimentoCarteira3MesesAntes();
				if(notificarMissoesProgramadas) {
					verificarMissoesProgramadas();
				}
				verificarMissoesIniciadasMaisDe7Dias();
			} else {
				Logger.getLogger(SIGATP_EMAIL).info("Servico desligado");
			}
			Logger.getLogger(SIGATP_EMAIL).info("Servico finalizado");
		} catch (Exception e) {
			Logger.getLogger(SIGATP_EMAIL).info("Erro ao criar Entity: " + e.getMessage());
		} finally {
			CustomScheduler.fecharEntityManager();
		}
	}

	private void verificarMissoesProgramadas() {
		List<Missao> missoes = new ArrayList<Missao>();
		String tituloEmail = "Missoes programadas nao iniciadas";
		String tipoNotificacao = "Nao iniciada";

		try {
			Calendar calendar = Calendar.getInstance();
			missoes = Missao.AR.find("estadoMissao = ? and dataHoraSaida < ? " + "order by condutor", EstadoMissao.PROGRAMADA, calendar).fetch();
			notificarMissoes(missoes, tituloEmail, tipoNotificacao);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void verificarMissoesIniciadasMaisDe7Dias() {
		List<Missao> missoes = new ArrayList<Missao>();
		String tituloEmail = "Missoes iniciadas a mais de 7 dias nao finalizadas";
		String tipoNotificacao = "Nao finalizada";

		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, -7);
			missoes = Missao.AR.find("estadoMissao = ? and dataHoraSaida < ? " + "order by condutor", EstadoMissao.INICIADA, calendar).fetch();
			notificarMissoes(missoes, tituloEmail, tipoNotificacao);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void notificarMissoes(List<Missao> missoes, String titulo, String notificacao) throws Exception {
		Condutor condutor = new Condutor();
		HashMap<Condutor, String> dadosCondutor = new HashMap<Condutor, String>();

		for (Missao item : missoes) {
			condutor = item.getCondutor();
			String sequencia = item.getSequence() + " " + item.getId() + ",";

			if (dadosCondutor.containsKey(condutor)) {
				dadosCondutor.put(condutor, dadosCondutor.get(condutor) + sequencia);
			} else {
				dadosCondutor.put(condutor, sequencia);
			}
		}

		if (dadosCondutor.size() > 0) {
			enviarEmail(titulo, notificacao, dadosCondutor);
		}
	}

	public static String substituirMarcacoesMensagem(String titulo, String notificacao, String lista, Object pessoa) {
		String sexo = "";
		String nome = "";
		String parteMensagem = "";
		Boolean plural = lista.split(",").length > 1 ? true : false;
		String mensagem;

		if (pessoa.getClass().equals(Condutor.class)) {
			sexo = ((Condutor) pessoa).getDpPessoa().getSexo().toUpperCase();
			nome = ((Condutor) pessoa).getNome();

			if (titulo.contains("Missoes")) {
				parteMensagem = plural ? "as miss&otilde;es " : "a miss&atilde;o ";

				if (notificacao.contains("Nao finalizada")) {
					parteMensagem += "abaixo, caso j&aacute; tenha/m sido realizada/s, " + "precisa/m ser finalizada/s.<br>";

				} else if (notificacao.contains("Nao iniciada")) {
					parteMensagem += "abaixo precisa/m ser iniciada/s ou cancelada/s.<br>";
				}
			}
		} else if (pessoa.getClass().equals(DpPessoa.class)) {
			sexo = ((DpPessoa) pessoa).getSexo().toUpperCase();
			nome = ((DpPessoa) pessoa).getNomePessoa();

			if (titulo.contains("Requisicoes")) {
				parteMensagem = plural ? "as requisi&ccedil;&otilde;es " : "a requisi&ccedil;&atilde;o ";

				if (notificacao.contains("Pendente aprovar")) {
					parteMensagem += "abaixo precisa/m ser autorizada/s ou rejeitada/s.<br>";
				}
			}
		}

		mensagem = sexo.equals("F") ? "Prezada Sra. " : "Prezado Sr. " + nome + ", ";
		mensagem += parteMensagem.replaceAll("/s", plural ? "s" : "").replaceAll("/m", plural ? "m" : "");
		return mensagem;
	}

	@SuppressWarnings("unchecked")
	private static void enviarEmail(String titulo, String notificacao, HashMap<?, String> dados) throws Exception {
		String hostName = System.getProperty(Parametro.buscarConfigSistemaEmVigor(CAMINHO_HOSTNAME_STANDALONE));
		final String finalMensagem = "Att.<br>M&oacute;dulo de Transportes do Siga.<br><br>" + "Aten&ccedil;&atilde;o: esta &eacute; uma mensagem autom&aacute;tica. Por favor, n&atilde;o responda.";

		Set<Object> itensKey = (Set<Object>) dados.keySet();

		for (Object item : itensKey) {
			String mensagemAlterada = substituirMarcacoesMensagem(titulo, notificacao, dados.get(item), item);
			String conteudoHTML = "<html>" + mensagemAlterada;
			String[] lista = dados.get(item).split(",");

			for (String itemLista : lista) {
				Boolean primeiraVez = true;
				String sequence = itemLista.substring(0, itemLista.indexOf(" "));
				String id = itemLista.substring(itemLista.indexOf(" ") + 1);
				List<String> parametros = new ArrayList<String>();

				if (titulo.contains("Missoes")) {
					if (notificacao.contains("Nao finalizada")) {
						parametros.add("id," + id + ",sigatp/app/missao/finalizar,Finalizar");
					} else if (notificacao.contains("Nao iniciada")) {
						parametros.add("id," + id + ",sigatp/app/missao/iniciar,Iniciar");
						parametros.add("id," + id + ",sigatp/app/missao/cancelar,Cancelar");
					}
				}

				if (titulo.contains("Requisicoes")) {
					if (notificacao.contains("Pendente aprovar")) {
						parametros.add("id," + id + ",sigatp/app/andamento/autorizar,Autorizar");
						parametros.add("id," + id + ",sigatp/app/andamento/rejeitar,Rejeitar");
					}
				}

				for (String parametro : parametros) {
					String[] itens = parametro.split(",");

					String caminhoUrl = "/" + itens[2] + "/" + itens[1];

					conteudoHTML += (primeiraVez ? "<p>" + sequence : "") + espacosHtml + "<a href='" + hostName + caminhoUrl + "'>" + itens[3] + "</a>" + espacosHtml;
					primeiraVez = false;
				}
			}

			conteudoHTML += "</p>";
			String remetente = SigaBaseProperties.getString("servidor.smtp.usuario.remetente");
			String assunto = titulo;
			String email = "";
			String destinatario[];
			boolean flagEnviarEmailParaLista = Boolean.parseBoolean(Parametro.buscarConfigSistemaEmVigor(CRON_ENVIARPARALISTA));

			if (!flagEnviarEmailParaLista) {
				if (item.getClass().equals(Condutor.class)) {
					email = ((Condutor) item).getDpPessoa().getEmailPessoa();
				} else if (item.getClass().equals(DpPessoa.class)) {
					email = ((DpPessoa) item).getEmailPessoa();
				}
				destinatario = new String[1];
				destinatario[0] = email;
			} else {
				email = Parametro.buscarConfigSistemaEmVigor(CRON_LISTAEMAIL);
				destinatario = email.split(",");
			}

			conteudoHTML += finalMensagem + "</html>";
			String conteudo = FormatarTextoHtml.retirarTagsHtml(conteudoHTML, "");

			Correio.enviar(remetente, destinatario, assunto, conteudo, conteudoHTML);
			SimpleDateFormat fr = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			Logger.getLogger(SIGATP_EMAIL).info(fr.format(calendar.getTime()) + " - Email enviado para " + email + ", assunto: " + assunto);

		}
	}
	
	private void verificarVencimentoCarteira3MesesAntes() {
		List<Condutor> condutores = new ArrayList<Condutor>();
		String tituloEmail = "Aviso antecipado de vencimento de Carteira de motorista";
		String tipoNotificacao = "vencimento de carteira de motorista";		
		try {
			condutores = Condutor.listarTodos();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, +3);
			if (condutores != null && condutores.size() > 0) {
				notificarCondutores(condutores, tituloEmail, tipoNotificacao,calendar);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void notificarCondutores(List<Condutor> condutores, String titulo, String notificacao, Calendar cal) throws Exception {
		Condutor condutor = new Condutor();
		HashMap<Condutor, String> dadosCondutor = new HashMap<Condutor, String>();

		for (Condutor item : condutores) {
			String sequencia = item.getNome() + " " + item.getId() + " ,";
			condutor = item;
			if (((condutor.getDataVencimentoCNH().getTimeInMillis()) > 0)) {
				if (condutor.getDataVencimentoCNH().getTime().compareTo(descartarHora(cal.getTime())) == 0) {
					if (dadosCondutor.containsKey(condutor)) {
						dadosCondutor.put(condutor, dadosCondutor.get(condutor)	+ sequencia);
					} else {
						dadosCondutor.put(condutor, sequencia);
					}
				}
			}
		}
		if (dadosCondutor.size() > 0) {
			enviarEmail(titulo, notificacao, dadosCondutor);
		}
	}
	
	private static Date descartarHora(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
}