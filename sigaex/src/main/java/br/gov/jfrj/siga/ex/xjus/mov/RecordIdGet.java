package br.gov.jfrj.siga.ex.xjus.mov;

import java.util.ArrayList;
import java.util.Date;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.HtmlToPlainText;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.ex.util.PdfToPlainText;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Facet;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Field;
import br.jus.trf2.xjus.record.api.XjusRecordAPIContext;

@AcessoPublico
public class RecordIdGet implements IXjusRecordAPI.IRecordIdGet {

	@Override
	public void run(Request req, Response resp, XjusRecordAPIContext ctx) throws Exception {
		try {
			long primaryKey;
			try {
				primaryKey = Long.valueOf(req.id);
			} catch (NumberFormatException nfe) {
			    removed(resp);
				return;
			}
			ExMovimentacao mov = ExDao.getInstance().consultar(primaryKey, ExMovimentacao.class, false);

			if (mov == null || mov.isCancelada()) {
                removed(resp);
                return;
			}

			ExDocumento doc = mov.getExDocumento();

            if (doc == null || doc.isCancelado() || doc.isSemEfeito()) {
                removed(resp);
                return;
            }

			Date dt = doc.getDtFinalizacao();
			if (dt == null || dt.before(mov.getDtIniMov()))
				dt = mov.getDtIniMov();

			resp.id = req.id;
			resp.url = Prop.get("/xjus.permalink.url") + doc.getCodigoCompacto() + "/" + mov.getIdMov();
			resp.acl = "PUBLIC";
			resp.refresh = "NEVER";
			resp.code = doc.getCodigo();
			resp.title = doc.getDescrDocumento();
            String dtMovYYYYMMDD = mov.getDtMovYYYYMMDD();
            if (dtMovYYYYMMDD != null && !dtMovYYYYMMDD.isEmpty())
                resp.dateref = dtMovYYYYMMDD;
			resp.field = new ArrayList<>();
			resp.facet = new ArrayList<>();
			resp.refresh = "NEVER";
			// resp.setLastModified(doc.getDtFinalizacao());

			addMetadataForMov(doc, mov, resp);
			br.gov.jfrj.siga.ex.xjus.doc.RecordIdGet.addAclForDoc(doc, resp);

			String html = mov.getHtml();
			if (html != null) {
				resp.content = HtmlToPlainText.getText(html).trim();
				return;
			}

			byte pdf[] = mov.getPdf();
			if (pdf != null) {
				resp.content = PdfToPlainText.getText(pdf);
				return;
			}
		} finally {
			ExDao.freeInstance();
		}

	}

    private void removed(Response resp) {
        resp.status = "REMOVED";
        CurrentRequest.get().getResponse().setStatus(206);
    }

	public void addField(Response resp, String name, String value) {
		Field fld = new Field();
		fld.kind = "TEXT";
		fld.name = name;
		fld.value = value;
		resp.field.add(fld);
	}

	public void addFacet(Response resp, String name, String value) {
		Facet facet = new Facet();
		facet.kind = "KEYWORD";
		facet.name = name;
		facet.value = value;
		resp.facet.add(facet);
	}

	public void addFieldAndFacet(Response resp, String name, String value) {
		addField(resp, name, value);
		addFacet(resp, name, value);
	}

	private void addMetadataForMov(ExDocumento doc, ExMovimentacao mov, Response resp) {
		addFacet(resp, "tipo", "Documento");
		addFieldAndFacet(resp, "orgao", doc.getOrgaoUsuario().getAcronimoOrgaoUsu());
		addField(resp, "codigo", doc.getCodigo() + ":" + mov.getIdMov());
		if (doc.getExTipoDocumento() != null) {
			addFieldAndFacet(resp, "origem",
					mov.getExTipoMovimentacao().equals(ExTipoDeMovimentacao.ANEXACAO) ? "Anexo"
							: "Despacho Curto");

		}
		if (doc.getDnmExNivelAcesso() != null)
			addField(resp, "acesso", doc.getDnmExNivelAcesso().getNmNivelAcesso());
		if (mov.getDtMovYYYYMMDD() != null) {
			addField(resp, "data", mov.getDtMovYYYYMMDD());
			addFacet(resp, "ano", mov.getDtMovYYYYMMDD().substring(0, 4));
			addFacet(resp, "mes", mov.getDtMovYYYYMMDD().substring(5, 7));
		}
		if (mov.getLotaSubscritor() != null)
			addFieldAndFacet(resp, "subscritor_lotacao", mov.getLotaSubscritor().getSiglaLotacao());
		if (mov.getSubscritor() != null)
			addField(resp, "subscritor", mov.getSubscritor().getNomePessoa());

//		if (mov.getLotaCadastrante() != null)
//			addFieldAndFacet(resp, "cadastrante_lotacao", mov
//					.getLotaCadastrante().getSiglaLotacao());
//		if (mov.getCadastrante() != null)
//			addField(resp, "cadastrante", mov.getCadastrante().getNomePessoa());
	}

	public String getContext() {
		return "obter a lista de índices";
	}
}
