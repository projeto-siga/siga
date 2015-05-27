package br.gov.jfrj.siga.sr.vraptor;

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

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrArquivo;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrAtributoSolicitacao;
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
import br.gov.jfrj.siga.sr.model.SrTipoMovimentacao;
import br.gov.jfrj.siga.sr.model.SrTipoPermissaoLista;
import br.gov.jfrj.siga.sr.model.SrUrgencia;
import br.gov.jfrj.siga.sr.model.vo.SrSolicitacaoListaVO;
import br.gov.jfrj.siga.sr.notifiers.Correio;
import br.gov.jfrj.siga.sr.util.SrSolicitacaoFiltro;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

import com.google.gson.Gson;

@Resource
@Path("app/solicitacao")
public class SolicitacaoController extends SrController {
	
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

    private Correio correio;

    public SolicitacaoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em, SrValidator srValidator, Correio correio) {
        super(request, result, dao, so, em, srValidator);
        this.correio = correio;
    }

    @Path("/exibirAcao")
    public void exibirAcao(SrSolicitacao solicitacao) throws Exception {
        Map<SrAcao, List<SrTarefa>> acoesEAtendentes = solicitacao.getAcoesEAtendentes();
        result.include("solicitacao", solicitacao);
        result.include("acoesEAtendentes", acoesEAtendentes);
    }

    @Path("/exibirAtributos")
    public void exibirAtributos(SrSolicitacao solicitacao) throws Exception {
        result.include("solicitacao", solicitacao);
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
        
    }

    public String configuracoesParaInclusaoAutomatica(Long idLista, boolean mostrarDesativados) throws Exception {
        SrLista lista = SrLista.AR.findById(idLista);
        return SrConfiguracao.buscaParaConfiguracaoInsercaoAutomaticaListaJSON(lista.getListaAtual(), mostrarDesativados);
    }

    /**
     * Recupera as {@link SrConfiguracao permissoes} de uma {@link SrLista lista}.
     *
     * @param idObjetivo
     *            - ID da lista
     * @return - String contendo a lista no formato jSon
     */
    public String buscarPermissoesLista(Long idLista) throws Exception {
        List<SrConfiguracao> permissoes;

        if (idLista != null) {
            SrLista lista = SrLista.AR.findById(idLista);

            // permissoes = new ArrayList<SrConfiguracao>(lista.getPermissoes(lotaTitular(), cadastrante()));
            permissoes = SrConfiguracao.listarPermissoesUsoLista(lista, false);
        } else
            permissoes = new ArrayList<SrConfiguracao>();

        return SrConfiguracao.convertToJSon(permissoes);
    }
    
    @Path("/listarListaDesativados")
    public void listarListaDesativados() throws Exception {
        listarLista(Boolean.TRUE);
    }     
    
    //@Path("/listarLista/gravar", "/gravarLista")
    @Path("/gravarLista")
    public void gravarLista(SrLista lista) throws Exception {
        lista.setLotaCadastrante(getLotaTitular());
        validarFormEditarLista(lista);
        lista.salvar();
        result.use(Results.http()).body(lista.toJson());
    }
    
    private void validarFormEditarLista(SrLista lista) {
        if (lista.getNomeLista() == null || lista.getNomeLista().trim().equals("")) {
            srValidator.addError("lista.nomeLista", "Nome da Lista não informados");
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
        result.include(ORGAOS, orgaos);
        result.include(LOCAIS, locais);
        result.include(TIPOS_PERMISSAO, tiposPermissao);
        result.include(SOLICITACAO_LISTA_VO, solicitacaoListaVO);
        result.include(TIPOS_PERMISSAO_JSON, tiposPermissaoJson);
        result.include("jsonPrioridades", jsonPrioridades);

    }
    
    @Path("/gravar")
    public void gravar(SrSolicitacao solicitacao) throws Exception {
        if (!solicitacao.isRascunho())
            validarFormEditar(solicitacao);

        solicitacao.salvar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
        result.redirectTo(SolicitacaoController.class).exibir(solicitacao.getId(), todoOContexto(), ocultas());
    }

    private void validarFormEditar(SrSolicitacao solicitacao) throws Exception {

        if (solicitacao.getSolicitante() == null) {
            srValidator.addError("solicitacao.solicitante", "Solicitante n&atilde;o informado");
        }
        if (solicitacao.getItemConfiguracao() == null) {
            srValidator.addError("solicitacao.itemConfiguracao", "Item n&atilde;o informado");
        }
        if (solicitacao.getAcao() == null) {
            srValidator.addError("solicitacao.acao", "A&ccedil&atilde;o n&atilde;o informada");
        }

        if (solicitacao.getDescrSolicitacao() == null || solicitacao.getDescrSolicitacao().trim().equals("")) {
            srValidator.addError("solicitacao.descrSolicitacao", "Descri&ccedil&atilde;o n&atilde;o informada");
        }

        HashMap<Long, Boolean> obrigatorio = solicitacao.getObrigatoriedadeTiposAtributoAssociados();
        for (SrAtributoSolicitacao att : solicitacao.getAtributoSolicitacaoSet()) {
            // Para evitar NullPointerExcetpion quando nao encontrar no Map
            if (Boolean.TRUE.equals(obrigatorio.get(att.getAtributo().getIdAtributo()))) {
                if ((att.getValorAtributoSolicitacao() == null || att.getValorAtributoSolicitacao().trim().equals("")))
                    srValidator.addError("solicitacao.atributoSolicitacaoMap[" + att.getAtributo().getIdAtributo() + "]", att.getAtributo().getNomeAtributo() + " n&atilde;o informado");
            }
        }

        if (srValidator.hasErrors()) {
        	enviarErroValidacao();
        }
    }


    public boolean todoOContexto() {
        return true;
        // return Boolean.parseBoolean(params.get("todoOContexto"));
    }
    

    public boolean ocultas() {
        return true;
        // return Boolean.parseBoolean(params.get("ocultas"));
    }

    @Path("/exibir/{id}/{todoOContexto}/{ocultas}")
    public void exibir(Long id, Boolean todoOContexto, Boolean ocultas) throws Exception {
        SrSolicitacao solicitacao = SrSolicitacao.AR.findById(id);
        if (solicitacao == null)
            throw new Exception("Solicitaï¿½ï¿½o nï¿½o encontrada");
        else
            solicitacao = solicitacao.getSolicitacaoAtual();

        if (solicitacao == null)
            throw new Exception("Esta solicitaï¿½ï¿½o foi excluï¿½da");

        SrMovimentacao movimentacao = new SrMovimentacao(solicitacao);

        if (todoOContexto == null)
            todoOContexto = solicitacao.isParteDeArvore();
        // Edson: foi solicitado que funcionasse do modo abaixo. Eh o melhor modo??
        // todoOContexto = solicitacao.solicitacaoPai == null ? true : false;
        if (ocultas == null)
            ocultas = false;

        Set<SrMovimentacao> movs = solicitacao.getMovimentacaoSet(ocultas, null, false, todoOContexto, !ocultas, false);
        
        result.include("solicitacao",solicitacao);
        result.include("movimentacao", movimentacao);
        result.include("todoOContexto",todoOContexto);
        result.include("ocultas",ocultas);
        result.include("movs",movs);
    }
    
    @Path("/exibirLocalRamalEMeioContato")
    public void exibirLocalRamalEMeioContato(SrSolicitacao solicitacao, DpPessoa solicitante) throws Exception {
    	if (solicitacao == null || solicitacao.getCadastrante() == null)
    		solicitacao = criarSolicitacaoComSolicitante();
    	
    	// preenche com os dados da Ãºltima solicitaÃ§Ã£o do usuÃ¡rio
    	solicitacao.deduzirLocalRamalEMeioContato();
    	
    	result.include("solicitacao", solicitacao);
    	result.include("locaisDisponiveis", solicitacao.getLocaisDisponiveis());
    	result.include("meiosComunicadaoList", SrMeioComunicacao.values());
    }

    public void exibirItemConfiguracao(SrSolicitacao solicitacao) throws Exception {
        if (solicitacao.getSolicitante() == null)
        	// render(solicitacao);
            result.include("solicitacao", solicitacao);

        else if (!solicitacao.getItensDisponiveis().contains(solicitacao.getItemConfiguracao())) {
        	solicitacao.setItemConfiguracao(null);

            DpPessoa titular = solicitacao.getTitular();
            DpLotacao lotaTitular = solicitacao.getLotaTitular();
            Map<SrAcao, List<SrTarefa>> acoesEAtendentes = solicitacao.getAcoesEAtendentes();
            
            result.include("solicitacao", solicitacao);
            result.include("titular", titular);
            result.include(LOTA_TITULAR, lotaTitular);
            result.include("acoesEAtendentes", acoesEAtendentes);
           // render(solicitacao, titular, lotaTitular, acoesEAtendentes);
        }
    }
    public void exibirConhecimentosRelacionados(SrSolicitacao solicitacao) throws Exception {
        // render(solicitacao);
    	
    	result.include("solicitacao", solicitacao);
    }
    public void exibirPrioridade(SrSolicitacao solicitacao) {
        solicitacao.associarPrioridadePeloGUT();
        // render(solicitacao);
    }
    public void listarSolicitacoesRelacionadas(SrSolicitacaoFiltro solicitacao, HashMap<Long, String> atributoSolicitacaoMap) throws Exception {

        solicitacao.setAtributoSolicitacaoMap(atributoSolicitacaoMap);
        List<Object[]> solicitacoesRelacionadas = solicitacao.buscarSimplificado();
        // render(solicitacoesRelacionadas);
    }

    // DB1: foi necessï¿½rio receber e passar o parametro "nome"(igual ao buscarItem())
    // para chamar a function javascript correta,
    // e o parametro "popup" porque este metodo ï¿½ usado tambï¿½m na lista,
    // e nï¿½o foi possï¿½vel deixar default no template(igual ao buscarItem.html)
    @SuppressWarnings("unchecked")
    @Path("/buscar")
    public void buscar(SrSolicitacaoFiltro filtro, String nome, boolean popup) throws Exception {
        SrSolicitacaoListaVO solicitacaoListaVO;
        try {
            if (filtro.isPesquisar()) {
                filtro.carregarSelecao();
                solicitacaoListaVO = SrSolicitacaoListaVO.fromFiltro(filtro, false, nome, popup, getLotaTitular(), getCadastrante());
            } else {
                solicitacaoListaVO = new SrSolicitacaoListaVO();
            }
        } catch (Exception e) {
            e.printStackTrace();
            solicitacaoListaVO = new SrSolicitacaoListaVO();
        }

        // Montando o filtro...
        String[] tipos = new String[] { "Pessoa", "Lotação" };
        List<CpMarcador> marcadores = ContextoPersistencia.em().createQuery("select distinct cpMarcador from SrMarca").getResultList();

        List<SrAtributo> atributosDisponiveisAdicao = atributosDisponiveisAdicaoConsulta(filtro);
        List<SrLista> listasPrioridade = SrLista.listar(false);

        result.include("solicitacaoListaVO", solicitacaoListaVO);
        result.include("tipos", tipos);
        result.include("marcadores", marcadores);
        result.include("filtro", filtro);
        result.include("nome", nome);
        result.include("popup", popup);
        result.include("atributosDisponiveisAdicao", atributosDisponiveisAdicao);
        result.include("listasPrioridade", listasPrioridade);
        result.include("prioridadesEnum", SrPrioridade.values());
    }

    public List<SrAtributo> atributosDisponiveisAdicaoConsulta(SrSolicitacaoFiltro filtro) throws Exception {
        List<SrAtributo> listaAtributosAdicao = new ArrayList<SrAtributo>();
        Map<Long, String> atributoMap = filtro.getAtributoSolicitacaoMap();

        for (SrAtributo srAtributo : SrAtributo.listarParaSolicitacao(Boolean.FALSE)) {
            if (!atributoMap.containsKey(srAtributo.getIdAtributo())) {
                listaAtributosAdicao.add(srAtributo);
            }
        }
        return listaAtributosAdicao;
    }
    
    @Path("/editar")
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
        //solicitacao.atualizarAcordos();

        List<CpComplexo> locais = ContextoPersistencia.em().createQuery("from CpComplexo").getResultList();

        Map<SrAcao, List<SrTarefa>> acoesEAtendentes = solicitacao.getAcoesEAtendentes();

        DpPessoaSelecao pessoaSel = new DpPessoaSelecao();
        pessoaSel.setId(getCadastrante().getId());
        pessoaSel.buscar();
		result.include("solicitacao.solicitante", pessoaSel);

        result.include("solicitacao",solicitacao);
        result.include("locais",locais);
        result.include("acoesEAtendentes",acoesEAtendentes);
        result.include("formaAcompanhamentoList", SrFormaAcompanhamento.values());
        result.include("gravidadeList", SrGravidade.values());
        result.include("tipoMotivoEscalonamentoList", SrTipoMotivoEscalonamento.values());
        result.include("urgenciaList",SrUrgencia.values());
        result.include("tendenciaList",SrTendencia.values());
        result.include("prioridadeList",SrPrioridade.values());
        result.include("locaisDisponiveis",solicitacao.getLocaisDisponiveis());
        result.include("meiosComunicadaoList",SrMeioComunicacao.values());
        result.include("itemConfiguracao",solicitacao.getItemConfiguracao());
        result.include("podeUtilizarServicoSigaGC",false);

    }
    
    @Path("/retirarDeLista")
    public void retirarDeLista(Long idSolicitacao, Long idLista) throws Exception {
        SrSolicitacao solicitacao = SrSolicitacao.AR.findById(idSolicitacao);
        SrLista lista = SrLista.AR.findById(idLista);
        solicitacao.retirarDeLista(lista, getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
        result.include("lista", lista);
        result.include("solicitacao", solicitacao);
    }
    
    private SrSolicitacao criarSolicitacaoComSolicitante() {
    	SrSolicitacao solicitacao = new SrSolicitacao();
    	solicitacao.setCadastrante(getCadastrante());
        solicitacao.setSolicitante(getTitular());
        
        return solicitacao;
    }
    
    @Path("/incluirEmListaGravar")
    public void incluirEmListaGravar(Long idSolicitacao, Long idLista, SrPrioridade prioridade, Boolean naoReposicionarAutomatico) throws Exception {
        if (idLista == null) {
            throw new Exception("Selecione a lista para inclusão da solicitação");
        }
        SrSolicitacao solicitacao = SrSolicitacao.AR.findById(idSolicitacao);
        SrLista lista = SrLista.AR.findById(idLista);
        solicitacao.incluirEmLista(lista, getCadastrante(), getLotaTitular(), prioridade, naoReposicionarAutomatico);     
        exibir(idSolicitacao, todoOContexto(), ocultas());
    }
    
    @Path("/responderPesquisaGravar")
    public void responderPesquisaGravar(Long id, Map<Long, String> respostaMap) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.responderPesquisa(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), respostaMap);
        exibir(id, todoOContexto(), ocultas());
    }
    
    @Path("/baixar")    
    public Download baixar(Long idArquivo) throws Exception {
         SrArquivo arq = SrArquivo.AR.findById(idArquivo);
         final InputStream inputStream = new ByteArrayInputStream(arq.getBlob());
         return new InputStreamDownload(inputStream, "application/pdf", arq.getNomeArquivo());
    }
    
    @Path("/escalonarGravar")
    public void escalonarGravar(Long id, Long itemConfiguracao, SrAcao acao, Long idAtendente, Long idAtendenteNaoDesignado, Long idDesignacao, SrTipoMotivoEscalonamento motivo, String descricao,
            Boolean criaFilha, Boolean fechadoAuto) throws Exception {
        if (itemConfiguracao == null || acao == null || acao.getIdAcao() == null || acao.getIdAcao().equals(0L))
            throw new Exception("Operacao nao permitida. Necessario informar um item de configuracao " + "e uma acao.");
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
        result.include("solicitacao", solicitacao);
        result.include("acoesEAtendentes", acoesEAtendentes);
    }
}
