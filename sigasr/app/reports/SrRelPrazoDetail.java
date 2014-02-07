package reports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.objectlab.kit.datecalc.common.DateCalculator;
import net.objectlab.kit.datecalc.common.DefaultHolidayCalendar;
import net.objectlab.kit.datecalc.common.HolidayCalendar;
import net.objectlab.kit.datecalc.common.HolidayHandlerType;
import net.objectlab.kit.datecalc.joda.LocalDateKitCalculatorsFactory;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.ISOChronology;

import de.jollyday.Holiday;
import de.jollyday.HolidayManager;

import models.SrMovimentacao;
import models.SrSolicitacao;
import play.db.jpa.JPA;
import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.dp.CpOcorrenciaFeriado;
import br.gov.jfrj.siga.dp.DpLotacao;

public class SrRelPrazoDetail extends RelatorioTemplate {
	
	public SrRelPrazoDetail(Map parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("dtIni").equals("")) {
			throw new DJBuilderException(
					"Parâmetro data inicial não informado!");
		}
		if (parametros.get("dtFim").equals("")) {
			throw new DJBuilderException(
					"Parâmetro data final não informado!");
		}
		if (parametros.get("lotacao") == null) {
			throw new DJBuilderException("Parâmetro lotação não informado!");
		}
		if (parametros.get("local") == null) {
			throw new DJBuilderException("Parâmetro local não informado!");
		}
}

	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException {
		this.setTitle("Relatório Detalhado de Solicitações por Prazo");
		this.addColuna("Solicitação", 20, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Horas Líquidas", 20, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Início/Fim", 40, RelatorioRapido.CENTRO, false);
		this.addColuna("Faixa", 20, RelatorioRapido.CENTRO, false);
		return this;
	}
	

	@Override
	public Collection processarDados() throws ParseException {

		List<String> d = new LinkedList<String>();
		List<CpOcorrenciaFeriado> ferjust =  CpOcorrenciaFeriado.find("select dtIniFeriado, dtFimFeriado from CpOcorrenciaFeriado").fetch();
		Set dataDosFeriados = new HashSet();
		Iterator it1 = ferjust.listIterator(); 
		while (it1.hasNext()) {
			Object[] obj = (Object[]) it1.next();
			DateTime inifer = new DateTime(obj[0]);
			DateTime fimfer = new DateTime(obj[1]);
			if (inifer != null) {
				dataDosFeriados.add(new LocalDate (inifer));
			}
			if (fimfer != null) {
				dataDosFeriados.add(new LocalDate(fimfer));
			}
		}
		/*--Histórico de lotações atendentes --*/
		String query = "select idLotacao from DpLotacao where idLotacaoIni = (select idLotacaoIni " +
			"from DpLotacao where idLotacao = " +  parametros.get("lotacao") + ")";
		List lotacoes = JPA.em().createQuery(query).getResultList();
		StringBuilder listalotacoes= new StringBuilder();
		for (int i  = 0; i < lotacoes.size(); i++){
			listalotacoes.append(lotacoes.get(i));
			if (i < ( lotacoes.size() - 1)) listalotacoes.append(",");
		}
		
		if (parametros.get("local").equals("0")) {
				List<SrSolicitacao> lista = SrSolicitacao.find(
					"select sol, mov " +
					"from SrSolicitacao sol, SrMovimentacao mov " +
					"where sol.idSolicitacao = mov.solicitacao " 
					+ "and mov.lotaAtendente in (" + listalotacoes + ") "
					+ "and mov.dtIniMov = (select max(movult.dtIniMov) " 
					+ "from SrMovimentacao movult " 
					+ "where movult.solicitacao = mov.solicitacao "
					+ " and movult.tipoMov = 7) "
					+ "and not exists (select 1 from SrMovimentacao movfec " 
					+	"where movfec.solicitacao = mov.solicitacao " 
					+	"and movfec.dtIniMov > mov.dtIniMov " 
					+	"and movfec.tipoMov <> 14) "
					+ "and sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') " 
					+ "and sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') ").fetch();
				Iterator it = lista.listIterator(); 
				while (it.hasNext()) {
						Object[] obj = (Object[]) it.next();
						SrSolicitacao sol = (SrSolicitacao) obj[0];
						SrMovimentacao mov = (SrMovimentacao) obj[1];
						DateTime startemp = new DateTime(sol.dtReg);
						DateTime endtemp = new DateTime(mov.dtIniMov);
						DateTime start1 = new DateTime();
						DateTime end1 = new DateTime();
				if (startemp.getHourOfDay() < 9) {
						start1 = new DateTime (startemp.getYear(),startemp.getMonthOfYear(),startemp.getDayOfMonth()+1,10,0,0);
				} else if (startemp.getHourOfDay() >= 18) {
						start1 = new DateTime (startemp.getYear(),startemp.getMonthOfYear(),startemp.getDayOfMonth()+1,10,0,0);							
				} else start1 = startemp;
				if (endtemp.getHourOfDay() < 9) {
						end1 = new DateTime (endtemp.getYear(),endtemp.getMonthOfYear(),endtemp.getDayOfMonth()+1,10,0,0);
				} else if (endtemp.getHourOfDay() >= 18) {
							end1 = new DateTime (endtemp.getYear(),endtemp.getMonthOfYear(),endtemp.getDayOfMonth()+1,10,0,0);							
				} else end1 = endtemp;
				int dias =  Days.daysBetween(start1.toDateMidnight(), end1.toDateMidnight()).getDays();
				HolidayCalendar calendarioDeFeriados = new DefaultHolidayCalendar(dataDosFeriados);
				LocalDateKitCalculatorsFactory.getDefaultInstance().registerHolidays("BR", calendarioDeFeriados);
				DateCalculator calendario =  LocalDateKitCalculatorsFactory.
				  									getDefaultInstance().
				   									getDateCalculator("BR", HolidayHandlerType.FORWARD);
				int diasNaoUteis = 0;
				int totalHorasTrabalhadas = 0;  
				BigDecimal totalMinutosTrabalhados = new BigDecimal(0.0);
					DateTime dataInicialTemporaria = new DateTime(start1);
					DateTime dataFinalTemporaria = new DateTime(end1);
					DateTime dtinitemp = new DateTime(start1);
					DateTime dtendtemp = new DateTime(end1);
					int i = 1;
					while (!dataInicialTemporaria.isAfter(dataFinalTemporaria)) {
						if (calendario.isNonWorkingDay(dataInicialTemporaria.toLocalDate())) {
							diasNaoUteis++;
						} else {
							if (i == dias) {
								dtinitemp = new DateTime(dataInicialTemporaria);
								dtendtemp = new DateTime(
										dataInicialTemporaria.getYear(),
										dataInicialTemporaria.getMonthOfYear(),
										dataInicialTemporaria.getDayOfMonth(), 18,
										0, 0);
							} else if (i > dias) {
								dtinitemp = new DateTime(dataInicialTemporaria);
								dtendtemp = new DateTime(end1);
							} else {
								dtinitemp = new DateTime(dataInicialTemporaria);
								dtendtemp = new DateTime(
										dataInicialTemporaria.getYear(),
										dataInicialTemporaria.getMonthOfYear(),
										dataInicialTemporaria.getDayOfMonth(), 18,
										0, 0);
							}
							totalHorasTrabalhadas += new Period(dtinitemp,
									dtendtemp).getHours();
							totalMinutosTrabalhados = new BigDecimal(Minutes
									.minutesBetween(dtinitemp, dtendtemp)
									.getMinutes()).add(totalMinutosTrabalhados);
						}
						dataInicialTemporaria = new DateTime(
								dataInicialTemporaria.getYear(),
								dataInicialTemporaria.getMonthOfYear(),
								dataInicialTemporaria.getDayOfMonth(), 9,
								0, 0).plusDays(1); 
						i++;
					}
					BigDecimal horasLiquidas = totalMinutosTrabalhados.divide(new BigDecimal("60"), 2, RoundingMode.HALF_UP);
					d.add(sol.getCodigo().toString());
					d.add(String.valueOf(horasLiquidas));
					d.add(sol.getDtRegString() + " - " +  mov.getDtIniMovDDMMYYHHMM());
					if (horasLiquidas.doubleValue() <= 1 ){
						d.add("Entre 0 e 1 hora");
					}
					if (horasLiquidas.doubleValue() > 1 && horasLiquidas.doubleValue() <= 2 ){
						d.add("Até 2 horas");
					}
					if (horasLiquidas.doubleValue() > 2 && horasLiquidas.doubleValue() <= 4 ){
						d.add("Até 4 horas");
					}
					if (horasLiquidas.doubleValue() > 4 && horasLiquidas.doubleValue() <= 12 ){
						d.add("Até 12 horas");
					}
					if (horasLiquidas.doubleValue() > 12 && horasLiquidas.doubleValue() <= 24 ){
						d.add("Até 24 horas");
					}
					if (horasLiquidas.doubleValue() > 24){
						d.add("Acima de 24 horas");
					}
				}
		} else {
			List<SrSolicitacao> lista = SrSolicitacao.find(
					"select sol, mov " +
					"from SrSolicitacao sol, SrMovimentacao mov " +
					"where sol.idSolicitacao = mov.solicitacao " +
					"and mov.lotaAtendente in (" + listalotacoes + ") " 
					+ "and sol.local = " + parametros.get("local") + " " 
					+ "and mov.dtIniMov = (select max(movult.dtIniMov) " 
					+ "from SrMovimentacao movult " 
					+ "where movult.solicitacao = mov.solicitacao "
					+ " and movult.tipoMov = 7) "
					+ "and not exists (select 1 from SrMovimentacao movfec " 
					+	"where movfec.solicitacao = mov.solicitacao " 
					+	"and movfec.dtIniMov > mov.dtIniMov " 
					+	"and movfec.tipoMov <> 14) "
					+ "and sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') " 
					+ "and sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') ").fetch();
				Iterator it = lista.listIterator(); 
				while (it.hasNext()) {
						Object[] obj = (Object[]) it.next();
						SrSolicitacao sol = (SrSolicitacao) obj[0];
						SrMovimentacao mov = (SrMovimentacao) obj[1];
						DateTime startemp = new DateTime(sol.dtReg);
						DateTime endtemp = new DateTime(mov.dtIniMov);
						DateTime start1 = new DateTime();
						DateTime end1 = new DateTime();
				if (startemp.getHourOfDay() < 10) {
						start1 = new DateTime (startemp.getYear(),startemp.getMonthOfYear(),startemp.getDayOfMonth()+1,10,0,0);
					} else if (startemp.getHourOfDay() >= 18) {
						start1 = new DateTime (startemp.getYear(),startemp.getMonthOfYear(),startemp.getDayOfMonth()+1,10,0,0);							
					} else start1 = startemp;
					if (endtemp.getHourOfDay() < 10) {
						end1 = new DateTime (endtemp.getYear(),endtemp.getMonthOfYear(),endtemp.getDayOfMonth()+1,10,0,0);
					} else if (endtemp.getHourOfDay() >= 18) {
							end1 = new DateTime (endtemp.getYear(),endtemp.getMonthOfYear(),endtemp.getDayOfMonth()+1,10,0,0);							
					} else end1 = endtemp;
					int dias =  Days.daysBetween(start1.toDateMidnight(), end1.toDateMidnight()).getDays();
					HolidayCalendar calendarioDeFeriados = new DefaultHolidayCalendar(dataDosFeriados);
					LocalDateKitCalculatorsFactory.getDefaultInstance().registerHolidays("BR", calendarioDeFeriados);
					DateCalculator calendario =  LocalDateKitCalculatorsFactory.
					  									getDefaultInstance().
					   									getDateCalculator("BR", HolidayHandlerType.FORWARD);
					int diasNaoUteis = 0;
					int totalHorasTrabalhadas = 0;  
					BigDecimal totalMinutosTrabalhados = new BigDecimal(0.0);
					DateTime dataInicialTemporaria = new DateTime(start1);
					DateTime dataFinalTemporaria = new DateTime(end1);
					DateTime dtinitemp = new DateTime(start1);
					DateTime dtendtemp = new DateTime(end1);
					int i = 1;
					while (!dataInicialTemporaria.isAfter(dataFinalTemporaria)) {
						if (calendario.isNonWorkingDay(dataInicialTemporaria.toLocalDate())) {
							diasNaoUteis++;
						} else {
							if (i == dias) {
								dtinitemp = new DateTime(dataInicialTemporaria);
								dtendtemp = new DateTime(
										dataInicialTemporaria.getYear(),
										dataInicialTemporaria.getMonthOfYear(),
										dataInicialTemporaria.getDayOfMonth(), 18,
										0, 0);
							} else if (i > dias) {
								dtinitemp = new DateTime(dataInicialTemporaria);
								dtendtemp = new DateTime(end1);
							} else {
								dtinitemp = new DateTime(dataInicialTemporaria);
								dtendtemp = new DateTime(
										dataInicialTemporaria.getYear(),
										dataInicialTemporaria.getMonthOfYear(),
										dataInicialTemporaria.getDayOfMonth(), 18,
										0, 0);
							}
							totalHorasTrabalhadas += new Period(dtinitemp,
									dtendtemp).getHours();
							totalMinutosTrabalhados = new BigDecimal(Minutes
									.minutesBetween(dtinitemp, dtendtemp)
									.getMinutes()).add(totalMinutosTrabalhados);
						}
						dataInicialTemporaria = new DateTime(
								dataInicialTemporaria.getYear(),
								dataInicialTemporaria.getMonthOfYear(),
								dataInicialTemporaria.getDayOfMonth(), 9,
								0, 0).plusDays(1); 
						i++;
					}
					BigDecimal horasLiquidas = totalMinutosTrabalhados.divide(new BigDecimal("60"), 2, RoundingMode.HALF_UP);
					d.add(sol.getCodigo().toString());
					d.add(String.valueOf(horasLiquidas));
					d.add(sol.getDtRegString() + " - " + mov.getDtIniMovDDMMYYYYHHMM());
					if (horasLiquidas.doubleValue() <= 1 ){
						d.add("Entre 0 e 1 hora");
					}
					if (horasLiquidas.doubleValue() > 1 && horasLiquidas.doubleValue() <= 2 ){
						d.add("Até 2 horas");
					}
					if (horasLiquidas.doubleValue() > 2 && horasLiquidas.doubleValue() <= 4 ){
						d.add("Até 4 horas");
					}
					if (horasLiquidas.doubleValue() > 4 && horasLiquidas.doubleValue() <= 12 ){
						d.add("Até 12 horas");
					}
					if (horasLiquidas.doubleValue() > 12 && horasLiquidas.doubleValue() <= 24 ){
						d.add("Até 24 horas");
					}
					if (horasLiquidas.doubleValue() > 24){
						d.add("Acima de 24 horas");
					}
				}
		}
		return d;
	}
}
