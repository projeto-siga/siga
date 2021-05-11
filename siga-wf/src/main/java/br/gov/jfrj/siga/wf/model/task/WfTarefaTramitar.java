package br.gov.jfrj.siga.wf.model.task;

import com.crivano.jflow.Engine;
import com.crivano.jflow.Task;
import com.crivano.jflow.TaskResult;
import com.crivano.jflow.model.enm.TaskResultKind;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.parser.SiglaParser;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.util.WfResp;

public class WfTarefaTramitar implements Task<WfDefinicaoDeTarefa, WfProcedimento> {

	@Override
	public TaskResult execute(WfDefinicaoDeTarefa td, WfProcedimento pi, Engine engine) throws Exception {
		if (!Utils.empty(pi.getPrincipal())) {
			WfResp destino = pi.calcResponsible(td);
			String siglaDestino = SiglaParser.makeSigla(destino.getPessoa(), destino.getLotacao());
			Service.getExService().transferir(pi.getPrincipal(), siglaDestino, null, true);
		}
		return new TaskResult(TaskResultKind.DONE, null, null, null, null);
	}

}
