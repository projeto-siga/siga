package reports;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import models.SrAcao;
import models.SrItemConfiguracao;
import models.SrSolicitacao;


import play.db.jpa.JPA;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.cp.CpComplexo;

public class SrRelAgendado extends RelatorioTemplate {

	public SrRelAgendado(Map parametros) throws DJBuilderException {
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
		
		this.setTitle("Relatório de Solicitações Agendadas por Localidade");
		this.addColuna("Local", 100, RelatorioRapido.ESQUERDA, true);
		this.addColuna("Solicitação", 30, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Item de Configuração", 30, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Ação", 35, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Data do Agendamento", 30, RelatorioRapido.CENTRO, false);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection processarDados() {

		List<Object> d = new LinkedList<Object>();
		String atendente = (String) parametros.get("atendente");
		Long percTotal = (long) 0;
			
		if (parametros.get("local").equals("0")) {
			if ((parametros.get("lotacao").equals("")) || (parametros.get("atendente") == null)) {
				String sqlQuery = "select complexo.nome_complexo, sol.id_Solicitacao, " 
									+ "item.titulo_item_configuracao, " 
									+ "acao.titulo_acao, "
									+ "mov.dt_Agendamento "
									+ "from Sr_Solicitacao sol inner join Sr_Movimentacao mov " 
									+ "on sol.id_Solicitacao = mov.id_solicitacao, "
									+ "corporativo.cp_complexo complexo, "
									+ "sr_item_configuracao item, "
									+ "sr_acao acao "
									+ "where mov.dt_Ini_Mov = (select max(movult.dt_Ini_Mov ) " 
									   + "from Sr_Movimentacao movult "
									   + "where movult.id_solicitacao = mov.id_solicitacao " 
									   + "and movult.id_tipo_movimentacao = 9 "
									   + "and movult.dt_agendamento is not null) " 
									   + "and not exists (select 1 from Sr_Movimentacao movfec "
									   + "where movfec.id_solicitacao = mov.id_solicitacao " 
									   + "and movfec.dt_Ini_Mov > mov.dt_Ini_Mov  "
									   + "and movfec.id_tipo_movimentacao <> 11) " 
									+ "and sol.id_complexo = complexo.id_complexo "
									+ "and sol.id_item_configuracao = item.id_item_configuracao "
									+ "and sol.id_acao = acao.id_acao " 
									+ "and  mov.dt_Ini_Mov >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
									+ "and  mov.dt_Ini_Mov <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') ";
						List query = JPA.em().createNativeQuery(sqlQuery).getResultList();
						Iterator it = query.listIterator(); 
						while (it.hasNext()) {
							Object[] obj = (Object[]) it.next();
							String itenslocais =  (String) obj[0];
							BigDecimal solicitacao = (BigDecimal) obj[1];
							String itensconfiguracao = (String) obj[2];
							String itensacao = (String) obj[3];
							Timestamp dtagendamento = (Timestamp) obj[4];
							SrSolicitacao sol = SrSolicitacao.findById(solicitacao.longValue());
							d.add(itenslocais);
							d.add(sol.getCodigo());
							d.add(itensconfiguracao);
							d.add(itensacao);
							if (dtagendamento != null)
								d.add(dtagendamento.toString());
							else d.add("");
						}
			} else {
							String querylot = "select idLotacao from DpLotacao where idLotacaoIni in (select idLotacaoIni " +
									"from DpLotacao where idLotacao in (" +  parametros.get("atendente") + "))";
							List lotacoes = JPA.em().createQuery(querylot).getResultList();
							StringBuilder listalotacoes= new StringBuilder();
							for (int i  = 0; i < lotacoes.size(); i++){
								listalotacoes.append(lotacoes.get(i));
								if (i < ( lotacoes.size() - 1)) listalotacoes.append(",");
							}
							String sqlQuery = "select complexo.nome_complexo, sol.id_Solicitacao, " 
									+ "item.titulo_item_configuracao, " 
									+ "acao.titulo_acao, "
									+ "mov.dt_Agendamento "
									+ "from Sr_Solicitacao sol inner join Sr_Movimentacao mov " 
									+ "on sol.id_Solicitacao = mov.id_solicitacao, "
									+ "corporativo.cp_complexo complexo, "
									+ "sr_item_configuracao item, "
									+ "sr_acao acao "
									+ "where mov.id_lota_atendente in (" + listalotacoes + ")"
									+ "and mov.dt_Ini_Mov = (select max(movult.dt_Ini_Mov ) " 
									   + "from Sr_Movimentacao movult "
									   + "where movult.id_solicitacao = mov.id_solicitacao " 
									   + "and movult.id_tipo_movimentacao = 9 "
									   + "and movult.dt_agendamento is not null) " 
									   + "and not exists (select 1 from Sr_Movimentacao movfec "
									   + "where movfec.id_solicitacao = mov.id_solicitacao " 
									   + "and movfec.dt_Ini_Mov > mov.dt_Ini_Mov  "
									   + "and movfec.id_tipo_movimentacao <> 11) " 
									+ "and sol.id_complexo = complexo.id_complexo "
									+ "and sol.id_item_configuracao = item.id_item_configuracao "
									+ "and sol.id_acao = acao.id_acao " 
									+ "and  mov.dt_Ini_Mov >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
									+ "and  mov.dt_Ini_Mov <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') ";
								List query = JPA.em().createNativeQuery(sqlQuery).getResultList();
								Iterator it = query.listIterator(); 
								while (it.hasNext()) {
									Object[] obj = (Object[]) it.next();
									String itenslocais =  (String) obj[0];
									BigDecimal solicitacao = (BigDecimal) obj[1];
									String itensconfiguracao = (String) obj[2];
									String itensacao = (String) obj[3];
									Timestamp dtagendamento = (Timestamp) obj[4];
									SrSolicitacao sol = SrSolicitacao.findById(solicitacao.longValue());
									d.add(itenslocais);
									d.add(sol.getCodigo());
									d.add(itensconfiguracao);
									d.add(itensacao);
									if (dtagendamento != null)
										d.add(dtagendamento.toString());
									else d.add("");
								}
					}
			} else {
				String querylot = "select idLotacao from DpLotacao where idLotacaoIni in (select idLotacaoIni " +
						"from DpLotacao where idLotacao in (" +  parametros.get("atendente") + "))";
				List lotacoes = JPA.em().createQuery(querylot).getResultList();
				StringBuilder listalotacoes= new StringBuilder();
				for (int i  = 0; i < lotacoes.size(); i++){
					listalotacoes.append(lotacoes.get(i));
					if (i < ( lotacoes.size() - 1)) listalotacoes.append(",");
				}
				String sqlQuery = "select complexo.nome_complexo, sol.id_Solicitacao, " 
						+ "item.titulo_item_configuracao, " 
						+ "acao.titulo_acao, "
						+ "mov.dt_Agendamento "
						+ "from Sr_Solicitacao sol inner join Sr_Movimentacao mov " 
						+ "on sol.id_Solicitacao = mov.id_solicitacao, "
						+ "corporativo.cp_complexo complexo, "
						+ "sr_item_configuracao item, "
						+ "sr_acao acao "
						+ "where mov.id_lota_atendente in (" + listalotacoes + ")"
						+ "and mov.dt_Ini_Mov = (select max(movult.dt_Ini_Mov ) " 
						   + "from Sr_Movimentacao movult "
						   + "where movult.id_solicitacao = mov.id_solicitacao " 
						   + "and movult.id_tipo_movimentacao = 9 "
						   + "and movult.dt_agendamento is not null) " 
						   + "and not exists (select 1 from Sr_Movimentacao movfec "
						   + "where movfec.id_solicitacao = mov.id_solicitacao " 
						   + "and movfec.dt_Ini_Mov > mov.dt_Ini_Mov  "
						   + "and movfec.id_tipo_movimentacao <> 11) " 
						+ "and sol.id_complexo = " + parametros.get("local") + " "
						+ "and sol.id_complexo = complexo.id_complexo "
						+ "and sol.id_item_configuracao = item.id_item_configuracao "
						+ "and sol.id_acao = acao.id_acao " 
						+ "and  mov.dt_Ini_Mov >= to_date('" + parametros.get("dtIni") + " 00:00:00','dd/MM/yy hh24:mi:ss') "
						+ "and  mov.dt_Ini_Mov <= to_date('" + parametros.get("dtFim") + " 23:59:59','dd/MM/yy hh24:mi:ss') ";
					List query = JPA.em().createNativeQuery(sqlQuery).getResultList();
					Iterator it = query.listIterator(); 
					while (it.hasNext()) {
						Object[] obj = (Object[]) it.next();
						String itenslocais =  (String) obj[0];
						BigDecimal solicitacao = (BigDecimal) obj[1];
						String itensconfiguracao = (String) obj[2];
						String itensacao = (String) obj[3];
						Timestamp dtagendamento = (Timestamp) obj[4];
						SrSolicitacao sol = SrSolicitacao.findById(solicitacao.longValue());
						d.add(itenslocais);
						d.add(sol.getCodigo());
						d.add(itensconfiguracao);
						d.add(itensacao);
						if (dtagendamento != null)
							d.add(dtagendamento.toString());
						else d.add("");
					}
			}
		return d;
		}
		
}
