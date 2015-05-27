package br.gov.jfrj.siga.sr.vraptor;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.cp.model.CpPerfilSelecao;
import br.gov.jfrj.siga.cp.model.DpCargoSelecao;
import br.gov.jfrj.siga.cp.model.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.enumeration.SrUnidadeMedida;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrAcordo;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrAtributoAcordo;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrOperador;
import br.gov.jfrj.siga.sr.model.SrPrioridade;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/acordo")
public class AcordoController extends SrController {

    private static final String ACORDOS = "acordos";

    public AcordoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em, SrValidator srValidator) {
        super(request, result, dao, so, em, srValidator);
        result.on(AplicacaoException.class).forwardTo(this).appexception();
        result.on(Exception.class).forwardTo(this).exception();
    }

    // @AssertAcesso(ADM_ADMINISTRAR)
    @SuppressWarnings("unchecked")
    @Path("/listar/{mostrarDesativados}")
    public void listar(boolean mostrarDesativados) throws Exception {

        List<SrAtributo> parametros = SrAtributo.listarParaAcordo(false);
        List<CpUnidadeMedida> unidadesMedida = CpDao.getInstance().listarUnidadesMedida();
        List<CpOrgaoUsuario> orgaos = CpOrgaoUsuario.AR.findAll();
        List<CpComplexo> locais = CpComplexo.AR.all().fetch();
        List<SrAcordo> acordos = SrAcordo.listar(mostrarDesativados);

        result.include(ACORDOS, acordos);
        result.include("nome", "");
        result.include("popup", Boolean.FALSE);
        result.include("mostrarDesativados", mostrarDesativados);
        result.include("parametros", parametros);
        result.include("unidadesMedida", unidadesMedida);
        result.include("orgaos", orgaos);
        result.include("locais", locais);
        result.include("operadores", SrOperador.values());
        result.include("prioridades", SrPrioridade.values());

        result.include("dpPessoaSel", new DpPessoaSelecao());
        result.include("atendenteSel", new DpLotacaoSelecao());
        result.include("lotacaoSel", new DpLotacaoSelecao());
        result.include("funcaoConfiancaSel", new DpFuncaoConfiancaSelecao());
        result.include("cargoSel", new DpCargoSelecao());
        result.include("cpGrupoSel", new CpPerfilSelecao());
    }

    // @AssertAcesso(ADM_ADMINISTRAR)
    @Path("/gravar")
    public void gravarAcordo(SrAcordo acordo, List<SrAtributoAcordo> atributoAcordoSet, List<Integer> unidadeMedida) throws Exception {
        if (acordo.getAtributoAcordoSet() != null)
            acordo.getAtributoAcordoSet().clear();
        if (isNotEmptyUnidadeMedida(unidadeMedida))
            acordo.getAtributoAcordoSet().addAll(buscaAtributosAcordo(atributoAcordoSet, unidadeMedida));

        acordo.salvar();
        result.use(Results.http()).body(acordo.toJson());
    }

    private HashMap<Integer, CpUnidadeMedida> getUnidadesMedida(List<Integer> unidadeMedida) {
        HashMap<Integer, CpUnidadeMedida> unidadesMedidaEncontradas = new HashMap<>();

        for (int i = 0; i < unidadeMedida.size(); i++) {
            CpUnidadeMedida cpUnidadeMedida = new CpUnidadeMedida();
            cpUnidadeMedida.setIdUnidadeMedida(Long.valueOf(unidadeMedida.get(i)));
            cpUnidadeMedida.setDescricao(SrUnidadeMedida.values()[unidadeMedida.get(i) - 1].getNome());
            unidadesMedidaEncontradas.put(i, cpUnidadeMedida);
        }
        return unidadesMedidaEncontradas;
    }

    private List<SrAtributoAcordo> buscaAtributosAcordo(List<SrAtributoAcordo> atributoAcordo, List<Integer> unidadeMedida) {
        HashMap<Integer, CpUnidadeMedida> unidadesMedidaEncontradas = getUnidadesMedida(unidadeMedida);

        for (int i = 0; i < atributoAcordo.size(); i++) {
            CpUnidadeMedida cpUnidadeMedida = unidadesMedidaEncontradas.get(i);
            SrAtributoAcordo srAtributoAcordo = atributoAcordo.get(i);
            srAtributoAcordo.setUnidadeMedida(cpUnidadeMedida);
        }
        return atributoAcordo;
    }

    private boolean isNotEmptyUnidadeMedida(List<Integer> unidadeMedida) {
        return unidadeMedida != null && !unidadeMedida.isEmpty();
    }

    // @AssertAcesso(ADM_ADMINISTRAR)
    @Path("/desativar")
    public void desativar(Long id) throws Exception {
        SrAcordo acordo = SrAcordo.AR.findById(id);
        acordo.finalizar();

        result.use(Results.http()).body(acordo.toJson());
    }

    // @AssertAcesso(ADM_ADMINISTRAR)
    @Path("/reativar")
    public void reativar(Long id) throws Exception {
        SrAcordo acordo = SrAcordo.AR.findById(id);
        acordo.salvar();

        result.use(Results.http()).body(acordo.toJson());
    }

    @Path("/abrangencias")
    public void buscarAbrangenciasAcordo(Long id, boolean exibirInativos) throws Exception {
        SrAcordo acordo = new SrAcordo();

        if (id != null)
            acordo = SrAcordo.AR.findById(id);
        List<SrConfiguracao> abrangencias = SrConfiguracao.listarAbrangenciasAcordo(exibirInativos, acordo);

        result.use(Results.http()).body(SrConfiguracao.convertToJSon(abrangencias));
    }

    // @AssertAcesso(ADM_ADMINISTRAR)
    public void gravarAbrangencia(SrConfiguracao associacao, List<SrItemConfiguracao> itemConfiguracaoSet, List<SrAcao> acoesSet) throws Exception {
        associacao.setItemConfiguracaoSet(itemConfiguracaoSet);
        associacao.setAcoesSet(acoesSet);

        associacao.salvarComoAbrangenciaAcordo();

        result.use(Results.http()).body(associacao.toJson());
    }

    // @AssertAcesso(ADM_ADMINISTRAR)
    @Path("/desativarAbrangenciaEdicao")
    public void desativarAbrangenciaEdicao(Long idAcordo, Long idAssociacao) throws Exception {
        SrConfiguracao abrangencia = SrConfiguracao.AR.findById(idAssociacao);
        abrangencia.finalizar();
        result.use(Results.http()).body(abrangencia.toJson());
    }

    // @AssertAcesso(ADM_ADMINISTRAR)
    @SuppressWarnings("unchecked")
    @Path("/buscar")
    public void buscar(boolean mostrarDesativados, String nome, boolean popup, String propriedade) throws Exception {
        List<SrAtributo> parametros = SrAtributo.listarParaAcordo(mostrarDesativados);
        List<CpUnidadeMedida> unidadesMedida = CpDao.getInstance().listarUnidadesMedida();
        List<CpOrgaoUsuario> orgaos = ContextoPersistencia.em().createQuery("from CpOrgaoUsuario").getResultList();
        List<CpComplexo> locais = CpComplexo.AR.all().fetch();
        List<SrAcordo> acordos = SrAcordo.listar(mostrarDesativados);

        result.include(ACORDOS, acordos);
        result.include("operadores", SrOperador.values());
        result.include("prioridades", SrPrioridade.values());
        result.include("parametros", parametros);
        result.include("unidadesMedida", unidadesMedida);
        result.include("orgaos", orgaos);
        result.include("locais", locais);
        result.include("mostrarDesativados", mostrarDesativados);
        result.include("nome", nome);
        result.include("popup", popup);
        result.include("propriedade", propriedade);
    }
}
