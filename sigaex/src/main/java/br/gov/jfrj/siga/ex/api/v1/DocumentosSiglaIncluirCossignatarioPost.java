 package br.gov.jfrj.siga.ex.api.v1;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaIncluirCossignatarioPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaIncluirCossignatarioPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaIncluirCossignatarioPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.builder.ExMovimentacaoBuilder;

public class DocumentosSiglaIncluirCossignatarioPost implements IDocumentosSiglaIncluirCossignatarioPost {

	private DpPessoa getPessoa(DocumentosSiglaIncluirCossignatarioPostRequest req) {
		DpPessoa pes = null;
		if (StringUtils.isNotEmpty(req.matricula)) {
			pes = new DpPessoa();
			pes.setSigla(req.matricula);
			pes = dao().consultarPorSigla(pes);
		}
		return pes;
	}

	@Override
	public void run(DocumentosSiglaIncluirCossignatarioPostRequest req,
			DocumentosSiglaIncluirCossignatarioPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ctx.assertAcesso("");

				ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp,
						"Documento a Incluir Cossignatário");

				ctx.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

				DpPessoa pes = getPessoa(req);
				Date dt = dao().consultarDataEHoraDoServidor();

				if (pes == null)
					throw new AplicacaoException("Pessoa deve ser informada.");

				if (!Ex.getInstance().getComp().podeIncluirCosignatario(ctx.getTitular(), ctx.getLotaTitular(), mob)) {
					throw new AplicacaoException("Não é possível incluir cossignatário");
				}

				if (req.funcao != null && req.funcao.trim().isEmpty())
					req.funcao = null;
				if (req.lotacao != null && req.lotacao.trim().isEmpty())
					req.lotacao = null;
				if (req.localidade != null && req.localidade.trim().isEmpty())
					req.localidade = null;
				String funcaoUnidadeCosignatario = null;
				if (req.funcao != null || req.lotacao != null || req.localidade != null)
					funcaoUnidadeCosignatario = StringUtils.trimToEmpty(req.funcao) + ";"
							+ StringUtils.trimToEmpty(req.lotacao) + ";" + StringUtils.trimToEmpty(req.localidade);

				final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder.novaInstancia().setMob(mob)
						.setDescrMov(funcaoUnidadeCosignatario);

				final ExMovimentacao mov = movimentacaoBuilder.construir(dao());
				mov.setSubscritor(pes);

				Ex.getInstance().getBL().incluirCosignatario(ctx.getCadastrante(), ctx.getLotaTitular(), mob.doc(),
						mov.getDtMov(), mov.getSubscritor(), mov.getDescrMov());

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
