package br.gov.jfrj.siga.api.v1;

import java.util.stream.Collectors;

import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.AcessoItem;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.AcessosGetRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.AcessosGetResponse;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.IAcessosGet;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.RequestInfo;
import br.gov.jfrj.siga.cp.CpAcesso;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class AcessosGet implements IAcessosGet {

	private AcessoItem cpAcessoToAcessoItem(CpAcesso a) {
		AcessoItem ai = new AcessoItem();

		ai.datahora = a.getDtInicio();
		ai.ip = a.getAuditIP();

		return ai;
	}

	@Override
	public void run(AcessosGetRequest req, AcessosGetResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(false, true)) {
			CurrentRequest.set(
					new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
			ApiContext.assertAcesso("");
			SigaObjects so = ApiContext.getSigaObjects();
			DpPessoa cadastrante = so.getCadastrante();
	
			resp.list = CpDao.getInstance().consultarAcessosRecentes(cadastrante).stream().map(this::cpAcessoToAcessoItem)
					.collect(Collectors.toList());
		} catch (AplicacaoException | SwaggerException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}

	@Override
	public String getContext() {
		return "listar acessos recentes";
	}
}
