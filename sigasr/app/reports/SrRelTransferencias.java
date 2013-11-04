package reports;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import models.SrMovimentacao;
import models.SrSolicitacao;

import org.hibernate.Query;

import play.db.jpa.JPA;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class SrRelTransferencias extends RelatorioTemplate {

	public SrRelTransferencias(Map parametros) throws DJBuilderException {
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
}

	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException {
		this.setTitle("Relatório de Transferências");
		this.addColuna("Solicitação", 18, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Movimentacao", 13, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Descrição", 40, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Solicitante", 10, RelatorioRapido.ESQUERDA, true);
		this.addColuna("Atendente", 13, RelatorioRapido.CENTRO, false);
		this.addColuna("Situação", 15, RelatorioRapido.ESQUERDA, true);
		this.addColuna("Item de Configuração", 16, RelatorioRapido.CENTRO, false);
		this.addColuna("Serviço", 16, RelatorioRapido.CENTRO, false);
		return this;
	}

	@Override
	public Collection processarDados() {

		List<String> e = new ArrayList<String>();
					
		String query = "select idLotacao from DpLotacao where idLotacaoIni = (select idLotacaoIni " +
				"from DpLotacao where idLotacao = " +  parametros.get("lotacao") + ")";
		List lotacoes = JPA.em().createQuery(query).getResultList();
		StringBuilder listalotacoes= new StringBuilder();
		for (int i  = 0; i < lotacoes.size(); i++){
			listalotacoes.append(lotacoes.get(i));
			if (i < ( lotacoes.size() - 1)) listalotacoes.append(",");
		}
		//pega mais de uma movimentacao...
		List<SrSolicitacao> lista = SrSolicitacao.find(
				"select sol, mov " +
				"from SrSolicitacao sol, SrMovimentacao mov " +
				"where mov.solicitacao=sol.hisIdIni " +
				"and mov.lotaAtendente in (" + listalotacoes + ") " +
				"and sol.hisDtFim is not null " +
				"and sol.hisIdIni <> sol.idSolicitacao " +
				"and sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') " +
				"and sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') " +
				"order by mov.tipoMov").fetch();
		
		List<SrSolicitacao> lst = SrSolicitacao.find(
				"select sol, mov " +
				"from SrSolicitacao sol, SrMovimentacao mov " +
				"where mov.solicitacao=sol.idSolicitacao " +
				"and mov.lotaAtendente in (" + listalotacoes + ") " +
				"and sol.dtReg >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') " +
				"and sol.dtReg <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') " +
				"and exists (select 1 from SrMovimentacao where solicitacao = mov.solicitacao and dtIniMov > mov.dtIniMov " +
				"and lotaAtendente <> mov.lotaAtendente " +
				"and dtIniMov >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') " +
                "and dtIniMov <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss')) " +
               	"order by mov.tipoMov").fetch();
		
		SortedSet<String> set = new TreeSet<String>();
		TreeMap<String, String> map = new TreeMap<String, String>();
		
		Iterator it = lista.listIterator();
		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();
			SrSolicitacao solic = (SrSolicitacao) obj[0];
			SrMovimentacao mov = (SrMovimentacao) obj[1];
			String dtreg, descricao, lotaSolicitante, lotaAtendente, descrEstado, itemConfig, descServico;
			if (mov.getDtIniString() != null) {
				dtreg = mov.getDtIniString().toString();
			} else {
				dtreg = "";
			}
			//Descrição da solicitação - 3º elemento
			if (solic.getDescricao() != null) {
				descricao = solic.getDescricao().toString();
			} else {
				descricao = "";
			}
			//Lotação solicitante - 4º elemento
			if (solic.solicitante.getLotacao().getDescricao() != null) {
				lotaSolicitante = solic.solicitante.getLotacao().getDescricao();
			} else {
				lotaSolicitante = "";
			}
			// Lotação atendente - 5º elemento
			if (mov.getAtendenteString() != null) {
				lotaAtendente = mov.getAtendenteString();
			} else {
				lotaAtendente = "";
			}
			//Estado da solicitação - 6º elemento
				descrEstado = "";

			//Item de configuração - 7º elemento
			if (solic.itemConfiguracao != null) {
				itemConfig = solic.itemConfiguracao.tituloItemConfiguracao.toString();
			} else {
				itemConfig = "";
			}
			//Serviço - 8º elemento
			if (solic.servico != null) {
				descServico = solic.servico.tituloServico.toString();
			} else {
				descServico = "";
			}
			String solicitacao = dtreg + ";" + descricao + ";" + lotaSolicitante + ";" + lotaAtendente + ";" + descrEstado + ";" + itemConfig + ";" + descServico;
			set.add(solic.getCodigo().toString());
			map.put(solic.getCodigo().toString(), solicitacao);
		}
		Iterator itl = lst.listIterator(); 
		while (itl.hasNext()) {
			Object[] obj = (Object[]) itl.next();
			SrSolicitacao solic = (SrSolicitacao) obj[0];
			SrMovimentacao mov = (SrMovimentacao) obj[1];
			String dtreg, descricao, lotaSolicitante, lotaAtendente, descrEstado, itemConfig, descServico;
			if (mov.getDtIniString() != null) {
				dtreg = mov.getDtIniString().toString();
			} else {
				dtreg = "";
			}
			//Descrição da solicitação - 3º elemento
			if (solic.getDescricao() != null) {
				descricao = solic.getDescricao().toString();
			} else {
				descricao = "";
			}
			//Lotação solicitante - 4º elemento
			if (solic.solicitante.getLotacao().getDescricao() != null) {
				lotaSolicitante = solic.solicitante.getLotacao().getDescricao();
			} else {
				lotaSolicitante = "";
			}
			// Lotação atendente - 5º elemento
			if (mov.getAtendenteString() != null) {
				lotaAtendente = mov.getAtendenteString();
			} else {
				lotaAtendente = "";
			}
			//Estado da solicitação - 6º elemento
			descrEstado = "";
			
			//Item de configuração - 7º elemento
			if (solic.itemConfiguracao != null) {
				itemConfig = solic.itemConfiguracao.tituloItemConfiguracao.toString();
			} else {
				itemConfig = "";
			}
			//Serviço - 8º elemento
			if (solic.servico != null) {
				descServico = solic.servico.tituloServico.toString();
			} else {
				descServico = "";
			}
			String solicitacao = dtreg + ";" + descricao + ";" + lotaSolicitante + ";" + lotaAtendente + ";" + descrEstado + ";" + itemConfig + ";" + descServico;
			set.add(solic.getCodigo().toString());
			map.put(solic.getCodigo().toString(), solicitacao);
		}
		for (String s : set) {
			e.add(s);
			if (map.containsKey(s)) {
				String[] p = map.get(s).split(";", 8);
				for (int i = 0; i < p.length; i++){
					if (p[i] != null) {
						e.add(p[i]);
					}	
					else {
						e.add("");
					}
					
				}
			}	
		}
		return e;
	}
}

