package br.gov.jfrj.siga.ex.api.v1;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.AcessoItem;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaAcessosDisponiveisGet;
import br.gov.jfrj.siga.ex.util.NivelDeAcessoUtil;

public class DocumentosSiglaAcessosDisponiveisGet implements IDocumentosSiglaAcessosDisponiveisGet {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Redefinir Acesso");

		ExDocumento doc = mob.doc();
		List<ExNivelAcesso> acessos = NivelDeAcessoUtil.getListaNivelAcesso(doc.getExTipoDocumento(),
				doc.getExFormaDocumento(), doc.getExModelo(), doc.getExClassificacaoAtual(), ctx.getTitular(),
				ctx.getLotaTitular());

		resp.list = new ArrayList<>();
		for (ExNivelAcesso m : acessos) {
			AcessoItem mr = new AcessoItem();
			mr.idAcesso = m.getIdNivelAcesso().toString();
			mr.nome = m.getNmNivelAcesso();
			resp.list.add(mr);
		}
	}

	@Override
	public String getContext() {
		return "obter lista de perfis ";
	}

}