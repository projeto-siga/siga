package br.gov.jfrj.siga.ex.util.BIE;

import java.util.Arrays;
import java.util.List;


public enum Unidade {

	PRES("Presidência", "PRES", "SECPRES", "ATPR"),
	VICE_PRESIDENCIA("Vice-Presidência", "VPC", "GAB/VPC", "AAVP"),
	EMARF("EMARF", "ASEC"),
	SECRETARIA_GERAL_TRF("Secretaria Geral", "SG", "SEASGP", "ATEG"),
	SECRETARIA_DE_RECURSOS_HUMANOS_TRF("Secretaria de Gestão de Pessoas", "SGP"),
	CCJF("CCJF", "CCJF"),
	SPO("Secretaria de Plan., Orç. e Finanças", "SPO", "SEASPO"),
	OUTRA("Outra", "XXX");

	private String descricao;
	
	private List<String> siglas;

	public String getDescricao() {
		return descricao;
	}

	Unidade(String descricao, String... siglas) {
		this.descricao = descricao;
		this.siglas = Arrays.asList(siglas);
	}

	public static Unidade porSigla(String sigla) {
		for (Unidade l : Unidade.values())
			if (l.getSiglas().contains(sigla))
				return l;
		return Unidade.OUTRA;
	}

	public List<String> getSiglas() {
		return siglas;
	}

	public void setSiglas(List<String> siglas) {
		this.siglas = siglas;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
