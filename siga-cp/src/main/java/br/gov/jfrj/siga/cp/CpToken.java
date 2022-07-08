package br.gov.jfrj.siga.cp;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the CP_TOKEN database table.
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "corporativo.cp_token")
@NamedQueries({
	@NamedQuery(name="CpToken.findAll", query="SELECT t FROM CpToken t"),
	@NamedQuery(name="CpToken.findById", query="SELECT t.idToken FROM CpToken t where t.idToken = :id ")
})

public class CpToken extends AbstractCpToken {
	
	final static public long TOKEN_URLPERMANENTE = 1;
	final static public long TOKEN_PIN = 2;
	final static public long TOKEN_SENHA = 3;
	final static public long TOKEN_COD_ACESSO_EXTERNO_AO_DOCUMENTO = 4;

	public CpToken() {
	}

	public long getId() {
		return getIdToken();
	}

	public void setId(long id) {
		setIdToken(id);
	}
	
}