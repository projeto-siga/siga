package br.gov.jfrj.siga.api.v1;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.ILocalidadesGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Localidade;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Uf;
import br.gov.jfrj.siga.dp.CpLocalidade;
import br.gov.jfrj.siga.dp.CpUF;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class LocalidadesGet implements ILocalidadesGet {
	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		if (StringUtils.isEmpty(req.idUf) && StringUtils.isEmpty(req.texto))
			throw new SwaggerException("Não foi informado nenhum parâmetro.", 404, null, req, resp, null);

		resp.list = pesquisarPorUfOuTexto(req, resp);
		if (resp.list == null || resp.list.isEmpty())
			throw new SwaggerException("Nenhum localidade foi encontrada para os parâmetros informados.", 404, null,
					req, resp, null);
	}

	private List<Localidade> pesquisarPorUfOuTexto(Request req, Response resp) {
		CpUF uf = new CpUF();
		List<CpLocalidade> l;
		if (StringUtils.isEmpty(req.idUf)) {
			l = CpDao.getInstance().consultarLocalidades();
		} else {
			uf.setIdUF(Long.valueOf(req.idUf));
			l = CpDao.getInstance().consultarLocalidadesPorUF(uf);
		}
		List<Localidade> listFull = l.stream().map(this::localidadeToResultadoPesquisa).collect(Collectors.toList());
		if (StringUtils.isEmpty(req.texto))
			return listFull;

		return listFull.stream().filter(loc -> loc.nome.contains(req.texto)).collect(Collectors.toList());
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
