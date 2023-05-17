package br.gov.jfrj.siga.gc.xjus;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.base.HtmlToPlainText;
import br.gov.jfrj.siga.gc.model.GcInformacao;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;
import br.jus.trf2.xjus.record.api.XjusRecordAPIContext;

public class RecordIdContentGet implements IXjusRecordAPI.IRecordIdContentGet {

	@Override
	public void run(Request req, Response resp, XjusRecordAPIContext ctx) throws Exception {
		long primaryKey;
		try {
			primaryKey = Long.valueOf(req.id);
		} catch (NumberFormatException nfe) {
			throw new PresentableUnloggedException("REMOVED");
		}

		GcInformacao inf = GcInformacao.AR.findById(primaryKey);

		if (inf == null || inf.isCancelado()) {
			throw new PresentableUnloggedException("REMOVED");
		}

		resp.id = req.id;

		String html = inf.getConteudoHTML();
		if (html != null) {
			resp.content = HtmlToPlainText.getText(html).trim();
			return;
		}
	}

	public String getContext() {
		return "obter a lista de índices";
	}
}
