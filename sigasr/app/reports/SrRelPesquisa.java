package reports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
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
import java.util.SortedSet;
import java.util.TreeMap;
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
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.Coluna;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.dp.CpOcorrenciaFeriado;
import br.gov.jfrj.siga.dp.DpLotacao;

public class SrRelPesquisa extends RelatorioTemplate {

	public SrRelPesquisa(Map parametros) throws DJBuilderException {
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
		
		this.setTitle("Relatório de Índice de Satisfação");
		estiloTituloColuna.setFont(new Font(8,"Arial",true));

		this.addColuna("Local", 100, RelatorioRapido.DIREITA,true, String.class);
		Coluna cls_1 = this.addColuna("Atendimento Bom ou Ótimo", 34, RelatorioRapido.DIREITA,
				false, Double.class);
		cls_1.setPadrao("0");
		Coluna perc_cls_1 = this.addColuna("%Total", 34, RelatorioRapido.DIREITA,
				false, Double.class);
		perc_cls_1.setPadrao("0.00%");
		Coluna cls_2 = this.addColuna("Atendimento Ruim", 33, RelatorioRapido.DIREITA, 
				false, Double.class);
		cls_2.setPadrao("0");
		Coluna perc_cls_2 = this.addColuna("%Total", 33, RelatorioRapido.DIREITA,
				false, Double.class);
		perc_cls_2.setPadrao("0.00%");
		return this;
	}

	@Override
	public Collection processarDados() throws ParseException {

		String local = new String();
		List<Object> d = new LinkedList<Object>();
		double atendAcMedia = 0;
		double atendAbMedia = 0;
		Long percTotal = (long) 0;
	
		if (parametros.get("lotacao").equals("")) {
			if (parametros.get("local").equals("0")) {
					SortedSet<String> set = new TreeSet<String>();
					SortedSet<String> setT = new TreeSet<String>();
					SortedSet<String> setA = new TreeSet<String>();
					TreeMap<String, Double> map = new TreeMap<String, Double>();
					TreeMap<String, Long> maptotais = new TreeMap<String, Long>();
					
					List<SrSolicitacao> listaTotal = SrSolicitacao.find(
						"select sol.local.nomeComplexo, count(resp.descrResposta) " 
						+ "from SrSolicitacao sol, SrMovimentacao mov, SrResposta resp "
						+ "where sol.idSolicitacao = mov.solicitacao "
						+ "and mov.idMovimentacao = resp.movimentacao "
						+ "and mov.tipoMov =16 "
						+ "and resp.pergunta.descrPergunta = 'Avaliação Final' "
						+ "and  sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
						+ "and  sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') "
						+ "group by sol.local.nomeComplexo" )
						.fetch();
					
					Iterator itTotal = listaTotal.listIterator();
					while (itTotal.hasNext()) {
						Object[] obj = (Object[]) itTotal.next();
						String complexo = (String) obj[0];
						Long total_local = (Long) obj[1];
						set.add(complexo);
						maptotais.put(complexo,total_local);
					}	
					
					for (String locais : set) {
						atendAcMedia = 0;
						atendAbMedia = 0;
						List<SrSolicitacao>  atendAcimaMedia = SrSolicitacao.find(
							"select sol.local.nomeComplexo, count(resp.descrResposta) " 
							+ "from SrSolicitacao sol, SrMovimentacao mov, SrResposta resp "
							+ "where sol.idSolicitacao = mov.solicitacao "
							+ "and sol.local.nomeComplexo = '" + locais.toString() + "' "
							+ "and mov.idMovimentacao = resp.movimentacao "
							+ "and mov.tipoMov =16 "
							+ "and resp.pergunta.descrPergunta = 'Avaliação Final' "
							+ "and resp.descrResposta in ('4','5') "
							+ "and  sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
							+ "and  sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') "
							+ "group by sol.local.nomeComplexo" )
							.fetch();
						List<SrSolicitacao>  atendAbaixoMedia = SrSolicitacao.find(
							"select sol.local.nomeComplexo, count(resp.descrResposta) " 
							+ "from SrSolicitacao sol, SrMovimentacao mov, SrResposta resp "
							+ "where sol.idSolicitacao = mov.solicitacao "
							+ "and sol.local.nomeComplexo = '" + locais.toString() + "' "
							+ "and mov.idMovimentacao = resp.movimentacao "
							+ "and mov.tipoMov =16 "
							+ "and resp.pergunta.descrPergunta = 'Avaliação Final' "
							+ "and resp.descrResposta in ('1','2') "
							+ "and  sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
							+ "and  sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') "
							+ "group by sol.local.nomeComplexo" )
							.fetch();
						Iterator itac = atendAcimaMedia.listIterator();
						while (itac.hasNext()) {
							Object[] obj = (Object[]) itac.next();
							local = (String) obj[0];
							Long totalacima= (Long) obj[1];
							set.add(local);
							map.put(chave(local,"acima"),Double.valueOf(totalacima));
						}
						Iterator itab = atendAbaixoMedia.listIterator();
						while (itab.hasNext()) {
							Object[] obj = (Object[]) itab.next();
							local = (String) obj[0];
							Long totalabaixo= (Long) obj[1];
							set.add(local);
							map.put(chave(local,"abaixo"), Double.valueOf(totalabaixo));
						}
						//for (String s : set) {
							d.add(locais);
							percTotal = maptotais.get(locais);
							if (map.containsKey(chave(locais, "acima"))) {
								atendAcMedia = map.get(chave(locais, "acima"));
								d.add(atendAcMedia);
								d.add((atendAcMedia/percTotal));
							} else 	{
								d.add(0D);
								d.add(0D);
							}
							
							if (map.containsKey(chave(locais, "abaixo"))) {
								atendAbMedia = map.get(chave(locais, "abaixo"));
								d.add(atendAbMedia);
								d.add((atendAbMedia/percTotal));
							} else 	{
								d.add(0D);
								d.add(0D);
							}
						} // final do for para o conjunto
					//}	// fim do for(String locais : set)
				} else {	//else do if parametros.get("local").equals("0"), ou seja, local = complexo.
						Long atendTotal = SrSolicitacao.find(
							"select count(resp.descrResposta) " 
							+ "from SrSolicitacao sol, SrMovimentacao mov, SrResposta resp "
							+ "where sol.idSolicitacao = mov.solicitacao "
							+ "and mov.idMovimentacao = resp.movimentacao "
							+ "and sol.local = '" + parametros.get("local") + "' "
							+ "and mov.tipoMov =16 "
							+ "and resp.pergunta.descrPergunta = 'Avaliação Final' "
							+ "and  sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
							+ "and  sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') ")
							.first();
						Long atendAcimaMedia = SrSolicitacao.find(
							"select count(resp.descrResposta) " 
							+ "from SrSolicitacao sol, SrMovimentacao mov, SrResposta resp "
							+ "where sol.idSolicitacao = mov.solicitacao "
							+ "and mov.idMovimentacao = resp.movimentacao "
							+ "and sol.local = '" + parametros.get("local") + "' "
							+ "and mov.tipoMov =16 "
							+ "and resp.pergunta.descrPergunta = 'Avaliação Final' "
							+ "and resp.descrResposta in ('4','5') "
							+ "and  sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
							+ "and  sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') ")
							.first();
						Long  atendAbaixoMedia = SrSolicitacao.find(
							"select count(resp.descrResposta) " 
							+ "from SrSolicitacao sol, SrMovimentacao mov, SrResposta resp "
							+ "where sol.idSolicitacao = mov.solicitacao "
							+ "and sol.local = '" + parametros.get("local") + "' "
							+ "and mov.idMovimentacao = resp.movimentacao "
							+ "and mov.tipoMov =16 "
							+ "and resp.pergunta.descrPergunta = 'Avaliação Final' "
							+ "and resp.descrResposta in ('1','2') "
							+ "and  sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
							+ "and  sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') ")
							.first();
						CpComplexo complexo = CpComplexo.findById(Long.valueOf(parametros.get("local").toString()));
						d.add( complexo.getNomeComplexo());
						//double doubleVal = (double)total/(double)percTotal;
						d.add((double)atendAcimaMedia);
						d.add(((double)atendAcimaMedia/(double)atendTotal));
						d.add((double)atendAbaixoMedia);
						d.add(((double)atendAbaixoMedia/(double)atendTotal));
					} // fim do else referente ao if parametros.get("local").equals("0") 
		} else  { //else do if lotacao (atendente) preenchido
			if (parametros.get("local").equals("0")) {
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
				
				List<SrSolicitacao> listaTotal = SrSolicitacao.find(
					"select sol.local.nomeComplexo,  mov.lotaAtendente.siglaLotacao, count(resp.descrResposta) " 
					+ "from SrSolicitacao sol, SrMovimentacao mov, SrResposta resp "
					+ "where sol.idSolicitacao = mov.solicitacao "
					+ "and mov.idMovimentacao = resp.movimentacao "
					+ "and mov.lotaAtendente in (" + listalotacoes + ") "
					+ "and mov.tipoMov =16 "
					+ "and resp.pergunta.descrPergunta = 'Avaliação Final' "
					+ "and  sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
					+ "and  sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') "
					+ "group by sol.local.nomeComplexo, mov.lotaAtendente.siglaLotacao" )
					.fetch();
				
				SortedSet<String> set = new TreeSet<String>();
				SortedSet<String> setT = new TreeSet<String>();
				SortedSet<String> setA = new TreeSet<String>();
				TreeMap<String, Double> map = new TreeMap<String, Double>();
				TreeMap<String, Long> maptotais = new TreeMap<String, Long>();
								
				Iterator itTotal = listaTotal.listIterator();
				while (itTotal.hasNext()) {
					Object[] obj = (Object[]) itTotal.next();
					String complexo = (String) obj[0];
					String lotaAtendente = (String) obj[1];
					Long total_local = (Long) obj[2];
					setT.add(complexo);
					setA.add(lotaAtendente);
					maptotais.put(chave(complexo, lotaAtendente),total_local);
				}	
				
				for (String atendentes : setT) {
					atendAcMedia = 0;
					atendAbMedia = 0;
					List<SrSolicitacao>  atendAcimaMedia = SrSolicitacao.find(
						"select sol.local.nomeComplexo, mov.lotaAtendente.siglaLotacao, count(resp.descrResposta) " 
						+ "from SrSolicitacao sol, SrMovimentacao mov, SrResposta resp "
						+ "where sol.idSolicitacao = mov.solicitacao "
						+ "and mov.lotaAtendente in (" + listalotacoes + ") "
						+ "and mov.idMovimentacao = resp.movimentacao "
						+ "and mov.tipoMov =16 "
						+ "and resp.pergunta.descrPergunta = 'Avaliação Final' "
						+ "and resp.descrResposta in ('4','5') "
						+ "and  sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
						+ "and  sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') "
						+ "group by sol.local.nomeComplexo, mov.lotaAtendente.siglaLotacao" )
						.fetch();
					List<SrSolicitacao>  atendAbaixoMedia = SrSolicitacao.find(
						"select sol.local.nomeComplexo, mov.lotaAtendente.siglaLotacao, count(resp.descrResposta) " 
						+ "from SrSolicitacao sol, SrMovimentacao mov, SrResposta resp "
						+ "where sol.idSolicitacao = mov.solicitacao "
						+ "and mov.lotaAtendente in (" + listalotacoes + ") "
						+ "and mov.idMovimentacao = resp.movimentacao "
						+ "and mov.tipoMov =16 "
						+ "and resp.pergunta.descrPergunta = 'Avaliação Final' "
						+ "and resp.descrResposta in ('1','2') "
						+ "and  sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
						+ "and  sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') "
						+ "group by sol.local.nomeComplexo, mov.lotaAtendente.siglaLotacao" )
						.fetch();
					Iterator itac = atendAcimaMedia.listIterator();
					while (itac.hasNext()) {
						Object[] obj = (Object[]) itac.next();
						local = (String) obj[0];
						String lotaAtendente = (String) obj[1];
						Long totalacima= (Long) obj[2];
						setT.add(local);
						setA.add(lotaAtendente);
						map.put(chave(local + lotaAtendente,"acima"),Double.valueOf(totalacima));
					}
					Iterator itab = atendAbaixoMedia.listIterator();
					while (itab.hasNext()) {
						Object[] obj = (Object[]) itab.next();
						local = (String) obj[0];
						String lotaAtendente = (String) obj[1];
						Long totalabaixo= (Long) obj[2];
						setT.add(local);
						setA.add(lotaAtendente);
						map.put(chave(local + lotaAtendente,"abaixo"), Double.valueOf(totalabaixo));
					}
					//for (String compl: setT) {
						for (String s : setA) {
							d.add(atendentes + " - " + s);
							percTotal = maptotais.get(atendentes+s);
							if (map.containsKey(chave(atendentes+s, "acima"))) {
								atendAcMedia = map.get(chave(atendentes+s, "acima"));
								d.add(atendAcMedia);
								d.add((atendAcMedia/percTotal));
							} else 	{
								d.add(0D);
								d.add(0D);
							}
							
							if (map.containsKey(chave(atendentes+s, "abaixo"))) {
								atendAbMedia = map.get(chave(atendentes+s, "abaixo"));
								d.add(atendAbMedia);
								d.add((atendAbMedia/percTotal));
							} else 	{
								d.add(0D);
								d.add(0D);
							}
						} // final do for para o conjunto
				}// fim do for(String atendentes : setT)
			} //fim do else referente ao if parametros.get("lotacao") preenchido - local = Todos 
			  else { //inicio do else if parametros.get("lotacao") preenchido - local preenchido
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
				
				List<SrSolicitacao> listaTotal = SrSolicitacao.find(
					"select sol.local.nomeComplexo,  mov.lotaAtendente.siglaLotacao, count(resp.descrResposta) " 
					+ "from SrSolicitacao sol, SrMovimentacao mov, SrResposta resp "
					+ "where sol.idSolicitacao = mov.solicitacao "
					+ "and mov.idMovimentacao = resp.movimentacao "
					+ "and mov.lotaAtendente in (" + listalotacoes + ") "
					+ "and sol.local = '" + parametros.get("local") + "' "
					+ "and mov.tipoMov =16 "
					+ "and resp.pergunta.descrPergunta = 'Avaliação Final' "
					+ "and  sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
					+ "and  sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') "
					+ "group by sol.local.nomeComplexo, mov.lotaAtendente.siglaLotacao" )
					.fetch();
				
				SortedSet<String> setT = new TreeSet<String>();
				SortedSet<String> setA = new TreeSet<String>();
				TreeMap<String, Double> map = new TreeMap<String, Double>();
				TreeMap<String, Long> maptotais = new TreeMap<String, Long>();
								
				Iterator itTotal = listaTotal.listIterator();
				while (itTotal.hasNext()) {
					Object[] obj = (Object[]) itTotal.next();
					String complexo = (String) obj[0];
					String lotaAtendente = (String) obj[1];
					Long total_local = (Long) obj[2];
					setT.add(complexo);
					setA.add(lotaAtendente);
					maptotais.put(chave(complexo, lotaAtendente),total_local);
				}	
				
				for (String atendentes : setT) {
					atendAcMedia = 0;
					atendAbMedia = 0;
					List<SrSolicitacao>  atendAcimaMedia = SrSolicitacao.find(
						"select sol.local.nomeComplexo, mov.lotaAtendente.siglaLotacao, count(resp.descrResposta) " 
						+ "from SrSolicitacao sol, SrMovimentacao mov, SrResposta resp "
						+ "where sol.idSolicitacao = mov.solicitacao "
						+ "and mov.lotaAtendente in (" + listalotacoes + ") "
						+ "and sol.local.nomeComplexo = '" + atendentes.toString() + "' "
						+ "and mov.idMovimentacao = resp.movimentacao "
						+ "and mov.tipoMov =16 "
						+ "and resp.pergunta.descrPergunta = 'Avaliação Final' "
						+ "and resp.descrResposta in ('4','5') "
						+ "and  sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
						+ "and  sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') "
						+ "group by sol.local.nomeComplexo, mov.lotaAtendente.siglaLotacao" )
						.fetch();
					List<SrSolicitacao>  atendAbaixoMedia = SrSolicitacao.find(
						"select sol.local.nomeComplexo, mov.lotaAtendente.siglaLotacao, count(resp.descrResposta) " 
						+ "from SrSolicitacao sol, SrMovimentacao mov, SrResposta resp "
						+ "where sol.idSolicitacao = mov.solicitacao "
						+ "and mov.lotaAtendente in (" + listalotacoes + ") "
						+ "and sol.local.nomeComplexo = '" + atendentes.toString() + "' "
						+ "and mov.idMovimentacao = resp.movimentacao "
						+ "and mov.tipoMov =16 "
						+ "and resp.pergunta.descrPergunta = 'Avaliação Final' "
						+ "and resp.descrResposta in ('1','2') "
						+ "and  sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
						+ "and  sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') "
						+ "group by sol.local.nomeComplexo, mov.lotaAtendente.siglaLotacao" )
						.fetch();
					Iterator itac = atendAcimaMedia.listIterator();
					while (itac.hasNext()) {
						Object[] obj = (Object[]) itac.next();
						local = (String) obj[0];
						String lotaAtendente = (String) obj[1];
						Long totalacima= (Long) obj[2];
						setT.add(local);
						setA.add(lotaAtendente);
						map.put(chave(local + lotaAtendente,"acima"),Double.valueOf(totalacima));
					}
					Iterator itab = atendAbaixoMedia.listIterator();
					while (itab.hasNext()) {
						Object[] obj = (Object[]) itab.next();
						local = (String) obj[0];
						String lotaAtendente = (String) obj[1];
						Long totalabaixo= (Long) obj[2];
						setT.add(local);
						setA.add(lotaAtendente);
						map.put(chave(local + lotaAtendente,"abaixo"), Double.valueOf(totalabaixo));
					}
					//for (String compl: setT) {
						for (String s : setA) {
							d.add(atendentes + " - " + s);
							percTotal = maptotais.get(atendentes+s);
							if (map.containsKey(chave(atendentes+s, "acima"))) {
								atendAcMedia = map.get(chave(atendentes+s, "acima"));
								d.add(atendAcMedia);
								d.add((atendAcMedia/percTotal));
							} else 	{
								d.add(0D);
								d.add(0D);
							}
							
							if (map.containsKey(chave(atendentes+s, "abaixo"))) {
								atendAbMedia = map.get(chave(atendentes+s, "abaixo"));
								d.add(atendAbMedia);
								d.add((atendAbMedia/percTotal));
							} else 	{
								d.add(0D);
								d.add(0D);
							}
						} // final do for para o conjunto
				}// fim do for(String atendentes : setT)
			}
		} //fim do else referente ao if parametros.get("lotacao") preenchido - local preenchido
		return d;															
	}
	
	private String chave(String local, String nivel) {
		return local + nivel;
	}
	
}
