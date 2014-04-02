package reports;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import models.SrSolicitacao;
import play.db.jpa.JPA;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.Coluna;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;

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
		perc.setPadrao("0.00%");
		return this;
	}

	@Override
	public Collection processarDados() {

		List<Object> d = new LinkedList<Object>();
		String atendente = (String) parametros.get("atendente");
		Long percTotal = (long) 0;
			
		if (parametros.get("local").equals("0")) {
			if ((parametros.get("lotacao").equals("")) && (parametros.get("atendente") == null)) {
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
						//	+ " and movult.lotaAtendente.orgaoUsuario = ) "
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
							d.add(doubleVal);
						}
			} else {
							String query = "";
							if  (parametros.get("atendente") != null)
								query = "select idLotacao from DpLotacao where idLotacaoIni in (select idLotacaoIni " +
										"from DpLotacao where idLotacao in (" +  parametros.get("atendente") + "))";
							else
								query = "select idLotacao from DpLotacao where idLotacaoIni in (select idLotacaoIni " +
										"from DpLotacao where idLotacao in (" +  parametros.get("lotacao") + "))";
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
									d.add(doubleVal);
								}
					}
			} else {
				
				if ((parametros.get("lotacao").equals("")) && (parametros.get("atendente") == null)) {
					percTotal = SrSolicitacao.find(
							"select count(*) " 
							+ "from SrSolicitacao sol, SrMovimentacao mov " 
							+ "where sol.idSolicitacao = mov.solicitacao "
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
							+ "where sol.idSolicitacao = mov.solicitacao "
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
							d.add(doubleVal);
						}
				} else {
					String query =  "";
					if (parametros.get("atendente") != null)
						query = "select idLotacao from DpLotacao where idLotacaoIni in (select idLotacaoIni " +
									"from DpLotacao where idLotacao in (" +  parametros.get("atendente") + "))";
					else 
						query = "select idLotacao from DpLotacao where idLotacaoIni in (select idLotacaoIni " +
								"from DpLotacao where idLotacao in (" +  parametros.get("lotacao") + "))";
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
							d.add(doubleVal);
						}
				} 
		}	
		return d;
	}
} 
