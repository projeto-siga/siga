package br.gov.jfrj.siga.idp.jwt;

/**
 * Opcões de inicialização do provider
 * @author kpf
 *
 */
public class SigaJwtOptions {

	private String password;
	private String modulo;
	private long ttlToken;
	
	public SigaJwtOptions(String password, long ttlToken, String modulo) {
		this.modulo = modulo;
		this.password = password;
		this.ttlToken = ttlToken;
	}

	public String getPassword() {
		return password;
	}

	public long getTtlToken() {
		return ttlToken;
	}
	
	public String getModulo() {
		return modulo;
	}

}
