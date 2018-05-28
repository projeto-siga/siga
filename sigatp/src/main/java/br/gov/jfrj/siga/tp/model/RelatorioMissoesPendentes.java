package br.gov.jfrj.siga.tp.model;

public class RelatorioMissoesPendentes {
	private Condutor condutor;
	private Long totalMissoes;

	public Condutor getCondutor() {
		return condutor;
	}

	public void setCondutor(Condutor condutor) {
		this.condutor = condutor;
	}

	public Long getTotalMissoes() {
		return totalMissoes;
	}

	public void setTotalMissoes(Long totalMissoes) {
		this.totalMissoes = totalMissoes;
	}
}