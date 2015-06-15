package br.gov.jfrj.siga.sr.vraptor;

import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.ADM_ADMINISTRAR;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.annotation.AssertAcesso;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrFatorMultiplicacao;
import br.gov.jfrj.siga.sr.model.SrGestorItem;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrPesquisa;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.model.vo.SelecionavelVO;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.uteis.PessoaLotaFuncCargoSelecaoHelper;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/itemConfiguracao")
public class ItemConfiguracaoController extends SrController {

    private static final String MOSTRAR_DESATIVADOS = "mostrarDesativados";
    private static final String ITENS = "itens";
    private static final String ORGAOS = "orgaos";
    private static final String LOCAIS = "locais";
    private static final String UNIDADES_MEDIDA = "unidadesMedida";
    private static final String PESQUISA_SATISFACAO = "pesquisaSatisfacao";

    public ItemConfiguracaoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em, SrValidator srValidator) {
        super(request, result, dao, so, em, srValidator);
    }

    @AssertAcesso(ADM_ADMINISTRAR)
    @SuppressWarnings("unchecked")
    @Path("/listar")
    public void listar(boolean mostrarDesativados) throws Exception {
        List<SrItemConfiguracao> itens = SrItemConfiguracao.listar(mostrarDesativados);
        List<CpOrgaoUsuario> orgaos = CpOrgaoUsuario.AR.em().createQuery("from CpOrgaoUsuario").getResultList();
        List<CpComplexo> locais = CpComplexo.AR.all().fetch();
        List<CpUnidadeMedida> unidadesMedida = CpDao.getInstance().listarUnidadesMedida();
        List<SrPesquisa> pesquisaSatisfacao = SrPesquisa.AR.find("hisDtFim is null").fetch();
        List<SrConfiguracao> designacoes = new ArrayList<SrConfiguracao>();

        result.include(ITENS, itens);
        result.include(ORGAOS, orgaos);
        result.include(LOCAIS, locais);
        result.include(UNIDADES_MEDIDA, unidadesMedida);
        result.include(PESQUISA_SATISFACAO, pesquisaSatisfacao);
        result.include(MOSTRAR_DESATIVADOS, mostrarDesativados);

        // Includes para os componentes de pessoaLotaSelecao de Gestor e Fator de Multiplicação
        result.include("gestorPessoaSel", new DpPessoaSelecao());
        result.include("gestorLotacaoSel", new DpLotacaoSelecao());
        result.include("fatorPessoaSel", new DpPessoaSelecao());
        result.include("fatorLotacaoSel", new DpLotacaoSelecao());

        // includes para o componente de Designação
        result.include("modoExibicao", "item");
        result.include("designacoes", designacoes);
        result.include("atendenteSel", new DpLotacaoSelecao());
        result.include("itemConfiguracao", new SelecionavelVO(null, null));
        result.include("acao", new SelecionavelVO(null, null));
        
        PessoaLotaFuncCargoSelecaoHelper.adicionarCamposSelecao(result);
    }

    @AssertAcesso(ADM_ADMINISTRAR)
    @Path("/desativar")
    public void desativar(Long id, boolean mostrarDesativados) throws Exception {
        SrItemConfiguracao item = SrItemConfiguracao.AR.findById(id);
        item.finalizar();

        result.use(Results.http()).body(item.getSrItemConfiguracaoJson());
    }

    @AssertAcesso(ADM_ADMINISTRAR)
    @Path("/reativar")
    public void reativar(Long id, boolean mostrarDesativados) throws Exception {
        SrItemConfiguracao item = SrItemConfiguracao.AR.findById(id);
        item.salvar();

        result.use(Results.http()).body(item.getSrItemConfiguracaoJson());
    }

    @AssertAcesso(ADM_ADMINISTRAR)
    @Path("/gravar")
    public void gravar(SrItemConfiguracao itemConfiguracao, List<SrGestorItem> gestorSet, List<SrFatorMultiplicacao> fatorMultiplicacaoSet) throws Exception {
        // WO para tratar o erro de conversão de listas do Vraptor
        itemConfiguracao.setGestorSet(gestorSet);
        itemConfiguracao.setFatorMultiplicacaoSet(fatorMultiplicacaoSet);

        validarFormEditarItem(itemConfiguracao);
        itemConfiguracao.salvar();

        // Atualiza os conhecimentos relacionados
        // Edson: deveria ser feito por webservice. Nao estah sendo coberta
        // a atualizacao da classificacao quando ocorre mudanca de posicao na
        // hierarquia, pois isso eh mais complexo de acertar.
        // try {
        // HashMap<String, String> atributos = new HashMap<String, String>();
        // for (Http.Header h : request.headers.values())
        // if (!h.name.equals("content-type"))
        // atributos.put(h.name, h.value());
        //
        // SrItemConfiguracao anterior = null;
        // List<SrItemConfiguracao> itens = itemConfiguracao.getHistoricoItemConfiguracao();
        // if(itens != null)
        // anterior = itens.get(0);
        // if (anterior != null
        // && !itemConfiguracao.tituloItemConfiguracao
        // .equals(anterior.tituloItemConfiguracao))
        // ConexaoHTTP.get("http://"
        // + Play.configuration.getProperty("servidor.principal")
        // + ":8080/sigagc/app/updateTag?before="
        // + anterior.getTituloSlugify() + "&after="
        // + itemConfiguracao.getTituloSlugify(), atributos);
        // } catch (Exception e) {
        // Logger.error("Item " + itemConfiguracao.idItemConfiguracao
        // + " salvo, mas nao foi possivel atualizar conhecimento");
        // e.printStackTrace();
        // }
        //
        result.use(Results.http()).body(itemConfiguracao.getSrItemConfiguracaoJson());
    }

    @Get
    @Path("/{id}/designacoes")
    public void buscarDesignacoes(Long id) throws Exception {
        List<SrConfiguracao> designacoes;

        if (id != null) {
            SrItemConfiguracao itemConfiguracao = SrItemConfiguracao.AR.findById(id);
            designacoes = new ArrayList<SrConfiguracao>(itemConfiguracao.getDesignacoesAtivas());
            designacoes.addAll(itemConfiguracao.getDesignacoesPai());
        } else
            designacoes = new ArrayList<SrConfiguracao>();

        result.use(Results.http()).body(SrConfiguracao.convertToJSon(designacoes));
    }

    private void validarFormEditarItem(SrItemConfiguracao itemConfiguracao) throws Exception {
        if (itemConfiguracao.getSiglaItemConfiguracao().equals("")) {
            srValidator.addError("siglaAcao", "Código não informado");
        }
        if (srValidator.hasErrors()) {
            enviarErroValidacao();
        }

    }

    @Path("/buscar")
    public void buscar(String sigla, String nome, String siglaItemConfiguracao, String tituloItemConfiguracao, SrSolicitacao sol, String propriedade) {
        List<SrItemConfiguracao> items = null;
        SrItemConfiguracao filtro = new SrItemConfiguracao();
        try {
            filtro.setSiglaItemConfiguracao(siglaItemConfiguracao);
            filtro.setTituloItemConfiguracao(tituloItemConfiguracao);

            if (sigla != null && !sigla.trim().equals(""))
                filtro.setSigla(sigla);

            if (buscaPorItens(sol)) {
                items = filtro.buscar(sol.getItensDisponiveis());
            } else {
                items = filtro.buscar();
            }
        } catch (Exception e) {
            items = new ArrayList<SrItemConfiguracao>();
        }
        if (sol.getLocal() == null) {
            sol.setLocal(new CpComplexo());
        }
        if (sol.getSolicitante() == null) {
            sol.setSolicitante(new DpPessoa());
        }
        result.include("items", items);
        result.include("sol", sol);
        result.include("filtro", filtro);
        result.include("nome", nome);
        result.include("propriedade", propriedade);
    }

    private boolean buscaPorItens(SrSolicitacao sol) {
        return sol != null && ((sol.getSolicitante() != null && sol.getSolicitante().getId() != null) || (sol.getLocal() != null && sol.getLocal().getIdComplexo() != null));
    }

    @Path("/selecionar")
    public void selecionar(String sigla, SrSolicitacao sol) throws Exception {
        SrItemConfiguracao sel = new SrItemConfiguracao().selecionar(sigla, sol.getItensDisponiveis());
        result.include("selecionar", sel);
        result.use(Results.status()).ok();
    }

}
