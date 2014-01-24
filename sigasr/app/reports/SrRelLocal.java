package reports;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import models.SrMovimentacao;
import models.SrSolicitacao;

import org.hibernate.Query;

import play.db.jpa.JPA;

import ar.com.fdvs.dj.core.layout.HorizontalBandAlignment;
import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.Coluna;
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
		this.addColuna("Item de Configuração", 50, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Ação", 50, RelatorioRapido.ESQUERDA, false);
		Coluna c = this.addColuna("Total", 40, RelatorioRapido.CENTRO, false, Long.class);
		this.setColunaTotal(c);
		return this;
	}

	@Override
	public Collection processarDados() {

		List<Object> d = new LinkedList<Object>();
		Long totsols = (long) 0;
		
		if (parametros.get("local").equals("0")) {
			if (parametros.get("lotacao").equals("")) {
				List<SrSolicitacao> lista = SrSolicitacao.find(
						"select sol.local.nomeComplexo, sol.itemConfiguracao.tituloItemConfiguracao, " +
						"sol.acao.tituloAcao, count(*) " +
						"from SrSolicitacao sol " +
						"where sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') " +
						"and sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') " +
						"group by sol.local.nomeComplexo, sol.itemConfiguracao.tituloItemConfiguracao, " +
						"sol.acao.tituloAcao").fetch();
						Iterator it = lista.listIterator(); 
						while (it.hasNext()) {
							Object[] obj = (Object[]) it.next();
							String itenslocais = (String) obj[0];
							String itensconf = (String) obj[1];
							String itensserv = (String) obj[2];
							Long total = (Long) obj[3];
							totsols = totsols + total;
							d.add(itenslocais.toString());
							d.add(itensconf.toString());
							d.add(itensserv.toString());
							d.add(total);
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
							List<SrSolicitacao> lista = SrSolicitacao.find(
								"select sol.local.nomeComplexo, sol.itemConfiguracao.tituloItemConfiguracao, sol.acao.tituloAcao, count(*) " +
								"from SrSolicitacao sol " +
								"where exists (select 1 from SrMovimentacao mov where mov.solicitacao = sol.idSolicitacao " +
								"				and mov.lotaAtendente in (" + listalotacoes + "))" +
								"and sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') " +
								"and sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') " +
										"group by sol.local.nomeComplexo, sol.itemConfiguracao.tituloItemConfiguracao, sol.acao.tituloAcao").fetch();
								Long tot = (long) 0;
								Iterator it = lista.listIterator(); 
								while (it.hasNext()) {
									Object[] obj = (Object[]) it.next();
									String itenslocais = (String) obj[0];
									String itensconf = (String) obj[1];
									String itensserv = (String) obj[2];
									Long total = (Long) obj[3];
									tot = tot + total;
									d.add(itenslocais.toString());
									d.add(itensconf.toString());
									d.add(itensserv.toString());
									d.add(total);
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
				List<SrSolicitacao> lista = SrSolicitacao.find(
					"select sol.local.nomeComplexo, sol.itemConfiguracao.tituloItemConfiguracao, " +
					"sol.acao.tituloAcao, count(*) " +
					"from SrSolicitacao sol " +
					"where exists (select 1 from SrMovimentacao mov where mov.solicitacao = sol.idSolicitacao " +
					"				and mov.lotaAtendente in (" + listalotacoes + "))" +
					"and sol.local = " + parametros.get("local") + " " +
					"and sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') " +
					"and sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') " +
							"group by sol.local.nomeComplexo, sol.itemConfiguracao.tituloItemConfiguracao, sol.acao.tituloAcao").fetch();
					Long tot = (long) 0;
					Iterator it = lista.listIterator(); 
					while (it.hasNext()) {
						Object[] obj = (Object[]) it.next();
						String itenslocais = (String) obj[0];
						String itensconf = (String) obj[1];
						String itensserv = (String) obj[2];
						Long total = (Long) obj[3];
						tot = tot + total;
						d.add(itenslocais.toString());
						d.add(itensconf.toString());
						d.add(itensserv.toString());
						d.add(total);
					}
			}
		return d;
		}
		
}
