package br.gov.jfrj.siga.ex.api.v1;

import java.util.Date;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaDefinirAcessoPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.Transacional;
import br.gov.jfrj.siga.vraptor.builder.ExMovimentacaoBuilder;

@Transacional
public class DocumentosSiglaDefinirAcessoPost implements IDocumentosSiglaDefinirAcessoPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Marcar");

		ctx.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

		if (!Ex.getInstance().getComp().podeRedefinirNivelAcesso(ctx.getTitular(), ctx.getLotaTitular(), mob)) {
			throw new AplicacaoException("Não é possível redefinir o nível de acesso");
		}

		ExNivelAcesso m = dao().consultar(Long.parseLong(req.idAcesso), ExNivelAcesso.class, false);
		Date dt = dao().consultarDataEHoraDoServidor();

		if (m == null)
			throw new AplicacaoException("Nível de acesso deve ser informado.");

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder.novaInstancia();

		final ExMovimentacao mov = movimentacaoBuilder.construir(dao());
		mov.setExNivelAcesso(m);
		Ex.getInstance().getBL().redefinirNivelAcesso(ctx.getCadastrante(), ctx.getLotaTitular(), mob.doc(),
				mov.getDtMov(), mov.getLotaResp(), mov.getResp(), mov.getSubscritor(), mov.getTitular(),
				mov.getNmFuncaoSubscritor(), m);

		resp.status = "OK";
	}

	public ExDao dao() {
		return ExDao.getInstance();
	}

	@Override
	public String getContext() {
		return "definir nível de acesso";
	}

}
