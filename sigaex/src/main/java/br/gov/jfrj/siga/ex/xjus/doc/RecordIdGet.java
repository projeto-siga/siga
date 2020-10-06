package br.gov.jfrj.siga.ex.xjus.doc;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import br.gov.jfrj.siga.base.HtmlToPlainText;
import br.gov.jfrj.siga.base.Prop;
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
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.RecordIdGetRequest;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.RecordIdGetResponse;

public class RecordIdGet implements IXjusRecordAPI.IRecordIdGet {

	@Override
	public void run(RecordIdGetRequest req, RecordIdGetResponse resp)
			throws Exception {
		try {
			long primaryKey;
			try {
				primaryKey = Long.valueOf(req.id);
			} catch (NumberFormatException nfe) {
				throw new RuntimeException("REMOVED");
			}
			ExDocumento doc = ExDao.getInstance().consultar(primaryKey,
					ExDocumento.class, false);

			if (doc == null || doc.isCancelado()) {
				throw new RuntimeException("REMOVED");
			}

			resp.id = req.id;
			resp.url = Prop.get("/xjus.permalink.url")
					+ doc.getCodigoCompacto();
			resp.acl = "PUBLIC";
			resp.refresh = "NEVER";
			resp.code = doc.getCodigo();
			resp.title = doc.getDescrDocumento();
			resp.field = new ArrayList<>();
			resp.facet = new ArrayList<>();
			resp.refresh = "NEVER";
			// resp.setLastModified(doc.getDtFinalizacao());

			addMetadataForDoc(doc, resp);
			addAclForDoc(doc, resp);

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

	public void addField(RecordIdGetResponse resp, String name, String value) {
		Field fld = new Field();
		fld.kind = "TEXT";
		fld.name = name;
		fld.value = value;
		resp.field.add(fld);
	}

	public void addFacet(RecordIdGetResponse resp, String name, String value) {
		Facet facet = new Facet();
		facet.kind = "KEYWORD";
		facet.name = name;
		facet.value = value;
		resp.facet.add(facet);
	}

	public void addFieldAndFacet(RecordIdGetResponse resp, String name,
			String value) {
		addField(resp, name, value);
		addFacet(resp, name, value);
	}

	public static void addAclForDoc(ExDocumento doc, RecordIdGetResponse resp) {
		if (doc.getDnmAcesso() == null
				|| doc.isDnmAcessoMAisAntigoQueODosPais()) {
			Ex.getInstance().getBL().atualizarDnmAcesso(doc);
		}
		String sAcessos = doc.getDnmAcesso();
		if (sAcessos == null) {
			Date dt = ExDao.getInstance().dt();
			ExAcesso acesso = new ExAcesso();
			sAcessos = acesso.getAcessosString(doc, dt);
			if (sAcessos == null || sAcessos.trim().length() == 0)
				throw new RuntimeException(
						"Não foi possível calcular os acesos de "
								+ doc.getSigla());
		}
		resp.acl = sAcessos.replace(ExAcesso.ACESSO_PUBLICO, "PUBLIC");
	}

	private void addMetadataForDoc(ExDocumento doc, RecordIdGetResponse resp) {
		addFacet(resp, "tipo", "Documento");
		addFieldAndFacet(resp, "orgao", doc.getOrgaoUsuario()
				.getAcronimoOrgaoUsu());
		addField(resp, "codigo", doc.getCodigo());
		if (doc.getExTipoDocumento() != null) {
			addFieldAndFacet(resp, "origem", doc.getExTipoDocumento()
					.getSigla());
		}
		if (doc.getExFormaDocumento() != null)
			addFieldAndFacet(resp, "especie", doc.getExFormaDocumento()
					.getDescricao());
		if (doc.getExModelo() != null)
			addFieldAndFacet(resp, "modelo", doc.getExModelo().getNmMod());
		if (doc.getDnmExNivelAcesso() != null)
			addField(resp, "acesso", doc.getDnmExNivelAcesso()
					.getNmNivelAcesso());
		if (doc.getDtDocYYYYMMDD() != null) {
			addField(resp, "data", doc.getDtDocYYYYMMDD());
			addFacet(resp, "ano", doc.getDtDocYYYYMMDD().substring(0, 4));
			addFacet(resp, "mes", doc.getDtDocYYYYMMDD().substring(5, 7));
		}

		ExClassificacao cAtual = doc.getExClassificacaoAtual();
		if (cAtual == null && doc.getExClassificacao() != null)
			cAtual = doc.getExClassificacao();
		if (cAtual != null) {
			String[] pais = MascaraUtil.getInstance().getPais(
					cAtual.getCodificacao());
			if (pais != null) {
				for (String sigla : pais) {
					ExClassificacao c = new ExClassificacao();
					c.setSigla(sigla);
					ExClassificacao cPai = ExDao.getInstance()
							.consultarPorSigla(c);
					if (cPai != null) {
						addField(
								resp,
								"classificacao_"
										+ MascaraUtil.getInstance()
												.calcularNivel(
														c.getCodificacao()),
								cPai.getDescrClassificacao());
					}
				}
			}

			addField(
					resp,
					"classificacao_"
							+ MascaraUtil.getInstance().calcularNivel(
									cAtual.getCodificacao()),
					cAtual.getDescricao());
		}

		if (doc.getLotaSubscritor() != null) {
			addFieldAndFacet(resp, "subscritor_lotacao", doc
					.getLotaSubscritor().getSiglaLotacao());
		}
		if (doc.getSubscritor() != null) {
			addField(resp, "subscritor", doc.getSubscritor().getNomePessoa());
		}
//		if (doc.getLotaCadastrante() != null) {
//			addField(resp, "cadastrante_lotacao", doc.getLotaCadastrante()
//					.getSiglaLotacao());
//		}
//		if (doc.getCadastrante() != null) {
//			addField(resp, "cadastrante", doc.getCadastrante().getNomePessoa());
//		}

		Map<String, String> map = doc.getResumo();
		if (map != null)
			for (String s : map.keySet()) {
				addField(resp, s, map.get(s));
			}
	}

	public String getContext() {
		return "obter a lista de índices";
	}
}
