package br.gov.jfrj.siga.sr.model;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

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
import br.gov.jfrj.siga.sr.model.vo.SrAcordoVO;
import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.Collections;

@Entity
@Table(name = "sr_acordo", schema = "sigasr")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class SrAcordo extends HistoricoSuporte implements Selecionavel {
	// public class SrAcordo extends HistoricoSuporte implements
	// ConvertableEntity {

	private static final long serialVersionUID = 1L;

	public static final ActiveRecord<SrAcordo> AR = new ActiveRecord<>(
			SrAcordo.class);

	@Id
	@SequenceGenerator(sequenceName = "SIGASR" + ".SR_ACORDO_SEQ", name = "srAcordoSeq")
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

	public List<SrAcordo> getMeuAcordoHistoricoSet() {
		return meuAcordoHistoricoSet;
	}

	public void setMeuAcordoHistoricoSet(List<SrAcordo> meuAcordoHistoricoSet) {
		this.meuAcordoHistoricoSet = meuAcordoHistoricoSet;
	}

	@OneToMany(targetEntity = SrParametroAcordo.class, mappedBy = "acordo", fetch = FetchType.LAZY)
	private List<SrParametroAcordo> parametroAcordoSet;

	public SrAcordo() {
		this.parametroAcordoSet = new ArrayList<>();
		this.meuAcordoHistoricoSet = new ArrayList<>();
	}

	@Override
	public Long getId() {
		return getIdAcordo();
	}

	@Override
	public void setId(Long idAcordo) {
		this.setIdAcordo(idAcordo);
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<SrAcordo> getHistoricoAcordo() {
		if (getAcordoInicial() != null)
			return getAcordoInicial().meuAcordoHistoricoSet;
		return Collections.emptyList();
	}

	public SrAcordo getAcordoAtual() {
		if (getHisDtFim() == null)
			return this;
		List<SrAcordo> acordos = getHistoricoAcordo();
		if (acordos == null || acordos.size() < 1)
			return null;
		return acordos.get(0);
	}
	
	public boolean contemParametro(SrParametro... param){
		List<SrParametro> paramList = Arrays.asList(param);
		for (SrParametroAcordo paramAcordo : getParametroAcordoSet())
			if (paramList.contains(paramAcordo.getParametro()))
				return true;
		return false;
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

	// Edson: NÃ£o consegui fazer com que esse cascade fosse automÃ¡tico.
	@Override
	public void salvarComHistorico() throws Exception {
		super.salvarComHistorico();
		if (getParametroAcordoSet() != null)
			for (SrParametroAcordo atributoAcordo : getParametroAcordoSet()) {
				atributoAcordo.setAcordo(this);
				atributoAcordo.salvarComHistorico();
			}
	}

	// Edson: NÃ£o consegui fazer com que esse cascade fosse automÃ¡tico.
	@Override
	public void finalizar() throws Exception {
		super.finalizar();
		if (getParametroAcordoSet() != null)
			for (SrParametroAcordo atributoAcordo : getParametroAcordoSet()) {
				atributoAcordo.finalizar();
			}
	}

	public SrParametroAcordo getAtributo(SrAtributo att) {
		for (SrParametroAcordo pa : getParametroAcordoSet())
			if (pa.getParametro().equals(att))
				return pa;
		return null;
	}

	public String getSigla() {
		if (getIdAcordo() != null)
			return getIdAcordo().toString();
		else
			return "";
	}

	public String getDescricao() {
		return getNomeAcordo();
	}

	public SrAcordoVO toVO() throws Exception {
		return SrAcordoVO.createFrom(this);
	}

	public String toJson() throws Exception {
		return this.toVO().toJson();
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

	@Override
	public void setSigla(String sigla) {

	}

	public List<SrParametroAcordo> getParametroAcordoSet() {
		return parametroAcordoSet;
	}

	public void setParametroAcordoSet(List<SrParametroAcordo> parametroAcordoSet) {
		this.parametroAcordoSet = parametroAcordoSet;
	}

}