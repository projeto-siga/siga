package br.gov.jfrj.siga.api.v1;

import org.apache.commons.lang3.StringUtils;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.ILotacoesSiglaGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Lotacao;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Orgao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class LotacoesSiglaGet implements ILotacoesSiglaGet {
	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		if (StringUtils.isEmpty(req.sigla))
			throw new SwaggerException("O parâmetro sigla é obrigatório.", 400, null, req, resp, null);

		final DpLotacao flt = new DpLotacao();
		flt.setSigla(req.sigla.toUpperCase());
		DpLotacao lota = CpDao.getInstance().consultarPorSigla(flt);
		if (lota == null)
			throw new SwaggerException("Nenhuma lotação foi encontrada contendo a sigla informada.", 404, null, req,
					resp, null);

		resp.lotacao = lotacaoToResultadoPesquisa(lota);
	}

	private Lotacao lotacaoToResultadoPesquisa(DpLotacao lota) {
		Lotacao rp = new Lotacao();
		Orgao orgao = new Orgao();

		rp.siglaLotacao = lota.getSigla();
		rp.idLotacao = lota.getId().toString();
		rp.idLotacaoIni = lota.getIdLotacaoIni().toString();
		rp.nome = lota.getNomeLotacao();
		rp.sigla = lota.getSiglaCompleta();
		// Orgao Pessoa
		CpOrgaoUsuario o = lota.getOrgaoUsuario();
		orgao.idOrgao = o.getId().toString();
		orgao.nome = o.getNmOrgaoAI();
		orgao.sigla = o.getSigla();

		rp.orgao = orgao;
		return rp;
	}

	@Override
	public String getContext() {
		return "selecionar lotações";
	}
}
