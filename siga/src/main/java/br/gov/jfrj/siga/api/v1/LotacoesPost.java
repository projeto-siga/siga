package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.ILotacoesPost;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.LotacoesPostRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.LotacoesPostResponse;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpLocalidade;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class LotacoesPost implements ILotacoesPost {
	@Override
	public void run(LotacoesPostRequest req, LotacoesPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			ApiContext.assertAcesso("WS_REST: Acesso aos webservices REST;CAD_LOTACAO: Cadastrar Lotação");
			SigaObjects so = ApiContext.getSigaObjects();

			if (req.siglaOrgao == null || "".equals(req.siglaOrgao))
					throw new SwaggerException(
							"Sigla do órgão é obrigatória", 400, null, req, resp, null);
					 
			if (req.idLocalidade != null && !req.idLocalidade.matches("\\d+"))
					throw new SwaggerException(
							"Id da localidade deve ser numérico.", 400, null, req, resp, null);
					 
			if (req.nome == null || "".equals(req.nome))
				throw new SwaggerException(
						"Nome é obrigatório", 400, null, req, resp, null);

			if(!req.nome.matches("[\'a-zA-ZàáâãéêíóôõúçÀÁÂÃÉÊÍÓÔÕÚÇ 0-9.,/-]+")) 
				throw new AplicacaoException("Nome com caracteres não permitidos");
			
			if (req.sigla == null || "".equals(req.sigla))
				throw new SwaggerException(
						"Sigla é obrigatória", 400, null, req, resp, null);

			if(!req.sigla.matches("[a-zA-ZçÇ0-9,/-]+")) 
				throw new AplicacaoException("Sigla com caracteres não permitidos");
			
			if (req.isAcessoExterno == null )
				throw new SwaggerException(
						"Parãmetro isAcessoExterno deve ser true ou false", 400, null, req, resp, null);

//			if (req.ativar != null && !"true".equals(req.ativar) && !"false".equals(req.ativar))
//				throw new SwaggerException(
//						"Parãmetro ativar deve ser true ou false", 400, null, req, resp, null);
//			String situacao = ("true".equals(req.ativar) ? "false" : "true");
			String situacao = "true";

			CpOrgaoUsuario orgaoFiltro = new CpOrgaoUsuario();
			orgaoFiltro.setSigla(req.siglaOrgao);
			CpOrgaoUsuario orgaoUsu = CpDao.getInstance().consultarPorSigla(orgaoFiltro);
			if (orgaoUsu == null)
				throw new SwaggerException(
						"Órgão não existente.", 400, null, req, resp, null);
			if (orgaoUsu.getId() != so.getTitular().getOrgaoUsuario().getId()
					&& !Cp.getInstance()
						.getConf()
						.podeUtilizarServicoPorConfiguracao(so.getTitular(), so.getLotaTitular(), 
							"SIGA:Sistema Integrado de Gestão Administrativa;WS_REST: Acesso aos webservices REST;"
							+ "CAD_LOTA_TODOS_ORGAOS: Cadatrar lotações em todos órgãos")) {
					throw new SwaggerException(
							"Usuário autorizado a incluir lotações somente em seu próprio órgão.", 400, null, req, resp, null);
				}
			Long idOrgaoUsu = orgaoUsu.getIdOrgaoUsu();

			Long idLocalidade = null;
			if (req.idLocalidade != null) {
				idLocalidade = Long.valueOf(req.idLocalidade);
				List<CpLocalidade> localidade = (ArrayList<CpLocalidade>) CpDao.getInstance()
						.consultarPorIdOuIdInicial(CpLocalidade.class, "idLocalidade", 
								null, idLocalidade);
				if (localidade.isEmpty())
					throw new SwaggerException(
							"Localidade não existente.", 400, null, req, resp, null);
			} else {
				throw new SwaggerException(
						"Id da localidade não informado.", 400, null, req, resp, null);
			}
				
			Long idLotaPai = null;
			if (req.idLotacaoPaiIni != null) {
				idLotaPai = Long.valueOf(req.idLotacaoPaiIni);
				DpLotacao lotaPai = CpDao.getInstance()
						.consultarPorIdInicial(DpLotacao.class, Long.valueOf(req.idLotacaoPaiIni));
				if (lotaPai != null)
					throw new SwaggerException(
							"Lotação pai não existente.", 400, null, req, resp, null);
				idLotaPai = lotaPai.getIdInicial();
			}
				
			DpLotacao lota = Cp.getInstance().getBL().criarLotacao(so.getIdentidadeCadastrante(), so.getTitular(), so.getTitular().getLotacao(), 
					null, req.nome, idOrgaoUsu, req.sigla, situacao, (req.isAcessoExterno ? true : null), 
					idLotaPai, idLocalidade);

			resp.idLotacao = lota.getId().toString();
			resp.siglaLotacao = lota.getSigla();
		} catch (AplicacaoException | SwaggerException e) {
			
			throw new SwaggerException(
					e.getMessage(), 400, null, req, resp, null);
		} catch (Exception e) {
			throw e;
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
