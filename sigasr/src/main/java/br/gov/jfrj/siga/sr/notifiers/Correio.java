package br.gov.jfrj.siga.sr.notifiers;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import org.jboss.logging.Logger;

import br.com.caelum.vraptor.freemarker.Freemarker;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.sr.model.SrMovimentacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.model.SrTipoMovimentacao;
import br.gov.jfrj.siga.sr.util.SigaSrProperties;
import br.gov.jfrj.siga.sr.vraptor.CompatibilidadeController;
import freemarker.template.TemplateException;

@Component
@RequestScoped
public class Correio {
	private static final String TEMPLATE_PESQUISA_SATISFACAO = "pesquisaSatisfacao";
	private static final String TEMPLATE_NOTIFICAR_ATENDENTE = "notificarAtendente";
	private static final String TEMPLATE_NOTIFICAR_REPLANEJAMENTO_MOVIMENTACAO = "notificarReplanejamentoMovimentacao";
	private static final String TEMPLATE_NOTIFICAR_CANCELAMENTO_MOVIMENTACAO = "notificarCancelamentoMovimentacao";
	private static final String TEMPLATE_NOTIFICAR_MOVIMENTACAO = "notificarMovimentacao";
	private static final String TEMPLATE_NOTIFICAR_ABERTURA = "notificarAbertura";
	private static final String MOVIMENTACAO_DA_SOLICITACAO = "Movimentação da Solicitação";

	private Freemarker freemarker;
	private PathBuilder pathBuilder;
	
	private static final Logger log = Logger.getLogger(Correio.class);

	public Correio(Freemarker freemarker, PathBuilder urlLogic) {
		this.freemarker = freemarker;
		this.pathBuilder = urlLogic;
	}

	public void notificarAtendente(SrMovimentacao movimentacao, SrSolicitacao sol) {
		
		List<String> recipients = movimentacao.getEmailsNotificacaoAtendende();

		if (!recipients.isEmpty()) {
			String assunto = "";
			if (movimentacao.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO)
		        assunto = "Fechamento de solicitação escalonada a partir de " + sol.getCodigo();
			else
		        assunto = "Solicitação " + sol.getCodigo() + " aguarda atendimento";
			
			String conteudo = getConteudoComSolicitacaoEMovimentacao(TEMPLATE_NOTIFICAR_ATENDENTE, movimentacao, sol);
			enviar(assunto, conteudo, recipients);
		}
	}

	public void notificarReplanejamentoMovimentacao(SrMovimentacao movimentacao) {
		SrSolicitacao solicitacao = movimentacao.getSolicitacao();
		List<String> recipients = movimentacao.getEmailsNotificacaoReplanejamento();

		if (!recipients.isEmpty()) {
			String assunto = MOVIMENTACAO_DA_SOLICITACAO + solicitacao.getCodigo();
			String conteudo = getConteudoComSolicitacaoEMovimentacao(TEMPLATE_NOTIFICAR_REPLANEJAMENTO_MOVIMENTACAO, movimentacao, solicitacao);
			enviar(assunto, conteudo, recipients);
		}
	}

	public void notificarCancelamentoMovimentacao(SrMovimentacao movimentacao) {
		SrSolicitacao solicitacao = movimentacao.getSolicitacao().getSolicitacaoAtual();
		Destinatario destinatario = solicitacao.getDestinatarioEmailNotificacao();

		if (destinatario.possuiEmailCadastrado()) {
			String assunto = MOVIMENTACAO_DA_SOLICITACAO + solicitacao.getCodigo();
			String conteudo = getConteudoComSolicitacaoEMovimentacao(TEMPLATE_NOTIFICAR_CANCELAMENTO_MOVIMENTACAO, movimentacao, solicitacao);
			enviar(assunto, conteudo, destinatario.getEnderecoEmail());
		}
	}

	public void notificarAbertura(SrSolicitacao solicitacao) {
		Destinatario destinatario = solicitacao.getDestinatarioEmailNotificacao();

		if (destinatario.possuiEmailCadastrado()) {
			String assunto = solicitacao.isFilha() ? 
					MessageFormat.format("Escalonamento da solicitação {0}", solicitacao.getSolicitacaoPai().getCodigo()) : 
					MessageFormat.format("Abertura da solicitação {0}",
					solicitacao.getCodigo());

			String conteudo = getConteudoComSolicitacao(TEMPLATE_NOTIFICAR_ABERTURA, solicitacao);
			enviar(assunto, conteudo, destinatario.getEnderecoEmail());
		}
	}

	public void notificarMovimentacao(SrMovimentacao movimentacao) {
		SrSolicitacao solicitacao = movimentacao.getSolicitacao().getSolicitacaoAtual();
		Destinatario destinatario = solicitacao.getDestinatarioEmailNotificacao();

		if (destinatario.possuiEmailCadastrado()) {
			String assunto = MOVIMENTACAO_DA_SOLICITACAO + solicitacao.getCodigo();
			String conteudo = getConteudoComSolicitacaoEMovimentacao(TEMPLATE_NOTIFICAR_MOVIMENTACAO, movimentacao, solicitacao);
			enviar(assunto, conteudo, destinatario.getEnderecoEmail());
		}
	}
	
	private String getConteudoComSolicitacao(String templatePath, SrSolicitacao sol) {
		try {
			return freemarker
					.use(templatePath)
					.with("sol", sol)
					.with("link", link(sol))
					.getContent();
		} catch (IOException | TemplateException e) {
			throw new CorreioException(MessageFormat.format("Erro ao processar template {0}", templatePath), e);
		}
	}
	
	private String getConteudoComSolicitacaoEMovimentacao(String templatePath, SrMovimentacao movimentacao, SrSolicitacao sol) {
		try {
			return freemarker
					.use(templatePath)
					.with("sol", sol)
					.with("movimentacao", movimentacao)
					.with("link", link(sol))
					.getContent();
		} catch (IOException | TemplateException e) {
			throw new CorreioException(MessageFormat.format("Erro ao processar template {0}", templatePath), e);
		}
	}

	private String link(SrSolicitacao solicitacao) {
		String url = SigaSrProperties.getString("url");
		if (url != null)
			return url + (url.endsWith("/") ? "" : "/") + "solicitacao/exibir?id=" + solicitacao.getId();
		log.error("Não foi encontrada a property siga.sr.url");
		try {
			pathBuilder
			.pathToRedirectTo(CompatibilidadeController.class)
			.exibir(null);
			return pathBuilder.getFullPath() + "?id=" + solicitacao.getId();
		} catch (Exception e) {
			throw new CorreioException("Não foi possível obter o endereço a partir do PathBuilder", e);
		} 
	}
	
	private void enviar(String assunto, String conteudo, List<String> destinatarios) {
		enviar(assunto, conteudo, destinatarios.toArray(new String[destinatarios.size()]));
	}

	private void enviar(String assunto, String conteudo, String... destinatarios) {
		CorreioThread t = new CorreioThread(assunto, conteudo, "", destinatarios);
		t.start();
	}
	
	public void pesquisaSatisfacao(SrSolicitacao sol) throws Exception {
		Destinatario destinatario = sol.getDestinatarioEmailNotificacao();

		if (destinatario.possuiEmailCadastrado()) {
			String assunto = MOVIMENTACAO_DA_SOLICITACAO + sol.getCodigo();
			String conteudo = getConteudoComSolicitacao(TEMPLATE_PESQUISA_SATISFACAO, sol);
			enviar(assunto, conteudo, destinatario.getEnderecoEmail());
		}
	}
	
	/**
	 * Classe que representa um thread de envio de e-mail. Há a necessidade do
	 * envio de e-mail ser assíncrono, caso contrário, o usuário sentirá uma
	 * degradação de performance.
	 * 
	 * @author kpf
	 * 
	 */
	static class CorreioThread extends Thread {

		String[] dest;
		String html;
		String txt;
		String assunto;

		public CorreioThread(String assunto, String html, String txt, String... dest) {
			super();
			this.dest = dest;
			this.html = html;
			this.txt = txt;	
			this.assunto = assunto;
		}

		@Override
		public void run() {		
			try{
				br.gov.jfrj.siga.base.Correio.enviar(SigaBaseProperties
						.getString("servidor.smtp.usuario.remetente"),
						dest,		
						assunto, txt, html);					
			} catch (Exception e) {
				log.error(e);
			}
		}
	}
	
}
