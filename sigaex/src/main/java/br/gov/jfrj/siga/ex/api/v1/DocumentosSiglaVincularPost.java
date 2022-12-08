package br.gov.jfrj.siga.ex.api.v1;

import java.util.Date;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaVincularPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeVincular;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeVinculo;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaVincularPost implements IDocumentosSiglaVincularPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mobFilho = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento Secundário");
		ExMobil mobPai = ctx.buscarEValidarMobil(req.siglavertambem, req, resp, "Documento Ver Também");

		ctx.assertAcesso(mobFilho, ctx.getCadastrante(), ctx.getLotaTitular());

		Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();

		Ex.getInstance().getComp().afirmar("Não é possível fazer vínculo", ExPodeVincular.class, ctx.getCadastrante(), ctx.getLotaTitular(), mobFilho);

		Ex.getInstance().getBL().referenciarDocumento(ctx.getCadastrante(), ctx.getLotaCadastrante(), mobFilho, mobPai, ExTipoDeVinculo.RELACIONAMENTO,
				dt, ctx.getCadastrante(), ctx.getCadastrante());

		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Juntar documento";
	}
}
