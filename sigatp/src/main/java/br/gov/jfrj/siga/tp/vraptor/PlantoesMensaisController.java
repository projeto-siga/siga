package br.gov.jfrj.siga.tp.vraptor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdmin;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissao;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissaoComplexo;
import br.gov.jfrj.siga.tp.dto.PlantaoDTO;
import br.gov.jfrj.siga.tp.exceptions.PlantoesMensaisException;
import br.gov.jfrj.siga.tp.model.Afastamento;
import br.gov.jfrj.siga.tp.model.Condutor;
import br.gov.jfrj.siga.tp.model.DiaDaSemana;
import br.gov.jfrj.siga.tp.model.Mes;
import br.gov.jfrj.siga.tp.model.Plantao;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/plantoesMensais")
public class PlantoesMensaisController extends TpController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PlantoesMensaisController.class);
    
    private static final String HORARIO_INICIO_PLANTAO_24H = "07:00";

	/**
	 * @deprecated CDI eyes only
	 */
	public PlantoesMensaisController() {
		super();
	}
	
	@Inject
    public PlantoesMensaisController(HttpServletRequest request, Result result,  Validator validator, SigaObjects so,  EntityManager em) {
        super(request, result, TpDao.getInstance(), validator, so, em);
    }

    @RoleAdmin
    @RoleAdminMissao
    @RoleAdminMissaoComplexo
    @Path("/imprimir")
    public void imprimir(String referencia) throws ParseException {
        List<Plantao> plantoes = Plantao.buscarTodosPorReferencia(referencia);
        ordenarPelaDataHoraInicioDoPlantao(plantoes);
        DiaDaSemana diaDaSemana = DiaDaSemana.getDiaDaSemana(plantoes.get(0).getDataHoraInicio());
        String dadosParaTitulo = plantoes.get(0).getReferencia();

        result.include("plantoes", plantoes);
        result.include("diaDaSemana", diaDaSemana);
        result.include("dadosParaTitulo", dadosParaTitulo);
    }

    @RoleAdmin
    @RoleAdminMissao
    @RoleAdminMissaoComplexo
    @Path("/editar")
    public void editar(String referencia) {
        List<Plantao> plantoes = Plantao.buscarTodosPorReferencia(referencia);
        Collections.sort(plantoes);
        Mes mes = extrairMesDaReferencia(referencia);
        int ano = extrairAnoDaReferencia(referencia);
        String hora = extrairHoraDaReferencia(referencia);
        montarDadosParaForm(plantoes, referencia, mes, ano, hora);
    }

    private String extrairHoraDaReferencia(String referencia) {
        String[] dados = referencia.split("[()]");
        return dados[1];
    }

    private int extrairAnoDaReferencia(String referencia) {
        String[] dados = referencia.split(" ");
        String retorno = dados[2];
        return Integer.parseInt(retorno);
    }

    private Mes extrairMesDaReferencia(String referencia) {
        String[] dados = referencia.split(" ");
        String mesPorExtenso = dados[0];
        return Mes.getMes(mesPorExtenso);
    }

    @Transactional
    @RoleAdmin
    @RoleAdminMissao
    @RoleAdminMissaoComplexo
    @Path("/excluir")
    public void excluir(String referencia) {
        List<Plantao> plantoesAExcluir = Plantao.buscarTodosPorReferencia(referencia);

        // verificar se este plantao mensal eh o atual ou passado; se sim, nao excluir
        if (!podeExcluirPlantaoMensal(plantoesAExcluir)) {
            validator.add(new I18nMessage("referencias", "plantoesMensais.podeExcluirPlantaoMensal.validation"));
            montarDadosParaListar();
            validator.onErrorUsePageOf(this).listar();
        }

        for (Iterator<Plantao> iterator = plantoesAExcluir.iterator(); iterator.hasNext();) {
            Plantao plantao = iterator.next();
            plantao.delete();
        }

        result.redirectTo(this).listar();
    }

    private boolean podeExcluirPlantaoMensal(List<Plantao> plantoesAExcluir) {
        Plantao teste = plantoesAExcluir.get(0);
        Calendar dataParaTestar = teste.getDataHoraInicio();

        Calendar ultimoDiaDoMes = Calendar.getInstance();
        ultimoDiaDoMes.set(Calendar.DATE, ultimoDiaDoMes.getMaximum(Calendar.DAY_OF_MONTH));

        if (ultimoDiaDoMes.compareTo(dataParaTestar) < 0) {
            return true;
        }

        return false;
    }

    @RoleAdmin
    @RoleAdminMissao
    @RoleAdminMissaoComplexo
    @Path("/listar")
    public void listar() {
        montarDadosParaListar();
    }

    private void montarDadosParaListar() {
        if (getTitular() != null && getTitular().getOrgaoUsuario() != null) {
            List<String> referencias = Plantao.getReferencias(getTitular().getOrgaoUsuario().getIdOrgaoUsu());
            result.include("referencias", referencias);
        }
    }

    private String[] gerarDatasPlantaoMensal(Integer mes, Integer ano, String hora) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        Calendar data = Calendar.getInstance();
        data.set(Calendar.MONTH, mes);
        data.set(Calendar.YEAR, ano);

        data.set(Calendar.DATE, data.getMinimum(Calendar.DAY_OF_MONTH));
        int ultimoDiaDoMesQueEuQuero = data.getActualMaximum(Calendar.DAY_OF_MONTH);
        String[] retorno = new String[ultimoDiaDoMesQueEuQuero + 1];

        for (int i = 0; i <= ultimoDiaDoMesQueEuQuero; i++) {
            retorno[i] = formato.format(data.getTime()) + " " + hora;
            data.add(Calendar.DAY_OF_MONTH, 1);
        }

        return retorno;
    }

    @RoleAdmin
    @RoleAdminMissao
    @RoleAdminMissaoComplexo
    @Path("/incluirInicio")
    public void incluirInicio() {
        montarDadosParaIncluirInicio();
    }

    private void montarDadosParaIncluirInicio() {
        Mes optMes = Mes.JANEIRO;

        Calendar dataParaTirarMes = Calendar.getInstance();
        dataParaTirarMes.add(Calendar.MONTH, 1);

        int anoCorrente = Calendar.getInstance().get(Calendar.YEAR);
        Integer[] optAno = { anoCorrente, anoCorrente + 1 };

        Mes mesDefault = Mes.getMes(dataParaTirarMes.get(Calendar.MONTH));
        int anoDefault = dataParaTirarMes.get(Calendar.YEAR);

        String[] optHora = criarOpcoesDeHora();
        String horaDefault = HORARIO_INICIO_PLANTAO_24H;

        result.include("optHora", optHora);
        result.include("horaDefault", horaDefault);
        result.include("optMes", optMes);
        result.include("mesDefault", mesDefault);
        result.include("optAno", optAno);
        result.include("anoDefault", anoDefault);
    }

    @RoleAdmin
    @RoleAdminMissao
    @RoleAdminMissaoComplexo
    @Path("/incluir")
    public void incluir(Mes mes, int ano, String hora) {
        String dadosParaTitulo = gerarDadosParaTituloEReferencia(mes, ano, hora);

        if (Plantao.plantaoMensalJaExiste(dadosParaTitulo)) {
            validator.add(new I18nMessage("hora", "plantoesMensais.plantaoMensalJaExiste.validation"));
            montarDadosParaIncluirInicio();
            validator.onErrorUsePageOf(this).incluirInicio();
        }

        String[] diasParaPlantoes = gerarDatasPlantaoMensal(mes.getCodigo(), ano, hora);
        List<Plantao> plantoes = new ArrayList<Plantao>();
        for (int cont = 0; cont < diasParaPlantoes.length - 1; cont++) {
            Plantao plantao = new Plantao();

            plantao.setDataHoraInicio(converterParaCalendar(diasParaPlantoes[cont]));
            plantao.setDataHoraFim(converterParaCalendar(diasParaPlantoes[cont + 1]));

            plantoes.add(plantao);
        }

        montarDadosParaForm(plantoes, dadosParaTitulo, mes, ano, hora);
    }

    private String gerarDadosParaTituloEReferencia(Mes mes, int ano, String hora) {
        return mes.getNomeExibicao() + " / " + ano + " (" + hora + ")";
    }

    private Calendar converterParaCalendar(String dataEmTexto) {
        Calendar retorno = Calendar.getInstance();
        SimpleDateFormat formatoDataEHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try {
            retorno.setTime(formatoDataEHora.parse(dataEmTexto));
        } catch (ParseException e) {
            throw new PlantoesMensaisException(MessagesBundle.getMessage("plantoesMensais.converterParaCalendar.exception"), e);
        }
        return retorno;
    }

    private void montarDadosParaForm(List<Plantao> plantoes, String dadosParaTitulo, Mes mes, int ano, String hora) {
        List<Condutor> condutores;
        try {
            condutores = Condutor.listarTodos(getTitular().getOrgaoUsuario());
            result.include("condutores", condutores);
        } catch (Exception e) {
            throw new PlantoesMensaisException(MessagesBundle.getMessage("plantoesMensais.montarDadosParaForm.exception"), e);
        }
        result.include("plantoes", plantoes);
        result.include("dadosParaTitulo", dadosParaTitulo);
        result.include("mes", mes);
        result.include("ano", ano);
        result.include("hora", hora);
    }

    private String[] criarOpcoesDeHora() {
        String[] retorno = new String[24];

        retorno[0] = "00:00";
        retorno[1] = "01:00";
        retorno[2] = "02:00";
        retorno[3] = "03:00";
        retorno[4] = "04:00";
        retorno[5] = "05:00";
        retorno[6] = "06:00";
        retorno[7] = "07:00";
        retorno[8] = "08:00";
        retorno[9] = "09:00";
        retorno[10] = "10:00";
        retorno[11] = "11:00";
        retorno[12] = "12:00";
        retorno[13] = "13:00";
        retorno[14] = "14:00";
        retorno[15] = "15:00";
        retorno[16] = "16:00";
        retorno[17] = "17:00";
        retorno[18] = "18:00";
        retorno[19] = "19:00";
        retorno[20] = "20:00";
        retorno[21] = "21:00";
        retorno[22] = "22:00";
        retorno[23] = "23:00";

        return retorno;
    }

    private Condutor recuperarCondutorBanco(Long id) {
        try {
            return Condutor.AR.findById(id);
        } catch (Exception e) {
            throw new PlantoesMensaisException(e);
        }
    }

    @Transactional
    @RoleAdmin
    @RoleAdminMissao
    @RoleAdminMissaoComplexo
    public void salvar(List<PlantaoDTO> plantoes, String dadosParaTitulo, Mes mes, int ano, String hora) {

        List<Plantao> listaPlantoes = recuperarListaPlantao(plantoes);

        ordenarPelaDataHoraInicioDoPlantao(listaPlantoes);

        boolean incluir = listaPlantoes.get(0).getId() == 0;

        SimpleDateFormat formatoDataEHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat formatoSomenteData = new SimpleDateFormat("dd/MM/yyyy");
        List<Plantao> plantoesComErro = new ArrayList<Plantao>();

        for (Iterator<Plantao> iterator = listaPlantoes.iterator(); iterator.hasNext();) {
            Plantao plantaoDaLista = iterator.next();
            Plantao plantao = incluir ? plantaoDaLista : montarPlantaoParaSalvar(plantaoDaLista);

            if (incluir) {
                plantao.setReferencia(dadosParaTitulo);
            }
            plantao.setCondutor(recuperarCondutorBanco(plantao.getCondutor().getId()));
            List<Afastamento> afastamentos = Afastamento.buscarPorCondutores(plantao.getCondutor(), plantao.getDataHoraInicio(), plantao.getDataHoraFim());
            if (afastamentos != null && !afastamentos.isEmpty()) {
                validator.add(new I18nMessage("plantao", "plantoesMensais.afastamentos.validation", plantao.getCondutor().getDadosParaExibicao(), formatoDataEHora.format(plantao.getDataHoraInicio()
                        .getTime()), formatoDataEHora.format(plantao.getDataHoraFim().getTime())));
                plantoesComErro.add(plantao);
            } else {
                try {
                    plantao.save();
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    validator.add(new I18nMessage("plantao:" + e.getMessage(), "plantoesMensais.erro.nao.identificado.salvar", formatoSomenteData.format(plantao.getDataHoraInicio().getTime())));
                    plantoesComErro.add(plantao);
                }
            }
        }

        if (!plantoesComErro.isEmpty()) {
            ContextoPersistencia.em().getTransaction().setRollbackOnly();
            if (incluir) {
                zerarIdsInvalidosDaListaDePlantao(listaPlantoes);
            }

            montarDadosParaForm(new ArrayList<Plantao>(listaPlantoes), dadosParaTitulo, mes, ano, hora);
            if (incluir) {
                validator.onErrorUsePageOf(this).incluir(mes, ano, hora);
            } else {
                validator.onErrorUsePageOf(this).editar(dadosParaTitulo);
            }
        }

        result.redirectTo(this).listar();

    }

    private List<Plantao> recuperarListaPlantao(List<PlantaoDTO> plantoes) {
        List<Plantao> retorno = new ArrayList<>();
        for (PlantaoDTO p : plantoes) {
            retorno.add(p.buscarPlantao());
        }
        return retorno;
    }

    private Plantao montarPlantaoParaSalvar(Plantao plantao) {
        Plantao retorno;
        try {
            retorno = Plantao.AR.findById(plantao.getId());
            retorno.setCondutor(Condutor.AR.findById(plantao.getCondutor().getId()));
            return retorno;
        } catch (Exception e) {
            throw new PlantoesMensaisException(e);
        }
    }

    private void ordenarPelaDataHoraInicioDoPlantao(List<Plantao> plantoes) {
        // nao vem ordenado conforme o HTML manda
        Collections.sort(plantoes);
    }

    private void zerarIdsInvalidosDaListaDePlantao(List<Plantao> plantoes) {
        // apos o rollback, os Ids atribuidos aos plantoes
        // que nao deram erro sao invalidos e precisam ser zerados
        for (Iterator<Plantao> iterator = plantoes.iterator(); iterator.hasNext();) {
            Plantao plantao = iterator.next();
            plantao.setId(0L);
        }
    }
}
