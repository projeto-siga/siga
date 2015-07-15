package br.gov.jfrj.siga.sr.notifiers;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import br.com.caelum.vraptor.freemarker.Freemarker;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.sr.model.SrMovimentacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.model.SrTipoMovimentacao;
import br.gov.jfrj.siga.sr.vraptor.SolicitacaoController;
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
	private static final String MOVIMENTACAO_DA_SOLICITACAO = "Movimentação da solicitação ";

	private Freemarker freemarker;
	private PathBuilder pathBuilder;

	public Correio(Freemarker freemarker, PathBuilder urlLogic) {
		this.freemarker = freemarker;
		this.pathBuilder = urlLogic;
	}

	private String remetentePadrao() {
		return SigaBaseProperties.getString("servidor.smtp.usuario.remetente");
	}

	public void notificarAtendente(SrMovimentacao movimentacao, SrSolicitacao sol) {
		
		List<String> recipients = movimentacao.getEmailsNotificacaoAtendende();

		if (!recipients.isEmpty()) {
			String assunto = "";
			if (movimentacao.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO)
		        assunto = "Fechamento de solicita��o escalonada a partir de " + sol.getCodigo();
			else
		        assunto = "Solicita��o " + sol.getCodigo() + " aguarda atendimento";
			
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
			throw novaCorreioException(MessageFormat.format("Erro ao processar template {0}", templatePath), e);
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
			throw novaCorreioException(MessageFormat.format("Erro ao processar template {0}", templatePath), e);
		}
	}

	private String link(SrSolicitacao solicitacao) {
		try {
			pathBuilder
				.pathToRedirectTo(SolicitacaoController.class)
				.exibir(solicitacao.getId(), Boolean.TRUE, Boolean.TRUE);
			
			return pathBuilder.getFullPath();
		} catch (Exception e) {
			throw novaCorreioException("Erro ao processar link na geracao de email", e);
		}
	}
	
	private void enviar(String assunto, String conteudo, String... destinatarios) {
		try {
			br.gov.jfrj.siga.base.Correio.enviar(remetentePadrao(), destinatarios, assunto, new String(), conteudo);
		} catch (Exception e) {
			throw novaCorreioException("Erro ao enviar email", e);
		}
	}

	private void enviar(String assunto, String conteudo, List<String> destinatarios) {
		try {
			br.gov.jfrj.siga.base.Correio.enviar(remetentePadrao(), destinatarios.toArray(new String[destinatarios.size()]), assunto, new String(), conteudo);
		} catch (Exception e) {
			throw novaCorreioException("Erro ao enviar email", e);
		}
	}
	
	private CorreioException novaCorreioException(String message, Exception e)  {
		e.printStackTrace();
		return new CorreioException(message, e);
	}
	
	public void pesquisaSatisfacao(SrSolicitacao sol) throws Exception {
		Destinatario destinatario = sol.getDestinatarioEmailNotificacao();

		if (destinatario.possuiEmailCadastrado()) {
			String assunto = MOVIMENTACAO_DA_SOLICITACAO + sol.getCodigo();
			String conteudo = getConteudoComSolicitacao(TEMPLATE_PESQUISA_SATISFACAO, sol);
			enviar(assunto, conteudo, destinatario.getEnderecoEmail());
		}
	}
}
