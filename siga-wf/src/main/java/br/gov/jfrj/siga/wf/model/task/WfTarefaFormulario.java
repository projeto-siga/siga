package br.gov.jfrj.siga.wf.model.task;

import com.crivano.jflow.task.TaskForm;

import br.gov.jfrj.siga.wf.model.WfDefinicaoDeDesvio;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeVariavel;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeResponsavel;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeTarefa;
import br.gov.jfrj.siga.wf.util.WfResp;

public class WfTarefaFormulario extends
		TaskForm<WfDefinicaoDeProcedimento, WfDefinicaoDeTarefa, WfResp, WfTipoDeTarefa, WfTipoDeResponsavel, WfDefinicaoDeVariavel, WfDefinicaoDeDesvio, WfProcedimento> {

	@Override
	public String getEvent(WfDefinicaoDeTarefa tarefa, WfProcedimento pi) {
		return pi.getPrincipal() + "|" + pi.getId() + "|" + tarefa.getId();
	}

}
