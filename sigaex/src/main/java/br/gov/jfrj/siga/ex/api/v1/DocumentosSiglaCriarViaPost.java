package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaCriarViaPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaCriarViaPost implements IDocumentosSiglaCriarViaPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Criar Via");

		if (!Ex.getInstance().getComp().podeCriarVia(ctx.getTitular(), ctx.getLotaTitular(), mob)) {
			throw new PresentableUnloggedException(
					"O documento " + mob.getSigla() + " não pode ser arquivado no corrente por "
							+ ctx.getLotaTitular().getSiglaCompleta() + "/" + ctx.getLotaTitular().getSiglaCompleta());
		}

		ctx.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

		Ex.getInstance().getBL().criarVia(ctx.getTitular(), ctx.getLotaTitular(), mob.doc());

		resp.sigla = mob.doc().getUltimaVia().getCodigoCompacto();
		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Criar via";
	}

}
