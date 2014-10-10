package notifiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.GcInformacao;
import play.mvc.Mailer;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class Correio extends Mailer {

	public static void notificar(GcInformacao inf, DpPessoa pessoa,
			DpLotacao lota, String email) {
		setSubject("Notificando Conhecimento do Siga-GC");
		String msgDestinatario = "";
		if (email != null && !email.trim().equals("")) {
			email = email.replace(";", "").replace(",", "");
			addRecipient(email.split("\\s+"));
		} else if (pessoa != null) {
			msgDestinatario = pessoa.getNomePessoa();
			addRecipient(pessoa.getEmailPessoa());
		} else if (lota != null) {
			msgDestinatario = "a lota&ccedil;&atilde;o " + lota.getSigla();
			for (DpPessoa p : lota.getDpPessoaLotadosSet()) {
				if (p.getHisDtFim() == null)
					addRecipient(p.getEmailPessoa());
			}
		}
		setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
		send(inf, msgDestinatario);
	}
}
