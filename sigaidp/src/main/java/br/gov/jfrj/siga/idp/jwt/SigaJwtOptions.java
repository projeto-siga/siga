package br.gov.jfrj.siga.idp.jwt;

public class SigaJwtOptions {

	private String password;
	private long ttlToken;
	
	public SigaJwtOptions(String password, long ttlToken) {
		this.password = password;
		this.ttlToken = ttlToken;
	}

	public String getPassword() {
		return password;
	}

	public long getTtlToken() {
		return ttlToken;
	}

}
