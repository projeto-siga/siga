package br.gov.jfrj.siga.api.v1;

import org.apache.commons.lang3.StringUtils;

import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IOrgaosSiglaGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Orgao;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.OrgaosSiglaGetRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.OrgaosSiglaGetResponse;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class OrgaosSiglaGet implements IOrgaosSiglaGet {
	@Override
	public void run(OrgaosSiglaGetRequest req, OrgaosSiglaGetResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(false, true)) {
			if (StringUtils.isEmpty(req.sigla))
				throw new SwaggerException(
						"O parâmetro sigla é obrigatório.", 400, null, req, resp, null);

			final CpOrgaoUsuario flt = new CpOrgaoUsuario();
			flt.setSigla(req.sigla.toUpperCase());
			CpOrgaoUsuario org = CpDao.getInstance().consultarPorSigla(flt);
			if (org == null)
				throw new SwaggerException("Nenhum órgão foi encontrado com a sigla informada.",
						404, null, req, resp, null);

			resp.orgao = orgaoToResultadoPesquisa(org);
			
		} catch (AplicacaoException | SwaggerException e) {
			throw e;
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
		return "selecionar órgãos";
	}
}
