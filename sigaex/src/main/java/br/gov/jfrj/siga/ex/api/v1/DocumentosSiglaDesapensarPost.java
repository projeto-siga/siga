package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaDesapensarPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaDesapensarPost implements IDocumentosSiglaDesapensarPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Desapensar");

		if (!Ex.getInstance().getComp().podeDesapensar(ctx.getTitular(), ctx.getLotaTitular(), mob)) {
			throw new PresentableUnloggedException("O documento " + mob.getSigla() + " não pode ser desapensado por "
					+ ctx.getTitular().getSiglaCompleta() + "/" + ctx.getLotaTitular().getSiglaCompleta());
		}

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
