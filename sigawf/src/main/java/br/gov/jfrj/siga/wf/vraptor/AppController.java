package br.gov.jfrj.siga.wf.vraptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.jbpm.context.def.VariableAccess;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.Comment;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.logging.log.ProcessLog;
import org.jbpm.taskmgmt.exe.PooledActor;
import org.jbpm.taskmgmt.exe.TaskInstance;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;
import br.gov.jfrj.siga.vraptor.SigaIdDescr;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.vraptor.StringQualquer;
import br.gov.jfrj.siga.wf.WfConhecimento;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.util.WfContextBuilder;
import br.gov.jfrj.siga.wf.util.WfTaskVO;

@Resource
@SuppressWarnings("unchecked")
public class AppController extends WfController {

	private static final String WF_REDIRECT_TO = "wf_redirect_to";

	public AppController(HttpServletRequest request, Result result, WfDao dao,
			SigaObjects so, WfUtil util) {
		super(request, result, dao, so, util);
	}

	public void resumo() throws Exception {
		inbox();
	}

	/**
	 * Coloca as tarefas que estão para o usuário no atributo "taskInstances".
	 * Além disso, coloca os procedimentos que podem ser iniciados pelo usuário
	 * do sistema no atributo "processDefinitions".Este método é executando ao
	 * ser chamada a action "inbox.action".
	 * 
	 * @return
	 * @throws Exception
	 */
	public void inbox() throws Exception {
		SortedSet<TaskInstance> taskInstances = Wf.getInstance().getBL()
				.getTaskList(getCadastrante(), getTitular(), getLotaTitular());
		Map<TaskInstance, String> pooledActors = new HashMap<TaskInstance, String>();
		Map<String, String> pooledActorsDescricao = new HashMap<String, String>();
		for (TaskInstance ti : taskInstances) {
			String sigla = util.getPooledActors(ti);
			pooledActors.put(ti, sigla);
			pooledActorsDescricao.put(sigla, util.getDescricao(sigla));
		}

		result.include("taskInstances", taskInstances);
		result.include("pooledActors", pooledActors);
		result.include("pooledActorsDescricao", pooledActorsDescricao);
	}

	/**
	 * Cria uma instância de processo baseando-se na definição de processo
	 * escolhida pelo usuário.
	 * 
	 * @return
	 * @throws Exception
	 */
	@Path("/app/initializeProcess/{pdId}")
	public void initializeProcess(Long pdId) throws Exception {
		if (pdId == null)
			throw new RuntimeException();
		ProcessInstance pi = Wf
				.getInstance()
				.getBL()
				.createProcessInstance(pdId, getCadastrante(),
						getCadastrante().getLotacao(), getTitular(),
						getLotaTitular(), null, null, true);
		for (TaskInstance ti : ((List<TaskInstance>) WfContextBuilder
				.getJbpmContext().getJbpmContext()
				.getTaskList(getTitular().getSigla()))) {
			if (ti.getProcessInstance().getId() == pi.getId()) {
				Long tiId = ti.getId();
				result.redirectTo(this).task(tiId);
				return;
			}
		}
		redirectToHome();
	}

	/**
	 * Prove uma url de teste para a inbox do workflow.
	 * 
	 * @return
	 * @throws Exception
	 */
	@Path("/app/testes/gadgetTest")
	public void test(String matricula) throws Exception {
		DpPessoa pes = so.daoPes(matricula);
		so.setTitular(pes);
		so.setLotaTitular(pes.getLotacao());
		result.forwardTo(this).inbox();
	}

	/**
	 * Action que inicializa todos os campos que serão necessários para
	 * finalizar a tarefa especificada pelo Id do ProcessoInstance e o Id do
	 * ProcessDefinition.
	 * 
	 * @return Action.SUCCESS
	 */
	@Path("/app/task/{tiId}")
	public void task(Long tiId) throws Exception {
		TaskInstance taskInstance = loadTaskInstance(tiId);
		List<SigaIdDescr> prioridades = new ArrayList<SigaIdDescr>();
		prioridades.add(new SigaIdDescr(1, "Muito Alta"));
		prioridades.add(new SigaIdDescr(2, "Alta"));
		prioridades.add(new SigaIdDescr(3, "Média"));
		prioridades.add(new SigaIdDescr(4, "Baixa"));
		prioridades.add(new SigaIdDescr(5, "Muito Baixa"));
		result.include("prioridades", prioridades);
		result.include("task", util.inicializarTaskVO(taskInstance));
		result.include("dot", util.getDot(taskInstance));
	}

	/**
	 * Action que finaliza uma tarefa com as variaveis atribuidas. Os valores
	 * são atribuídos às variáveis da tarefa e depois a tarefa é transferida ao
	 * próximo passo. Se a próxima tarefa for para o mesmo ator, esta é exibida
	 * imediatamente.
	 * 
	 * @return
	 * @throws CsisException
	 */
	public void executeTask(Long tiId, String[] fieldNames,
			StringQualquer[] fieldValues, String transitionName, String sigla)
			throws Exception {
		String cadastrante = getTitular().getSigla() + "@"
				+ getLotaTitular().getSiglaCompleta();

		TaskInstance taskInstance = loadTaskInstance(tiId);

		// Pegar automaticamente
		if (taskInstance.getActorId() == null) {
			taskInstance.setActorId(getTitular().getSiglaCompleta(), false);
			carregarAtorEGrupo(taskInstance);
		}

		util.assertPodeTransferirDocumentosVinculados(taskInstance, cadastrante);

		if (taskInstance.getTask().getTaskController() != null) {
			List<VariableAccess> vList = taskInstance.getTask()
					.getTaskController().getVariableAccesses();
			if (fieldNames != null) {
				for (int n = 0, c = 0; n < fieldNames.length; n++) {
					// Associa cada variavel com seu valore especifico
					for (VariableAccess variable : vList) {
						if (variable.getMappedName().equals(fieldNames[n])
								&& variable.isWritable()) {
							Object value;
							if (variable.getMappedName().startsWith("doc_")) {
								value = param(variable.getMappedName()
										+ "_expedienteSel.sigla");
							} else if (variable.getMappedName().startsWith(
									"pes_")) {
								value = param(variable.getMappedName()
										+ "_pessoaSel.sigla");
							} else if (variable.getMappedName().startsWith(
									"lot_")) {
								value = param(variable.getMappedName()
										+ "_lotacaoSel.sigla");
							} else if (variable.getMappedName().startsWith(
									"dt_")) {
								value = SigaCalendar
										.converteStringEmData(fieldValues[c]
												.toString());
								c++;
							} else if (variable.getMappedName().startsWith(
									"sel_")) {
								value = fieldValues[c];
								c++;
							} else {
								value = fieldValues[c];
								c++;

							}
							
							if (value instanceof StringQualquer)
								value = ((StringQualquer) value).toString();
							
							// Verifica se as variáveis "required" foram
							// preenchidas"
							if (variable.isRequired()
									&& (value == null || (value instanceof String && (((String) value)
											.trim().length() == 0)))) {
								throw new AplicacaoException("O campo "
										+ variable.getVariableName()
										+ " deve ser preenchido");
							}

							taskInstance
									.getToken()
									.getProcessInstance()
									.getContextInstance()
									.setVariable(variable.getMappedName(),
											value);
						}
					}
				}
			}
		}

		// Bloco que transfere a tarefa
		if (transitionName != null && transitionName.contains("» ")) {
			transitionName = transitionName.substring(0,
					transitionName.indexOf("» "));
		}
		/*
		 * código inserido para corrigir o caracter &raquo; (») contido no botão
		 * e enviado pelo submit. Este, no caso de autenticação por certificado,
		 * vem em conjunto com o caracter Â. (
		 */
		if (transitionName != null && transitionName.contains("Â» ")) {
			transitionName = transitionName.substring(0,
					transitionName.indexOf("Â» "));
		}
		/*
		 * fim do código inserido para corrigir o caracter &raquo;
		 */
		if (transitionName != null){
			transitionName = transitionName.trim();
			if (transitionName.length() == 0)
				transitionName = null;
		}
		// Transition transition = taskInstance.getTask().getTaskNode()
		// .getLeavingTransition(transitionName);
		boolean prosseguiu = false;
		for (Transition transition : (List<Transition>) (taskInstance
				.getAvailableTransitions())) {
			if ((transition.getName() == null && transitionName == null)
					|| ((transition.getName() != null && transitionName != null) && (transition
							.getName().equals(new String(transitionName
							.getBytes("ISO-8859-1")))))
					|| ((transition.getName() != null && transitionName != null) && (transition
							.getName().equals(new String(transitionName
							.getBytes("UTF-8")))))) {
				taskInstance.end(transition);
				prosseguiu = true;
				break;
			}
		}
		
		//Se não prosseguiu pelos caminhos alternativos, prossiga pelo caminho padrão.
		if (!prosseguiu){
			taskInstance.end();
		}

		WfContextBuilder.getJbpmContext().getJbpmContext()
				.save(taskInstance.getToken().getProcessInstance());

		// Service.getExService ().transferir ( "RJ-MEM-2007/00595-A" ,
		// "@RJSCO" , "RJ13635@RJSESAD" );

		util.transferirDocumentosVinculados(taskInstance.getProcessInstance(),
				cadastrante);

		// Redireciona para a pagina escolhida quando o procedimento criar uma
		// variavel pre-definida
		//
		String redirectTo = (String) taskInstance.getVariable(WF_REDIRECT_TO);
		if (redirectTo != null) {
			// taskInstance.deleteVariable(WF_REDIRECT_TO);
			WfDao.getInstance().getSessao().flush();
			taskInstance.getProcessInstance().getContextInstance()
					.deleteVariable(WF_REDIRECT_TO);
			WfDao.getInstance().getSessao().flush();
			result.redirectTo(redirectTo);
			return;
		}

		// Apresenta a pagina do documento quando a sigla for fornecida
		//
		if (sigla != null) {
			result.redirectTo("/../sigaex/app/expediente/doc/exibir?sigla="
					+ sigla);
			return;
		}

		// Verificar se um novo task foi criado para o process instance em
		// questão. Se esse
		// task existir e for designado para o mesmo ator, então a próxima
		// página a ser exibida será a página de apresentação do task, e não a
		// página inicial.
		{
			long idpi = taskInstance.getProcessInstance().getId();
			SortedSet<TaskInstance> tasks = Wf
					.getInstance()
					.getBL()
					.getTaskList(getCadastrante(), getTitular(),
							getLotaTitular());
			for (TaskInstance ti : tasks) {
				if (ti.getProcessInstance().getId() == idpi) {
					result.redirectTo(this).task(ti.getId());
					return;
				}
			}
		}
		redirectToHome();
	}

	public void pegar(Long tiId, String sigla) throws Exception {
		TaskInstance taskInstance = loadTaskInstance(tiId);

		// Pegar
		if (taskInstance.getActorId() == null) {
			taskInstance.setActorId(getTitular().getSiglaCompleta(), false);
			carregarAtorEGrupo(taskInstance);
		}

		if (sigla != null) {
			result.redirectTo("/../sigaex/app/expediente/doc/exibir?sigla="
					+ sigla);
			return;
		}
		result.redirectTo(this).task(tiId);
	}

	/**
	 * Grava o conhecimento a respeito de como uma tarefa deve ser executada.
	 * 
	 * @return
	 * @throws CsisException
	 */
	public void saveKnowledge(Long tiId, String conhecimento) throws Exception {
		TaskInstance taskInstance = getTaskInstance(tiId);

		WfConhecimento cAntigo = ((WfDao) dao).consultarConhecimento(
				taskInstance.getProcessInstance().getProcessDefinition()
						.getName(), taskInstance.getName());

		WfConhecimento cNovo = new WfConhecimento();
		if (cAntigo != null) {
			PropertyUtils.copyProperties(cNovo, cAntigo);
			cNovo.setId(null);
		} else {
			cNovo.setProcedimento(taskInstance.getProcessInstance()
					.getProcessDefinition().getName());
			cNovo.setTarefa(taskInstance.getName());
		}
		cNovo.setDescricao(conhecimento);

		dao.gravarComHistorico(cNovo, cAntigo, null, getIdentidadeCadastrante());

		result.redirectTo(this).task(tiId);
	}

	/**
	 * Action que finaliza uma tarefa com as variaveis atribuidas.
	 * 
	 * @return
	 * @throws CsisException
	 */
	public void assignTask(Long tiId, DpPessoaSelecao ator_pessoaSel,
			DpLotacaoSelecao lotaAtor_lotacaoSel, Integer prioridade,
			String justificativa) throws Exception {
		TaskInstance taskInstance = getTaskInstance(tiId);
		ProcessInstance pi = taskInstance.getProcessInstance();

		String actorId = null;
		String lotaActorId = null;

		if (ator_pessoaSel.getId() != null)
			actorId = daoPes(ator_pessoaSel.getId()).getSigla();
		if (lotaAtor_lotacaoSel.getId() != null)
			lotaActorId = daoLot(lotaAtor_lotacaoSel.getId())
					.getSiglaCompleta();

		if (actorId == null && lotaActorId == null)
			throw new AplicacaoException(
					"Não é permitido designar a tarefa quando tanto a pessoa quanto a lotação estão em branco.");

		DpLotacao lotAtualAtor = null;
		DpLotacao lotAtualPool = null;
		if (taskInstance.getActorId() != null) {
			DpPessoa pesAtual = new DpPessoa();
			pesAtual.setSigla(taskInstance.getActorId());
			lotAtualAtor = dao.consultarPorSigla(pesAtual).getLotacao();
		}
		if (taskInstance.getPooledActors().size() != 0) {
			DpLotacaoDaoFiltro lotflt = new DpLotacaoDaoFiltro();
			lotflt.setSiglaCompleta(((PooledActor) taskInstance
					.getPooledActors().toArray()[0]).getActorId());
			lotAtualPool = (DpLotacao) dao.consultarPorSigla(lotflt);
		}

		boolean fActorChanged = (actorId != null && taskInstance.getActorId() == null)
				|| (actorId == null && taskInstance.getActorId() != null)
				|| (actorId != null && taskInstance.getActorId() != null && !taskInstance
						.getActorId().equals(actorId));

		boolean fPooledActorsChanged = (lotaActorId != null && taskInstance
				.getPooledActors().size() == 0)
				|| (lotaActorId == null && taskInstance.getPooledActors()
						.size() != 0)
				|| (lotaActorId != null
						&& taskInstance.getPooledActors().size() != 0 && !((PooledActor) taskInstance
						.getPooledActors().toArray()[0]).getActorId().equals(
						lotaActorId) || 
						(taskInstance.getSwimlaneInstance() != null && 
						!((PooledActor)taskInstance.getSwimlaneInstance().getPooledActors().toArray()[0]).getActorId().equals(lotaActorId))
					);

		if (fActorChanged || fPooledActorsChanged)
			util.assertPodeTransferirDocumentosVinculados(taskInstance,
					util.getSiglaTitular());

		if (fActorChanged) {
			if (actorId != null) {
//				util.assertLotacaoAscendenteOuDescendente(
//						lotAtualAtor != null ? lotAtualAtor : lotAtualPool,
//						daoPes(ator_pessoaSel.getId()).getLotacao());
			}
			taskInstance.setActorId(actorId);
		}

		if (fPooledActorsChanged) {
			if (lotaActorId == null)
				taskInstance.setPooledActors(new String[] {});
			else {
				//util.assertLotacaoAscendenteOuDescendente(
				//		lotAtualPool != null ? lotAtualPool : lotAtualAtor,
				//		daoLot(lotaAtor_lotacaoSel.getId()));
				taskInstance.setPooledActors(new String[] { lotaActorId });
				if (taskInstance.getSwimlaneInstance() != null){
					//PooledActor pa = (PooledActor) taskInstance.getSwimlaneInstance().getPooledActors().iterator().next();
					//pa.setActorId(lotaActorId);
					taskInstance.getSwimlaneInstance().setPooledActors(new String[] {lotaActorId});
				}else{
				}
			}
		}
		if (fActorChanged || fPooledActorsChanged)
			util.transferirDocumentosVinculados(pi, util.getSiglaTitular());

		taskInstance.setPriority(prioridade);

		if (fActorChanged) {
			Comment c = new Comment(
					getCadastrante().getSigla(),
					"Tarefa designada para "
							+ (taskInstance.getActorId() != null ? "pessoa "
									+ taskInstance.getActorId() + "."
									: " nenhuma pessoa.")
							+ (justificativa != null
									&& justificativa.trim().length() > 0 ? " Justificativa: "
									+ justificativa + "."
									: ""));
			taskInstance.addComment(c);
		}

		if (fPooledActorsChanged) {
			Comment c = new Comment(
					getCadastrante().getSigla(),
					"Tarefa designada para "
							+ (taskInstance.getPooledActors().size() != 0 ? "lotação "
									+ ((PooledActor) taskInstance
											.getPooledActors().toArray()[0])
											.getActorId() + "."
									: " nenhuma lotação.")
							+ (justificativa != null
									&& justificativa.trim().length() > 0 ? " Justificativa: "
									+ justificativa + "."
									: ""));
			taskInstance.addComment(c);
		}

		result.redirectTo(this).task(taskInstance.getId());
	}

	/**
	 * Insere um comentário à tarefa.
	 * 
	 * @return
	 * @throws CsisException
	 */
	public void commentTask(Long tiId, String comentario) throws Exception {
		TaskInstance taskInstance = getTaskInstance(tiId);
		if (comentario != null && comentario.trim().length() > 0) {
			Comment c = new Comment(getCadastrante().getSigla(), comentario);
			taskInstance.addComment(c);
		}
		result.redirectTo(this).task(taskInstance.getId());
	}

	/**
	 * Verifica se as tarefas que estão designadas para o usuário estão
	 * associadas a documentos do SIGA-DOC (por intermédio da variável iniciada
	 * com o prefixo "doc_". Se estiver, chama o método addTask(). Este método
	 * executado quando a action "doc.action" for chamada.
	 */
	public void doc(String sigla) throws Exception {
		Map<String, List<WfTaskVO>> mobilMap = new TreeMap<String, List<WfTaskVO>>();

		SortedSet<TaskInstance> taskInstances = Wf.getInstance().getBL()
				.getTaskList(sigla);
		for (TaskInstance ti : taskInstances) {
			if (ti.getTask().getTaskController() != null) {
				List<VariableAccess> variableAccesses = (List<VariableAccess>) ti
						.getTask().getTaskController().getVariableAccesses();

				for (VariableAccess _variable : variableAccesses) {
					String name = _variable.getMappedName();
					if (name != null && name.startsWith("doc_")) {
						String value = (String) ti.getContextInstance()
								.getVariable(name);
						if (value != null) {
							if (value.startsWith(sigla)) {
								util.addTask(mobilMap, ti, value, sigla);
							}
						}
					}
				}
			}
		}
		result.include("mobilMap", mobilMap);
	}

	private TaskInstance getTaskInstance(Long tiId) throws Exception {
		TaskInstance taskInstance = WfContextBuilder.getJbpmContext()
				.getJbpmContext().getTaskInstance(tiId);
		if (taskInstance == null)
			throw new Exception("Atividade indisponível.");
		return taskInstance;
	}

	/**
	 * Carrega as informações relativas à tarefa, tais como, a instância da
	 * tarefa, os logs, a prioridade e suas variáveis.
	 * 
	 * @throws Exception
	 */
	private TaskInstance loadTaskInstance(Long tiId) throws Exception {
		TaskInstance taskInstance = getTaskInstance(tiId);

		List<ProcessLog> logs = (List<ProcessLog>) WfContextBuilder
				.getJbpmContext().getJbpmContext().getLoggingSession()
				.findLogsByToken(taskInstance.getToken().getId());

		int prioridade = taskInstance.getPriority();
		carregarAtorEGrupo(taskInstance);

		result.include("taskInstance", taskInstance);
		result.include("logs", logs);
		result.include("prioridade", prioridade);

		return taskInstance;
	}

	/**
	 * Carrega a pessoa e a lotação designados à terefa.
	 * 
	 * @param taskInstance
	 * 
	 * @throws AplicacaoException
	 */
	private void carregarAtorEGrupo(TaskInstance taskInstance)
			throws AplicacaoException {
		DpPessoaSelecao atorSel = new DpPessoaSelecao();
		DpLotacaoSelecao lotaAtorSel = new DpLotacaoSelecao();

		if (taskInstance.getActorId() != null) {
			atorSel.setSigla(taskInstance.getActorId());
			atorSel.buscarPorSigla();
		}
		if (taskInstance.getPooledActors().size() != 0) {
			String actorId = (String) ((PooledActor) (taskInstance
					.getPooledActors().toArray()[0])).getActorId();
			if (actorId != null) {
				lotaAtorSel.setSigla(actorId);
				lotaAtorSel.buscarPorSigla();
			}
		}
		result.include("atorSel", atorSel);
		result.include("lotaAtorSel", lotaAtorSel);
	}

}
