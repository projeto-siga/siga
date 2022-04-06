package br.gov.jfrj.siga.wf.model.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.crivano.jflow.Engine;
import com.crivano.jflow.TaskResult;
import com.crivano.jflow.model.enm.TaskResultKind;
import com.crivano.jflow.task.TaskForm;

import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeDesvio;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeVariavel;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeResponsavel;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeTarefa;
import br.gov.jfrj.siga.wf.util.WfResp;

public class WfTarefaSubprocedimento extends
		TaskForm<WfDefinicaoDeProcedimento, WfDefinicaoDeTarefa, WfResp, WfTipoDeTarefa, WfTipoDeResponsavel, WfDefinicaoDeVariavel, WfDefinicaoDeDesvio, WfProcedimento> {

	@Override
	public String getEvent(WfDefinicaoDeTarefa tarefa, WfProcedimento pi) {
		return getSiglaDoSubprocedimentoCriado(pi) + "|" + pi.getId() + "|" + tarefa.getId();
	}

	@Override
	public TaskResult execute(WfDefinicaoDeTarefa td, WfProcedimento pi, Engine engine) throws Exception {
		long pdId = td.getRefId();
		pdId = WfDefinicaoDeProcedimento.AR.findById(pdId).getAtual().getId();
		List<String> keys = new ArrayList<>();
		List<Object> values = new ArrayList<>();
		pi.getVariable().keySet().stream().forEach(t -> {
			keys.add(t);
			values.add(pi.getVariable().get(t));
		});
		WfProcedimento subpi = Wf.getInstance().getBL().criarProcedimento(pdId, null, pi.getTitular(),
				pi.getLotaTitular(), null, pi.getTipoDePrincipal(), pi.getPrincipal(), keys, values, false);
		pi.getVariable().put(getIdentificadorDaVariavel(pi.getDefinicaoDeTarefaCorrente()), subpi.getSigla());
		return resume(td, pi, null, null, engine);
	}

	public static String getIdentificadorDaVariavel(WfDefinicaoDeTarefa td) {
		String s = Texto.slugify(td.getNome(), true, true);
		if (!s.startsWith("wf_"))
			s = "wf_" + s;
		return s;
	}

	public static String getSiglaDoSubprocedimentoCriado(WfProcedimento pi) {
		return (String) pi.getVariable().get(getIdentificadorDaVariavel(pi.getDefinicaoDeTarefaCorrente()));
	}

	@Override
	public TaskResult resume(WfDefinicaoDeTarefa td, WfProcedimento pi, Integer detourIndex, Map<String, Object> param,
			Engine<?, ?, ?> engine) throws Exception {
		String siglaDoSubprocedimentoCriado = getSiglaDoSubprocedimentoCriado(pi);
		WfProcedimento subpi = WfProcedimento.findBySigla(siglaDoSubprocedimentoCriado);
		if (!subpi.isFinalizado())
			return new TaskResult(TaskResultKind.PAUSE, null, null, getEvent(td, pi), pi.calcResponsible(td));
		return new TaskResult(TaskResultKind.DONE, null, null, null, null);
	}

}
