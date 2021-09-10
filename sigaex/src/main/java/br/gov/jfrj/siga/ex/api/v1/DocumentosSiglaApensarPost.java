package br.gov.jfrj.siga.ex.api.v1;

import java.util.Date;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaApensarPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaApensarPost implements IDocumentosSiglaApensarPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mobFilho = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento Secundário");
		ExMobil mobPai = ctx.buscarEValidarMobil(req.siglamestre, req, resp, "Documento Mestre");

		ctx.assertAcesso(mobFilho, ctx.getCadastrante(), ctx.getLotaTitular());

		Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();

		if (!Ex.getInstance().getComp().podeApensar(ctx.getCadastrante(), ctx.getLotaTitular(), mobFilho)) {
			throw new AplicacaoException("Não é possível fazer apensação");
		}

		Ex.getInstance().getBL().apensarDocumento(ctx.getCadastrante(), ctx.getTitular(), ctx.getLotaCadastrante(),
				mobFilho, mobPai, dt, ctx.getCadastrante(), ctx.getCadastrante());

		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Juntar documento";
	}
}
