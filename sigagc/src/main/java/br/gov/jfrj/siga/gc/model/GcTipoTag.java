package br.gov.jfrj.siga.gc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "sigagc.gc_tipo_tag")
public class GcTipoTag extends Objeto {
	public static final long TIPO_TAG_CLASSIFICACAO = 1;
	public static final long TIPO_TAG_HASHTAG = 2;
	public static final long TIPO_TAG_ANCORA = 3;

	public static ActiveRecord<GcTipoTag> AR = new ActiveRecord<>(
			GcTipoTag.class);

	@Id
	@Column(name = "ID_TIPO_TAG")
	private Long id;

	@Column(name = "NOME_TIPO_TAG", nullable = false)
	private String nome;

	public void setId(Long id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public GcTipoTag() {
		super();
	}

	public GcTipoTag(long id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	static public GcTipoTag findBySymbol(String symbol) {
		GcTipoTag tipoTag;
		switch (symbol) {
		case "@":
			tipoTag = GcTipoTag.AR.findById(GcTipoTag.TIPO_TAG_CLASSIFICACAO);
			break;
		case "#":
			tipoTag = GcTipoTag.AR.findById(GcTipoTag.TIPO_TAG_HASHTAG);
			break;
		default:
			tipoTag = GcTipoTag.AR.findById(GcTipoTag.TIPO_TAG_ANCORA);
		}
		return tipoTag;
	}
}
