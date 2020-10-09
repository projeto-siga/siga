package br.gov.jfrj.siga.api.v1;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.ILotacaoIdLotacaoIniLotacaoAtualGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.LotacaoAtual;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.LotacaoIdLotacaoIniLotacaoAtualGetRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.LotacaoIdLotacaoIniLotacaoAtualGetResponse;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class LotacaoIdLotacaoIniLotacaoAtualGet implements ILotacaoIdLotacaoIniLotacaoAtualGet {

	@Override
	public String getContext() {
		return "selecionar lotação atual";
	}

	@Override
	public void run(LotacaoIdLotacaoIniLotacaoAtualGetRequest req, LotacaoIdLotacaoIniLotacaoAtualGetResponse resp)
			throws Exception {
		CurrentRequest.set(new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
		SwaggerHelper.buscarEValidarUsuarioLogado();

		try {
			//TODO: ver se precisa de outros parametros listarLotacao
			DpLotacao Lotacao = new DpLotacao();
			Lotacao.setIdLotacaoIni(Long.valueOf(req.idLotacaoIni));
			
			DpLotacao lotacaoAtual = CpDao.getInstance().obterLotacaoAtual(Lotacao);
			
			LotacaoAtual lotacaoResp = new LotacaoAtual();
			lotacaoResp.idLotacao = lotacaoAtual.getId().toString();
			lotacaoResp.idLotacaoIni = lotacaoAtual.getIdLotacaoIni().toString();
			lotacaoResp.nome = lotacaoAtual.getNomeLotacao();
			lotacaoResp.sigla = lotacaoAtual.getSiglaCompleta();
			lotacaoResp.orgao = lotacaoAtual.getOrgaoUsuario().getNmOrgaoUsu();
			
			resp.lotacaoAtual = lotacaoResp;
		}catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
		
	}

}
