package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaPdfCompletoGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaPdfCompletoGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocSiglaPdfCompletoGet;
import br.gov.jfrj.siga.ex.api.v1.TokenCriarPost.Usuario;
import br.gov.jfrj.siga.hibernate.ExDao;

public class DocSiglaPdfCompletoGet implements IDocSiglaPdfCompletoGet {

	@Override
	public void run(DocSiglaPdfCompletoGetRequest req,
			DocSiglaPdfCompletoGetResponse resp) throws Exception {
		Usuario u = TokenCriarPost.assertUsuario();

		try {
			DpPessoa cadastrante = ExDao.getInstance().getPessoaPorPrincipal(u.usuario);
			DpPessoa titular = cadastrante;
			DpLotacao lotaTitular = cadastrante.getLotacao();

			ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
			flt.setSigla(req.sigla);
			ExMobil mob = ExDao.getInstance().consultarPorSigla(flt);

			Utils.assertAcesso(mob, titular, lotaTitular);

			resp.jwt = DownloadJwtFilenameGet.jwt(u.usuario,
					mob.getCodigoCompacto(), null, null, null);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public String getContext() {
		return "obter documento";
	}

}
