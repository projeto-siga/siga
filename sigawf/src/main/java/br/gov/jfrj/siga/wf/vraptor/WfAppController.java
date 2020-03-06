package br.gov.jfrj.siga.wf.vraptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.crivano.jflow.model.enm.ProcessInstanceStatus;
import com.crivano.jflow.model.enm.VariableEditingKind;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.vraptor.SigaIdDescr;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.vraptor.StringQualquer;
import br.gov.jfrj.siga.vraptor.Transacional;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeDesvio;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeVariavel;
import br.gov.jfrj.siga.wf.model.WfMov;
import br.gov.jfrj.siga.wf.model.WfMovAnotacao;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.model.enm.WfPrioridade;
import br.gov.jfrj.siga.wf.util.WfTarefa;
import br.gov.jfrj.siga.wf.util.WfTaskVO;
import br.gov.jfrj.siga.wf.util.WfUtil;

@Controller
@SuppressWarnings("unchecked")
public class WfAppController extends WfController {
	private static final String VERIFICADOR_ACESSO = "INI:Iniciar";

	/**
	 * @deprecated CDI eyes only
	 */
	public WfAppController() {
		super();
	}

	@Inject
	public WfAppController(HttpServletRequest request, Result result, WfDao dao, SigaObjects so, WfUtil util) {
		super(request, result, dao, so, util);
	}

	public void resumo() throws Exception {
		inbox();
	}

	public WfDao dao() {
		return (WfDao) dao;
	}

	/**
	 * Coloca as tarefas que estão para o usuário no atributo "taskInstances". Além
	 * disso, coloca os procedimentos que podem ser iniciados pelo usuário do
	 * sistema no atributo "processDefinitions".Este método é executando ao ser
	 * chamada a action "inbox.action".
	 * 
	 * @return
	 * @throws Exception
	 */
	public void inbox() throws Exception {
		SortedSet<WfTarefa> tis = new TreeSet<>();
		List<WfProcedimento> pis = dao().consultarProcedimentosPorLotacao(getLotaTitular());
		for (WfProcedimento pi : pis) {
			tis.add(new WfTarefa(pi));
		}
		result.include("taskInstances", tis);
	}

	/**
	 * Cria uma instância de processo baseando-se na definição de processo escolhida
	 * pelo usuário.
	 * 
	 * @return
	 * @throws Exception
	 */
	@Path("/app/iniciar")
	public void iniciar() throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);
		List<WfDefinicaoDeProcedimento> modelos = dao().listarAtivos(WfDefinicaoDeProcedimento.class, "nome");
		result.include("itens", modelos);
	}

	/**
	 * Cria uma instância de processo baseando-se na definição de processo escolhida
	 * pelo usuário.
	 * 
	 * @return
	 * @throws Exception
	 */
	@Transacional
	@Post
	@Path("/app/iniciar/{pdId}")
	public void iniciar(Long pdId) throws Exception {
		if (pdId == null)
			throw new RuntimeException();
		WfProcedimento pi = Wf.getInstance().getBL().createProcessInstance(pdId, getTitular(), getLotaTitular(),
				getIdentidadeCadastrante(), null, null, true);
		if (pi.getStatus() == ProcessInstanceStatus.PAUSED)
			result.redirectTo(this).procedimento(pi.getId());
		else
			redirectToHome();
	}

	/**
	 * Prove uma url de teste para a inbox do workflow.
	 * 
	 * @return
	 * @throws Exception
	 */
	@Path("/public/app/testes/gadgetTest")
	public void test(String matricula) throws Exception {
		DpPessoa pes = so.daoPes(matricula);
		so.setTitular(pes);
		so.setLotaTitular(pes.getLotacao());
		result.forwardTo(this).inbox();
	}

	/**
	 * Action que inicializa todos os campos que serão necessários para finalizar a
	 * tarefa especificada pelo Id do ProcessoInstance e o Id do ProcessDefinition.
	 * 
	 * @return Action.SUCCESS
	 */
	@Path("/app/procedimento/{piId}")
	public void procedimento(Long piId) throws Exception {
		WfProcedimento pi = loadTaskInstance(piId);
		List<SigaIdDescr> prioridades = new ArrayList<SigaIdDescr>();

//		final DpPessoaSelecao pessoaSel = new DpPessoaSelecao();
//		final DpLotacaoSelecao lotacaoSel = new DpLotacaoSelecao();
//
//		pessoaSel.buscarPorObjeto(pd.getPessoa());

		prioridades.add(new SigaIdDescr(WfPrioridade.MUITO_ALTA, "Muito Alta"));
		prioridades.add(new SigaIdDescr(WfPrioridade.ALTA, "Alta"));
		prioridades.add(new SigaIdDescr(WfPrioridade.MEDIA, "Média"));
		prioridades.add(new SigaIdDescr(WfPrioridade.BAIXA, "Baixa"));
		prioridades.add(new SigaIdDescr(WfPrioridade.MUITO_BAIXA, "Muito Baixa"));
		result.include("prioridades", prioridades);
		result.include("task", util.inicializarTaskVO(new WfTarefa(pi)));
		result.include("dot", util.getDot(new WfTarefa(pi)));
		result.include("movs", pi.getMovimentacoes());
		result.include("piId", pi.getId());
	}

	/**
	 * Action que finaliza uma tarefa com as variaveis atribuidas. Os valores são
	 * atribuídos às variáveis da tarefa e depois a tarefa é transferida ao próximo
	 * passo. Se a próxima tarefa for para o mesmo ator, esta é exibida
	 * imediatamente.
	 * 
	 * @return
	 * @throws CsisException
	 */
	@Transacional
	@Post
	@Path("/app/procedimento/{piId}/continuar")
	public void continuar(Long piId, String[] fieldNames, StringQualquer[] fieldValues, String transitionName,
			String sigla) throws Exception {
		String cadastrante = getTitular().getSigla() + "@" + getLotaTitular().getSiglaCompleta();

		WfProcedimento pi = loadTaskInstance(piId);

		WfDefinicaoDeTarefa td = pi.getCurrentTaskDefinition();

		// TODO Pegar automaticamente

		util.assertPodeTransferirDocumentosVinculados(new WfTarefa(pi), cadastrante);

		Map<String, Object> param = new HashMap<>();

		if (td.getVariable() != null) {
			for (int n = 0, c = 0; n < fieldNames.length; n++) {
				// Associa cada variavel com seu valore especifico
				for (WfDefinicaoDeVariavel variable : td.getVariable()) {
					if (fieldNames[n] == null)
						continue;
					if (variable.getIdentifier().equals(fieldNames[n])
							&& variable.getEditingKind() == VariableEditingKind.READ_WRITE) {
						Object value;
						if (variable.getIdentifier().startsWith("doc_")) {
							value = param(variable.getIdentifier() + "_expedienteSel.sigla");
						} else if (variable.getIdentifier().startsWith("pes_")) {
							value = param(variable.getIdentifier() + "_pessoaSel.sigla");
						} else if (variable.getIdentifier().startsWith("lot_")) {
							value = param(variable.getIdentifier() + "_lotacaoSel.sigla");
						} else if (variable.getIdentifier().startsWith("dt_")) {
							value = SigaCalendar.converteStringEmData(fieldValues[c].toString());
							c++;
						} else if (variable.getIdentifier().startsWith("sel_")) {
							value = fieldValues[c];
							c++;
						} else {
							value = fieldValues[c];
							c++;

						}

						if (value instanceof StringQualquer)
							value = ((StringQualquer) value).toString();

						// TODO: Verifica se as variáveis "required" foram preenchidas
						if (variable.isRequired() && (value == null
								|| (value instanceof String && (((String) value).trim().length() == 0)))) {
							throw new AplicacaoException("O campo " + variable.getTitle() + " deve ser preenchido");
						}

						param.put(fieldNames[n], value);
					}
				}
			}
		}

		Integer desvio = null;
		if (transitionName != null && td.getDetour() != null) {
			int i = 0;
			for (WfDefinicaoDeDesvio detour : td.getDetour()) {
				if (transitionName.equals(detour.getNome())) {
					desvio = i;
					break;
				}
				i++;
			}
		}

		Wf.getInstance().getBL().prosseguir(pi.getEvent(), desvio, param, getTitular(), getLotaTitular(),
				getIdentidadeCadastrante());

		// Redireciona para a pagina escolhida quando o procedimento criar uma
		// variavel pre-definida
		//
//		String redirectTo = (String) taskInstance.getVariable(WF_REDIRECT_TO);
//		if (redirectTo != null) {
//			// taskInstance.deleteVariable(WF_REDIRECT_TO);
//			WfDao.getInstance().getSessao().flush();
//			taskInstance.getProcessInstance().getContextInstance().deleteVariable(WF_REDIRECT_TO);
//			WfDao.getInstance().getSessao().flush();
//			result.redirectTo(redirectTo);
//			return;
//		}

		// Apresenta a pagina do documento quando a sigla for fornecida
		//
		if (sigla != null) {
			result.redirectTo("/../sigaex/app/expediente/doc/exibir?sigla=" + sigla);
			return;
		}

		// Verificar se um novo task foi criado para o process instance em
		// questão. Se esse
		// task existir e for designado para o mesmo ator, então a próxima
		// página a ser exibida será a página de apresentação do task, e não a
		// página inicial.
		if (pi.getStatus() == ProcessInstanceStatus.PAUSED && pi.getLotacao() != null
				&& pi.getLotacao().equivale(getLotaTitular())) {
			result.redirectTo(this).procedimento(pi.getId());
			return;
		}

		result.redirectTo(this).procedimento(pi.getId());
	}

	// TODO: Implementar o "pegar"
//	public void pegar(Long tiId, String sigla) throws Exception {
//		TaskInstance taskInstance = loadTaskInstance(tiId);
//
//		// Pegar
//		if (taskInstance.getActorId() == null) {
//			taskInstance.setActorId(getTitular().getSiglaCompleta(), false);
//			carregarAtorEGrupo(taskInstance);
//		}
//
//		if (sigla != null) {
//			result.redirectTo("/../sigaex/app/expediente/doc/exibir?sigla=" + sigla);
//			return;
//		}
//		result.redirectTo(this).task(tiId);
//	}

	// TODO Pensar se queremos ter o conceito de conhecimento dentro do WF mesmo ou
	// se é melhor usar sempre o GC
	/**
	 * Grava o conhecimento a respeito de como uma tarefa deve ser executada.
	 * 
	 * @return
	 * @throws CsisException
	 */
//	public void saveKnowledge(Long tdId, String conhecimento) throws Exception {
//		TaskInstance taskInstance = getTaskInstance(tiId);
//
//		WfConhecimento cAntigo = ((WfDao) dao).consultarConhecimento(
//				taskInstance.getProcessInstance().getProcessDefinition().getName(), taskInstance.getName());
//
//		WfConhecimento cNovo = new WfConhecimento();
//		if (cAntigo != null) {
//			PropertyUtils.copyProperties(cNovo, cAntigo);
//			cNovo.setId(null);
//		} else {
//			cNovo.setProcedimento(taskInstance.getProcessInstance().getProcessDefinition().getName());
//			cNovo.setTarefa(taskInstance.getName());
//		}
//		cNovo.setDescricao(conhecimento);
//
//		dao.gravarComHistorico(cNovo, cAntigo, null, getIdentidadeCadastrante());
//
//		result.redirectTo(this).task(tiId);
//	}

	/**
	 * Action que finaliza uma tarefa com as variaveis atribuidas.
	 * 
	 * @return
	 * @throws CsisException
	 */
	@Transacional
	public void assignTask(Long piId, DpPessoaSelecao ator_pessoaSel, DpLotacaoSelecao lotaAtor_lotacaoSel,
			WfPrioridade prioridade, String justificativa) throws Exception {
		WfProcedimento pi = dao().consultar(piId, WfProcedimento.class, false);

		DpPessoa actor = ator_pessoaSel.getObjeto();
		DpLotacao lotaActor = lotaAtor_lotacaoSel.getObjeto();

		if (actor == null && lotaActor == null)
			throw new AplicacaoException(
					"Não é permitido designar a tarefa quando tanto a pessoa quanto a lotação estão em branco.");
		if (pi.getStatus() != ProcessInstanceStatus.PAUSED)
			throw new AplicacaoException("Não é permitido designar a tarefa quando o procedimento não está pausado.");

		boolean alterado = false;
		if ((pi.getPessoa() == null && actor != null)
				|| (pi.getPessoa() != null && (actor == null || !pi.getPessoa().equivale(actor)))) {
			pi.setPessoa(actor);
			alterado = true;
		}

		if ((pi.getLotacao() == null && lotaActor != null)
				|| (pi.getLotacao() != null && (lotaActor == null || !pi.getLotacao().equivale(lotaActor)))) {
			pi.setLotacao(lotaActor);
			alterado = true;
		}

		if (alterado) {
			util.assertPodeTransferirDocumentosVinculados(new WfTarefa(pi), util.getSiglaTitular());
			util.transferirDocumentosVinculados(pi, util.getSiglaTitular());
		}

		pi.setPrioridade(prioridade);

		// TODO Acrescentar uma movimentação de atribuição aqui!

		result.redirectTo(this).procedimento(pi.getId());
	}

	/**
	 * Insere um comentário à tarefa.
	 * 
	 * @return
	 * @throws CsisException
	 */
	@Transacional
	@Post
	@Path("/app/anotar")
	public void anotar(Long id, String descrMov) throws Exception {
		WfProcedimento pi = dao().consultar(id, WfProcedimento.class, false);
		Wf.getInstance().getBL().anotar(pi, descrMov, getTitular(), getLotaTitular(), getIdentidadeCadastrante());
		result.redirectTo(this).procedimento(id);
	}

	/**
	 * Insere um comentário à tarefa.
	 * 
	 * @return
	 * @throws CsisException
	 */
	@Transacional
	@Post
	@Path("/app/procedimento/{id}/anotacao/{idMov}/excluir")
	public void anotacaoExcluir(Long id, Long idMov) throws Exception {
		WfProcedimento pi = dao().consultar(id, WfProcedimento.class, false);
		for (WfMov mov : pi.getMovimentacoes()) {
			if (mov.getId().equals(idMov) && mov instanceof WfMovAnotacao) {
				Wf.getInstance().getBL().excluirAnotacao(pi, (WfMovAnotacao) mov);
				result.redirectTo(this).procedimento(id);
			}
		}
		throw new Exception("Movimentação não encontrada.");
	}

	/**
	 * Verifica se as tarefas que estão designadas para o usuário estão associadas a
	 * documentos do SIGA-DOC (por intermédio da variável iniciada com o prefixo
	 * "doc_". Se estiver, chama o método addTask(). Este método executado quando a
	 * action "doc.action" for chamada.
	 */
	public void doc(String sigla) throws Exception {
		Map<String, List<WfTaskVO>> mobilMap = new TreeMap<String, List<WfTaskVO>>();

		SortedSet<WfTarefa> taskInstances = Wf.getInstance().getBL().getTaskList(sigla);
		for (WfTarefa ti : taskInstances) {
			if (ti.getInstanciaDeProcesso().getStatus() != ProcessInstanceStatus.PAUSED)
				continue;
			if (!getTitular().equivale(ti.getInstanciaDeProcesso().getPessoa())
					&& !getLotaTitular().equivale(ti.getInstanciaDeProcesso().getLotacao()))
				continue;
			util.addTask(mobilMap, ti, ti.getInstanciaDeProcesso().getPrincipal(), sigla);
		}
		result.include("mobilMap", mobilMap);

	}

	/**
	 * Carrega as informações relativas à tarefa, tais como, a instância da tarefa,
	 * os logs, a prioridade e suas variáveis.
	 * 
	 * @throws Exception
	 */
	private WfProcedimento loadTaskInstance(Long piId) throws Exception {
		WfProcedimento pi = dao().consultar(piId, WfProcedimento.class, false);

		// List<ProcessLog> logs = (List<ProcessLog>)
		// WfContextBuilder.getJbpmContext().getJbpmContext()
		// .getLoggingSession().findLogsByToken(taskInstance.getToken().getId());

		// int prioridade = taskInstance.getPriority();
		// carregarAtorEGrupo(taskInstance);

		// result.include("taskInstance", taskInstance);
		// result.include("logs", logs);
		// result.include("prioridade", prioridade);

		return pi;
	}
}
