package br.gov.jfrj.siga.gc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "GC_TIPO_TAG", schema = "SIGAGC")
public class GcTipoTag extends Objeto {
	public static final long TIPO_TAG_CLASSIFICACAO = 1;
	public static final long TIPO_TAG_HASHTAG = 2;
	public static final long TIPO_TAG_ANCORA = 3;

	public static ActiveRecord<GcTipoTag> AR = new ActiveRecord<>(
			GcTipoTag.class);

	@Id
	@Column(name = "ID_TIPO_TAG")
	private long id;

	@Column(name = "NOME_TIPO_TAG", nullable = false)
	private String nome;

	public void setId(long id) {
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

	public long getId() {
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
