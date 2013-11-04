package reports;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import models.SrMovimentacao;
import models.SrSolicitacao;

import org.hibernate.Query;

import play.db.jpa.JPA;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
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
		//if (parametros.get("lotacao") == null) {
		//	throw new DJBuilderException("Parâmetro lotação não informado!");
		//}
		if (parametros.get("local") == null) {
			throw new DJBuilderException("Parâmetro local não informado!");
		}
}

	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException {
		this.setTitle("Relatório de Solicitações por Localidade");
		this.addColuna("Item de Configuração", 30, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Serviço", 30, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Total", 20, RelatorioRapido.CENTRO, false);
		//this.addColuna("Tot", 20, RelatorioRapido.CENTRO, false);
		//this.setp
		return this;
	}

	@Override
	public Collection processarDados() {

		List<String> d = new LinkedList<String>();
		
		if (parametros.get("lotacao").equals("")) {
			List<SrSolicitacao> lista = SrSolicitacao.find(
					"select sol.itemConfiguracao.tituloItemConfiguracao, sol.servico.tituloServico, count(*) " +
					"from SrSolicitacao sol, SrMovimentacao mov " +
					"where sol.idSolicitacao = mov.solicitacao " +
					"and sol.local = " + parametros.get("local") + " " +
					"and sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') " +
					"and sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') " +
							"group by sol.itemConfiguracao.tituloItemConfiguracao, sol.servico.tituloServico").fetch();

					Iterator it = lista.listIterator(); 
					Long tot = (long) 0;
					while (it.hasNext()) {
						Object[] obj = (Object[]) it.next();
						String itensconf = (String) obj[0];
						String itensserv = (String) obj[1];
						Long total = (Long) obj[2];
						tot = tot + total;
						d.add(itensconf.toString());
						d.add(itensserv.toString());
						d.add(total.toString());
						//d.add(tot.toString());
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
							"select sol.itemConfiguracao.tituloItemConfiguracao, sol.servico.tituloServico, count(*) " +
							"from SrSolicitacao sol, SrMovimentacao mov " +
							"where sol.idSolicitacao = mov.solicitacao " +
							"and mov.lotaAtendente in (" + listalotacoes + ") " +
							"and sol.local = " + parametros.get("local") + " " +
							"and sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') " +
							"and sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') " +
									"group by sol.itemConfiguracao.tituloItemConfiguracao, sol.servico.tituloServico").fetch();
							Long tot = (long) 0;
							Iterator it = lista.listIterator(); 
							while (it.hasNext()) {
								Object[] obj = (Object[]) it.next();
								String itensconf = (String) obj[0];
								String itensserv = (String) obj[1];
								Long total = (Long) obj[2];
								tot = tot + total;
								d.add(itensconf.toString());
								d.add(itensserv.toString());
								d.add(total.toString());
								//d.add(tot.toString());
					}
		
		}
		return d;
	}

}
