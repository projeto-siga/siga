package br.gov.jfrj.siga.sr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "SR_TIPO_PERMISSAO_LISTA", schema = "SIGASR")
public class SrTipoPermissaoLista extends Objeto{
	
	public static ActiveRecord<SrTipoPermissaoLista> AR = new ActiveRecord<>(SrTipoPermissaoLista.class);

	final static public long GESTAO = 1; 

	final static public long PRIORIZACAO = 2; 

	final static public long INCLUSAO = 3; 
	
	final static public long CONSULTA = 4; 

	@Id
	@Column(name = "ID_TIPO_PERMISSAO")
	private Long idTipoPermissaoLista;
	
	@Column(name = "DESCR_TIPO_PERMISSAO", nullable = false)
	private String descrTipoPermissaoLista;
	
	public void setIdTipoPermissaoLista(Long idTipoPermissaoLista) {
		this.idTipoPermissaoLista = idTipoPermissaoLista;
	}

	public void setDescrTipoPermissaoLista(String descrTipoPermissaoLista) {
		this.descrTipoPermissaoLista = descrTipoPermissaoLista;
	}

	public SrTipoPermissaoLista(int id, String descricao) {
		super();
		this.idTipoPermissaoLista = Long.valueOf(id);
		this.descrTipoPermissaoLista = descricao;
	}

	public Long getIdTipoPermissaoLista() {
		return idTipoPermissaoLista;
	}
	
	public String getDescrTipoPermissaoLista() {
		return descrTipoPermissaoLista;
	}
	
	/**
	 * Classe que representa um V.O. de {@link SrTipoPermissaoLista}.
	 */
	public class SrTipoPermissaoListaVO {
		
		public Long idTipoPermissaoLista;
		public String descrTipoPermissaoLista;
		
		public SrTipoPermissaoListaVO(Long id, String descricao) {
			this.idTipoPermissaoLista = id;
			this.descrTipoPermissaoLista= descricao;
		}
	}
	
	public SrTipoPermissaoListaVO toVO() {
		return new SrTipoPermissaoListaVO(this.idTipoPermissaoLista, this.descrTipoPermissaoLista);
	}
	
}
