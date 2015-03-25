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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Selecionavel;

@Entity
@Table(name = "SR_ACORDO", schema = "SIGASR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SrAcordo extends HistoricoSuporte implements Selecionavel {

	public static ActiveRecord<SrAcordo> AR = new ActiveRecord<>(SrAcordo.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_ACORDO_SEQ", name = "srAcordoSeq")
	@GeneratedValue(generator = "srAcordoSeq")
	@Column(name = "ID_ACORDO")
	private Long idAcordo;

	@Column(name = "NOME_ACORDO")
	private String nomeAcordo;

	@Column(name = "DESCR_ACORDO")
	private String descrAcordo;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	private SrAcordo acordoInicial;

	@OneToMany(targetEntity = SrAcordo.class, mappedBy = "acordoInicial", fetch = FetchType.LAZY)
	@OrderBy("hisDtIni desc")
	private List<SrAcordo> meuAcordoHistoricoSet;

	@OneToMany(targetEntity = SrAtributoAcordo.class, mappedBy = "acordo", fetch = FetchType.LAZY)
	private Set<SrAtributoAcordo> atributoAcordoSet;

	public SrAcordo() {

	}

	public Long getId() {
		return this.idAcordo;
	}

	public void setId(Long id) {
		idAcordo = id;
	}

	public Long getIdAcordo() {
		return idAcordo;
	}

	public void setIdAcordo(Long idAcordo) {
		this.idAcordo = idAcordo;
	}

	public String getNomeAcordo() {
		return nomeAcordo;
	}

	public void setNomeAcordo(String nomeAcordo) {
		this.nomeAcordo = nomeAcordo;
	}

	public String getDescrAcordo() {
		return descrAcordo;
	}

	public void setDescrAcordo(String descrAcordo) {
		this.descrAcordo = descrAcordo;
	}

	public SrAcordo getAcordoInicial() {
		return acordoInicial;
	}

	public void setAcordoInicial(SrAcordo acordoInicial) {
		this.acordoInicial = acordoInicial;
	}

	public List<SrAcordo> getMeuAcordoHistoricoSet() {
		return meuAcordoHistoricoSet;
	}

	public void setMeuAcordoHistoricoSet(List<SrAcordo> meuAcordoHistoricoSet) {
		this.meuAcordoHistoricoSet = meuAcordoHistoricoSet;
	}

	public Set<SrAtributoAcordo> getAtributoAcordoSet() {
		return atributoAcordoSet;
	}

	public void setAtributoAcordoSet(Set<SrAtributoAcordo> atributoAcordoSet) {
		this.atributoAcordoSet = atributoAcordoSet;
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

	public static List<SrAcordo> listar(boolean mostrarDesativados) {
		if (!mostrarDesativados) {
			return SrAcordo.AR.find("byHisDtFimIsNull").fetch();
		} else {
			StringBuilder str = new StringBuilder();
			str.append("SELECT p FROM SrAcordo p where p.idAcordo IN (");
			str.append("SELECT MAX(idAcordo) FROM SrAcordo GROUP BY hisIdIni)");

			return em().createQuery(str.toString()).getResultList();
		}
	}

	// Edson: Não consegui fazer com que esse cascade fosse automático.
	public void salvar() throws Exception {
		super.salvarComHistorico();
		if (atributoAcordoSet != null)
			for (SrAtributoAcordo atributoAcordo : atributoAcordoSet) {
				atributoAcordo.setAcordo(this);
				atributoAcordo.save();
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
			if (pa.getAtributo().equals(att))
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

	public Integer getAtributoEmSegundos(String codigo) {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDescricao() {
		return nomeAcordo;
	}

}