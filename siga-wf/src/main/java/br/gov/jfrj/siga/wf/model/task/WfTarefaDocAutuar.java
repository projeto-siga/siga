package br.gov.jfrj.siga.wf.model.task;

import java.util.Map;

import com.crivano.jflow.Engine;
import com.crivano.jflow.TaskResult;
import com.crivano.jflow.model.enm.TaskResultKind;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfProcedimento;

public class WfTarefaDocAutuar extends WfTarefaDocCriar {

	@Override
	public String getSiglaMobilPai(WfProcedimento pi) {
		return null;
	}

	@Override
	public String getSiglaMobilFilho(WfProcedimento pi) {
		return pi.getPrincipal();
	}

	@Override
	public TaskResult resume(WfDefinicaoDeTarefa td, WfProcedimento pi, Integer detourIndex, Map<String, Object> param,
			Engine<?, ?, ?> engine) throws Exception {
		String siglaDoDocumentoCriado = getSiglaDoDocumentoCriado(pi);

		// Caso o documento tenha sido finalizado, atualiza a variável
		if (siglaDoDocumentoCriado.startsWith("TMP")) {
			String siglaAtualDoDocumento = Service.getExService().obterSiglaAtual(siglaDoDocumentoCriado);
			if (!siglaDoDocumentoCriado.equals(siglaAtualDoDocumento)) {
				pi.getVariable().put(getIdentificadorDaVariavel(pi.getDefinicaoDeTarefaCorrente()), siglaAtualDoDocumento);
				siglaDoDocumentoCriado = siglaAtualDoDocumento;
			}
		}
		
		if (isAguardarAssinatura(td) && !Service.getExService().isAssinado(siglaDoDocumentoCriado, null))
			return new TaskResult(TaskResultKind.PAUSE, null, null, getEvent(td, pi), pi.calcResponsible(td));
		if (isAguardarJuntada(td) && !Service.getExService().isJuntado(getSiglaMobilFilho(pi), siglaDoDocumentoCriado))
			return new TaskResult(TaskResultKind.PAUSE, null, null, getEvent(td, pi), pi.calcResponsible(td));
		pi.setPrincipal(getSiglaDoDocumentoCriado(pi));
		return new TaskResult(TaskResultKind.DONE, null, null, null, null);
	}

}
