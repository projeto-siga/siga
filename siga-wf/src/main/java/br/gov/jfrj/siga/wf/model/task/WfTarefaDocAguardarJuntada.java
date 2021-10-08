package br.gov.jfrj.siga.wf.model.task;

import java.util.Date;
import java.util.Map;

import com.crivano.jflow.Engine;
import com.crivano.jflow.TaskResult;
import com.crivano.jflow.model.enm.TaskResultKind;
import com.crivano.jflow.task.TaskForm;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeDesvio;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeVariavel;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeResponsavel;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeTarefa;
import br.gov.jfrj.siga.wf.util.WfResp;

public class WfTarefaDocAguardarJuntada extends
		TaskForm<WfDefinicaoDeProcedimento, WfDefinicaoDeTarefa, WfResp, WfTipoDeTarefa, WfTipoDeResponsavel, WfDefinicaoDeVariavel, WfDefinicaoDeDesvio, WfProcedimento> {

	@Override
	public String getEvent(WfDefinicaoDeTarefa tarefa, WfProcedimento pi) {
		return pi.getPrincipal() + "|" + pi.getId() + "|" + tarefa.getId();
	}

	@Override
	public TaskResult execute(WfDefinicaoDeTarefa td, WfProcedimento pi, Engine engine) throws Exception {
		if (Utils.empty(pi.getPrincipal()) || Service.getExService().isModeloIncluso(pi.getPrincipal(), td.getRefId(), pi.getEventoData() != null ? pi.getEventoData() : new Date()))
			return new TaskResult(TaskResultKind.DONE, null, null, null, null);
		return new TaskResult(TaskResultKind.PAUSE, null, null, getEvent(td, pi), pi.calcResponsible(td));
	}

	@Override
	public TaskResult resume(WfDefinicaoDeTarefa td, WfProcedimento pi, Integer detourIndex, Map<String, Object> param,
			Engine<?, ?, ?> engine) throws Exception {
		return execute(td, pi, engine);
	}
}
