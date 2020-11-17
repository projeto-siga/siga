package br.gov.jfrj.siga.gc.model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.base.AplicacaoException;
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

	static public Pattern tagPattern = Pattern
			.compile("^([@#^])(?:([\\w-]+?)(?:-(\\d)(?:-(\\d+))?)?:)?([\\w\\d-]+)$");

	@Id
	@SequenceGenerator(sequenceName = "SIGAGC.hibernate_sequence", name = "gcTagSeq")
	@GeneratedValue(generator = "gcTagSeq")
	@Column(name = "ID_TAG")
	private long id;

	@ManyToOne(optional = false)
	private GcTipoTag tipo;

	@Column(name = "CATEGORIA", length = 32)
	private String categoria;

	@Column(name = "TITULO", length = 256)
	private String titulo;

	// @Column(name = "HIERARQUIA_INDICE")
	// private Integer indice;
	//
	// @Column(name = "ID_EXTERNA", length = 256)
	// private String ide;

	public void setId(long id) {
		this.id = id;
	}

	public void setTipo(GcTipoTag tipo) {
		this.tipo = tipo;
	}

	public GcTag() {
		// TODO Auto-generated constructor stub
	}

	public GcTag(GcTipoTag tipo, String categoria, String titulo) {
		super();
		this.tipo = tipo;
		this.setCategoria(categoria);
		this.setTitulo(titulo);
	}

	public static GcTag getInstance(String s, List<GcTag> lista,
			boolean atualizarTitulo, boolean criar) throws Exception {
		Matcher matcher = tagPattern.matcher(s);
		if (!matcher.find()) {
			throw new Exception("Tag inválido. Deve seguir o padrão: "
					+ GcTag.tagPattern.toString());
		}
		String tipo = matcher.group(1);
		String grupo = matcher.group(2);
		Integer indice = (matcher.group(3) != null) ? Integer.parseInt(matcher
				.group(3)) : null;
		String ide = matcher.group(4);
		String titulo = matcher.group(5);

		GcTag tag = null;

		// Tentar localizar na lista antes de buscar no banco de dados
		if (lista != null) {
			for (GcTag t : lista) {
				if (grupo != null && ide != null) {
					if (tipo.equals(t.getTipo().getNome()) && grupo.equals(t.getHierarquiaGrupo())
							&& ide.equals(t.getHierarquiaId())) {
						tag = t;
						break;
					}
				}
			}
		}

		GcTipoTag tipoTag;
		switch (tipo) {
		case "@":
			tipoTag = GcTipoTag.AR
					.findById(GcTipoTag.TIPO_TAG_CLASSIFICACAO);
			break;
		case "#":
			tipoTag = GcTipoTag.AR.findById(GcTipoTag.TIPO_TAG_HASHTAG);
			break;
		default:
			tipoTag = GcTipoTag.AR.findById(GcTipoTag.TIPO_TAG_ANCORA);
		}
		
		// Se não estiver na lista, busca no banco
		if (tag == null) {
			String query = "from GcTag where tipo = " + tipoTag.getId();
			if (grupo != null && ide != null) {
				query += " and categoria like '" + grupo + "-%-" + ide + "'";
			}
			List<GcTag> itens = GcTag.AR.find(query).fetch();

			if (itens.size() == 0 && !criar)
				return null;
			if (itens.size() == 1)
				tag = itens.get(0);
			if (itens.size() > 1)
				return null;
		}

		if (tag == null) {
			tag = new GcTag();
			tag.setCategoria(grupo + "-" + indice + "-" + ide);
			tag.tipo = tipoTag;
			tag.setTitulo(titulo);
		}

		// Atualiza o título
		if (titulo != null && !titulo.equals(tag.getTitulo())
				&& atualizarTitulo)
			tag.setTitulo(titulo);

		return tag;
	}

	@Override
	public int compareTo(GcTag o) {
		int i = 0;
		i = Long.valueOf(tipo.getId()).compareTo(Long.valueOf(o.tipo.getId()));
		if (i != 0)
			return i;
		i = GcBL.compareStrings(getCategoria(), o.getCategoria());
		if (i != 0)
			return i;
		return GcBL.compareStrings(getTitulo(), o.getTitulo());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((getCategoria() == null) ? 0 : getCategoria().hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		result = prime * result
				+ ((getTitulo() == null) ? 0 : getTitulo().hashCode());
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
		if (getCategoria() == null) {
			if (other.getCategoria() != null)
				return false;
		} else if (!getCategoria().equals(other.getCategoria()))
			return false;
		if (id != other.id)
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		if (getTitulo() == null) {
			if (other.getTitulo() != null)
				return false;
		} else if (!getTitulo().equals(other.getTitulo()))
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
		return this.getTitulo();
	}

	@Override
	public void setSigla(String sigla) {
		this.setTitulo(sigla);
	}

	@Override
	public String getDescricao() {
		return this.getTitulo();
	}

	@Override
	public void setDescricao(String descricao) {
		this.setTitulo(descricao);
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
		if (getTitulo() != null && getTitulo().trim().length() > 0) {
			String t = Texto.slugify(getTitulo(), true, true);
			query += " and lower(titulo) like '%" + t + "%'";
		}
		return GcTag.AR.find(query).fetch();
	}

	public static List<? extends Selecionavel> buscar(GcTag tag)
			throws Exception {
		String query = "from GcTag where 1=1";
		if (tag.getTitulo() != null && tag.getTitulo().trim().length() > 0) {
			String t = Texto.slugify(tag.getTitulo(), true, true);
			query += "and lower(titulo) like '%" + t + "%'";
		}
		return GcTag.AR.find(query).fetch();
	}

	@Override
	public String toString() {
		return (tipo.getId() == 1 ? "@" : (tipo.getId() == 2 ? "#" : "^"))
				+ (getCategoria() != null ? getCategoria() + ":" : "")
				+ getTitulo();
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

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getHierarquiaGrupo() {
		Matcher matcher = GcTag.tagPattern.matcher(this.toString());
		if (!matcher.find()) {
			return null;
		}
		String grupo = matcher.group(2);
		if (grupo == null || grupo.length() == 0)
			return null;

		return grupo;
	}

	public Integer getHierarquiaIndice() {
		Matcher matcher = GcTag.tagPattern.matcher(this.toString());
		if (!matcher.find()) {
			return null;
		}
		String s = matcher.group(3);
		if (s == null || s.length() == 0)
			return null;
		return Integer.valueOf(s);
	}

	public String getHierarquiaId() {
		Matcher matcher = GcTag.tagPattern.matcher(this.toString());
		if (!matcher.find()) {
			return null;
		}
		String id = matcher.group(4);
		if (id == null || id.length() == 0)
			return null;

		return id;
	}

	public void setHierarquiaGrupo(String grupo) {
		if (grupo == null) {
			this.categoria = null;
			return;
		}
		String s = grupo;
		if (getHierarquiaIndice() != null) {
			this.categoria += "-" + getHierarquiaIndice();
			if (getHierarquiaId() != null)
				this.categoria += "-" + getHierarquiaId();
		}

	}

	public void setHierarquiaIndice(Integer indice) {
		if (getHierarquiaGrupo() == null)
			throw new AplicacaoException(
					"Não é permitido atualizar o indice se um conhecimeto que não possui grupo.");
		if (getHierarquiaId() == null)
			this.categoria = getHierarquiaGrupo() + "-" + indice;
		else
			this.categoria = getHierarquiaGrupo() + "-" + indice + "-"
					+ getHierarquiaId();
	}

	public void setHierarquiaId(Long ide) {
		if (getHierarquiaGrupo() == null)
			throw new AplicacaoException(
					"Não é permitido atualizar o id externo se um conhecimeto que não possui grupo.");
		if (getHierarquiaIndice() == null)
			throw new AplicacaoException(
					"Não é permitido atualizar o id externo se um conhecimeto que não possui Indice.");
		if (ide == null)
			this.categoria = getHierarquiaGrupo() + "-" + getHierarquiaIndice();
		else
			this.categoria = getHierarquiaGrupo() + "-" + getHierarquiaIndice()
					+ "-" + ide;
	}

}
