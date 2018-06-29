package br.gov.jfrj.siga.idp.jwt;

/**
 * Constrói um SigaJwtOptions
 * @author kpf
 *
 */
public class SigaJwtOptionsBuilder {

	private String modulo;
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

	public SigaJwtOptionsBuilder setModulo(String modulo) {
		this.modulo = modulo;
		return this;
	}

	public SigaJwtOptions build() {
		return new SigaJwtOptions(password,ttlToken,modulo);
	}

}
