package br.gov.jfrj.siga.cp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import br.gov.jfrj.siga.model.Objeto;


public class AbstractCpUnidadeMedida extends Objeto implements Serializable{

	private Integer idUnidadeMedida;
	private String descricao;
	
	public void setIdUnidadeMedida(Integer idUnidadeMedida) {
		this.idUnidadeMedida = idUnidadeMedida;
	}
	public Integer getIdUnidadeMedida() {
		return idUnidadeMedida;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getDescricao() {
		return descricao;
	}


}
