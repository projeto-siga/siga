package br.gov.jfrj.siga.api.v1;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IFuncoesGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.FuncaoConfianca;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.FuncoesGetRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.FuncoesGetResponse;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpFuncaoConfiancaDaoFiltro;

public class FuncoesGet implements IFuncoesGet {
	@Override
	public void run(FuncoesGetRequest req, FuncoesGetResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(false, true)) {
			CurrentRequest.set(
					new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
			if (StringUtils.isEmpty(req.idOrgao))
				throw new SwaggerException(
						"O id do órgão é obrigatório.", 400, null, req, resp, null);

//			if (Long.valueOf(req.idOrgao) != so.getTitular().getOrgaoUsuario().getIdOrgaoUsu()) 
//				throw new SwaggerException(
//						"Usuário não autorizado para consultar dados deste órgão.", 403, null, req, resp, null);

			resp.list = pesquisarPorNome(req, resp);
			if (resp.list.isEmpty()) 
				throw new SwaggerException(
						"Nenhuma função de confiança foi encontrada para os parâmetros informados.", 404, null, req, resp, null);

		} catch (AplicacaoException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}

	private List<FuncaoConfianca> pesquisarPorNome(FuncoesGetRequest req, FuncoesGetResponse resp) {
		final DpFuncaoConfiancaDaoFiltro flt = new DpFuncaoConfiancaDaoFiltro();
		if (!(req.nomeFuncaoConfianca == null || req.nomeFuncaoConfianca.isEmpty() || req.nomeFuncaoConfianca == "")) 
			flt.setNome(Texto.removeAcentoMaiusculas(req.nomeFuncaoConfianca));
		flt.setIdOrgaoUsu(Long.valueOf(req.idOrgao));
		List<DpFuncaoConfianca> l = CpDao.getInstance().consultarPorFiltro(flt);
		return l.stream().map(this::cargoToResultadoPesquisa).collect(Collectors.toList());
	}

	@Override
	public String getContext() {
		return "selecionar cargos";
	}

	private FuncaoConfianca cargoToResultadoPesquisa(DpFuncaoConfianca cargo) {
		FuncaoConfianca func = new FuncaoConfianca();

		func.sigla = cargo.getSigla();
		func.idFuncaoConfianca = cargo.getId().toString();
		func.idFuncaoConfiancaIni = cargo.getIdFuncaoIni().toString();
		func.idExterna = cargo.getIdExterna().toString();
		func.nome = cargo.getNomeFuncao();
		return func;
	}

}
