package br.gov.jfrj.siga.api.v1;

import java.util.List;
import java.util.stream.Collectors;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IUfsGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Uf;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.UfsGetRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.UfsGetResponse;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.CpUF;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class UfsGet implements IUfsGet {
	@Override
	public void run(UfsGetRequest req, UfsGetResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(false, true)) {
			resp.list = listarUf(req, resp);
			if (resp.list.isEmpty()) 
				throw new SwaggerException(
						"Nenhuma uf foi encontrada.", 404, null, req, resp, null);
			
		} catch (AplicacaoException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	private List<Uf> listarUf(UfsGetRequest req, UfsGetResponse resp) {
		List<CpUF> l = CpDao.getInstance().consultarUF();
		return l.stream().map(this::ufToResultadoPesquisa).collect(Collectors.toList());
	}

	@Override
	public String getContext() {
		return "listar ufs";
	}

	private Uf ufToResultadoPesquisa(CpUF uf) {
		Uf ufResult = new Uf();

		ufResult.idUf = uf.getId().toString();
		ufResult.nomeUf = uf.getNmUF();
		return ufResult;
	}

}