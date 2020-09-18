package br.gov.jfrj.siga.tp.vraptor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.gov.jfrj.siga.tp.exceptions.RelatorioException;
import br.gov.jfrj.siga.tp.model.Afastamento;
import br.gov.jfrj.siga.tp.model.DiaDaSemana;
import br.gov.jfrj.siga.tp.model.DiaDeTrabalho;
import br.gov.jfrj.siga.tp.model.EscalaDeTrabalho;
import br.gov.jfrj.siga.tp.model.Missao;
import br.gov.jfrj.siga.tp.model.Plantao;
import br.gov.jfrj.siga.tp.model.ServicoVeiculo;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/relatorio")
public class RelatorioController extends TpController {

    private static final String CONDUTOR = "condutor";
    private static final String REGISTROS = "registros";
    private static final String DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm";
    private static final int HORA_FINAL_EXPEDIENTE = 19;
    private static final int MINUTO_FINAL_EXPEDIENTE = 0;
    private static final int SEGUNDO_FINAL_EXPEDIENTE = 0;
    private static final int HORA_FINAL_DIA = 23;
    private static final int MINUTO_FINAL_DIA = 59;
    private static final int SEGUNDO_FINAL_DIA = 59;
    private static final int HORA_INICIAL_DIA = 0;
    private static final int MINUTO_INICIAL_DIA = 0;
    private static final int SEGUNDO_INICIAL_DIA = 0;
    private static final String SEPARADOR_VIRGULA = "\', \'";
    
    private static SimpleDateFormat formatoDataDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat formatoDataYYYYMDHMS = new SimpleDateFormat("yyyy,M,d,H,m,s");

	/**
	 * @deprecated CDI eyes only
	 */
	public RelatorioController() {
		super();
	}
	
	@Inject
    public RelatorioController(HttpServletRequest request, Result result,   Validator validator, SigaObjects so,  EntityManager em) {
        super(request, result, TpDao.getInstance(), validator, so, em);
    }

    @Path("/listarAgendaPorCondutorNoProximoDia/{idCondutor}/{dataPesquisa*}")
    public void listarAgendaPorCondutorNoProximoDia(Long idCondutor, Calendar dataPesquisa) throws ParseException {
        dataPesquisa.add(Calendar.DAY_OF_MONTH, 1);
        result.forwardTo(RelatorioController.class).listarAgendaPorCondutor(idCondutor, formatoDataDDMMYYYY.format(dataPesquisa.getTime()));
    }

    @Path("/listarAgendaPorCondutorNoDiaAnterior/{idCondutor}/{dataPesquisa*}")
    public void listarAgendaPorCondutorNoDiaAnterior(Long idCondutor, Calendar dataPesquisa) throws ParseException {
        dataPesquisa.add(Calendar.DAY_OF_MONTH, -1);
        
        result.forwardTo(RelatorioController.class).listarAgendaPorCondutor(idCondutor, formatoDataDDMMYYYY.format(dataPesquisa.getTime()));
    }

    @Path("/listarAgendaPorVeiculoNoProximoDia/{idVeiculo}/{dataPesquisa*}")
    public void listarAgendaPorVeiculoNoProximoDia(Long idVeiculo, Calendar dataPesquisa) throws ParseException {
        dataPesquisa.add(Calendar.DAY_OF_MONTH, 1);
        result.forwardTo(RelatorioController.class).listarAgendaPorVeiculo(idVeiculo, formatoDataDDMMYYYY.format(dataPesquisa.getTime()));
    }

    @Path("/listarAgendaPorVeiculoNoDiaAnterior/{idVeiculo}/{dataPesquisa*}")
    public void listarAgendaPorVeiculoNoDiaAnterior(Long idVeiculo, Calendar dataPesquisa) throws ParseException {
        dataPesquisa.add(Calendar.DAY_OF_MONTH, -1);
        result.forwardTo(RelatorioController.class).listarAgendaPorVeiculo(idVeiculo, formatoDataDDMMYYYY.format(dataPesquisa.getTime()));
    }

    @Path("/listarAgendaTodosCondutores")
    public void listarAgendaTodosCondutores() throws ParseException {
        result.forwardTo(RelatorioController.class).listarAgendaPorCondutor(0L, getToday());
    }

    @Path("/listarAgendaTodosVeiculos")
    public void listarAgendaTodosVeiculos() throws ParseException {
        result.forwardTo(RelatorioController.class).listarAgendaPorVeiculo(0L, getToday());
    }

    @SuppressWarnings("unused")
	@Path("/listarAgendaPorCondutor/{idCondutor}/{dataPesquisa*}")
    public void listarAgendaPorCondutor(Long idCondutor, String dataPesquisa) throws ParseException {
    	if(true) {
    		throw new RuntimeException("Relatorio em manutencao.");
    	}
        
    	Long idCondutorParaBusca = verificaIdNulo(idCondutor);

        String registros = "";
        Calendar dataHoraPesquisa = Calendar.getInstance();
        SimpleDateFormat formatar = new SimpleDateFormat(DD_MM_YYYY_HH_MM);

        String strDataPesquisa = null;

        if (dataPesquisa != null)
            dataHoraPesquisa = toCalendar(dataPesquisa);

        strDataPesquisa = String.format("%02d", dataHoraPesquisa.get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d", dataHoraPesquisa.get(Calendar.MONTH) + 1) + "/"
                + String.format("%04d", dataHoraPesquisa.get(Calendar.YEAR));
        dataHoraPesquisa.setTime(formatar.parse(strDataPesquisa + " 00:00"));

        List<EscalaDeTrabalho> escalas = EscalaDeTrabalho.buscarPorCondutores(idCondutorParaBusca, strDataPesquisa);
        List<EscalaDeTrabalho> escalasFiltradas = filtrarPorOrgao(escalas, EscalaDeTrabalho.class);

        List<Afastamento> afastamentos = Afastamento.buscarPorCondutores(idCondutorParaBusca, strDataPesquisa);
        List<Afastamento> afastamentosFiltrados = filtrarPorOrgao(afastamentos, Afastamento.class);

        List<Plantao> plantoes = Plantao.buscarPorCondutores(idCondutorParaBusca, strDataPesquisa);
        List<Plantao> plantoesFiltrados = filtrarPorOrgao(plantoes, Plantao.class);

        List<Missao> missoes = Missao.buscarPorCondutores(idCondutorParaBusca, strDataPesquisa);
        List<Missao> missoesFiltradas = filtrarPorOrgao(missoes, Missao.class);

        registros = adicionarDadosEscalaDeTrabalho(registros, dataHoraPesquisa, escalasFiltradas);

        String registrosEscala = registros;
        registros = gerarTimeLine(dataHoraPesquisa, registrosEscala, afastamentosFiltrados, plantoesFiltrados, missoesFiltradas, new ArrayList<ServicoVeiculo>(), CONDUTOR);

        result.include("dataPesquisa", formatoDataDDMMYYYY.format(dataHoraPesquisa.getTime()));
        result.include(REGISTROS, registros);
        result.include("idCondutor", idCondutor);
        result.include("entidade", "Condutor");
    }

    private String adicionarDadosEscalaDeTrabalho(String registros, Calendar dataHoraPesquisa, List<EscalaDeTrabalho> escalasFiltradas) throws ParseException {
        String delim = "";
        StringBuilder registrosRetorno = new StringBuilder(registros);

        for (EscalaDeTrabalho escala : escalasFiltradas) {
            SimpleDateFormat formatar1 = new SimpleDateFormat(DD_MM_YYYY_HH_MM);
            String srtDataPesquisa = formatar1.format(dataHoraPesquisa.getTime());
            DiaDaSemana diaDePesquisa = DiaDaSemana.getDiaDaSemana(srtDataPesquisa);
            delim = adicionarDadosDiaDeTrabalho(dataHoraPesquisa, delim, registrosRetorno, escala, diaDePesquisa, formatoDataYYYYMDHMS);
        }
        return registrosRetorno.toString();
    }

    private String adicionarDadosDiaDeTrabalho(Calendar dataHoraPesquisa, String delimitador, StringBuilder registrosRetorno, EscalaDeTrabalho escala, DiaDaSemana diaDePesquisa,
            SimpleDateFormat formatoData1) {
        Calendar dataHoraInicioTemp = Calendar.getInstance();
        Calendar dataHoraFimTemp = recuperarDataEHora(escala.getDataVigenciaInicio(), HORA_FINAL_DIA, MINUTO_FINAL_DIA, SEGUNDO_FINAL_DIA);
        String delim = delimitador;

        for (DiaDeTrabalho dia : escala.getDiasDeTrabalho()) {
            if (diaDePesquisa.isEquals(dia.getDiaEntrada())) {
                dataHoraInicioTemp = recuperarDataEHora(dataHoraPesquisa, dia.getHoraEntrada().get(Calendar.HOUR_OF_DAY), dia.getHoraEntrada().get(Calendar.MINUTE),
                        dia.getHoraEntrada().get(Calendar.SECOND));

                if (diaDePesquisa.isEquals(dia.getDiaSaida())) {
                    dataHoraFimTemp = recuperarDataEHora(dataHoraPesquisa, dia.getHoraSaida().get(Calendar.HOUR_OF_DAY), dia.getHoraSaida().get(Calendar.MINUTE),
                            dia.getHoraSaida().get(Calendar.SECOND));
                } else {
                    dataHoraFimTemp = recuperarDataEHora(dataHoraPesquisa, HORA_FINAL_EXPEDIENTE, MINUTO_FINAL_EXPEDIENTE, SEGUNDO_FINAL_EXPEDIENTE);
                }
                
                dataParaJavascript(dataHoraInicioTemp);

                registrosRetorno.append(delim);
                registrosRetorno.append("[ \'" + "Escalas" + SEPARADOR_VIRGULA + escala.getCondutor().getNome() + "\', new Date(" + formatoData1.format(dataHoraInicioTemp.getTime()) + "), new Date(");
                registrosRetorno.append(formatoData1.format(dataParaJavascript(dataHoraFimTemp)) + ") ]");
                delim = ", ";
            }
        }
        return delim;
    }

    private Date dataParaJavascript(Calendar dataHoraInicioTemp) {
        dataHoraInicioTemp.add(Calendar.MONTH, -1);
        return dataHoraInicioTemp.getTime();
    }

    @SuppressWarnings("unused")
	@Path("/listarAgendaPorVeiculo/{idVeiculo}/{dataPesquisa*}")
    public void listarAgendaPorVeiculo(Long idVeiculo, String dataPesquisa) throws ParseException {

    	if(true) {
    		throw new RuntimeException("Relatorio em manutencao.");
    	}
        
    	Long idVeiculoParaBusca = verificaIdNulo(idVeiculo);

        String registros = "";

        Calendar dataHoraPesquisa = Calendar.getInstance();
        SimpleDateFormat formatar = new SimpleDateFormat(DD_MM_YYYY_HH_MM);

        String strDataPesquisa = null;

        if (dataPesquisa != null)
            dataHoraPesquisa = toCalendar(dataPesquisa);

        strDataPesquisa = String.format("%02d", dataHoraPesquisa.get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d", dataHoraPesquisa.get(Calendar.MONTH) + 1) + "/"
                + String.format("%04d", dataHoraPesquisa.get(Calendar.YEAR));
        dataHoraPesquisa.setTime(formatar.parse(strDataPesquisa + " 00:00"));

        List<Missao> missoes = Missao.buscarPorVeiculos(idVeiculoParaBusca, strDataPesquisa);
        List<Missao> missoesFiltradas = filtrarPorOrgao(missoes, Missao.class);

        List<ServicoVeiculo> servicosVeiculos = ServicoVeiculo.buscarPorVeiculo(idVeiculoParaBusca, strDataPesquisa);
        List<ServicoVeiculo> servicosFiltrados = filtrarPorOrgao(servicosVeiculos, ServicoVeiculo.class);

        registros = gerarTimeLine(dataHoraPesquisa, "", new ArrayList<Afastamento>(), new ArrayList<Plantao>(), missoesFiltradas, servicosFiltrados, "veiculo");

        result.include("dataPesquisa", formatoDataDDMMYYYY.format(dataHoraPesquisa.getTime()));
        result.include(REGISTROS, registros);
        result.include("idVeiculo", idVeiculo);
        result.include("entidade", "Veiculo");
    }

    private String gerarTimeLine(Calendar dataHoraPesquisa, String registros, List<Afastamento> afastamentos, List<Plantao> plantoes, List<Missao> missoes, List<ServicoVeiculo> servicosVeiculos,
            String entidade) {
        StringBuilder registrosGerados = new StringBuilder(registros);
        String delim = "";

        if (!registrosGerados.toString().isEmpty())
            delim = ", ";

        delim = adicionarDadosAfastamento(dataHoraPesquisa, afastamentos, registrosGerados, delim);
        delim = adicionarDadosPlantao(dataHoraPesquisa, plantoes, registrosGerados, delim);
        delim = adicionarDadosServicoVeiculo(dataHoraPesquisa, servicosVeiculos, registrosGerados, delim);
        delim = adicionarDadosCondutor(dataHoraPesquisa, missoes, entidade, registrosGerados, delim);

        return registrosGerados.toString();
    }

    private boolean mesmaDataEApos(Calendar origem, Calendar comparativo) {
        if (origem == null || comparativo == null) {
            return false;
        }
        return mesmaData(origem, comparativo) && isAfter(origem, comparativo);
    }

    private boolean mesmaData(Calendar origem, Calendar comparativo) {
        return mesmoAno(origem, comparativo) && mesmoMes(origem, comparativo) && mesmoDiaDoMes(origem, comparativo);
    }

    private boolean isAfter(Calendar origem, Calendar comparativo) {
        return origem.after(comparativo);
    }

    private boolean mesmoDiaDoMes(Calendar origem, Calendar comparativo) {
        return origem.get(Calendar.DAY_OF_MONTH) == comparativo.get(Calendar.DAY_OF_MONTH);
    }

    private boolean mesmoMes(Calendar origem, Calendar comparativo) {
        return origem.get(Calendar.MONTH) == comparativo.get(Calendar.MONTH);
    }

    private boolean mesmoAno(Calendar origem, Calendar comparativo) {
        return origem.get(Calendar.YEAR) == comparativo.get(Calendar.YEAR);
    }

    private String adicionarDadosAfastamento(Calendar dataHoraPesquisa, List<Afastamento> afastamentos, StringBuilder registrosGerados, String delimitador) {
        String delim = delimitador;
        for (Afastamento afastamento : afastamentos) {
            registrosGerados.append(delim);
            
            registrosGerados.append("[ \'" + "Afastamentos" + SEPARADOR_VIRGULA + afastamento.getCondutor().getNome() + "\', new Date(");

            if (mesmaDataEApos(afastamento.getDataHoraInicio(), dataHoraPesquisa)) {
                registrosGerados.append(formatoDataYYYYMDHMS.format(dataParaJavascript(afastamento.getDataHoraInicio())) + "), new Date(");
            } else {
                Calendar dataHora = recuperarDataEHora(dataHoraPesquisa, HORA_INICIAL_DIA, MINUTO_INICIAL_DIA, SEGUNDO_INICIAL_DIA);
                registrosGerados.append(formatoDataYYYYMDHMS.format(dataParaJavascript(dataHora)) + "), new Date(");
            }

            if (mesmaDataEApos(afastamento.getDataHoraFim(), dataHoraPesquisa)) {
                registrosGerados.append(formatoDataYYYYMDHMS.format(dataParaJavascript(afastamento.getDataHoraFim())) + ") ]");
                delim = ", ";
                continue;
            }
            Calendar dataHora = recuperarDataEHora(dataHoraPesquisa, HORA_FINAL_EXPEDIENTE, MINUTO_FINAL_EXPEDIENTE, SEGUNDO_FINAL_EXPEDIENTE);
            registrosGerados.append(formatoDataYYYYMDHMS.format(dataParaJavascript(dataHora)) + ") ]");
            delim = ", ";
        }
        return delim;
    }

    private String adicionarDadosPlantao(Calendar dataHoraPesquisa, List<Plantao> plantoes, StringBuilder registrosGerados, String delimitador) {
        String delim = delimitador;
        for (Plantao plantao : plantoes) {
            registrosGerados.append(delim);
            registrosGerados.append("[ \'" + "Plantoes" + SEPARADOR_VIRGULA + plantao.getCondutor().getNome() + "\', new Date(");

            if (mesmaDataEApos(plantao.getDataHoraInicio(), dataHoraPesquisa)) {
                registrosGerados.append(formatoDataYYYYMDHMS.format(dataParaJavascript(plantao.getDataHoraInicio())) + "), new Date(");
            } else {
                Calendar dataHora = recuperarDataEHora(dataHoraPesquisa, HORA_INICIAL_DIA, MINUTO_INICIAL_DIA, SEGUNDO_INICIAL_DIA);
                registrosGerados.append(formatoDataYYYYMDHMS.format(dataParaJavascript(dataHora)) + "), new Date(");
            }

            if (mesmaDataEApos(plantao.getDataHoraFim(), dataHoraPesquisa)) {
                registrosGerados.append(formatoDataYYYYMDHMS.format(dataParaJavascript(plantao.getDataHoraFim())) + ") ]");
                delim = ", ";
                continue;
            }
            Calendar dataHora = recuperarDataEHora(dataHoraPesquisa, HORA_FINAL_EXPEDIENTE, MINUTO_FINAL_EXPEDIENTE, SEGUNDO_FINAL_EXPEDIENTE);
            registrosGerados.append(formatoDataYYYYMDHMS.format(dataParaJavascript(dataHora)) + ") ]");
            delim = ", ";
        }
        return delim;
    }

    private String adicionarDadosServicoVeiculo(Calendar dataHoraPesquisa, List<ServicoVeiculo> servicosVeiculos, StringBuilder registrosGerados, String delimitador) {
        String delim = delimitador;
        for (ServicoVeiculo servicoVeiculo : servicosVeiculos) {
            registrosGerados.append(delim);

            registrosGerados.append("[ \'" + "Servicos" + SEPARADOR_VIRGULA + servicoVeiculo.getVeiculo().getPlaca() + "\', new Date(");

            if (mesmaDataEApos(servicoVeiculo.getDataHoraInicio(), dataHoraPesquisa)) {
                registrosGerados.append(formatoDataYYYYMDHMS.format(dataParaJavascript(servicoVeiculo.getDataHoraInicio())) + "), new Date(");
            } else {
                Calendar dataHora = recuperarDataEHora(dataHoraPesquisa, HORA_INICIAL_DIA, MINUTO_INICIAL_DIA, SEGUNDO_INICIAL_DIA);
                registrosGerados.append(formatoDataYYYYMDHMS.format(dataParaJavascript(dataHora)) + "), new Date(");
            }

            if (mesmaDataEApos(servicoVeiculo.getDataHoraFim(), dataHoraPesquisa)) {
                registrosGerados.append(formatoDataYYYYMDHMS.format(dataParaJavascript(servicoVeiculo.getDataHoraFim())) + ") ]");
                delim = ", ";
                continue;
            }
            Calendar dataHora = recuperarDataEHora(dataHoraPesquisa, HORA_FINAL_EXPEDIENTE, MINUTO_FINAL_EXPEDIENTE, SEGUNDO_FINAL_EXPEDIENTE);
            registrosGerados.append(formatoDataYYYYMDHMS.format(dataParaJavascript(dataHora)) + ") ]");
            delim = ", ";
        }
        return delim;
    }

    private String adicionarDadosCondutor(Calendar dataHoraPesquisa, List<Missao> missoes, String entidade, StringBuilder registrosGerados, String delimitador) {
        String label = CONDUTOR;
        String delim = delimitador;
        for (Missao missao : missoes) {
            registrosGerados.append(delim);

            if (CONDUTOR.equals(entidade)) {
                label = missao.getSequence() + "-" + missao.getCondutor().getNome();
            } else {
                label = missao.getSequence() + "-" + missao.getVeiculo().getPlaca();
            }

            registrosGerados.append("[ \'" + "Missoes" + SEPARADOR_VIRGULA + label + "\', new Date(");

            if (mesmaDataEApos(missao.getDataHoraSaida(), dataHoraPesquisa)) {
                registrosGerados.append(formatoDataYYYYMDHMS.format(dataParaJavascript(missao.getDataHoraSaida())) + "), new Date(");
            } else {
                Calendar dataHora = recuperarDataEHora(dataHoraPesquisa, HORA_INICIAL_DIA, MINUTO_INICIAL_DIA, SEGUNDO_INICIAL_DIA);
                registrosGerados.append(formatoDataYYYYMDHMS.format(dataParaJavascript(dataHora)) + "), new Date(");

            }

            if (mesmaDataEApos(missao.getDataHoraRetorno(), dataHoraPesquisa)) {
                registrosGerados.append(formatoDataYYYYMDHMS.format(dataParaJavascript(missao.getDataHoraRetorno())) + ") ]");
                delim = ", ";
                continue;
            }
            Calendar dataHora = recuperarDataEHora(dataHoraPesquisa, HORA_FINAL_EXPEDIENTE, MINUTO_FINAL_EXPEDIENTE, SEGUNDO_FINAL_EXPEDIENTE);
            registrosGerados.append(formatoDataYYYYMDHMS.format(dataParaJavascript(dataHora)) + ") ]");
            delim = ", ";
        }
        return delim;
    }

    @Path("/listarMissoesEmAndamento")
    public void listarMissoesEmAndamento() {
        List<Missao> missoes = Missao.buscarEmAndamento();
        List<Missao> missoesFiltradas = filtrarPorOrgao(missoes, Missao.class);

        StringBuilder registros = new StringBuilder();

        for (int i = 0; i < missoesFiltradas.size(); i++) {
            registros.append("[ \'" + missoes.get(i).getSequence() + " - " + missoes.get(i).getVeiculo().getPlaca() + SEPARADOR_VIRGULA + missoes.get(i).getCondutor().getNome() + "\', new Date("
                    + formatoDataYYYYMDHMS.format(dataParaJavascript(missoes.get(i).getDataHoraSaida())) + "), new Date(");

            if (missoes.get(i).getDataHoraRetorno() != null)
                registros.append(formatoDataYYYYMDHMS.format(dataParaJavascript(missoes.get(i).getDataHoraRetorno())) + ") ]");
            else {
                Calendar dataHora = recuperarDataEHora(missoes.get(i).getDataHoraSaida(), HORA_FINAL_EXPEDIENTE, MINUTO_FINAL_EXPEDIENTE, SEGUNDO_FINAL_EXPEDIENTE);
                registros.append(formatoDataYYYYMDHMS.format(dataParaJavascript(dataHora)) + ") ]");
            }
            if (i < missoes.size() - 1)
                registros.append(", ");
        }

        result.include(REGISTROS, registros.toString());
    }

    @Path("/listarDadosDaMissao/{id}")
    public void listarDadosDaMissao(Long id) throws RelatorioException {
        try {
            Missao missao = Missao.AR.findById(id);
            result.include("missao", missao);
        } catch (Exception e) {
            throw new RelatorioException(e);
        }
    }

    private <T> List<T> filtrarPorOrgao(List<T> lista, final Class<T> classe) {
        return Lists.newArrayList(Iterables.filter(lista, new PredicateImplementation<T>(classe)));
    }

    private static Calendar recuperarDataEHora(Calendar dataHoraPesquisa, int hora, int minuto, int segundo) {
        Calendar dataHora = Calendar.getInstance();
        dataHora.set(Calendar.YEAR, dataHoraPesquisa.get(Calendar.YEAR));
        dataHora.set(Calendar.MONTH, dataHoraPesquisa.get(Calendar.MONTH));
        dataHora.set(Calendar.DAY_OF_MONTH, dataHoraPesquisa.get(Calendar.DAY_OF_MONTH));
        dataHora.set(Calendar.HOUR_OF_DAY, hora);
        dataHora.set(Calendar.MINUTE, minuto);
        dataHora.set(Calendar.SECOND, segundo);
        return dataHora;
    }

    private Long verificaIdNulo(Long id) {
        if (id != null && id > 0)
            return id;

        return null;
    }

    private final class PredicateImplementation<T> implements Predicate<T> {
        private final Class<T> classe;

        private PredicateImplementation(Class<T> classe) {
            this.classe = classe;
        }

        private boolean applyPlantao(T objeto) {
            if (classe.equals(Plantao.class)) {
                Plantao obj = (Plantao) objeto;
                return obj.getCondutor().getCpOrgaoUsuario().getId().equals(getTitular().getOrgaoUsuario().getId());
            } else {
                return false;
            }
        }

        private boolean applyAfastamento(T objeto) {
            if (classe.equals(Afastamento.class)) {
                Afastamento obj = (Afastamento) objeto;
                return obj.getCondutor().getCpOrgaoUsuario().getId().equals(getTitular().getOrgaoUsuario().getId());
            } else {
                return false;
            }
        }

        private boolean applyEscalaDeTrabalho(T objeto) {
            if (classe.equals(EscalaDeTrabalho.class)) {
                EscalaDeTrabalho obj = (EscalaDeTrabalho) objeto;
                return obj.getCondutor().getCpOrgaoUsuario().getId().equals(getTitular().getOrgaoUsuario().getId());
            } else {
                return false;
            }
        }

        private boolean applyServicoVeiculo(T objeto) {
            if (classe.equals(ServicoVeiculo.class)) {
                ServicoVeiculo obj = (ServicoVeiculo) objeto;
                return obj.getCpOrgaoUsuario().getId().equals(getTitular().getOrgaoUsuario().getId());
            } else {
                return false;
            }
        }

        private boolean applyMissao(T objeto) {
            if (classe.equals(Missao.class)) {
                Missao obj = (Missao) objeto;
                return obj.getCpOrgaoUsuario().getId().equals(getTitular().getOrgaoUsuario().getId());
            } else {
                return false;
            }
        }

        @Override
        public boolean apply(T objeto) {
            boolean plantoAfastamentoEscala = applyPlantao(objeto) || applyAfastamento(objeto) || applyEscalaDeTrabalho(objeto);
            boolean servicoMissao = applyServicoVeiculo(objeto) || applyMissao(objeto);
            return plantoAfastamentoEscala || servicoMissao;
        }
    }
    
    public static String getToday() {
        return formatoDataDDMMYYYY.format(Calendar.getInstance().getTime());
    }
    
    private Calendar toCalendar(String dataPesquisa) throws ParseException {
        Date dateObj = null;
        
         dateObj = formatoDataDDMMYYYY.parse(dataPesquisa);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateObj); 
        
        return cal;
    }
}
