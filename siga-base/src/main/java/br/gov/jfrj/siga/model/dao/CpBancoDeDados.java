package br.gov.jfrj.siga.model.dao;

public enum CpBancoDeDados {
	ORACLE("Oracle"), MYSQL("MySQL"), POSTGRESQL("PostgreSQL");

	private String descr;

	private CpBancoDeDados(String descr) {
		this.descr = descr;
	}

	public String getDescr() {
		return this.descr;
	}
}
