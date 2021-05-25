package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaExcluirPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaExcluirPost implements IDocumentosSiglaExcluirPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Excluir");

		if (!Ex.getInstance().getComp().podeExcluir(ctx.getTitular(), ctx.getLotaTitular(), mob)) {
			throw new PresentableUnloggedException(
					"Exclusão do documento " + mob.getSigla() + " não pode ser realizada por "
							+ ctx.getTitular().getSiglaCompleta() + "/" + ctx.getLotaTitular().getSiglaCompleta());
		}

		ctx.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

		Ex.getInstance().getBL().excluirDocumento(mob.doc(), ctx.getCadastrante(), ctx.getLotaCadastrante(), false);

		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Excluir documento";
	}

}
