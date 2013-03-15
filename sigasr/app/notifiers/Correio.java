package notifiers;

import models.SrAndamento;
import models.SrSolicitacao;

import org.apache.commons.mail.HtmlEmail;

import play.mvc.Mailer;

public class Correio extends Mailer {

	public static void notificarAndamento(SrAndamento andamento) {
		SrSolicitacao sol = andamento.solicitacao.getSolicitacaoAtual();
		setSubject("Andamento da solicitação " + sol.getCodigo());
		addRecipient(sol.solicitante.getEmailPessoa());
		setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
		send(andamento, sol);
	}
	
	public static void notificarCancelamentoAndamento(SrAndamento andamento) {
		SrSolicitacao sol = andamento.solicitacao.getSolicitacaoAtual();
		setSubject("Andamento da solicitação " + sol.getCodigo());
		addRecipient(sol.solicitante.getEmailPessoa());
		setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
		send(andamento, sol);
	}
	
	public static void notificarAbertura(SrSolicitacao sol) {
		setSubject("Abertura da solicitação " + sol.getCodigo());
		addRecipient(sol.solicitante.getEmailPessoa());
		setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
		send(sol);
	}
}
