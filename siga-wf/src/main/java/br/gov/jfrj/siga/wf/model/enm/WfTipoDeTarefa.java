package br.gov.jfrj.siga.wf.model.enm;

import com.crivano.jflow.Task;
import com.crivano.jflow.model.TaskKind;
import com.crivano.jflow.task.TaskDecision;
import com.crivano.jflow.task.TaskEval;

import br.gov.jfrj.siga.wf.model.task.WfTarefaArquivar;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocAguardarAssinatura;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocAguardarAuxiliar;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocAguardarJuntada;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocAutuar;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocCriar;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocIncluirCopia;
import br.gov.jfrj.siga.wf.model.task.WfTarefaEmail;
import br.gov.jfrj.siga.wf.model.task.WfTarefaFormulario;
import br.gov.jfrj.siga.wf.model.task.WfTarefaSubprocedimento;
import br.gov.jfrj.siga.wf.model.task.WfTarefaTramitar;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocJuntar;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocAlterarPrincipal;

public enum WfTipoDeTarefa implements TaskKind {
	/*
	 * O código a seguir possibilita adicionar um novo tipo de tarefa nesse padrão:
	 * TAREFA("descr", "grafico", "graphTitle", classe, exigirResponsavel, tramitarPrincipal, suportarVariaveis, suportarDesvios),
	 */
	AGUARDAR_ASSINATURA_PRINCIPAL("Aguardar Assinatura", "rectangle", "Aguardar Assinatura",
			WfTarefaDocAguardarAssinatura.class, true, true, false, false),
	//
	TRAMITAR_PRINCIPAL("Enviar", "rectangle", "Enviar", WfTarefaTramitar.class, true, true, false, false),
	//
	ARQUIVAR_PRINCIPAL("Arquivar", "rectangle", "Arquivar", WfTarefaArquivar.class, false, true, false, false),
	//
	INCLUIR_DOCUMENTO("Incluir Documento", "rectangle", "Incluir Documento", WfTarefaDocAguardarJuntada.class, true, true, false, false),
	//
	INCLUIR_COPIA("Incluir Cópia", "rectangle", "Incluir Cópia", WfTarefaDocIncluirCopia.class, false, true, false, false),
	//
	CRIAR_DOCUMENTO("Criar Documento", "rectangle", "Criar Documento", WfTarefaDocCriar.class, true, true, false, false),
	//
	AUTUAR_DOCUMENTO("Autuar Documento", "rectangle", "Autuar", WfTarefaDocAutuar.class, true, true, false, false),
	//
	FORMULARIO("Tarefa de Usuário", "tab", null, WfTarefaFormulario.class, true, true, true, true),
	//
	DECISAO("Desvio Automático", "diamond", null, TaskDecision.class, false, true, false, true),
	//
	EXECUTAR("Script", "rectangle", null, TaskEval.class, false, true, true, false),
	//
	EMAIL("Enviar E-mail Automático", "folder", null, WfTarefaEmail.class, true, true, false, false),
	//
	SUBPROCEDIMENTO("Subprocedimento", "rectangle", "Subprocedimento", WfTarefaSubprocedimento.class, false, true, false, false),
	//
	JUNTAR("Juntar", "rectangle", "Juntar", WfTarefaDocJuntar.class, true, true, false, false),
	//
	ALTERAR_PRINCIPAL("Alterar Principal", "rectangle", "Alterar Principal", WfTarefaDocAlterarPrincipal.class, true, true, false, false),
	//
	INCLUIR_AUXILIAR("Incluir Auxiliar", "rectangle", "Incluir Auxiliar", WfTarefaDocAguardarAuxiliar.class, true, true, false, false);
	//
	

	private final String descr;

	private Class<? extends Task> clazz;

	private String graphKind;
	private String graphTitle;

	private boolean exigirResponsavel;
	
	private boolean tramitarPrincipal;

	private boolean suportarVariaveis;
	private boolean suportarDesvios;

	WfTipoDeTarefa(String descr, String graphKind, String graphTitle, Class<? extends Task> clazz,
			boolean exigirResponsavel, boolean tramitarPrincipal, boolean suportarVariaveis, boolean suportarDesvios) {
		this.descr = descr;
		this.graphKind = graphKind;
		this.graphTitle = graphTitle;
		this.clazz = clazz;
		this.exigirResponsavel = exigirResponsavel;
		this.tramitarPrincipal = tramitarPrincipal;
		this.suportarVariaveis = suportarVariaveis;
		this.suportarDesvios = suportarDesvios;
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

	public boolean isSuportarVariaveis() {
		return suportarVariaveis;
	}

	public boolean isSuportarDesvios() {
		return suportarDesvios;
	}
}
