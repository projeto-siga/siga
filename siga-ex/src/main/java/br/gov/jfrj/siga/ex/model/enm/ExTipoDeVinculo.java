package br.gov.jfrj.siga.ex.model.enm;

import java.util.Map;
import java.util.TreeMap;

import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum ExTipoDeVinculo implements IEnumWithId {
	RELACIONAMENTO(1, "Ver também", "Veja também", "Veja também"),

	ALTERACAO(2, "Alteracao", "Alterado por", "Altera"),

	REVOGACAO(3, "Revogação", "Revogado por", "Revoga"),

	CANCELAMENTO(4, "Cancelamento", "Cancelado por", "Cancela"),
	
	SEM_EFEITO (5, "Sem efeito", "Tornado sem efeito por", "Torna sem efeito");

	private final int id;
	private final String descr;
	private final String acao;
	private final String acaoInversa;

	ExTipoDeVinculo(int id, String descr, String acao, String acaoInversa) {
		this.id = id;
		this.descr = descr;
		this.acao = acao;
		this.acaoInversa = acaoInversa;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	public String getDescr() {
		return this.descr;
	}

	public String getAcao() {
		return this.acao;
	}

	public String getAcaoInversa() {
		return acaoInversa;
	}

	public static Map<String, String> toMap() {
		final Map<String, String> map = new TreeMap<String, String>();
		for (ExTipoDeVinculo i : values())
			map.put(i.name(), i.descr);
		return map;
	}
}
