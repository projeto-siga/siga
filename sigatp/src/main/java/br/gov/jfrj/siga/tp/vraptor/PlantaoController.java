package br.gov.jfrj.siga.tp.vraptor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.SimpleMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdmin;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissao;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissaoComplexo;
import br.gov.jfrj.siga.tp.model.Afastamento;
import br.gov.jfrj.siga.tp.model.Condutor;
import br.gov.jfrj.siga.tp.model.ItemMenu;
import br.gov.jfrj.siga.tp.model.Missao;
import br.gov.jfrj.siga.tp.model.Plantao;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.util.FormatarDataHora;
import br.gov.jfrj.siga.tp.util.FormatarTextoHtml;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/plantao")
public class PlantaoController extends TpController {

    private static final String CONDUTOR_ID = "condutor.id";
    private static final String PLANTAO = "plantao";

	/**
	 * @deprecated CDI eyes only
	 */
	public PlantaoController() {
		super();
	}
	
	@Inject
    public PlantaoController(HttpServletRequest request, Result result,  Validator validator, SigaObjects so,  EntityManager em) {
        super(request, result, TpDao.getInstance(), validator, so, em);
    }

    @Path("/listarPorCondutor/{idCondutor}")
    public void listarPorCondutor(Long idCondutor) throws Exception {
        Condutor condutor = buscaCondutor(idCondutor);
        List<Plantao> plantoes = Plantao.buscarTodosPorCondutor(condutor);

        MenuMontador.instance(result).recuperarMenuCondutores(idCondutor, ItemMenu.PLANTOES);

        result.include("plantoes", plantoes);
        result.include("condutor", condutor);
    }

    @RoleAdmin
    @RoleAdminMissao
    @RoleAdminMissaoComplexo
    @Path("/editar/{idCondutor}/{id}")
    public void editar(Long idCondutor, Long id) throws Exception {
        Plantao plantao;
        if (id > 0)
            plantao = Plantao.AR.findById(id);
        else {
            plantao = new Plantao();
            plantao.setCondutor(buscaCondutor(idCondutor));
        }

        result.include(PLANTAO, plantao);
        result.include("idCond", idCondutor);
    }

    @RoleAdmin
    @RoleAdminMissao
    @RoleAdminMissaoComplexo
    @Path("/incluir/{idCondutor}")
    public void incluir(Long idCondutor) throws Exception {
        result.forwardTo(PlantaoController.class).editar(idCondutor, 0L);
    }

    @Transactional
    @RoleAdmin
    @RoleAdminMissao
    @RoleAdminMissaoComplexo
    @Path("/salvar")
    public void salvar(@Valid Plantao plantao, Calendar dataHoraInicioNova, Calendar dataHoraFimNova) throws Exception {
        Long idCondutor = setaCondutor(plantao);
        Long idPlantao = isEdicao(plantao) ? plantao.getId() : 0L;

        if (validator.hasErrors()) {
            result.include(PLANTAO, plantao);
            redirecionaPaginaCasoOcorraErros(idCondutor, idPlantao);
        }

        error(!plantao.ordemDeDatasCorreta(), "dataHoraInicio", "plantoes.dataHoraInicio.validation");

        List<Afastamento> afastamentos = Afastamento.buscarPorCondutores(plantao.getCondutor().getId(), FormatarDataHora.formatarData(plantao.getDataHoraInicio()), FormatarDataHora.formatarData(plantao.getDataHoraFim()));

        StringBuilder sbAfastamento = new StringBuilder();
        for (Afastamento item : afastamentos) {
            sbAfastamento.append("".equals(sbAfastamento.toString())? "" : ",");
            sbAfastamento.append(FormatarDataHora.formatarData(item.getDataHoraInicio()) + " a " + FormatarDataHora.formatarData(item.getDataHoraFim()));
        }

        String listaAfastamento = sbAfastamento.toString();
        if (!"".equals(listaAfastamento))
            validator.add(new SimpleMessage(PLANTAO,"Condutor afastado " + getMensagemPeriodo(listaAfastamento) + " de: " + listaAfastamento + "."));

        if (validator.hasErrors()) {
            result.include(PLANTAO, plantao);
            redirecionaPaginaCasoOcorraErros(idCondutor, idPlantao);
        } else {
            List<Plantao> plantoes = Plantao.buscarPorCondutores(plantao.getCondutor().getId(), FormatarDataHora.formatarData(plantao.getDataHoraInicio()), 
            		FormatarDataHora.formatarData(plantao.getDataHoraFim()), plantao.getId());
            StringBuilder sbPlantao = new StringBuilder();
            for (Plantao item : plantoes) {
                sbPlantao.append("".equals(sbPlantao.toString()) ? "" : ",");
                sbPlantao.append(FormatarDataHora.formatarData(item.getDataHoraInicio()) + " a " + FormatarDataHora.formatarData(item.getDataHoraFim()));
            }

            String listaPlantao = sbPlantao.toString();
            if (!"".equals(listaPlantao))
                validator.add(new SimpleMessage(PLANTAO,"Condutor em plant&atilde;o " + getMensagemPeriodo(listaPlantao) + " de: " + listaPlantao + "."));
        }

        if (validator.hasErrors()) {
            result.include(PLANTAO, plantao);
            redirecionaPaginaCasoOcorraErros(idCondutor, idPlantao);
        } else {
            if (isEdicao(plantao) && !(plantao.getDataHoraInicio().before(dataHoraInicioNova) && plantao.getDataHoraFim().after(dataHoraFimNova))) {
                List<Missao> missoes = retornarMissoesCondutorPlantao(plantao, dataHoraInicioNova, dataHoraFimNova);
                StringBuilder sbMissoes = new StringBuilder();
                for (Missao item : missoes) {
                    sbMissoes.append("".equals(sbMissoes.toString()) ? "" : ",");
                    sbMissoes.append(item.getSequence());
                }
                String listaMissoes = sbMissoes.toString();
                error(!missoes.isEmpty(), "LinkErroCondutor", listaMissoes);
            }

            if (validator.hasErrors()) {
                result.include(PLANTAO, plantao);
                redirecionaPaginaCasoOcorraErros(idCondutor, idPlantao);
            } else {
                plantao.save();
                result.redirectTo(PlantaoController.class).listarPorCondutor(idCondutor);
            }
        }
    }

    @Transactional
    @RoleAdmin
    @RoleAdminMissao
    @RoleAdminMissaoComplexo
    @Path("/excluir/{id}")
    public void excluir(Long id) throws Exception {
        EntityTransaction tx = Plantao.AR.em().getTransaction();

        Plantao plantao = Plantao.AR.findById(id);
        Long idCondutor = plantao.getCondutor().getId();

        List<Missao> missoes = retornarMissoesCondutorPlantao(plantao, null, null);
        StringBuilder listaMissoes = new StringBuilder();
        String delimitador = "";

        for (Missao item : missoes) {
            listaMissoes.append(delimitador).append(item.getSequence());
            delimitador = ",";
        }

        error(!missoes.isEmpty(), "LinkErroCondutor", listaMissoes.toString());

        if (validator.hasErrors()) {
            redirecionaPaginaCasoOcorraErros(idCondutor, id);
        } else {

            if (!tx.isActive())
                tx.begin();

            try {
                plantao.delete();
                tx.commit();

                result.redirectTo(PlantaoController.class).listarPorCondutor(idCondutor);
            } catch (PersistenceException ex) {
                tx.rollback();

                if (FormatarTextoHtml.removerAcentuacao(ex.getCause().getCause().getMessage()).contains("restricao de integridade")) 
                    validator.add(new I18nMessage(PLANTAO, "plantao.excluir.validation"));
                else
                    validator.add(new SimpleMessage(PLANTAO,ex.getMessage()));

            } catch (Exception ex) {
                tx.rollback();

                validator.add(new SimpleMessage(PLANTAO,ex.getMessage()));
            }

            validator.onErrorForwardTo(PlantaoController.class).listarPorCondutor(idCondutor);
        }
    }

    private Condutor buscaCondutor(Long idCondutor) throws Exception {
        return Condutor.AR.findById(idCondutor);
    }

/*    private static String formatarData(Calendar data) {
        return String.format("%02d", data.get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d", data.get(Calendar.MONTH) + 1) + "/" + String.format("%04d", data.get(Calendar.YEAR));
    }
*/
    private String getMensagemPeriodo(String lista) {
        if (lista != null)
            return lista.contains(",") ? "nos per&iacute;odos" : "no per&iacute;odo";

        return "";
    }

    private boolean isEdicao(Plantao plantao) {
        return plantao.getId() > 0;
    }

    private Long setaCondutor(Plantao plantao) throws Exception {
        Long idCondutor = plantao.getCondutor().getId();
        plantao.setCondutor(buscaCondutor(idCondutor));
        return idCondutor;
    }

    private void redirecionaPaginaCasoOcorraErros(Long idCondutor, Long idPlantao) throws Exception {
        validator.onErrorUse(Results.page()).of(PlantaoController.class).editar(idCondutor, idPlantao);
    }

    private List<Missao> retornarMissoesCondutorPlantao(Plantao plantao, Calendar dataHoraInicioNova, Calendar dataHoraFimNova) {
        List<Missao> retorno = new ArrayList<Missao>();

        if (dataHoraInicioNova == null && dataHoraFimNova == null) {
            return Missao.retornarMissoes(CONDUTOR_ID, plantao.getCondutor().getId(), plantao.getCondutor().getCpOrgaoUsuario().getId(), plantao.getDataHoraInicio(), plantao.getDataHoraFim());
        }

        if (dataHoraInicioNova != null && dataHoraInicioNova.after(plantao.getDataHoraInicio())) {
            retorno.addAll(Missao.retornarMissoes(CONDUTOR_ID, plantao.getCondutor().getId(), plantao.getCondutor().getCpOrgaoUsuario().getId(), dataHoraInicioNova, plantao.getDataHoraInicio()));
        }

        if (dataHoraFimNova != null && dataHoraFimNova.before(plantao.getDataHoraFim())) {
            retorno.addAll(Missao.retornarMissoes(CONDUTOR_ID, plantao.getCondutor().getId(), plantao.getCondutor().getCpOrgaoUsuario().getId(), plantao.getDataHoraFim(), dataHoraFimNova));
        }

        return retorno;
    }

}