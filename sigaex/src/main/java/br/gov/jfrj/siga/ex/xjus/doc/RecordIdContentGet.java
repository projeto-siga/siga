package br.gov.jfrj.siga.ex.xjus.doc;

import br.gov.jfrj.siga.base.HtmlToPlainText;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExAcesso;
import br.gov.jfrj.siga.ex.util.MascaraUtil;
import br.gov.jfrj.siga.ex.util.PdfToPlainText;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Facet;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Field;
import br.jus.trf2.xjus.record.api.XjusRecordAPIContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.crivano.swaggerservlet.PresentableUnloggedException;

@AcessoPublico
public class RecordIdContentGet implements IXjusRecordAPI.IRecordIdContentGet {

	@Override
	public void run(Request req, Response resp, XjusRecordAPIContext ctx) throws Exception {
		try {
			long primaryKey;
			try {
				primaryKey = Long.valueOf(req.id);
			} catch (NumberFormatException nfe) {
				throw new PresentableUnloggedException("REMOVED");
			}
			ExDocumento doc = ExDao.getInstance().consultar(primaryKey, ExDocumento.class, false);

			if (doc == null || doc.isCancelado()) {
				throw new PresentableUnloggedException("REMOVED");
			}

			resp.id = req.id;

			String html = doc.getHtml();
			if (html != null) {
				resp.content = HtmlToPlainText.getText(html).trim();
				return;
			}

			byte pdf[] = doc.getPdf();
			if (pdf != null) {
				resp.content = PdfToPlainText.getText(pdf);
				return;
			}
		} finally {
			ExDao.freeInstance();
		}

	}

	public String getContext() {
		return "obter a lista de índices";
	}
}
