package br.gov.jfrj.siga.wf.model.enm;

import com.crivano.jflow.Task;
import com.crivano.jflow.model.TaskKind;
import com.crivano.jflow.task.TaskDecision;
import com.crivano.jflow.task.TaskEmail;
import com.crivano.jflow.task.TaskEval;

import br.gov.jfrj.siga.wf.model.task.WfTarefaArquivar;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocAguardarAssinatura;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocAguardarAuxiliar;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocAguardarJuntada;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocAutuar;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocCriar;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocIncluirCopia;
import br.gov.jfrj.siga.wf.model.task.WfTarefaFormulario;
import br.gov.jfrj.siga.wf.model.task.WfTarefaSubprocedimento;
import br.gov.jfrj.siga.wf.model.task.WfTarefaTramitar;

public enum WfTipoDeTarefa implements TaskKind {

	//
	AGUARDAR_ASSINATURA_PRINCIPAL("Aguardar Assinatura", "rectangle", "Aguardar Assinatura",
			WfTarefaDocAguardarAssinatura.class, true, true),
	//
	TRAMITAR_PRINCIPAL("Enviar", "rectangle", "Enviar", WfTarefaTramitar.class, true, true),
	//
	ARQUIVAR_PRINCIPAL("Arquivar", "rectangle", "Arquivar", WfTarefaArquivar.class, false, true),
	//
	INCLUIR_DOCUMENTO("Incluir Documento", "rectangle", "Incluir Documento", WfTarefaDocAguardarJuntada.class, true, true),
	//
	INCLUIR_COPIA("Incluir Cópia", "rectangle", "Incluir Cópia", WfTarefaDocIncluirCopia.class, false, true),
	//
	CRIAR_DOCUMENTO("Criar Documento", "rectangle", "Criar Documento", WfTarefaDocCriar.class, true, true),
	//
	AUTUAR_DOCUMENTO("Autuar Documento", "rectangle", "Autuar", WfTarefaDocAutuar.class, true, false),
	//
	FORMULARIO("Formulário", "tab", null, WfTarefaFormulario.class, true, true),
	//
	DECISAO("Decisão", "diamond", null, TaskDecision.class, false, true),
	//
	EXECUTAR("Executar", "rectangle", null, TaskEval.class, false, true),
	//
	EMAIL("Email", "folder", null, TaskEmail.class, true, true),
	//
	SUBPROCEDIMENTO("Subprocedimento", "rectangle", "Subprocedimento", WfTarefaSubprocedimento.class, false, true),
	//
	INCLUIR_AUXILIAR("Incluir Auxiliar", "rectangle", "Incluir Auxiliar", WfTarefaDocAguardarAuxiliar.class, true, true);


	private final String descr;

	private Class<? extends Task> clazz;

	private String graphKind;
	private String graphTitle;

	private boolean exigirResponsavel;
	
	private boolean tramitarPrincipal;

	WfTipoDeTarefa(String descr, String graphKind, String graphTitle, Class<? extends Task> clazz,
			boolean exigirResponsavel, boolean tramitarPrincipal) {
		this.descr = descr;
		this.graphKind = graphKind;
		this.graphTitle = graphTitle;
		this.clazz = clazz;
		this.exigirResponsavel = exigirResponsavel;
		this.tramitarPrincipal = tramitarPrincipal;
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
	
	public boolean isTramitarPrincipal() {
		return tramitarPrincipal;
	}
}
