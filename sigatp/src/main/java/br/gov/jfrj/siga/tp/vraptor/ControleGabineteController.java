package br.gov.jfrj.siga.tp.vraptor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.tp.auth.AutorizacaoGI;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminGabinete;
import br.gov.jfrj.siga.tp.auth.annotation.RoleGabinete;
import br.gov.jfrj.siga.tp.exceptions.ControleGabineteControllerException;
import br.gov.jfrj.siga.tp.model.Condutor;
import br.gov.jfrj.siga.tp.model.ControleGabinete;
import br.gov.jfrj.siga.tp.model.ItemMenu;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.model.Veiculo;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/controleGabinete")
public class ControleGabineteController extends TpController {

    private static final String CONTROLES_GABINETE = "controlesGabinete";
    private static final String CONTROLE_GABINETE = "controleGabinete";
    private static final String VEICULOS = "veiculos";
    private static final String CONDUTORES = "condutores";
    @Inject
    private AutorizacaoGI autorizacaoGI;

	/**
	 * @deprecated CDI eyes only
	 */
	public ControleGabineteController() {
		super();
	}
	
	@Inject
    public ControleGabineteController(HttpServletRequest request, Result result,   Validator validator, SigaObjects so,   EntityManager em) {
        super(request, result, TpDao.getInstance(), validator, so, em);
    }

    @RoleGabinete
    @RoleAdminGabinete
    @Path("/listar")
    public void listar() {
        List<ControleGabinete> controlesGabinete;
        if (autorizacaoGI.ehAdminGabinete())
            controlesGabinete = ControleGabinete.listarTodos();
        else
            controlesGabinete = ControleGabinete.listarPorCondutor(Condutor.recuperarLogado(getTitular(), getTitular().getOrgaoUsuario()));

        result.include(CONTROLES_GABINETE, controlesGabinete);
    }

    // Verificar se o MenuMontador e realmente utilizado
    @RoleGabinete
    @RoleAdminGabinete
    @Path("/listarPorVeiculo/{idVeiculo}")
    public void listarPorVeiculo(Long idVeiculo) throws ControleGabineteControllerException {
        try {
            Veiculo veiculo = Veiculo.AR.findById(idVeiculo);
            List<ControleGabinete> controlesGabinete = ControleGabinete.buscarTodosPorVeiculo(veiculo);
            MenuMontador.instance(result).recuperarMenuVeiculos(idVeiculo, ItemMenu.DADOSCADASTRAIS);

            result.include(CONTROLES_GABINETE, controlesGabinete);
            result.include("veiculo", veiculo);
        } catch (Exception e) {
            throw new ControleGabineteControllerException(e);
        }
    }

    @RoleGabinete
    @RoleAdminGabinete
    @Path("/incluir")
    public void incluir() throws ControleGabineteControllerException {
        List<Veiculo> veiculos = recuperarListaDeVeiculos();
        List<Condutor> condutores = recuperarListaDeCondutores();

        ControleGabinete controleGabinete = new ControleGabinete();
        result.include(CONTROLE_GABINETE, controleGabinete);
        result.include(VEICULOS, veiculos);
        result.include(CONDUTORES, condutores);
    }

    private List<Veiculo> recuperarListaDeVeiculos() throws ControleGabineteControllerException {
        try {
            return Veiculo.listarFiltradoPor(getTitular().getOrgaoUsuario(), getTitular().getLotacao());
        } catch (Exception e) {
            throw new ControleGabineteControllerException(e);
        }
    }

    private List<Condutor> recuperarListaDeCondutores() throws ControleGabineteControllerException {
        try {
            List<Condutor> condutores;
            if (autorizacaoGI.ehAdminGabinete())
                condutores = Condutor.listarFiltradoPor(getTitular().getOrgaoUsuario(), getTitular().getLotacao());
            else {
                condutores = new ArrayList<Condutor>();
                condutores.add(Condutor.recuperarLogado(getTitular(), getTitular().getOrgaoUsuario()));
            }
            return condutores;
        } catch (Exception e) {
            throw new ControleGabineteControllerException(e);
        }
    }

    @RoleGabinete
    @RoleAdminGabinete
    @Path("/editar/{id}")
    public void editar(Long id) throws ControleGabineteControllerException {
        try {
            ControleGabinete controleGabinete = ControleGabinete.AR.findById(id);
            verificarAcesso(controleGabinete);
            List<Veiculo> veiculos = recuperarListaDeVeiculos();
            List<Condutor> condutores = recuperarListaDeCondutores();

            result.include(CONTROLE_GABINETE, controleGabinete);
            result.include(VEICULOS, veiculos);
            result.include(CONDUTORES, condutores);
        } catch (Exception e) {
            throw new ControleGabineteControllerException(e);
        }
    }

    private void verificarAcesso(ControleGabinete controleGabinete) throws ControleGabineteControllerException {
        if (!(autorizacaoGI.ehAdminGabinete() && getTitular().getLotacao().equivale(controleGabinete.getTitular().getLotacao())) && !controleGabinete.getTitular().equivale(getTitular()))
            throw new ControleGabineteControllerException(MessagesBundle.getMessage("controlesGabinete.verificarAcesso.exception"));
    }

    private void verificarOdometrosSaidaRetorno(ControleGabinete controleGabinete) {
        if (controleGabinete.getOdometroEmKmSaida() > controleGabinete.getOdometroEmKmRetorno())
            validator.add(new I18nMessage("odometroEmKmRetorno", "controlesGabinete.odometroEmKmRetorno.validation"));
    }

    private void verificarDatasInicialFinal(ControleGabinete controleGabinete) {
        if (controleGabinete.getDataHoraSaida() == null || controleGabinete.getDataHoraRetorno() == null) {
            validator.add(new I18nMessage("dataHoraSaida", "controlesGabinete.dataHoraSaida.validation"));
            return;
        }

        Boolean dataSaidaAntesDeDataRetorno = controleGabinete.getDataHoraSaida().before(controleGabinete.getDataHoraRetorno());
        if (!dataSaidaAntesDeDataRetorno)
            validator.add(new I18nMessage("dataHoraRetorno", "controlesGabinete.dataSaidaAntesDeDataRetorno.validation"));
    }

    private void verificarOdometroRetornoControleAnterior(ControleGabinete controleGabinete) {
        double ultimoOdometroDesteVeiculo = ControleGabinete.buscarUltimoOdometroPorVeiculo(controleGabinete.getVeiculo(), controleGabinete);
        if (controleGabinete.getOdometroEmKmSaida() < ultimoOdometroDesteVeiculo)
            validator.add(new I18nMessage("odometroEmKmSaida", "controlesGabinete.odometroEmKmSaida.validation"));
    }

    @Transactional
    @RoleGabinete
    @RoleAdminGabinete
    public void salvar(@Valid ControleGabinete controleGabinete) throws ControleGabineteControllerException {
        if (!controleGabinete.getId().equals(0L))
            verificarAcesso(controleGabinete);

        verificarOdometroRetornoControleAnterior(controleGabinete);
        verificarOdometrosSaidaRetorno(controleGabinete);
        verificarDatasInicialFinal(controleGabinete);

        if (validator.hasErrors()) {
            List<Veiculo> veiculos = recuperarListaDeVeiculos();
            List<Condutor> condutores = recuperarListaDeCondutores();

            result.include(CONTROLE_GABINETE, controleGabinete);
            result.include(VEICULOS, veiculos);
            result.include(CONDUTORES, condutores);

            validator.onErrorUse(Results.page()).of(ControleGabineteController.class).editar(controleGabinete.getId());
        } else {
            if (controleGabinete.getId() == 0)
                controleGabinete.setDataHora(Calendar.getInstance());

            controleGabinete.setSolicitante(getCadastrante());
            controleGabinete.setTitular(getTitular());

            controleGabinete.save();
            result.redirectTo(ControleGabineteController.class).listar();
        }
    }

    @Transactional
    @RoleGabinete
    @RoleAdminGabinete
    @Path("/excluir/{id}")
    public void excluir(Long id) throws ControleGabineteControllerException {
        try {
            ControleGabinete controleGabinete = ControleGabinete.AR.findById(id);
            verificarAcesso(controleGabinete);
            controleGabinete.delete();

            result.redirectTo(ControleGabineteController.class).listar();
        } catch (Exception e) {
            throw new ControleGabineteControllerException(e);
        }
    }
}
