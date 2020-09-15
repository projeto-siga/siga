package br.gov.jfrj.siga.ex.api.v1;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.cp.CpAcesso;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.AcessoItem;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.AcessosGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.AcessosGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IAcessosGet;
import br.gov.jfrj.siga.ex.api.v1.TokenCriarPost.Usuario;
import br.gov.jfrj.siga.hibernate.ExDao;

public class AcessosGet implements IAcessosGet {

	@Override
	public void run(AcessosGetRequest req, AcessosGetResponse resp) throws Exception {
		Usuario u = TokenCriarPost.assertUsuario();

		resp.list = new ArrayList<>();


		DpPessoa cadastrante = ExDao.getInstance().getPessoaPorPrincipal(u.usuario);

		List<CpAcesso> l = ExDao.getInstance().consultarAcessosRecentes(cadastrante);

		for (CpAcesso a : l) {
			AcessoItem ai = new AcessoItem();
			ai.datahora = a.getDtInicio();
			ai.ip = a.getAuditIP();
			resp.list.add(ai);
		}
	}


	@Override
	public String getContext() {
		return "listar acessos recentes";
	}
}
