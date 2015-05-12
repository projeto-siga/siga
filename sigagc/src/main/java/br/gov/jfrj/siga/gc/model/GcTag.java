package br.gov.jfrj.siga.gc.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.gc.ObjetoSelecionavel;
import br.gov.jfrj.siga.gc.util.GcBL;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.model.Selecionavel;

@Entity
@Table(name = "GC_TAG", schema = "SIGAGC")
@NamedQueries({ @NamedQuery(name = "listarTagCategorias", query = "select t.categoria from GcTag t where t.categoria is not null group by t.categoria order by t.categoria") })
public class GcTag extends Objeto implements Comparable<GcTag>,
		ObjetoSelecionavel {
	public static ActiveRecord<GcTag> AR = new ActiveRecord<>(GcTag.class);

	@Id
	@SequenceGenerator(sequenceName = "SIGAGC.hibernate_sequence", name = "gcTagSeq")
	@GeneratedValue(generator = "gcTagSeq")
	@Column(name = "ID_TAG")
	public long id;

	@ManyToOne(optional = false)
	public GcTipoTag tipo;

	@Column(name = "CATEGORIA", length = 10)
	public String categoria;

	@Column(name = "TITULO", length = 256)
	public String titulo;

	public GcTag() {
		// TODO Auto-generated constructor stub
	}

	public GcTag(GcTipoTag tipo, String categoria, String titulo) {
		super();
		this.tipo = tipo;
		this.categoria = categoria;
		this.titulo = titulo;
	}

	@Override
	public int compareTo(GcTag o) {
		int i = 0;
		i = Long.valueOf(tipo.id).compareTo(Long.valueOf(o.tipo.id));
		if (i != 0)
			return i;
		i = GcBL.compareStrings(categoria, o.categoria);
		if (i != 0)
			return i;
		return GcBL.compareStrings(titulo, o.titulo);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		GcTag other = (GcTag) obj;
		if (categoria == null) {
			if (other.categoria != null)
				return false;
		} else if (!categoria.equals(other.categoria))
			return false;
		if (id != other.id)
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		if (titulo == null) {
			if (other.titulo != null)
				return false;
		} else if (!titulo.equals(other.titulo))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public void setId(Long id) {
		if (id == null) {
			this.id = 0;
			return;
		}
		this.id = id;
	}

	@Override
	public String getSigla() {
		return this.titulo;
	}

	@Override
	public void setSigla(String sigla) {
		this.titulo = sigla;
	}

	@Override
	public String getDescricao() {
		return this.titulo;
	}

	@Override
	public void setDescricao(String descricao) {
		this.titulo = descricao;
	}

	@Override
	public ObjetoSelecionavel selecionar(String sigla) throws Exception {
		setSigla(sigla);
		List<GcTag> itens = (List<GcTag>) buscar(this);
		if (itens.size() == 0 || itens.size() > 1)
			return null;
		return itens.get(0);
	}

	@Override
	public List<? extends ObjetoSelecionavel> buscar() throws Exception {
		String query = "from GcTag where 1=1";
		if (titulo != null && titulo.trim().length() > 0) {
			String t = Texto.slugify(titulo, true, true);
			query += " and titulo like '%" + t + "%'";
		}
		return GcTag.AR.find(query).fetch();
	}

	public static List<? extends Selecionavel> buscar(GcTag tag)
			throws Exception {
		String query = "from GcTag where 1=1";
		if (tag.titulo != null && tag.titulo.trim().length() > 0) {
			String t = Texto.slugify(tag.titulo, true, true);
			query += " and titulo like '%" + t + "%'";
		}
		return GcTag.AR.find(query).fetch();
	}

	@Override
	public String toString() {
		return (tipo.id == 1 ? "@" : (tipo.id == 2 ? "#" : "^"))
				+ (categoria != null ? categoria + ":" : "") + titulo;
	}

	public GcTipoTag getTipo() {
		return tipo;
	}

	public String getCategoria() {
		return categoria;
	}

	public String getTitulo() {
		return titulo;
	}
}
