package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.ILotacoesPost;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpLocalidade;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class LotacoesPost implements ILotacoesPost {
	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		try {
			ctx.assertAcesso("WS_REST: Acesso aos webservices REST;CAD_LOTACAO: Cadastrar Lotação");

			if (req.siglaOrgao == null || "".equals(req.siglaOrgao))
				throw new AplicacaoException("Sigla do órgão é obrigatória");

			if (req.idLocalidade != null && !req.idLocalidade.matches("\\d+"))
				throw new AplicacaoException("Id da localidade deve ser numérico.");

			if (req.nome == null || "".equals(req.nome))
				throw new AplicacaoException("Nome é obrigatório");

			if (!req.nome.matches("[\'a-zA-ZàáâãéêíóôõúçÀÁÂÃÉÊÍÓÔÕÚÇ 0-9.,/-]+"))
				throw new AplicacaoException("Nome com caracteres não permitidos");

			if (req.sigla == null || "".equals(req.sigla))
				throw new AplicacaoException("Sigla é obrigatória");

			if (!req.sigla.matches("[a-zA-ZçÇ0-9,/-]+"))
				throw new AplicacaoException("Sigla com caracteres não permitidos");

			if (req.isAcessoExterno == null)
				throw new AplicacaoException("Parãmetro isAcessoExterno deve ser true ou false");

			// if (req.ativar != null && !"true".equals(req.ativar) &&
			// !"false".equals(req.ativar))
			// throw new SwaggerException(
			// "Parãmetro ativar deve ser true ou false", 400, null, req, resp, null);
			// String situacao = ("true".equals(req.ativar) ? "false" : "true");
			String situacao = "true";

			CpOrgaoUsuario orgaoFiltro = new CpOrgaoUsuario();
			orgaoFiltro.setSigla(req.siglaOrgao);
			CpOrgaoUsuario orgaoUsu = CpDao.getInstance().consultarPorSigla(orgaoFiltro);
			if (orgaoUsu == null)
				throw new AplicacaoException("Órgão não existente.");
			if (orgaoUsu.getId() != ctx.getTitular().getOrgaoUsuario().getId()
					&& !Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(ctx.getTitular(),
							ctx.getLotaTitular(),
							"SIGA:Sistema Integrado de Gestão Administrativa;WS_REST: Acesso aos webservices REST;"
									+ "CAD_LOTA_TODOS_ORGAOS: Cadatrar lotações em todos órgãos")) {
				throw new SwaggerException("Usuário autorizado a incluir lotações somente em seu próprio órgão.", 403,
						null, req, resp, null);
			}
			Long idOrgaoUsu = orgaoUsu.getIdOrgaoUsu();

			Long idLocalidade = null;
			if (req.idLocalidade != null) {
				idLocalidade = Long.valueOf(req.idLocalidade);
				List<CpLocalidade> localidade = (ArrayList<CpLocalidade>) CpDao.getInstance()
						.consultarPorIdOuIdInicial(CpLocalidade.class, "idLocalidade", null, idLocalidade);
				if (localidade.isEmpty())
					throw new AplicacaoException("Localidade não existente.");
			} else {
				throw new AplicacaoException("Id da localidade não informado.");
			}

			Long idLotaPai = null;
			if (req.idLotacaoPaiIni != null) {
				idLotaPai = Long.valueOf(req.idLotacaoPaiIni);
				DpLotacao lotaPai = CpDao.getInstance().consultarPorIdInicial(DpLotacao.class,
						Long.valueOf(req.idLotacaoPaiIni));
				if (lotaPai != null)
					throw new AplicacaoException("Lotação pai não existente.");
				idLotaPai = lotaPai.getIdInicial();
			}

			DpLotacao lota = Cp.getInstance().getBL().criarLotacao(ctx.getIdentidadeCadastrante(), ctx.getTitular(),
					ctx.getTitular().getLotacao(), null, req.nome, idOrgaoUsu, req.sigla, situacao,
					(req.isAcessoExterno ? true : null), idLotaPai, idLocalidade);

			resp.idLotacao = lota.getId().toString();
			resp.siglaLotacao = lota.getSigla();
		} catch (RegraNegocioException | AplicacaoException e) {
			throw new SwaggerException(e.getMessage(), 400, null, req, resp, null);
		}
	}

	@Override
	public String getContext() {
		return "incluir lotacoes";
	}

	protected CpDao dao() {
		return CpDao.getInstance();
	}

}
