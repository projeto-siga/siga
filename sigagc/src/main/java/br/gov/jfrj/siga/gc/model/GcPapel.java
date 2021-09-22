package br.gov.jfrj.siga.gc.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "sigagc.gc_papel")
public class GcPapel extends Objeto {

	public static ActiveRecord<GcPapel> AR = new ActiveRecord<>(
			GcPapel.class);

	private static final long serialVersionUID = 2096492527289909346L;

	final static public long PAPEL_INTERESSADO = 1;
	
	final static public long PAPEL_EXECUTOR = 2;	
	
	@Id
	@Column(name = "ID_PAPEL")
	private java.lang.Long idPapel;

	@Column(name = "DESC_PAPEL")
	private java.lang.String descPapel;

	@OneToMany(mappedBy = "papel")
	private Set<GcMovimentacao> gcMovimentacaoSet;

	public java.lang.Long getIdPapel() {
		return idPapel;
	}

	public void setIdPapel(java.lang.Long idPapel) {
		this.idPapel = idPapel;
	}

	public java.lang.String getDescPapel() {
		return descPapel;
	}

	public void setDescPapel(java.lang.String descPapel) {
		this.descPapel = descPapel;
	}

	public Set<GcMovimentacao> getGcMovimentacaoSet() {
		return gcMovimentacaoSet;
	}

	public void setGcMovimentacaoSet(Set<GcMovimentacao> gcMovimentacaoSet) {
		this.gcMovimentacaoSet = gcMovimentacaoSet;
	}
	
	public Long getId(){
		return getIdPapel();
	}
	
	public void setId(Long id){
		setIdPapel(id);
	}
}
