package br.gov.jfrj.siga.gc.vraptor;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.freemarker.Freemarker;
import br.com.caelum.vraptor.view.LinkToHandler;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.gc.model.GcInformacao;

@RequestScoped
public class Correio {

	private final Freemarker freemarker;
	private LinkToHandler linkTo;

	/**
	 * @deprecated CDI eyes only
	 */
	public Correio() {
		super();
		this.freemarker = null;
		this.linkTo = null;
	}
	
	@Inject
	public Correio(Freemarker freemarker, LinkToHandler linkTo) {
		this.freemarker = freemarker;
		this.linkTo = linkTo;
	}

	public void notificar(GcInformacao informacao, DpPessoa pesResponsavel,
			DpLotacao lotResponsavel, String email) throws Exception {
		if(lotResponsavel != null && lotResponsavel.getId() != null)
			lotResponsavel = DpLotacao.AR.findById(lotResponsavel.getId());		
		String msgDestinatario = "";
		String[] destinatarios = null;

		if (email != null && !email.trim().equals("")) {
			email = email.replace(";", "").replace(",", "");
			destinatarios = email.split("\\s+");
		} else if (pesResponsavel != null) {
			msgDestinatario = pesResponsavel.getNomePessoa();
			destinatarios = new String[] { pesResponsavel.getEmailPessoa() };
		} else if (lotResponsavel != null) {
			msgDestinatario = "a lotação "
					+ lotResponsavel.getSigla();
			email = "";
			for (DpPessoa p : lotResponsavel.getDpPessoaLotadosSet()) {
				if (p.getHisDtFim() == null && p.getEmailPessoaAtual() != null){
					if (email.length() > 0){
						email += " ";
					}
						email += p.getEmailPessoa();
				}
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
