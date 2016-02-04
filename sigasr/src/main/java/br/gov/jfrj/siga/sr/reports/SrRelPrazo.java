package br.gov.jfrj.siga.sr.reports;

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

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import ar.com.fdvs.dj.domain.constants.Font;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.Coluna;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.dp.CpOcorrenciaFeriado;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.model.SrMovimentacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.model.SrTipoMovimentacao;

public class SrRelPrazo extends RelatorioTemplate {

	@SuppressWarnings("rawtypes")
	public SrRelPrazo(Map parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("dtIni").equals("")) {
			throw new DJBuilderException(
					"Parâmetro data inicial não informado!");
		}
		if (parametros.get("dtFim").equals("")) {
			throw new DJBuilderException("Parâmetro data final não informado!");
		}
		if (parametros.get("local") == null) {
			throw new DJBuilderException("Parâmetro local não informado!");
		}
	}

	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException {

		this.setTitle("Relatório de Solicitações por Nível de Serviço");
		estiloTituloColuna.setFont(new Font(8, "Arial", true));

		this.addColuna("Local", 100, RelatorioRapido.DIREITA, true,
				String.class);
		Coluna cls_1 = this.addColuna("Até 1h", 11, RelatorioRapido.DIREITA,
				false, Double.class);
		cls_1.setPadrao("0");
		Coluna perc_cls_1 = this.addColuna("%Total", 11,
				RelatorioRapido.DIREITA, false, Double.class);
		perc_cls_1.setPadrao("0.00%");
		Coluna cls_2 = this.addColuna("Até 2hs", 11, RelatorioRapido.DIREITA,
				false, Double.class);
		cls_2.setPadrao("0");
		Coluna perc_cls_2 = this.addColuna("%Total", 11,
				RelatorioRapido.DIREITA, false, Double.class);
		perc_cls_2.setPadrao("0.00%");
		Coluna cls_4 = this.addColuna("Até 4hs", 11, RelatorioRapido.DIREITA,
				false, Double.class);
		cls_4.setPadrao("0");
		Coluna perc_cls_4 = this.addColuna("%Total", 11,
				RelatorioRapido.DIREITA, false, Double.class);
		perc_cls_4.setPadrao("0.00%");
		Coluna cls_12 = this.addColuna("Até 12hs", 11, RelatorioRapido.DIREITA,
				false, Double.class);
		cls_12.setPadrao("0");
		Coluna perc_cls_12 = this.addColuna("%Total", 11,
				RelatorioRapido.DIREITA, false, Double.class);
		perc_cls_12.setPadrao("0.00%");
		Coluna cls_24 = this.addColuna("Até 24 hs", 11,
				RelatorioRapido.DIREITA, false, Double.class);
		cls_24.setPadrao("0");
		Coluna perc_cls_24 = this.addColuna("%Total", 11,
				RelatorioRapido.DIREITA, false, Double.class);
		perc_cls_24.setPadrao("0.00%");
		Coluna cls_ac24 = this.addColuna("Acima de 24 hs", 12,
				RelatorioRapido.DIREITA, false, Double.class);
		cls_ac24.setPadrao("0");
		Coluna perc_cls_ac24 = this.addColuna("%Total", 12,
				RelatorioRapido.DIREITA, false, Double.class);
		perc_cls_ac24.setPadrao("0.00%");
		return this;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Collection processarDados() throws Exception {

		String local = new String();
		List<Object> d = new LinkedList<Object>();
		double cls_1 = 0;
		double cls_2 = 0;
		double cls_4 = 0;
		double cls_12 = 0;
		double cls_24 = 0;
		double cls_ac24 = 0;
		Long percTotal = (long) 0.;

		Set dataDosFeriados = calculaFeriados();

		if ((parametros.get("lotacao").equals(""))
				&& (parametros.get("atendente") == null)) {
			if (parametros.get("local").equals("0")) {
				SortedSet<String> set = new TreeSet<String>();
				TreeMap<String, Double> map = new TreeMap<String, Double>();
				TreeMap<String, Long> maptotais = new TreeMap<String, Long>();

				List<SrSolicitacao> listaTotal = SrSolicitacao.AR
						.find("select sol.local.nomeComplexo, count(*) "
								+ "from SrSolicitacao sol, SrMovimentacao mov "
								+ "where sol.idSolicitacao = mov.solicitacao "
								+ "and mov.dtIniMov = (select max(movult.dtIniMov) "
								+ "from SrMovimentacao movult "
								+ "where movult.solicitacao = mov.solicitacao "
								+ " and movult.tipoMov = 7 "
								+ " and movult.lotaAtendente.orgaoUsuario = 1) "
								+ "and not exists (select 1 from SrMovimentacao movfec "
								+ "where movfec.solicitacao = mov.solicitacao "
								+ "and movfec.dtIniMov > mov.dtIniMov "
								+ "and movfec.tipoMov <> 14) "
								+ "and  sol.dtReg >= to_date('"
								+ parametros.get("dtIni")
								+ " 00:00:00','dd/MM/yy hh24:mi:ss') "
								+ "and  sol.dtReg <= to_date('"
								+ parametros.get("dtFim")
								+ " 23:59:59','dd/MM/yy hh24:mi:ss') "
								+ "group by sol.local.nomeComplexo").fetch();

				Iterator itTotal = listaTotal.listIterator();
				while (itTotal.hasNext()) {
					Object[] obj = (Object[]) itTotal.next();
					String complexo = (String) obj[0];
					Long total_local = (Long) obj[1];
					set.add(complexo);
					maptotais.put(complexo, total_local);
				}

				for (String locais : set) {
					cls_1 = 0;
					cls_2 = 0;
					cls_4 = 0;
					cls_12 = 0;
					cls_24 = 0;
					cls_ac24 = 0;
					List<SrSolicitacao> solicDoComplexo = SrSolicitacao.AR
							.find("select sol.local.nomeComplexo, sol.idSolicitacao, sol.dtReg, mov.dtIniMov "
									+ "from SrSolicitacao sol, SrMovimentacao mov "
									+ "where sol.idSolicitacao = mov.solicitacao "
									+ "and sol.local.nomeComplexo = '"
									+ locais.toString()
									+ "' "
									+ "and mov.dtIniMov = (select max(movult.dtIniMov) "
									+ "from SrMovimentacao movult "
									+ "where movult.solicitacao = mov.solicitacao "
									+ " and movult.tipoMov = 7 "
									+ " and movult.lotaAtendente.orgaoUsuario = 1) "
									+ "and not exists (select 1 from SrMovimentacao movfec "
									+ "where movfec.solicitacao = mov.solicitacao "
									+ "and movfec.dtIniMov > mov.dtIniMov "
									+ "and movfec.tipoMov <> 14) "
									+ "and  sol.dtReg >= to_date('"
									+ parametros.get("dtIni")
									+ " 00:00:00','dd/MM/yy hh24:mi:ss') "
									+ "and  sol.dtReg <= to_date('"
									+ parametros.get("dtFim")
									+ " 23:59:59','dd/MM/yy hh24:mi:ss') ")
							.fetch();
					
					Iterator it = solicDoComplexo.listIterator();
					while (it.hasNext()) {
						Object[] obj = (Object[]) it.next();
						local = (String) obj[0];
						set.add(local);
						SrSolicitacao sol = SrSolicitacao.AR.findById(obj[1]);
						DateTime startemp = new DateTime(obj[2]);
						DateTime endtemp = new DateTime(obj[3]);
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
						int dias = Days.daysBetween(start1.toLocalDateTime(),
								end1.toLocalDateTime()).getDays();
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
						DateTime dataInicialTemporaria = new DateTime(start1);
						DateTime dataFinalTemporaria = new DateTime(end1);
						DateTime dtinitemp = new DateTime(start1);
						DateTime dtendtemp = new DateTime(end1);
						int i = 1;
						while (!dataInicialTemporaria
								.isAfter(dataFinalTemporaria)) {
							if (calendario
									.isNonWorkingDay(dataInicialTemporaria
											.toLocalDate())) {
								diasNaoUteis++;
							} else {
								if (i == dias) {
									dtinitemp = new DateTime(
											dataInicialTemporaria);
									dtendtemp = new DateTime(
											dataInicialTemporaria.getYear(),
											dataInicialTemporaria
													.getMonthOfYear(),
											dataInicialTemporaria
													.getDayOfMonth(), 18, 0, 0);
								} else if (i > dias) {
									dtinitemp = new DateTime(
											dataInicialTemporaria);
									dtendtemp = new DateTime(end1);
								} else {
									dtinitemp = new DateTime(
											dataInicialTemporaria);
									dtendtemp = new DateTime(
											dataInicialTemporaria.getYear(),
											dataInicialTemporaria
													.getMonthOfYear(),
											dataInicialTemporaria
													.getDayOfMonth(), 18, 0, 0);
								}
								totalHorasTrabalhadas += new Period(dtinitemp,
										dtendtemp).getHours();
								totalMinutosTrabalhados = new BigDecimal(
										Minutes.minutesBetween(dtinitemp,
												dtendtemp).getMinutes())
										.add(totalMinutosTrabalhados);
							}
							dataInicialTemporaria = new DateTime(
									dataInicialTemporaria.getYear(),
									dataInicialTemporaria.getMonthOfYear(),
									dataInicialTemporaria.getDayOfMonth(), 9,
									0, 0).plusDays(1);
							i++;
						}
						BigDecimal totalMinutosPendentes = calcularPendencias(sol);
						totalMinutosTrabalhados = totalMinutosTrabalhados
								.subtract(totalMinutosPendentes);
						BigDecimal horasLiquidas = totalMinutosTrabalhados
								.divide(new BigDecimal("60"), 2,
										RoundingMode.HALF_UP);
						if (horasLiquidas.doubleValue() <= 1) {
							cls_1 = cls_1 + 1;
							map.put(chave(local, "1"), cls_1);
						}
						if (horasLiquidas.doubleValue() > 1
								&& horasLiquidas.doubleValue() <= 2) {
							cls_2 = cls_2 + 1;
							map.put(chave(local, "2"), cls_2);
						}
						if (horasLiquidas.doubleValue() > 2
								&& horasLiquidas.doubleValue() <= 4) {
							cls_4 = cls_4 + 1;
							map.put(chave(local, "4"), cls_4);
						}
						if (horasLiquidas.doubleValue() > 4
								&& horasLiquidas.doubleValue() <= 12) {
							cls_12 = cls_12 + 1;
							map.put(chave(local, "12"), cls_12);
						}
						if (horasLiquidas.doubleValue() > 12
								&& horasLiquidas.doubleValue() <= 24) {
							cls_24 = cls_24 + 1;
							map.put(chave(local, "24"), cls_24);
						}
						if (horasLiquidas.doubleValue() > 24) {
							cls_ac24 = cls_ac24 + 1;
							map.put(chave(local, "ac24"), cls_ac24);
						}
					}
				}
				for (String s : set) {
					d.add(s);
					percTotal = maptotais.get(s);
					if (map.containsKey(chave(s, "1"))) {
						cls_1 = map.get(chave(s, "1"));
						d.add(cls_1);
						d.add((cls_1 / percTotal));
					} else {
						d.add(0D);
						d.add(0D);
					}

					if (map.containsKey(chave(s, "2"))) {
						cls_2 = map.get(chave(s, "2"));
						d.add(cls_2);
						d.add((cls_2 / percTotal));
					} else {
						d.add(0D);
						d.add(0D);
					}

					if (map.containsKey(chave(s, "4"))) {
						cls_4 = map.get(chave(s, "4"));
						d.add(cls_4);
						d.add((cls_4 / percTotal));
					} else {
						d.add(0D);
						d.add(0D);
					}

					if (map.containsKey(chave(s, "12"))) {
						cls_12 = map.get(chave(s, "12"));
						d.add(cls_12);
						d.add((cls_12 / percTotal));
					} else {
						d.add(0D);
						d.add(0D);
					}

					if (map.containsKey(chave(s, "24"))) {
						cls_24 = map.get(chave(s, "24"));
						d.add(cls_24);
						d.add((cls_24 / percTotal));
					} else {
						d.add(0D);
						d.add(0D);
					}

					if (map.containsKey(chave(s, "ac24"))) {
						cls_ac24 = map.get(chave(s, "ac24"));
						d.add(cls_ac24);
						d.add((cls_ac24 / percTotal));
					} else {
						d.add(0D);
						d.add(0D);
					}
				} // final do for para o conjunto
			} else { // else do if parametros.get("local").equals("0")
				percTotal = SrSolicitacao.AR
						.find("select count(*) "
								+ "from SrSolicitacao sol, SrMovimentacao mov "
								+ "where sol.idSolicitacao = mov.solicitacao "
								+ "and sol.local = '"
								+ parametros.get("local")
								+ "' "
								+ "and mov.dtIniMov = (select max(movult.dtIniMov) "
								+ "from SrMovimentacao movult "
								+ "where movult.solicitacao = mov.solicitacao "
								+ " and movult.tipoMov = 7 "
								+ " and movult.lotaAtendente.orgaoUsuario = 1) "
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

				List<SrSolicitacao> lista = SrSolicitacao.AR
						.find("select sol.local.nomeComplexo, sol.dtReg, mov.dtIniMov,sol.idSolicitacao "
								+ "from SrSolicitacao sol, SrMovimentacao mov "
								+ "where sol.idSolicitacao = mov.solicitacao "
								+ "and sol.local = "
								+ parametros.get("local")
								+ " "
								+ "and mov.dtIniMov = (select max(movult.dtIniMov) "
								+ "from SrMovimentacao movult "
								+ "where movult.solicitacao = mov.solicitacao "
								+ " and movult.tipoMov = 7"
								+ " and movult.lotaAtendente.orgaoUsuario = 1) "
								+ "and not exists (select 1 from SrMovimentacao movfec "
								+ "where movfec.solicitacao = mov.solicitacao "
								+ "and movfec.dtIniMov > mov.dtIniMov "
								+ "and movfec.tipoMov <> 14) "
								+ "and sol.orgaoUsuario = 1 "
								+ "and  sol.dtReg >= to_date('"
								+ parametros.get("dtIni")
								+ " 00:00:00','dd/MM/yy hh24:mi:ss') "
								+ "and  sol.dtReg <= to_date('"
								+ parametros.get("dtFim")
								+ " 23:59:59','dd/MM/yy hh24:mi:ss') ").fetch();
				Iterator it = lista.listIterator();
				while (it.hasNext()) {
					Object[] obj = (Object[]) it.next();
					local = (String) obj[0];
					DateTime startemp = new DateTime(obj[1]);
					DateTime endtemp = new DateTime(obj[2]);
					SrSolicitacao sol = SrSolicitacao.AR.findById(obj[3]);
					DateTime start1 = new DateTime();
					DateTime end1 = new DateTime();
					DateTime startp = new DateTime();
					DateTime endp = new DateTime();
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
					int dias = Days.daysBetween(start1.toLocalDateTime(),
							end1.toLocalDateTime()).getDays();
					HolidayCalendar calendarioDeFeriados = new DefaultHolidayCalendar(
							dataDosFeriados);
					LocalDateKitCalculatorsFactory.getDefaultInstance()
							.registerHolidays("BR", calendarioDeFeriados);
					DateCalculator calendario = LocalDateKitCalculatorsFactory
							.getDefaultInstance().getDateCalculator("BR",
									HolidayHandlerType.FORWARD);
					int diasNaoUteis = 0;
					// int totalHorasTrabalhadas = 0;
					// int totalHorasPendentes = 0;
					BigDecimal totalMinutosTrabalhados = new BigDecimal(0.0);
					DateTime dataInicialTemporaria = new DateTime(start1);
					DateTime dataFinalTemporaria = new DateTime(end1);
					DateTime dtinitemp = new DateTime(start1);
					DateTime dtendtemp = new DateTime(end1);
					int i = 1;
					while (!dataInicialTemporaria.isAfter(dataFinalTemporaria)) {
						if (calendario.isNonWorkingDay(dataInicialTemporaria
								.toLocalDate())) {
							diasNaoUteis++;
						} else {
							if (i == dias) {
								dtinitemp = new DateTime(dataInicialTemporaria);
								dtendtemp = new DateTime(
										dataInicialTemporaria.getYear(),
										dataInicialTemporaria.getMonthOfYear(),
										dataInicialTemporaria.getDayOfMonth(),
										18, 0, 0);
							} else if (i > dias) {
								dtinitemp = new DateTime(dataInicialTemporaria);
								dtendtemp = new DateTime(end1);
							} else {
								dtinitemp = new DateTime(dataInicialTemporaria);
								dtendtemp = new DateTime(
										dataInicialTemporaria.getYear(),
										dataInicialTemporaria.getMonthOfYear(),
										dataInicialTemporaria.getDayOfMonth(),
										18, 0, 0);
							}
							// totalHorasTrabalhadas += new Period(dtinitemp,
							// dtendtemp).getHours();
							totalMinutosTrabalhados = new BigDecimal(Minutes
									.minutesBetween(dtinitemp, dtendtemp)
									.getMinutes()).add(totalMinutosTrabalhados);
						}
						dataInicialTemporaria = new DateTime(
								dataInicialTemporaria.getYear(),
								dataInicialTemporaria.getMonthOfYear(),
								dataInicialTemporaria.getDayOfMonth(), 9, 0, 0)
								.plusDays(1);
						i++;
					}
					BigDecimal totalMinutosPendentes = calcularPendencias(sol);
					totalMinutosTrabalhados = totalMinutosTrabalhados
							.subtract(totalMinutosPendentes);
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
				d.add(local);
				d.add(cls_1);
				d.add((cls_1 / percTotal));
				d.add(cls_2);
				d.add((cls_2 / percTotal));
				d.add(cls_4);
				d.add((cls_4 / percTotal));
				d.add(cls_12);
				d.add((cls_12 / percTotal));
				d.add(cls_24);
				d.add((cls_24 / percTotal));
				d.add(cls_ac24);
				d.add((cls_ac24 / percTotal));
			} // fim do else referente ao if parametros.get("local").equals("0")
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
			List lotacoes = ContextoPersistencia.em().createQuery(query).getResultList();
			StringBuilder listalotacoes = new StringBuilder();
			for (int i = 0; i < lotacoes.size(); i++) {
				listalotacoes.append(lotacoes.get(i));
				if (i < (lotacoes.size() - 1))
					listalotacoes.append(",");
			}
			if (parametros.get("local").equals("0")) {
				SortedSet<String> set = new TreeSet<String>();
				TreeMap<String, Double> map = new TreeMap<String, Double>();
				TreeMap<String, Long> maptotais = new TreeMap<String, Long>();

				List<SrSolicitacao> listaTotal = SrSolicitacao.AR
						.find("select sol.local.nomeComplexo, count(*) "
								+ "from SrSolicitacao sol, SrMovimentacao mov "
								+ "where sol.idSolicitacao = mov.solicitacao "
								+ "and mov.lotaAtendente in ("
								+ listalotacoes
								+ ") "
								+ "and mov.dtIniMov = (select max(movult.dtIniMov) "
								+ "from SrMovimentacao movult "
								+ "where movult.solicitacao = mov.solicitacao "
								+ " and movult.tipoMov = 7 "
								+ " and movult.lotaAtendente.orgaoUsuario = 1) "
								+ "and not exists (select 1 from SrMovimentacao movfec "
								+ "where movfec.solicitacao = mov.solicitacao "
								+ "and movfec.dtIniMov > mov.dtIniMov "
								+ "and movfec.tipoMov <> 14) "
								+ "and  sol.dtReg >= to_date('"
								+ parametros.get("dtIni")
								+ " 00:00:00','dd/MM/yy hh24:mi:ss') "
								+ "and  sol.dtReg <= to_date('"
								+ parametros.get("dtFim")
								+ " 23:59:59','dd/MM/yy hh24:mi:ss') "
								+ "group by sol.local.nomeComplexo").fetch();

				Iterator itTotal = listaTotal.listIterator();
				while (itTotal.hasNext()) {
					Object[] obj = (Object[]) itTotal.next();
					String complexo = (String) obj[0];
					Long total_local = (Long) obj[1];
					set.add(complexo);
					maptotais.put(complexo, total_local);
				}

				for (String locais : set) {
					cls_1 = 0;
					cls_2 = 0;
					cls_4 = 0;
					cls_12 = 0;
					cls_24 = 0;
					cls_ac24 = 0;
					List<SrSolicitacao> solicDoComplexo = SrSolicitacao.AR
							.find("select sol.local.nomeComplexo, sol.idSolicitacao, sol.dtReg, mov.dtIniMov "
									+ "from SrSolicitacao sol, SrMovimentacao mov "
									+ "where sol.idSolicitacao = mov.solicitacao "
									+ "and mov.lotaAtendente in ("
									+ listalotacoes
									+ ") "
									+ "and sol.local.nomeComplexo = '"
									+ locais.toString()
									+ "' "
									+ "and mov.dtIniMov = (select max(movult.dtIniMov) "
									+ "from SrMovimentacao movult "
									+ "where movult.solicitacao = mov.solicitacao "
									+ " and movult.tipoMov = 7 "
									+ " and movult.lotaAtendente.orgaoUsuario = 1) "
									+ "and not exists (select 1 from SrMovimentacao movfec "
									+ "where movfec.solicitacao = mov.solicitacao "
									+ "and movfec.dtIniMov > mov.dtIniMov "
									+ "and movfec.tipoMov <> 14) "
									+ "and  sol.dtReg >= to_date('"
									+ parametros.get("dtIni")
									+ " 00:00:00','dd/MM/yy hh24:mi:ss') "
									+ "and  sol.dtReg <= to_date('"
									+ parametros.get("dtFim")
									+ " 23:59:59','dd/MM/yy hh24:mi:ss') ")
							.fetch();
					Iterator it = solicDoComplexo.listIterator();
					while (it.hasNext()) {
						Object[] obj = (Object[]) it.next();
						local = (String) obj[0];
						set.add(local);
						SrSolicitacao sol = SrSolicitacao.AR.findById(obj[1]);
						DateTime startemp = new DateTime(obj[2]);
						DateTime endtemp = new DateTime(obj[3]);
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
						int dias = Days.daysBetween(start1.toLocalDateTime(),
								end1.toLocalDateTime()).getDays();
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
						DateTime dataInicialTemporaria = new DateTime(start1);
						DateTime dataFinalTemporaria = new DateTime(end1);
						DateTime dtinitemp = new DateTime(start1);
						DateTime dtendtemp = new DateTime(end1);
						int i = 1;
						while (!dataInicialTemporaria
								.isAfter(dataFinalTemporaria)) {
							if (calendario
									.isNonWorkingDay(dataInicialTemporaria
											.toLocalDate())) {
								diasNaoUteis++;
							} else {
								if (i == dias) {
									dtinitemp = new DateTime(
											dataInicialTemporaria);
									dtendtemp = new DateTime(
											dataInicialTemporaria.getYear(),
											dataInicialTemporaria
													.getMonthOfYear(),
											dataInicialTemporaria
													.getDayOfMonth(), 18, 0, 0);
								} else if (i > dias) {
									dtinitemp = new DateTime(
											dataInicialTemporaria);
									dtendtemp = new DateTime(end1);
								} else {
									dtinitemp = new DateTime(
											dataInicialTemporaria);
									dtendtemp = new DateTime(
											dataInicialTemporaria.getYear(),
											dataInicialTemporaria
													.getMonthOfYear(),
											dataInicialTemporaria
													.getDayOfMonth(), 18, 0, 0);
								}
								totalHorasTrabalhadas += new Period(dtinitemp,
										dtendtemp).getHours();
								totalMinutosTrabalhados = new BigDecimal(
										Minutes.minutesBetween(dtinitemp,
												dtendtemp).getMinutes())
										.add(totalMinutosTrabalhados);
							}
							dataInicialTemporaria = new DateTime(
									dataInicialTemporaria.getYear(),
									dataInicialTemporaria.getMonthOfYear(),
									dataInicialTemporaria.getDayOfMonth(), 9,
									0, 0).plusDays(1);
							i++;
						}
						BigDecimal totalMinutosPendentes = calcularPendencias(sol);
						totalMinutosTrabalhados = totalMinutosTrabalhados
								.subtract(totalMinutosPendentes);
						BigDecimal horasLiquidas = totalMinutosTrabalhados
								.divide(new BigDecimal("60"), 2,
										RoundingMode.HALF_UP);
						if (horasLiquidas.doubleValue() <= 1) {
							cls_1 = cls_1 + 1;
							map.put(chave(local, "1"), cls_1);
						}
						if (horasLiquidas.doubleValue() > 1
								&& horasLiquidas.doubleValue() <= 2) {
							cls_2 = cls_2 + 1;
							map.put(chave(local, "2"), cls_2);
						}
						if (horasLiquidas.doubleValue() > 2
								&& horasLiquidas.doubleValue() <= 4) {
							cls_4 = cls_4 + 1;
							map.put(chave(local, "4"), cls_4);
						}
						if (horasLiquidas.doubleValue() > 4
								&& horasLiquidas.doubleValue() <= 12) {
							cls_12 = cls_12 + 1;
							map.put(chave(local, "12"), cls_12);
						}
						if (horasLiquidas.doubleValue() > 12
								&& horasLiquidas.doubleValue() <= 24) {
							cls_24 = cls_24 + 1;
							map.put(chave(local, "24"), cls_24);
						}
						if (horasLiquidas.doubleValue() > 24) {
							cls_ac24 = cls_ac24 + 1;
							map.put(chave(local, "ac24"), cls_ac24);
						}
					}
				}
				for (String s : set) {
					d.add(s);
					percTotal = maptotais.get(s);
					if (map.containsKey(chave(s, "1"))) {
						cls_1 = map.get(chave(s, "1"));
						d.add(cls_1);
						d.add((cls_1 / percTotal));
					} else {
						d.add(0D);
						d.add(0D);
					}

					if (map.containsKey(chave(s, "2"))) {
						cls_2 = map.get(chave(s, "2"));
						d.add(cls_2);
						d.add((cls_2 / percTotal));
					} else {
						d.add(0D);
						d.add(0D);
					}

					if (map.containsKey(chave(s, "4"))) {
						cls_4 = map.get(chave(s, "4"));
						d.add(cls_4);
						d.add((cls_4 / percTotal));
					} else {
						d.add(0D);
						d.add(0D);
					}

					if (map.containsKey(chave(s, "12"))) {
						cls_12 = map.get(chave(s, "12"));
						d.add(cls_12);
						d.add((cls_12 / percTotal));
					} else {
						d.add(0D);
						d.add(0D);
					}

					if (map.containsKey(chave(s, "24"))) {
						cls_24 = map.get(chave(s, "24"));
						d.add(cls_24);
						d.add((cls_24 / percTotal));
					} else {
						d.add(0D);
						d.add(0D);
					}

					if (map.containsKey(chave(s, "ac24"))) {
						cls_ac24 = map.get(chave(s, "ac24"));
						d.add(cls_ac24);
						d.add((cls_ac24 / percTotal));
					} else {
						d.add(0D);
						d.add(0D);
					}
				} // final do for para o conjunto
			} else {

				SortedSet<String> set = new TreeSet<String>();
				TreeMap<String, Double> map = new TreeMap<String, Double>();
				TreeMap<String, Long> maptotais = new TreeMap<String, Long>();

				percTotal = SrSolicitacao.AR
						.find("select count(*) "
								+ "from SrSolicitacao sol, SrMovimentacao mov "
								+ "where sol.idSolicitacao = mov.solicitacao "
								+ "and sol.local = "
								+ parametros.get("local")
								+ " "
								+ "and mov.lotaAtendente in ("
								+ listalotacoes
								+ ") "
								+ "and mov.dtIniMov = (select max(movult.dtIniMov) "
								+ "from SrMovimentacao movult "
								+ "where movult.solicitacao = mov.solicitacao "
								+ " and movult.tipoMov = 7 "
								+ " and movult.lotaAtendente.orgaoUsuario = 1) "
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

				List<SrSolicitacao> listaTotal = SrSolicitacao.AR
						.find("select mov.lotaAtendente.siglaLotacao, count(*) "
								+ "from SrSolicitacao sol, SrMovimentacao mov "
								+ "where sol.idSolicitacao = mov.solicitacao "
								+ "and sol.local = "
								+ parametros.get("local")
								+ " "
								+ "and mov.lotaAtendente in ("
								+ listalotacoes
								+ ") "
								+ "and mov.dtIniMov = (select max(movult.dtIniMov) "
								+ "from SrMovimentacao movult "
								+ "where movult.solicitacao = mov.solicitacao "
								+ " and movult.tipoMov = 7 "
								+ " and movult.lotaAtendente.orgaoUsuario = 1) "
								+ "and not exists (select 1 from SrMovimentacao movfec "
								+ "where movfec.solicitacao = mov.solicitacao "
								+ "and movfec.dtIniMov > mov.dtIniMov "
								+ "and movfec.tipoMov <> 14) "
								+ "and  sol.dtReg >= to_date('"
								+ parametros.get("dtIni")
								+ " 00:00:00','dd/MM/yy hh24:mi:ss') "
								+ "and  sol.dtReg <= to_date('"
								+ parametros.get("dtFim")
								+ " 23:59:59','dd/MM/yy hh24:mi:ss') "
								+ "group by mov.lotaAtendente.siglaLotacao")
						.fetch();

				Iterator itTotal = listaTotal.listIterator();
				while (itTotal.hasNext()) {
					Object[] obj = (Object[]) itTotal.next();
					local = (String) obj[0];
					Long total_local = (Long) obj[1];
					set.add(local);
					maptotais.put(local, total_local);
				}

				for (String locais : set) {
					cls_1 = 0;
					cls_2 = 0;
					cls_4 = 0;
					cls_12 = 0;
					cls_24 = 0;
					cls_ac24 = 0;
					List<SrSolicitacao> solicDoComplexo = SrSolicitacao.AR
							.find("select mov.lotaAtendente.siglaLotacao, sol.idSolicitacao, sol.dtReg, mov.dtIniMov "
									+ "from SrSolicitacao sol, SrMovimentacao mov "
									+ "where sol.idSolicitacao = mov.solicitacao "
									+ "and mov.lotaAtendente in ("
									+ listalotacoes
									+ ") "
									+ "and sol.local = "
									+ parametros.get("local")
									+ " "
									+ "and mov.dtIniMov = (select max(movult.dtIniMov) "
									+ "from SrMovimentacao movult "
									+ "where movult.solicitacao = mov.solicitacao "
									+ " and movult.tipoMov = 7 "
									+ " and movult.lotaAtendente.orgaoUsuario = 1) "
									+ "and not exists (select 1 from SrMovimentacao movfec "
									+ "where movfec.solicitacao = mov.solicitacao "
									+ "and movfec.dtIniMov > mov.dtIniMov "
									+ "and movfec.tipoMov <> 14) "
									+ "and  sol.dtReg >= to_date('"
									+ parametros.get("dtIni")
									+ " 00:00:00','dd/MM/yy hh24:mi:ss') "
									+ "and  sol.dtReg <= to_date('"
									+ parametros.get("dtFim")
									+ " 23:59:59','dd/MM/yy hh24:mi:ss') ")
							.fetch();
					Iterator it = solicDoComplexo.listIterator();
					while (it.hasNext()) {
						Object[] obj = (Object[]) it.next();
						local = (String) obj[0];
						set.add(local);
						SrSolicitacao sol = SrSolicitacao.AR.findById(obj[1]);
						DateTime startemp = new DateTime(obj[2]);
						DateTime endtemp = new DateTime(obj[3]);
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
						int dias = Days.daysBetween(start1.toLocalDateTime(),
								end1.toLocalDateTime()).getDays();
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
						DateTime dataInicialTemporaria = new DateTime(start1);
						DateTime dataFinalTemporaria = new DateTime(end1);
						DateTime dtinitemp = new DateTime(start1);
						DateTime dtendtemp = new DateTime(end1);
						int i = 1;
						while (!dataInicialTemporaria
								.isAfter(dataFinalTemporaria)) {
							if (calendario
									.isNonWorkingDay(dataInicialTemporaria
											.toLocalDate())) {
								diasNaoUteis++;
							} else {
								if (i == dias) {
									dtinitemp = new DateTime(
											dataInicialTemporaria);
									dtendtemp = new DateTime(
											dataInicialTemporaria.getYear(),
											dataInicialTemporaria
													.getMonthOfYear(),
											dataInicialTemporaria
													.getDayOfMonth(), 18, 0, 0);
								} else if (i > dias) {
									dtinitemp = new DateTime(
											dataInicialTemporaria);
									dtendtemp = new DateTime(end1);
								} else {
									dtinitemp = new DateTime(
											dataInicialTemporaria);
									dtendtemp = new DateTime(
											dataInicialTemporaria.getYear(),
											dataInicialTemporaria
													.getMonthOfYear(),
											dataInicialTemporaria
													.getDayOfMonth(), 18, 0, 0);
								}
								totalHorasTrabalhadas += new Period(dtinitemp,
										dtendtemp).getHours();
								totalMinutosTrabalhados = new BigDecimal(
										Minutes.minutesBetween(dtinitemp,
												dtendtemp).getMinutes())
										.add(totalMinutosTrabalhados);
							}
							dataInicialTemporaria = new DateTime(
									dataInicialTemporaria.getYear(),
									dataInicialTemporaria.getMonthOfYear(),
									dataInicialTemporaria.getDayOfMonth(), 9,
									0, 0).plusDays(1);
							i++;
						}
						BigDecimal totalMinutosPendentes = calcularPendencias(sol);
						totalMinutosTrabalhados = totalMinutosTrabalhados
								.subtract(totalMinutosPendentes);
						BigDecimal horasLiquidas = totalMinutosTrabalhados
								.divide(new BigDecimal("60"), 2,
										RoundingMode.HALF_UP);
						if (horasLiquidas.doubleValue() <= 1) {
							cls_1 = cls_1 + 1;
							map.put(chave(local, "1"), cls_1);
						}
						if (horasLiquidas.doubleValue() > 1
								&& horasLiquidas.doubleValue() <= 2) {
							cls_2 = cls_2 + 1;
							map.put(chave(local, "2"), cls_2);
						}
						if (horasLiquidas.doubleValue() > 2
								&& horasLiquidas.doubleValue() <= 4) {
							cls_4 = cls_4 + 1;
							map.put(chave(local, "4"), cls_4);
						}
						if (horasLiquidas.doubleValue() > 4
								&& horasLiquidas.doubleValue() <= 12) {
							cls_12 = cls_12 + 1;
							map.put(chave(local, "12"), cls_12);
						}
						if (horasLiquidas.doubleValue() > 12
								&& horasLiquidas.doubleValue() <= 24) {
							cls_24 = cls_24 + 1;
							map.put(chave(local, "24"), cls_24);
						}
						if (horasLiquidas.doubleValue() > 24) {
							cls_ac24 = cls_ac24 + 1;
							map.put(chave(local, "ac24"), cls_ac24);
						}
					}
				} // for do set [SESIA, SESUT]
				for (String s : set) {
					d.add(s);
					percTotal = maptotais.get(s);
					if (map.containsKey(chave(s, "1"))) {
						cls_1 = map.get(chave(s, "1"));
						d.add(cls_1);
						d.add((cls_1 / percTotal));
					} else {
						d.add(0D);
						d.add(0D);
					}

					if (map.containsKey(chave(s, "2"))) {
						cls_2 = map.get(chave(s, "2"));
						d.add(cls_2);
						d.add((cls_2 / percTotal));
					} else {
						d.add(0D);
						d.add(0D);
					}

					if (map.containsKey(chave(s, "4"))) {
						cls_4 = map.get(chave(s, "4"));
						d.add(cls_4);
						d.add((cls_4 / percTotal));
					} else {
						d.add(0D);
						d.add(0D);
					}

					if (map.containsKey(chave(s, "12"))) {
						cls_12 = map.get(chave(s, "12"));
						d.add(cls_12);
						d.add((cls_12 / percTotal));
					} else {
						d.add(0D);
						d.add(0D);
					}

					if (map.containsKey(chave(s, "24"))) {
						cls_24 = map.get(chave(s, "24"));
						d.add(cls_24);
						d.add((cls_24 / percTotal));
					} else {
						d.add(0D);
						d.add(0D);
					}

					if (map.containsKey(chave(s, "ac24"))) {
						cls_ac24 = map.get(chave(s, "ac24"));
						d.add(cls_ac24);
						d.add((cls_ac24 / percTotal));
					} else {
						d.add(0D);
						d.add(0D);
					}
				} // final do for para o conjunto
			}
		} else {
			String query = "";
			if (parametros.get("atendente") != null)
				query = "select idLotacao from DpLotacao where idLotacaoIni in (select idLotacaoIni "
						+ "from DpLotacao where idLotacao in ("
						+ parametros.get("atendente") + "))";
			else
				query = "select idLotacao from DpLotacao where idLotacaoIni in (select idLotacaoIni "
						+ "from DpLotacao where idLotacao in ("
						+ parametros.get("lotacao") + "))";
			List lotacoes = ContextoPersistencia.em().createQuery(query).getResultList();
			StringBuilder listalotacoes = new StringBuilder();
			for (int i = 0; i < lotacoes.size(); i++) {
				listalotacoes.append(lotacoes.get(i));
				if (i < (lotacoes.size() - 1))
					listalotacoes.append(",");
			}
			percTotal = SrSolicitacao.AR
					.find("select count(*) "
							+ "from SrSolicitacao sol, SrMovimentacao mov "
							+ "where sol.idSolicitacao = mov.solicitacao "
							+ "and mov.lotaAtendente in ("
							+ listalotacoes
							+ ") "
							+ "and sol.local = "
							+ parametros.get("local")
							+ " "
							+ "and mov.dtIniMov = (select max(movult.dtIniMov) "
							+ "from SrMovimentacao movult "
							+ "where movult.solicitacao = mov.solicitacao "
							+ " and movult.tipoMov = 7 "
							+ " and movult.lotaAtendente.orgaoUsuario = 1) "
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

			List<SrSolicitacao> lista = SrSolicitacao.AR
					.find("select mov.lotaAtendente.siglaLotacao, sol.idSolicitacao, sol.dtReg, mov.dtIniMov "
							+ "from SrSolicitacao sol, SrMovimentacao mov "
							+ "where sol.idSolicitacao = mov.solicitacao "
							+ "and mov.lotaAtendente in ("
							+ listalotacoes
							+ ") "
							+ "and sol.local = "
							+ parametros.get("local")
							+ " "
							+ "and mov.dtIniMov = (select max(movult.dtIniMov) "
							+ "from SrMovimentacao movult "
							+ "where movult.solicitacao = mov.solicitacao "
							+ " and movult.tipoMov = 7 "
							+ " and movult.lotaAtendente.orgaoUsuario = 1) "
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
			Iterator it = lista.listIterator();
			while (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				local = (String) obj[0];
				SrSolicitacao sol = SrSolicitacao.AR.findById(obj[1]);
				DateTime startemp = new DateTime(obj[2]);
				DateTime endtemp = new DateTime(obj[3]);
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
				int dias = Days.daysBetween(start1.toLocalDateTime(),
						end1.toLocalDateTime()).getDays();
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
				DateTime dataInicialTemporaria = new DateTime(start1);
				DateTime dataFinalTemporaria = new DateTime(end1);
				DateTime dtinitemp = new DateTime(start1);
				DateTime dtendtemp = new DateTime(end1);
				int i = 1;
				while (!dataInicialTemporaria.isAfter(dataFinalTemporaria)) {
					if (calendario.isNonWorkingDay(dataInicialTemporaria
							.toLocalDate())) {
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
							dataInicialTemporaria.getDayOfMonth(), 9, 0, 0)
							.plusDays(1);
					i++;
				}
				BigDecimal totalMinutosPendentes = calcularPendencias(sol);
				totalMinutosTrabalhados = totalMinutosTrabalhados
						.subtract(totalMinutosPendentes);
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
			d.add(local);
			d.add(cls_1);
			d.add((cls_1 / percTotal));
			d.add(cls_2);
			d.add((cls_2 / percTotal));
			d.add(cls_4);
			d.add((cls_4 / percTotal));
			d.add(cls_12);
			d.add((cls_12 / percTotal));
			d.add(cls_24);
			d.add((cls_24 / percTotal));
			d.add(cls_ac24);
			d.add((cls_ac24 / percTotal));
		}
		return d;
	}

	private Set calculaFeriados() {
		List<CpOcorrenciaFeriado> ferjust = CpOcorrenciaFeriado.AR.find(
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

	private BigDecimal calcularPendencias(SrSolicitacao sol) {

		BigDecimal totalMinutosPendentes = new BigDecimal(0.0);

		List<DateTime> listaPendentes = new ArrayList<DateTime>();

		List<Object> d = new LinkedList<Object>();
		List<CpOcorrenciaFeriado> ferjust = CpOcorrenciaFeriado.AR.find(
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

		for (SrMovimentacao mov : sol.getMovimentacaoSet()) {
			if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA) {
				listaPendentes.add(new DateTime(mov.getDtIniMov()));
			}
			if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FIM_PENDENCIA) {
				listaPendentes.add(new DateTime(mov.getDtIniMov()));
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
			HolidayCalendar calendarioDeFeriados = new DefaultHolidayCalendar(
					dataDosFeriados);
			LocalDateKitCalculatorsFactory.getDefaultInstance()
					.registerHolidays("BR", calendarioDeFeriados);
			DateCalculator calendario = LocalDateKitCalculatorsFactory
					.getDefaultInstance().getDateCalculator("BR",
							HolidayHandlerType.FORWARD);
			int diasNaoUteis = 0;
			int totalHorasTrabalhadas = 0;
			int totalHorasPendentes = 0;
			DateTime dataInicialTemporaria = new DateTime(start1);
			DateTime dataFinalTemporaria = new DateTime(end1);
			DateTime dtinitemp = new DateTime(start1);
			DateTime dtendtemp = new DateTime(end1);
			int i = 1;
			while (!dataInicialTemporaria.isAfter(dataFinalTemporaria)) {
				if (calendario.isNonWorkingDay(dataInicialTemporaria
						.toLocalDate())) {
					diasNaoUteis++;
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
					totalHorasTrabalhadas += new Period(dtinitemp, dtendtemp)
							.getHours();
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
