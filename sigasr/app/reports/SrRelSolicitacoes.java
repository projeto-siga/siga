package reports;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import models.SrSolicitacao;

import org.hibernate.Query;

import play.db.jpa.JPA;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class SrRelSolicitacoes extends RelatorioTemplate {

	public SrRelSolicitacoes(Map parametros) throws DJBuilderException {
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
		if (parametros.get("situacao") == null) {
			throw new DJBuilderException("Parâmetro situação não informado!");
		}
}

	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException {
		this.setTitle("Relatório de Solicitações");
		this.addColuna("Solicitação", 20, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Movimentação", 15, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Descrição", 35, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Solicitante", 10, RelatorioRapido.ESQUERDA, true);
		this.addColuna("Atendente", 15, RelatorioRapido.CENTRO, false);
		this.addColuna("Situação", 15, RelatorioRapido.ESQUERDA, true);
		this.addColuna("Item de Configuração", 16, RelatorioRapido.CENTRO, false);
		this.addColuna("Serviço", 20, RelatorioRapido.CENTRO, false);
		return this;
	}

	@Override
	public Collection processarDados() {

		List<String> d = new LinkedList<String>();
		
		String query = "select idLotacao from DpLotacao where idLotacaoIni = (select idLotacaoIni " +
				"from DpLotacao where idLotacao = " +  parametros.get("lotacao") + ")";
		List lotacoes = JPA.em().createQuery(query).getResultList();
		StringBuilder listalotacoes= new StringBuilder();
		for (int i  = 0; i < lotacoes.size(); i++){
			listalotacoes.append(lotacoes.get(i));
			if (i < ( lotacoes.size() - 1)) listalotacoes.append(",");
		}

		if (parametros.get("situacao").equals("Todas")) {
			List<SrSolicitacao> lista = SrSolicitacao.find(
				"select sol.idSolicitacao, to_char(mov.dtIniMov,'dd/mm/yy hh24:mi'), sol.descrSolicitacao, " +
				"sol.lotaSolicitante.siglaLotacao, mov.lotaAtendente.siglaLotacao, " +
				"mov.tipoMov.nome, sol.itemConfiguracao.tituloItemConfiguracao, sol.servico.tituloServico " +
				"from SrSolicitacao sol, SrMovimentacao mov " +
				"where sol.idSolicitacao = mov.solicitacao " +
				"and sol.idSolicitacao = sol.hisIdIni " +
				"and mov.lotaAtendente in (" + listalotacoes + ") " +
				"and mov.idMovimentacao = (select max(idMovimentacao) from SrMovimentacao " +
				"where solicitacao = mov.solicitacao and lotaAtendente = mov.lotaAtendente) " +
				"and sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') " +
				"and sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') " +
				"order by mov.tipoMov.nome").fetch();

				Iterator it = lista.listIterator(); 
				while (it.hasNext()) {
						Object[] sol = (Object[]) it.next();
						SrSolicitacao solic = new SrSolicitacao();
						/* Pegar numero da solicitacao */
						d.add(solic.getCodigo((Long)sol[0]));
						d.add(sol[1].toString());
						d.add(sol[2].toString());
						d.add(sol[3].toString());
						d.add(sol[4].toString());
						d.add(sol[5].toString());
						d.add(sol[6].toString());
						d.add(sol[7].toString());
				}
			} else {
				List<SrSolicitacao> lista = SrSolicitacao.find(
					"select sol.idSolicitacao, to_char(mov.dtIniMov,'dd/mm/yy hh24:mi'), sol.descrSolicitacao, " +
					"sol.lotaSolicitante.siglaLotacao, mov.lotaAtendente.siglaLotacao, " +
					"mov.tipoMov.nome, sol.itemConfiguracao.tituloItemConfiguracao, sol.servico.tituloServico " +
					"from SrSolicitacao sol, SrMovimentacao mov " +
					"where sol.idSolicitacao = mov.solicitacao " +
					"and sol.idSolicitacao = sol.hisIdIni " +
					"and mov.lotaAtendente in (" + listalotacoes + ") " +
					"and mov.idMovimentacao = (select max(idMovimentacao) from SrMovimentacao " +
					"where solicitacao = mov.solicitacao and lotaAtendente = mov.lotaAtendente) " +
					"and mov.tipoMov= " + parametros.get("situacao") + " " +
					"and sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') " +
					"and sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') " +
					"order by mov.tipoMov.nome").fetch();

					Iterator it = lista.listIterator(); 
					while (it.hasNext()) {
						Object[] sol = (Object[]) it.next();
						SrSolicitacao solic = new SrSolicitacao();
						/* Pegar numero da solicitacao */
						d.add(solic.getCodigo((Long)sol[0]));
						d.add(sol[1].toString());
						d.add(sol[2].toString());
						d.add(sol[3].toString());
						d.add(sol[4].toString());
						d.add(sol[5].toString());
						d.add(sol[6].toString());
						d.add(sol[7].toString());
			}
		}
		
		return d;
	}

}
