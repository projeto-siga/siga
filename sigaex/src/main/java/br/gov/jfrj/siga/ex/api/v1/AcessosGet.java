package br.gov.jfrj.siga.ex.api.v1;

import java.util.stream.Collectors;

import br.gov.jfrj.siga.cp.CpAcesso;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.AcessoItem;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.AcessosGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.AcessosGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IAcessosGet;
import br.gov.jfrj.siga.hibernate.ExDao;
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
		SwaggerHelper.buscarEValidarUsuarioLogado();
		SigaObjects so = SwaggerHelper.getSigaObjects();
		DpPessoa cadastrante = so.getCadastrante();

		resp.list = ExDao.getInstance().consultarAcessosRecentes(cadastrante).stream().map(this::cpAcessoToAcessoItem)
				.collect(Collectors.toList());
	}

	@Override
	public String getContext() {
		return "listar acessos recentes";
	}
}
