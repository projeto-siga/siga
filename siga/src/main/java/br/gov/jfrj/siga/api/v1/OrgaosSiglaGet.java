package br.gov.jfrj.siga.api.v1;

import org.apache.commons.lang3.StringUtils;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IOrgaosSiglaGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Orgao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class OrgaosSiglaGet implements IOrgaosSiglaGet {
	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		if (StringUtils.isEmpty(req.sigla))
			throw new SwaggerException("O parâmetro sigla é obrigatório.", 400, null, req, resp, null);

		final CpOrgaoUsuario flt = new CpOrgaoUsuario();
		flt.setSigla(req.sigla.toUpperCase());
		CpOrgaoUsuario org = CpDao.getInstance().consultarPorSigla(flt);
		if (org == null)
			throw new SwaggerException("Nenhum órgão foi encontrado com a sigla informada.", 404, null, req, resp,
					null);

		resp.orgao = orgaoToResultadoPesquisa(org);
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
		return "selecionar órgãos";
	}
}
