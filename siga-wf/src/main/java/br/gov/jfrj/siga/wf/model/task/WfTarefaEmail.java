package br.gov.jfrj.siga.wf.model.task;

import com.crivano.jflow.Engine;
import com.crivano.jflow.Task;
import com.crivano.jflow.TaskResult;
import com.crivano.jflow.model.Responsible;
import com.crivano.jflow.model.enm.TaskResultKind;

import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.util.WfHandler;
import br.gov.jfrj.siga.wf.util.WfResp;

public class WfTarefaEmail implements Task<WfDefinicaoDeTarefa, WfProcedimento> {

	@Override
	public TaskResult execute(WfDefinicaoDeTarefa td, WfProcedimento pi, Engine engine) throws Exception {
		String subject = engine.getHandler().evalTemplate(pi, td.getSubject());
		String text = engine.getHandler().evalTemplate(pi, td.getText());
		WfResp responsible = (WfResp) pi.calcResponsible(td);
		((WfHandler)engine.getHandler()).sendEmail(responsible, td.getEmail(), subject, text);
		return new TaskResult(TaskResultKind.DONE, null, null, null, null);
	}

}
