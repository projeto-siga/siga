package reports;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import models.SrMovimentacao;
import models.SrSolicitacao;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.hibernate.Query;

import play.db.jpa.JPA;
import ar.com.fdvs.dj.core.DJConstants;
import ar.com.fdvs.dj.core.layout.HorizontalBandAlignment;
import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.DJCrosstab;
import ar.com.fdvs.dj.domain.DJCrosstabColumn;
import ar.com.fdvs.dj.domain.DJCrosstabRow;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.CrosstabBuilder;
import ar.com.fdvs.dj.domain.builders.CrosstabColumnBuilder;
import ar.com.fdvs.dj.domain.builders.CrosstabRowBuilder;
import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.Coluna;
import br.gov.jfrj.relatorio.dinamico.IConstantes;
import br.gov.jfrj.relatorio.dinamico.LayoutRelatorioDinamico;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class SrRelLocal extends RelatorioTemplate {

	public SrRelLocal(Map parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("dtIni").equals("")) {
			throw new DJBuilderException(
					"Parâmetro data inicial não informado!");
		}
		if (parametros.get("dtFim").equals("")) {
			throw new DJBuilderException(
					"Parâmetro data final não informado!");
		}
		if (parametros.get("local") == null) {
			throw new DJBuilderException("Parâmetro local não informado!");
		}
}

	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, ColumnBuilderException {
		
		this.setTitle("Relatório de Solicitações por Localidade");
		this.addColuna("Local", 100, RelatorioRapido.ESQUERDA, true);
		this.addColuna("Item de Configuração", 40, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Ação", 35, RelatorioRapido.ESQUERDA, false);
		Coluna c = this.addColuna("Total", 30, RelatorioRapido.CENTRO, false, Long.class);
		this.setColunaTotal(c);
		Coluna perc = this.addColuna("% do Total", 30, RelatorioRapido.ESQUERDA, false, Double.class);
		this.setColunaPercTotal(perc);
		perc.setPadrao("#.00");
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection processarDados() {

		List<Object> d = new LinkedList<Object>();
		String atendente = (String) parametros.get("atendente");
		Long percTotal = (long) 0;
			
		if (parametros.get("local").equals("0")) {
			if (parametros.get("lotacao").equals("")) {
				percTotal = SrSolicitacao.find(
						"select count(*) " 
						+ "from SrSolicitacao sol, SrMovimentacao mov " 
						+ "where sol.idSolicitacao = mov.solicitacao "
						+ "and mov.dtIniMov = (select max(movult.dtIniMov) " 
							+ "from SrMovimentacao movult " 
							+ "where movult.solicitacao = mov.solicitacao "
							+ " and movult.tipoMov = 7) "
						+ "and not exists (select 1 from SrMovimentacao movfec " 
						+		"where movfec.solicitacao = mov.solicitacao " 
						+		"and movfec.dtIniMov > mov.dtIniMov " 
						+		"and movfec.tipoMov <> 14) "
						+ "and  mov.dtIniMov >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
						+ "and  mov.dtIniMov <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') ")
						.first();
				List<SrSolicitacao> lista = SrSolicitacao.find(
						"select sol.local.nomeComplexo, sol.itemConfiguracao.tituloItemConfiguracao, " +
						"sol.acao.tituloAcao, count(*) " +
						"from SrSolicitacao sol, SrMovimentacao mov " 
						+ "where sol.idSolicitacao = mov.solicitacao "
						+ "and mov.dtIniMov = (select max(movult.dtIniMov) " 
							+ "from SrMovimentacao movult " 
							+ "where movult.solicitacao = mov.solicitacao "
							+ " and movult.tipoMov = 7) "
						+ "and not exists (select 1 from SrMovimentacao movfec " 
						+		"where movfec.solicitacao = mov.solicitacao " 
						+		"and movfec.dtIniMov > mov.dtIniMov " 
						+		"and movfec.tipoMov <> 14) "
						+ "and  mov.dtIniMov >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
						+ "and  mov.dtIniMov <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') "
						+ " group by sol.local.nomeComplexo, sol.itemConfiguracao.tituloItemConfiguracao, " 
						+ "sol.acao.tituloAcao").fetch();
						
						Iterator it = lista.listIterator(); 
						while (it.hasNext()) {
							Object[] obj = (Object[]) it.next();
							String itenslocais = (String) obj[0];
							String itensconf = (String) obj[1];
							String itensserv = (String) obj[2];
							Long total = (Long) obj[3];
							d.add(itenslocais.toString());
							d.add(itensconf.toString());
							d.add(itensserv.toString());
							d.add(total);
							double doubleVal = (double)total/(double)percTotal;
							d.add(doubleVal*100);
						}
			} else {
							//String query = "select idLotacao from DpLotacao where idLotacaoIni = (select idLotacaoIni " +
							//"from DpLotacao where idLotacao = " +  parametros.get("lotacao") + ")";
							String query = "select idLotacao from DpLotacao where idLotacaoIni in (select idLotacaoIni " +
									"from DpLotacao where idLotacao in (" +  parametros.get("atendente") + "))";
							List lotacoes = JPA.em().createQuery(query).getResultList();
							StringBuilder listalotacoes= new StringBuilder();
							for (int i  = 0; i < lotacoes.size(); i++){
								listalotacoes.append(lotacoes.get(i));
								if (i < ( lotacoes.size() - 1)) listalotacoes.append(",");
							}
							percTotal = SrSolicitacao.find(
									"select count(*) " 
									+ "from SrSolicitacao sol, SrMovimentacao mov " 
									+ "where exists (select 1 from SrMovimentacao mov " +
									"where mov.solicitacao = sol.idSolicitacao " +
									" and mov.lotaAtendente in (" + listalotacoes + "))" 
									+ "and sol.idSolicitacao = mov.solicitacao "
									+ "and mov.dtIniMov = (select max(movult.dtIniMov) " 
										+ "from SrMovimentacao movult " 
										+ "where movult.solicitacao = mov.solicitacao "
										+ " and movult.tipoMov = 7) "
									+ "and not exists (select 1 from SrMovimentacao movfec " 
									+		"where movfec.solicitacao = mov.solicitacao " 
									+		"and movfec.dtIniMov > mov.dtIniMov " 
									+		"and movfec.tipoMov <> 14) "
									+ "and  mov.dtIniMov >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
									+ "and  mov.dtIniMov <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') ")
									.first();
							List<SrSolicitacao> lista = SrSolicitacao.find(
								"select sol.local.nomeComplexo, sol.itemConfiguracao.tituloItemConfiguracao, " +
								"sol.acao.tituloAcao, count(*) " 
								+ "from SrSolicitacao sol, SrMovimentacao mov " 
								+ "where exists (select 1 from SrMovimentacao mov " +
												"where mov.solicitacao = sol.idSolicitacao " +
												" and mov.lotaAtendente in (" + listalotacoes + "))" 
								+ "and sol.idSolicitacao = mov.solicitacao "
								+ "and mov.dtIniMov = (select max(movult.dtIniMov) " 
									+ "from SrMovimentacao movult " 
									+ "where movult.solicitacao = mov.solicitacao "
									+ " and movult.tipoMov = 7) "
								+ "and not exists (select 1 from SrMovimentacao movfec " 
								+		"where movfec.solicitacao = mov.solicitacao " 
								+		"and movfec.dtIniMov > mov.dtIniMov " 
								+		"and movfec.tipoMov <> 14) "
								+ "and  mov.dtIniMov >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
								+ "and  mov.dtIniMov <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') "
								+ " group by sol.local.nomeComplexo, sol.itemConfiguracao.tituloItemConfiguracao, " 
								+ "sol.acao.tituloAcao").fetch();
								Iterator it = lista.listIterator(); 
								while (it.hasNext()) {
									Object[] obj = (Object[]) it.next();
									String itenslocais = (String) obj[0];
									String itensconf = (String) obj[1];
									String itensserv = (String) obj[2];
									Long total = (Long) obj[3];
									d.add(itenslocais.toString());
									d.add(itensconf.toString());
									d.add(itensserv.toString());
									d.add(total);
									double doubleVal = (double)total/(double)percTotal;
									d.add(doubleVal*100);
								}
					}
			} else {
				String query = "select idLotacao from DpLotacao where idLotacaoIni = (select idLotacaoIni " +
				"from DpLotacao where idLotacao = " +  parametros.get("lotacao") + ")";
				List lotacoes = JPA.em().createQuery(query).getResultList();
				StringBuilder listalotacoes= new StringBuilder();
				for (int i  = 0; i < lotacoes.size(); i++){
					listalotacoes.append(lotacoes.get(i));
					if (i < ( lotacoes.size() - 1)) listalotacoes.append(",");
				}
				percTotal = SrSolicitacao.find(
						"select count(*) " 
						+ "from SrSolicitacao sol, SrMovimentacao mov " 
						+ "where exists (select 1 from SrMovimentacao mov " 
											+ "where mov.solicitacao = sol.idSolicitacao " 
											+ " and mov.lotaAtendente in (" + listalotacoes + "))" 
						+ "and sol.idSolicitacao = mov.solicitacao "
						+ "and sol.local = " + parametros.get("local") + " "
						+ "and mov.dtIniMov = (select max(movult.dtIniMov) " 
							+ "from SrMovimentacao movult " 
							+ "where movult.solicitacao = mov.solicitacao "
							+ " and movult.tipoMov = 7) "
						+ "and not exists (select 1 from SrMovimentacao movfec " 
						+		"where movfec.solicitacao = mov.solicitacao " 
						+		"and movfec.dtIniMov > mov.dtIniMov " 
						+		"and movfec.tipoMov <> 14) "
						+ "and  mov.dtIniMov >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
						+ "and  mov.dtIniMov <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') ")
						.first();
				List<SrSolicitacao> lista = SrSolicitacao.find(
					"select sol.local.nomeComplexo, sol.itemConfiguracao.tituloItemConfiguracao, " 
					+ "sol.acao.tituloAcao, count(*) " 
					+ "from SrSolicitacao sol, SrMovimentacao mov " 
					+ "where exists (select 1 from SrMovimentacao mov " 
					+ "where mov.solicitacao = sol.idSolicitacao " 
					+ " and mov.lotaAtendente in (" + listalotacoes + "))" 
					+ "and sol.idSolicitacao = mov.solicitacao "
					+ "and sol.local = " + parametros.get("local") + " "
					+ "and mov.dtIniMov = (select max(movult.dtIniMov) " 
							+ "from SrMovimentacao movult " 
								+ "where movult.solicitacao = mov.solicitacao "
									+ " and movult.tipoMov = 7) "
					+ "and not exists (select 1 from SrMovimentacao movfec " 
							+ "where movfec.solicitacao = mov.solicitacao " 
							+ "and movfec.dtIniMov > mov.dtIniMov " 
							+ "and movfec.tipoMov <> 14) "
					+ "and  mov.dtIniMov >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
					+ "and  mov.dtIniMov <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') "
					+ " group by sol.local.nomeComplexo, sol.itemConfiguracao.tituloItemConfiguracao, " 
					+ "sol.acao.tituloAcao").fetch();
					Iterator it = lista.listIterator(); 
					while (it.hasNext()) {
						Object[] obj = (Object[]) it.next();
						String itenslocais = (String) obj[0];
						String itensconf = (String) obj[1];
						String itensserv = (String) obj[2];
						Long total = (Long) obj[3];
						d.add(itenslocais.toString());
						d.add(itensconf.toString());
						d.add(itensserv.toString());
						d.add(total);
						double doubleVal = (double)total/(double)percTotal;
						d.add(doubleVal*100);
					}
			}
		return d;
		}
		
}
