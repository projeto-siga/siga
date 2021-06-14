package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IOrgaosGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Orgao;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.CpOrgaoUsuarioDaoFiltro;

public class OrgaosGet implements IOrgaosGet {
	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		if (req.texto != null && req.idOrgao != null) {
			throw new AplicacaoException("Pesquisa permitida somente por um dos argumentos.");
		}

		if (req.texto != null && !req.texto.isEmpty()) {
			resp.list = pesquisarPorTexto(req, resp);
			return;
		}

		if (req.idOrgao != null && !req.idOrgao.isEmpty()) {
			resp.list = pesquisarOrgaoPorId(req, resp);
			return;
		}

		throw new AplicacaoException("Não foi fornecido nenhum parâmetro.");
	}

	private List<Orgao> pesquisarPorTexto(Request req, Response resp) throws SwaggerException {
		final CpOrgaoUsuarioDaoFiltro flt = new CpOrgaoUsuarioDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(req.texto));
		List<CpOrgaoUsuario> l;
		l = CpDao.getInstance().consultarPorFiltro(flt);
		if (l.isEmpty())
			throw new SwaggerException("Nenhum órgão foi encontrado para os parâmetros informados.", 404, null, req,
					resp, null);
		return l.stream().map(this::orgaoToResultadoPesquisa).collect(Collectors.toList());
	}

	private List<Orgao> pesquisarOrgaoPorId(Request req, Response resp) throws SwaggerException {
		try {
			CpOrgaoUsuario orgao = CpDao.getInstance().consultarOrgaoUsuarioPorId(Long.valueOf(req.idOrgao));
			List<Orgao> l = new ArrayList<>();
			l.add(orgaoToResultadoPesquisa(orgao));
			if (l.isEmpty())
				throw new SwaggerException("Nenhum órgão foi encontrado para os parâmetros informados.", 404, null, req,
						resp, null);
			return l;
		} catch (NoResultException e) {
			throw new SwaggerException("Nenhum órgão foi encontrado para os parâmetros informados.", 404, null, req,
					resp, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}

	private Orgao orgaoToResultadoPesquisa(CpOrgaoUsuario o) {
		Orgao orgao = new Orgao();

		orgao.idOrgao = o.getId().toString();
		orgao.nome = o.getNmOrgaoAI();
		orgao.sigla = o.getSigla();

		return orgao;
	}

	@Override
	public String getContext() {
		return "selecionar orgaos";
	}

}