package br.gov.jfrj.siga.ex.api.v1;

import java.util.Date;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaIncluirCopiaPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaIncluirCopiaPost implements IDocumentosSiglaIncluirCopiaPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento Principal");
		ExMobil mobCopia = ctx.buscarEValidarMobil(req.siglacopia, req, resp, "Documento Cópia");

		ctx.assertAcesso(mob, ctx.getCadastrante(), ctx.getLotaTitular());

		Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();

		if (!Ex.getInstance().getComp().podeCopiar(ctx.getCadastrante(), ctx.getLotaTitular(), mob)) {
			throw new AplicacaoException("Não é possível copiar");
		}

		Ex.getInstance().getBL().copiar(ctx.getCadastrante(), ctx.getLotaCadastrante(), mob, mobCopia, dt, null,
				ctx.getTitular());

		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Juntar documento";
	}
}
