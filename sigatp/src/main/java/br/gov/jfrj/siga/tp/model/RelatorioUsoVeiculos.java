package br.gov.jfrj.siga.tp.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;
import javax.validation.constraints.NotNull;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.tp.util.FormatarDataHora;

public class RelatorioUsoVeiculos {
	@NotNull
	private Calendar dataInicio;

	private Calendar dataFim;
	
	private static final String _HORA_FIM_DIA = "23:59:59";
    private static final String _HORA_INICIO_DIA = "00:00:00";
    
    public class LinhaRelatorio implements Comparable<LinhaRelatorio> {
    	private Calendar dataHoraSaida;
    	private Calendar dataHoraRetorno;
    	private String descricaoVeiculo;
    	private String descricaoFinalidade;
    	private double distanciaPercorrida;
    	private String sequenceMissao;
    	
    	public LinhaRelatorio(Calendar dataHoraSaida, Calendar dataHoraRetorno, String descricaoVeiculo, String descricaoFinalidade, double distanciaPercorrida, String sequenceMissao) {
			this.dataHoraSaida = dataHoraSaida;
			this.dataHoraRetorno = dataHoraRetorno;
			this.descricaoVeiculo = descricaoVeiculo;
			this.descricaoFinalidade = descricaoFinalidade;
			this.distanciaPercorrida = distanciaPercorrida;
			this.sequenceMissao = sequenceMissao;
			
		}

		public double getDistanciaPercorrida() {
			return distanciaPercorrida;
		}

		public String getSequenceMissao() {
			return sequenceMissao;
		}

		public Calendar getDataHoraSaida() {
			return dataHoraSaida;
		}

		public Calendar getDataHoraRetorno() {
			return dataHoraRetorno;
		}

		public String getDescricaoVeiculo() {
			return descricaoVeiculo;
		}

		public String getDescricaoFinalidade() {
			return descricaoFinalidade;
		}

		@Override
		public int compareTo(LinhaRelatorio o) {
			int retorno = dataHoraRetorno.compareTo(o.getDataHoraRetorno());
			return retorno == 0 ? this.descricaoVeiculo.compareTo(o.getDescricaoVeiculo()) : retorno;
		}
    }

	//lista de missoes finalizadas, com requisicoes atendidas, ordenadas por data e depois veiculo
	private List<LinhaRelatorio> linhas;

	public Calendar getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Calendar dataInicio) {
		this.dataInicio = dataInicio;
		try {
			dataInicio.setTime(FormatarDataHora.formatarDataHora(dataInicio, _HORA_INICIO_DIA));
		} catch (Exception e) {
			throw new RuntimeException("Erro ao formatar a data de inicio: " + e.getMessage());
		}
	}

	public Calendar getDataFim() {
		return dataFim;
	}

	public void setDataFim(Calendar dataFim) {
		this.dataFim = dataFim;
		try {
			dataFim.setTime(FormatarDataHora.formatarDataHora(dataFim, _HORA_FIM_DIA));
		} catch(Exception e) {
			throw new RuntimeException("Erro ao formatar a data de fim: " + e.getMessage());
		}
	}

	public List<LinhaRelatorio> getLinhas() {
		return linhas;
	}

	public void setLinhas(List<LinhaRelatorio> linhas) {
		this.linhas = linhas;
	}
	
	public boolean adicionaLinha(LinhaRelatorio linha) {
		if(linha == null || linha.getDataHoraSaida() == null || linha.getDataHoraRetorno() == null 
				|| linha.getDescricaoVeiculo() == null || linha.getDescricaoFinalidade() == null) {
			return false;
		}
		if(linhas == null) {
			linhas = new ArrayList<RelatorioUsoVeiculos.LinhaRelatorio>();
		}
		this.linhas.add(linha);
		return true;
	}

	@SuppressWarnings("unchecked")
	public void recuperarUsoVeiculos(CpOrgaoUsuario orgao) {
    	if(!datasEstaoCorretas()) {
    		throw new RuntimeException("Datas de inicio ou fim invalidas.");
    	}
    	
    	List<Missao> lista = null;
    	
    	String qrl = "SELECT distinct m "
                 + "FROM Missao m "
                 + "INNER JOIN m.requisicoesTransporte r "
                 + "INNER JOIN r.andamentos a "
                 + "WHERE m.cpOrgaoUsuario = :orgao and m.estadoMissao = :estadoMissao "
                 + "and   m.dataHoraRetorno BETWEEN :dataInicio AND :dataFim? "
                 + "and   m.id = a.missao.id "
                 + "AND   a.estadoRequisicao in (:estadoRequisicao1, :estadoRequisicao2) "
                 + "ORDER BY m.id";

		Query qry = ContextoPersistencia.em().createQuery(qrl);
		qry.setParameter("orgao", orgao);
		qry.setParameter("estadoMissao", EstadoMissao.FINALIZADA);
		qry.setParameter("dataInicio", dataInicio);
		qry.setParameter("dataFim", dataFim);
		qry.setParameter("estadoRequisicao1", EstadoRequisicao.ATENDIDA);
		qry.setParameter("estadoRequisicao2", EstadoRequisicao.ATENDIDAPARCIALMENTE);
		lista = (List<Missao>) qry.getResultList();
		
		for (int i = 0; i < lista.size(); i++) {
			Missao m = lista.get(i);
			
			StringBuffer finalidadeCondensada = new StringBuffer();
			boolean primeiro = true;
			
			for (Iterator<RequisicaoTransporte> iter = m.getRequisicoesTransporte().iterator(); iter.hasNext();) {
				RequisicaoTransporte r = (RequisicaoTransporte) iter.next();
				
				if(!primeiro) {
					finalidadeCondensada.append("; ");
				} else {
					primeiro = false;
				}
				
				finalidadeCondensada.append(r.getTipoFinalidade().getDescricao());
				if(!(r.getFinalidade() == null || r.getFinalidade().isEmpty())) {
					finalidadeCondensada.append(" - " + r.getFinalidade());
				}
			}
			
			double distanciaPercorrida = m.getOdometroRetornoEmKm() - m.getOdometroSaidaEmKm();
			
			LinhaRelatorio novaLinha = new LinhaRelatorio(m.getDataHoraSaida(), m.getDataHoraRetorno(), 
												m.getVeiculo().getDadosParaExibicao(), finalidadeCondensada.toString(), 
												distanciaPercorrida, m.getSequence());
			this.adicionaLinha(novaLinha);
		}
		
		Collections.sort(linhas);

    }

	public boolean datasEstaoCorretas() {
		if(dataInicio == null || dataFim == null || dataFim.getTime().before(dataInicio.getTime())) {
    		return false;
    	}
		return true;
	}

}