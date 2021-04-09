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
	AGUARDAR_ASSINATURA_PRINCIPAL("Aguardar Assinatura", "rectangle", WfTarefaAguardarAssinatura.class, true),
	//
	TRAMITAR_PRINCIPAL("Enviar", "rectangle", WfTarefaTramitar.class, true),
	//
	ARQUIVAR_PRINCIPAL("Arquivar", "rectangle", WfTarefaArquivar.class, false),
	//
	INCLUIR_DOCUMENTO("Incluir Documento", "rectangle", WfTarefaIncluirDocumento.class, true),
	//
	FORMULARIO("Formulário", "rectangle", WfTarefaFormulario.class, true),
	//
	DECISAO("Decisão", "diamond", TaskDecision.class, false),
	//
	EXECUTAR("Executar", "rectangle", TaskEval.class, false),
	//
	EMAIL("Email", "folder", TaskEmail.class, true);

	private final String descr;

	private Class<? extends Task> clazz;

	private String graphKind;

	private boolean exigirResponsavel;

	WfTipoDeTarefa(String descr, String graphKind, Class<? extends Task> clazz, boolean exigirResponsavel) {
		this.descr = descr;
		this.graphKind = graphKind;
		this.clazz = clazz;
		this.exigirResponsavel = exigirResponsavel;
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

	public boolean isExigirResponsavel() {
		return exigirResponsavel;
	}
}
