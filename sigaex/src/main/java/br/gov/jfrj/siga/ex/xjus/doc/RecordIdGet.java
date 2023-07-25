package br.gov.jfrj.siga.ex.xjus.doc;

import java.util.Date;
import java.util.Map;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.HtmlToPlainText;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.cp.util.XjusUtils;
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
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.IRecordIdGet.Response;
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
            ExDocumento doc = ExDao.getInstance().consultar(primaryKey, ExDocumento.class, false);

            if (doc == null || doc.isCancelado() || doc.isSemEfeito()) {
                removed(resp);
                return;
            }

            resp.id = req.id;
            resp.url = Prop.get("/xjus.permalink.url") + doc.getCodigoCompacto();
            resp.acl = "PUBLIC";
            resp.refresh = "NEVER";
            resp.code = doc.getCodigo();
            resp.title = doc.getDescrDocumento();
            String dtDocYYYYMMDD = doc.getDtDocYYYYMMDD();
            if (dtDocYYYYMMDD != null && !dtDocYYYYMMDD.isEmpty())
                resp.dateref = dtDocYYYYMMDD;

            // resp.setLastModified(doc.getDtFinalizacao());

            addMetadataForDoc(doc, resp);
            addAclForDoc(doc, resp);

            String html = doc.getHtml();
            if (html != null) {
                resp.content = HtmlToPlainText.getText(html);
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

    public static void addAclForDoc(ExDocumento doc, Response resp) {
        if (doc.getDnmAcesso() == null || doc.isDnmAcessoMAisAntigoQueODosPais()) {
            Ex.getInstance().getBL().atualizarDnmAcesso(doc);
        }
        String sAcessos = doc.getDnmAcesso();
        if (sAcessos == null) {
            Date dt = ExDao.getInstance().dt();
            ExAcesso acesso = new ExAcesso();
            sAcessos = acesso.getAcessosString(doc, dt);
            if (sAcessos == null || sAcessos.trim().length() == 0)
                throw new RuntimeException("Não foi possível calcular os acesos de " + doc.getSigla());
        }
        resp.acl = sAcessos.replace(XjusUtils.ACESSO_PUBLICO, "PUBLIC");
    }

    private void addMetadataForDoc(ExDocumento doc, Response resp) {
        addFacet(resp, "tipo", "Documento");
        addFieldAndFacet(resp, "orgao", doc.getOrgaoUsuario().getAcronimoOrgaoUsu());
        addField(resp, "codigo", doc.getCodigo());
        if (doc.getExTipoDocumento() != null) {
            addFieldAndFacet(resp, "origem", doc.getExTipoDocumento().getSigla());
        }
        if (doc.getExFormaDocumento() != null)
            addFieldAndFacet(resp, "especie", doc.getExFormaDocumento().getDescricao());
        if (doc.getExModelo() != null)
            addFieldAndFacet(resp, "modelo", doc.getExModelo().getNmMod());
        if (doc.getDnmExNivelAcesso() != null)
            addField(resp, "acesso", doc.getDnmExNivelAcesso().getNmNivelAcesso());

        String dtDocYYYYMMDD = doc.getDtDocYYYYMMDD();
        if (dtDocYYYYMMDD != null && !dtDocYYYYMMDD.isEmpty()) {
            addField(resp, "data", dtDocYYYYMMDD);
            addFacet(resp, "ano", dtDocYYYYMMDD.substring(0, 4));
            addFacet(resp, "mes", dtDocYYYYMMDD.substring(5, 7));
        }

        ExClassificacao cAtual = doc.getExClassificacaoAtual();
        if (cAtual == null && doc.getExClassificacao() != null)
            cAtual = doc.getExClassificacao();
        if (cAtual != null) {
            String[] pais = MascaraUtil.getInstance().getPais(cAtual.getCodificacao());
            if (pais != null) {
                for (String sigla : pais) {
                    ExClassificacao c = new ExClassificacao();
                    c.setSigla(sigla);
                    ExClassificacao cPai = ExDao.getInstance().consultarPorSigla(c);
                    if (cPai != null) {
                        addField(resp, "classificacao_" + MascaraUtil.getInstance().calcularNivel(c.getCodificacao()),
                                cPai.getDescrClassificacao());
                    }
                }
            }

            addField(resp, "classificacao_" + MascaraUtil.getInstance().calcularNivel(cAtual.getCodificacao()),
                    cAtual.getDescricao());
        }

        if (doc.getLotaSubscritor() != null) {
            addFieldAndFacet(resp, "subscritor_lotacao", doc.getLotaSubscritor().getSiglaLotacao());
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
