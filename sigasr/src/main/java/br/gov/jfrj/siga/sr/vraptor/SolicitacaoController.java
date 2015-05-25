package br.gov.jfrj.siga.sr.vraptor;

import static br.com.caelum.vraptor.view.Results.http;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.caelum.vraptor.view.HttpResult;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrAtributoSolicitacao;
import br.gov.jfrj.siga.sr.model.SrFormaAcompanhamento;
import br.gov.jfrj.siga.sr.model.SrGravidade;
import br.gov.jfrj.siga.sr.model.SrLista;
import br.gov.jfrj.siga.sr.model.SrMeioComunicacao;
import br.gov.jfrj.siga.sr.model.SrMovimentacao;
import br.gov.jfrj.siga.sr.model.SrPrioridade;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.model.SrTendencia;
import br.gov.jfrj.siga.sr.model.SrSolicitacao.SrTarefa;
import br.gov.jfrj.siga.sr.model.SrUrgencia;
import br.gov.jfrj.siga.sr.model.vo.SrSolicitacaoListaVO;
import br.gov.jfrj.siga.sr.notifiers.Correio;
import br.gov.jfrj.siga.sr.util.SrSolicitacaoFiltro;
import br.gov.jfrj.siga.sr.validator.SrError;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/solicitacao")
public class SolicitacaoController extends SrController {
    private Correio correio;
    private Validator validator;

    public SolicitacaoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em, SrValidator srValidator, Correio correio, Validator validator) {
        super(request, result, dao, so, em, srValidator);
        this.correio = correio;
        this.validator = validator;
    }

    @Path("/teste")
    public void teste(boolean banco) {
        SrSolicitacao value = new SrSolicitacao();
        if (banco) {
            value = (SrSolicitacao) SrSolicitacao.AR.all().fetch().get(0);
        }

        result.include("meiosComunicadaoList", SrMeioComunicacao.values());
        result.include("locaisDisponiveis", value.getLocaisDisponiveis());
        result.include("solicitacao", value);
    }

    @Path("/testeErro")
    public void testeErro() {
        validator.add(new ValidationMessage("Chamas", "nome.teste"));
        validator.add(new ValidationMessage("Eternas", "nome.teste"));
        validator.add(new ValidationMessage("do", "nome.teste"));
        validator.add(new ValidationMessage("Aconchego", "nome.teste"));

        validator.onErrorUsePageOf(this).teste(false);

    }
    
    @Path("/gravar")
    public void gravar(SrSolicitacao solicitacao) throws Exception {

        if (!solicitacao.isRascunho())
            validarFormEditar(solicitacao);

        solicitacao.salvar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
        Long id = solicitacao.getIdSolicitacao();
        exibir(id, todoOContexto(), ocultas());
    }

    @SuppressWarnings("static-access")
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

    public void exibir(Long id, Boolean todoOContexto, Boolean ocultas) throws Exception {
        SrSolicitacao solicitacao = SrSolicitacao.AR.findById(id);
        if (solicitacao == null)
            throw new Exception("Solicitação não encontrada");
        else
            solicitacao = solicitacao.getSolicitacaoAtual();

        if (solicitacao == null)
            throw new Exception("Esta solicitação foi excluída");

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
    
    public void exibirLocalRamalEMeioContato(SrSolicitacao solicitacao) throws Exception {
        // render(solicitacao.deduzirLocalRamalEMeioContato());
    }

    public void exibirItemConfiguracao(SrSolicitacao solicitacao) throws Exception {
        if (solicitacao.getSolicitante() == null)
            // render(solicitacao);

            if (!solicitacao.getItensDisponiveis().contains(solicitacao.getItemConfiguracao()))
                solicitacao.setItemConfiguracao(null);

        DpPessoa titular = solicitacao.getTitular();
        DpLotacao lotaTitular = solicitacao.getLotaTitular();
        Map<SrAcao, List<SrTarefa>> acoesEAtendentes = solicitacao.getAcoesEAtendentes();
        // render(solicitacao, titular, lotaTitular, acoesEAtendentes);
    }
    public void exibirConhecimentosRelacionados(SrSolicitacao solicitacao) throws Exception {
        // render(solicitacao);
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

    // DB1: foi necessário receber e passar o parametro "nome"(igual ao buscarItem())
    // para chamar a function javascript correta,
    // e o parametro "popup" porque este metodo é usado também na lista,
    // e não foi possível deixar default no template(igual ao buscarItem.html)
    @SuppressWarnings("unchecked")
    public void buscarSolicitacao(SrSolicitacaoFiltro filtro, String nome, boolean popup) throws Exception {
        SrSolicitacaoListaVO solicitacaoListaVO;

        try {
            if (filtro.pesquisar) {
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
        // render(solicitacaoListaVO, tipos, marcadores, filtro, nome, popup, atributosDisponiveisAdicao, listasPrioridade);
    }

    public List<SrAtributo> atributosDisponiveisAdicaoConsulta(SrSolicitacaoFiltro filtro) throws Exception {
        List<SrAtributo> listaAtributosAdicao = new ArrayList<SrAtributo>();
        HashMap<Long, String> atributoMap = filtro.getAtributoSolicitacaoMap();

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

        result.include("solicitacao",solicitacao);
        result.include("locais",locais);
        result.include("acoesEAtendentes",acoesEAtendentes);
        result.include("formaAcompanhamentoList", SrFormaAcompanhamento.values());
        result.include("gravidadeList", SrGravidade.values());
        result.include("urgenciaList",SrUrgencia.values());
        result.include("tendenciaList",SrTendencia.values());
        result.include("prioridadeList",SrPrioridade.values());
        
    }
}
