
package br.gov.jfrj.siga.wf.api.v1;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.IAtivosGet;
import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.Procedimento;
import br.gov.jfrj.siga.wf.api.v1.apio.ProcedimentoAPIO;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.util.WfTarefa;

public class AtivosGet implements IAtivosGet {

	@Override
	public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception {
		SortedSet<WfTarefa> tarefas = obterTarefasAtivas(ctx.getTitular(), ctx.getLotaTitular());
		for (WfTarefa t : tarefas) {
			Procedimento r = new ProcedimentoAPIO(t.getInstanciaDeProcedimento());
			resp.list.add(r);
		}
	}

	public static SortedSet<WfTarefa> obterTarefasAtivas(DpPessoa titular, DpLotacao lotaTitular) {
		SortedSet<WfTarefa> tis = new TreeSet<>();
		List<WfProcedimento> pis = WfDao.getInstance().consultarProcedimentosPorPessoaOuLotacao(titular, lotaTitular);
		for (WfProcedimento pi : pis) {
			tis.add(new WfTarefa(pi));
		}
		return tis;
	}

	@Override
	public String getContext() {
		return "obter procedimentos ativos";
	}

}
