package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import models.SrAcao.SrAcaoVO;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import play.db.jpa.GenericModel;

@Entity
@Table(name = "SR_TIPO_PERMISSAO_LISTA", schema = "SIGASR")
public class SrTipoPermissaoLista extends GenericModel{

	final static public long GESTAO = 1; 

	final static public long PRIORIZACAO = 2; 

	final static public long INCLUSAO = 3; 
	
	final static public long CONSULTA = 4; 

	@Id
	@Column(name = "ID_TIPO_PERMISSAO")
	public Long idTipoPermissaoLista;
	
	@Column(name = "DESCR_TIPO_PERMISSAO", nullable = false)
	public String descrTipoPermissaoLista;
	
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
