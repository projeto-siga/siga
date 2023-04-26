package br.gov.jfrj.siga.ex.vo;

import br.gov.jfrj.siga.dp.DpPessoa;

public class AssinanteVO {
	
	DpPessoa subscritor;
	DpPessoa titular;
	String nmFuncao;
	String nmLotacao;
	String nmSubscritor;

	public AssinanteVO(DpPessoa subscritor, DpPessoa titular, String nmFuncao, String nmLotacao, String nmSubscritor) {
		this.subscritor = subscritor;
		this.titular = titular;
		this.nmFuncao = nmFuncao;
		this.nmLotacao = nmLotacao;
		this.nmSubscritor = nmSubscritor;
	}

	public DpPessoa getSubscritor() {
		return subscritor;
	}

	public void setSubscritor(DpPessoa subscritor) {
		this.subscritor = subscritor;
	}

	public DpPessoa getTitular() {
		return titular;
	}

	public void setTitular(DpPessoa titular) {
		this.titular = titular;
	}

	public String getNmFuncao() {
		return nmFuncao;
	}

	public void setNmFuncao(String nmFuncao) {
		this.nmFuncao = nmFuncao;
	}

	public String getNmLotacao() {
		return nmLotacao;
	}

	public void setNmLotacao(String nmLotacao) {
		this.nmLotacao = nmLotacao;
	}

	public String getNmSubscritor() {
		return nmSubscritor;
	}

	public void setNmSubscritor(String nmSubscritor) {
		this.nmSubscritor = nmSubscritor;
	}
}
