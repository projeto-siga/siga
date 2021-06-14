package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaDesentranharPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaDesentranharPost implements IDocumentosSiglaDesentranharPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Desentranhar");

		if (!Ex.getInstance().getComp().podeCancelarJuntada(ctx.getTitular(), ctx.getLotaTitular(), mob)) {
			throw new PresentableUnloggedException("O documento " + mob.getSigla() + " não pode ser desentranhado por "
					+ ctx.getTitular().getSiglaCompleta() + "/" + ctx.getLotaTitular().getSiglaCompleta());
		}

		ctx.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

		Ex.getInstance().getBL().cancelarJuntada(ctx.getCadastrante(), ctx.getLotaCadastrante(), mob,
				ExDao.getInstance().getServerDateTime(), ctx.getTitular(), ctx.getTitular(), req.motivo);

		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Arquivar no Corrente";
	}

}
