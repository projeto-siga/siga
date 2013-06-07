package br.gov.jfrj.siga.cp;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
public class AbstractCpUnidadeMedida extends Objeto implements Serializable{

	@Id
	@Column(name = "ID_UNIDADE_MEDIDA", nullable = false)
	private Long idUnidadeMedida;
	
	@Column(name = "DESCR_UNIDADE_MEDIDA")
	private String descricao;
	
	public void setIdUnidadeMedida(Long idUnidadeMedida) {
		this.idUnidadeMedida = idUnidadeMedida;
	}
	public Long getIdUnidadeMedida() {
		return idUnidadeMedida;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getDescricao() {
		return descricao;
	}


}
