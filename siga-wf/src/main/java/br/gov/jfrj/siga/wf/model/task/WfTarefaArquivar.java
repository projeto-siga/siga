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

public class WfTarefaArquivar implements Task<WfDefinicaoDeTarefa, WfProcedimento> {

	@Override
	public TaskResult execute(WfDefinicaoDeTarefa td, WfProcedimento pi, Engine engine) throws Exception {
		if (!Utils.empty(pi.getPrincipal()))
			Service.getExService().arquivarCorrente(pi.getPrincipal(), null, null);
		return new TaskResult(TaskResultKind.DONE, null, null, null, null);
	}

}
