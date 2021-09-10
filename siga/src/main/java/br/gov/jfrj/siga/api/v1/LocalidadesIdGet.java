package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.ILocalidadesIdGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Localidade;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Uf;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.CpLocalidade;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class LocalidadesIdGet implements ILocalidadesIdGet {
	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		if (req.id == null) {
			throw new AplicacaoException("O argumento de pesquisa id é obrigatório.");
		}
		resp.localidade = pesquisarPorId(req, resp);
	}

	private Localidade pesquisarPorId(Request req, Response resp) throws SwaggerException {
		List<CpLocalidade> l = (ArrayList<CpLocalidade>) CpDao.getInstance()
				.consultarPorIdOuIdInicial(CpLocalidade.class, "idLocalidade", null, Long.valueOf(req.id));
		if (l.size() == 0)
			throw new SwaggerException("Nenhuma localidade foi encontrada para os parâmetros informados.", 404, null,
					req, resp, null);

		return localidadeToResultadoPesquisa(l.get(0));
	}

	@Override
	public String getContext() {
		return "selecionar localidades";
	}

	private Localidade localidadeToResultadoPesquisa(CpLocalidade localidade) {
		Localidade loc = new Localidade();

		loc.idLocalidade = localidade.getId().toString();
		loc.nome = localidade.getNmLocalidade();
		Uf uf = new Uf();
		uf.idUf = localidade.getUF().getIdUF().toString();
		uf.nomeUf = localidade.getUF().getNmUF();
		loc.uf = uf;
		return loc;
	}

}