package br.gov.jfrj.siga.ex.api.v1;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaDefinirPerfilPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaDefinirPerfilPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaDefinirPerfilPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.builder.ExMovimentacaoBuilder;

public class DocumentosSiglaDefinirPerfilPost implements IDocumentosSiglaDefinirPerfilPost {

	private DpPessoa getPessoa(DocumentosSiglaDefinirPerfilPostRequest req) {
		DpPessoa pes = null;
		if (StringUtils.isNotEmpty(req.matricula)) {
			pes = new DpPessoa();
			pes.setSigla(req.matricula);
			pes = dao().consultarPorSigla(pes);
		}
		return pes;
	}

	private DpLotacao getLotacao(DocumentosSiglaDefinirPerfilPostRequest req) {
		DpLotacao lot = null;
		if (StringUtils.isNotEmpty(req.lotacao)) {
			lot = new DpLotacao();
			lot.setSigla(req.lotacao);
			lot = dao().consultarPorSigla(lot);
		}
		return lot;
	}

	@Override
	public void run(DocumentosSiglaDefinirPerfilPostRequest req, DocumentosSiglaDefinirPerfilPostResponse resp)
			throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ctx.assertAcesso("");

				ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp,
						"Documento a Marcar");
				
				ctx.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

				ExPapel m = dao().consultar(Long.parseLong(req.idPerfil), ExPapel.class, false);
				DpLotacao lot = getLotacao(req);
				DpPessoa pes = getPessoa(req);
				Date dt = dao().consultarDataEHoraDoServidor();

				if (m == null)
					throw new AplicacaoException("Perfil deve ser informado.");

				final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder.novaInstancia();

				if (pes == null && lot == null)
					throw new AplicacaoException("Pessoa ou lotação devem ser informadas.");

				final ExMovimentacao mov = movimentacaoBuilder.construir(dao());
				mov.setExPapel(m);
				mov.setResp(pes);
				mov.setLotaResp(lot);
				Ex.getInstance().getBL().vincularPapel(ctx.getCadastrante(), ctx.getLotaTitular(), mob, mov.getDtMov(),
						mov.getLotaResp(), mov.getResp(), mov.getSubscritor(), mov.getTitular(), mov.getDescrMov(),
						mov.getNmFuncaoSubscritor(), mov.getExPapel());
				resp.status = "OK";
			} catch (RegraNegocioException | AplicacaoException e) {
				ctx.rollback(e);
				throw new SwaggerException(e.getMessage(), 400, null, req, resp, null);
			} catch (Exception e) {
				ctx.rollback(e);
				throw e;
			}
		}
	}

	public ExDao dao() {
		return ExDao.getInstance();
	}

	@Override
	public String getContext() {
		return "definir perfil";
	}

}
