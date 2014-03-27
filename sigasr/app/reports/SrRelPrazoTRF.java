package reports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.objectlab.kit.datecalc.common.DateCalculator;
import net.objectlab.kit.datecalc.common.DefaultHolidayCalendar;
import net.objectlab.kit.datecalc.common.HolidayCalendar;
import net.objectlab.kit.datecalc.common.HolidayHandlerType;
import net.objectlab.kit.datecalc.joda.LocalDateKitCalculatorsFactory;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.Period;

import models.SrMovimentacao;
import models.SrSolicitacao;
import models.SrTipoMovimentacao;
import play.db.jpa.JPA;
import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import ar.com.fdvs.dj.domain.constants.Font;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.Coluna;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.dp.CpOcorrenciaFeriado;
import br.gov.jfrj.siga.dp.DpLotacao;

public class SrRelPrazoTRF extends RelatorioTemplate {

	@SuppressWarnings("rawtypes")
	public SrRelPrazoTRF(Map parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("dtIni").equals("")) {
			throw new DJBuilderException(
					"Parâmetro data inicial não informado!");
		}
		if (parametros.get("dtFim").equals("")) {
			throw new DJBuilderException("Parâmetro data final não informado!");
		}
	}

	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException {

		this.setTitle("Relatório de Solicitações por Nível de Serviço");
		estiloTituloColuna.setFont(new Font(9, "Arial", true));

		this.addColuna("Local", 100, RelatorioRapido.DIREITA, true,
				String.class);
		Coluna cls_15m = this.addColuna("Até 15m", 11, RelatorioRapido.DIREITA,
				false, Double.class);
		cls_15m.setPadrao("0");
		Coluna perc_cls_15m = this.addColuna("%Total", 11,
				RelatorioRapido.DIREITA, false, Double.class);
		perc_cls_15m.setPadrao("0.00%");
		Coluna cls_1 = this.addColuna("Até 1h", 11, RelatorioRapido.DIREITA,
				false, Double.class);
		cls_1.setPadrao("0");
		Coluna perc_cls_1 = this.addColuna("%Total", 11,
				RelatorioRapido.DIREITA, false, Double.class);
		perc_cls_1.setPadrao("0.00%");
		Coluna cls_3 = this.addColuna("Até 3hs", 11, RelatorioRapido.DIREITA,
				false, Double.class);
		cls_3.setPadrao("0");
		Coluna perc_cls_3 = this.addColuna("%Total", 11,
				RelatorioRapido.DIREITA, false, Double.class);
		perc_cls_3.setPadrao("0.00%");
		Coluna cls_8 = this.addColuna("Até 8hs", 11, RelatorioRapido.DIREITA,
				false, Double.class);
		cls_8.setPadrao("0");
		Coluna perc_cls_8 = this.addColuna("%Total", 11,
				RelatorioRapido.DIREITA, false, Double.class);
		perc_cls_8.setPadrao("0.00%");
		Coluna cls_15 = this.addColuna("Até 15hs", 11, RelatorioRapido.DIREITA,
				false, Double.class);
		cls_15.setPadrao("0");
		Coluna perc_cls_15 = this.addColuna("%Total", 11,
				RelatorioRapido.DIREITA, false, Double.class);
		perc_cls_15.setPadrao("0.00%");
		return this;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Collection processarDados() throws Exception {

		String local = new String();
		List<Object> d = new LinkedList<Object>();
		double cls_15m = 0;
		double cls_1 = 0;
		double cls_3 = 0;
		double cls_8 = 0;
		double cls_15 = 0;
		Long percTotal = (long) 0.;

		Set dataDosFeriados = calculaFeriados();

		if ((parametros.get("lotacao").equals(""))
				&& (parametros.get("atendente") == null)) {

			SortedSet<String> set = new TreeSet<String>();
			TreeMap<String, Double> map = new TreeMap<String, Double>();
			TreeMap<String, Long> maptotais = new TreeMap<String, Long>();

			percTotal = SrSolicitacao
					.find("select count(*) "
							+ "from SrSolicitacao sol, SrMovimentacao mov "
							+ "where sol.idSolicitacao = mov.solicitacao "
							+ "and mov.dtIniMov = (select max(movult.dtIniMov) "
							+ "from SrMovimentacao movult "
							+ "where movult.solicitacao = mov.solicitacao "
							+ " and movult.tipoMov = 7 "
							+ " and movult.lotaAtendente.orgaoUsuario = 3) "
							+ "and not exists (select 1 from SrMovimentacao movfec "
							+ "where movfec.solicitacao = mov.solicitacao "
							+ "and movfec.dtIniMov > mov.dtIniMov "
							+ "and movfec.tipoMov <> 14) "
							+ "and  sol.dtReg >= to_date('"
							+ parametros.get("dtIni")
							+ " 00:00:00','dd/MM/yy hh24:mi:ss') "
							+ "and  sol.dtReg <= to_date('"
							+ parametros.get("dtFim")
							+ " 23:59:59','dd/MM/yy hh24:mi:ss') ").first();

			List<SrSolicitacao> solicDoOrgao = SrSolicitacao
					.find("select sol.idSolicitacao, sol.dtReg, mov.dtIniMov "
							+ "from SrSolicitacao sol, SrMovimentacao mov "
							+ "where sol.idSolicitacao = mov.solicitacao "
							+ "and mov.dtIniMov = (select max(movult.dtIniMov) "
							+ "from SrMovimentacao movult "
							+ "where movult.solicitacao = mov.solicitacao "
							+ " and movult.tipoMov = 7 "
							+ " and movult.lotaAtendente.orgaoUsuario = 3) "
							+ "and not exists (select 1 from SrMovimentacao movfec "
							+ "where movfec.solicitacao = mov.solicitacao "
							+ "and movfec.dtIniMov > mov.dtIniMov "
							+ "and movfec.tipoMov <> 14) "
							+ "and  sol.dtReg >= to_date('"
							+ parametros.get("dtIni")
							+ " 00:00:00','dd/MM/yy hh24:mi:ss') "
							+ "and  sol.dtReg <= to_date('"
							+ parametros.get("dtFim")
							+ " 23:59:59','dd/MM/yy hh24:mi:ss') ").fetch();

			Iterator it = solicDoOrgao.listIterator();
			while (it.hasNext()) {

				Object[] obj = (Object[]) it.next();

				BigDecimal totalMinutosTrabalhados = calculaTotalMinutosTrabalhados(
						obj, dataDosFeriados);
				BigDecimal horasLiquidas = totalMinutosTrabalhados.divide(
						new BigDecimal("60"), 2, RoundingMode.HALF_UP);

				if (totalMinutosTrabalhados.doubleValue() <= 15) {
					cls_15m = cls_15m + 1;
				}
				if (totalMinutosTrabalhados.doubleValue() > 15
						&& horasLiquidas.doubleValue() <= 1) {
					cls_1 = cls_1 + 1;
				}
				if (horasLiquidas.doubleValue() > 1
						&& horasLiquidas.doubleValue() <= 3) {
					cls_3 = cls_3 + 1;
				}
				if (horasLiquidas.doubleValue() > 3
						&& horasLiquidas.doubleValue() <= 8) {
					cls_8 = cls_8 + 1;
				}
				if (horasLiquidas.doubleValue() > 8
						&& horasLiquidas.doubleValue() <= 15) {
					cls_15 = cls_15 + 1;
				}
			}

			d.add(cls_15m);
			d.add((cls_15m / percTotal));
			d.add(cls_1);
			d.add((cls_1 / percTotal));
			d.add(cls_3);
			d.add((cls_3 / percTotal));
			d.add(cls_8);
			d.add((cls_8 / percTotal));
			d.add(cls_15);
			d.add((cls_15 / percTotal));

		} else if (!(parametros.get("lotacao").equals(""))
				&& !(parametros.get("atendente") == null)) {

			String query = "";
			if (parametros.get("atendente") != null)
				query = "select idLotacao from DpLotacao where idLotacaoIni in (select idLotacaoIni "
						+ "from DpLotacao where idLotacao in ("
						+ parametros.get("atendente") + "))";
			else
				query = "select idLotacao from DpLotacao where idLotacaoIni in (select idLotacaoIni "
						+ "from DpLotacao where idLotacao in ("
						+ parametros.get("lotacao") + "))";

			StringBuilder listalotacoes = listaLotacoes(query);

			SortedSet<String> set = new TreeSet<String>();
			TreeMap<String, Double> map = new TreeMap<String, Double>();
			TreeMap<String, Long> maptotais = new TreeMap<String, Long>();

			/*
			 * percTotal = SrSolicitacao
			 		.find("select count(*) "
							+ "from SrSolicitacao sol, SrMovimentacao mov "
							+ "where sol.idSolicitacao = mov.solicitacao "
							+ "and mov.lotaAtendente in ("
							+ listalotacoes
							+ ") "
							+ "and mov.dtIniMov = (select max(movult.dtIniMov) "
							+ "from SrMovimentacao movult "
							+ "where movult.solicitacao = mov.solicitacao "
							+ " and movult.tipoMov = 7 "
							+ " and movult.lotaAtendente.orgaoUsuario = 3) "
							+ "and not exists (select 1 from SrMovimentacao movfec "
							+ "where movfec.solicitacao = mov.solicitacao "
							+ "and movfec.dtIniMov > mov.dtIniMov "
							+ "and movfec.tipoMov <> 14) "
							+ "and  sol.dtReg >= to_date('"
							+ parametros.get("dtIni")
							+ " 00:00:00','dd/MM/yy hh24:mi:ss') "
							+ "and  sol.dtReg <= to_date('"
							+ parametros.get("dtFim")
							+ " 23:59:59','dd/MM/yy hh24:mi:ss') ").first();
			 */
			
			List<SrSolicitacao> solicPorAtendente = SrSolicitacao
					.find("select mov.lotaAtendente, count(*) "
							+ "from SrSolicitacao sol, SrMovimentacao mov "
							+ "where sol.idSolicitacao = mov.solicitacao "
							+ "and mov.lotaAtendente in ("
							+ listalotacoes
							+ ") "
							+ "and mov.dtIniMov = (select max(movult.dtIniMov) "
							+ "from SrMovimentacao movult "
							+ "where movult.solicitacao = mov.solicitacao "
							+ " and movult.tipoMov = 7 "
							+ " and movult.lotaAtendente.orgaoUsuario = 3) "
							+ "and not exists (select 1 from SrMovimentacao movfec "
							+ "where movfec.solicitacao = mov.solicitacao "
							+ "and movfec.dtIniMov > mov.dtIniMov "
							+ "and movfec.tipoMov <> 14) "
							+ "and  sol.dtReg >= to_date('"
							+ parametros.get("dtIni")
							+ " 00:00:00','dd/MM/yy hh24:mi:ss') "
							+ "and  sol.dtReg <= to_date('"
							+ parametros.get("dtFim")
							+ " 23:59:59','dd/MM/yy hh24:mi:ss') ").fetch();

			Iterator itTotal = solicPorAtendente.listIterator();
			while (itTotal.hasNext()) {
				Object[] obj = (Object[]) itTotal.next();
				String lotaAtendente = (String) obj[0];
				Long totalPorAtendente = (Long) obj[1];
				set.add(lotaAtendente);
				maptotais.put(lotaAtendente, totalPorAtendente);
			}

			List<SrSolicitacao> solicDoOrgao = SrSolicitacao
					.find("select sol.idSolicitacao, sol.dtReg, mov.dtIniMov, mov.lotaAtendente "
							+ "from SrSolicitacao sol, SrMovimentacao mov "
							+ "where sol.idSolicitacao = mov.solicitacao "
							+ "and mov.lotaAtendente in ("
							+ listalotacoes
							+ ") "
							+ "and mov.dtIniMov = (select max(movult.dtIniMov) "
							+ "from SrMovimentacao movult "
							+ "where movult.solicitacao = mov.solicitacao "
							+ " and movult.tipoMov = 7 "
							+ " and movult.lotaAtendente.orgaoUsuario = 3) "
							+ "and not exists (select 1 from SrMovimentacao movfec "
							+ "where movfec.solicitacao = mov.solicitacao "
							+ "and movfec.dtIniMov > mov.dtIniMov "
							+ "and movfec.tipoMov <> 14) "
							+ "and  sol.dtReg >= to_date('"
							+ parametros.get("dtIni")
							+ " 00:00:00','dd/MM/yy hh24:mi:ss') "
							+ "and  sol.dtReg <= to_date('"
							+ parametros.get("dtFim")
							+ " 23:59:59','dd/MM/yy hh24:mi:ss') ").fetch();

			Iterator it = solicDoOrgao.listIterator();
			while (it.hasNext()) {

				Object[] obj = (Object[]) it.next();

				SrMovimentacao mov = (SrMovimentacao) (obj[4]);
				String lotaAtendente = mov.lotaAtendente.getNomeLotacao();
				
				BigDecimal totalMinutosTrabalhados = calculaTotalMinutosTrabalhados(
						obj, dataDosFeriados);
				BigDecimal horasLiquidas = totalMinutosTrabalhados.divide(
						new BigDecimal("60"), 2, RoundingMode.HALF_UP);

				if (totalMinutosTrabalhados.doubleValue() <= 15) {
					cls_15m = cls_15m + 1;
					map.put(chave(lotaAtendente, "15m"), cls_15m);
				}
				if (totalMinutosTrabalhados.doubleValue() > 15
						&& horasLiquidas.doubleValue() <= 1) {
					cls_1 = cls_1 + 1;
					map.put(chave(lotaAtendente, "1"), cls_1);
				}
				if (horasLiquidas.doubleValue() > 1
						&& horasLiquidas.doubleValue() <= 3) {
					cls_3 = cls_3 + 1;
					map.put(chave(lotaAtendente, "3"), cls_3);
				}
				if (horasLiquidas.doubleValue() > 3
						&& horasLiquidas.doubleValue() <= 8) {
					cls_8 = cls_8 + 1;
					map.put(chave(lotaAtendente, "8"), cls_8);
				}
				if (horasLiquidas.doubleValue() > 8
						&& horasLiquidas.doubleValue() <= 15) {
					cls_15 = cls_15 + 1;
					map.put(chave(lotaAtendente, "15"), cls_15);
				}
			}
			for (String s : set) {
				d.add(s);
				percTotal = maptotais.get(s);
				if (map.containsKey(chave(s, "15m"))) {
					cls_1 = map.get(chave(s, "15m"));
					d.add(cls_15m);
					d.add((cls_15m / percTotal));
				} else {
					d.add(0D);
					d.add(0D);
				}
				if (map.containsKey(chave(s, "1"))) {
					cls_1 = map.get(chave(s, "1"));
					d.add(cls_1);
					d.add((cls_1 / percTotal));
				} else {
					d.add(0D);
					d.add(0D);
				}
				if (map.containsKey(chave(s, "3"))) {
					cls_1 = map.get(chave(s, "3"));
					d.add(cls_3);
					d.add((cls_3 / percTotal));
				} else {
					d.add(0D);
					d.add(0D);
				}
				if (map.containsKey(chave(s, "8"))) {
					cls_8 = map.get(chave(s, "8"));
					d.add(cls_8);
					d.add((cls_8 / percTotal));
				} else {
					d.add(0D);
					d.add(0D);
				}
				if (map.containsKey(chave(s, "15"))) {
					cls_15 = map.get(chave(s, "15"));
					d.add(cls_15);
					d.add((cls_15 / percTotal));
				} else {
					d.add(0D);
					d.add(0D);
				}
			}
		}
		return d;
	}

	private StringBuilder listaLotacoes(String query) {
		List lotacoes = JPA.em().createQuery(query).getResultList();
		StringBuilder listalotacoes = new StringBuilder();
		for (int i = 0; i < lotacoes.size(); i++) {
			listalotacoes.append(lotacoes.get(i));
			if (i < (lotacoes.size() - 1))
				listalotacoes.append(",");
		}
		return listalotacoes;
	}

	private BigDecimal calculaTotalMinutosTrabalhados(Object[] obj,
			Set dataDosFeriados) {

		SrSolicitacao sol = SrSolicitacao.findById(obj[0]);
		DateTime startemp = new DateTime(obj[1]);
		DateTime endtemp = new DateTime(obj[2]);
		DateTime start1 = new DateTime();
		DateTime end1 = new DateTime();
		if (startemp.getHourOfDay() < 10) {
			start1 = new DateTime(startemp.getYear(),
					startemp.getMonthOfYear(), startemp.getDayOfMonth() + 1,
					10, 0, 0);
		} else if (startemp.getHourOfDay() >= 18) {
			start1 = new DateTime(startemp.getYear(),
					startemp.getMonthOfYear(), startemp.getDayOfMonth() + 1,
					10, 0, 0);
		} else
			start1 = startemp;
		if (endtemp.getHourOfDay() < 10) {
			end1 = new DateTime(endtemp.getYear(), endtemp.getMonthOfYear(),
					endtemp.getDayOfMonth() + 1, 10, 0, 0);
		} else if (endtemp.getHourOfDay() >= 18) {
			end1 = new DateTime(endtemp.getYear(), endtemp.getMonthOfYear(),
					endtemp.getDayOfMonth() + 1, 10, 0, 0);
		} else
			end1 = endtemp;
		int dias = Days.daysBetween(start1.toLocalDateTime(),
				end1.toLocalDateTime()).getDays();
		HolidayCalendar calendarioDeFeriados = new DefaultHolidayCalendar(
				dataDosFeriados);
		LocalDateKitCalculatorsFactory.getDefaultInstance().registerHolidays(
				"BR", calendarioDeFeriados);
		DateCalculator calendario = LocalDateKitCalculatorsFactory
				.getDefaultInstance().getDateCalculator("BR",
						HolidayHandlerType.FORWARD);
		BigDecimal totalMinutosTrabalhados = new BigDecimal(0.0);
		DateTime dataInicialTemporaria = new DateTime(start1);
		DateTime dataFinalTemporaria = new DateTime(end1);
		DateTime dtinitemp = new DateTime(start1);
		DateTime dtendtemp = new DateTime(end1);
		int i = 1;
		while (!dataInicialTemporaria.isAfter(dataFinalTemporaria)) {
			if (calendario.isNonWorkingDay(dataInicialTemporaria.toLocalDate())) {
			} else {
				if (i == dias) {
					dtinitemp = new DateTime(dataInicialTemporaria);
					dtendtemp = new DateTime(dataInicialTemporaria.getYear(),
							dataInicialTemporaria.getMonthOfYear(),
							dataInicialTemporaria.getDayOfMonth(), 18, 0, 0);
				} else if (i > dias) {
					dtinitemp = new DateTime(dataInicialTemporaria);
					dtendtemp = new DateTime(end1);
				} else {
					dtinitemp = new DateTime(dataInicialTemporaria);
					dtendtemp = new DateTime(dataInicialTemporaria.getYear(),
							dataInicialTemporaria.getMonthOfYear(),
							dataInicialTemporaria.getDayOfMonth(), 18, 0, 0);
				}
				new Period(dtinitemp, dtendtemp).getHours();
				totalMinutosTrabalhados = new BigDecimal(Minutes
						.minutesBetween(dtinitemp, dtendtemp).getMinutes())
						.add(totalMinutosTrabalhados);
			}
			dataInicialTemporaria = new DateTime(
					dataInicialTemporaria.getYear(),
					dataInicialTemporaria.getMonthOfYear(),
					dataInicialTemporaria.getDayOfMonth(), 9, 0, 0).plusDays(1);
			i++;
		}
		BigDecimal totalMinutosPendentes = calcularPendencias(sol);
		totalMinutosTrabalhados = totalMinutosTrabalhados
				.subtract(totalMinutosPendentes);
		return (totalMinutosTrabalhados);
	}

	private Set calculaFeriados() {
		List<CpOcorrenciaFeriado> ferjust = CpOcorrenciaFeriado.find(
				"select dtIniFeriado, dtFimFeriado from CpOcorrenciaFeriado")
				.fetch();
		Set dataDosFeriados = new HashSet();
		Iterator it1 = ferjust.listIterator();
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
		return dataDosFeriados;

	}

	@SuppressWarnings("unchecked")
	private BigDecimal calcularPendencias(SrSolicitacao sol) {

		BigDecimal totalMinutosPendentes = new BigDecimal(0.0);

		List<DateTime> listaPendentes = new ArrayList<DateTime>();

		new LinkedList<Object>();
		List<CpOcorrenciaFeriado> ferjust = CpOcorrenciaFeriado.find(
				"select dtIniFeriado, dtFimFeriado from CpOcorrenciaFeriado")
				.fetch();
		@SuppressWarnings("rawtypes")
		Set dataDosFeriados = new HashSet();
		Iterator it1 = ferjust.listIterator();
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

		for (SrMovimentacao mov : sol.getMovimentacaoSet()) {
			if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA) {
				listaPendentes.add(new DateTime(mov.dtIniMov));
			}
			if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FIM_PENDENCIA) {
				listaPendentes.add(new DateTime(mov.dtIniMov));
			}
		}

		for (int j = 0; j < listaPendentes.size(); j++) {
			DateTime startemp = new DateTime(listaPendentes.get(j + 1));
			DateTime endtemp = new DateTime(listaPendentes.get(j));
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
						endtemp.getMonthOfYear(), endtemp.getDayOfMonth() + 1,
						10, 0, 0);
			} else if (endtemp.getHourOfDay() >= 18) {
				end1 = new DateTime(endtemp.getYear(),
						endtemp.getMonthOfYear(), endtemp.getDayOfMonth() + 1,
						10, 0, 0);
			} else
				end1 = endtemp;
			int dias = Days.daysBetween(start1.toLocalDateTime(),
					end1.toLocalDateTime()).getDays();
			@SuppressWarnings("rawtypes")
			HolidayCalendar calendarioDeFeriados = new DefaultHolidayCalendar(
					dataDosFeriados);
			LocalDateKitCalculatorsFactory.getDefaultInstance()
					.registerHolidays("BR", calendarioDeFeriados);
			@SuppressWarnings("rawtypes")
			DateCalculator calendario = LocalDateKitCalculatorsFactory
					.getDefaultInstance().getDateCalculator("BR",
							HolidayHandlerType.FORWARD);
			DateTime dataInicialTemporaria = new DateTime(start1);
			DateTime dataFinalTemporaria = new DateTime(end1);
			DateTime dtinitemp = new DateTime(start1);
			DateTime dtendtemp = new DateTime(end1);
			int i = 1;
			while (!dataInicialTemporaria.isAfter(dataFinalTemporaria)) {
				if (calendario.isNonWorkingDay(dataInicialTemporaria
						.toLocalDate())) {
				} else {
					if (i == dias) {
						dtinitemp = new DateTime(dataInicialTemporaria);
						dtendtemp = new DateTime(
								dataInicialTemporaria.getYear(),
								dataInicialTemporaria.getMonthOfYear(),
								dataInicialTemporaria.getDayOfMonth(), 18, 0, 0);
					} else if (i > dias) {
						dtinitemp = new DateTime(dataInicialTemporaria);
						dtendtemp = new DateTime(end1);
					} else {
						dtinitemp = new DateTime(dataInicialTemporaria);
						dtendtemp = new DateTime(
								dataInicialTemporaria.getYear(),
								dataInicialTemporaria.getMonthOfYear(),
								dataInicialTemporaria.getDayOfMonth(), 18, 0, 0);
					}
					new Period(dtinitemp, dtendtemp).getHours();
					totalMinutosPendentes = new BigDecimal(Minutes
							.minutesBetween(dtinitemp, dtendtemp).getMinutes())
							.add(totalMinutosPendentes);
				}
				dataInicialTemporaria = new DateTime(
						dataInicialTemporaria.getYear(),
						dataInicialTemporaria.getMonthOfYear(),
						dataInicialTemporaria.getDayOfMonth(), 9, 0, 0)
						.plusDays(1);
				i++;
			}
			j = j + 1;
		}
		return totalMinutosPendentes;
	}

	private String chave(String local, String nivel) {
		return local + nivel;
	}

}
