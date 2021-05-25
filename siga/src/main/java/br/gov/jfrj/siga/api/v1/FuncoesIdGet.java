package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.FuncaoConfianca;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.IFuncoesIdGet;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class FuncoesIdGet implements IFuncoesIdGet {
	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		if (StringUtils.isEmpty(req.id))
			throw new SwaggerException("O argumento de pesquisa id é obrigatório.", 400, null, req, resp, null);

		resp.funcaoConfianca = pesquisarPorId(req, resp);
	}

	private FuncaoConfianca pesquisarPorId(Request req, Response resp) throws SwaggerException {
		ArrayList<DpFuncaoConfianca> la = (ArrayList<DpFuncaoConfianca>) CpDao.getInstance()
				.consultarPorIdOuIdInicial(DpFuncaoConfianca.class, "idFuncaoIni", null, Long.valueOf(req.id));
		ArrayList<DpFuncaoConfianca> l = (ArrayList<DpFuncaoConfianca>) CpDao.getInstance().consultarPorIdOuIdInicial(
				DpFuncaoConfianca.class, "idFuncaoIni", "dataFimFuncao", Long.valueOf(req.id));
		if (l.size() == 0)
			throw new SwaggerException("Nenhuma função de confiança foi encontrada para os parâmetros informados.", 404,
					null, req, resp, null);

//		if (Long.valueOf(funcao.getOrgaoUsuario().getIdOrgaoUsu()) != so.getTitular().getOrgaoUsuario().getIdOrgaoUsu()) 
//			throw new SwaggerException(
//					"Usuário não autorizado para consultar dados deste órgão.", 403, null, req, resp, null);
		return funcaoToResultadoPesquisa(l.get(0));
	}

	@Override
	public String getContext() {
		return "selecionar funções";
	}

	private FuncaoConfianca funcaoToResultadoPesquisa(DpFuncaoConfianca funcao) {
		FuncaoConfianca func = new FuncaoConfianca();

		func.sigla = funcao.getSigla();
		func.idFuncaoConfianca = funcao.getId().toString();
		func.idFuncaoConfiancaIni = funcao.getIdFuncaoIni().toString();
		func.idExterna = funcao.getIdExterna().toString();
		func.nome = funcao.getNomeFuncao();
		return func;
	}

}
