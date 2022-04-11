package br.gov.jfrj.siga.model.dao;

public enum BancoDeDados {
	ORACLE("Oracle"), MYSQL("MySQL"), POSTGRESQL("PostgreSQL");

	private String descr;

	private BancoDeDados(String descr) {
		this.descr = descr;
	}

	public String getDescr() {
		return this.descr;
	}
}
