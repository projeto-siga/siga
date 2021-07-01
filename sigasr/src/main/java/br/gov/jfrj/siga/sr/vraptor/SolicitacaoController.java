package br.gov.jfrj.siga.sr.vraptor;

import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.SALVAR_SOLICITACAO_AO_ABRIR;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;

import com.google.gson.Gson;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.download.ByteArrayDownload;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.model.DpCargoSelecao;
import br.gov.jfrj.siga.cp.model.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrArquivo;
import br.gov.jfrj.siga.sr.model.SrAtributoSolicitacao;
import br.gov.jfrj.siga.sr.model.SrAtributoSolicitacaoMap;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrConfiguracaoBL;
import br.gov.jfrj.siga.sr.model.SrConfiguracaoCache;
import br.gov.jfrj.siga.sr.model.SrEtapaSolicitacao;
import br.gov.jfrj.siga.sr.model.SrFormaAcompanhamento;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrLista;
import br.gov.jfrj.siga.sr.model.SrMarca;
import br.gov.jfrj.siga.sr.model.SrMeioComunicacao;
import br.gov.jfrj.siga.sr.model.SrMovimentacao;
import br.gov.jfrj.siga.sr.model.SrPendencia;
import br.gov.jfrj.siga.sr.model.SrPrioridade;
import br.gov.jfrj.siga.sr.model.SrPrioridadeSolicitacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao.SrTarefa;
import br.gov.jfrj.siga.sr.model.SrTipoMotivoEscalonamento;
import br.gov.jfrj.siga.sr.model.SrTipoMotivoFechamento;
import br.gov.jfrj.siga.sr.model.SrTipoMotivoPendencia;
import br.gov.jfrj.siga.sr.model.SrTipoMovimentacao;
import br.gov.jfrj.siga.sr.model.SrTipoPermissaoLista;
import br.gov.jfrj.siga.sr.model.enm.SrTipoDeConfiguracao;
import br.gov.jfrj.siga.sr.model.vo.SrListaVO;
import br.gov.jfrj.siga.sr.model.vo.SrSolicitacaoListaVO;
import br.gov.jfrj.siga.sr.util.SrSolicitacaoFiltro;
import br.gov.jfrj.siga.sr.util.SrViewUtil;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.uteis.PessoaLotaFuncCargoSelecaoHelper;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.vraptor.Transacional;
import edu.emory.mathcs.backport.java.util.Arrays;

@Controller
@Path("/app/solicitacao")
public class SolicitacaoController extends SrController {
	private static final String TITULAR = "titular";
	private static final String ACOES_E_ATENDENTES = "acoesEAtendentes";
	private static final String SOLICITACAO = "solicitacao";
	private static final String TIPOS_PERMISSAO_JSON = "tiposPermissaoJson";
	private static final String CADASTRANTE = "cadastrante";
	private static final String LOTA_TITULAR = "lotaTitular";
	private static final String MOSTRAR_DESATIVADOS = "mostrarDesativados";
	private static final String LISTAS = "listas";
	private static final String SOLICITACAO_LISTA_VO = "solicitacaoListaVO";
	private static final String TIPOS_PERMISSAO = "tiposPermissao";
	private static final String LOCAIS = "locais";
	private static final String LISTA = "lista";
	private static final String ORGAOS = "orgaos";
	private static final String PODE_REMOVER = "podeRemover";
	private static final String PODE_EDITAR = "podeEditar";
	private static final String PODE_PRIORIZAR = "podePriorizar";
	private static final String FILTRO = "filtro";
	private static final String PRIORIDADE_LIST = "prioridadeList";
	private static final String TIPO_MOTIVO_ESCALONAMENTO_LIST = "tipoMotivoEscalonamentoList";

	/**
	 * @deprecated CDI eyes only
	 */
	public SolicitacaoController() {
		super();
	}

	@Inject
	public SolicitacaoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em,
			SrValidator srValidator, Validator validator) throws Throwable {
		super(request, result, dao, so, em, srValidator);

		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
	}

	@SuppressWarnings("unchecked")
	@Path("/listarLista")
	public void listarLista(boolean mostrarDesativados) throws Exception {
		List<CpOrgaoUsuario> orgaos = ContextoPersistencia.em().createQuery("from CpOrgaoUsuario").getResultList();
		List<CpComplexo> locais = CpComplexo.AR.all().fetch();
		List<SrTipoPermissaoLista> tiposPermissao = SrTipoPermissaoLista.AR.all().fetch();
		List<SrLista> listas = SrLista.listar(mostrarDesativados);
		String tiposPermissaoJson = new Gson().toJson(tiposPermissao);

		result.include(ORGAOS, orgaos);
		result.include(LOCAIS, locais);
		result.include(TIPOS_PERMISSAO, tiposPermissao);
		result.include(LISTAS, listas);
		result.include(MOSTRAR_DESATIVADOS, mostrarDesativados);
		result.include(LOTA_TITULAR, getLotaTitular());
		result.include(CADASTRANTE, getCadastrante());
		result.include(TIPOS_PERMISSAO_JSON, tiposPermissaoJson);
		result.include("prioridades", SrPrioridade.getValoresEmOrdem());

		PessoaLotaFuncCargoSelecaoHelper.adicionarCamposSelecao(result);

		result.include("lotacaolotacaoAtualSel", new DpLotacaoSelecao());
		result.include("dpPessoapessoaAtualSel", new DpPessoaSelecao());
		result.include("funcaoConfiancafuncaoAtualSel", new DpFuncaoConfiancaSelecao());
		result.include("cargocargoAtualSel", new DpCargoSelecao());
	}

	@Transacional
	@Path("/gravarPermissaoUsoLista")
	public void gravarPermissaoUsoLista(SrConfiguracao permissao) throws Exception {
		permissao.salvarComoPermissaoUsoLista();
		result.use(Results.http()).body(permissao.toVO().toJson());
	}

	@Path("/listarPermissaoUsoLista")
	public void listarPermissaoUsoLista(Long idLista, boolean mostrarDesativados) throws Exception {

		SrLista lista = new SrLista();
		if (idLista != null)
			lista = SrLista.AR.findById(idLista);
		List<SrConfiguracao> associacoes = SrConfiguracao.listarPermissoesUsoLista(lista, mostrarDesativados);

		result.use(Results.http()).body(SrConfiguracao.convertToJSon(associacoes));
	}

	@Path("/desativarPermissaoUsoListaEdicao")
	public void desativarPermissaoUsoListaEdicao(Long idLista, Long idPermissao) throws Exception {
		SrConfiguracao configuracao = ContextoPersistencia.em().find(SrConfiguracao.class, idPermissao);
		configuracao.finalizar();

		result.use(Results.http()).body(configuracao.getSrConfiguracaoJson());
	}

	@Path("/configuracoesParaInclusaoAutomatica")
	public void configuracoesParaInclusaoAutomatica(Long idLista, boolean mostrarDesativados) throws Exception {
		SrLista lista = SrLista.AR.findById(idLista);

		result.use(Results.http()).body(SrConfiguracao
				.buscaParaConfiguracaoInsercaoAutomaticaListaJSON(lista.getListaAtual(), mostrarDesativados));
	}

	@Transacional
	@Path("/configuracaoAutomaticaGravar")
	public void configuracaoAutomaticaGravar(SrConfiguracao configuracao, List<SrItemConfiguracao> itemConfiguracaoSet,
			List<SrAcao> acoesSet) throws Exception {
		configuracao.setAcoesSet(acoesSet);
		configuracao.setItemConfiguracaoSet(itemConfiguracaoSet);
		configuracao.salvarComoInclusaoAutomaticaLista(configuracao.getListaPrioridade());
		result.use(Results.http()).body(configuracao.toVO().toJson());
	}

	@Transacional
	@Path("/desativarConfiguracaoAutomaticaGravar")
	public void desativarConfiguracaoAutomaticaGravar(Long id) throws Exception {
		SrConfiguracao configuracao = ContextoPersistencia.em().find(SrConfiguracao.class, id);
		configuracao.finalizar();
		result.use(Results.http()).body(configuracao.toVO().toJson());
	}

	@Transacional
	@Path("/reativarConfiguracaoAutomaticaGravar")
	public void reativarConfiguracaoAutomaticaGravar(Long id) throws Exception {
		SrConfiguracao configuracao = ContextoPersistencia.em().find(SrConfiguracao.class, id);
		configuracao.salvarComHistorico();
		result.use(Results.http()).body(configuracao.toVO().toJson());
	}

	@Path("/buscarPermissoesLista")
	public void buscarPermissoesLista(Long idLista) throws Exception {
		List<SrConfiguracao> permissoes;

		if (idLista != null) {
			SrLista lista = SrLista.AR.findById(idLista);
			permissoes = new ArrayList<>(lista.getPermissoes(getTitular().getLotacao(), getCadastrante()));
			permissoes = SrConfiguracao.listarPermissoesUsoLista(lista, false);
		} else
			permissoes = new ArrayList<SrConfiguracao>();

		result.use(Results.http()).body(SrConfiguracao.convertToJSon(permissoes));
	}

	@Transacional
	@Path("/gravarLista")
	public void gravarLista(SrLista lista) throws Exception {
		lista.setLotaCadastrante(getLotaTitular());
		validarFormEditarLista(lista);
		lista.salvarComHistorico();
		SrListaVO srListaVO = getSrListaVOComPermissoes(lista);
		result.use(Results.http()).body(srListaVO.toJson());
	}

	private SrListaVO getSrListaVOComPermissoes(SrLista lista) {
		SrListaVO srListaVO = lista.toVO();
		srListaVO.setPodeConsultar(lista.podeConsultar(getLotaTitular(), getTitular()));
		srListaVO.setPodeEditar(lista.podeEditar(getLotaTitular(), getTitular()));
		return srListaVO;
	}

	private void validarFormEditarLista(SrLista lista) {
		if (lista.getNomeLista() == null || lista.getNomeLista().trim().equals("")) {
			srValidator.addError("lista.nomeLista", "Nome da Lista no informados");
		}

		if (srValidator.hasErrors()) {
			enviarErroValidacao();
		}
	}

	@Transacional
	@Path("/desativarLista")
	public void desativarLista(Long id, boolean mostrarDesativados) throws Exception {
		SrLista lista = SrLista.AR.findById(id);
		lista.finalizar();

		result.use(Results.http()).body(lista.toJson());
	}

	@Transacional
	@Path("/reativarLista")
	public void reativarLista(Long id, boolean mostrarDesativados) throws Exception {
		SrLista lista = SrLista.AR.findById(id);
		lista.salvarComHistorico();
		result.use(Results.http()).body(lista.toJson());
	}

	@SuppressWarnings("unchecked")
	@Path("/exibirLista/{id}")
	public void exibirLista(Long id) throws Exception {
		SrLista lista = SrLista.AR.findById(id);
		if (lista == null)
			throw new AplicacaoException("Lista não encontrada");
		List<CpOrgaoUsuario> orgaos = ContextoPersistencia.em().createQuery("from CpOrgaoUsuario").getResultList();
		List<CpComplexo> locais = CpComplexo.AR.all().fetch();
		List<SrTipoPermissaoLista> tiposPermissao = SrTipoPermissaoLista.AR.all().fetch();
		SrSolicitacaoFiltro filtro = new SrSolicitacaoFiltro();
		SrSolicitacaoListaVO solicitacaoListaVO;
		String tiposPermissaoJson = new Gson().toJson(tiposPermissao);
		filtro.setIdListaPrioridade(id);
		lista = lista.getListaAtual();
		String jsonPrioridades = SrPrioridade.getJSON().toString();

		if (!lista.podeConsultar(getLotaTitular(), getTitular())) {
			throw new AplicacaoException("Exibi\u00e7\u00e3o n\u00e3o permitida");
		}

		solicitacaoListaVO = new SrSolicitacaoListaVO(filtro, true, "", false, getLotaTitular(), getCadastrante());

		result.include(LISTA, lista);
		result.include(PODE_REMOVER, lista.podeRemover(getLotaTitular(), getTitular()));
		result.include(PODE_EDITAR, lista.podeEditar(getLotaTitular(), getTitular()));
		result.include(PODE_PRIORIZAR, lista.podePriorizar(getLotaTitular(), getTitular()));
		result.include(ORGAOS, orgaos);
		result.include(LOCAIS, locais);
		result.include(TIPOS_PERMISSAO, tiposPermissao);
		result.include(SOLICITACAO_LISTA_VO, solicitacaoListaVO);
		result.include(FILTRO, filtro);
		result.include(TIPOS_PERMISSAO_JSON, tiposPermissaoJson);
		result.include("jsonPrioridades", jsonPrioridades);
		result.include(PRIORIDADE_LIST, SrPrioridade.values());

		result.include("lotacaoParaInclusaoAutomaticaSel", new DpLotacaoSelecao());
		result.include("prioridades", SrPrioridade.getValoresEmOrdem());
		result.include(LOTA_TITULAR, getLotaTitular());
		result.include(CADASTRANTE, getCadastrante());

		PessoaLotaFuncCargoSelecaoHelper.adicionarCamposSelecao(result);
	}

	@Transacional
	@Path("/gravar")
	public void gravar(SrSolicitacao solicitacao) throws Exception {
		forcarCargaDoItemEDaAcao(solicitacao);

		if (solicitacao == null)
			throw new AplicacaoException("Não foram informados dados suficientes para a gravação");

		limparDpPessoa(solicitacao);

		// Edson: por causa do detach no ObjetoObjectInstantiator:
		if (solicitacao.getSolicitacaoInicial() != null) {
			solicitacao.setSolicitacaoInicial(SrSolicitacao.AR.findById(solicitacao.getSolicitacaoInicial().getId()));
			solicitacao.getSolicitacaoFilhaSet();
		}

		// Edson: antigamente, ao regravar uma solicitação que já estava em elaboração,
		// o atributo arquivo era
		// setado como null pelo Play automaticamente, mas agora os atributos vazios são
		// eliminados do request
		if (solicitacao.getArquivo() != null && solicitacao.getArquivo().getId() != null)
			solicitacao.setArquivo(null);

		// Chrys: Impede que uma solicitação deixe de ser rascunho caso tenha alguma
		// pendência.
		// Solicitacao temporária e solicitacao rascunho são conceitos diferentes.
		// solicitacao.isTemporaria() retorna true caso a sigla da solicitação comece
		// com "TMP"
		// solicitacao.isRascunho() retorna true se o checkbox rascunho (da tela de
		// edição) estiver marcado
		if (solicitacao.isTemporaria() && !solicitacao.isRascunho() && solicitacao.isPendente()) {
			throw new AplicacaoException(
					"Para que a solicitação possa deixar de ser rascunho, é necessário terminar a pendência.");
		}

		if (!solicitacao.isRascunho() && !validarFormEditar(solicitacao)) {
			enviarErroValidacao();
			return;
		}

		// Carregar a acao:
		if (solicitacao.getAcao() != null && solicitacao.getAcao().getTituloAcao() == null
				&& solicitacao.getAcao().getIdAcao() != null) {
			solicitacao.setAcao(SrAcao.AR.findById(solicitacao.getAcao().getIdAcao()));
		}

		// BJN - caso a solicitação seja NOVA E do tipo "Atividades da Lotação",
		// o atendente deverá ser a própria lotação do cadastrante titular
		// MARRETA SOLICITACAO PARA PROPRIA LOTACAO
		// voltando a pegar a lotacao do titular ao inves da do solicitante...
		if ((solicitacao.getIdSolicitacao() == null)
				&& solicitacao.getAcao().getTituloAcao().toLowerCase().startsWith("atividades da lotação")) {
			// solicitacao.setAtendenteNaoDesignado(solicitacao.getSolicitante().getLotacao().getLotacaoAtual());
			solicitacao.setAtendenteNaoDesignado(getTitular().getLotacao().getLotacaoAtual());
		}
		// FIM MARRETA

		solicitacao.salvar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
		result.use(Results.http()).body(solicitacao.getSiglaCompacta());
	}

	private void incluirListasEdicaoSolicitacao(SrSolicitacao solicitacao) throws Exception {
		result.include(SOLICITACAO, solicitacao);
		result.include("locais", ContextoPersistencia.em().createQuery("from CpComplexo").getResultList());
		result.include("formaAcompanhamentoList", SrFormaAcompanhamento.values());
		result.include("gravidadeList", solicitacao.getGravidadesDisponiveisEPrioridades());
		result.include("tipoMotivoEscalonamentoList", SrTipoMotivoEscalonamento.values());
		result.include(PRIORIDADE_LIST, SrPrioridade.values());
		result.include("locaisDisponiveis", solicitacao.getLocaisDisponiveis());
		result.include("meiosComunicadaoList", SrMeioComunicacao.values());
		result.include("podeUtilizarServicoSigaGC", podeUtilizarServico("SIGA;GC"));
		result.include("podeVerGestorItem", podeUtilizarServico("SIGA;SR;VER_GESTOR_ITEM"));
		result.include("atributoAssociados", solicitacao.getAtributoAssociados());
		result.include("atributoSolicitacaoMap", solicitacao.getAtributoSolicitacaoMap());
		result.include("solicitante", solicitacao.getSolicitante());
		result.include("siglaCompacta", solicitacao.getSiglaCompacta());
		result.include("local", solicitacao.getLocal());
		result.include("lotacaoDoTitularLegivel", getTitular().getLotacao().getLotacaoAtual().toString() + " - "
				+ getTitular().getLotacao().getLotacaoAtual().getNomeLotacao());
		result.include("solicitanteDescricaoCompleta", getDescricaoCompleta(solicitacao.getSolicitante()));
	}

	private String getDescricaoCompleta(DpPessoa solicitante) {
		String result = "";
		try {
			result = solicitante.getDescricaoCompleta();
		} catch (Exception e) {
		}
		return result;
	}

	private boolean validarFormEditar(SrSolicitacao solicitacao) throws Exception {
		if (solicitacao.getSolicitante() == null || solicitacao.getSolicitante().getId() == null)
			srValidator.addError("solicitacao.solicitante", "Solicitante n\u00e3o informado");

		if (solicitacao.getDescrSolicitacao() == null || "".equals(solicitacao.getDescrSolicitacao().trim()))
			srValidator.addError("solicitacao.descrSolicitacao", "Descri&ccedil&atilde;o n&atilde;o informada");

		if (solicitacao.getTelPrincipal() == null || "".equals(solicitacao.getTelPrincipal().trim()))
			srValidator.addError("solicitacao.telPrincipal", "Telefone n&atilde;o informado");

		if (solicitacao.getEndereco() == null || "".equals(solicitacao.getEndereco().trim()))
			srValidator.addError("solicitacao.endereco", "Endere&ccedil;o de atendimento n&atilde;o informado");

		validarFormReclassificar(solicitacao);

		return !srValidator.hasErrors();
	}

	private boolean validarFormReclassificar(SrSolicitacao solicitacao) throws Exception {
		if (solicitacao.getItemConfiguracao() == null || solicitacao.getItemConfiguracao().getId() == null)
			srValidator.addError("solicitacao.itemConfiguracao", "Item n&atilde;o informado");
		else
			solicitacao.setItemConfiguracao(SrItemConfiguracao.AR.findById(solicitacao.getItemConfiguracao().getId()));
		if (solicitacao.getAcao() == null || solicitacao.getAcao().getId() == null)
			srValidator.addError("solicitacao.acao", "A&ccedil&atilde;o n&atilde;o informada");
		else
			solicitacao.setAcao(SrAcao.AR.findById(solicitacao.getAcao().getId()));
		// atributos
		Map<Long, Boolean> obrigatorio = solicitacao.getObrigatoriedadeTiposAtributoAssociados();
		int index = 0;
		for (Map.Entry<Long, SrAtributoSolicitacaoMap> atributo : solicitacao.getAtributoSolicitacaoMap().entrySet()) {
			// Para evitar NullPointerExcetpion quando nao encontrar no Map
			if (Boolean.TRUE.equals(obrigatorio.get(atributo.getKey())))
				if (atributo.getValue() != null && (atributo.getValue().getValorAtributo() == null
						|| "".equals(atributo.getValue().getValorAtributo().trim())))
					srValidator.addError("solicitacao.atributoSolicitacaoList[" + index + "].valorAtributo",
							"Atributo n&atilde;o informado");
			index++;
		}
		return !srValidator.hasErrors();
	}

	public Boolean todoOContexto() {
		return Boolean.parseBoolean(getRequest().getParameter("todoOContexto"));
	}

	public Boolean ocultas() {
		return Boolean.parseBoolean(getRequest().getParameter("ocultas"));
	}

	@Path("/exibir/{sigla}/{todoOContexto}/{ocultas}")
	public void exibirComParametros(String sigla, Boolean todoOContexto, Boolean ocultas) throws Exception {
		result.forwardTo(this).exibir(sigla, todoOContexto, ocultas);
	}

	@Path("/exibir/{sigla}")
	public void exibir(String sigla, Boolean todoOContexto, Boolean ocultas) throws Exception {

		if (sigla == null || sigla.trim().equals(""))
			throw new AplicacaoException("Número não informado");

		SrSolicitacao solicitacao = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular())
				.selecionar(sigla);

		if (solicitacao == null)
			throw new AplicacaoException("Esta solicita\u00e7\u00e3o foi exclu\u00edda");

		SrMovimentacao movimentacao = new SrMovimentacao(solicitacao);

		List<DpPessoa> atendentes = solicitacao.getPessoasAtendentesDisponiveis();

		// BJN - vetar usuario externo caso nao seja atendente ou o proprio solicitante
		if (getTitular().isUsuarioExterno()) {
			boolean ehUmAtendente = atendentes.contains(getTitular());
			boolean ehSolicitante = getTitular().equals(solicitacao.getSolicitante());
			if (!ehUmAtendente && !ehSolicitante)
				throw new AplicacaoException("Este usuário n\u00e3o pode acessar esta solicita\u00e7\u00e3o.");
		}

		if (todoOContexto == null)
			todoOContexto = solicitacao.isParteDeArvore();

		if (ocultas == null)
			ocultas = false;

		// Para informacoes do solicitante, exibidas no sidebar
		// (exibirIncludeSidebarSolicitacao):
		setupInfoSolicitante(solicitacao);

		Set<SrMovimentacao> movs = solicitacao.getMovimentacaoSet(ocultas, null, false, todoOContexto, !ocultas, false);
		Set<SrArquivo> arqs = solicitacao.getArquivosAnexos(todoOContexto);
		Set<SrLista> listas = solicitacao.getListasAssociadas(todoOContexto);
		List<SrPendencia> pendencias = solicitacao.getPendenciasEmAberto(todoOContexto);
		Set<SrEtapaSolicitacao> etapasCronometro = solicitacao.getEtapas(getLotaTitular(), todoOContexto);
		Set<SrEtapaSolicitacao> etapas = solicitacao.getEtapas(todoOContexto);
		Set<SrSolicitacao> vinculadas = solicitacao.getSolicitacoesVinculadas(todoOContexto);
		Set<SrSolicitacao> juntadas = solicitacao.getSolicitacoesJuntadas(todoOContexto);
		Set<SrAtributoSolicitacao> atributos = solicitacao.getAtributoSolicitacaoSetAtual(todoOContexto);

		result.include(SOLICITACAO, solicitacao);
		result.include("movimentacao", movimentacao);
		result.include("movs", movs);
		result.include("arqs", arqs);
		result.include("listas", listas);
		result.include("pendencias", pendencias);
		result.include("etapas", etapas);
		result.include("etapasCronometro", etapasCronometro);
		result.include("vinculadas", vinculadas);
		result.include("juntadas", juntadas);
		result.include("todoOContexto", todoOContexto);
		result.include("ocultas", ocultas);
		result.include("atendentes", atendentes);
		result.include("motivosPendencia", SrTipoMotivoPendencia.values());
		result.include(PRIORIDADE_LIST, SrPrioridade.values());
		result.include("podeUtilizarServicoSigaGC", podeUtilizarServico("SIGA;GC"));
		result.include("atributos", atributos);
	}

	private void setupInfoSolicitante(SrSolicitacao solicitacao) {
		// para evitar erro de 'no session' quando acessado diretamente apos reiniciar
		if (solicitacao.getSolicitante() != null) {
			solicitacao.getSolicitante().getSiglaCompleta();
			solicitacao.getSolicitante().getFuncaoStringIniciaisMaiusculas();
			solicitacao.getSolicitante().getLotacao().getSiglaCompleta();
		}
		if (solicitacao.getLocal() != null)
			solicitacao.getLocal().getNomeComplexo();
	}

	@SuppressWarnings("unchecked")
	@Path("/buscar")
	public void buscar(SrSolicitacaoFiltro filtro, String propriedade, boolean popup, boolean telaDeListas)
			throws Exception {

		setupFiltros(filtro);

		if (filtro != null && filtro.isPesquisar()) {
			SrSolicitacaoListaVO solicitacaoListaVO = new SrSolicitacaoListaVO(filtro, telaDeListas, propriedade, popup,
					getLotaTitular(), getCadastrante());
			result.use(Results.json()).withoutRoot().from(solicitacaoListaVO).excludeAll().include("recordsFiltered")
					.include("data").serialize();
		} else {
			if (filtro == null || !filtro.isRazoavelmentePreenchido()) {
				filtro = novoFiltroZerado();
			}

			result.include("solicitacaoListaVO",
					new SrSolicitacaoListaVO(filtro, false, propriedade, popup, getLotaTitular(), getCadastrante()));
			result.include("tipos", new String[] { "Pessoa", "Lota\u00e7\u00e3o" });

			List<SrMarca> marcadores = ContextoPersistencia.em()
					.createQuery("select distinct cpMarcador from SrMarca marca where marca.cpMarcador.idMarcador <> 9")
					.getResultList();
			result.include("marcadores", marcadores);

			result.include("filtro", filtro);
			result.include("propriedade", propriedade);
			result.include("popup", popup);
			result.include("locaisDisponiveis", filtro.getLocaisParaBusca());
			result.include("listasPrioridade", SrLista.listar(false));
			result.include("prioridadesEnum", SrPrioridade.values());
		}
	}

	private void setupFiltros(SrSolicitacaoFiltro filtro) {

		if (filtro == null)
			return;

		if (filtro.getSituacao() != null && filtro.getSituacao().getIdMarcador() != null
				&& filtro.getSituacao().getDescrMarcador() == null) {
			filtro.setSituacao(CpMarcador.AR.findById(filtro.getSituacao().getIdMarcador()));
		}
		if (filtro.getItemConfiguracao() != null && filtro.getItemConfiguracao().getIdItemConfiguracao() != null) {
			filtro.setItemConfiguracao(
					SrItemConfiguracao.AR.findById(filtro.getItemConfiguracao().getIdItemConfiguracao()));
		}
		if (filtro.getAcao() != null && filtro.getAcao().getIdAcao() != null) {
			filtro.setAcao(SrAcao.AR.findById(filtro.getAcao().getIdAcao()));
		}
		if (filtro.getCadastranteBusca() != null && filtro.getCadastranteBusca().getIdPessoa() != null) {
			filtro.setCadastranteBusca(DpPessoa.AR.findById(filtro.getCadastranteBusca().getIdPessoa()));
		}
		if (filtro.getLotaCadastranteBusca() != null && filtro.getLotaCadastranteBusca().getIdLotacao() != null) {
			filtro.setLotaCadastranteBusca(DpLotacao.AR.findById(filtro.getLotaCadastranteBusca().getIdLotacao()));
		}
		if (filtro.getSolicitante() != null && filtro.getSolicitante().getIdPessoa() != null) {
			filtro.setSolicitante(DpPessoa.AR.findById(filtro.getSolicitante().getIdPessoa()));
		}
		if (filtro.getLotaSolicitante() != null && filtro.getLotaSolicitante().getIdLotacao() != null) {
			filtro.setLotaSolicitante(DpLotacao.AR.findById(filtro.getLotaSolicitante().getIdLotacao()));
		}
		if (filtro.getAtendente() != null && filtro.getAtendente().getIdPessoa() != null) {
			filtro.setAtendente(DpPessoa.AR.findById(filtro.getAtendente().getIdPessoa()));
		}
		if (filtro.getLotaAtendente() != null && filtro.getLotaAtendente().getIdLotacao() != null) {
			filtro.setLotaAtendente(DpLotacao.AR.findById(filtro.getLotaAtendente().getIdLotacao()));
		}
	}

	private SrSolicitacaoFiltro novoFiltroZerado() {
		SrSolicitacaoFiltro retorno = new SrSolicitacaoFiltro();
		Date hoje = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		retorno.setDtIni(formato.format(hoje));
		retorno.setDtFim(formato.format(hoje));
		retorno.setAtendente(getCadastrante());
		return retorno;
	}

	/**
	 * Metodo necessario devido a diferenca da 7.2, onde os objetos ja vem
	 * instanciados, mas vazios.
	 * 
	 * @param solicitacao
	 */
	private void limparDpPessoa(SrSolicitacao solicitacao) {
		if (solicitacao != null) {
			if (solicitacao.getSolicitante() != null && solicitacao.getSolicitante().getId() == null)
				solicitacao.setSolicitante(null);
			if (solicitacao.getCadastrante() != null && solicitacao.getCadastrante().getId() == null)
				solicitacao.setCadastrante(null);
			if (solicitacao.getInterlocutor() != null && solicitacao.getInterlocutor().getId() == null)
				solicitacao.setInterlocutor(null);
			if (solicitacao.getItemConfiguracao() != null && solicitacao.getItemConfiguracao().getId() == null)
				solicitacao.setItemConfiguracao(null);
		}
	}

	private void setupItemConfiguracao(SrSolicitacao solicitacao) throws Exception {
		if (solicitacao.getItemConfiguracao() != null) {
			SrItemConfiguracao srItemConf = solicitacao.getItemConfiguracao();
			boolean found = false;
			for (SrItemConfiguracao itemConf : solicitacao.getItensDisponiveis()) {
				if (srItemConf.getIdItemConfiguracao().equals(itemConf.getIdItemConfiguracao())) {
					found = true;
				}
			}
			if (!found) {
				solicitacao.setItemConfiguracao(null);
			}
		}
	}

	private void setupAcoes(SrSolicitacao solicitacao) throws Exception {
		if (solicitacao.getAcao() != null) {
			if (solicitacao.getAcao().getHisIdIni() == null && solicitacao.getAcao().getIdAcao() != null)
				solicitacao.setAcao(SrAcao.AR.findById(solicitacao.getAcao().getIdAcao()));
			boolean containsAcao = false;
			for (List<SrTarefa> tarefas : solicitacao.getAcoesEAtendentes().values())
				for (SrTarefa t : tarefas)
					if (t.getAcao().equivale(solicitacao.getAcao()))
						containsAcao = true;
			if (!containsAcao)
				solicitacao.setAcao(null);
		}
	}

	@Path({ "/editar", "/editar/{sigla}" })
	public void editar(String sigla, SrSolicitacao solicitacao, String item, String acao, String descricao,
			Long solicitante) throws Exception {
		// Edson: se a sigla é != null, está vindo pelo link Editar. Se sigla for ==
		// null mas solicitacao for != null é um postback.

		limparDpPessoa(solicitacao);

		if (sigla != null) {
			solicitacao = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular()).selecionar(sigla);
			// carregamento forçado de atributos lazy
			if (solicitacao.getAcordos() != null)
				solicitacao.getAcordos().size();
			Hibernate.initialize(solicitacao.getMeuAtributoSolicitacaoSet());
			if (!solicitacao.isRascunho()) {
				throw new AplicacaoException("Não é possível editar Solicitação que não seja rascunho.");
			}
		} else {
			if (solicitacao == null || solicitante == null) {

				solicitacao = new SrSolicitacao();
				try {
					so.assertAcesso(SALVAR_SOLICITACAO_AO_ABRIR);
					solicitacao.setRascunho(true);
					solicitacao.salvar(getCadastrante(), getLotaCadastrante(), getTitular(), getLotaTitular());
					solicitacao.setRascunho(false);

					// Edson: para evitar que o JPA tente salvar a solicitação por causa dos
					// próximos set's chamados
					if (solicitacao.getAcordos() != null)
						solicitacao.getAcordos().size();
					em().detach(solicitacao);

				} catch (AplicacaoException ae) {
					solicitacao.setCadastrante(getCadastrante());
					solicitacao.setLotaCadastrante(getLotaCadastrante());
					solicitacao.setTitular(getTitular());
					solicitacao.setLotaTitular(getLotaTitular());
					solicitacao.completarPreenchimento();
				}

				if (item != null && !item.equals(""))
					solicitacao.setItemConfiguracao((SrItemConfiguracao) SrItemConfiguracao.AR
							.find("bySiglaItemConfiguracaoAndHisDtFimIsNull", item).first());
				if (acao != null && !acao.equals(""))
					solicitacao.setAcao((SrAcao) SrAcao.AR.find("bySiglaAcaoAndHisDtFimIsNull", acao).first());

				forcarCargaDoItemEDaAcao(solicitacao);

				if (descricao != null && !descricao.equals(""))
					solicitacao.setDescricao(descricao);
			}

			// Edson: O deduzir(), o setItem(), o setAcao() e o asociarPrioridade() deveriam
			// ser chamados dentro da própria solicitação pois é responsabilidade
			// da própria classe atualizar os seus atributos para manter consistência após a
			// entrada de um dado.
			if (solicitacao.getLocal() == null || (solicitacao.getSolicitante() != null
					&& !solicitacao.getSolicitante().getId().equals(solicitante)))
				solicitacao.deduzirLocalRamalEMeioContato();

			setupItemConfiguracao(solicitacao);

			setupAcoes(solicitacao);

			// Edson: por causa do detach:
			if (solicitacao.getSolicitacaoInicial() != null) {
				solicitacao
						.setSolicitacaoInicial(SrSolicitacao.AR.findById(solicitacao.getSolicitacaoInicial().getId()));
			}
			if (solicitacao.getItemConfiguracao() != null)
				solicitacao
						.setItemConfiguracao(SrItemConfiguracao.AR.findById(solicitacao.getItemConfiguracao().getId()));
		}

		try {
			result.include("etapasCronometro", solicitacao.getEtapas(getLotaTitular(), false));
		} catch (LazyInitializationException lie) {
			// Edson: se é um postback, não recarregar os acordos
		}

		incluirListasEdicaoSolicitacao(solicitacao);

	}

	// Nato: Por algum motivo não estava carregados itens e ações
	//
	public void forcarCargaDoItemEDaAcao(SrSolicitacao solicitacao) {
		if (solicitacao == null)
			return;
		String idItem = getRequest().getParameter("solicitacao.itemConfiguracao.id");
		if ((solicitacao.getItemConfiguracao() == null || solicitacao.getItemConfiguracao().getDescricao() == null)
				&& idItem != null && idItem.trim().length() > 0)
			solicitacao.setItemConfiguracao(SrItemConfiguracao.AR.findById(Long.parseLong(idItem)));
		String idAcao = getRequest().getParameter("solicitacao.acao.id");
		if ((solicitacao.getAcao() == null || solicitacao.getAcao().getDescricao() == null) && idAcao != null
				&& idAcao.trim().length() > 0)
			solicitacao.setAcao(SrAcao.AR.findById(Long.parseLong(idAcao)));
	}

	@Path("/listarSolicitacoesRelacionadas")
	public void listarSolicitacoesRelacionadas(SrSolicitacao solicitacao, SrSolicitacaoFiltro filtro) throws Exception {
		forcarCargaDoItemEDaAcao(solicitacao);
		if (filtro == null && solicitacao != null) {
			filtro = new SrSolicitacaoFiltro();
			filtro.setSolicitante(solicitacao.getSolicitante());
			filtro.setItemConfiguracao(solicitacao.getItemConfiguracao());
			filtro.setAcao(solicitacao.getAcao());
		}
		if (solicitacao.getAcao() != null && solicitacao.getAcao().getTituloAcao() == null
				&& solicitacao.getAcao().getIdAcao() != null) {
			solicitacao.setAcao(SrAcao.AR.findById(solicitacao.getAcao().getIdAcao()));
		}
		if (solicitacao.getItemConfiguracao() != null && solicitacao.getItemConfiguracao().getId() == null) {
			solicitacao.setItemConfiguracao(null);
		}
		result.include("solicitacoesRelacionadas", filtro.buscarSimplificado(getTitular()));
		result.include("filtro", filtro);
		result.include(SOLICITACAO, solicitacao);
	}

	@Transacional
	@Path("/retirarDeLista")
	public void retirarDeLista(String sigla, Long idLista) throws Exception {
		if (sigla == null || sigla.trim().equals(""))
			throw new AplicacaoException("Número não informado");
		SrSolicitacao solicitacao = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular())
				.selecionar(sigla);
		SrLista lista = SrLista.AR.findById(idLista);
		solicitacao.retirarDeLista(lista, getCadastrante(), getCadastrante().getLotacao(), getTitular(),
				getLotaTitular());
		result.redirectTo(this).exibirLista(idLista);
	}

	@Path("/incluirEmLista")
	public void incluirEmLista(String sigla) throws Exception {
		if (sigla == null || sigla.trim().equals(""))
			throw new AplicacaoException("Número não informado");
		SrSolicitacao solicitacao = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular())
				.selecionar(sigla);
		solicitacao = solicitacao.getSolicitacaoAtual();
		List<SrPrioridade> prioridades = SrPrioridade.getValoresEmOrdem();

		result.include(SOLICITACAO, solicitacao);
		result.include("prioridades", prioridades);
	}

	@Transacional
	@Path("/incluirEmListaGravar")
	public void incluirEmListaGravar(String sigla, Long idLista, SrPrioridade prioridade,
			boolean naoReposicionarAutomatico) throws Exception {
		if (idLista == null) {
			throw new AplicacaoException("Selecione a lista para inclus\u00e3o da solicita\u00e7\u00e3o");
		}
		if (sigla == null || sigla.trim().equals(""))
			throw new AplicacaoException("Número não informado");

		SrSolicitacao solicitacao = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular())
				.selecionar(sigla);
		SrLista lista = SrLista.AR.findById(idLista);
		solicitacao.incluirEmLista(lista, getCadastrante(), getCadastrante().getLotacao(), getTitular(),
				getLotaTitular(), prioridade, naoReposicionarAutomatico);
		result.redirectTo(this).exibir(solicitacao.getSiglaCompacta(), todoOContexto(), ocultas());
	}

	@Path("/reclassificar")
	public void reclassificar(SrSolicitacao solicitacao) throws Exception {

		carregaItemConfiguracao(solicitacao);

		if (solicitacao.getCodigo() == null || solicitacao.getCodigo().trim().equals(""))
			throw new AplicacaoException("Número não informado");
		SrSolicitacao solicitacaoEntity = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular())
				.selecionar(solicitacao.getCodigo());

		Hibernate.initialize(solicitacaoEntity.getMeuAtributoSolicitacaoSet());
		// Edson: por algum motivo, está sendo necessário dar o detach na
		// solicitacaoPai, se não, o JPA entende que o arquivo
		// foi alterado e precisa ser salvo, o que dá erro pois o arquivo também é
		// detached:
		if (solicitacaoEntity.isFilha())
			for (SrSolicitacao sol : solicitacaoEntity.getPaiDaArvore().getSolicitacaoFilhaSetRecursivo())
				em().detach(sol);
		// Edson: para evitar que o JPA tente salvar a solicitação por causa dos set's
		// chamados:
		em().detach(solicitacaoEntity);

		solicitacaoEntity.setTitular(getTitular());
		solicitacaoEntity.setLotaTitular(getLotaTitular());
		if (solicitacao.getItemConfiguracao() != null)
			solicitacaoEntity.setItemConfiguracao(solicitacao.getItemConfiguracao());
		else
			solicitacaoEntity.setItemConfiguracao(solicitacaoEntity.getItemAtual());
		if (solicitacao.getAcao() != null)
			solicitacaoEntity.setAcao(solicitacao.getAcao());
		else
			solicitacaoEntity.setAcao(solicitacaoEntity.getAcaoAtual());
		if (solicitacao.getAtributoSolicitacaoMap() != null && !solicitacao.getAtributoSolicitacaoMap().isEmpty())
			solicitacaoEntity.setAtributoSolicitacaoMap(solicitacao.getAtributoSolicitacaoMap());

		incluirListasReclassificacao(solicitacaoEntity);
	}

	@Transacional
	@Path("/reclassificarGravar")
	public void reclassificarGravar(SrSolicitacao solicitacao) throws Exception {
		forcarCargaDoItemEDaAcao(solicitacao);
		if (solicitacao.getCodigo() == null || solicitacao.getCodigo().trim().equals(""))
			throw new AplicacaoException("Número não informado");

		if (!validarFormReclassificar(solicitacao)) {
			enviarErroValidacao();
			return;
		}
		SrSolicitacao solicitacaoEntity = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular())
				.selecionar(solicitacao.getCodigo());
		solicitacaoEntity.reclassificar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(),
				solicitacao);
		result.use(Results.http()).body(solicitacaoEntity.getSiglaCompacta());
	}

	private void incluirListasReclassificacao(SrSolicitacao solicitacao) throws Exception {
		result.include(SOLICITACAO, solicitacao);
		result.include("siglaCompacta", solicitacao.getSiglaCompacta());
		result.include("solicitante", solicitacao.getSolicitante());
		result.include("local", solicitacao.getLocal());
		result.include("acoesEAtendentes", solicitacao.getAcoesEAtendentes());
		result.include("atributoAssociados", solicitacao.getAtributoAssociados());
		result.include("atributoSolicitacaoMap", solicitacao.getAtributoSolicitacaoMap());
	}

	@Path("/fechar")
	public void fechar(SrSolicitacao solicitacao) throws Exception {
		forcarCargaDoItemEDaAcao(solicitacao);
		carregaItemConfiguracao(solicitacao);

		reclassificar(solicitacao);
		Set<SrTipoMotivoFechamento> motivos = new TreeSet<SrTipoMotivoFechamento>(
				new Comparator<SrTipoMotivoFechamento>() {
					@Override
					public int compare(SrTipoMotivoFechamento o1, SrTipoMotivoFechamento o2) {
						int id1 = o1.getidTipoMotivoFechamento(), id2 = o2.getidTipoMotivoFechamento();
						return id1 > id2 ? +1 : id1 < id2 ? -1 : 0;
					}
				});
		motivos.addAll(Arrays.asList(SrTipoMotivoFechamento.values()));
		result.include("motivosFechamento", motivos);
	}

	@Transacional
	@Path("/fecharGravar")
	public void fecharGravar(SrSolicitacao solicitacao, String motivoFechar, SrTipoMotivoFechamento tpMotivo,
			String conhecimento) throws Exception {
		forcarCargaDoItemEDaAcao(solicitacao);
		if (solicitacao.getCodigo() == null || solicitacao.getCodigo().trim().equals(""))
			throw new AplicacaoException("Número não informado");

		if (!validarFormReclassificar(solicitacao)) {
			enviarErroValidacao();
			return;
		}
		SrSolicitacao solicitacaoEntity = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular())
				.selecionar(solicitacao.getCodigo());
		solicitacaoEntity.fechar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(),
				solicitacao.getItemConfiguracao(), solicitacao.getAcao(), motivoFechar, tpMotivo, conhecimento,
				solicitacao.getAtributoSolicitacaoMap());
		result.use(Results.http()).body(solicitacaoEntity.getSiglaCompacta());
	}

	@Path("/erPesquisa")
	public void responderPesquisa(String sigla) throws Exception {
		/*
		 * SrSolicitacao sol = SrSolicitacao.findById(id); SrPesquisa pesquisa =
		 * sol.getPesquisaDesignada(); if (pesquisa == null) throw new
		 * Exception("NÃ£o foi encontrada nenhuma pesquisa designada para esta solicitaÃ§Ã£o."
		 * ); pesquisa = SrPesquisa.findById(pesquisa.idPesquisa); pesquisa =
		 * pesquisa.getPesquisaAtual(); render(id, pesquisa);
		 */
	}

	@Transacional
	@Path("/responderPesquisaGravar")
	public void responderPesquisaGravar(String sigla, Map<Long, String> respostaMap) throws Exception {
		if (sigla == null || sigla.trim().equals(""))
			throw new AplicacaoException("Número não informado");

		SrSolicitacao sol = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular()).selecionar(sigla);
		sol.responderPesquisa(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(),
				respostaMap);
		result.redirectTo(this).exibir(sol.getSiglaCompacta(), todoOContexto(), ocultas());
	}

	@Path("/baixar/{idArquivo}")
	public Download baixar(Long idArquivo) throws Exception {
		if (idArquivo == null)
			throw new AplicacaoException("Arquivo não informado");
		SrArquivo arq = SrArquivo.AR.findById(idArquivo);
		return new ByteArrayDownload(arq.getBlob(), arq.getMime(), arq.getNomeArquivo(), false);
	}

	private void carregaItemConfiguracao(SrSolicitacao solicitacao) {
		SrItemConfiguracao item = solicitacao.getItemConfiguracao();
		if (item != null && item.getIdItemConfiguracao() != null && item.getDescrItemConfiguracao() == null) {
			solicitacao.setItemConfiguracao(SrItemConfiguracao.AR.findById(item.getIdItemConfiguracao()));
		}
	}

	@Path("/escalonar")
	public void escalonar(SrSolicitacao solicitacao) throws Exception {

		carregaItemConfiguracao(solicitacao);

		if (solicitacao.getCodigo() == null || solicitacao.getCodigo().trim().equals(""))
			throw new AplicacaoException("Número não informado");
		SrSolicitacao solicitacaoEntity = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular())
				.selecionar(solicitacao.getCodigo());

		Hibernate.initialize(solicitacaoEntity.getMeuAtributoSolicitacaoSet());
		// Edson: por algum motivo, está sendo necessário dar o detach na
		// solicitacaoPai, se não, o JPA entende que o arquivo
		// foi alterado e precisa ser salvo, o que dá erro pois o arquivo também é
		// detached:
		if (solicitacaoEntity.isFilha())
			for (SrSolicitacao sol : solicitacaoEntity.getPaiDaArvore().getSolicitacaoFilhaSetRecursivo())
				em().detach(sol);

		// Edson: para evitar que o JPA tente salvar a solicitação por causa dos set's
		// chamados:
		em().detach(solicitacaoEntity);

		solicitacaoEntity.setTitular(getTitular());
		solicitacaoEntity.setLotaTitular(getLotaTitular());
		if (solicitacao.getItemConfiguracao() != null)
			solicitacaoEntity.setItemConfiguracao(solicitacao.getItemConfiguracao());
		else
			solicitacaoEntity.setItemConfiguracao(solicitacaoEntity.getItemAtual());
		if (solicitacao.getAcao() != null)
			solicitacaoEntity.setAcao(solicitacao.getAcao());
		else
			solicitacaoEntity.setAcao(solicitacaoEntity.getAcaoAtual());
		if (solicitacao.getAtributoSolicitacaoMap() != null && !solicitacao.getAtributoSolicitacaoMap().isEmpty())
			solicitacaoEntity.setAtributoSolicitacaoMap(solicitacao.getAtributoSolicitacaoMap());

		CpConfiguracao filtro = new CpConfiguracao();
		filtro.setDpPessoa(getTitular());
		filtro.setLotacao(getLotaTitular());
		filtro.setBuscarPorPerfis(true);
		filtro.setCpTipoConfiguracao(SrTipoDeConfiguracao.ESCALONAMENTO_SOL_FILHA);
		CpSituacaoDeConfiguracaoEnum situacao = SrConfiguracaoBL.get().buscaSituacao(filtro, new int[] { 0 }, null);
		boolean criarFilhaDefault = false;
		if (situacao != null
				&& (situacao == CpSituacaoDeConfiguracaoEnum.PODE || situacao == CpSituacaoDeConfiguracaoEnum.DEFAULT))
			criarFilhaDefault = true;

		incluirListasReclassificacao(solicitacaoEntity);
		result.include("criarFilhaDefault", criarFilhaDefault);
		result.include("isFechadoAutomaticamente", solicitacaoEntity.isFechadoAutomaticamente());
		result.include("solicitacaoPai", solicitacaoEntity.getSolicitacaoPai());
		result.include("isPai", solicitacaoEntity.isPai());
		result.include("codigo", solicitacaoEntity.isFilha() ? solicitacaoEntity.getSolicitacaoPai().getCodigo()
				: solicitacaoEntity.getCodigo());
		result.include(TIPO_MOTIVO_ESCALONAMENTO_LIST, SrTipoMotivoEscalonamento.values());
	}

	@Transacional
	@Path("/escalonarGravar")
	public void escalonarGravar(SrSolicitacao solicitacao, DpLotacao atendente, DpLotacao atendenteNaoDesignado,
			SrTipoMotivoEscalonamento motivo, String descricao, Boolean criaFilha, Boolean fechadoAuto)
			throws Exception {
		forcarCargaDoItemEDaAcao(solicitacao);

		if (solicitacao.getCodigo() == null || solicitacao.getCodigo().trim().equals(""))
			throw new AplicacaoException("Número não informado");

		if (!validarFormReclassificar(solicitacao)) {
			enviarErroValidacao();
			return;
		}

		SrSolicitacao solicitacaoEntity = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular())
				.selecionar(solicitacao.getCodigo());

		if (criaFilha) {
			SrSolicitacao filha = solicitacaoEntity.escalonarCriandoFilha(getCadastrante(),
					getCadastrante().getLotacao(), getTitular(), getLotaTitular(), solicitacao.getItemConfiguracao(),
					solicitacao.getAcao(), solicitacao.getDesignacao(), atendenteNaoDesignado, fechadoAuto, descricao,
					solicitacao.getAtributoSolicitacaoMap());
			result.use(Results.http()).body(filha.getSiglaCompacta());
		} else {
			solicitacaoEntity.escalonarPorMovimentacao(getCadastrante(), getCadastrante().getLotacao(), getTitular(),
					getLotaTitular(), solicitacao.getItemConfiguracao(), solicitacao.getAcao(),
					solicitacao.getDesignacao(), atendenteNaoDesignado, motivo, descricao, atendente,
					solicitacao.getAtributoSolicitacaoMap());
			result.use(Results.http()).body(solicitacaoEntity.getSiglaCompacta());
		}
	}

	@Transacional
	@Path("/vincular")
	public void vincular(String sigla, SrSolicitacao solRecebeVinculo, String justificativa) throws Exception {
		if (sigla == null || sigla.trim().equals(""))
			throw new AplicacaoException("Número não informado");
		SrSolicitacao sol = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular()).selecionar(sigla);
		sol.vincular(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), solRecebeVinculo,
				justificativa);
		result.redirectTo(this).exibir(sol.getSiglaCompacta(), todoOContexto(), ocultas());
	}

	@Transacional
	@Path("/juntar")
	public void juntar(String sigla, SrSolicitacao solRecebeJuntada, String justificativa) throws Exception {
		if (sigla == null || sigla.trim().equals(""))
			throw new AplicacaoException("Número não informado");
		SrSolicitacao sol = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular()).selecionar(sigla);
		sol.juntar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), solRecebeJuntada,
				justificativa);
		result.redirectTo(this).exibir(sol.getSiglaCompacta(), todoOContexto(), ocultas());
	}

	@Transacional
	@Path("/desentranhar")
	public void desentranhar(String sigla, String justificativa) throws Exception {
		if (sigla == null || sigla.trim().equals(""))
			throw new AplicacaoException("Número não informado");
		SrSolicitacao sol = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular()).selecionar(sigla);
		sol.desentranhar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(),
				justificativa);
		result.redirectTo(this).exibir(sol.getSiglaCompacta(), todoOContexto(), ocultas());
	}

	@Transacional
	@Path("/cancelar")
	public void cancelar(String sigla) throws Exception {
		if (sigla == null || sigla.trim().equals(""))
			throw new AplicacaoException("Número não informado");

		SrSolicitacao sol = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular()).selecionar(sigla);
		sol.cancelar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
		result.redirectTo(this).exibir(sol.getSiglaCompacta(), todoOContexto(), ocultas());
	}

	@Transacional
	@Path("/reabrir")
	public void reabrir(String sigla) throws Exception {
		if (sigla == null || sigla.trim().equals(""))
			throw new AplicacaoException("Número não informado");
		SrSolicitacao sol = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular()).selecionar(sigla);
		sol.reabrir(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
		result.redirectTo(this).exibir(sol.getSiglaCompacta(), todoOContexto(), ocultas());
	}

	@Transacional
	@Path("/deixarPendente")
	public void deixarPendente(String sigla, SrTipoMotivoPendencia motivo, String calendario, String horario,
			String detalheMotivo) throws Exception {
		if (sigla == null || sigla.trim().equals(""))
			throw new AplicacaoException("Número não informado");
		SrSolicitacao sol = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular()).selecionar(sigla);

		if (calendario != null && !calendario.equals("")) {
			calendario += " " + (horario != null && !horario.equals("") ? horario : "23:59");
		}

		sol.deixarPendente(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), motivo,
				detalheMotivo, SrViewUtil.fromDDMMYYYYHHMM(calendario));
		result.redirectTo(this).exibir(sol.getSiglaCompacta(), todoOContexto(), ocultas());
	}

	@Transacional
	@Path("/excluir")
	public void excluir(String sigla) throws Exception {
		if (sigla == null || sigla.trim().equals(""))
			throw new AplicacaoException("Número não informado");

		SrSolicitacao sol = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular()).selecionar(sigla);
		sol.excluir();
		result.redirectTo("/../siga/");
	}

	@Transacional
	@Path("/anexarArquivo")
	public void anexarArquivo(SrMovimentacao movimentacao) throws Exception {
		if (movimentacao == null || movimentacao.getArquivo() == null)
			throw new AplicacaoException("Não foram informados dados suficientes para a anexação");
		if (movimentacao.getAtendente() != null && movimentacao.getAtendente().getIdPessoa() == null)
			movimentacao.setAtendente(null);
		movimentacao.salvar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
		result.redirectTo(this).exibir(movimentacao.getSolicitacao().getSiglaCompacta(), todoOContexto(), ocultas());
	}

	@Path("/termoAtendimento")
	public void termoAtendimento(String sigla) throws Exception {
		if (sigla == null || sigla.trim().equals(""))
			throw new AplicacaoException("Número não informado");

		SrSolicitacao solicitacao = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular())
				.selecionar(sigla);
		if (solicitacao.isFilha()) {
			solicitacao.setDescrSolicitacao(solicitacao.getDescricao());
		}
		boolean todoOContexto = solicitacao.isParteDeArvore();
		Set<SrAtributoSolicitacao> atributos = solicitacao.getAtributoSolicitacaoSetAtual(todoOContexto);

		result.include(SOLICITACAO, solicitacao);
		result.include("atributos", atributos);
	}

	@Transacional
	@Path("/desfazerUltimaMovimentacao")
	public void desfazerUltimaMovimentacao(String sigla) throws Exception {
		if (sigla == null || sigla.trim().equals(""))
			throw new AplicacaoException("Número não informado");

		SrSolicitacao sol = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular()).selecionar(sigla);
		sol.desfazerUltimaMovimentacao(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
		result.redirectTo(this).exibir(sol.getSiglaCompacta(), todoOContexto(), ocultas());
	}

	@Transacional
	@Path("/alterarPrioridade")
	public void alterarPrioridade(String sigla, SrPrioridade prioridade) throws Exception {
		if (sigla == null || sigla.trim().equals(""))
			throw new AplicacaoException("Número não informado");
		SrSolicitacao sol = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular()).selecionar(sigla);
		sol.alterarPrioridade(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(),
				prioridade);
		result.redirectTo(this).exibir(sol.getSiglaCompacta(), todoOContexto(), ocultas());
	}

	@Transacional
	@Path("/terminarPendencia")
	public void terminarPendencia(String sigla, String descricao, Long idMovimentacao) throws Exception {
		if (sigla == null || sigla.trim().equals(""))
			throw new AplicacaoException("Número não informado");
		SrSolicitacao sol = (SrSolicitacao) new SrSolicitacao().setLotaTitular(getLotaTitular()).selecionar(sigla);
		sol.terminarPendencia(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(),
				descricao, idMovimentacao);
		result.redirectTo(this).exibir(sol.getSiglaCompacta(), todoOContexto(), ocultas());
	}

	@Transacional
	@Path("/darAndamento")
	public void darAndamento(SrMovimentacao movimentacao) throws Exception {
		movimentacao.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO));
		movimentacao.salvar(getCadastrante(), getLotaCadastrante(), getTitular(), getLotaTitular());
		result.redirectTo(this).exibir(movimentacao.getSolicitacao().getSiglaCompacta(), todoOContexto(), ocultas());
	}

	@Transacional
	@Path("/priorizarLista")
	public void priorizarLista(List<SrPrioridadeSolicitacao> listaPrioridadeSolicitacao, Long id) throws Exception {
		for (SrPrioridadeSolicitacao pNova : listaPrioridadeSolicitacao) {
			SrPrioridadeSolicitacao p = SrPrioridadeSolicitacao.AR.findById(pNova.getId());
			if (p.getNumPosicao() != pNova.getNumPosicao() || p.getPrioridade() != pNova.getPrioridade()
					|| p.getNaoReposicionarAutomatico() != pNova.getNaoReposicionarAutomatico()) {
				p.setNumPosicao(pNova.getNumPosicao());
				p.setPrioridade(pNova.getPrioridade());
				p.setNaoReposicionarAutomatico(pNova.getNaoReposicionarAutomatico());
				p.save();
			} else
				em().detach(p);
		}
		// exibirLista(id);
		result.use(Results.http()).setStatusCode(200);
	}

	@Get("/public/app/solicitacao/selecionar")
	@Post
	public void selecionarPublico(String sigla, String matricula) throws Exception {
		try {
			SrSolicitacao sol = new SrSolicitacao();
			if (matricula != null) {
				DpPessoa pes = dao().getPessoaFromSigla(matricula);
				if (pes != null)
					sol.setLotaTitular(pes.getLotacao());
			}
			sol = (SrSolicitacao) sol.selecionar(sigla);

			if (sol != null) {
				result.use(Results.http()).body("1;" + sol.getId() + ";" + sol.getSigla() + ";"
						+ "/sigasr/app/solicitacao/exibir/" + sol.getSiglaCompacta());
				return;
			}
		} catch (Exception ex) {

		}
		result.use(Results.http()).body("0");
	}

	@Get
	@Post
	@Path("/selecionar")
	public void selecionar(String sigla) throws Exception {
		SrSolicitacao sel = new SrSolicitacao();
		sel.setLotaTitular(getLotaTitular());
		sel = (SrSolicitacao) sel.selecionar(sigla);

		if (sel != null) {
			result.use(Results.http()).body("1;" + sel.getId() + ";" + sel.getSigla() + ";" + sel.getDescricao());
		} else {
			result.use(Results.http()).body("0");
		}
	}

	@SuppressWarnings("rawtypes")
	@Path("/gadget")
	public void gadget() {
		Query query = ContextoPersistencia.em().createNamedQuery("contarSrMarcas");
		query.setParameter("idPessoaIni", getTitular().getIdInicial());
		query.setParameter("idLotacaoIni", getLotaTitular().getIdInicial());
		query.setParameter("dbDatetime", CpDao.getInstance().consultarDataEHoraDoServidor());
		List contagens = query.getResultList();
		result.include("contagens", contagens);
	}

	@Transacional
	@Path("/atributo/gravar")
	public void gravarAtributo(SrAtributoSolicitacao atributo) throws Exception {
		SrAtributoSolicitacao atributoEntity = SrAtributoSolicitacao.AR.findById(atributo.getId());
		atributoEntity.gravar(atributo.getValorAtributoSolicitacao(), getCadastrante(), getLotaCadastrante());
		result.use(Results.http()).body(atributo.getValorAtributoSolicitacao());
	}

	@Transacional
	@Path("/atributo/excluir")
	public void excluirAtributo(Long id) throws Exception {
		SrAtributoSolicitacao atributoEntity = SrAtributoSolicitacao.AR.findById(id);
		atributoEntity.excluir(getCadastrante(), getLotaCadastrante());
		result.use(Results.status()).ok();
	}
}
