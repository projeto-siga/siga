package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaFinalizarPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeFinalizar;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaFinalizarPost implements IDocumentosSiglaFinalizarPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Finalizar");

		Ex.getInstance().getComp().afirmar("A elaboração do documento " + mob.getSigla() + " não pode ser finalizado por "
				+ ctx.getTitular().getSiglaCompleta() + "/" + ctx.getLotaTitular().getSiglaCompleta(), ExPodeFinalizar.class, ctx.getTitular(), ctx.getLotaTitular(), mob.doc());

		ctx.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

		Ex.getInstance().getBL().finalizar(ctx.getCadastrante(), ctx.getLotaCadastrante(), mob.doc());

		resp.sigla = mob.doc().getCodigo();
		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Finalizar elaboração";
	}

}
