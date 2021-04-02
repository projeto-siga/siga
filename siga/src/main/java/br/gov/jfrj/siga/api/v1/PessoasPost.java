package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPessoasPost;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PessoasPostRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PessoasPostResponse;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpBL;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class PessoasPost implements IPessoasPost {
	@Override
	public void run(PessoasPostRequest req, PessoasPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ApiContext.assertAcesso("WS_REST: Acesso aos webservices REST;CAD_PESSOA: Cadastrar Pessoa");
				SigaObjects so = ApiContext.getSigaObjects();
	
				if (req.siglaOrgao == null || "".equals(req.siglaOrgao))
						throw new AplicacaoException("Sigla do órgão é obrigatória");
						 
				if (req.idCargoIni != null && !req.idCargoIni.matches("\\d+"))
						throw new AplicacaoException("Id do Cargo deve ser numérico.");
						 
				if (req.idFuncaoConfiancaIni != null && !req.idFuncaoConfiancaIni.matches("\\d+"))
						throw new AplicacaoException("Id da Função de Confiança deve ser numérico.");
						 
				if (req.email == null || "".equals(req.email))
					throw new AplicacaoException("E-mail é obrigatório");
				
				CpOrgaoUsuario orgaoFiltro = new CpOrgaoUsuario();
				orgaoFiltro.setSigla(req.siglaOrgao);
				CpOrgaoUsuario orgaoUsu = CpDao.getInstance().consultarPorSigla(orgaoFiltro);
				if (orgaoUsu == null)
					throw new AplicacaoException("Órgão não existente.");
				if (orgaoUsu.getId() != so.getTitular().getOrgaoUsuario().getId()
					&& !Cp.getInstance()
						.getConf()
						.podeUtilizarServicoPorConfiguracao(so.getTitular(), so.getLotaTitular(), 
							"SIGA:Sistema Integrado de Gestão Administrativa;WS_REST: Acesso aos webservices REST;"
							+ "CAD_PES_TODOS_ORGAOS: Cadatrar pessoas em todos órgãos")) {
					throw new AplicacaoException("Usuário autorizado a incluir pessoas somente em seu próprio órgão.");
				}
				
				Long idOrgaoUsu = orgaoUsu.getIdOrgaoUsu();
	
				Long idCargo = null;
				if (req.idCargoIni != null) {
					idCargo = Long.valueOf(req.idCargoIni);
					List<DpCargo> cargo = (ArrayList<DpCargo>) CpDao.getInstance()
							.consultarPorIdOuIdInicial(DpCargo.class, "idCargoIni", "dataFimCargo", Long.valueOf(req.idCargoIni));
					if (cargo == null || cargo.isEmpty())
						throw new AplicacaoException("Cargo não existente.");
					if (cargo.get(0).getOrgaoUsuario().getIdOrgaoUsu() != idOrgaoUsu)
						throw new AplicacaoException("Cargo não existe no órgão especificado.");
				}
					
				Long idFuncao = null;
				if (req.idFuncaoConfiancaIni != null) {
					idFuncao = Long.valueOf(req.idFuncaoConfiancaIni);
					List<DpFuncaoConfianca> funcaoConfianca = (ArrayList<DpFuncaoConfianca>) CpDao.getInstance()
							.consultarPorIdOuIdInicial(DpFuncaoConfianca.class, "idFuncao", "dataFimFuncao", Long.valueOf(req.idFuncaoConfiancaIni));
					if (funcaoConfianca == null || funcaoConfianca.isEmpty())
						throw new AplicacaoException("Função de confiança não existente.");
					if (funcaoConfianca.get(0).getOrgaoUsuario().getIdOrgaoUsu() != idOrgaoUsu)
						throw new AplicacaoException("Função de confiança não existe no órgão especificado.");
				}
					
				if (req.lotacao == null || "".equals(req.lotacao))
					throw new AplicacaoException("Lotação é obrigatória");
					 
				if (req.rgUf != null && req.rgUf.length() != 2)
					throw new AplicacaoException(
							"UF do RG deve ser informada com dois caracteres ou deve ser nula.");
					
				DpLotacao lotacaoFiltro = new DpLotacao();
				lotacaoFiltro.setSigla(req.lotacao);
				DpLotacao lota = CpDao.getInstance().consultarPorSigla(lotacaoFiltro);
				if (lota == null)
					throw new AplicacaoException("Lotação não existente.");
				if (lota.getOrgaoUsuario().getIdOrgaoUsu() != idOrgaoUsu)
					throw new AplicacaoException("Lotação não existe no órgão especificado.");
				Long idLotacao = lota.getId();
	
				String nmPessoa = req.nome;
				String nomeExibicao = req.nomeAbreviado;
				String dtNascimento = req.dataNascimento;
				String cpf = req.formCpf;
				String email = req.email;
				String identidade = req.rg;
				String orgaoIdentidade = req.rgOrgaoExpedidor;
				String ufIdentidade = null;
				if (req.rgUf != null)
					req.rgUf.toUpperCase();
				String dataExpedicaoIdentidade = req.rgDataExpedicao;
				String enviarEmail = null;
				if (req.enviarEmailAcesso) 
					enviarEmail = req.enviarEmailAcesso.toString();
	
				DpPessoa pes = new CpBL().criarUsuario(null, so.getIdentidadeCadastrante(), idOrgaoUsu, idCargo, 
						idFuncao, idLotacao, nmPessoa, dtNascimento, cpf, email, identidade,
						orgaoIdentidade, ufIdentidade, dataExpedicaoIdentidade, nomeExibicao, enviarEmail);
	
				resp.idPessoa = pes.getId().toString();
				resp.siglaPessoa = pes.getSigla();
			} catch (RegraNegocioException | AplicacaoException e) {
				ctx.rollback(e);
				throw new SwaggerException(e.getMessage(), 400, null, req, resp, null);
			} catch (Exception e) {
				ctx.rollback(e);
				throw e;
			}
		}
	}
	
	@Override
	public String getContext() {
		return "selecionar pessoas";
	}

	protected CpDao dao() {
		return CpDao.getInstance();
	}
	
}
