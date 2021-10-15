package br.gov.jfrj.siga.api.v1;

import java.util.stream.Collectors;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.AcessoItem;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.IAcessosGet;
import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.RequestInfo;
import br.gov.jfrj.siga.cp.CpAcesso;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class AcessosGet implements IAcessosGet {

	private AcessoItem cpAcessoToAcessoItem(CpAcesso a) {
		AcessoItem ai = new AcessoItem();

		ai.datahora = a.getDtInicio();
		ai.ip = a.getAuditIP();

		return ai;
	}

	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		CurrentRequest.set(
				new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
		resp.list = CpDao.getInstance().consultarAcessosRecentes(ctx.getCadastrante()).stream()
				.map(this::cpAcessoToAcessoItem).collect(Collectors.toList());
	}

	@Override
	public String getContext() {
		return "listar acessos recentes";
	}
}
