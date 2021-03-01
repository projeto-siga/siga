package br.gov.jfrj.siga.api.v1;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.ICargosGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Cargo;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.CargosGetRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.CargosGetResponse;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpCargoDaoFiltro;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class CargosGet implements ICargosGet {
	@Override
	public void run(CargosGetRequest req, CargosGetResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(false, true)) {
			CurrentRequest.set(
					new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
			SigaObjects so = ApiContext.getSigaObjects();
			if (StringUtils.isEmpty(req.idOrgao))
					throw new SwaggerException(
							"O id do órgão é obrigatório.", 400, null, req, resp, null);
	
//			if (Long.valueOf(req.idOrgao) != so.getTitular().getOrgaoUsuario().getIdOrgaoUsu()) 
//				throw new SwaggerException(
//						"Usuário não autorizado para consultar dados deste órgão.", 403, null, req, resp, null);
	
			resp.list = pesquisarPorNome(req, resp);
			if (resp.list.isEmpty()) 
				throw new SwaggerException(
						"Nenhum cargo foi encontrado para os parâmetros informados.", 404, null, req, resp, null);
			
		} catch (AplicacaoException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}

	private List<Cargo> pesquisarPorNome(CargosGetRequest req, CargosGetResponse resp) {
		final DpCargoDaoFiltro flt = new DpCargoDaoFiltro();
		if (!(req.nomeCargo == null || req.nomeCargo.isEmpty() || req.nomeCargo == "")) 
			flt.setNome(Texto.removeAcentoMaiusculas(req.nomeCargo));
		flt.setIdOrgaoUsu(Long.valueOf(req.idOrgao));
		List<DpCargo> l = CpDao.getInstance().consultarPorFiltro(flt);
		return l.stream().map(this::cargoToResultadoPesquisa).collect(Collectors.toList());
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
