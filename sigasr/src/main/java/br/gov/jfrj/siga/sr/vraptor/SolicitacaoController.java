package br.gov.jfrj.siga.sr.vraptor;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrLista;
import br.gov.jfrj.siga.sr.model.SrPrioridade;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.model.SrTipoPermissaoLista;
import br.gov.jfrj.siga.sr.model.SrSolicitacao.SrTarefa;
import br.gov.jfrj.siga.sr.model.vo.SrSolicitacaoListaVO;
import br.gov.jfrj.siga.sr.notifiers.Correio;
import br.gov.jfrj.siga.sr.util.SrSolicitacaoFiltro;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/solicitacao")
public class SolicitacaoController extends SrController {
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
        result.include("listas", listas);
        result.include("mostrarDesativados", mostrarDesativados);
        result.include("lotaTitular", getLotaTitular());
        result.include("cadastrante", getCadastrante());
        

        // render(listas, mostrarDesativados, orgaos, locais, tiposPermissao, tiposPermissaoJson);
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
            srValidator.addError("lista.nomeLista", "Nome da Lista nÃ£o informados");
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
    @Path("/exibirLista")
    public void exibirLista(Long id) throws Exception {
        SrLista lista = SrLista.AR.findById(id);
        List<CpOrgaoUsuario> orgaos = ContextoPersistencia.em().createQuery("from CpOrgaoUsuario").getResultList();
        List<CpComplexo> locais = CpComplexo.AR.all().fetch();
        List<SrTipoPermissaoLista> tiposPermissao = SrTipoPermissaoLista.AR.all().fetch();
        SrSolicitacaoFiltro filtro = new SrSolicitacaoFiltro();
        SrSolicitacaoListaVO solicitacaoListaVO;
        String tiposPermissaoJson = new Gson().toJson(tiposPermissao);
        filtro.idListaPrioridade = id;
        lista = lista.getListaAtual();
        String jsonPrioridades = SrPrioridade.getJSON().toString();

        if (!lista.podeConsultar(getLotaTitular(), getCadastrante())) {
            throw new Exception("Exibição não permitida");
        }

        try {
            solicitacaoListaVO = SrSolicitacaoListaVO.fromFiltro(filtro, true, "", false, getLotaTitular(), getCadastrante());
        } catch (Exception e) {
            e.printStackTrace();
            solicitacaoListaVO = new SrSolicitacaoListaVO();
        }

        // render(lista, orgaos, locais, tiposPermissao, solicitacaoListaVO, tiposPermissaoJson, jsonPrioridades);
    }
    
    

}
