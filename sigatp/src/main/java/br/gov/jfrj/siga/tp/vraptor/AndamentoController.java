package br.gov.jfrj.siga.tp.vraptor;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdmin;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminFrota;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissao;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAprovador;
import br.gov.jfrj.siga.tp.exceptions.AndamentoControllerException;
import br.gov.jfrj.siga.tp.exceptions.ApplicationControllerException;
import br.gov.jfrj.siga.tp.model.Andamento;
import br.gov.jfrj.siga.tp.model.EstadoRequisicao;
import br.gov.jfrj.siga.tp.model.RequisicaoTransporte;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import javax.transaction.Transactional;

@Path("/app/andamento/")
@Controller
public class AndamentoController extends TpController {

    private static final String ANDAMENTO = "andamento";

	/**
	 * @deprecated CDI eyes only
	 */
	public AndamentoController() {
		super();
	}
	
	@Inject
    public AndamentoController(HttpServletRequest request, Result result,  Validator validator, SigaObjects so,  EntityManager em) {
        super(request, result, TpDao.getInstance(), validator, so, em);
    }

    @Path("listarPorRequisicao/{idRequisicao}/{popUp}")
    public void listarPorRequisicao(Long idRequisicao, boolean popUp) throws AndamentoControllerException {
        RequisicaoTransporte requisicaoTransporte = RequisicaoTransporte.AR.findById(idRequisicao);
        List<Andamento> andamentos = Andamento.AR.find("requisicaoTransporte = :requisicaoTransporte order by id desc", Collections.singletonMap("requisicaoTransporte",requisicaoTransporte)).fetch();
        MenuMontador.instance(result).recuperarMenuRequisicoes(idRequisicao, popUp, popUp);
        result.include("andamentos", andamentos);
        result.include("requisicaoTransporte", requisicaoTransporte);

    }

    @RoleAdmin
    @RoleAdminFrota
    @RoleAdminMissao
    @RoleAprovador
    @Transactional
    @Path("salvar")
    public void salvar(Andamento andamento) throws AndamentoControllerException {
        try {
            if (andamento.getRequisicaoTransporte().getUltimoEstado() == EstadoRequisicao.CANCELADA) {
                validator.add(new I18nMessage("estadoRequisicao", "andamentos.estadoRequisicao.andamento.validation"));
                redirecionarSeErroAoSalvar(andamento);
            }

            if (andamento.getEstadoRequisicao() == EstadoRequisicao.CANCELADA || andamento.getEstadoRequisicao() == EstadoRequisicao.REJEITADA) {
                error(null == andamento.getDescricao() || "".equals(andamento.getDescricao()), ANDAMENTO, "views.erro.campoObrigatorio");
                redirecionarSeErroAoSalvar(andamento);
            }

            if (andamento.getEstadoRequisicao() == EstadoRequisicao.CANCELADA) {
                if (andamento.getRequisicaoTransporte().cancelar(getCadastrante(), "CANCELADA"))
                    result.redirectTo(ApplicationController.class).index();
                else {
                    validator.add(new I18nMessage("estadoRequisicao", "andamentos.estadoRequisicao.andamento.validation"));
                    redirecionarSeErroAoSalvar(andamento);
                }
            } else {
                DpPessoa dpPessoa = getCadastrante();
                andamento.setResponsavel(dpPessoa);
                andamento.setDataAndamento(Calendar.getInstance());
                validator.validate(andamento);
                redirecionarSeErroAoSalvar(andamento);
                andamento.save();
                result.redirectTo(RequisicaoController.class).listarPAprovar();
            }
        } catch (ApplicationControllerException e) {
            throw new AndamentoControllerException(e);
        }
    }

    private void redirecionarSeErroAoSalvar(Andamento andamento) throws AndamentoControllerException {
        if (validator.hasErrors()) {
            MenuMontador.instance(result).recuperarMenuRequisicoes(andamento.getRequisicaoTransporte().getId(), false, false);
            result.include(ANDAMENTO, andamento);
            switch (andamento.getEstadoRequisicao()) {
            case AUTORIZADA:
                validator.onErrorUse(Results.logic()).forwardTo(AndamentoController.class).autorizar(andamento.getRequisicaoTransporte().getId());
                break;
            case CANCELADA:
                validator.onErrorUse(Results.logic()).forwardTo(AndamentoController.class).cancelar(andamento.getRequisicaoTransporte().getId());
                break;
            case REJEITADA:
                validator.onErrorUse(Results.logic()).forwardTo(AndamentoController.class).rejeitar(andamento.getRequisicaoTransporte().getId());
                break;
            default:
                break;
            }
        }
    }

    @RoleAdmin
    @RoleAdminMissao
    @RoleAprovador
    @Path("autorizar/{id}")
    public void autorizar(Long id) throws AndamentoControllerException {
        result.include("acao", "Autorizar");
        result.forwardTo(this).atualizarEstado(id);
    }

    @RoleAdmin
    @RoleAdminMissao
    @RoleAprovador
    @Path("cancelar/{id}")
    public void cancelar(Long id) throws AndamentoControllerException {
        result.include("acao", "Cancelar");
        result.forwardTo(this).atualizarEstado(id);
    }

    
    @RoleAdmin
    @RoleAdminMissao
    @RoleAprovador
    @Path("rejeitar/{id}")
    public void rejeitar(Long id) throws AndamentoControllerException {
        result.include("acao", "Rejeitar");
        result.forwardTo(this).atualizarEstado(id);
    }

    public void atualizarEstado(Long id) throws AndamentoControllerException {

        Andamento andamento = new Andamento();
        andamento.setRequisicaoTransporte(RequisicaoTransporte.AR.findById(id));
        String acao = (String) result.included().get("acao");
        String acaoExecutada = (acao.substring(0, acao.length() - 1) + "DA").toUpperCase();
        andamento.setEstadoRequisicao(EstadoRequisicao.valueOf(acaoExecutada));
        MenuMontador.instance(result).recuperarMenuRequisicoes(id, false, true);

        result.include(ANDAMENTO, andamento);

        if ((isAcaoAutorizar(acao) || isAcaoRejeitar(acao)) && isRequisicaoEmAndamento(andamento))
            throw new AndamentoControllerException(MessagesBundle.getMessage("andamentos.autorizarOuCancelar.exception", andamento.getRequisicaoTransporte().buscarSequence()));
    }

    private boolean isRequisicaoEmAndamento(Andamento andamento) {
        return andamento.getRequisicaoTransporte().getUltimoAndamento().getEstadoRequisicao() != EstadoRequisicao.AUTORIZADA
                && andamento.getRequisicaoTransporte().getUltimoAndamento().getEstadoRequisicao() != EstadoRequisicao.REJEITADA
                && andamento.getRequisicaoTransporte().getUltimoAndamento().getEstadoRequisicao() != EstadoRequisicao.ABERTA;
    }

    private boolean isAcaoRejeitar(String acao) {
        return "Rejeitar".equals(acao);
    }

    private boolean isAcaoAutorizar(String acao) {
        return "Autorizar".equals(acao);
    }
}
