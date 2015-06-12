package br.gov.jfrj.siga.sr.vraptor;

import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.ADM_ADMINISTRAR;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.model.CpPerfilSelecao;
import br.gov.jfrj.siga.cp.model.DpCargoSelecao;
import br.gov.jfrj.siga.cp.model.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.annotation.AssertAcesso;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrArquivo;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrAtributoSolicitacao;
import br.gov.jfrj.siga.sr.model.SrAtributoSolicitacaoMap;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrFormaAcompanhamento;
import br.gov.jfrj.siga.sr.model.SrGravidade;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrLista;
import br.gov.jfrj.siga.sr.model.SrMeioComunicacao;
import br.gov.jfrj.siga.sr.model.SrMovimentacao;
import br.gov.jfrj.siga.sr.model.SrPrioridade;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao.SrTarefa;
import br.gov.jfrj.siga.sr.model.SrTendencia;
import br.gov.jfrj.siga.sr.model.SrTipoMotivoEscalonamento;
import br.gov.jfrj.siga.sr.model.SrTipoMotivoPendencia;
import br.gov.jfrj.siga.sr.model.SrTipoMovimentacao;
import br.gov.jfrj.siga.sr.model.SrTipoPermissaoLista;
import br.gov.jfrj.siga.sr.model.SrUrgencia;
import br.gov.jfrj.siga.sr.model.vo.SrSolicitacaoListaVO;
import br.gov.jfrj.siga.sr.util.AtualizacaoLista;
import br.gov.jfrj.siga.sr.util.SrSolicitacaoFiltro;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

import com.google.gson.Gson;

@Resource
@Path("app/solicitacao")
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
	private static final String PODEREMOVER = "podeEditar";
	private static final String PODEEDITAR = "podeRemover";
	private static final String PODEPRIORIZAR = "podePriorizar";
	private static final String FILTRO = "filtro";
	private static final String PRIORIDADELIST = "prioridadeList";    

    private Validator validator;

    public SolicitacaoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em,  SrValidator srValidator, Validator validator) {
        super(request, result, dao, so, em, srValidator);
        this.validator = validator;
    }

    @Path("/exibirAcao")
    public void exibirAcao(SrSolicitacao solicitacao) throws Exception {
        Map<SrAcao, List<SrTarefa>> acoesEAtendentes = solicitacao.getAcoesEAtendentes();
        result.include(SOLICITACAO, solicitacao);
        result.include(ACOES_E_ATENDENTES, acoesEAtendentes);
    }

    @Path("/exibirAtributos")
    public void exibirAtributos(SrSolicitacao solicitacao) throws Exception {
        result.include(SOLICITACAO, solicitacao);
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
		result.include("dpPessoaSel", new DpPessoaSelecao());
		result.include("lotacaoSel", new DpLotacaoSelecao());
		result.include("funcaoConfiancaSel", new DpFuncaoConfiancaSelecao());
		result.include("cargoSel", new DpCargoSelecao());
		result.include("cpGrupoSel", new CpPerfilSelecao());
		
		result.include("lotacaolotacaoAtualSel", new DpLotacaoSelecao());
        result.include("dpPessoapessoaAtualSel", new DpPessoaSelecao());
        result.include("funcaoConfiancafuncaoAtualSel", new DpFuncaoConfiancaSelecao());
        result.include("cargocargoAtualSel", new DpCargoSelecao());
    }

    @AssertAcesso(ADM_ADMINISTRAR)
    @Path("/gravarPermissaoUsoLista")
    public void gravarPermissaoUsoLista(SrConfiguracao permissao, List<SrTipoPermissaoLista> tipoPermissaoSet) throws Exception {
    	permissao.setTipoPermissaoSet(tipoPermissaoSet);
        permissao.salvarComoPermissaoUsoLista();

        result.use(Results.http()).body(permissao.toVO().toJson());
    }

    @AssertAcesso(ADM_ADMINISTRAR)
    @Path("/listarPermissaoUsoLista")
    public void listarPermissaoUsoLista(Long idLista, boolean mostrarDesativados) throws Exception {

        SrLista lista = new SrLista();
        if (idLista != null)
            lista = SrLista.AR.findById(idLista);
        List<SrConfiguracao> associacoes = SrConfiguracao.listarPermissoesUsoLista(lista, mostrarDesativados);

        result.use(Results.http()).body(SrConfiguracao.convertToJSon(associacoes));
    }
    
    @AssertAcesso(ADM_ADMINISTRAR)
    @Path("/desativarPermissaoUsoListaEdicao")
    public void desativarPermissaoUsoListaEdicao(Long idLista, Long idPermissao) throws Exception {
        SrConfiguracao configuracao = ContextoPersistencia.em().find(SrConfiguracao.class, idPermissao);
        configuracao.finalizar();

        result.use(Results.http()).body(configuracao.getSrConfiguracaoJson());
    }

    @Path("/configuracoesParaInclusaoAutomatica")
    public void configuracoesParaInclusaoAutomatica(Long idLista, boolean mostrarDesativados) throws Exception {
        SrLista lista = SrLista.AR.findById(idLista);

        result.use(Results.http()).body(SrConfiguracao.buscaParaConfiguracaoInsercaoAutomaticaListaJSON(lista.getListaAtual(), mostrarDesativados));
    }

    @Path("/configuracaoAutomaticaGravar")
    public void configuracaoAutomaticaGravar(SrConfiguracao configuracaoInclusaoAutomatica) throws Exception {
        configuracaoInclusaoAutomatica.salvarComoInclusaoAutomaticaLista(configuracaoInclusaoAutomatica.getListaPrioridade());
        result.use(Results.http()).body(configuracaoInclusaoAutomatica.toVO().toJson());
    }

    @Path("/desativarConfiguracaoAutomaticaGravar")
    public void desativarConfiguracaoAutomaticaGravar(Long id) throws Exception {
        SrConfiguracao configuracao = ContextoPersistencia.em().find(SrConfiguracao.class, id);
        configuracao.finalizar();
        result.use(Results.http()).body(configuracao.toVO().toJson());
    }

    @Path("/reativarConfiguracaoAutomaticaGravar")
    public void reativarConfiguracaoAutomaticaGravar(Long id) throws Exception {
        SrConfiguracao configuracao = ContextoPersistencia.em().find(SrConfiguracao.class, id);
        configuracao.salvar();
        result.use(Results.http()).body(configuracao.toVO().toJson());
    }

//    @Path({"/listarListaDesativados"})
    @Path("/buscarPermissoesLista")
    public void buscarPermissoesLista(Long idLista) throws Exception {
        List<SrConfiguracao> permissoes;

        if (idLista != null) {
            SrLista lista = SrLista.AR.findById(idLista);
            permissoes = new ArrayList<SrConfiguracao>(lista.getPermissoes(getTitular().getLotacao(), getCadastrante()));
            permissoes = SrConfiguracao.listarPermissoesUsoLista(lista, false);
        } else
            permissoes = new ArrayList<SrConfiguracao>();

        result.use(Results.http()).body(SrConfiguracao.convertToJSon(permissoes));
    }

    @Path("/gravarLista")
    public void gravarLista(SrLista lista) throws Exception {
        lista.setLotaCadastrante(getLotaTitular());
        validarFormEditarLista(lista);
        lista.salvar();
        result.use(Results.http()).body(lista.toJson());
    }

    private void validarFormEditarLista(SrLista lista) {
        if (lista.getNomeLista() == null || lista.getNomeLista().trim().equals("")) {
            srValidator.addError("lista.nomeLista", "Nome da Lista no informados");
        }

        if (srValidator.hasErrors()) {
            enviarErroValidacao();
        }
    }

    @Path("/desativarLista")
    public void desativarLista(Long id, boolean mostrarDesativados) throws Exception {
        SrLista lista = SrLista.AR.findById(id);
        lista.finalizar();

        result.use(Results.http()).body(lista.toJson());
    }

    @Path("/reativarLista")
    public void reativarLista(Long id, boolean mostrarDesativados) throws Exception {
        SrLista lista = SrLista.AR.findById(id);
        lista.salvar();
        result.use(Results.http()).body(lista.toJson());
    }

    @SuppressWarnings("unchecked")
    @Path("/exibirLista/{id}")
    public void exibirLista(Long id) throws Exception {
        SrLista lista = SrLista.AR.findById(id);
        List<CpOrgaoUsuario> orgaos = ContextoPersistencia.em().createQuery("from CpOrgaoUsuario").getResultList();
        List<CpComplexo> locais = CpComplexo.AR.all().fetch();
        List<SrTipoPermissaoLista> tiposPermissao = SrTipoPermissaoLista.AR.all().fetch();
        SrSolicitacaoFiltro filtro = new SrSolicitacaoFiltro();
        SrSolicitacaoListaVO solicitacaoListaVO;
        String tiposPermissaoJson = new Gson().toJson(tiposPermissao);
        filtro.setIdListaPrioridade(id);
        lista = lista.getListaAtual();
        String jsonPrioridades = SrPrioridade.getJSON().toString();

        if (!lista.podeConsultar(getLotaTitular(), getCadastrante())) {
            throw new Exception("Exibi��o n�o permitida");
        }

        try {
            solicitacaoListaVO = SrSolicitacaoListaVO.fromFiltro(filtro, true, "", false, getLotaTitular(), getCadastrante());
        } catch (Exception e) {
            e.printStackTrace();
            solicitacaoListaVO = new SrSolicitacaoListaVO();
        }

        result.include(LISTA, lista);
        result.include(PODEREMOVER, lista.podeRemover(getLotaTitular(), getCadastrante()));
        result.include(PODEEDITAR, lista.podeEditar(getLotaTitular(), getCadastrante()));
        result.include(PODEPRIORIZAR, lista.podePriorizar(getLotaTitular(), getCadastrante()));
        result.include(ORGAOS, orgaos);
        result.include(LOCAIS, locais);
        result.include(TIPOS_PERMISSAO, tiposPermissao);
        result.include(SOLICITACAO_LISTA_VO, solicitacaoListaVO);
        result.include(FILTRO, filtro);
        result.include(TIPOS_PERMISSAO_JSON, tiposPermissaoJson);
        result.include("jsonPrioridades", jsonPrioridades);
        result.include(PRIORIDADELIST, SrPrioridade.values());
        
        
        result.include("lotacaoParaInclusaoAutomaticaSel", new DpLotacaoSelecao());
        result.include("prioridades", SrPrioridade.getValoresEmOrdem());
        result.include(LOTA_TITULAR, getLotaTitular());
        result.include(CADASTRANTE, getCadastrante());
    }

    @Path("/gravar")
    public void gravar(SrSolicitacao solicitacao) throws Exception {
        if (!solicitacao.isRascunho() && !validarFormEditar(solicitacao)) {
        	incluirListasEdicaoSolicitacao(solicitacao);
            validator.onErrorUsePageOf(SolicitacaoController.class).editar(solicitacao.getId());
            
        	return;
        }

        solicitacao.salvar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
        result.redirectTo(SolicitacaoController.class).exibir(solicitacao.getId(), todoOContexto(), ocultas());
    }

    private boolean validarFormEditar(SrSolicitacao solicitacao) throws Exception {
        if (solicitacao.getSolicitante() == null || solicitacao.getSolicitante().getId() == null) {
            validator.add(new ValidationMessage("Solicitante não informado", "solicitacao.solicitante"));
        }
        if (solicitacao.getItemConfiguracao() == null || solicitacao.getItemConfiguracao().getId() == null) {
            validator.add(new ValidationMessage("Item n&atilde;o informado", "solicitacao.itemConfiguracao"));
        }
        if (solicitacao.getAcao() == null || solicitacao.getAcao().getId() == null) {
            validator.add(new ValidationMessage("A&ccedil&atilde;o n&atilde;o informada", "solicitacao.acao"));
        }

        if (solicitacao.getDescrSolicitacao() == null || "".equals(solicitacao.getDescrSolicitacao().trim())) {
            validator.add(new ValidationMessage("Descri&ccedil&atilde;o n&atilde;o informada", "solicitacao.descrSolicitacao"));
        }

        Map<Long, Boolean> obrigatorio = solicitacao.getObrigatoriedadeTiposAtributoAssociados();
        for (int i = 0; i < solicitacao.getAtributoSolicitacaoSet().size(); i++) {
        	SrAtributoSolicitacao att = solicitacao.getAtributoSolicitacaoSet().get(i);
        	
            // Para evitar NullPointerExcetpion quando nao encontrar no Map
            if (Boolean.TRUE.equals(obrigatorio.get(att.getAtributo().getIdAtributo()))) {
                if ((att.getValorAtributoSolicitacao() == null || "".equals(att.getValorAtributoSolicitacao().trim()))) {
                	validator.add(new ValidationMessage(att.getAtributo().getNomeAtributo() + " n&atilde;o informado", "solicitacao.atributoSolicitacaoMap[" + i + "]"));
                }
            }
        }
        
        return !validator.hasErrors();
    }

	public boolean todoOContexto() {
        return true;
        // return Boolean.parseBoolean(params.get("todoOContexto"));
    }

    public boolean ocultas() {
        return true;
        // return Boolean.parseBoolean(params.get("ocultas"));
    }

    @Path({"/exibir/{id}", "/exibir/{id}/{todoOContexto}/{ocultas}"})
    public void exibir(Long id, Boolean todoOContexto, Boolean ocultas) throws Exception {
        SrSolicitacao solicitacao = SrSolicitacao.AR.findById(id);
        if (solicitacao == null)
            throw new Exception("Solicita��o n�o encontrada");
        else
            solicitacao = solicitacao.getSolicitacaoAtual();

        if (solicitacao == null)
            throw new Exception("Esta solicita��o foi exclu�da");

        SrMovimentacao movimentacao = new SrMovimentacao(solicitacao);

        List<DpPessoa> atendentes = solicitacao.getPessoasAtendentesDisponiveis();

        if (todoOContexto == null)
            todoOContexto = solicitacao.isParteDeArvore();
        // Edson: foi solicitado que funcionasse do modo abaixo. Eh o melhor modo??
        // todoOContexto = solicitacao.solicitacaoPai == null ? true : false;
        if (ocultas == null)
            ocultas = false;

        Set<SrMovimentacao> movs = solicitacao.getMovimentacaoSet(ocultas, null, false, todoOContexto, !ocultas, false);

        result.include(SOLICITACAO, solicitacao);
        result.include("movimentacao", movimentacao);
        result.include("todoOContexto", todoOContexto);
        result.include("ocultas", ocultas);
        result.include("movs", movs);
        result.include("atendentes", atendentes);
    }

    @Path("/exibirLocalRamalEMeioContato")
    public void exibirLocalRamalEMeioContato(SrSolicitacao solicitacao, DpPessoa solicitante) throws Exception {
        if (solicitacao == null || solicitacao.getCadastrante() == null)
            solicitacao = criarSolicitacaoComSolicitante();

        // preenche com os dados da �ltima solicita��o do usu�rio
        solicitacao.deduzirLocalRamalEMeioContato();

        result.include(SOLICITACAO, solicitacao);
        result.include("locaisDisponiveis", solicitacao.getLocaisDisponiveis());
        result.include("meiosComunicadaoList", SrMeioComunicacao.values());
    }

    @Path("/exibirItemConfiguracao")
    public void exibirItemConfiguracao(SrSolicitacao solicitacao) throws Exception {
        if (solicitacao.getSolicitante() == null)
            result.include(SOLICITACAO, solicitacao);

        else if (!solicitacao.getItensDisponiveis().contains(solicitacao.getItemConfiguracao())) {
            solicitacao.setItemConfiguracao(null);

            DpPessoa titular = solicitacao.getTitular();
            DpLotacao lotaTitular = solicitacao.getLotaTitular();
            Map<SrAcao, List<SrTarefa>> acoesEAtendentes = solicitacao.getAcoesEAtendentes();

            result.include(SOLICITACAO, solicitacao);
            result.include(TITULAR, titular);
            result.include(LOTA_TITULAR, lotaTitular);
            result.include(ACOES_E_ATENDENTES, acoesEAtendentes);
        }
    }

    public void exibirConhecimentosRelacionados(SrSolicitacao solicitacao) throws Exception {
        result.include(SOLICITACAO, solicitacao);
        result.include("podeUtilizarServico",podeUtilizarServico("SIGA;GC"));
    }

    @Path("/exibirPrioridade")
    public void exibirPrioridade(SrSolicitacao solicitacao) {
        solicitacao.associarPrioridadePeloGUT();

        result.include(SOLICITACAO, solicitacao);
        result.include(PRIORIDADELIST, SrPrioridade.values());
    }

    @Path("/listarSolicitacoesRelacionadas")
    public void listarSolicitacoesRelacionadas(SrSolicitacaoFiltro solicitacao, List<SrAtributoSolicitacaoMap> atributoSolicitacaoMap) throws Exception {

        //solicitacao.setAtributoSolicitacaoMap(atributoSolicitacaoMap);
        List<Object[]> solicitacoesRelacionadas = solicitacao.buscarSimplificado();

        result.include("solicitacoesRelacionadas", solicitacoesRelacionadas);
    }

    // DB1: foi necess�rio receber e passar o parametro "nome"(igual ao buscarItem())
    // para chamar a function javascript correta,
    // e o parametro "popup" porque este metodo � usado tamb�m na lista,
    // e n�o foi poss�vel deixar default no template(igual ao buscarItem.html)
    @SuppressWarnings("unchecked")
    @Path("/buscar")
    public void buscar(SrSolicitacaoFiltro filtro, String propriedade, boolean popup) throws Exception {
        SrSolicitacaoListaVO solicitacaoListaVO;
        try {
            if (filtro.isPesquisar()) {
                filtro.carregarSelecao();
                solicitacaoListaVO = SrSolicitacaoListaVO.fromFiltro(filtro, false, propriedade, popup, getLotaTitular(), getCadastrante());
            } else {
                solicitacaoListaVO = new SrSolicitacaoListaVO();
            }
        } catch (Exception e) {
            e.printStackTrace();
            solicitacaoListaVO = new SrSolicitacaoListaVO();
        }

        // Montando o filtro...
        String[] tipos = new String[] { "Pessoa", "Lota��o" };
        List<CpMarcador> marcadores = ContextoPersistencia.em().createQuery("select distinct cpMarcador from SrMarca").getResultList();

        List<SrAtributo> atributosDisponiveisAdicao = atributosDisponiveisAdicaoConsulta(filtro);
        List<SrLista> listasPrioridade = SrLista.listar(false);

        result.include("solicitacaoListaVO", solicitacaoListaVO);
        result.include("tipos", tipos);
        result.include("marcadores", marcadores);
        result.include("filtro", filtro);
        result.include("propriedade", propriedade);
        result.include("popup", popup);
        result.include("atributosDisponiveisAdicao", atributosDisponiveisAdicao);
        result.include("listasPrioridade", listasPrioridade);
        result.include("prioridadesEnum", SrPrioridade.values());
    }

    public List<SrAtributo> atributosDisponiveisAdicaoConsulta(SrSolicitacaoFiltro filtro) throws Exception {
        List<SrAtributo> listaAtributosAdicao = new ArrayList<SrAtributo>();
        List<SrAtributoSolicitacaoMap> atributoMap = filtro.getAtributoSolicitacaoMap();

        for (SrAtributo srAtributo : SrAtributo.listarParaSolicitacao(Boolean.FALSE)) {
        	SrAtributoSolicitacaoMap atrib = new SrAtributoSolicitacaoMap(srAtributo.getIdAtributo(),srAtributo.getDescrAtributo());
            if (!atributoMap.contains(atrib)) {
                listaAtributosAdicao.add(srAtributo);
            }
        }
        return listaAtributosAdicao;
    }

	@Path({ "/editar", "/editar/{id}", "/exibir/editar" })
    public void editar(Long id) throws Exception {
        SrSolicitacao solicitacao;

        if (id == null) {
            solicitacao = new SrSolicitacao();
            solicitacao.setSolicitante(getTitular());
        } else
            solicitacao = SrSolicitacao.AR.findById(id);

        if (solicitacao.getDtOrigem() == null)
            solicitacao.setDtOrigem(new Date());
        if (solicitacao.getDtIniEdicao() == null)
            solicitacao.setDtIniEdicao(new Date());
        solicitacao.atualizarAcordos();

        incluirListasEdicaoSolicitacao(solicitacao);
    }

	@SuppressWarnings("unchecked")
	private void incluirListasEdicaoSolicitacao(SrSolicitacao solicitacao)
			throws Exception {
		List<CpComplexo> locais = ContextoPersistencia.em().createQuery("from CpComplexo").getResultList();

        Map<SrAcao, List<SrTarefa>> acoesEAtendentes = solicitacao.getAcoesEAtendentes();

        DpPessoaSelecao pessoaSel = new DpPessoaSelecao();
        pessoaSel.setId(getCadastrante().getId());
        pessoaSel.buscar();
        result.include("solicitacao.solicitante", pessoaSel);

        result.include(SOLICITACAO, solicitacao);
        result.include("locais", locais);
        result.include(ACOES_E_ATENDENTES, acoesEAtendentes);
        result.include("formaAcompanhamentoList", SrFormaAcompanhamento.values());
        result.include("gravidadeList", SrGravidade.values());
        result.include("tipoMotivoEscalonamentoList", SrTipoMotivoEscalonamento.values());
        result.include("urgenciaList", SrUrgencia.values());
        result.include("tendenciaList", SrTendencia.values());
        result.include(PRIORIDADELIST, SrPrioridade.values());
        result.include("locaisDisponiveis", solicitacao.getLocaisDisponiveis());
        result.include("meiosComunicadaoList", SrMeioComunicacao.values());
        result.include("itemConfiguracao", solicitacao.getItemConfiguracao());
        result.include("podeUtilizarServicoSigaGC", false);
	}

    @Path("/retirarDeLista")
    public void retirarDeLista(Long idSolicitacao, Long idLista) throws Exception {
        SrSolicitacao solicitacao = SrSolicitacao.AR.findById(idSolicitacao);
        SrLista lista = SrLista.AR.findById(idLista);
        solicitacao.retirarDeLista(lista, getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
        result.forwardTo(this).exibirLista(idLista);
    }

    private SrSolicitacao criarSolicitacaoComSolicitante() {
        SrSolicitacao solicitacao = new SrSolicitacao();
        solicitacao.setCadastrante(getCadastrante());
        solicitacao.setSolicitante(getTitular());

        return solicitacao;
    }

    @Path("/exibir/incluirEmLista")
    public void incluirEmLista(Long id) throws Exception {
        SrSolicitacao solicitacao = SrSolicitacao.AR.findById(id);
        solicitacao = solicitacao.getSolicitacaoAtual();
        List<SrPrioridade> prioridades = SrPrioridade.getValoresEmOrdem();

        result.include("solicitacao", solicitacao);
        result.include("prioridades", prioridades);
    }
    
    @Path("/exibir/incluirEmListaGravar")
    public void incluirEmListaGravar(Long idSolicitacao, Long idLista, SrPrioridade prioridade, Boolean naoReposicionarAutomatico) throws Exception {
        if (idLista == null) {
            throw new Exception("Selecione a lista para inclus�o da solicita��o");
        }
        SrSolicitacao solicitacao = SrSolicitacao.AR.findById(idSolicitacao);
        SrLista lista = SrLista.AR.findById(idLista);
        solicitacao.incluirEmLista(lista, getCadastrante(), getLotaTitular(), prioridade, naoReposicionarAutomatico);
        exibir(idSolicitacao, todoOContexto(), ocultas());
    }

    @Path("/exibir/fechar")
    public void fechar(Long id, String motivo) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.fechar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), motivo);
        result.redirectTo(this).exibir(sol.getIdSolicitacao(), todoOContexto(), ocultas());
    }

    @Path("/exibir/responderPesquisa")
    public void responderPesquisa(Long id) throws Exception {
        /*
         * SrSolicitacao sol = SrSolicitacao.findById(id); SrPesquisa pesquisa = sol.getPesquisaDesignada(); if (pesquisa == null) throw new
         * Exception("Não foi encontrada nenhuma pesquisa designada para esta solicitação."); pesquisa = SrPesquisa.findById(pesquisa.idPesquisa); pesquisa = pesquisa.getPesquisaAtual(); render(id,
         * pesquisa);
         */
    }

    
    @Path("/responderPesquisaGravar")
    public void responderPesquisaGravar(Long id, Map<Long, String> respostaMap) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.responderPesquisa(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), respostaMap);
        exibir(id, todoOContexto(), ocultas());
    }

    @Path("/baixar/{idArquivo}")
    public Download baixar(Long idArquivo) throws Exception {
        SrArquivo arq = SrArquivo.AR.findById(idArquivo);
        final InputStream inputStream = new ByteArrayInputStream(arq.getBlob());
        return new InputStreamDownload(inputStream, "text/plain", arq.getNomeArquivo());
    }

    @Path("/exibir/escalonar")
    public void escalonar(Long id) throws Exception {
        SrSolicitacao solicitacao = SrSolicitacao.AR.findById(id);
        solicitacao.setTitular(getTitular());
        solicitacao.setLotaTitular(getLotaTitular());
        solicitacao = solicitacao.getSolicitacaoAtual();
        Map<SrAcao, List<SrTarefa>> acoesEAtendentes = solicitacao.getAcoesEAtendentes();

        result.include("solicitacao", solicitacao);
        result.include("acoesEAtendentes", acoesEAtendentes);
    }
    
    @Path("/escalonarGravar")
    public void escalonarGravar(Long id, Long itemConfiguracao, SrAcao acao, Long idAtendente, Long idAtendenteNaoDesignado, Long idDesignacao, SrTipoMotivoEscalonamento motivo, String descricao,
            Boolean criaFilha, Boolean fechadoAuto) throws Exception {
        if (itemConfiguracao == null || acao == null || acao.getIdAcao() == null || acao.getIdAcao().equals(0L))
            throw new Exception("Opera��o nao permitida. Necessario informar um item de configura��o " + "e uma a��o.");
        SrSolicitacao solicitacao = SrSolicitacao.AR.findById(id);

        DpLotacao atendenteNaoDesignado = null;
        DpLotacao atendente = null;
        if (idAtendente != null)
            atendente = ContextoPersistencia.em().find(DpLotacao.class, idAtendente);
        if (idAtendenteNaoDesignado != null)
            atendenteNaoDesignado = ContextoPersistencia.em().find(DpLotacao.class, idAtendenteNaoDesignado);

        if (criaFilha) {
            if (fechadoAuto != null) {
                solicitacao.setFechadoAutomaticamente(fechadoAuto);
                solicitacao.save();
            }
            SrSolicitacao filha = null;
            if (solicitacao.isFilha())
                filha = solicitacao.getSolicitacaoPai().criarFilhaSemSalvar();
            else
                filha = solicitacao.criarFilhaSemSalvar();
            filha.setItemConfiguracao(SrItemConfiguracao.AR.findById(itemConfiguracao));
            filha.setAcao(SrAcao.AR.findById(acao.getIdAcao()));
            filha.setDesignacao(SrConfiguracao.AR.findById(idDesignacao));
            filha.setDescrSolicitacao(descricao);
            if (idAtendenteNaoDesignado != null)
                filha.setAtendenteNaoDesignado(atendenteNaoDesignado);
            filha.salvar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
            exibir(filha.getIdSolicitacao(), todoOContexto(), ocultas());
        } else {
            SrMovimentacao mov = new SrMovimentacao(solicitacao);
            mov.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO));
            mov.setItemConfiguracao(SrItemConfiguracao.AR.findById(itemConfiguracao));
            mov.setAcao(SrAcao.AR.findById(acao.getIdAcao()));
            mov.setLotaAtendente(atendenteNaoDesignado != null ? atendenteNaoDesignado : atendente);
            if (solicitacao.getAtendente() != null && !mov.getLotaAtendente().equivale(solicitacao.getAtendente().getLotacao()))
                mov.setAtendente(null);
            mov.setMotivoEscalonamento(motivo);
            mov.setDesignacao(SrConfiguracao.AR.findById(idDesignacao));
            mov.setDescrMovimentacao("Motivo: " + mov.getMotivoEscalonamento() + "; Item: " + mov.getItemConfiguracao().getTituloItemConfiguracao() + "; A��o: " + mov.getAcao().getTituloAcao()
                    + "; Atendente: " + mov.getLotaAtendente().getSigla());
            mov.salvar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
            exibir(solicitacao.getIdSolicitacao(), todoOContexto(), ocultas());
        }
    }

    @Path("/exibirAcaoEscalonar")
    public void exibirAcaoEscalonar(Long id, Long itemConfiguracao) throws Exception {
        SrSolicitacao solicitacao = SrSolicitacao.AR.findById(id);
        solicitacao.setTitular(getTitular());
        solicitacao.setLotaTitular(getLotaTitular());
        Map<SrAcao, List<SrTarefa>> acoesEAtendentes = new HashMap<SrAcao, List<SrTarefa>>();
        if (itemConfiguracao != null) {
            solicitacao.setItemConfiguracao(SrItemConfiguracao.AR.findById(itemConfiguracao));
            acoesEAtendentes = solicitacao.getAcoesEAtendentes();
        }
        result.include(SOLICITACAO, solicitacao);
        result.include(ACOES_E_ATENDENTES, acoesEAtendentes);
    }
    
    @Path("/exibir/vincular")
    public void vincular(Long idSolicitacaoAVincular, Long idSolicitacaoRecebeVinculo, String justificativa) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(idSolicitacaoAVincular);
        SrSolicitacao solRecebeVinculo = SrSolicitacao.AR.findById(idSolicitacaoRecebeVinculo);
        sol.vincular(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), solRecebeVinculo, justificativa);
        exibir(idSolicitacaoAVincular, todoOContexto(), ocultas());
    }
    
    @Path("/exibir/juntar")
    public void juntar(Long idSolicitacaoAJuntar, Long idSolicitacaoRecebeJuntada, String justificativa) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(idSolicitacaoAJuntar);
        SrSolicitacao solRecebeJuntada = SrSolicitacao.AR.findById(idSolicitacaoRecebeJuntada);
        sol.juntar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), solRecebeJuntada, justificativa);
        exibir(idSolicitacaoAJuntar, todoOContexto(), ocultas());
    }
    
    @Path("/exibir/desentranhar")
    public void desentranhar(Long id, String justificativa) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.desentranhar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), justificativa);
        exibir(id, todoOContexto(), ocultas());
    }
    
    @Path("/exibir/cancelar")
    public void cancelar(Long id) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.cancelar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
        exibir(id, todoOContexto(), ocultas());
    }
    
    @Path("/exibir/reabrir")
    public void reabrir(Long id) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.reabrir(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
        exibir(id, todoOContexto(), ocultas());
    }
    
    @Path("/exibir/deixarPendente")
    public void deixarPendente(Long id, SrTipoMotivoPendencia motivo, String calendario, String horario, String detalheMotivo) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.deixarPendente(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), motivo, calendario, horario, detalheMotivo);
        exibir(id, todoOContexto(), ocultas());
    }
    
    @Path("/exibir/excluir")
    public void excluir(Long id) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.excluir();
        editar(null);
    }
    
    @Path("/exibir/anexarArquivo")
    public void anexarArquivo(SrMovimentacao movimentacao) throws Exception {
        movimentacao.salvar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
        exibir(movimentacao.getSolicitacao().getIdSolicitacao(), todoOContexto(), ocultas());
    }
    
    @Path("/exibir/termoAtendimento")
    public void termoAtendimento(Long id) throws Exception {
        SrSolicitacao solicitacao = SrSolicitacao.AR.findById(id);

        result.include("solicitacao", solicitacao);
    }
    
    @Path("/exibir/desfazerUltimaMovimentacao")
    public void desfazerUltimaMovimentacao(Long id) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.desfazerUltimaMovimentacao(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
        exibir(id, todoOContexto(), ocultas());
    }
    
    @Path("/exibir/alterarPrazo")
    public void alterarPrazo(Long id, String motivo, String calendario, String horario) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.alterarPrazo(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), motivo, calendario, horario);
        exibir(id, todoOContexto(), ocultas());
    }
    
    @Path("/exibir/terminarPendencia")
    public void terminarPendencia(Long id, String descricao, Long idMovimentacao) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.terminarPendencia(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), descricao, idMovimentacao);
        exibir(id, todoOContexto(), ocultas());
    }
    
    @Path("/exibir/darAndamento")
    public void darAndamento(SrMovimentacao movimentacao) throws Exception {
        movimentacao.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO));
        movimentacao.salvar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
        result.redirectTo(this).exibir(movimentacao.getSolicitacao().getIdSolicitacao(), todoOContexto(), ocultas());
    }

    @Path("/exibir/priorizarLista")
    public void priorizarLista(List<AtualizacaoLista> listaPrioridadeSolicitacao, Long id) throws Exception {
        SrLista lista = SrLista.AR.findById(id);
        lista.priorizar(getCadastrante(), getLotaTitular(), listaPrioridadeSolicitacao);
        exibirLista(id);
        result.use(Results.http()).setStatusCode(200);
    }
    
    @Get
	@Post
    @Path("/selecionar")
    public void selecionar(String sigla) throws Exception {
        SrSolicitacao sel = new SrSolicitacao();
        sel.setLotaTitular(getLotaTitular());
        sel = (SrSolicitacao) sel.selecionar(sigla);
        
        if (sel != null) {
        	result.include("sel", sel);
        	result.forwardTo("../../jsp/ajax_retorno.jsp");
        }
        else {
        	result.forwardTo("../../jsp/ajax_vazio.jsp");
        }
    }
}