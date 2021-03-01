package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;

import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.ICargosIdGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Cargo;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.CargosIdGetRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.CargosIdGetResponse;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class CargosIdGet implements ICargosIdGet {
	private SigaObjects so;
	@Override
	public void run(CargosIdGetRequest req, CargosIdGetResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(false, true)) {
			CurrentRequest.set(
					new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
			so = ApiContext.getSigaObjects();
			
			if (req.id == null) {
				throw new AplicacaoException("O argumento de pesquisa id é obrigatório.");
			}
	
			resp.cargo = pesquisarPorId(req, resp);
		} catch (AplicacaoException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}

	private Cargo pesquisarPorId(CargosIdGetRequest req, CargosIdGetResponse resp) throws SwaggerException {
		List<DpCargo> l = (ArrayList<DpCargo>) CpDao.getInstance()
				.consultarPorIdOuIdInicial(DpCargo.class, "idCargoIni", "dataFimCargo", Long.valueOf(req.id));
		if (l.size() == 0)
			throw new SwaggerException(
					"Nenhum cargo foi encontrado para os parâmetros informados.", 404, null, req, resp, null);

//		if (Long.valueOf(cargo.getOrgaoUsuario().getIdOrgaoUsu()) != so.getTitular().getOrgaoUsuario().getIdOrgaoUsu()) 
//			throw new SwaggerException(
//					"Usuário não autorizado para consultar dados deste órgão.", 403, null, req, resp, null);

		return cargoToResultadoPesquisa(l.get(0));
	}

	@Override
	public String getContext() {
		return "selecionar cargos";
	}

	private Cargo cargoToResultadoPesquisa(DpCargo cargo) {
		Cargo crgo = new Cargo();

		crgo.sigla = cargo.getSigla();
		crgo.idCargo = cargo.getId().toString();
		crgo.idCargoIni = cargo.getIdCargoIni().toString();
		crgo.idExterna = cargo.getIdExterna().toString();
		crgo.nome = cargo.getNomeCargo();
		return crgo;
	}

}
