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

	public CpToken() {
	}

	public long getId() {
		return getIdToken();
	}

	public void setId(long id) {
		setIdToken(id);
	}
	
}