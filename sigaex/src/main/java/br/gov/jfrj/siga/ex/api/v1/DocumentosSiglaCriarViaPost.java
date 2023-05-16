package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaCriarViaPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeCriarVia;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaCriarViaPost implements IDocumentosSiglaCriarViaPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Criar Via");

		Ex.getInstance().getComp().afirmar("Não é possível criar vias neste documento", ExPodeCriarVia.class, ctx.getTitular(), ctx.getLotaTitular(), mob);
		
		ctx.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

		Ex.getInstance().getBL().criarVia(ctx.getCadastrante(), ctx.getLotaCadastrante(),
				ctx.getTitular(), ctx.getLotaTitular(), mob.doc());

		resp.sigla = mob.doc().getUltimaVia().getCodigoCompacto();
		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Criar via";
	}

}
