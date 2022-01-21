package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaExcluirPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeExcluir;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaExcluirPost implements IDocumentosSiglaExcluirPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Excluir");

		Ex.getInstance().getComp().afirmar("Exclusão do documento " + mob.getSigla() + " não pode ser realizada por "
				+ ctx.getTitular().getSiglaCompleta() + "/" + ctx.getLotaTitular().getSiglaCompleta(), ExPodeExcluir.class, ctx.getTitular(), ctx.getLotaTitular(), mob);

		ctx.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

		Ex.getInstance().getBL().excluirDocumento(mob.doc(), ctx.getCadastrante(), ctx.getLotaCadastrante(), false);

		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Excluir documento";
	}

}
