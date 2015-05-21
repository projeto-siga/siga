package br.gov.jfrj.siga.gc.vraptor;

import br.com.caelum.vraptor.freemarker.Freemarker;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.view.LinkToHandler;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.gc.model.GcInformacao;

@Component
@RequestScoped
public class Correio {

	private final Freemarker freemarker;
	private LinkToHandler linkTo;

	public Correio(Freemarker freemarker, LinkToHandler linkTo) {
		this.freemarker = freemarker;
		this.linkTo = linkTo;
	}

	public void notificar(GcInformacao informacao, DpPessoa pesResponsavel,
			DpLotacao lotResponsavel, String email) throws Exception {

		String msgDestinatario = "";
		String[] destinatarios = null;

		if (email != null && !email.trim().equals("")) {
			email = email.replace(";", "").replace(",", "");
			destinatarios = email.split("\\s+");
		} else if (pesResponsavel != null) {
			msgDestinatario = pesResponsavel.getNomePessoa();
			destinatarios = new String[] { pesResponsavel.getEmailPessoa() };
		} else if (lotResponsavel != null) {
			msgDestinatario = "a lota&ccedil;&atilde;o "
					+ lotResponsavel.getSigla();
			email = "";
			for (DpPessoa p : lotResponsavel.getDpPessoaLotadosSet()) {
				if (p.getHisDtFim() == null)
					if (email.length() > 0)
						email += " ";
				email += p.getEmailPessoa();
			}
			destinatarios = email.split("\\s+");
		}

		String bodyTXT = freemarker.use("notificar")
				.with("msgDestinatario", msgDestinatario)
				.with("inf", informacao).with("linkTo", linkTo).getContent();

		br.gov.jfrj.siga.base.Correio.enviar(
				"Administrador do Siga<sigadocs@jfrj.jus.br>", destinatarios,
				"Notificando Conhecimento do Siga-GC", bodyTXT);

	}

}
