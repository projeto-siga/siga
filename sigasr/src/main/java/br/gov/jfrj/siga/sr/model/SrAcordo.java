package br.gov.jfrj.siga.sr.model;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.jfrj.siga.base.util.Catalogs;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sr.model.vo.SrAcordoVO;
import br.gov.jfrj.siga.vraptor.entity.HistoricoSuporteVraptor;

@Entity
@Table(name = "SR_ACORDO", schema = Catalogs.SIGASR)
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class SrAcordo extends HistoricoSuporteVraptor implements Selecionavel {
	private static final long serialVersionUID = 1L;
	
	public static ActiveRecord<SrAcordo> AR = new ActiveRecord<>(SrAcordo.class);	

	@Id
	@SequenceGenerator(sequenceName = "SR_ACORDO_SEQ", schema = Catalogs.SIGASR, name = "srAcordoSeq")
	@GeneratedValue(generator = "srAcordoSeq")
	@Column(name = "ID_ACORDO")
	public Long idAcordo;

	@Column(name = "NOME_ACORDO")
	public String nomeAcordo;

	@Column(name = "DESCR_ACORDO")
	public String descrAcordo;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrAcordo acordoInicial;

	@OneToMany(targetEntity = SrAcordo.class, mappedBy = "acordoInicial", fetch = FetchType.LAZY)
	//@OrderBy("hisDtIni desc")
	public List<SrAcordo> meuAcordoHistoricoSet;

	@OneToMany(targetEntity = SrAtributoAcordo.class, mappedBy = "acordo", fetch = FetchType.LAZY)
	public Set<SrAtributoAcordo> atributoAcordoSet;

	public SrAcordo() {

	}

	public Long getId() {
		return this.idAcordo;
	}

	public void setId(Long id) {
		idAcordo = id;
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	public List<SrAcordo> getHistoricoAcordo() {
		if (acordoInicial != null)
			return acordoInicial.meuAcordoHistoricoSet;
		return null;
	}

	public SrAcordo getAcordoAtual() {
		if (getHisDtFim() == null)
			return this;
		List<SrAcordo> acordos = getHistoricoAcordo();
		if (acordos == null)
			return null;
		return acordos.get(0);
	}

	public SrAcordo selecionar(String sigla) {
		List<SrAcordo> acordos = buscar(sigla);
		return acordos.size() == 1 ? acordos.get(0) : null;
	}

	public List<SrAcordo> buscar(String sigla) {
		return SrAcordo.AR.find("byHisDtFimIsNullAndNomeAcordo", sigla).fetch();
	}

	@SuppressWarnings("unchecked")
	public static List<SrAcordo> listar(boolean mostrarDesativados) {
		if (!mostrarDesativados) {
			return SrAcordo.AR.find("byHisDtFimIsNull").fetch();
		} else {
			StringBuilder str = new StringBuilder();
			str.append("SELECT p FROM SrAcordo p where p.idAcordo IN (");
			str.append("SELECT MAX(idAcordo) FROM SrAcordo GROUP BY hisIdIni)");

			return AR.em().createQuery(str.toString()).getResultList();
		}
	}

	// Edson: Não consegui fazer com que esse cascade fosse automático.
	@Override
	public void salvar() throws Exception {
		super.salvar();
		if (atributoAcordoSet != null)
			for (SrAtributoAcordo atributoAcordo : atributoAcordoSet) {
				atributoAcordo.acordo = this;
				atributoAcordo.salvar();
			}
	}

	// Edson: Não consegui fazer com que esse cascade fosse automático.
	@Override
	public void finalizar() throws Exception {
		super.finalizar();
		if (atributoAcordoSet != null)
			for (SrAtributoAcordo atributoAcordo : atributoAcordoSet) {
				atributoAcordo.finalizar();
			}
	}

	public SrAtributoAcordo getAtributo(SrAtributo att) {
		for (SrAtributoAcordo pa : atributoAcordoSet)
			if (pa.atributo.equals(att))
				return pa;
		return null;
	}

	private SrAtributoAcordo getAtributo(String codigo) {
		if (atributoAcordoSet == null)
			return null;
		SrAtributo att = SrAtributo.get(codigo);
		if (att == null)
			return null;
		return getAtributo(att);
	}

	public Long getAtributoEmSegundos(String codigo) {
		SrAtributoAcordo pa = getAtributo(codigo);
		if (pa == null)
			return null;
		return pa.getValorEmSegundos();
	}

	@Override
	public String getSigla() {
		return idAcordo.toString();
	}

	@Override
	public void setSigla(String sigla) {
		
	}

	@Override
	public String getDescricao() {
		return nomeAcordo;
	}
	
	public SrAcordoVO toVO() {
		return SrAcordoVO.createFrom(this);
	}
	
	public String toJson() {
		return this.toVO().toJson();
	}

}