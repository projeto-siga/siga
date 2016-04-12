package br.gov.jfrj.siga.sr.model;

import java.util.Date;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sr.util.SrViewUtil;


public class SrAtendimento implements Comparable<SrAtendimento> {

	private SrSolicitacao solicitacao;
	private Date dataInicio;
	private Date dataFinal;
	private SrValor tempoAtendimento;
	private SrFaixa faixa;
	private DpLotacao lotacaoAtendente;
	private DpLotacao lotacaoAtendenteDestino;
	private DpPessoa pessoaAtendente;
	private String tipoAtendimento;
	private String itemConfiguracao;
	private String acao;
	
	public SrAtendimento() {
		
	}
	public SrAtendimento(SrSolicitacao solicitacao, Date dataInicio,
			Date dataFinal, SrValor tempoAtendimento,
			DpLotacao lotacaoAtendente, DpLotacao lotacaoAtendenteDestino,
			DpPessoa pessoaAtendente, String tipoAtendimento,
			String itemConfiguracao, String acao) {
		this.solicitacao = solicitacao;
		this.dataInicio = dataInicio;
		this.dataFinal = dataFinal;
		this.tempoAtendimento = tempoAtendimento;
		this.lotacaoAtendente = lotacaoAtendente;
		this.lotacaoAtendenteDestino = lotacaoAtendenteDestino;
		this.pessoaAtendente = pessoaAtendente;
		this.tipoAtendimento = tipoAtendimento;
		this.itemConfiguracao = itemConfiguracao;
		this.acao = acao;
	}
	
	public SrAtendimento(SrSolicitacao solicitacao, Date dataFinal, DpPessoa pessoaAtendente, 
			String itemConfiguracao, String acao, String tipoAtendimento,  
			DpLotacao lotacaoAtendenteDestino) {
		this.solicitacao = solicitacao;
		this.dataFinal = dataFinal;
		this.pessoaAtendente = pessoaAtendente;
		this.itemConfiguracao = itemConfiguracao;
		this.acao = acao;
		this.tipoAtendimento = tipoAtendimento;
		this.lotacaoAtendenteDestino = lotacaoAtendenteDestino;
	}
	
	public SrSolicitacao getSolicitacao() {
		return solicitacao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public SrValor getTempoAtendimento() {
		return tempoAtendimento;
	}

	public SrFaixa getFaixa() {
		return faixa;
	}

	public DpLotacao getLotacaoAtendente() {
		return lotacaoAtendente;
	}	
	
	public DpPessoa getPessoaAtendente() {
		return pessoaAtendente;
	}
	
	public String getItemConfiguracao() {
		return itemConfiguracao;
	}

	public DpLotacao getLotacaoAtendenteDestino() {
		return lotacaoAtendenteDestino;
	}
	
	public String getTipoAtendimento() {
		return tipoAtendimento;
	}

	public String getAcao() {
		return acao;
	}
	
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	
	public void setTempoAtendimento(SrValor tempoAtendimento) {
		this.tempoAtendimento = tempoAtendimento;
	}

	public void setLotacaoAtendente(DpLotacao lotacaoAtendente) {
		this.lotacaoAtendente = lotacaoAtendente;
	}

	private void setFaixa(SrFaixa faixa) {
		this.faixa = faixa;
	}

	public String getDataInicioDDMMYYYYHHMMSS() {
		return SrViewUtil.toDDMMYYYYHHMMSS(dataInicio);
	}
	
	public String getDataFinalDDMMYYYYHHMMSS() {
		return SrViewUtil.toDDMMYYYYHHMMSS(dataFinal);
	}
	
	public enum SrFaixa {		
		ATE_1(1, "Ate 1 horas"), ATE_2(2, "Ate 2 horas"), ATE_4(3, "Ate 4 horas"), 
		ATE_8(4, "Ate 8 horas"), ATE_12(5, "Ate 12 horas"),ATE_16(6, "Ate 16 horas"), 
		ATE_24(7, "Ate 24 horas"), ACIMA_24(8, "Acima de 24 horas"), ATE_15MIN(9, "Ate 15 minutos"),
		ATE_3(10, "Ate 3 horas"), ATE_15(11, "Ate 15 horas"), ACIMA_15(12, "Acima de 15 horas");
		
		public int idFaixa;
		public String descricao;
		
		private SrFaixa(int idFaixa, String descricao) {
			this.idFaixa = idFaixa;
			this.descricao = descricao;
		}
	}
	
	public void definirFaixa(CpOrgaoUsuario orgao) {
		if (tempoAtendimento != null) {
			if (orgao.getAcronimoOrgaoUsu().equals("JFRJ"))	
				setFaixaJFRJ(tempoAtendimento.getValorEmHora());
			else
				setFaixaTRF(tempoAtendimento.getValorEmHora());
		}
	}
		
	private void setFaixaTRF(float horas) {
		if (horas <= 0.25)
			 setFaixa(SrFaixa.ATE_15MIN);
		else if (horas > 0.25 && horas <= 1)
			setFaixa(SrFaixa.ATE_1);
		else if (horas > 1 && horas <= 3)
			setFaixa(SrFaixa.ATE_3);
		else if (horas > 3 && horas <= 8)
			setFaixa(SrFaixa.ATE_8);
		else if (horas > 8 && horas <= 15)
			setFaixa(SrFaixa.ATE_15);
		else
			setFaixa(SrFaixa.ACIMA_15);
	}
	
	private void setFaixaJFRJ(float horas) {
		if (horas <= 1)
			setFaixa(SrFaixa.ATE_1);
		else if (horas > 1 && horas <= 2)
			setFaixa(SrFaixa.ATE_2);
		else if (horas > 2 && horas <= 4)
			setFaixa(SrFaixa.ATE_4);
		else if (horas > 4 && horas <= 8)
			setFaixa(SrFaixa.ATE_8);
		else if (horas > 8 && horas <= 12)
			setFaixa(SrFaixa.ATE_12);
		else if (horas > 12 && horas <= 16)
			setFaixa(SrFaixa.ATE_16);
		else if (horas > 16 && horas <= 24)
			setFaixa(SrFaixa.ATE_24);
		else
			setFaixa(SrFaixa.ACIMA_24);
	}
	@Override
	public int compareTo(SrAtendimento o) {
		if (this != null && o != null) {
			if (this.getTempoAtendimento() == null || o.getTempoAtendimento() == null || 
					this.getTempoAtendimento().toString().equals(o.getTempoAtendimento().toString())) {
				if (this.getSolicitacao().getCodigo().equals(o.getSolicitacao().getCodigo()))
					return this.getDataInicio().compareTo(o.getDataInicio());
				else
					return this.getSolicitacao().getCodigo().compareTo(o.getSolicitacao().getCodigo());
			} 
			return this.getTempoAtendimento().compareTo(o.getTempoAtendimento());
		}
		return 0;
	}
}
