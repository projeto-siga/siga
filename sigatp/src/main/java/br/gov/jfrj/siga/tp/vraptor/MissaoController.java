package br.gov.jfrj.siga.tp.vraptor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.tp.auth.AutorizacaoGI;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdmin;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissao;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissaoComplexo;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAgente;
import br.gov.jfrj.siga.tp.enums.Template;
import br.gov.jfrj.siga.tp.model.Andamento;
import br.gov.jfrj.siga.tp.model.CategoriaCNH;
import br.gov.jfrj.siga.tp.model.Condutor;
import br.gov.jfrj.siga.tp.model.EscalaDeTrabalho;
import br.gov.jfrj.siga.tp.model.EstadoMissao;
import br.gov.jfrj.siga.tp.model.EstadoRequisicao;
import br.gov.jfrj.siga.tp.model.Missao;
import br.gov.jfrj.siga.tp.model.NivelDeCombustivel;
import br.gov.jfrj.siga.tp.model.Parametro;
import br.gov.jfrj.siga.tp.model.RequisicaoTransporte;
import br.gov.jfrj.siga.tp.model.RequisicaoVsEstado;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.model.Veiculo;
import br.gov.jfrj.siga.tp.model.vo.ItemVO;
import br.gov.jfrj.siga.tp.model.vo.MissaoVO;
import br.gov.jfrj.siga.tp.util.FormatarDataHora;
import br.gov.jfrj.siga.tp.util.PerguntaSimNao;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/missao")
public class MissaoController extends TpController {

	private static final String MOSTRAR_DADOS_PROGRAMADA = "mostrarDadosProgramada";
    private static final String MOSTRAR_BOTOES_EDITAR = "mostrarBotoesEditar";
    private static final String MISSOES_AUTORIZACAO_GI_SEM_ACESSO_EXCEPTION = "missoes.autorizacaoGI.semAcesso.exception";
	private static final String MISSAO_REQUISICOES_TRANSPORTE_REQUIRED = "missao.requisicoesTransporte.required";
	private static final String ODOMETRO_RETORNO_EM_KM_STR = "odometroRetornoEmKm";
	private static final String VEICULOS_STR = "veiculos";
	private static final String MOSTRAR_DADOS_INICIADA_STR = "mostrarDadosIniciada";
	private static final String MOSTRAR_DADOS_FINALIZADA_STR = "mostrarDadosFinalizada";
	private static final String MISSAO_STR = "missao";
	private static final String MISSOES_STR = "missoes";
	private static final String ESTADO_MISSAO_STR = "estadoMissao";
	private static final String CONDUTORES_STR = "condutores";
	private static final String CONDUTOR_STR = "condutor";
	private static final String REQUISICOES_TRANSPORTE_STR = "requisicoesTransporte";
	private static final String PATTERN_DDMMYYYYHHMM = "dd/MM/yyyy HH:mm";
	private static final String PATTERN_DDMMYYYYHHMM_MYSQL = "yyyy-MM-dd HH:mm";
	private static final String MISSAO_CONDUTOR_REQUIRED = "missao.condutor.required";
	private static final String PROGRAMAR_RAPIDO =  "programarRapido";
	private static final String MISSOESPORCONDUTOREESCALA_STR = "missoesPorCondutoreEscala";

	@Inject
	private AutorizacaoGI autorizacaoGI;
	private RequisicaoController requisicaoController;
	
	/**
	 * @deprecated CDI eyes only
	 */
	public MissaoController() {
		super();
	}
	
	@Inject
	public MissaoController(HttpServletRequest request, Result result,  Validator validator, SigaObjects so,  EntityManager em, RequisicaoController requisicaoController){
		super(request, result, TpDao.getInstance(), validator, so, em);
		this.requisicaoController = requisicaoController;
	}

	@RoleAdmin
	@RoleAdminMissao
	@RoleAgente
	@RoleAdminMissaoComplexo
	@Path("/listar")
	public void listar() throws Exception {
		HashMap<String, Object> parametros = new HashMap<String,Object>();
		parametros.put("cpOrgaoUsuario", getTitular().getOrgaoUsuario());
		parametros.put("estado1", EstadoMissao.PROGRAMADA);
		parametros.put("estado2", EstadoMissao.INICIADA);	
		parametros.put("estado3", EstadoMissao.FINALIZADA);
		parametros.put("estado4", EstadoMissao.CANCELADA);		
		StringBuilder criterio = new StringBuilder();
		criterio.append("cpOrgaoUsuario = :cpOrgaoUsuario and estadoMissao in (:estado1,:estado2,:estado3,:estado4)");
		List<Missao> missoes = carregarMissoesUltimosDiasInformadosPorEstados(criterio, parametros);
		EstadoMissao estadoMissao = EstadoMissao.PROGRAMADA;
		EstadoMissao estMis = EstadoMissao.CANCELADA;
		MenuMontador.instance(result).recuperarMenuMissoes(null);
		montarComboCondutor();
		
		result.include("estMis", estMis);
		result.include(MISSOES_STR, missoes);
		result.include(ESTADO_MISSAO_STR, estadoMissao);
		result.include(CONDUTOR_STR, new Condutor());
	}
	
	public void listar(EstadoMissao estado, String mensagem) throws Exception  {
		error(null != mensagem, "missao", mensagem);
		MenuMontador.instance(result).recuperarMenuMissoes(estado);
		validator.onErrorUse(Results.logic()).forwardTo(MissaoController.class).listarFiltrado(estado);
	}
	
	private List<Missao> carregarMissoesUltimosDiasInformadosPorEstados(StringBuilder criterio, HashMap<String, Object> parametrosBuscar) throws Exception {
        StringBuilder criterioBusca = new StringBuilder();
		int totalDias = Integer.parseInt(Parametro.buscarValorEmVigor("total.dias.pesquisa", getTitular(), autorizacaoGI.getComplexoPadrao()));
		criterioBusca.append("((dataHoraRetorno is null and dataHoraSaida >= :ultimosdias) or (dataHoraRetorno >= :ultimosdias)) and cpOrgaoUsuario = :cpOrgaoUsuario ");
        criterioBusca.append(" and ").append(criterio);
        Calendar ultimosdias = Calendar.getInstance();
        ultimosdias.add(Calendar.DATE, -totalDias);
        parametrosBuscar.put("ultimosdias",ultimosdias);
    
        return recuperarMissoes(criterioBusca, parametrosBuscar);
    }	
	
	private List<Missao> recuperarMissoes(StringBuilder criterioBusca, HashMap<String, Object> parametrosParaBuscar) throws Exception {

		if (!autorizacaoGI.ehAdministrador() && !autorizacaoGI.ehAdministradorMissao() && !autorizacaoGI.ehAdministradorMissaoPorComplexo()) {
			Condutor condutorLogado = Condutor.recuperarLogado(getTitular(), getTitular().getOrgaoUsuario());
			if (condutorLogado != null) {
				criterioBusca.append(" and condutor = :condutor");
				parametrosParaBuscar.put("condutor", condutorLogado);
			}

		} else if (autorizacaoGI.ehAdministradorMissaoPorComplexo()) {
			criterioBusca.append(" and cpComplexo = :cpComplexo");
			parametrosParaBuscar.put("cpComplexo", autorizacaoGI.getComplexoAdministrador());
		}

		return Missao.AR.find(criterioBusca.toString() + " order by dataHoraSaida desc", parametrosParaBuscar).fetch();
	}

	@RoleAgente
	@Path("/listarPorCondutorLogado")
	public void listarPorCondutorLogado() throws Exception {
		Condutor condutorLogado = Condutor.recuperarLogado(getTitular(), getTitular().getOrgaoUsuario());
		List<Missao> missoes = Missao.buscarTodasAsMissoesPorCondutor(condutorLogado);
		EstadoMissao estMis = EstadoMissao.CANCELADA;
		MenuMontador.instance(result).recuperarMenuMissoes(null);
		EstadoMissao estadoMissao = EstadoMissao.PROGRAMADA;
		montarComboCondutor();

		result.include("estMis", estMis);
		result.include(MISSOES_STR, missoes);
		result.include(ESTADO_MISSAO_STR, estadoMissao);
		result.include(CONDUTOR_STR, new Condutor());
		result.use(Results.page()).of(MissaoController.class).listar();
	}

	@RoleAgente
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/listarAvancado")
	public void listarAvancado(Condutor condutorEscalado, EstadoMissao estadoMissao,  Calendar dataInicio, Calendar dataFim) throws Exception {
		Condutor condutorEncontrado = Condutor.AR.findById(condutorEscalado.getId());
		List<Missao> missoes = Missao.buscarTodasAsMissoesAvancado(condutorEncontrado, estadoMissao, dataInicio, dataFim);
		EstadoMissao estMis = EstadoMissao.PROGRAMADA;
		MenuMontador.instance(result).recuperarMenuMissoesAvancado();
		validarListarParaCondutorEscalado(condutorEncontrado);
		montarComboCondutor();

		result.include("estMis", estMis);
		result.include(MISSOES_STR, missoes);
		result.include("condutorEscalado", condutorEncontrado);
		result.include(ESTADO_MISSAO_STR, estadoMissao);
		result.include("dataInicio", dataInicio);
		result.include("dataFim", dataFim);
		result.include("estadoMissao", estadoMissao);
		result.use(Results.page()).of(MissaoController.class).listar();
	}

	protected void validarListarParaCondutorEscalado(Condutor condutorEscalado) throws Exception {
		Missao missao = new Missao();
		missao.setId(-1L);
		missao.setCondutor(condutorEscalado);
		checarCondutorPeloUsuarioAutenticado(missao);
	}

	@RoleAgente
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
    @Path({ "/listarFiltrado/{estado}", "/listarFiltrado" })
	public void listarFiltrado(EstadoMissao estado) throws Exception {
		EstadoMissao estadoMissao = seEstadoNuloUsarDefault(estado);
		EstadoMissao estMis = EstadoMissao.CANCELADA;
		
		HashMap<String, Object> parametros = new HashMap<String,Object>();
		parametros.put("cpOrgaoUsuario", getTitular().getOrgaoUsuario());
		parametros.put("estadoMissao", estadoMissao);
		StringBuilder criterio = new StringBuilder();
		criterio.append("cpOrgaoUsuario = :cpOrgaoUsuario and estadoMissao = :estadoMissao");
		List<Missao> missoes = carregarMissoesUltimosDiasInformadosPorEstados(criterio, parametros);
		MenuMontador.instance(result).recuperarMenuMissoes(estado);
		montarComboCondutor();

		result.include("estMis", estMis);
		result.include(MISSOES_STR, missoes);
		result.include(ESTADO_MISSAO_STR, estadoMissao);
		result.include(CONDUTOR_STR, new Condutor());
		result.use(Results.page()).of(MissaoController.class).listar();
	}

	private EstadoMissao seEstadoNuloUsarDefault(EstadoMissao estado) {
		return null == estado ? EstadoMissao.PROGRAMADA : estado;
	}

    @Transactional
	@RoleAgente
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/salvar")
	public void salvar(Missao missao, List<RequisicaoTransporte> requisicoesTransporteAlt, List<RequisicaoTransporte> requisicoesTransporteAnt) throws Exception {
    	
    	error(null == missao.getCondutor() || null == missao.getVeiculo() || null == missao.getDataHoraSaida(), "missao", "views.erro.campoObrigatorio");
	    missao.setCpOrgaoUsuario(getTitular().getOrgaoUsuario());

	    Template template;
	    if (missao.getId() > 0)
	        template = Template.EDITAR;
	    else {
	        missao.setSequence(missao.getCpOrgaoUsuario());
	        template = Template.INCLUIR;
	    }

		if (requisicoesTransporteAlt == null || requisicoesTransporteAlt.isEmpty())
			validator.add(new I18nMessage(REQUISICOES_TRANSPORTE_STR, MISSAO_REQUISICOES_TRANSPORTE_REQUIRED));
		else
		    missao.setRequisicoesTransporte(requisicoesTransporteAlt);

		if (missao.getCondutor() == null)
			validator.add(new I18nMessage(REQUISICOES_TRANSPORTE_STR, MISSAO_CONDUTOR_REQUIRED));
		else	
			checarCategoriaCNHVeiculoCondutor(missao);
		
		redirecionarSeErroAoSalvar(missao, template);
		
		DpPessoa dpPessoa = getCadastrante();
		missao.setResponsavel(dpPessoa);

		validarRequisicoesDeServico(missao, template);

		boolean novaMissao = false;
		if (missao.getId() == 0) {
			novaMissao = true;
			missao.setDataHora(Calendar.getInstance());
		}

		checarCondutorPeloUsuarioAutenticado(missao);
		Missao missaoPronta = recuperarComplexoPeloPerfil(missao);
		missaoPronta.save();

		if (novaMissao)
			gravarAndamentos(dpPessoa, "PROGRAMADO NA MISSAO N. " + missaoPronta.getSequence(), missaoPronta.getRequisicoesTransporte(), missaoPronta, EstadoRequisicao.PROGRAMADA);
		else {
			deletarAndamentos(requisicoesTransporteAnt, missaoPronta);
			atualizarAndamentos(missaoPronta);
		}

		buscarPelaSequence(false, missaoPronta.getSequence());
	}

	private boolean validarRequisicoesDeServico(Missao missao, Template template) throws Exception {
		Boolean temRequisicaoDeServico = false;
		Veiculo veiculoInicial = null;

		for (Iterator<RequisicaoTransporte> iterator = missao.getRequisicoesTransporte().iterator(); iterator.hasNext();) {
			RequisicaoTransporte req = iterator.next();
			req = RequisicaoTransporte.AR.findById(req.getId());

			if (req.getServicoVeiculo() != null) {
				temRequisicaoDeServico = true;
				if (veiculoInicial == null)
					veiculoInicial = req.getServicoVeiculo().getVeiculo();
				else if (!veiculoInicial.equals(req.getServicoVeiculo().getVeiculo())) {
					validator.add(new I18nMessage("veiculo", "missoes.veiculo.validation"));
					redirecionarSeErroAoSalvar(missao, template);
				}
			}
		}

		return temRequisicaoDeServico;
	}

	private void gravarAndamentos(DpPessoa dpPessoa, String descricao, RequisicaoTransporte[] requisicoesTransporte, Missao missao, EstadoRequisicao[] estadosRequisicao) {

		for (int i = 0; i < requisicoesTransporte.length; i++) {
			RequisicaoTransporte requisicaoTransporte = requisicoesTransporte[i];

			Andamento andamento = new Andamento();
			andamento.setDescricao(descricao);
			andamento.setDataAndamento(Calendar.getInstance());
			andamento.setEstadoRequisicao(estadosRequisicao[i]);
			andamento.setRequisicaoTransporte(requisicaoTransporte);
			andamento.setResponsavel(dpPessoa);
			andamento.setMissao(missao);
			andamento.save();
		}
	}

	private void gravarAndamentos(DpPessoa dpPessoa, String descricao, List<RequisicaoTransporte> requisicoesTransporte, Missao missao, EstadoRequisicao estadoRequisicao) {
		for (RequisicaoTransporte requisicaoTransporte : requisicoesTransporte)
			gravaAndamento(dpPessoa, descricao, missao, estadoRequisicao, requisicaoTransporte);
	}

	private void deletarAndamentos(List<RequisicaoTransporte> requisicoesTransporte, Missao missao) throws Exception {
		HashMap<String, Object> parametros = new HashMap<String,Object>();
		for (RequisicaoTransporte requisicaoTransporte : requisicoesTransporte) {
			parametros.put("idRequisicaoTransporte", requisicaoTransporte.getId());
			List<Andamento> andamentos = Andamento.AR.find("requisicaoTransporte.id = :idRequisicaoTransporte order by id desc", parametros).fetch();
			parametros.clear();
			for (Andamento andamento : andamentos) {
				if (missaoEmAndamento(missao, andamento) && andamento.getEstadoRequisicao().equals(EstadoRequisicao.PROGRAMADA)) {
					andamento.delete();
				}
			}
		}
	}

	private void atualizarAndamentos(Missao missao) throws Exception {
		HashMap<String, Object> parametros = new HashMap<String,Object>();
		for (RequisicaoTransporte requisicaoTransporte : missao.getRequisicoesTransporte()) {
			parametros.put("idRequisicaoTransporte", requisicaoTransporte.getId());
			List<Andamento> andamentos = Andamento.AR.find("requisicaoTransporte.id = :idRequisicaoTransporte order by id desc", parametros).fetch();
			parametros.clear();
			for (Andamento andamento : andamentos)
				if (!missaoEmAndamento(missao, andamento)) {
					gravaAndamento(getCadastrante(), "PROGRAMADA", missao, EstadoRequisicao.PROGRAMADA, requisicaoTransporte);
					break;
				}
		}
	}

	private boolean missaoEmAndamento(Missao missao, Andamento andamento) {
		return andamento.getMissao() != null && andamento.getMissao().getId().equals(missao.getId());
	}

	private void gravaAndamento(DpPessoa dpPessoa, String descricao, Missao missao, EstadoRequisicao estadoRequisicao, RequisicaoTransporte requisicaoTransporte) {
		Andamento andamento = new Andamento();
		andamento.setDescricao(descricao);
		andamento.setDataAndamento(Calendar.getInstance());
		andamento.setEstadoRequisicao(estadoRequisicao);
		andamento.setRequisicaoTransporte(requisicaoTransporte);
		andamento.setResponsavel(dpPessoa);
		andamento.setMissao(missao);
		andamento.save();
	}

	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@RoleAgente
	@Path("/finalizar/{id}")
	public void finalizar(Long id) throws Exception {
		Missao missao = Missao.AR.findById(id);
		montarDadosSeDataHoraSaidaNotNull(missao);
		checarCondutorPeloUsuarioAutenticado(missao);
		Integer i = 0;
		RequisicaoVsEstado[] requisicoesVsEstados = new RequisicaoVsEstado[missao.getRequisicoesTransporte().size()];
		for (RequisicaoTransporte requisicaoTransporte : missao.getRequisicoesTransporte()) {
			RequisicaoVsEstado requisicaoVsEstados = new RequisicaoVsEstado();
			requisicaoVsEstados.setIdRequisicaoTransporte(requisicaoTransporte.getId());
			requisicaoVsEstados.setEstado(requisicaoTransporte.getUltimoEstado());
			requisicoesVsEstados[i] = requisicaoVsEstados;
			i = i + 1;
		}

		result.include(MISSAO_STR, missao);
		result.include("requisicoesVsEstados", requisicoesVsEstados);
		result.include("mostrarBotoesFinalizar", true);
		result.include(MOSTRAR_DADOS_PROGRAMADA, true);
		result.include(MOSTRAR_DADOS_INICIADA_STR, true);
		result.include(MOSTRAR_DADOS_FINALIZADA_STR, true);
		result.include("estadosRequisicao", EstadoRequisicao.valuesComboAtendimentoMissao());
		condicaoComponentesVeiculo();
	}

    @Transactional
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@RoleAgente
	@Path("/finalizarMissao")
	public void finalizarMissao(@Valid Missao missao, RequisicaoVsEstado[] requisicoesVsEstados) throws Exception {
		verificarDatasInicialFinal(missao);
		verificarOdometroSaidaZerado(missao);
		verificarOdometroRetornoZerado(missao);
		verificarOdometrosSaidaRetorno(missao);
		DpPessoa dpPessoa = getCadastrante();

		result.include("requisicoesVsEstados", requisicoesVsEstados);

		checarCategoriaCNHVeiculoCondutor(missao);
		redirecionarSeErroAoSalvar(missao, Template.FINALIZAR);
		missao.setCpOrgaoUsuario(getTitular().getOrgaoUsuario());
		missao.setResponsavel(dpPessoa);
		missao.setEstadoMissao(EstadoMissao.FINALIZADA);
		checarCondutorPeloUsuarioAutenticado(missao);

		Missao missaoPronta = recuperarComplexoPeloPerfil(missao);
		missaoPronta.save();

		RequisicaoTransporte[] requisicoes = missaoPronta.getRequisicoesTransporte().toArray(new RequisicaoTransporte[missaoPronta.getRequisicoesTransporte().size()]);
		EstadoRequisicao[] estadosRequisicao = new EstadoRequisicao[missaoPronta.getRequisicoesTransporte().size()];
		for (int i = 0; i < requisicoes.length; i++) {
			estadosRequisicao[i] = RequisicaoVsEstado.encontrarEstadoNaLista(requisicoesVsEstados, requisicoes[i].getId());
		}

		gravarAndamentos(dpPessoa, "PELA MISSAO N." + missaoPronta.getSequence(), requisicoes, missaoPronta, estadosRequisicao);
		buscarPelaSequence(false, missaoPronta.getSequence());
	}

	private void verificarOdometrosSaidaRetorno(Missao missao) {
		if (missao.getOdometroSaidaEmKm() > missao.getOdometroRetornoEmKm()) {
			validator.add(new I18nMessage(ODOMETRO_RETORNO_EM_KM_STR, "missoes.odometroRetornoEmKm.validation"));
		}
	}

	private void verificarOdometroSaidaZerado(Missao missao) {
		if (isOdometroZerado(missao.getOdometroSaidaEmKm()))
			validator.add(new I18nMessage(ODOMETRO_RETORNO_EM_KM_STR, "missao.finalizar.odometro.saida"));
	}

	private void verificarOdometroRetornoZerado(Missao missao) {
		if (isOdometroZerado(missao.getOdometroRetornoEmKm()))
			validator.add(new I18nMessage(ODOMETRO_RETORNO_EM_KM_STR, "missao.finalizar.odometro.retorno"));
	}

    private boolean isOdometroZerado(Double odometro) {
        return odometro.equals(0.0);
    }    
    
    @Transactional
    @RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@RoleAgente
	@Path("/iniciarMissao")
	public void iniciarMissao(Missao missao, List<RequisicaoTransporte> requisicoesTransporteAlt) throws Exception {		
		if (missao.getDataHoraSaida() == null) {
			validator.add(new I18nMessage("dataHoraSaida", "missoes.dataHoraSaidaNulo.validation"));
		}
		else{
			verificarDisponibilidadeDeCondutor(missao);
			verificarOdometroSaidaZerado(missao);
		}
		
		DpPessoa dpPessoa = getCadastrante();

		if (null == requisicoesTransporteAlt || requisicoesTransporteAlt.isEmpty()) {
			missao.setRequisicoesTransporte(requisicoesTransporteAlt);
			validator.add(new I18nMessage(REQUISICOES_TRANSPORTE_STR, MISSAO_REQUISICOES_TRANSPORTE_REQUIRED));
		}

		checarCategoriaCNHVeiculoCondutor(missao);

		redirecionarSeErroAoSalvar(missao, Template.INICIAR);

		boolean temRequisicaoDeServico = validarRequisicoesDeServico(missao, Template.INICIAR);

		if (!temRequisicaoDeServico) {
			verificarDisponibilidadeDeVeiculo(missao);
		}

		missao.setCpOrgaoUsuario(getTitular().getOrgaoUsuario());
		missao.setResponsavel(dpPessoa);
		missao.setEstadoMissao(EstadoMissao.INICIADA);

		checarCondutorPeloUsuarioAutenticado(missao);

		Missao missaoPronta = recuperarComplexoPeloPerfil(missao);
		checarComplexo(missaoPronta.getCpComplexo().getIdComplexo());

		missaoPronta.save();
		gravarAndamentos(dpPessoa, "PELA MISSAO N." + missaoPronta.getSequence(), missaoPronta.getRequisicoesTransporte(), missaoPronta, EstadoRequisicao.EMATENDIMENTO);
		buscarPelaSequence(false, missaoPronta.getSequence());
	}

	protected Missao recuperarComplexoPeloPerfil(Missao missao) throws Exception {
		if (autorizacaoGI.ehAgente() || autorizacaoGI.ehAdministradorMissaoPorComplexo()) {
			RequisicaoTransporte req1 = RequisicaoTransporte.AR.findById(missao.getRequisicoesTransporte().get(0).getId());
			missao.setCpComplexo(req1.getCpComplexo());
		} else
			missao.setCpComplexo(autorizacaoGI.getComplexoPadrao());

		return missao;
	}

    @Transactional
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/iniciarMissaoRapido")
	public void iniciarMissaoRapido(Missao missao, List<RequisicaoTransporte> requisicoesTransporteAlt) throws Exception {
		Template template = Template.INICIORAPIDO;

		if (requisicoesTransporteAlt == null || requisicoesTransporteAlt.isEmpty())
		    validator.add(new I18nMessage(REQUISICOES_TRANSPORTE_STR, MISSAO_REQUISICOES_TRANSPORTE_REQUIRED));
		else
		    missao.setRequisicoesTransporte(requisicoesTransporteAlt);

		validarOdometro(missao);
		redirecionarSeErroAoSalvar(missao, template);

		missao.setCpOrgaoUsuario(getTitular().getOrgaoUsuario());
		missao.setSequence(missao.getCpOrgaoUsuario());
		DpPessoa dpPessoa = getCadastrante();
		missao.setResponsavel(dpPessoa);


		boolean temRequisicaoDeServico = validarRequisicoesDeServico(missao, template);

		if (!temRequisicaoDeServico)
			verificarDisponibilidadeDeVeiculo(missao);

		checarCategoriaCNHVeiculoCondutor(missao);
		redirecionarSeErroAoSalvar(missao, template);
		missao.setDataHora(Calendar.getInstance());
		missao.setInicioRapido(PerguntaSimNao.SIM);

		Missao missaoPronta = recuperarComplexoPeloPerfil(missao);
		checarCondutorPeloUsuarioAutenticado(missaoPronta);
		checarComplexo(missaoPronta.getCpComplexo().getIdComplexo());

		missaoPronta.save();

		gravarAndamentos(dpPessoa, "PROGRAMADO POR INICIO RAPIDO PARA MISSAO NO. " + missaoPronta.getSequence(), missaoPronta.getRequisicoesTransporte(), missaoPronta, EstadoRequisicao.PROGRAMADA);
		validarRequisicoesDeServico(missaoPronta, template);
		missaoPronta.setEstadoMissao(EstadoMissao.INICIADA);

		missaoPronta.save();
		gravarAndamentos(dpPessoa, "INICIO RAPIDO PELA MISSAO N." + missaoPronta.getSequence(), missaoPronta.getRequisicoesTransporte(), missaoPronta, EstadoRequisicao.EMATENDIMENTO);

		result.include("mostrarBotoesIniciarRapido", true);
		result.include(MOSTRAR_DADOS_PROGRAMADA, true);
		result.include(MOSTRAR_DADOS_INICIADA_STR, true);

		result.forwardTo(this).buscarPelaSequence(false, missaoPronta.getSequence());
	}

    private void validarOdometro(Missao missao) {
        error(missao.getOdometroSaidaEmKm().equals(0.0), MISSAO_STR, "veiculo.odometroEmKmAtual.zero.validation");
    }

	@RoleAdmin
	@RoleAgente
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/iniciar/{id}")
	public void iniciar(Long id) throws Exception {
		Missao missao = Missao.AR.findById(id);
		//montarCombos();
		montarDadosSeDataHoraSaidaNotNull(missao);
		checarCondutorPeloUsuarioAutenticado(missao);
		checarComplexo(missao.getCpComplexo().getIdComplexo());

		condicaoComponentesVeiculo();

		condicaoComponentesIniciarMissao(missao);
	}

    private void condicaoComponentesIniciarMissao(Missao missao) {
        result.include(MISSAO_STR, missao);
		result.include("mostrarBotoesIniciar", true);
		result.include(MOSTRAR_DADOS_PROGRAMADA, true);
		result.include(MOSTRAR_DADOS_INICIADA_STR, true);
		result.include(MOSTRAR_DADOS_FINALIZADA_STR, false);
    }

    private void condicaoComponentesVeiculo() {
        result.include("estepes", PerguntaSimNao.values());
		result.include("triangulos", PerguntaSimNao.values());
		result.include("cartoesSeguro", PerguntaSimNao.values());
		result.include("extintores", PerguntaSimNao.values());
		result.include("cartoesAbastecimento", PerguntaSimNao.values());
		result.include("limpeza", PerguntaSimNao.values());
		result.include("ferramentas", PerguntaSimNao.values());
		result.include("cartoesSaida", PerguntaSimNao.values());
		result.include("avariasAparentesRetorno", PerguntaSimNao.values());
		result.include("avariasAparentesSaida", PerguntaSimNao.values());
		result.include("licencas", PerguntaSimNao.values());

		result.include("nivelCombustivelRetorno", NivelDeCombustivel.values());
		result.include("niveisCombustivelSaida", NivelDeCombustivel.values());
    }

	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/cancelar/{id}")
	public void cancelar(Long id) throws Exception {
		Missao missao = Missao.AR.findById(id);
		checarCondutorPeloUsuarioAutenticado(missao);
		checarComplexo(missao.getCpComplexo().getIdComplexo());
		List<RequisicaoTransporte> requisicoesParaCancelar = new ArrayList<RequisicaoTransporte>();
		requisicoesParaCancelar.addAll(requisicaoController.podeCancelar(missao));
		result.include("requisicoesParaCancelar", requisicoesParaCancelar);
		result.include(MISSAO_STR, missao);
	}

    @Transactional
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/cancelarMissao")
	public void cancelarMissao(@Valid Missao missao, List<RequisicaoTransporte> requisicoes) throws Exception {
		verificarJustificativaPreenchida(missao);

		DpPessoa dpPessoa = getCadastrante();
		redirecionarSeErroAoCancelar(missao);
		missao.setCpOrgaoUsuario(getTitular().getOrgaoUsuario());
		missao.setResponsavel(dpPessoa);
		missao.setEstadoMissao(EstadoMissao.CANCELADA);
		checarCondutorPeloUsuarioAutenticado(missao);
		checarComplexo(missao.getCpComplexo().getIdComplexo());
		
		if(!missao.getRequisicoesTransporte().isEmpty()){
		    missao.setCpComplexo(missao.getRequisicoesTransporte().get(0).getCpComplexo());
		}
		missao.save();
		
		if (requisicoes != null && !requisicoes.isEmpty()) {
			for (RequisicaoTransporte requisicao : requisicoes) {
				requisicao.cancelarSemMissao(missao.getResponsavel(), missao.getSequence() + ": " + missao.getJustificativa());
			}
		}

		for (RequisicaoTransporte requisicao : missao.getRequisicoesTransporte()) {
			if (requisicoes == null || !requisicoes.contains(requisicao)) {
				gravaAndamento(dpPessoa, "MISSAO NO. " + missao.getSequence() + " CANCELADA", missao, EstadoRequisicao.NAOATENDIDA, requisicao);
			}
		}
		
		result.redirectTo(this).listarFiltrado(EstadoMissao.CANCELADA);
	}

	private void verificarJustificativaPreenchida(Missao missao) throws Exception {
		if (null == missao.getJustificativa() || missao.getJustificativa().isEmpty()) {
			validator.add(new I18nMessage("justificativa", "missoes.justificativa.validation"));
			cancelar(missao.getId());
		}
	}

	private void verificarDisponibilidadeDeVeiculo(Missao m) throws Exception {
		Boolean veiculoEstaDisponivel = Veiculo.estaDisponivel(m);
		if (!veiculoEstaDisponivel) {
			validator.add(new I18nMessage("veiculo", "missoes.veiculoEstaDisponivel.validation", m.getVeiculo().getDadosParaExibicao()));
		}

	}

	private void verificarDisponibilidadeDeCondutor(Missao m) throws Exception {
		Boolean condutorEstaDisponivel = Condutor.estaDisponivel(m);
		if (!condutorEstaDisponivel) {
			validator.add(new I18nMessage(CONDUTOR_STR, "missoes.condutorEstaDisponivel.validation", m.getCondutor().getDadosParaExibicao()));
		}
	}

	private void verificarDatasInicialFinal(Missao m) {
		Boolean dataSaidaAntesDeDataRetorno = m.getDataHoraSaida().before(m.getDataHoraRetorno());
		if (!dataSaidaAntesDeDataRetorno) {
			validator.add(new I18nMessage("dataHoraRetorno", "missoes.dataSaidaAntesDeDataRetorno.validation"));
		}
	}

	@SuppressWarnings("unchecked")
	private void redirecionarSeErroAoSalvar(Missao missao, Template template) throws Exception {
		if (validator.hasErrors()) {
			montarCombos();
			if (missao.getRequisicoesTransporte() != null) {
				List<RequisicaoTransporte> requisicoesTransporte = (List<RequisicaoTransporte>) getRequest().getAttribute(REQUISICOES_TRANSPORTE_STR);
				for (int i = 0; i < missao.getRequisicoesTransporte().size(); i++)
					if (missao.getRequisicoesTransporte().get(i) != null) {
						RequisicaoTransporte req = RequisicaoTransporte.AR.findById(missao.getRequisicoesTransporte().get(i).getId());
						missao.getRequisicoesTransporte().set(i, req);
					}

				requisicoesTransporte.removeAll(missao.getRequisicoesTransporte());
				result.include(REQUISICOES_TRANSPORTE_STR, requisicoesTransporte);
				result.include("estadoRequisicao", EstadoRequisicao.ATENDIDA);
			}

			if (missao.getDataHoraSaida() != null) {
				String dataHoraSaidaStr = new SimpleDateFormat(FormatarDataHora.recuperaFormato(PATTERN_DDMMYYYYHHMM,PATTERN_DDMMYYYYHHMM_MYSQL)).format(missao.getDataHoraSaida().getTime());
				if (!autorizacaoGI.ehAdministrador() && !autorizacaoGI.ehAdministradorMissao() && !autorizacaoGI.ehAdministradorMissaoPorComplexo()) {
					List<Condutor> condutores = new ArrayList<Condutor>();
					condutores.add(missao.getCondutor());

					List<Veiculo> veiculos = new ArrayList<Veiculo>();
					veiculos.add(missao.getVeiculo());

					result.include(CONDUTORES_STR, condutores);
					result.include(VEICULOS_STR, veiculos);
				} else {
					result.include(CONDUTORES_STR, listarCondutoresDisponiveis(missao.getId(), getTitular().getOrgaoUsuario().getId(), dataHoraSaidaStr, missao.getInicioRapido()));
					result.include(VEICULOS_STR, listarVeiculosDisponiveis(missao.getId(), getTitular().getOrgaoUsuario().getId(), dataHoraSaidaStr));
				}
			}

			renderTemplate(template, missao);
		}
	}

	private void redirecionarSeErroAoCancelar(Missao missao) throws Exception {
		if (validator.hasErrors()) {
			Template template = Template.CANCELAR;

			renderTemplate(template, missao);
		}
	}

	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/incluirInicioRapido")
	public void incluirInicioRapido(List<Long> req) throws Exception {
		redirecionarCasoRequisicaoNula(req);

		Missao missao = new Missao();
		missao.setInicioRapido(PerguntaSimNao.SIM);
		missao.setRequisicoesTransporte(new ArrayList<RequisicaoTransporte>());
		for (Long id : req)
			missao.getRequisicoesTransporte().add((RequisicaoTransporte) RequisicaoTransporte.AR.findById(id));

		removerRequisicoesDoRenderArgs(missao.getRequisicoesTransporte());
		result.forwardTo(this).inicioRapido(missao);
	}

	@RoleAdmin
    @RoleAdminMissao
    @RoleAdminMissaoComplexo
    @Path("/inicioRapido")
	public void inicioRapido(Missao missao) {
	    result.include(MISSAO_STR, missao);
	    result.include("mostrarBotoesIniciarRapido", true);
	    result.include("mostrarDadosProgramada", true);
	    result.include("mostrarDadosIniciada", true);

	    condicaoComponentesVeiculo();
	}

	private void redirecionarCasoRequisicaoNula(List<Long> req) throws Exception {
		if (req.isEmpty())
			result.redirectTo(this).incluir();
	}

	@RoleAdmin
	@RoleAgente
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/editar/{id}")
	public void editar(Long id) throws Exception {
		Missao missao = Missao.AR.findById(id);

		try {
			montarCombos();
			montarDadosSeDataHoraSaidaNotNull(missao);
			checarCondutorPeloUsuarioAutenticado(missao);
			checarComplexo(missao.getCpComplexo().getIdComplexo());
			MenuMontador.instance(result, autorizacaoGI).recuperarMenuMissao(id, missao.getEstadoMissao());
	
			result.include(MOSTRAR_BOTOES_EDITAR, true);
			result.include(MOSTRAR_DADOS_PROGRAMADA, true);
			result.include("ultimosEstados", EstadoRequisicao.valuesComboAtendimentoMissao());
	
			condicaoComponentesVeiculo();
	
			consultarEstadoDaMissao(missao);
		}
		catch (Exception ex) {
			listar(missao.getEstadoMissao(), ex.getMessage());
		}
	}

    private void montarDadosSeDataHoraSaidaNotNull(Missao missao) throws Exception {
        if(null != missao.getDataHoraSaida())
		    montarDadosParaAMissao(missao);
    }

    private void consultarEstadoDaMissao(Missao missao) {
        if("INICIADA".equals(missao.getEstadoMissao().getDescricao()))
			result.include(MOSTRAR_DADOS_INICIADA_STR, true);

		if("FINALIZADA".equals(missao.getEstadoMissao().getDescricao())) {
			result.include(MOSTRAR_DADOS_INICIADA_STR, true);
			result.include(MOSTRAR_DADOS_FINALIZADA_STR, true);
		}
    }

	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@RoleAgente
	@Path("/buscarPelaSequence")
	public void buscarPelaSequence(boolean popUp, String sequence) throws Exception {
		try {
			recuperarPelaSigla(sequence, popUp);
			result.forwardTo(this).ler();
		}
		catch (Exception ex) {
			listar(EstadoMissao.PROGRAMADA, ex.getMessage());
		}
	}

	@Path("/ler")
	public void ler() {
		/**
		 * Direciona para a view LER
		 */
	}

	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@RoleAgente
	@Path("/ler/{id}")
	public void ler(Long id) throws Exception {
		Missao missao = Missao.AR.findById(id);
		montarDadosParaAMissao(missao);
		MenuMontador.instance(result, autorizacaoGI).recuperarMenuMissao(id, missao.getEstadoMissao());
		checarCondutorPeloUsuarioAutenticado(missao);
		checarComplexo(missao.getCpComplexo().getIdComplexo());
	}

	protected void montarCombos() throws Exception {
		Calendar ultimos7dias = Calendar.getInstance();
		ultimos7dias.add(Calendar.DATE, -7);
		CpOrgaoUsuario orgaoParametro = getTitular().getOrgaoUsuario();
		EstadoRequisicao[] estados = { EstadoRequisicao.AUTORIZADA, EstadoRequisicao.PROGRAMADA, EstadoRequisicao.EMATENDIMENTO, EstadoRequisicao.NAOATENDIDA, EstadoRequisicao.ATENDIDAPARCIALMENTE };
		StringBuilder criterioBusca = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("ultimos7dias",ultimos7dias);
        parametros.put("cpOrgaoUsuario",orgaoParametro);
		criterioBusca.append("((dataHoraRetornoPrevisto is not null and dataHoraRetornoPrevisto >= :ultimos7dias) or (dataHoraRetornoPrevisto is null and dataHoraSaidaPrevista >= :ultimos7dias)) and cpOrgaoUsuario = :cpOrgaoUsuario");
		requisicaoController.recuperarRequisicoes(criterioBusca, parametros, estados);
	}

	protected void montarComboCondutor() throws Exception {
		List<Condutor> condutores = autorizacaoGI.ehAdministradorMissaoPorComplexo() ? Condutor.listarEscaladosDoComplexo(true, autorizacaoGI.recuperarComplexoAdministrador(), getTitular().getOrgaoUsuario()) : Condutor.listarEscalados(true, getTitular().getOrgaoUsuario());
		result.include("condutoresEscalados", condutores);
	}
	
    @Transactional
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/excluir/{id}")
	public void excluir(Long id) throws Exception {
		Missao missao = Missao.AR.findById(id);
		checarCondutorPeloUsuarioAutenticado(missao);
		checarComplexo(missao.getCpComplexo().getIdComplexo());
		missao.delete();
		result.redirectTo(this).listar();
	}

	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@RoleAgente
	@Path("/listarVeiculosECondutoresDisponiveis")
	public void listarVeiculosECondutoresDisponiveis(Long idMissao, String veiculosDisp, PerguntaSimNao inicioRapido, String dataSaida) throws Exception {
		Missao missao = Missao.AR.findById(idMissao);
		List<Veiculo> veiculosDisponiveis = null;

		if ("".equals(veiculosDisp) || null == veiculosDisp)
			veiculosDisponiveis = listarVeiculosDisponiveis(idMissao, getTitular().getOrgaoUsuario().getId(), dataSaida);
		else
			veiculosDisponiveis = listarVeiculosDisponiveis(veiculosDisp);

		List<Condutor> condutoresDisponiveis = listarCondutoresDisponiveis(idMissao, getTitular().getOrgaoUsuario().getId(), dataSaida, inicioRapido);

		ObjectMapper oM = new ObjectMapper();
		ObjectWriter oW = oM.writer().withDefaultPrettyPrinter();

		if(condutoresDisponiveis.isEmpty()) {
		    String data = oW.writeValueAsString(MessagesBundle.getMessage("missao.inicia.sem.condutor.disponivel"));
		    result.use(Results.http()).body(data);
		    return;
		}

        MissaoVO missaoVO = new MissaoVO();

        for (Veiculo veiculo : veiculosDisponiveis) {
            boolean selected = missao != null && veiculo.equals(missao.getVeiculo());
            missaoVO.veiculos.add(new ItemVO(veiculo.getId(), veiculo.getDadosParaExibicao(), selected));
        }

        for (Condutor condutor : condutoresDisponiveis) {
            boolean selected = missao != null && condutor.equals(missao.getCondutor());
            missaoVO.condutores.add(new ItemVO(condutor.getId(), condutor.getDadosParaExibicao(), selected));
        }

        missaoVO.disabled = autorizacaoGI.ehAgente() && !autorizacaoGI.ehAdministrador() && !autorizacaoGI.ehAdministradorMissao();

        String data = oW.writeValueAsString(missaoVO);

		result.use(Results.http()).body(data);
	}

	@SuppressWarnings("unchecked")
    protected void buscarPorCondutoreseEscala(EscalaDeTrabalho escala) {
		SimpleDateFormat formatar = new SimpleDateFormat(FormatarDataHora.recuperaFormato(PATTERN_DDMMYYYYHHMM,PATTERN_DDMMYYYYHHMM_MYSQL));
		String dataFormatadaFimOracle;
		if (escala.getDataVigenciaFim() == null) {
			dataFormatadaFimOracle = FormatarDataHora.recuperaDataFim();
		} else {
			String dataFim = formatar.format(escala.getDataVigenciaFim().getTime());
			dataFormatadaFimOracle = dataFim;
		}

		String dataInicio = formatar.format(escala.getDataVigenciaInicio().getTime());
		String dataFormatadaInicioOracle = dataInicio;
		List<Missao> missoes = null;

		String qrl = 	"SELECT p FROM Missao p" +
		" WHERE  p.condutor.id = " + escala.getCondutor().getId() +
		" AND    p.estadoMissao NOT IN ('" + EstadoMissao.CANCELADA + "','" + EstadoMissao.FINALIZADA + "')" +
		" AND   (p.dataHoraSaida >= '" + dataFormatadaInicioOracle +
		"' AND    p.dataHoraSaida <= '" + dataFormatadaFimOracle + "')";

		Query qry = Missao.AR.em().createQuery(qrl);
		missoes = (List<Missao>) qry.getResultList();

		result.include(MISSOESPORCONDUTOREESCALA_STR, missoes);
	}

	protected void checarCategoriaCNHVeiculoCondutor(Missao missao) throws Exception {
		CategoriaCNH categoriaCondutor1 = null;
		CategoriaCNH categoriaCondutor2 = null;

		if (missao.getCondutor().getCategoriaCNH().getDescricao().length() == 2) {
			switch (missao.getCondutor().getCategoriaCNH().getDescricao()) {
			case "AB":
				categoriaCondutor1 = CategoriaCNH.A;
				categoriaCondutor2 = CategoriaCNH.B;
				break;
			case "AC":
				categoriaCondutor1 = CategoriaCNH.A;
				categoriaCondutor2 = CategoriaCNH.C;
				break;
			case "AD":
				categoriaCondutor1 = CategoriaCNH.A;
				categoriaCondutor2 = CategoriaCNH.D;
				break;
			case "AE":
				categoriaCondutor1 = CategoriaCNH.A;
				categoriaCondutor2 = CategoriaCNH.E;
				break;
			default:
				break;
			}

			if (categoriaCondutor1 != null && categoriaCondutor2 != null && (categoriaCondutor1.compareTo(missao.getVeiculo().getCategoriaCNH()) >= 0 || categoriaCondutor2.compareTo(missao.getVeiculo().getCategoriaCNH()) >= 0))
				return;
		} else {
			if (missao.getCondutor().getCategoriaCNH().compareTo(missao.getVeiculo().getCategoriaCNH()) >= 0) {
				return;
			}
		}

		validator.add(new I18nMessage("categoriaCnhCondutor", "missao.categoriaCNHCondutorErradaParaVeiculo.validation"));
	}

	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/incluirComRequisicoes")
	public void incluirComRequisicoes(List<Long> req) throws Exception {
	    montarCombos();
		redirecionarCasoRequisicaoNula(req);

		Missao missao = new Missao();
		missao.setInicioRapido(PerguntaSimNao.NAO);
		missao.setRequisicoesTransporte(new ArrayList<RequisicaoTransporte>());

		for (Long id : req)
			missao.getRequisicoesTransporte().add((RequisicaoTransporte) RequisicaoTransporte.AR.findById(id));

		removerRequisicoesDoRenderArgs(missao.getRequisicoesTransporte());
		
		result.include(PROGRAMAR_RAPIDO, true);
		result.include(MISSAO_STR, missao);
		result.include(MOSTRAR_BOTOES_EDITAR, true);
		result.use(Results.page()).of(MissaoController.class).incluir();
	}

	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/incluir")
	public void incluir() throws Exception {
		Missao missao = getMissao();
		missao.setInicioRapido(PerguntaSimNao.NAO);
		MenuMontador.instance(result, autorizacaoGI).recuperarMenuMissao(missao.getId(), missao.getEstadoMissao());

		montarCombos();

		result.include(MISSAO_STR, missao);
		result.include(MOSTRAR_BOTOES_EDITAR, true);
	}

    private Missao getMissao() {
        return null != getRequest().getAttribute(MISSAO_STR) ? (Missao) getRequest().getAttribute(MISSAO_STR) : new Missao();
    }

	private void removerRequisicoesDoRenderArgs(List<RequisicaoTransporte> requisicoesTransporte) {
		@SuppressWarnings("unchecked")
		List<RequisicaoTransporte> requisicoes = (List<RequisicaoTransporte>) getRequest().getAttribute(REQUISICOES_TRANSPORTE_STR);

		if (requisicoes != null)
			requisicoes.removeAll(requisicoesTransporte);

	}

	protected void recuperarPelaSigla(String sequence, Boolean popUp) throws Exception {
		Missao missao = Missao.buscar(sequence);
		montarDadosParaAMissao(missao);
		MenuMontador.instance(result, autorizacaoGI).recuperarMenuMissao(missao.getId(), missao.getEstadoMissao());

		if (popUp != null)
			result.include("mostrarMenu", !popUp);
		else
			result.include("mostrarMenu", true);

		checarCondutorPeloUsuarioAutenticado(missao);
		checarComplexo(missao.getCpComplexo().getIdComplexo());
	}

	protected void montarDadosParaAMissao(Missao missao) throws Exception {
		removerRequisicoesDoRenderArgs(missao.getRequisicoesTransporte());
		SimpleDateFormat formatar = new SimpleDateFormat(FormatarDataHora.recuperaFormato(PATTERN_DDMMYYYYHHMM,PATTERN_DDMMYYYYHHMM_MYSQL));
		String dataHoraSaidaStr = formatar.format(missao.getDataHoraSaida().getTime());
		List<Condutor> condutores = listarCondutoresDisponiveis(missao.getId(), getTitular().getOrgaoUsuario().getId(), dataHoraSaidaStr, missao.getInicioRapido());
		boolean encontrouCondutor = false;

		if (condutores != null && !condutores.isEmpty())
			for (Condutor condutor : condutores) {
				if (condutor.getId().equals(missao.getCondutor().getId())) {
					encontrouCondutor = true;
					break;
				}
			}
		else
			condutores = new ArrayList<Condutor>();

		if (!encontrouCondutor)
			condutores.add(missao.getCondutor());

		StringBuilder veiculosDisp = new StringBuilder();
		for (RequisicaoTransporte req : missao.getRequisicoesTransporte())
			if (req.getServicoVeiculo() != null) {
				req = RequisicaoTransporte.AR.findById(req.getId());
				veiculosDisp.append(req.getServicoVeiculo().getVeiculo().getId()).append(", ");
			}

		List<Veiculo> veiculos = new ArrayList<Veiculo>();

		if ("".equals(veiculosDisp.toString()))
			veiculos = listarVeiculosDisponiveis(missao.getId(), getTitular().getOrgaoUsuario().getId(), dataHoraSaidaStr);
		else
			veiculos = listarVeiculosDisponiveis(veiculosDisp.toString());

		boolean encontrouVeiculo = false;

		if (veiculos != null && !veiculos.isEmpty())
			for (Veiculo veiculo : veiculos) {
				if (veiculo.getId().equals(missao.getVeiculo().getId())) {
					encontrouVeiculo = true;
					break;
				}
			}
		else
			veiculos = new ArrayList<Veiculo>();

		if (!encontrouVeiculo)
			veiculos.add(missao.getVeiculo());

		result.include(CONDUTORES_STR, condutores);
		result.include(VEICULOS_STR, veiculos);
		result.include(MISSAO_STR, missao);

		EstadoRequisicao estadoRequisicao = EstadoRequisicao.ATENDIDA;
		EstadoMissao estadoMissao = EstadoMissao.PROGRAMADA;

		EstadoRequisicao[] estados = new EstadoRequisicao[missao.getRequisicoesTransporte().size()];
		for (int i = 0; i < estados.length; i++)
			estados[i] = EstadoRequisicao.ATENDIDA;

		result.include("estados", estados);
		result.include("estadoRequisicao", estadoRequisicao);
		result.include(ESTADO_MISSAO_STR, estadoMissao);
	}

	protected void checarCondutorPeloUsuarioAutenticado(Missao missao) throws Exception {
		if (autorizacaoGI.ehAgente() && !autorizacaoGI.ehAdministrador() && !autorizacaoGI.ehAdministradorMissao()) {
			if (missao.getId() == 0)
				throw new Exception(MessagesBundle.getMessage(MISSOES_STR, "missoes.autorizacaoGI.ehAgente.exception"));

			if (!getTitular().equivale(missao.getCondutor().getDpPessoa()))
				throw new Exception(MessagesBundle.getMessage(MISSOES_STR, MISSOES_AUTORIZACAO_GI_SEM_ACESSO_EXCEPTION));
		}
	}

	private List<Veiculo> listarVeiculosDisponiveis(String veiculosDisp) throws Exception {
		List<Veiculo> veiculos = new ArrayList<Veiculo>();
		List<String> itens = Arrays.asList(veiculosDisp.substring(0, veiculosDisp.length() - 2).split("\\s*,\\s*"));

		if (!itens.isEmpty())
			for (String item : itens) {
				Veiculo itemVeiculo = new Veiculo();
				itemVeiculo = Veiculo.AR.findById(Long.parseLong(item));

				if (!veiculos.contains(itemVeiculo))
					veiculos.add(itemVeiculo);
			}

		return veiculos;
	}

	private List<Veiculo> listarVeiculosDisponiveis(Long idMissao, Long idOrgao, String dataSaida) {
		return Veiculo.listarDisponiveis(dataSaida, idMissao, idOrgao);
	}

	private List<Condutor> listarCondutoresDisponiveis(Long idMissao, Long idOrgao, String dataSaida, PerguntaSimNao inicioRapido) throws Exception {
		return Condutor.listarDisponiveis(dataSaida, idMissao, idOrgao, inicioRapido);
	}

	private void checarComplexo(Long idComplexo) throws Exception {
		if (autorizacaoGI.ehAdministradorMissaoPorComplexo()) {
			if (!autorizacaoGI.getComplexoAdministrador().getIdComplexo().equals(idComplexo)) {
				throw new Exception(MessagesBundle.getMessage(MISSOES_STR, MISSOES_AUTORIZACAO_GI_SEM_ACESSO_EXCEPTION));
			} else if (autorizacaoGI.ehAprovador() && !autorizacaoGI.getComplexoPadrao().getIdComplexo().equals(idComplexo)) {
				throw new Exception(MessagesBundle.getMessage(MISSOES_STR, MISSOES_AUTORIZACAO_GI_SEM_ACESSO_EXCEPTION));
			}
		}
	}

	private void renderTemplate(Template template, Missao missao) throws Exception {
	    result.include("missao", missao);

		switch(template) {
			case EDITAR:
			    validator.onErrorUse(Results.logic()).forwardTo(MissaoController.class).editar(missao.getId());
				break;

			case INCLUIR:
			    result.include(MOSTRAR_BOTOES_EDITAR, true);
			    validator.onErrorUse(Results.page()).of(MissaoController.class).incluir();
				break;
			case INICIORAPIDO:
			    validator.onErrorUse(Results.logic()).forwardTo(MissaoController.class).inicioRapido(missao);
				break;

			case CANCELAR:
			    validator.onErrorUse(Results.page()).of(MissaoController.class).cancelar(missao.getId());
				break;

			case FINALIZAR:
                validator.onErrorUse(Results.logic()).forwardTo(MissaoController.class).finalizar(missao.getId());
                break;

			case INICIAR:
			    condicaoComponentesVeiculo();
			    condicaoComponentesIniciarMissao(missao);
                validator.onErrorUse(Results.page()).of(MissaoController.class).iniciar(missao.getId());
                break;

			default:
				break;
		}
	}

	/* Metodo AJAX */
	@Path("/getMissaoLinhasTotal")
	public void getMissaoLinhasTotal() {
		result.use(Results.http()).body(Parametro.buscarConfigSistemaEmVigor("missao.linhas.total"));
    }
	
	@Path("/listarMissoesPendentesPorCondutor/{idCondutor}")
	public void listarMissoesPendentesPorCondutor(Long idCondutor) throws Exception {
		List<Missao> missoes = Missao.buscarMissoesProgramadasPorCondutor(idCondutor);
		EstadoMissao estMis = EstadoMissao.CANCELADA;
		MenuMontador.instance(result).recuperarMenuMissoes(null);
		EstadoMissao estadoMissao = EstadoMissao.PROGRAMADA;
		montarComboCondutor();

		result.include("estMis", estMis);
		result.include(MISSOES_STR, missoes);
		result.include(ESTADO_MISSAO_STR, estadoMissao);
		result.include(CONDUTOR_STR, new Condutor());
		result.use(Results.page()).of(MissaoController.class).listar();
	}
}