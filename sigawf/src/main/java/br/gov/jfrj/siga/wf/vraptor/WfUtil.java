package br.gov.jfrj.siga.wf.vraptor;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jbpm.context.def.VariableAccess;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.graph.node.TaskNode;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.PooledActor;
import org.jbpm.taskmgmt.exe.SwimlaneInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

import br.com.caelum.vraptor.ioc.Component;
import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.util.WfAssignmentHandler;
import br.gov.jfrj.siga.wf.util.WfGraphFactory;
import br.gov.jfrj.siga.wf.util.WfTaskVO;

@Component
public class WfUtil {
	protected Map<String, String> mapDescricao = new TreeMap<String, String>();

	private SigaObjects so;

	public WfUtil(SigaObjects so) {
		super();
		this.so = so;
	}

	/**
	 * Retorna a sigla do ator a quem estï¿½ atribuida a tarefa, ou a sigla da
	 * lotaï¿½ï¿½o se a tarefa estiver no pool. Se a tarefa estiver com o titular,
	 * retorna "(minha)"
	 * 
	 * 
	 * @param ti
	 * @return
	 */
	private String getAtendente(TaskInstance ti) {
		if (ti.getActorId() != null) {
			if (ti.getActorId().equals(so.getTitular().getSigla()))
				return "(minha)";
			else
				return ti.getActorId();
		}
		if (ti.getPooledActors().size() == 0)
			return "";
		String s = ((PooledActor) ti.getPooledActors().toArray()[0])
				.getActorId();
		if (s.startsWith(so.getLotaTitular().getOrgaoUsuario().getSigla()))
			s = s.substring(so.getLotaTitular().getOrgaoUsuario().getSigla()
					.length());
		return s;
	}

	public String getDescricao(String sigla) {
		if (mapDescricao.containsKey(sigla)) {
			return mapDescricao.get(sigla);
		}

		DpLotacao lot = so.daoLot(so.getLotaTitular().getOrgaoUsuario()
				.getSigla()
				+ sigla);
		if (lot != null) {
			mapDescricao.put(sigla, lot.getDescricao());
			return lot.getDescricao();
		}

		DpPessoa pes = so.daoPes(sigla);
		if (pes != null) {
			mapDescricao.put(sigla, pes.getDescricao());
			return pes.getDescricao();
		}
		
		return "";
	}

	/**
	 * Retorna a sigla do primeiro ator que faï¿½a parte do pool de atores na task
	 * instance.
	 * 
	 * @param ti
	 * @return
	 */
	public String getPooledActors(TaskInstance ti) {
		if (ti.getActorId() != null)
			return ti.getActorId();
		if (ti.getPooledActors().size() == 0)
			return "";
		String s = ((PooledActor) ti.getPooledActors().toArray()[0])
				.getActorId();
		if (s.startsWith(so.getLotaTitular().getOrgaoUsuario().getSigla()))
			s = s.substring(so.getLotaTitular().getOrgaoUsuario().getSigla()
					.length());
		return s;
	}

	/**
	 * Inicializa a variï¿½vel "task" para que seus atributos possam ser
	 * visualizados pelas actions.
	 * 
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws Exception
	 * @throws AplicacaoException
	 */
	public WfTaskVO inicializarTaskVO(TaskInstance taskInstance)
			throws IllegalAccessException, InvocationTargetException,
			Exception, AplicacaoException {
		WfTaskVO task = new WfTaskVO(taskInstance, so.getTitular(),
				so.getLotaTitular());
		task.setConhecimentoEditavel(so.getTitular().getSigla()
				.equals(taskInstance.getActorId()));
		return task;
	}

	private String designacao(String actorId, Set pooledActors) {
		if (actorId == null && pooledActors == null)
			return null;
		if (pooledActors == null || pooledActors.size() == 0)
			return actorId;
		String pa = "";
		for (Object a : pooledActors) {
			if (pa.length() > 0)
				pa += "/";
			if (a instanceof PooledActor)
				pa += ((PooledActor) a).getActorId();
		}

		if (actorId == null)
			return pa;

		return actorId + " - " + pa;
	}

	public String getDot(TaskInstance taskInstance)
			throws UnsupportedEncodingException, Exception {
		ProcessInstance pi = taskInstance.getProcessInstance();
		ProcessDefinition pd = pi.getProcessDefinition();
		byte ab[] = pd.getFileDefinition().getBytes("processdefinition.xml");
		String sXML = new String(ab, "UTF-8");

		WfGraphFactory graph = new WfGraphFactory();

		// Build a delegation map
		Map<String, String> map = new HashMap<String, String>();
		// for (TaskInstance ti : ((Collection<TaskInstance>) pi
		// .getTaskMgmtInstance().getTaskInstances())) {
		// if (ti.getSwimlaneInstance() != null)
		// map.put(ti.getTask().getParent().getName(),
		// designacao(ti.getSwimlaneInstance().getActorId(), ti
		// .getSwimlaneInstance().getPooledActors()));
		// else if (ti.getTask().getSwimlane() == null) {
		// map.put(ti.getTask().getParent().getName(),
		// designacao(ti.getActorId(), ti.getPooledActors()));
		// }
		// }

		WfAssignmentHandler ah = new WfAssignmentHandler();
		ExecutionContext ctx = new ExecutionContext(taskInstance.getToken());
		for (Task t : ((Collection<Task>) pd.getTaskMgmtDefinition().getTasks()
				.values())) {
			String designacao = null;
			TaskNode taskNode = t.getTaskNode();
			if (t.getSwimlane() != null) {
				{
					SwimlaneInstance si;
					si = taskInstance.getTaskMgmtInstance()
							.getSwimlaneInstance(t.getSwimlane().getName());
					if (si != null)
						designacao = designacao(si.getActorId(),
								si.getPooledActors());
					if (designacao != null) {
						if (taskNode != null)
							map.put(taskNode.getName(), designacao);
					} else {
						si = new SwimlaneInstance(t.getSwimlane());
						si.setTaskMgmtInstance(taskInstance
								.getTaskMgmtInstance());
						try {
							ctx.setTaskInstance(taskInstance);
							ah.assign(si, ctx);
							designacao = designacao(si.getActorId(),
									si.getPooledActors());
							if (taskNode != null)
								map.put(taskNode.getName(), designacao);
						} catch (AplicacaoException e) {

						}
					}
				}
			} else {
				TaskInstance ti = new TaskInstance();
				ti.setTask(t);
				ti.setTaskMgmtInstance(taskInstance.getTaskMgmtInstance());
				try {
					ctx.setTaskInstance(ti);
					ah.assign(ti, ctx);
					designacao = designacao(ti.getActorId(),
							ti.getPooledActors());
					map.put(taskNode.getName(), designacao);
				} catch (AplicacaoException e) {

				}
			}
		}

		String dot;

		dot = graph.gerarDOT(sXML,
				taskInstance.getTask().getParent().getName(), map);

		dot = dot.replace("\n", " ");
		dot = dot.replace("\r", " ");

		return dot;
	}

	public static void transferirDocumentosVinculados(ProcessInstance pi,
			String siglaTitular) throws Exception {
		ExService service = Service.getExService();
		
		ArrayList<Token> tokens = new ArrayList<Token>();
		pi.getRootToken().collectChildrenRecursively(tokens);
		tokens.add(pi.getRootToken());
		
		ArrayList<TaskInstance> tis = new ArrayList<TaskInstance>();
		for (Token t : tokens) {
			tis.addAll((Collection<TaskInstance>) (pi
				.getTaskMgmtInstance().getSignallingTasks(new ExecutionContext(t))));
		}

		for (TaskInstance ti : tis) {
			if (ti.getTask().getTaskController() != null) {
				List<VariableAccess> variableAccesses = (List<VariableAccess>) ti
						.getTask().getTaskController().getVariableAccesses();
				for (VariableAccess variable : variableAccesses) {
					if (variable.getMappedName().startsWith("doc_")
							&& variable.isReadable() && !variable.isWritable() && !variable.getAccess().toString().contains(WfTaskVO.DISABLE_DOC_FORWARD)) {
						String value = (String) ti.getToken()
								.getProcessInstance().getContextInstance()
								.getVariable(variable.getMappedName());
						if (value != null && value.trim().length() == 0)
							value = null;
						String destino = ti.getActorId();
						
						if (destino != null){
							DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
							flt.setSigla(destino);
							DpPessoa ator = (DpPessoa) WfDao.getInstance()
									.consultarPorSigla(flt);
							if (!atorDeveReexecutarTarefa(ator,ti)){
								destino = null;
							}
						}
						
						if (destino == null && ti.getPooledActors() != null){
							for (PooledActor lot : (Collection<PooledActor>) ti
									.getPooledActors()) {
								destino = "@" + lot.getActorId();
								break;
							}
						}
						if (value != null && destino != null)
							service.transferir(value, destino, siglaTitular, true);
					}
				}
			}
		}
	}
	
	private static boolean atorDeveReexecutarTarefa(DpPessoa ator, TaskInstance ti) {
		if (ti.getPooledActors()!=null && ti.getPooledActors().size() == 1) {
			DpLotacaoDaoFiltro lotflt = new DpLotacaoDaoFiltro();
			lotflt.setSiglaCompleta(((PooledActor) ti.getPooledActors().toArray()[0]).getActorId());
			DpLotacao lotacao = (DpLotacao) WfDao.getInstance().consultarPorSigla(lotflt);
			return !ator.isFechada() && ator.getLotacao().equivale(lotacao);
		} else {
			return false;
		}
	}

	public void assertPodeTransferirDocumentosVinculados(TaskInstance ti,
			String siglaTitular) throws Exception {
		ExService service = Service.getExService();

		if (ti.getTask().getTaskController() != null) {
			List<VariableAccess> variableAccesses = (List<VariableAccess>) ti
					.getTask().getTaskController().getVariableAccesses();
			for (VariableAccess variable : variableAccesses) {
				if (variable.getMappedName().startsWith("doc_")
						&& variable.isReadable()
						&& !variable.isWritable()
						&& !variable.getAccess().toString()
								.contains(WfTaskVO.DISABLE_DOC_FORWARD)) {
					String value = (String) ti.getToken().getProcessInstance()
							.getContextInstance()
							.getVariable(variable.getMappedName());
					if (value != null && value.trim().length() != 0)
						if (!service.podeTransferir(value, siglaTitular, false)) {
							throw new AplicacaoException(
									"A tarefa nï¿½o pode prosseguir porque o documento '"
											+ value
											+ "' nï¿½o pode ser transferido. Por favor, verifique se o documento estï¿½ em sua lotaï¿½ï¿½o e se estï¿½ 'Aguardando andamento'.");
						}
				}
			}
		}
	}

	public boolean assertLotacaoAscendenteOuDescendente(DpLotacao lotAtual,
			DpLotacao lotFutura) throws AplicacaoException {
		if (lotAtual.getIdInicial().equals(lotFutura.getIdInicial()))
			return true;

		// Linha ascendente
		DpLotacao lot = lotAtual;
		while (lot.getLotacaoPai() != null) {
			lot = lot.getLotacaoPai();
			if (lot.getIdInicial().equals(lotFutura.getIdInicial()))
				return true;
		}

		// Descendente direta
		lot = lotFutura;
		while (lot.getLotacaoPai() != null) {
			lot = lot.getLotacaoPai();
			if (lot.getIdInicial().equals(lotAtual.getIdInicial()))
				return true;
		}

		throw new AplicacaoException(
				"A designaï¿½ï¿½o de '"
						+ lotAtual.getSigla()
						+ "' para '"
						+ lotFutura.getSigla()
						+ "' nï¿½o ï¿½ permitida pois sï¿½ sï¿½o aceitas lotaï¿½ï¿½es ascendentes seguindo a linha do organograma ou descendentes diretas.");
	}

	public String getSiglaTitular() {
		return so.getTitular().getSigla() + "@"
				+ so.getLotaTitular().getSiglaCompleta();
	}

	/**
	 * Monta o objeto WfTaskVO (view object, que serï¿½ usado na interface do
	 * usuï¿½rio).
	 * 
	 * @param taskInstance
	 * @param variableAccesses
	 * @param siglaDoc
	 * @throws Exception
	 */
	public void addTask(Map<String, List<WfTaskVO>> mobilMap,
			TaskInstance taskInstance, String siglaDoc, String sigla)
			throws Exception {
		WfTaskVO task = new WfTaskVO(taskInstance, sigla, so.getTitular(),
				so.getLotaTitular());
		if (!mobilMap.containsKey(siglaDoc)) {
			mobilMap.put(siglaDoc, new ArrayList<WfTaskVO>());
		}
		List<WfTaskVO> tasks = mobilMap.get(siglaDoc);
		tasks.add(task);
	}

}
