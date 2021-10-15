package br.gov.jfrj.siga.api.v1;

import java.util.List;
import java.util.stream.Collectors;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IUfsGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Uf;
import br.gov.jfrj.siga.dp.CpUF;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class UfsGet implements IUfsGet {
	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		resp.list = listarUf(req, resp);
		if (resp.list.isEmpty())
			throw new SwaggerException("Nenhuma uf foi encontrada.", 404, null, req, resp, null);
	}

	private List<Uf> listarUf(Request req, Response resp) {
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