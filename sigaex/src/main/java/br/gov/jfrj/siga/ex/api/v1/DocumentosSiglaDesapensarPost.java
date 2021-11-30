package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaDesapensarPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeDesapensar;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaDesapensarPost implements IDocumentosSiglaDesapensarPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Desapensar");

		Ex.getInstance().getComp().afirmar(
				"O documento " + mob.getSigla() + " não pode ser desapensado por " + ctx.getTitular().getSiglaCompleta()
						+ "/" + ctx.getLotaTitular().getSiglaCompleta(),
				ExPodeDesapensar.class, ctx.getTitular(), ctx.getLotaTitular(), mob);

		ctx.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

		Ex.getInstance().getBL().desapensarDocumento(ctx.getCadastrante(), ctx.getLotaCadastrante(), mob, null, null,
				ctx.getTitular());

		resp.sigla = mob.doc().getCodigo();
		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Arquivar no Corrente";
	}

}
