package br.gov.jfrj.siga.wf.model.enm;

import com.crivano.jflow.Task;
import com.crivano.jflow.model.TaskKind;
import com.crivano.jflow.task.TaskDecision;
import com.crivano.jflow.task.TaskEmail;
import com.crivano.jflow.task.TaskEval;

import br.gov.jfrj.siga.wf.model.task.WfTarefaArquivar;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocAguardarAssinatura;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocAguardarJuntada;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocAutuar;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocCriar;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocIncluirCopia;
import br.gov.jfrj.siga.wf.model.task.WfTarefaFormulario;
import br.gov.jfrj.siga.wf.model.task.WfTarefaTramitar;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocAguardarAuxiliar;

public enum WfTipoDeTarefa implements TaskKind {

	//
	AGUARDAR_ASSINATURA_PRINCIPAL("Aguardar Assinatura", "rectangle", "Aguardar Assinatura",
			WfTarefaDocAguardarAssinatura.class, true),
	//
	TRAMITAR_PRINCIPAL("Enviar", "rectangle", "Enviar", WfTarefaTramitar.class, true),
	//
	ARQUIVAR_PRINCIPAL("Arquivar", "rectangle", "Arquivar", WfTarefaArquivar.class, false),
	//
	INCLUIR_DOCUMENTO("Incluir Documento", "rectangle", "Incluir Documento", WfTarefaDocAguardarJuntada.class, true),
	//
	INCLUIR_COPIA("Incluir Cópia", "rectangle", "Incluir Cópia", WfTarefaDocIncluirCopia.class, false),
	//
	CRIAR_DOCUMENTO("Criar Documento", "rectangle", "Criar Documento", WfTarefaDocCriar.class, true),
	//
	AUTUAR_DOCUMENTO("Autuar Documento", "rectangle", "Autuar", WfTarefaDocAutuar.class, true),
	//
	FORMULARIO("Formulário", "tab", null, WfTarefaFormulario.class, true),
	//
	DECISAO("Decisão", "diamond", null, TaskDecision.class, false),
	//
	EXECUTAR("Executar", "rectangle", null, TaskEval.class, false),
	//
	EMAIL("Email", "folder", null, TaskEmail.class, true),
	//
	INCLUIR_AUXILIAR("Incluir Auxiliar", "rectangle", "Incluir Auxiliar", WfTarefaDocAguardarAuxiliar.class, true);


	private final String descr;

	private Class<? extends Task> clazz;

	private String graphKind;
	private String graphTitle;

	private boolean exigirResponsavel;

	WfTipoDeTarefa(String descr, String graphKind, String graphTitle, Class<? extends Task> clazz,
			boolean exigirResponsavel) {
		this.descr = descr;
		this.graphKind = graphKind;
		this.graphTitle = graphTitle;
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

	@Override
	public String getGraphTitle() {
		return this.graphTitle;
	}

	public boolean isExigirResponsavel() {
		return exigirResponsavel;
	}
}
