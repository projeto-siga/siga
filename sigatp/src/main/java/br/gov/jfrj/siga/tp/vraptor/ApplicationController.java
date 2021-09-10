package br.gov.jfrj.siga.tp.vraptor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.tp.auth.AutorizacaoGI;
import br.gov.jfrj.siga.tp.exceptions.ApplicationControllerException;
import br.gov.jfrj.siga.tp.model.Condutor;
import br.gov.jfrj.siga.tp.model.EstadoMissao;
import br.gov.jfrj.siga.tp.model.EstadoRequisicao;
import br.gov.jfrj.siga.tp.model.EstadoServico;
import br.gov.jfrj.siga.tp.model.Missao;
import br.gov.jfrj.siga.tp.model.Parametro;
import br.gov.jfrj.siga.tp.model.RequisicaoTransporte;
import br.gov.jfrj.siga.tp.model.ServicoVeiculo;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.util.CondutorFiltro;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import javax.transaction.Transactional;

@Controller
@Path("/app/application")
@SuppressWarnings("unused")
public class ApplicationController extends TpController {

    private final class PredicateImplementation<T> implements Predicate<T> {
        private final String descricao;

        private PredicateImplementation(String descricao) {
            this.descricao = descricao;
        }

        @Override
        public boolean apply(T objeto) {
            return applyMissao(objeto) || applyRequisicaoTransporte(objeto, totalDiasARecuperar) || applyServico(objeto);
        }

        private boolean applyServico(T obj) {
            if (obj instanceof ServicoVeiculo) {
                ServicoVeiculo servico = (ServicoVeiculo) obj;
                return servico.getSituacaoServico().getDescricao().equals(descricao);
            } else {
                return false;
            }
        }

        private boolean applyRequisicaoTransporte(T obj, int totalDias) {
            if (obj instanceof RequisicaoTransporte) {
                RequisicaoTransporte requisicao = (RequisicaoTransporte) obj;
                Calendar ultimosdias = Calendar.getInstance();
                ultimosdias.add(Calendar.DATE, -totalDias);
                if ("".equals(descricao)) {
                    return requisicao.getDataHoraSaidaPrevista().after(ultimosdias) && requisicao.getCpOrgaoUsuario().getIdOrgaoUsu().equals(getTitular().getOrgaoUsuario().getIdOrgaoUsu());
                } else {
                    return requisicao.getUltimoEstado().getDescricao().equals(descricao) && requisicao.getDataHoraSaidaPrevista().after(ultimosdias)
                            && requisicao.getCpOrgaoUsuario().getIdOrgaoUsu().equals(getTitular().getOrgaoUsuario().getIdOrgaoUsu());
                }
            } else {
                return false;
            }
        }
        
        private boolean applyMissao(T obj) {
            if (obj instanceof Missao) {
                Missao missao = (Missao) obj;
                return missao.getEstadoMissao().getDescricao().equals(descricao);
            } else {
                return false;
            }
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);

    private AutorizacaoGI autorizacaoGI;
	protected int totalDiasARecuperar;

	/**
	 * @deprecated CDI eyes only
	 */
	public ApplicationController() {
		super();
	}
	
	@Inject	
	public ApplicationController(HttpServletRequest request, Result result,  Validator validator, SigaObjects so,  EntityManager em, AutorizacaoGI autorizacaoGI) {
        super(request, result, TpDao.getInstance(), validator, so, em);
        this.autorizacaoGI = autorizacaoGI;
        this.totalDiasARecuperar = retornaDias();
    }
    
    private int retornaDias() {
    	return Integer.parseInt(Parametro.buscarValorEmVigor("total.dias.pesquisa", getTitular(), autorizacaoGI.getComplexoPadrao()));
    }

    @Path("/index")
    public void index() throws ApplicationControllerException {
        try {
            if (autorizacaoGI.ehAdministrador() || autorizacaoGI.ehAdministradorMissao() || autorizacaoGI.ehAdministradorMissaoPorComplexo()) {
                result.forwardTo(RequisicaoController.class).listarFiltrado("AUTORIZADA", "NAOATENDIDA");
            } else if (autorizacaoGI.ehAgente()) {
                result.forwardTo(RequisicaoController.class).listarFiltrado("AUTORIZADA", "NAOATENDIDA");
            } else if (autorizacaoGI.ehAprovador()) {
                result.forwardTo(RequisicaoController.class).listarPAprovar();
            } else {
                result.forwardTo(RequisicaoController.class).listar();
            }
        } catch (Exception e) {
            throw new ApplicationControllerException(e);
        }
    }

    @Path("/selecionarPessoa")
    public void selecionarPessoa() {
        CondutorFiltro filtro = new CondutorFiltro();
        filtro.condutorFiltro = new DpPessoa();
        filtro.lotaCondutorFiltro = new DpLotacao();
        result.include("filtro", filtro);
    }

    @Path({ "/selecionarPessoa/{sigla}/{tipo}/{nome}", "/selecionarPessoa" })
    public void selecionarSiga(String sigla, String tipo, String nome) throws ApplicationControllerException {
        try {
            result.redirectTo("/siga/app/" + tipo + "/selecionar?" + "propriedade=" + tipo + nome + "&sigla=" + URLEncoder.encode(sigla, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new ApplicationControllerException(e);
        }
    }

    @Path({ "/buscarSiga/{sigla}/{tipo}/{nome}", "/buscarSiga" })
    public void buscarSiga(String sigla, String tipo, String nome) throws ApplicationControllerException {
        try {
            result.redirectTo("/siga/app/" + tipo + "/buscar?" + "propriedade=" + tipo + nome + "&sigla=" + URLEncoder.encode(sigla, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new ApplicationControllerException(e);
        }
    }

    @Path("/exibirManualUsuario")
    public void exibirManualUsuario() {
        /**
         * Metodo utilizado somente para o VRaptor renderizar a view em questao.
         */
    }

    @Path("/exibirManualUsuarioDeGabinete")
    public void exibirManualUsuarioDeGabinete() {
        /**
         * Metodo utilizado somente para o VRaptor renderizar a view em questao.
         */
    }

    @Path("/gadget")
    public void gadget() {
        try {
            Long idOrgaoUsu = Long.valueOf(getTitular().getOrgaoUsuario().getIdOrgaoUsu());
            ArrayList<String[]> lista = new ArrayList<String[]>();
            
            if (equalsSearch(true, autorizacaoGI.ehAdministrador(), autorizacaoGI.ehAdministradorMissao(), autorizacaoGI.ehAdministradorMissaoPorComplexo())) {
                adicionarRequisicoesAdministrador(lista);
            }

            else if (equalsSearch(true, autorizacaoGI.ehAgente())) {
                adicionarMissoesAgente(idOrgaoUsu, lista);
            }

            else if (equalsSearch(true, autorizacaoGI.ehAprovador())) {
                adicionarRequisicoesAprovador(lista);
            }

            else if (equalsSearch(true, autorizacaoGI.ehAdministradorFrota())) {
                adicionarServicosAdministradorFrota(idOrgaoUsu, lista);
            }

            else {
                adicionarRequisicoes(lista);
            }

            result.include("lista", lista);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    private void adicionarRequisicoes(List<String[]> lista) throws ApplicationControllerException {
        List<RequisicaoTransporte> requisicoes;
        try {
            requisicoes = RequisicaoTransporte.listarParaAgendamento(getTitular());
            Integer total = totalizarItemLista(requisicoes, "");
            if (!requisicoes.isEmpty()) {
                lista.add(adicionarItemLista("/sigatp/app/requisicao/listar", "Requisi&ccedil;&otilde;es", total));
            }
        } catch (Exception e) {
            throw new ApplicationControllerException(e);
        }
    }

    private void adicionarServicosAdministradorFrota(Long idOrgaoUsu, List<String[]> lista) {
        EstadoServico[] estados = { EstadoServico.AGENDADO, EstadoServico.INICIADO };
		Map<String, Object> parametros = new HashMap<String,Object>();
		parametros.put("idOrgaoUsu",idOrgaoUsu);
		parametros.put("estado1",estados[0]);
		parametros.put("estado2",estados[1]);
        String query = "cpOrgaoUsuario.idOrgaoUsu=:idOrgaoUsu and (situacaoServico = :estado1 or situacaoServico = :estado2)";
        List<ServicoVeiculo> servicos = ServicoVeiculo.AR.find(query, parametros).fetch();

        for (EstadoServico item : estados) {
            String titulo = "Servi&ccedil;os " + (item.equals(EstadoServico.AGENDADO) ? "agendados" : "iniciados");
            Integer total = totalizarItemLista(servicos, item.getDescricao());
            if (total > 0) {
                lista.add(adicionarItemLista("/sigatp/app/servicoVeiculo/listarFiltrado/" + item.getDescricao(), titulo, total));
            }
        }
    }

    private void adicionarRequisicoesAprovador(List<String[]> lista) {
        EstadoRequisicao estado = EstadoRequisicao.ABERTA;
        List<RequisicaoTransporte> requisicoes = RequisicaoTransporte.listar(estado);
        if (!requisicoes.isEmpty()) {
            lista.add(adicionarItemLista("/sigatp/app/requisicao/listarFiltrado/" + estado.getDescricao(), "Requisi&ccedil;&otilde;es a autorizar", requisicoes.size()));
        }
    }

    private void adicionarMissoesAgente(Long idOrgaoUsu, List<String[]> lista) {
        String titulo;
        int total;
        Long idCondutor = Condutor.recuperarLogado(getTitular(), getTitular().getOrgaoUsuario()).getId();
        EstadoMissao[] estados = { EstadoMissao.PROGRAMADA, EstadoMissao.INICIADA };
		Map<String, Object> parametros = new HashMap<String,Object>();
		parametros.put("idOrgaoUsu",idOrgaoUsu);
		parametros.put("idCondutor",idCondutor);
		parametros.put("estado1",estados[0]);
		parametros.put("estado2",estados[1]);
        String query = "condutor.id = :idCondutor and cpOrgaoUsuario.idOrgaoUsu = :idOrgaoUsu and (estadoMissao = :estado1 or estadoMissao = :estado2)";
        List<Missao> missoes = Missao.AR.find(query, parametros).fetch();

        for (EstadoMissao item : estados) {
            titulo = "Miss&otilde;es " + (item.equals(EstadoMissao.PROGRAMADA) ? "programadas" : "iniciadas");
            total = totalizarItemLista(missoes, item.getDescricao());
            if (total > 0) {
                lista.add(adicionarItemLista("/sigatp/app/missao/listarFiltrado/" + item.getDescricao(), titulo, total));
            }
        }
    }
    
    private void adicionarRequisicoesAdministrador(List<String[]> lista) {
        String titulo = "";
        int total;
        EstadoRequisicao[] estados = { EstadoRequisicao.AUTORIZADA, EstadoRequisicao.NAOATENDIDA };
        List<RequisicaoTransporte> requisicoes = RequisicaoTransporte.listar(estados);

        for (EstadoRequisicao item : estados) {
            titulo = "Requisi&ccedil;&otilde;es " + (item.equals(EstadoRequisicao.AUTORIZADA) ? "autorizadas" : "n&atilde;o atendidas");
            total = totalizarItemLista(requisicoes, item.getDescricao());
            if (total > 0) {
                lista.add(adicionarItemLista("/sigatp/app/requisicao/listarFiltrado/" + item.getDescricao(), titulo, total));
            }
        }
    }

    private String[] adicionarItemLista(String url, String titulo, int total) {
        String[][] itemLista = new String[1][3];
        itemLista[0][0] = url;
        itemLista[0][1] = titulo;
        itemLista[0][2] = String.valueOf(total);
        return itemLista[0];
    }

	private Boolean equalsSearch(Object item, Object... search) {
		List<Object> lista = new ArrayList<Object>(Arrays.asList(search));
		return lista.contains(item);
    }

    private <T> Integer totalizarItemLista(List<T> lista, String itemDescricao) {
        final String descricao = itemDescricao;
        int total = 0;

        try {
            if (!lista.isEmpty()) {
                List<T> itemFiltrado = Lists.newArrayList(Iterables.filter(lista, new PredicateImplementation<T>(descricao)));
                total = itemFiltrado.size();
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return total;
    }
}