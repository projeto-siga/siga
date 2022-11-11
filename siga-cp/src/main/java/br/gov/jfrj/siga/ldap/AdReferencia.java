package br.gov.jfrj.siga.ldap;

/**
 * Representa os elementos que estão fora da estrutura padrão na árvore do AD
 * @author kpf
 *
 */
public class AdReferencia extends AdObjeto {

	/**
	 * Campo com o valor DN do objeto LDAP externo.
	 */
	private String referencia;
	
	public AdReferencia(String nome, String idExterna, String dnDominio) {
		super(nome, idExterna, dnDominio);
	}

	public void setReferencia(String ref) {
		this.referencia = ref;
	}
	
	public String getReferencia() {
		return referencia;
	}
	
	@Override
	public String getNomeCompleto() {
		return referencia;
	}

}
