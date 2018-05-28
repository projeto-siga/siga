package br.gov.jfrj.siga.tp.model;

import java.util.List;

import javax.validation.constraints.NotNull;

public class RelatorioConsumoMedio {
	
	@NotNull
	private Veiculo veiculo;

	@NotNull
	private Abastecimento abastecimentoInicial;
	
	@NotNull
	private Abastecimento abastecimentoFinal;
	
	private List<Missao> missoes;
	private Double kmPercorridos;
	private Double consumoMedio;
	
	
	public Veiculo getVeiculo() {
		return veiculo;
	}
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	public Abastecimento getAbastecimentoInicial() {
		return abastecimentoInicial;
	}
	public void setAbastecimentoInicial(Abastecimento abastecimentoInicial) {
		this.abastecimentoInicial = abastecimentoInicial;
	}
	public Abastecimento getAbastecimentoFinal() {
		return abastecimentoFinal;
	}
	public void setAbastecimentoFinal(Abastecimento abastecimentoFinal) {
		this.abastecimentoFinal = abastecimentoFinal;
	}
	public List<Missao> getMissoes() {
		return missoes;
	}
	public void setMissoes(List<Missao> missoes) {
		this.missoes = missoes;
	}
	public Double getKmPercorridos() {
		return kmPercorridos;
	}
	public void setKmPercorridos(Double kmPercorridos) {
		this.kmPercorridos = kmPercorridos;
	}
	public Double getConsumoMedio() {
		return consumoMedio;
	}
	public void setConsumoMedio(Double consumoMedio) {
		this.consumoMedio = consumoMedio;
	}
}