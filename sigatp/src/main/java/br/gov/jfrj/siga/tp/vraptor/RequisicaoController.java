package br.gov.jfrj.siga.tp.vraptor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.tp.auth.AutorizacaoGI;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdmin;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissao;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissaoComplexo;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAprovador;
import br.gov.jfrj.siga.tp.model.Andamento;
import br.gov.jfrj.siga.tp.model.EstadoMissao;
import br.gov.jfrj.siga.tp.model.EstadoRequisicao;
import br.gov.jfrj.siga.tp.model.FinalidadeRequisicao;
import br.gov.jfrj.siga.tp.model.Missao;
import br.gov.jfrj.siga.tp.model.Parametro;
import br.gov.jfrj.siga.tp.model.RequisicaoTransporte;
import br.gov.jfrj.siga.tp.model.TipoDePassageiro;
import br.gov.jfrj.siga.tp.model.TipoRequisicao;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.util.SigaTpException;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import javax.transaction.Transactional;

@Controller
@Path("/app/requisicao")
public class RequisicaoController extends TpController {

    private static final String REQUISICOES_CHECAR_SOLICITANTE_EXCEPTION = "requisicoes.checarSolicitante.exception";
    private static final String TIPOS_REQUISICAO = "tiposRequisicao";
    private static final String CHECK_RETORNO = "checkRetorno";
    private static final String ESTADO_REQUISICAO = "estadoRequisicao";
    private static final String REQUISICAO_TRANSPORTE = "requisicaoTransporte";
    
    @Inject
    private AutorizacaoGI autorizacaoGI;

	/**
	 * @deprecated CDI eyes only
	 */
	public RequisicaoController() {
		super();
	}
	
	@Inject
    public RequisicaoController(HttpServletRequest request, Result result, Validator validator, SigaObjects so,  EntityManager em) {
        super(request, result, TpDao.getInstance(), validator, so, em);
    }

    @Path("/listar")
    public void listar() {
		EstadoRequisicao estReq = EstadoRequisicao.CANCELADA;
		carregarRequisicoesUltimosDiasInformadosPorEstados(null);
        result.include(ESTADO_REQUISICAO, EstadoRequisicao.PROGRAMADA);
        MenuMontador.instance(result).recuperarMenuListarRequisicoes(null);
        result.include("estadosRequisicao", EstadoRequisicao.values());        
        result.include("estReq", estReq);
    }

    @RoleAprovador
    @RoleAdmin
    @RoleAdminMissao
    @RoleAdminMissaoComplexo
    @Path("/listarPAprovar")
    public void listarPAprovar() {
    	EstadoRequisicao estReq = EstadoRequisicao.CANCELADA;    	
    	EstadoRequisicao[] estadosRequisicao = { EstadoRequisicao.ABERTA, EstadoRequisicao.AUTORIZADA, EstadoRequisicao.REJEITADA };
    	carregarRequisicoesUltimosDiasInformadosPorEstados(estadosRequisicao);
        result.include(ESTADO_REQUISICAO, EstadoRequisicao.ABERTA);
        MenuMontador.instance(result).recuperarMenuListarPAprovarRequisicoes(null);
        List<CpComplexo> complexos = TpDao.find(CpComplexo.class, "orgaoUsuario", getTitular().getOrgaoUsuario()).fetch();
        result.include("complexos", complexos);
        result.include("estadosRequisicao", estadosRequisicao);        
        result.include("estReq", estReq);
    }

    @RoleAdmin
    @RoleAdminMissao
    @Transactional
    @Path("/salvarNovoComplexo")
    public void salvarNovoComplexo(Long[] req, CpComplexo novoComplexo) {
        if (req == null) {
            throw new NullPointerException(MessagesBundle.getMessage("requisicoes.salvarNovoComplexo.exception"));
        }

        for (int cont = 0; cont < req.length; cont++) {
            RequisicaoTransporte requisicao = RequisicaoTransporte.AR.findById(req[cont]);
            requisicao.setCpComplexo(novoComplexo);
            requisicao.save();
        }

        result.redirectTo(this).listarPAprovar();
    }

    @Path("/listarFiltrado/{estadoRequisicao}")
    public void listarFiltrado(String estadoRequisicao) {
        result.forwardTo(this).listarFiltrado(estadoRequisicao, null);
    }

    @Path("/listarFiltrado/{estadoRequisicao}/{estadoRequisicaoP}")
    public void listarFiltrado(String estadoRequisicao, String estadoRequisicaoP) {
        EstadoRequisicao estadoReq = EstadoRequisicao.valueOf(estadoRequisicao);
        EstadoRequisicao estadoReqP = EstadoRequisicao.valueOf(null != estadoRequisicaoP ? estadoRequisicaoP : estadoRequisicao);
        EstadoRequisicao[] estadosRequisicao = { estadoReq, estadoReqP };
        carregarRequisicoesUltimosDiasInformadosPorEstados(estadosRequisicao);
        MenuMontador.instance(result).recuperarMenuListarRequisicoes(estadoReq, estadoReqP);
		EstadoRequisicao estReq = EstadoRequisicao.CANCELADA;
        result.include("estReq", estReq);
        result.include(ESTADO_REQUISICAO, estadoRequisicao);
        result.use(Results.page()).of(RequisicaoController.class).listar();
    }
    
    @Path("/listarAvancado")
    public void listarAvancado(Calendar dataInicio, Calendar dataFim, EstadoRequisicao estadoRequisicao) throws Exception {
		EstadoRequisicao estReq = EstadoRequisicao.CANCELADA;
		List<RequisicaoTransporte> requisicoesTransporte = RequisicaoTransporte.buscarRequisicoesAvancado(dataInicio, dataFim, getTitular().getOrgaoUsuario(), estadoRequisicao); 
        MenuMontador.instance(result).recuperarMenuListarRequisicoesAvancado();
        result.include("requisicoesTransporte", requisicoesTransporte);
        result.include("dataInicio", dataInicio);		
        result.include("dataFim", dataFim);		
        result.include("estadoRequisicao", estadoRequisicao);		
        result.include("estReq", estReq);
        result.use(Results.page()).of(RequisicaoController.class).listar();
    }
    
    @Path("/listarAvancadoPAprovar")
    public void listarAvancadoPAprovar(Calendar dataInicio, Calendar dataFim, EstadoRequisicao estadoRequisicao) throws Exception {
		EstadoRequisicao estReq = EstadoRequisicao.CANCELADA;
		List<RequisicaoTransporte> requisicoesTransporte = RequisicaoTransporte.buscarRequisicoesAvancado(dataInicio, dataFim, getTitular().getOrgaoUsuario(), estadoRequisicao); 
        MenuMontador.instance(result).recuperarMenuListarPAprovarRequisicoesAvancado();
        EstadoRequisicao[] estadosRequisicao = { EstadoRequisicao.ABERTA, EstadoRequisicao.AUTORIZADA, EstadoRequisicao.REJEITADA };
        filtrarRequisicoes(requisicoesTransporte, estadosRequisicao);
        result.include("requisicoesTransporte", requisicoesTransporte);
        result.include("dataInicio", dataInicio);		
        result.include("dataFim", dataFim);		
        result.include("estadoRequisicao", estadoRequisicao);		
        result.include("estReq", estReq);        
        result.include("estadosRequisicao", estadosRequisicao);
        result.use(Results.page()).of(RequisicaoController.class).listarPAprovar();
    }

    @RoleAdmin
    @RoleAdminMissao
    @RoleAdminMissaoComplexo
    @RoleAprovador
    @Path("/listarPAprovarFiltrado/{estadoRequisicao}")
    public void listarPAprovarFiltrado(EstadoRequisicao estadoRequisicao) {
        EstadoRequisicao[] estadosRequisicao = { estadoRequisicao };
        carregarRequisicoesUltimosDiasInformadosPorEstados(estadosRequisicao);
        MenuMontador.instance(result).recuperarMenuListarPAprovarRequisicoes(estadoRequisicao);
        EstadoRequisicao estReq = EstadoRequisicao.CANCELADA;
        result.include("estReq", estReq);
        result.include(ESTADO_REQUISICAO, estadoRequisicao);
        result.use(Results.page()).of(RequisicaoController.class).listarPAprovar();
    }

    @Transactional
    @Path("/salvar")
    public void salvar(RequisicaoTransporte requisicaoTransporte, TipoDePassageiro[] tiposDePassageiros, boolean checkRetorno, boolean checkSemPassageiros) throws Exception {
        validar(requisicaoTransporte, checkSemPassageiros, tiposDePassageiros, checkRetorno);
        requisicaoTransporte.setTiposDePassageiro(converterTiposDePassageiros(tiposDePassageiros));
        redirecionarSeErroAoSalvar(requisicaoTransporte, checkRetorno, checkSemPassageiros);

        DpPessoa dpPessoa = recuperaPessoa(requisicaoTransporte.getIdSolicitante());
        checarSolicitante(dpPessoa.getIdInicial(), autorizacaoGI.getComplexoPadrao().getIdComplexo(), true);

        requisicaoTransporte.setCpOrgaoUsuario(getTitular().getOrgaoUsuario());

        requisicaoTransporte.setCpComplexo(autorizacaoGI.getComplexoPadrao());

        requisicaoTransporte.setSequence(requisicaoTransporte.getCpOrgaoUsuario());
        boolean novaRequisicao = false;

        if (requisicaoTransporte.getId() == 0) {
            novaRequisicao = true;
            requisicaoTransporte.setDataHora(Calendar.getInstance());
        }

        requisicaoTransporte.setSolicitante(recuperaPessoa(requisicaoTransporte.getIdSolicitante()));
        requisicaoTransporte.save();

        if (novaRequisicao) {
            Andamento andamento = new Andamento();
            andamento.setDescricao("NOVA REQUISICAO");
            andamento.setDataAndamento(Calendar.getInstance());
            andamento.setEstadoRequisicao(EstadoRequisicao.ABERTA);
            andamento.setRequisicaoTransporte(requisicaoTransporte);
            andamento.setResponsavel(getCadastrante());
            andamento.save();
        }

        result.redirectTo(this).listar();
    }

    private void validar(RequisicaoTransporte requisicaoTransporte, boolean checkSemPassageiros, TipoDePassageiro[] tiposDePassageiros, boolean checkRetorno) {
        if ((requisicaoTransporte.getDataHoraSaidaPrevista() != null) && (requisicaoTransporte.getDataHoraRetornoPrevisto() != null) && 
        	(!requisicaoTransporte.ordemDeDatasCorreta()) || (checkRetorno && requisicaoTransporte.getDataHoraRetornoPrevisto() == null)) {
            validator.add(new I18nMessage("dataHoraRetornoPrevisto", "requisicaoTransporte.dataHoraRetornoPrevistoOrdemCorreta.validation"));
        }

        if (!checkSemPassageiros && ((tiposDePassageiros == null) || (tiposDePassageiros.length == 0))) {
            validator.add(new I18nMessage("tiposDePassageiros", "requisicaoTransporte.tiposDePassageiros.validation"));
        }

        if (!checkSemPassageiros && (requisicaoTransporte.getPassageiros() == null || requisicaoTransporte.getPassageiros().isEmpty())) {
            validator.add(new I18nMessage("passageiros", "requisicaoTransporte.passageiros.validation"));
        }

        if (requisicaoTransporte.getTipoFinalidade().ehOutra() && requisicaoTransporte.getFinalidade() == null) {
            validator.add(new I18nMessage("finalidade", "requisicaoTransporte.finalidade.validation"));
        }

		if(!autorizacaoGI.ehAdministrador()) {
			Calendar hoje = Calendar.getInstance();
			if ( requisicaoTransporte.getDataHoraSaidaPrevista() != null && (requisicaoTransporte.getDataHoraSaidaPrevista().before(hoje)) ){				
				validator.add(new I18nMessage("dataHoraSaidaPrevistaRetroativa", "requisicaoTransporte.dataHoraSaidaPrevistaRetroativa.validation"));
			}
			if ( requisicaoTransporte.getDataHoraRetornoPrevisto() != null && (requisicaoTransporte.getDataHoraRetornoPrevisto().before(hoje)) ){				
				validator.add(new I18nMessage("dataHoraRetornoPrevistoRetroativo", "requisicaoTransporte.dataHoraRetornoPrevistoRetroativo.validation"));
			}			
		}	
		else
		{			
			Calendar ultimos30dias = Calendar.getInstance();
			ultimos30dias.add(Calendar.DATE, -30);
			if (requisicaoTransporte.getDataHoraSaidaPrevista() != null && (requisicaoTransporte.getDataHoraSaidaPrevista().before(ultimos30dias)) ){				
				validator.add(new I18nMessage("dataHoraSaidaPrevistaRetroativaAdmin", "requisicaoTransporte.dataHoraSaidaPrevistaRetroativaAdmin.validation"));
			}
			if (requisicaoTransporte.getDataHoraRetornoPrevisto() != null && (requisicaoTransporte.getDataHoraRetornoPrevisto().before(ultimos30dias)) ){				
				validator.add(new I18nMessage("dataHoraRetornoPrevistoRetroativoAdmin", "requisicaoTransporte.dataHoraRetornoPrevistoRetroativoAdmin.validation"));
			}			
		}
        validator.validate(requisicaoTransporte);
    }

    private void carregarRequisicoesUltimosDiasInformadosPorEstados(EstadoRequisicao[] estadosRequisicao) {
        StringBuilder criterioBusca = new StringBuilder();
		int totalDias = Integer.parseInt(Parametro.buscarValorEmVigor("total.dias.pesquisa", getTitular(), autorizacaoGI.getComplexoPadrao()));
        criterioBusca.append("((dataHoraRetornoPrevisto is null and dataHoraSaidaPrevista >= :ultimosdias) or (dataHoraRetornoPrevisto >= :ultimosdias)) and cpOrgaoUsuario = :cpOrgaoUsuario ");
        Calendar ultimosdias = Calendar.getInstance();
        ultimosdias.add(Calendar.DATE, -totalDias);
        HashMap<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("ultimosdias", ultimosdias);
        parametros.put("cpOrgaoUsuario", getTitular().getOrgaoUsuario());
        recuperarRequisicoes(criterioBusca, parametros, estadosRequisicao);
    }

    protected void recuperarRequisicoes(StringBuilder criterioBusca, HashMap<String, Object> parametros, EstadoRequisicao[] estadosRequisicao) {
        if (!autorizacaoGI.ehAdministrador() && !autorizacaoGI.ehAdministradorMissao() && !autorizacaoGI.ehAdministradorMissaoPorComplexo() && !autorizacaoGI.ehAprovador()) {
            criterioBusca.append(" and solicitante.idPessoaIni = :idPessoaIni");
            parametros.put("idPessoaIni", getTitular().getIdInicial());
        } else {
            if (autorizacaoGI.ehAdministradorMissaoPorComplexo() || autorizacaoGI.ehAprovador()) {
                criterioBusca.append(" and cpComplexo = :cpComplexo");

                if (autorizacaoGI.ehAdministradorMissaoPorComplexo()) {
                    parametros.put("cpComplexo", autorizacaoGI.getComplexoAdministrador());
                } else {
                    parametros.put("cpComplexo", autorizacaoGI.getComplexoPadrao());
                }
            }
        }
        criterioBusca.append(" order by dataHoraSaidaPrevista desc");
        List<RequisicaoTransporte> requisicoesTransporte = RequisicaoTransporte.AR.find(criterioBusca.toString(), parametros).fetch();

        if (estadosRequisicao != null) {
            filtrarRequisicoes(requisicoesTransporte, estadosRequisicao);
        }
        result.include("requisicoesTransporte", requisicoesTransporte);
    }

    private void filtrarRequisicoes(List<RequisicaoTransporte> requisicoesTransporte, EstadoRequisicao[] estadosRequisicao) {
        Boolean filtrarRequisicao;
        for (Iterator<RequisicaoTransporte> iterator = requisicoesTransporte.iterator(); iterator.hasNext();) {
            filtrarRequisicao = true;
            RequisicaoTransporte requisicaoTransporte = iterator.next();

            for (EstadoRequisicao estadoRequisicao : estadosRequisicao) {
                if (requisicaoTransporte.getUltimoAndamento().getEstadoRequisicao().equals(estadoRequisicao)) {
                    filtrarRequisicao = false;
                    break;
                }
            }

            if (filtrarRequisicao) {
                iterator.remove();
            }
        }
    }

    private List<TipoDePassageiro> converterTiposDePassageiros(TipoDePassageiro[] tiposDePassageiros) {
        List<TipoDePassageiro> tiposParaSalvar = new ArrayList<TipoDePassageiro>();

        if ((tiposDePassageiros == null) || (tiposDePassageiros.length == 0)) {
            tiposParaSalvar.add(TipoDePassageiro.NENHUM);
        }
        else {
            for (int i = 0; i < tiposDePassageiros.length; i++) {
                tiposParaSalvar.add(tiposDePassageiros[i]);
            }
        }

        return tiposParaSalvar;
    }
    
	private void recuperarERenderizarTiposDePassageiro(DpPessoa usuario, CpComplexo complexo) {
		List<TipoDePassageiro> tipos = TipoDePassageiro.valuesParaComboRequisicao(usuario, complexo,  autorizacaoGI.ehAdministrador());
 		result.include("opcoesDeTiposDePassageiro", tipos);
	}

    @Transactional
	@Path("/salvarAndamentos")
    public void salvarAndamentos(@Valid RequisicaoTransporte requisicaoTransporte, boolean checkRetorno, boolean checkSemPassageiros) throws Exception{
        redirecionarSeErroAoSalvar(requisicaoTransporte, checkRetorno, checkSemPassageiros);
        checarSolicitante(requisicaoTransporte.getSolicitante().getIdInicial(), requisicaoTransporte.getCpComplexo().getIdComplexo(), true);

        requisicaoTransporte.setCpOrgaoUsuario(getTitular().getOrgaoUsuario());
        requisicaoTransporte.save();
        requisicaoTransporte.refresh();

        if (requisicaoTransporte.getId() == 0) {
            Andamento andamento = new Andamento();
            andamento.setDescricao("NOVA REQUISICAO");
            andamento.setDataAndamento(Calendar.getInstance());
            andamento.setEstadoRequisicao(EstadoRequisicao.ABERTA);
            andamento.setRequisicaoTransporte(requisicaoTransporte);
            andamento.setResponsavel(getCadastrante());
            andamento.save();
        }

        result.redirectTo(this).listar();
    }

    private void redirecionarSeErroAoSalvar(RequisicaoTransporte requisicaoTransporte, boolean checkRetorno, boolean checkSemPassageiros) throws Exception {
        if (validator.hasErrors()) {
            carregarTiposDeCarga(requisicaoTransporte);
            recuperarERenderizarTiposDePassageiro(getTitular(), autorizacaoGI.getComplexoPadrao());
            carregarFinalidades();

            result.include(REQUISICAO_TRANSPORTE, requisicaoTransporte);
            result.include(CHECK_RETORNO, checkRetorno);
            result.include("checkSemPassageiros", checkSemPassageiros);
            result.include(TIPOS_REQUISICAO, TipoRequisicao.values());

            if (requisicaoTransporte.getId() > 0) {
                validator.onErrorUse(Results.page()).of(RequisicaoController.class).editar(requisicaoTransporte.getId());
            }
            else {
                validator.onErrorUse(Results.page()).of(RequisicaoController.class).incluir();
            }
        }
    }

    protected void carregarFinalidades() throws Exception {
    	boolean mostrarOutra;
		mostrarOutra = autorizacaoGI.ehAdministrador() ? true : Boolean.valueOf(Parametro.buscarValorEmVigor("mostrarTipoFinalidadeOutra", getTitular(), autorizacaoGI.getComplexoPadrao()));
    	List<FinalidadeRequisicao> finalidades = FinalidadeRequisicao.listarParaCombo(mostrarOutra);
     	result.include("finalidades", finalidades);
     	result.include("mostrarDetalhes", mostrarOutra);
    }

    @Path("/incluir")
    public void incluir() throws Exception {
        RequisicaoTransporte requisicaoTransporte = new RequisicaoTransporte();
        DpPessoa dpPessoa = getTitular();
        requisicaoTransporte.setSolicitante(dpPessoa);
        requisicaoTransporte.setIdSolicitante(dpPessoa.getId());

        carregarTiposDeCarga(requisicaoTransporte);
        recuperarERenderizarTiposDePassageiro(getTitular(), autorizacaoGI.getComplexoPadrao());
        carregarFinalidades();

        recuperarERenderizarTiposDePassageiro(getTitular(), autorizacaoGI.getComplexoPadrao());
        result.include(TIPOS_REQUISICAO, TipoRequisicao.values());
        result.include(REQUISICAO_TRANSPORTE, requisicaoTransporte);
    }

    @Path("/editar/{id}")
    public void editar(Long id) throws Exception {
        RequisicaoTransporte requisicaoTransporte = RequisicaoTransporte.AR.findById(id);
        checarSolicitante(requisicaoTransporte.getSolicitante().getIdInicial(), requisicaoTransporte.getCpComplexo().getIdComplexo(), true);
        requisicaoTransporte.setIdSolicitante(requisicaoTransporte.getSolicitante().getId());

        carregarTiposDeCarga(requisicaoTransporte);
        recuperarERenderizarTiposDePassageiro(getTitular(), autorizacaoGI.getComplexoPadrao());
        carregarFinalidades();
        boolean checkRetorno = requisicaoTransporte.getDataHoraRetornoPrevisto() == null ? false : true;

        result.include("esconderBotoes", false);
        result.include(REQUISICAO_TRANSPORTE, requisicaoTransporte);
        result.include(CHECK_RETORNO, checkRetorno);
        result.include(TIPOS_REQUISICAO, TipoRequisicao.values());
    }

    private void checarSolicitante(Long idSolicitante, Long idComplexo, Boolean escrita) {
        if (!autorizacaoGI.ehAdministrador() && !autorizacaoGI.ehAprovador() && !autorizacaoGI.ehAgente() && !autorizacaoGI.ehAdministradorMissao()
                && !autorizacaoGI.ehAdministradorMissaoPorComplexo()) {
            if (!getTitular().getIdInicial().equals(idSolicitante)) {
                try {
                    throw new Exception(MessagesBundle.getMessage(REQUISICOES_CHECAR_SOLICITANTE_EXCEPTION));
                } catch (Exception e) {
                    tratarExcecoes(e);
                }
            }
        } else if (autorizacaoGI.ehAgente()) {
            if (!getTitular().getIdInicial().equals(idSolicitante) && escrita) {
                try {
                    throw new Exception(MessagesBundle.getMessage(REQUISICOES_CHECAR_SOLICITANTE_EXCEPTION));
                } catch (Exception e) {
                    tratarExcecoes(e);
                }
            }
        } else if (autorizacaoGI.ehAdministradorMissaoPorComplexo()) {
            if (!autorizacaoGI.getComplexoAdministrador().getIdComplexo().equals(idComplexo) && escrita) {
                try {
                    throw new Exception(MessagesBundle.getMessage(REQUISICOES_CHECAR_SOLICITANTE_EXCEPTION));
                } catch (Exception e) {
                    tratarExcecoes(e);
                }
            } else if (autorizacaoGI.ehAprovador() && (!autorizacaoGI.getComplexoPadrao().getIdComplexo().equals(idComplexo) && escrita)) {
                try {
                    throw new Exception(MessagesBundle.getMessage(REQUISICOES_CHECAR_SOLICITANTE_EXCEPTION));
                } catch (Exception e) {
                    tratarExcecoes(e);
                }
            }
        }
    }

    @Path("/ler/{id}")
    public void ler(Long id) throws Exception  {
        RequisicaoTransporte requisicaoTransporte = RequisicaoTransporte.AR.findById(id);
        checarSolicitante(requisicaoTransporte.getSolicitante().getIdInicial(), requisicaoTransporte.getCpComplexo().getIdComplexo(), false);
        requisicaoTransporte.setIdSolicitante(requisicaoTransporte.getSolicitante().getId());
        MenuMontador.instance(result).recuperarMenuRequisicoes(id, false, false);
        carregarTiposDeCarga(requisicaoTransporte);
        recuperarERenderizarTiposDePassageiro(getTitular(), autorizacaoGI.getComplexoPadrao());
        carregarFinalidades();
        result.include(REQUISICAO_TRANSPORTE, requisicaoTransporte);
    }

    protected void carregarTiposDeCarga(RequisicaoTransporte req) {
    	TipoDePassageiro tipoDePassageiro = TipoDePassageiro.CARGA;
        boolean checkSemPassageiros = false;

        if ((req != null) && (req.getTiposDePassageiro() != null)) {
            checkSemPassageiros = req.getTiposDePassageiro().contains(TipoDePassageiro.NENHUM);
        }

        result.include("tipoDePassageiro", tipoDePassageiro);
        recuperarERenderizarTiposDePassageiro(getTitular(), autorizacaoGI.getComplexoPadrao());
        result.include("checkSemPassageiros", checkSemPassageiros);
    }

    @Path("/ler")
    public void ler() throws Exception {
        result.include("esconderBotoes", true);
    }

    @Path("/buscarPelaSequence")
    public void buscarPelaSequence(boolean popUp, String sequence) throws Exception  {
        RequisicaoTransporte requisicaoTransporte = recuperarPelaSigla(sequence, popUp);
        carregarTiposDeCarga(requisicaoTransporte);
        recuperarERenderizarTiposDePassageiro(getTitular(), autorizacaoGI.getComplexoPadrao());
        carregarFinalidades();

        result.include(TIPOS_REQUISICAO, TipoRequisicao.values());
        result.include(REQUISICAO_TRANSPORTE, requisicaoTransporte);
        result.forwardTo(this).ler();
    }

    protected RequisicaoTransporte recuperarPelaSigla(String sequence, boolean popUp) {
        RequisicaoTransporte requisicaoTransporte = RequisicaoTransporte.buscar(sequence);
        checarSolicitante(requisicaoTransporte.getSolicitante().getIdInicial(), requisicaoTransporte.getCpComplexo().getIdComplexo(), false);
        MenuMontador.instance(result).recuperarMenuRequisicoes(requisicaoTransporte.getId(), popUp, false);
        requisicaoTransporte.setIdSolicitante(requisicaoTransporte.getSolicitante().getId());

        if (!popUp) {
            MenuMontador.instance(result).recuperarMenuRequisicoes(requisicaoTransporte.getId(), popUp, false);
        }

        result.include(REQUISICAO_TRANSPORTE, requisicaoTransporte);

        if (null != requisicaoTransporte.getDataHoraRetornoPrevisto()) {
            result.include(CHECK_RETORNO, true);
        }

        return requisicaoTransporte;
    }

    @Transactional
    @Path("/excluir/{id}")
    public void excluir(Long id) throws Exception {
        RequisicaoTransporte requisicaoTransporte = RequisicaoTransporte.AR.findById(id);
        checarSolicitante(requisicaoTransporte.getSolicitante().getIdInicial(), requisicaoTransporte.getCpComplexo().getIdComplexo(), true);
        requisicaoTransporte.setIdSolicitante(requisicaoTransporte.getSolicitante().getId());

        try {
            requisicaoTransporte.excluir(false);
        } catch (SigaTpException ex) {
            error(true, REQUISICAO_TRANSPORTE, ex.getMessage().toString());
            if (validator.hasErrors()) {
                MenuMontador.instance(result).recuperarMenuRequisicoes(requisicaoTransporte.getId(), true, false);
                carregarTiposDeCarga(requisicaoTransporte);
                carregarFinalidades();
                result.include(REQUISICAO_TRANSPORTE, requisicaoTransporte);
                result.redirectTo(this).ler();
            }
        } catch (Exception ex) {
            throw ex;
        }

        result.redirectTo(this).listar();
    }

    private DpPessoa recuperaPessoa(Long idSolicitante) {
        DpPessoa dpPessoa = DpPessoa.AR.findById(idSolicitante);
        HashMap<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("idPessoaIni",  dpPessoa.getIdInicial());
        return DpPessoa.AR.find("idPessoaIni = :idPessoaIni and dataFimPessoa = null", parametros).first();
    }

    private void tratarExcecoes(Exception e) {
        throw new AplicacaoException(e.getMessage());
    }
    
    protected List<RequisicaoTransporte> podeCancelar(Missao missaoAExcluirNaVerificacao) throws Exception {
		List<RequisicaoTransporte> requisicoesParaCancelar = new ArrayList<RequisicaoTransporte>();
		
		List<RequisicaoTransporte> requisicoesDaMissao = missaoAExcluirNaVerificacao.getRequisicoesTransporte();
		
		for (Iterator<RequisicaoTransporte> it = requisicoesDaMissao.iterator(); it.hasNext();) {
			RequisicaoTransporte req = (RequisicaoTransporte) it.next();
			if(req.getMissoes().size() == 1) {
				requisicoesParaCancelar.add(req);
			} else {
				List<Missao> missoesDaReq = req.getMissoes();
				boolean podeCancelar = true;
				for (Iterator<Missao> it2 = missoesDaReq.iterator(); it2.hasNext();) {
					Missao missao = (Missao) it2.next();
					if(!missao.equals(missaoAExcluirNaVerificacao)) {
						podeCancelar = podeCancelar && missaoCanceladaOuFinalizadaSemAtender(missao, req);
					}
				}
				if(podeCancelar) {
					requisicoesParaCancelar.add(req);
				}
			}
		}
		
		return requisicoesParaCancelar;
	}

	private boolean missaoCanceladaOuFinalizadaSemAtender(Missao missaoAConsultar, RequisicaoTransporte requisicaoAConsultar) {
		boolean cancelada = missaoAConsultar.getEstadoMissao().equals(EstadoMissao.CANCELADA);
		
		boolean finalizadaSemAtender;
		try {
	        HashMap<String, Object> parametros = new HashMap<String, Object>();
	        parametros.put("requisicaoTransporte", requisicaoAConsultar);
	        parametros.put("missao", missaoAConsultar);
			Andamento andamentoEmComum = (Andamento) Andamento.AR.find("requisicaoTransporte = :requisicaoTransporte and missao = :missao order by dataAndamento desc", parametros).first();
			finalizadaSemAtender = andamentoEmComum.getEstadoRequisicao().equals(EstadoRequisicao.NAOATENDIDA);
		} catch (Exception e) {
			finalizadaSemAtender = true;
		}
		
		return cancelada || finalizadaSemAtender;
	}

	/* Metodo AJAX */
	@Path("/getRequisicaoLinhasTotal")
    public void getRequisicaoLinhasTotal() {
		result.use(Results.http()).body(Parametro.buscarConfigSistemaEmVigor("requisicao.linhas.total"));
    }
}