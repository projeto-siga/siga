package br.gov.jfrj.siga.tp.vraptor;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.tp.auth.AutorizacaoGI;
import br.gov.jfrj.siga.tp.exceptions.RestControllerException;
import br.gov.jfrj.siga.tp.model.Andamento;
import br.gov.jfrj.siga.tp.model.EstadoRequisicao;
import br.gov.jfrj.siga.tp.model.FinalidadeRequisicao;
import br.gov.jfrj.siga.tp.model.RequisicaoTransporte;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.rest.RequisicaoTransporteRest;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/requisicaoRest")
public class RequisicaoRestController extends TpController {

    private static final String REQUISICOES_REST_REQ_NULL_EXCEPTION = "requisicoesRest.reqNull.exception";
    private static final String REQUISICAO_REST = "requisicaoRest";
    @Inject
    private AutorizacaoGI autorizacaoGI;

	/**
	 * @deprecated CDI eyes only
	 */
	public RequisicaoRestController() {
		super();
	}
	
	@Inject
    public RequisicaoRestController(HttpServletRequest request, Result result,   Validator validator, SigaObjects so,  EntityManager em) {
        super(request, result, TpDao.getInstance(), validator, so, em);
    }

    @Path("/ver/{id}")
    public void ver(Long id) throws RestControllerException {
        try {
            RequisicaoTransporteRest req = RequisicaoTransporteRest.recuperar(id);

            if (req.getRequisicaoTransporte() == null)
                throw new NullPointerException(MessagesBundle.getMessage(REQUISICOES_REST_REQ_NULL_EXCEPTION));
            
            result.use(Results.json()).from(req.getRequisicaoTransporte()).serialize();
        } catch (Exception e) {
            throw new RestControllerException(e);
        }
    }

    @Path("/buscar/{codigoDocumento*}")
    public void buscar(String codigoDocumento) throws RestControllerException {
        try {
            RequisicaoTransporteRest req = RequisicaoTransporteRest.recuperarPelaSequence(codigoDocumento);

            if (req.getRequisicaoTransporte() == null)
                throw new NullPointerException(MessagesBundle.getMessage(REQUISICOES_REST_REQ_NULL_EXCEPTION));
            
            result.use(Results.json()).from(req.getRequisicaoTransporte()).serialize();
        } catch (Exception e) {
            throw new RestControllerException(e);
        }
        
    }

    @Path("/estaAutorizada/{codigoDocumento*}")
    public void estaAutorizada(String codigoDocumento) throws RestControllerException {
        result.use(Results.http()).body(estaNesseEstado(codigoDocumento, EstadoRequisicao.AUTORIZADA).toString());
    }

    @Path("/estaRejeitada/{codigoDocumento*}")
    public void estaRejeitada(String codigoDocumento) throws RestControllerException {
        result.use(Results.http()).body(estaNesseEstado(codigoDocumento, EstadoRequisicao.REJEITADA).toString());
    }

    @Path("/estaAtendida/{codigoDocumento*}")
    public void estaAtendida(String codigoDocumento) throws RestControllerException {
        result.use(Results.http()).body(estaNesseEstado(codigoDocumento, EstadoRequisicao.ATENDIDA).toString());
    }

    private Boolean estaNesseEstado(String sequence, EstadoRequisicao estadoRequisicao) throws RestControllerException {
        try {
            RequisicaoTransporteRest req = RequisicaoTransporteRest.recuperarPelaSequence(sequence);

            if (req.getRequisicaoTransporte() == null)
                throw new NullPointerException(MessagesBundle.getMessage(REQUISICAO_REST, REQUISICOES_REST_REQ_NULL_EXCEPTION));

            return req.getUltimoAndamento().equals(estadoRequisicao.getDescricao());
        } catch (Exception e) {
            throw new RestControllerException(e);
        }
    }

    private void validarRequisicao(RequisicaoTransporte requisicaoTransporte) {
        if (requisicaoTransporte.getDataHoraRetornoPrevisto() != null && (requisicaoTransporte.getDataHoraSaidaPrevista() != null) && (!requisicaoTransporte.ordemDeDatasCorreta()))
            validator.add(new I18nMessage("dataHoraRetornoPrevisto", "requisicaoTransporte.dataHoraRetornoPrevisto.validation"));

        if ((requisicaoTransporte.getTiposDePassageiro() == null) || (requisicaoTransporte.getTiposDePassageiro().isEmpty()))
            validator.add(new I18nMessage("tiposDePassageiros", "requisicaoTransporte.tiposDePassageiros.validation"));

        if (requisicaoTransporte.getPassageiros() == null || requisicaoTransporte.getPassageiros().isEmpty())
            validator.add(new I18nMessage("passageiros", "requisicaoTransporte.passageirosNomeEContato.validation"));

        if (requisicaoTransporte.getTipoFinalidade().ehOutra() && requisicaoTransporte.getFinalidade().isEmpty())
            validator.add(new I18nMessage("finalidade", "requisicaoTransporte.finalidade.validation"));
    }

    @Path("/incluir/{body*}")
    public void incluir(String body) throws RestControllerException {
        try {
            RequisicaoTransporte req = new RequisicaoTransporte();
            Map<String, String> map = transformarDadosRecebidos(body);

            RequisicaoTransporteRest.converterParaRequisicao(req, map);
            validarRequisicao(req);

            if (validator.hasErrors()) {
                // exibir o erro na saida
                List<Message> listaErros = validator.getErrors();
                Error[] erros = listaErros.toArray(new Error[listaErros.size()]);

                StringBuilder mensagemErro = new StringBuilder("Erro! ").append(new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(erros));
                result.use(Results.http()).body(mensagemErro.toString());
            }

            DpPessoa dpPessoa = recuperaPessoa(req.getIdSolicitante());

            req.setSolicitante(dpPessoa);
            req.setCpOrgaoUsuario(dpPessoa.getOrgaoUsuario());
            req.setCpComplexo(autorizacaoGI.recuperarComplexoPadrao(dpPessoa));

            checarSolicitante(req, dpPessoa, true);

            req.setDataHora(Calendar.getInstance());
            req.setSequence(req.getCpOrgaoUsuario());

            req.setSolicitante(dpPessoa);

            hackSimularUsuarioLogadoParaRevInfo(dpPessoa);

            req.save();

            // gravar andamento
            req.refresh();
            Andamento andamento = new Andamento();
            andamento.setDescricao("NOVA REQUISICAO");
            andamento.setDataAndamento(Calendar.getInstance());
            andamento.setEstadoRequisicao(EstadoRequisicao.ABERTA);
            andamento.setRequisicaoTransporte(req);
            andamento.setResponsavel(dpPessoa);
            andamento.save();

            ver(req.getId());
        } catch (Exception e) {
            throw new RestControllerException(e);
        }
    }

    private Map<String, String> transformarDadosRecebidos(String body) throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        ObjectMapper mapper = new ObjectMapper();
        map = mapper.readValue(body, new TypeReference<HashMap<String, String>>() {        });
        return map;
    }

    private void hackSimularUsuarioLogadoParaRevInfo(DpPessoa usuario) {
        result.include("cadastrante", usuario);
    }

    @Path("/alterar/{body*}")
    public void alterar(String body) throws RestControllerException {
        try {
            Map<String, String> map = transformarDadosRecebidos(body);

            long idABuscar = Long.parseLong(map.get("id"));
            RequisicaoTransporteRest requisicaoRestAAlterar = RequisicaoTransporteRest.recuperarEConverter(idABuscar, map);
            RequisicaoTransporte requisicaoAAlterar = RequisicaoTransporteRest.recuperarRequisicao(requisicaoRestAAlterar);

            validarRequisicao(requisicaoAAlterar);

            if (validator.hasErrors()) {
                List<Message> listaErros = validator.getErrors();
                Error[] erros = listaErros.toArray(new Error[listaErros.size()]);

                StringBuilder mensagemErro = new StringBuilder("Erro! ").append(new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(erros));
                result.use(Results.http()).body(mensagemErro.toString());
            }

            DpPessoa dpPessoa = recuperaPessoa(Long.parseLong(map.get("idInicialDpPessoaSolicitante")));

            checarSolicitante(requisicaoAAlterar, dpPessoa, true);

            hackSimularUsuarioLogadoParaRevInfo(dpPessoa);

            requisicaoAAlterar.save();

            ver(requisicaoAAlterar.getId());
        } catch (Exception e) {
            throw new RestControllerException(e);
        }
    }

    @Path("/excluir")
    public void excluir() {
        result.use(Results.http()).body("Opera&ccedil;&atilde;o n&atilde;o suportada");
    }

    protected void carregarFinalidades() {
        result.include("finalidedes", FinalidadeRequisicao.listarTodos());
    }

    private void checarSolicitante(RequisicaoTransporte req, DpPessoa pessoaAcesso, Boolean escrita) throws RestControllerException {
        if (!pessoaAcesso.getIdInicial().equals(req.getIdSolicitante()) && escrita)
            //throw new RestControllerException(new I18nMessage(REQUISICAO_REST, "requisicoes.checarSolicitante.exception").getMessage());
        	throw new RestControllerException(MessagesBundle.getMessage("requisicoes.checarSolicitante.exception"));
    }

    private DpPessoa recuperaPessoa(Long idSolicitante) throws RestControllerException {
        try {
            DpPessoa dpPessoa = DpPessoa.AR.findById(idSolicitante);
    		Map<String, Object> parametros = new HashMap<String,Object>();
    		parametros.put("idPessoaIni",dpPessoa.getIdInicial());
            return DpPessoa.AR.find("idPessoaIni = :idPessoaIni and dataFimPessoa = null", parametros).first();
        } catch (Exception e) {
            throw new RestControllerException(e);
        }
    }
    
}
