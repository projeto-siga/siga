package br.gov.jfrj.siga.sr.model;

import java.io.Serializable;
import java.util.Objects;

public class DadosRHId implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4073994018672583875L;
	private Long pessoa_id;
	private Long lotacao_id;
	
	public DadosRHId() {
		
	}
	
	public DadosRHId(Long pessoa_id, Long lotacao_id) {
		this.pessoa_id = pessoa_id;
		this.lotacao_id = lotacao_id;
	}
	
	 @Override
	  public boolean equals(Object o) {
	      if (this == o) return true;
	      if (o == null || getClass() != o.getClass()) return false;
	      DadosRHId dadosRHId1 = (DadosRHId) o;
	      if (pessoa_id != dadosRHId1.pessoa_id) return false;
	      return lotacao_id == dadosRHId1.lotacao_id;
	  }

	  @Override
	  public int hashCode() {
	      return Objects.hash(pessoa_id, lotacao_id);
	  }
	
}
