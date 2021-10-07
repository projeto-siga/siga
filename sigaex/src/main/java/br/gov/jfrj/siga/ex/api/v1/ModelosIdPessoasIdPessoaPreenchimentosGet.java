package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IModelosIdPessoasIdPessoaPreenchimentosGet;
import br.gov.jfrj.siga.hibernate.ExDao;

public class ModelosIdPessoasIdPessoaPreenchimentosGet implements IModelosIdPessoasIdPessoaPreenchimentosGet {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExDao dao = ExDao.getInstance();
		DpPessoa pes = dao.consultar(Long.parseLong(req.idPessoa), DpPessoa.class, false);
		resp.list = ModelosIdLotacoesIdLotacaoPreenchimentosGet.listarPreenchimentos(Long.parseLong(req.id), pes.getId());
	}

	@Override
	public String getContext() {
		return "obter preenchimentos";
	}
}
