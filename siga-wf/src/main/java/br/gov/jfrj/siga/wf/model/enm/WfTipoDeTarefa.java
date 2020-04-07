package br.gov.jfrj.siga.wf.model.enm;

import com.crivano.jflow.Task;
import com.crivano.jflow.model.TaskKind;
import com.crivano.jflow.task.TaskDecision;
import com.crivano.jflow.task.TaskEmail;
import com.crivano.jflow.task.TaskEval;

import br.gov.jfrj.siga.wf.model.task.WfTarefaAguardarAssinatura;
import br.gov.jfrj.siga.wf.model.task.WfTarefaArquivar;
import br.gov.jfrj.siga.wf.model.task.WfTarefaFormulario;
import br.gov.jfrj.siga.wf.model.task.WfTarefaIncluirDocumento;
import br.gov.jfrj.siga.wf.model.task.WfTarefaTramitar;

public enum WfTipoDeTarefa implements TaskKind {

	//
	AGUARDAR_ASSINATURA_PRINCIPAL("Aguardar Assinatura", "rectangle", WfTarefaAguardarAssinatura.class),
	//
	TRAMITAR_PRINCIPAL("Enviar", "rectangle", WfTarefaTramitar.class),
	//
	ARQUIVAR_PRINCIPAL("Arquivar", "rectangle", WfTarefaArquivar.class),
	//
	INCLUIR_DOCUMENTO("Incluir Documento", "rectangle", WfTarefaIncluirDocumento.class),
	//
	FORMULARIO("Formulário", "rectangle", WfTarefaFormulario.class),
	//
	DECISAO("Decisão", "diamond", TaskDecision.class),
	//
	EXECUTAR("Executar", "rectangle", TaskEval.class),
	//
	EMAIL("Email", "folder", TaskEmail.class);

	private final String descr;

	private Class<? extends Task> clazz;

	private String graphKind;

	WfTipoDeTarefa(String descr, String graphKind, Class<? extends Task> clazz) {
		this.descr = descr;
		this.graphKind = graphKind;
		this.clazz = clazz;
	}

	@Override
	public String getDescr() {
		return this.descr;
	}

	@Override
	public Class<? extends Task> getClazz() {
		return this.clazz;
	}

	@Override
	public String getGraphKind() {
		return this.graphKind;
	}
}
