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

import models.SrAndamento;
import models.SrSolicitacao;
import play.db.jpa.JPA;
import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.dp.CpOcorrenciaFeriado;
import br.gov.jfrj.siga.dp.DpLotacao;

public class SrRelPrazo extends RelatorioTemplate {

	public SrRelPrazo(Map parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("dtIni").equals("")) {
			throw new DJBuilderException(
					"Parâmetro data inicial não informado!");
		}
		if (parametros.get("dtFim").equals("")) {
			throw new DJBuilderException("Parâmetro data final não informado!");
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
		this.setTitle("Relatório de Solicitações por Prazo");
		this.addColuna("Atendidos em até 1 hora", 16, RelatorioRapido.ESQUERDA,
				false);
		this.addColuna("Atendidos entre 1 até 2 horas", 16,
				RelatorioRapido.ESQUERDA, false);
		this.addColuna("Atendidos entre 2 até 4 horas", 16,
				RelatorioRapido.ESQUERDA, false);
		this.addColuna("Atendidos entre 4 até 12 horas", 16,
				RelatorioRapido.ESQUERDA, false);
		this.addColuna("Atendidos entre 12 até 24 horas", 16,
				RelatorioRapido.ESQUERDA, false);
		this.addColuna("Atendidos acima de 24 horas", 16,
				RelatorioRapido.ESQUERDA, false);
		return this;
	}

	@Override
	public Collection processarDados() throws ParseException {

		List<String> d = new LinkedList<String>();
		List<CpOcorrenciaFeriado> ferjust = CpOcorrenciaFeriado.find(
				"select dtIniFeriado, dtFimFeriado from CpOcorrenciaFeriado")
				.fetch();
		Set dataDosFeriados = new HashSet();
		Iterator it1 = ferjust.listIterator();
		int cls_1 = 0;
		int cls_2 = 0;
		int cls_4 = 0;
		int cls_12 = 0;
		int cls_24 = 0;
		int cls_ac24 = 0;
		while (it1.hasNext()) {
			Object[] obj = (Object[]) it1.next();
			DateTime inifer = new DateTime(obj[0]);
			DateTime fimfer = new DateTime(obj[1]);
			if (inifer != null) {
				dataDosFeriados.add(new LocalDate(inifer));
			}
			if (fimfer != null) {
				dataDosFeriados.add(new LocalDate(fimfer));
			}
		}
		String query = "select idLotacao from DpLotacao where idLotacaoIni = (select idLotacaoIni "
				+ "from DpLotacao where idLotacao = "
				+ parametros.get("lotacao") + ")";
		List lotacoes = JPA.em().createQuery(query).getResultList();
		StringBuilder listalotacoes = new StringBuilder();
		for (int i = 0; i < lotacoes.size(); i++) {
			listalotacoes.append(lotacoes.get(i));
			if (i < (lotacoes.size() - 1))
				listalotacoes.append(",");
		}

		if (parametros.get("local").equals("0")) {
			List<SrSolicitacao> lista = SrSolicitacao.find(
					"select sol, andam "
							+ "from SrSolicitacao sol, SrAndamento andam "
							+ "where sol.idSolicitacao = andam.solicitacao "
							+ "and andam.lotaAtendente in (" + listalotacoes
							+ ") " + "and andam.estado = 2 "
							+ "and sol.dtReg >= to_date('"
							+ parametros.get("dtIni")
							+ " 00:00:00','dd/MM/yy hh24:mi:ss') "
							+ "and sol.dtReg <= to_date('"
							+ parametros.get("dtFim")
							+ " 23:59:59','dd/MM/yy hh24:mi:ss') ").fetch();
			Iterator it = lista.listIterator();
			while (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				SrSolicitacao sol = (SrSolicitacao) obj[0];
				SrAndamento andam = (SrAndamento) obj[1];
				DateTime startemp = new DateTime(sol.dtReg);
				DateTime endtemp = new DateTime(andam.dtReg);
				DateTime start1 = new DateTime();
				DateTime end1 = new DateTime();
				if (startemp.getHourOfDay() < 10) {
					start1 = new DateTime(startemp.getYear(),
							startemp.getMonthOfYear(),
							startemp.getDayOfMonth() + 1, 10, 0, 0);
				} else if (startemp.getHourOfDay() >= 18) {
					start1 = new DateTime(startemp.getYear(),
							startemp.getMonthOfYear(),
							startemp.getDayOfMonth() + 1, 10, 0, 0);
				} else
					start1 = startemp;
				if (endtemp.getHourOfDay() < 10) {
					end1 = new DateTime(endtemp.getYear(),
							endtemp.getMonthOfYear(),
							endtemp.getDayOfMonth() + 1, 10, 0, 0);
				} else if (endtemp.getHourOfDay() >= 18) {
					end1 = new DateTime(endtemp.getYear(),
							endtemp.getMonthOfYear(),
							endtemp.getDayOfMonth() + 1, 10, 0, 0);
				} else
					end1 = endtemp;
				int dias = Days.daysBetween(start1.toDateMidnight(),
						end1.toDateMidnight()).getDays();
				HolidayCalendar calendarioDeFeriados = new DefaultHolidayCalendar(
						dataDosFeriados);
				LocalDateKitCalculatorsFactory.getDefaultInstance()
						.registerHolidays("BR", calendarioDeFeriados);
				DateCalculator calendario = LocalDateKitCalculatorsFactory
						.getDefaultInstance().getDateCalculator("BR",
								HolidayHandlerType.FORWARD);
				int diasNaoUteis = 0;
				int totalHorasTrabalhadas = 0;
				BigDecimal totalMinutosTrabalhados = new BigDecimal(0.0);
				LocalDate dataInicialTemporaria = new LocalDate(start1);
				LocalDate dataFinalTemporaria = new LocalDate(end1);
				DateTime dtinitemp = new DateTime(start1);
				DateTime dtendtemp = new DateTime(end1);
				int i = 1;
				while (!dataInicialTemporaria.isAfter(dataFinalTemporaria)) {
					if (calendario.isNonWorkingDay(dataInicialTemporaria)) {
						diasNaoUteis++;
					} else {
						if (i == 1) {
							dtinitemp = new DateTime(start1);
							dtendtemp = new DateTime(
									dataInicialTemporaria.getYear(),
									dataInicialTemporaria.getMonthOfYear(),
									dataInicialTemporaria.getDayOfMonth(), 18,
									0, 0);
						} else if (i > dias) {
							dtinitemp = new DateTime(
									dataInicialTemporaria.getYear(),
									dataInicialTemporaria.getMonthOfYear(),
									dataInicialTemporaria.getDayOfMonth(), 9,
									0, 0);
							dtendtemp = new DateTime(end1);
						} else {
							dtinitemp = new DateTime(
									dataInicialTemporaria.getYear(),
									dataInicialTemporaria.getMonthOfYear(),
									dataInicialTemporaria.getDayOfMonth(), 9,
									0, 0);
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
					dataInicialTemporaria = dataInicialTemporaria.plusDays(1);
					i++;
				}
				BigDecimal horasLiquidas = totalMinutosTrabalhados.divide(
						new BigDecimal("60"), 2, RoundingMode.HALF_UP);
				if (horasLiquidas.doubleValue() <= 1) {
					cls_1 = cls_1 + 1;
				}
				if (horasLiquidas.doubleValue() > 1
						&& horasLiquidas.doubleValue() <= 2) {
					cls_2 = cls_2 + 1;
				}
				if (horasLiquidas.doubleValue() > 2
						&& horasLiquidas.doubleValue() <= 4) {
					cls_4 = cls_4 + 1;
				}
				if (horasLiquidas.doubleValue() > 4
						&& horasLiquidas.doubleValue() <= 12) {
					cls_12 = cls_12 + 1;
				}
				if (horasLiquidas.doubleValue() > 12
						&& horasLiquidas.doubleValue() <= 24) {
					cls_24 = cls_24 + 1;
				}
				if (horasLiquidas.doubleValue() > 24) {
					cls_ac24 = cls_ac24 + 1;
				}
			}
			d.add(String.valueOf(cls_1));
			d.add(String.valueOf(cls_2));
			d.add(String.valueOf(cls_4));
			d.add(String.valueOf(cls_12));
			d.add(String.valueOf(cls_24));
			d.add(String.valueOf(cls_ac24));
		} else {
			List<SrSolicitacao> lista = SrSolicitacao.find(
					"select sol, andam "
							+ "from SrSolicitacao sol, SrAndamento andam "
							+ "where sol.idSolicitacao = andam.solicitacao "
							+ "and andam.lotaAtendente in (" + listalotacoes
							+ ") " + "and sol.local = "
							+ parametros.get("local") + " "
							+ "and andam.estado = 2 "
							+ "and sol.dtReg >= to_date('"
							+ parametros.get("dtIni")
							+ " 00:00:00','dd/MM/yy hh24:mi:ss') "
							+ "and sol.dtReg <= to_date('"
							+ parametros.get("dtFim")
							+ " 23:59:59','dd/MM/yy hh24:mi:ss') ").fetch();
			Iterator it = lista.listIterator();
			while (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				SrSolicitacao sol = (SrSolicitacao) obj[0];
				SrAndamento andam = (SrAndamento) obj[1];
				DateTime startemp = new DateTime(sol.dtReg);
				DateTime endtemp = new DateTime(andam.dtReg);
				DateTime start1 = new DateTime();
				DateTime end1 = new DateTime();
				if (startemp.getHourOfDay() < 10) {
					start1 = new DateTime(startemp.getYear(),
							startemp.getMonthOfYear(),
							startemp.getDayOfMonth() + 1, 10, 0, 0);
				} else if (startemp.getHourOfDay() >= 18) {
					start1 = new DateTime(startemp.getYear(),
							startemp.getMonthOfYear(),
							startemp.getDayOfMonth() + 1, 10, 0, 0);
				} else
					start1 = startemp;
				if (endtemp.getHourOfDay() < 10) {
					end1 = new DateTime(endtemp.getYear(),
							endtemp.getMonthOfYear(),
							endtemp.getDayOfMonth() + 1, 10, 0, 0);
				} else if (endtemp.getHourOfDay() >= 18) {
					end1 = new DateTime(endtemp.getYear(),
							endtemp.getMonthOfYear(),
							endtemp.getDayOfMonth() + 1, 10, 0, 0);
				} else
					end1 = endtemp;
				int dias = Days.daysBetween(start1.toDateMidnight(),
						end1.toDateMidnight()).getDays();
				HolidayCalendar calendarioDeFeriados = new DefaultHolidayCalendar(
						dataDosFeriados);
				LocalDateKitCalculatorsFactory.getDefaultInstance()
						.registerHolidays("BR", calendarioDeFeriados);
				DateCalculator calendario = LocalDateKitCalculatorsFactory
						.getDefaultInstance().getDateCalculator("BR",
								HolidayHandlerType.FORWARD);
				int diasNaoUteis = 0;
				int totalHorasTrabalhadas = 0;
				BigDecimal totalMinutosTrabalhados = new BigDecimal(0.0);
				LocalDate dataInicialTemporaria = new LocalDate(start1);
				LocalDate dataFinalTemporaria = new LocalDate(end1);
				DateTime dtinitemp = new DateTime(start1);
				DateTime dtendtemp = new DateTime(end1);
				int i = 1;
				while (!dataInicialTemporaria.isAfter(dataFinalTemporaria)) {
					if (calendario.isNonWorkingDay(dataInicialTemporaria)) {
						diasNaoUteis++;
					} else {
						if (i == 1) {
							dtinitemp = new DateTime(start1);
							dtendtemp = new DateTime(
									dataInicialTemporaria.getYear(),
									dataInicialTemporaria.getMonthOfYear(),
									dataInicialTemporaria.getDayOfMonth(), 18,
									0, 0);
						} else if (i > dias) {
							dtinitemp = new DateTime(
									dataInicialTemporaria.getYear(),
									dataInicialTemporaria.getMonthOfYear(),
									dataInicialTemporaria.getDayOfMonth(), 9,
									0, 0);
							dtendtemp = new DateTime(end1);
						} else {
							dtinitemp = new DateTime(
									dataInicialTemporaria.getYear(),
									dataInicialTemporaria.getMonthOfYear(),
									dataInicialTemporaria.getDayOfMonth(), 9,
									0, 0);
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
					dataInicialTemporaria = dataInicialTemporaria.plusDays(1);
					i++;
				}
				BigDecimal horasLiquidas = totalMinutosTrabalhados.divide(
						new BigDecimal("60"), 2, RoundingMode.HALF_UP);
				if (horasLiquidas.doubleValue() <= 1) {
					cls_1 = cls_1 + 1;
				}
				if (horasLiquidas.doubleValue() > 1
						&& horasLiquidas.doubleValue() <= 2) {
					cls_2 = cls_2 + 1;
				}
				if (horasLiquidas.doubleValue() > 2
						&& horasLiquidas.doubleValue() <= 4) {
					cls_4 = cls_4 + 1;
				}
				if (horasLiquidas.doubleValue() > 4
						&& horasLiquidas.doubleValue() <= 12) {
					cls_12 = cls_12 + 1;
				}
				if (horasLiquidas.doubleValue() > 12
						&& horasLiquidas.doubleValue() <= 24) {
					cls_24 = cls_24 + 1;
				}
				if (horasLiquidas.doubleValue() > 24) {
					cls_ac24 = cls_ac24 + 1;
				}
			}
			d.add(String.valueOf(cls_1));
			d.add(String.valueOf(cls_2));
			d.add(String.valueOf(cls_4));
			d.add(String.valueOf(cls_12));
			d.add(String.valueOf(cls_24));
			d.add(String.valueOf(cls_ac24));
		}
		return d;
	}

}
