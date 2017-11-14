package br.gov.jfrj.siga.idp.jwt;

public class SigaJwtOptionsBuilder {

	private String password;
	private long ttlToken;
	
	public SigaJwtOptionsBuilder setTTL(long ttlToken) {
		this.ttlToken = ttlToken; 
		return this;
	}

	public SigaJwtOptionsBuilder setPassword(String password) {
		this.password = password;
		return this;
	}

	public SigaJwtOptions build() {
		return new SigaJwtOptions(password,ttlToken);
	}

}
