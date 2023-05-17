package br.gov.jfrj.siga.gc.xjus;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.base.HtmlToPlainText;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.util.XjusUtils;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.gc.model.GcAcesso;
import br.gov.jfrj.siga.gc.model.GcInformacao;
import br.gov.jfrj.siga.gc.model.GcTag;
import br.gov.jfrj.siga.gc.model.GcTipoTag;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Facet;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Field;
import br.jus.trf2.xjus.record.api.XjusRecordAPIContext;

public class RecordIdGet implements IXjusRecordAPI.IRecordIdGet {

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
		resp.url = Prop.get("/xjus.permalink.url") + inf.getSiglaCompacta();
		resp.acl = "PUBLIC";
		resp.refresh = "NEVER";
		resp.code = inf.getSigla();
		resp.title = inf.getArq().getTitulo();
		resp.field = new ArrayList<>();
		resp.facet = new ArrayList<>();
		resp.refresh = "NEVER";
		// resp.setLastModified(doc.getDtFinalizacao());

		addMetadataForDoc(inf, resp);
		addAclForDoc(inf, resp);

		String html = inf.getConteudoHTML();
		if (html != null) {
			resp.content = HtmlToPlainText.getText(html).trim();
			return;
		}
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

	public static void addAclForDoc(GcInformacao inf, Response resp) {
		Date dt = CpDao.getInstance().dt();
		GcAcesso acesso = new GcAcesso();
		Set<Object> acessos = acesso.calcularAcessos(inf);
		String sAcessos = XjusUtils.getAcessosString(acessos, dt, true, true);
		if (sAcessos == null || sAcessos.trim().length() == 0)
			throw new RuntimeException("Não foi possível calcular os acesos de " + inf.getSigla());
		resp.acl = sAcessos.replace(XjusUtils.ACESSO_PUBLICO, "PUBLIC");
	}

	private void addMetadataForDoc(GcInformacao inf, Response resp) {
		addFacet(resp, "tipo", "Informação");
		addFieldAndFacet(resp, "orgao", inf.getOu().getAcronimoOrgaoUsu());
		addField(resp, "codigo", inf.getSigla());
//		if (inf.getTipo() != null) 
//			addFieldAndFacet(resp, "origem", inf.getTipo().getNome());
		if (inf.getTipo() != null)
			addFieldAndFacet(resp, "especie", inf.getTipo().getNome());
//		if (inf.getExModelo() != null)
//			addFieldAndFacet(resp, "modelo", inf.getExModelo().getNmMod());
//		if (inf.getDnmExNivelAcesso() != null)
//			addField(resp, "acesso", inf.getDnmExNivelAcesso().getNmNivelAcesso());
		String yyyymmdd = inf.getDtYYYYMMDD();
		if (yyyymmdd != null) {
			addField(resp, "data", yyyymmdd);
			addFacet(resp, "ano", yyyymmdd.substring(0, 4));
			addFacet(resp, "mes", yyyymmdd.substring(5, 7));
		}

//		ExClassificacao cAtual = inf.getExClassificacaoAtual();
//		if (cAtual == null && inf.getExClassificacao() != null)
//			cAtual = inf.getExClassificacao();
//		if (cAtual != null) {
//			String[] pais = MascaraUtil.getInstance().getPais(cAtual.getCodificacao());
//			if (pais != null) {
//				for (String sigla : pais) {
//					ExClassificacao c = new ExClassificacao();
//					c.setSigla(sigla);
//					ExClassificacao cPai = ExDao.getInstance().consultarPorSigla(c);
//					if (cPai != null) {
//						addField(resp, "classificacao_" + MascaraUtil.getInstance().calcularNivel(c.getCodificacao()),
//								cPai.getDescrClassificacao());
//					}
//				}
//			}
//
//			addField(resp, "classificacao_" + MascaraUtil.getInstance().calcularNivel(cAtual.getCodificacao()),
//					cAtual.getDescricao());
//		}

		if (inf.getLotacao() != null) {
			addFieldAndFacet(resp, "subscritor_lotacao", inf.getLotacao().getSiglaLotacao());
		}
		if (inf.getAutor() != null) {
			addField(resp, "subscritor", inf.getAutor().getNomePessoa());
		}
//		if (doc.getLotaCadastrante() != null) {
//			addField(resp, "cadastrante_lotacao", doc.getLotaCadastrante()
//					.getSiglaLotacao());
//		}
//		if (doc.getCadastrante() != null) {
//			addField(resp, "cadastrante", doc.getCadastrante().getNomePessoa());
//		}

		for (GcTag tag : inf.getTags()) {
			if (tag.getTipo().getId().equals(GcTipoTag.TIPO_TAG_CLASSIFICACAO)
					|| tag.getTipo().getId().equals(GcTipoTag.TIPO_TAG_HASHTAG))
				addFacet(resp, "etiqueta", tag.getTitulo());
		}
	}

	public String getContext() {
		return "obter a lista de índices";
	}
}
