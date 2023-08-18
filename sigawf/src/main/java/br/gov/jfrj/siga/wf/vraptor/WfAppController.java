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

import org.apache.commons.lang.StringUtils;

import com.crivano.jflow.model.enm.ProcessInstanceStatus;
import com.crivano.jflow.model.enm.VariableEditingKind;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.SelecaoGenerica;
import br.gov.jfrj.siga.vraptor.SigaIdDescr;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.vraptor.StringQualquer;
import br.gov.jfrj.siga.vraptor.Transacional;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.bl.WfBL;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.dao.WfDao.ListaETotal;
import br.gov.jfrj.siga.wf.logic.WfPodeEditarVariaveis;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeVariavel;
import br.gov.jfrj.siga.wf.model.WfMov;
import br.gov.jfrj.siga.wf.model.WfMovAnotacao;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.model.enm.WfPrioridade;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDePrincipal;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeVariavel;
import br.gov.jfrj.siga.wf.util.WfDefinicaoDeProcedimentoSelecao;
import br.gov.jfrj.siga.wf.util.WfProcedimentoDaoFiltro;
import br.gov.jfrj.siga.wf.util.WfTarefa;
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

	@Get
	@Path("/app/ativos")
	public void ativos() throws Exception {
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
	@Get
	@Path("/app/inbox")
	public void inbox() throws Exception {
		SortedSet<WfTarefa> tis = new TreeSet<>();
		List<WfProcedimento> pis = dao().consultarProcedimentosPorPessoaOuLotacao(getTitular(), getLotaTitular());
		for (WfProcedimento pi : pis) {
			tis.add(new WfTarefa(pi));
		}
		result.include("tarefas", tis);
	}

	/**
	 * Cria uma instância de processo baseando-se na definição de processo escolhida
	 * pelo usuário.
	 * 
	 * @return
	 * @throws Exception
	 */
	@Get
	@Path("/app/listar-para-iniciar")
	public void listarParaIniciar() throws Exception {
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
	@Get
	@Path("/app/iniciar/{pdId}")
	public void iniciar(Long pdId) throws Exception {
		if (pdId == null)
			throw new RuntimeException();
		WfDefinicaoDeProcedimento pd = dao().consultar(pdId, WfDefinicaoDeProcedimento.class, false);
		result.include("pd", pd);
	}

	/**
	 * Cria uma instância de processo baseando-se na definição de processo escolhida
	 * pelo usuário.
	 * 
	 * Opção Inciar Procedimento (/sigawf/app/iniciar/21), ao clicar em ok executa essa função.
	 * Recebe como parametro o Documento Principal
	 * 
	 * @return
	 * @throws Exception
	 */
	@Transacional
	@Post
	@Path("/app/iniciar/{pdId}")
	public void iniciar(Long pdId, Long tdId, WfTipoDePrincipal tipoDePrincipal, String principal,
			SelecaoGenerica documentoRefSel) throws Exception {
		if (documentoRefSel != null && documentoRefSel.getSigla() != null) {
			principal = documentoRefSel.getSigla();
		}
		if (pdId == null)
			throw new RuntimeException("Identificador da definição de procedimento não encontrado");
		WfDefinicaoDeProcedimento pd = dao().consultar(pdId, WfDefinicaoDeProcedimento.class, false);

		Integer idx = null;
		if (tdId != null) {
			idx = pd.getIndexById(Long.toString(tdId));
			if (idx == null)
				throw new RuntimeException("Identificador da definição de tarefa não encontrado");
		}

		WfProcedimento pi = Wf.getInstance().getBL().criarProcedimento(pdId, idx, getTitular(), getLotaTitular(),
				getIdentidadeCadastrante(), tipoDePrincipal, principal, null, null, true);
		result.redirectTo(this).procedimento(pi.getId().toString());
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
	public void procedimento(String piId) throws Exception {
		WfProcedimento pi;
		if (StringUtils.isNumeric(piId))
			pi = loadTaskInstance(Long.valueOf(piId));
		else
			pi = WfProcedimento.findBySigla(piId);
		List<SigaIdDescr> prioridades = new ArrayList<SigaIdDescr>();

		prioridades.add(new SigaIdDescr(WfPrioridade.MUITO_ALTA, "Muito Alta"));
		prioridades.add(new SigaIdDescr(WfPrioridade.ALTA, "Alta"));
		prioridades.add(new SigaIdDescr(WfPrioridade.MEDIA, "Média"));
		prioridades.add(new SigaIdDescr(WfPrioridade.BAIXA, "Baixa"));
		prioridades.add(new SigaIdDescr(WfPrioridade.MUITO_BAIXA, "Muito Baixa"));
		result.include("prioridades", prioridades);
		result.include("dot", util.getDot(new WfTarefa(pi)));
		result.include("movs", pi.getMovimentacoes());
		result.include("piId", pi.getId());
		result.include("pi", pi);
	}

	/**
	 * Verifica se as tarefas que estão designadas para o usuário estão associadas a
	 * documentos do SIGA-DOC (por intermédio da variável iniciada com o prefixo
	 * "doc_". Se estiver, chama o método addTask(). Este método executado quando a
	 * action "doc.action" for chamada.
	 */
	@Get
	@Path("/app/doc")
	public void doc(String sigla) throws Exception {
		Boolean destaque = true;
		Map<String, List<WfProcedimento>> mobilMap = new TreeMap<String, List<WfProcedimento>>();

		List<WfProcedimento> taskInstances = Wf.getInstance().getBL().getTaskList(sigla);
		for (WfProcedimento pi : taskInstances) {
			if (pi.getStatus() != ProcessInstanceStatus.PAUSED && pi.getStatus() != ProcessInstanceStatus.RESUMING)
				continue;
			String principal = pi.getPrincipal();
			if (principal == null)
				continue;

			if (!mobilMap.containsKey(principal)) {
				mobilMap.put(principal, new ArrayList<WfProcedimento>());
			}
			List<WfProcedimento> tasks = mobilMap.get(principal);
			tasks.add(pi);
		}
		result.include("mobilMap", mobilMap);
		result.include("destaque", destaque);
	}

	/**
	 * 
	 * 
	 * /** Action que finaliza uma tarefa com as variaveis atribuidas. Os valores
	 * são atribuídos às variáveis da tarefa e depois a tarefa é transferida ao
	 * próximo passo. Se a próxima tarefa for para o mesmo ator, esta é exibida
	 * imediatamente.
	 * 
	 * @return
	 * @throws CsisException
	 */
	@Transacional
	@Post
	@Path("/app/procedimento/{piId}/continuar")
	public void continuar(Long piId, String[] campoIdentificador, StringQualquer[] campoValor, Integer indiceDoDesvio,
			String sigla) throws Exception {
		if (indiceDoDesvio != null && indiceDoDesvio == -1) {
			// redireciona para o método salvar quando o desvio é -1
			result.forwardTo(this).salvar(piId, campoIdentificador, campoValor, sigla);
			return;
		}

		String cadastrante = getTitular().getSigla() + "@" + getLotaTitular().getSiglaCompleta();

		WfProcedimento pi = loadTaskInstance(piId);

		WfDefinicaoDeTarefa td = pi.getCurrentTaskDefinition();

		Map<String, Object> param = carregarVariaveis(td, campoIdentificador, campoValor);

		Integer desvio = null;
		if (indiceDoDesvio != null && td.getDetour() != null && td.getDetour().size() > indiceDoDesvio) {
			desvio = indiceDoDesvio;
		}

		Wf.getInstance().getBL().prosseguir(pi.getIdEvent(), desvio, param, getTitular(), getLotaTitular(),
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
		if (pi.getStatus() == ProcessInstanceStatus.PAUSED && pi.getEventoLotacao() != null
				&& pi.getEventoLotacao().equivale(getLotaTitular())) {
			result.redirectTo(this).procedimento(pi.getId().toString());
			return;
		}

		result.redirectTo(this).procedimento(pi.getId().toString());
	}

	@Transacional
	@Post
	@Path("/app/procedimento/{piId}/salvar")
	public void salvar(Long piId, String[] campoIdentificador, StringQualquer[] campoValor, String sigla)
			throws Exception {
		String cadastrante = getTitular().getSigla() + "@" + getLotaTitular().getSiglaCompleta();

		WfProcedimento pi = loadTaskInstance(piId);

		WfDefinicaoDeTarefa td = pi.getCurrentTaskDefinition();

		Map<String, Object> param = carregarVariaveis(td, campoIdentificador, campoValor);

		Wf.getInstance().getBL().salvar(pi, td, param, getTitular(), getLotaTitular(), getIdentidadeCadastrante());

		if (sigla != null)
			result.redirectTo("/../sigaex/app/expediente/doc/exibir?sigla=" + sigla);
		else
			result.redirectTo(this).procedimento(pi.getId().toString());
	}

	@Path("/app/procedimento/{piId}/editar-variaveis")
	public void editarVariaveis(String piId) throws Exception {
		WfProcedimento pi;
		if (StringUtils.isNumeric(piId))
			pi = loadTaskInstance(Long.valueOf(piId));
		else
			pi = WfProcedimento.findBySigla(piId);
		WfPodeEditarVariaveis.afirmar(pi, getTitular(), getLotaTitular());
		WfDefinicaoDeTarefa tdSuper = pi.getDefinicaoDeProcedimento().gerarDefinicaoDeTarefaComTodasAsVariaveis();
		result.include("piId", pi.getId());
		result.include("pi", pi);
		result.include("td", tdSuper);
	}

	@Transacional
	@Post
	@Path("/app/procedimento/{piId}/salvar-variaveis")
	public void salvarVariaveis(Long piId, String[] campoIdentificador, StringQualquer[] campoValor) throws Exception {
		WfProcedimento pi = loadTaskInstance(piId);
		WfPodeEditarVariaveis.afirmar(pi, getTitular(), getLotaTitular());
		WfDefinicaoDeTarefa td = pi.getDefinicaoDeProcedimento().gerarDefinicaoDeTarefaComTodasAsVariaveis();
		Map<String, Object> param = carregarVariaveis(td, campoIdentificador, campoValor);
		Wf.getInstance().getBL().salvar(pi, td, param, getTitular(), getLotaTitular(), getIdentidadeCadastrante());
		result.redirectTo(this).procedimento(pi.getId().toString());
	}

	private Map<String, Object> carregarVariaveis(WfDefinicaoDeTarefa td, String[] campoIdentificador,
			StringQualquer[] campoValor) {
		// TODO Pegar automaticamente

		// WfBL.assertPodeTransferirDocumentosVinculados(new WfTarefa(pi), cadastrante);

		Map<String, Object> param = new HashMap<>();

		if (td.getVariable() != null) {
			for (int n = 0, c = 0; n < campoIdentificador.length; n++) {
				// Associa cada variavel com seu valore especifico
				for (WfDefinicaoDeVariavel variable : td.getVariable()) {
					if (campoIdentificador[n] == null)
						continue;
					if (variable.getIdentifier().equals(campoIdentificador[n])
							&& (variable.getEditingKind() == VariableEditingKind.READ_WRITE
									|| variable.getEditingKind() == VariableEditingKind.READ_WRITE_REQUIRED)) {
						Object value;
						StringQualquer campo = c < campoValor.length ? campoValor[c] : null;
						if (variable.getTipo() == WfTipoDeVariavel.DOC_MOBIL) {
							value = param(variable.getIdentifier() + "_expedienteSel.sigla");
						} else if (variable.getTipo() == WfTipoDeVariavel.PESSOA) {
							value = param(variable.getIdentifier() + "_pessoaSel.sigla");
						} else if (variable.getTipo() == WfTipoDeVariavel.LOTACAO) {
							value = param(variable.getIdentifier() + "_lotacaoSel.sigla");
						} else if (variable.getTipo() == WfTipoDeVariavel.DATE) {
							value = SigaCalendar.converteStringEmData(campo.toString());
							c++;
						} else if (variable.getTipo() == WfTipoDeVariavel.BOOLEAN) {
							value = converterParaBoolean(campo);
							c++;
						} else if (variable.getTipo() == WfTipoDeVariavel.DOUBLE) {
							value = converterParaDouble(campo);
							c++;
						} else if (variable.getTipo() == WfTipoDeVariavel.SELECAO) {
							value = campo;
							c++;
						} else {
							value = campo;
							c++;
						}

						if (value instanceof StringQualquer)
							value = ((StringQualquer) value).toString();

						// TODO: Verifica se as variáveis "required" foram preenchidas
						if (variable.isRequired() && (value == null
								|| (value instanceof String && (((String) value).trim().length() == 0)))) {
							throw new AplicacaoException("O campo " + variable.getTitle() + " deve ser preenchido");
						}

						param.put(campoIdentificador[n], value);
					}
				}
			}
		}
		return param;
	}

	private Boolean converterParaBoolean(StringQualquer campo) {
		if (campo == null)
			return null;
		String s = campo.toString().trim();
		if (s.length() == 0)
			return null;
		return "1".equals(s) || "true".equals(s);
	}

	private Double converterParaDouble(StringQualquer campo) {
		if (campo == null)
			return null;
		String s = campo.toString().trim();
		if (s.length() == 0)
			return null;
		if (s.contains("."))
			s = s.replace(".", "");
		if (s.contains(","))
			s = s.replace(",", ".");
		return Double.parseDouble(s);
	}

	@Transacional
	@Post
	@Path("/app/procedimento/{sigla}/pegar")
	public void pegar(String sigla, String siglaPrincipal) throws Exception {
		WfProcedimento pi = dao().consultarPorSigla(sigla, WfProcedimento.class, null);

		Wf.getInstance().getBL().pegar(pi, getTitular(), getLotaTitular(), getIdentidadeCadastrante());

		if (siglaPrincipal != null) {
			result.redirectTo("/../sigaex/app/expediente/doc/exibir?sigla=" + siglaPrincipal);
			return;
		}
		result.redirectTo(this).procedimento(pi.getId().toString());
	}

	@Transacional
	@Post
	@Path("/app/procedimento/{sigla}/redirecionar")
	public void redirecionar(String sigla, String siglaPrincipal, Long tdId) throws Exception {
		WfProcedimento pi = dao().consultarPorSigla(sigla, WfProcedimento.class, null);

		if (tdId == null)
			throw new RuntimeException("Identificador da definição de tarefa não pode ser nulo");

		Integer idx = pi.getIndexById(Long.toString(tdId));
		if (idx == null)
			throw new RuntimeException("Identificador da definição de tarefa não encontrado");

		Wf.getInstance().getBL().redirecionar(pi, idx, getTitular(), getLotaTitular(), getIdentidadeCadastrante());

		if (siglaPrincipal != null) {
			result.redirectTo("/../sigaex/app/expediente/doc/exibir?sigla=" + siglaPrincipal);
			return;
		}
		result.redirectTo(this).procedimento(pi.getId().toString());
	}

	@Transacional
	@Post
	@Path("/app/procedimento/{sigla}/terminar")
	public void terminar(String sigla, String siglaPrincipal) throws Exception {
		WfProcedimento pi = dao().consultarPorSigla(sigla, WfProcedimento.class, null);

		Wf.getInstance().getBL().terminar(pi, getTitular(), getLotaTitular(), getIdentidadeCadastrante());

		if (siglaPrincipal != null) {
			result.redirectTo("/../sigaex/app/expediente/doc/exibir?sigla=" + siglaPrincipal);
			return;
		}
		result.redirectTo(this).procedimento(pi.getId().toString());
	}

	@Transacional
	@Get
	@Post
	@Path("/app/procedimento/{sigla}/retomar")
	public void retomar(String sigla, String siglaPrincipal) throws Exception {
		WfProcedimento pi = dao().consultarPorSigla(sigla, WfProcedimento.class, null);

		Wf.getInstance().getBL().retomar(pi, getTitular(), getLotaTitular(), getIdentidadeCadastrante());

		if (siglaPrincipal != null) {
			result.redirectTo("/../sigaex/app/expediente/doc/exibir?sigla=" + siglaPrincipal);
			return;
		}
		result.redirectTo(this).procedimento(pi.getId().toString());
	}

	@Transacional
	@Post
	@Path("/app/procedimento/{sigla}/priorizar")
	public void priorizar(String sigla, WfPrioridade prioridade) throws Exception {
		WfProcedimento pi = dao().consultarPorSigla(sigla, WfProcedimento.class, null);

		Wf.getInstance().getBL().priorizar(pi, prioridade, getTitular(), getLotaTitular(), getIdentidadeCadastrante());

		result.redirectTo(this).procedimento(pi.getId().toString());
	}

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
		if ((pi.getEventoPessoa() == null && actor != null)
				|| (pi.getEventoPessoa() != null && (actor == null || !pi.getEventoPessoa().equivale(actor)))) {
			pi.setEventoPessoa(actor);
			alterado = true;
		}

		if ((pi.getEventoLotacao() == null && lotaActor != null) || (pi.getEventoLotacao() != null
				&& (lotaActor == null || !pi.getEventoLotacao().equivale(lotaActor)))) {
			pi.setEventoLotacao(lotaActor);
			alterado = true;
		}

		if (alterado) {
			WfBL.assertPodeTransferirDocumentosVinculados(new WfTarefa(pi), util.getSiglaTitular());
			WfBL.transferirDocumentosVinculados(pi, util.getSiglaTitular());
		}

		pi.setPrioridade(prioridade);

		// TODO Acrescentar uma movimentação de atribuição aqui!

		result.redirectTo(this).procedimento(pi.getId().toString());
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
		result.redirectTo(this).procedimento(id.toString());
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
				result.redirectTo(this).procedimento(id.toString());
			}
		}
		throw new Exception("Movimentação não encontrada.");
	}

	
//	public WfDefinicaoDeProcedimentoSelecao definicaoDeProcedimentoSel = new WfDefinicaoDeProcedimentoSelecao();
//
//	public WfDefinicaoDeProcedimentoSelecao getDefinicaoDeProcedimentoSel() {
//		return definicaoDeProcedimentoSel;
//	}
	
	@Get("app/pesquisar-procedimentos")
	public void pesquisar(Long idDefinicaoDeProcedimento, final Boolean ativos,final int offset) {
//		if (definicaoDeProcedimentoSel == null)
//			definicaoDeProcedimentoSel = new WfDefinicaoDeProcedimentoSelecao();
		
//		assertAcesso("PESQ:Pesquisar");
		if (getCadastrante().isUsuarioExterno() && Prop.isGovSP()) {
			throw new AplicacaoException("Pesquisa indisponível para Usuários Externos.");
		}
		
		getP().setOffset(offset);

		final WfProcedimentoDaoFiltro flt = new WfProcedimentoDaoFiltro();
//		flt.definicaoDeProcedimento = definicaoDeProcedimentoSel.buscarObjeto();
		if (idDefinicaoDeProcedimento != null)
		flt.definicaoDeProcedimento = WfDefinicaoDeProcedimento.AR.findById(idDefinicaoDeProcedimento);
		flt.ativos = ativos;

		int itemsPerPage = 50;
		ListaETotal<WfProcedimento> cet = dao().consultarPorFiltro(flt, offset, itemsPerPage);

//		result.include("definicaoDeProcedimentoSel", definicaoDeProcedimentoSel);
		result.include("ativos", ativos);
		result.include("itemPagina", itemsPerPage);
		result.include("tamanho", cet.total);
		result.include("itens", cet.lista);
		result.include("idDefinicaoDeProcedimento", idDefinicaoDeProcedimento);
		result.include("definicoesDeProcedimentos", dao().listarDefinicoesDeProcedimentos());
	}

	/**
	 * Carrega as informações relativas à tarefa, tais como, a instância da tarefa,
	 * os logs, a prioridade e suas variáveis.
	 * 
	 * @throws Exception
	 */
	private WfProcedimento loadTaskInstance(Long piId) throws Exception {
		WfProcedimento pi = dao().consultar(piId, WfProcedimento.class, false);
		return pi;
	}
}
