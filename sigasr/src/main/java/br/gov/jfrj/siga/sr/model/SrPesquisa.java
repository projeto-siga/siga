package br.gov.jfrj.siga.sr.model;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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

import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "SR_PESQUISA", schema = "SIGASR")
public class SrPesquisa extends HistoricoSuporte {

	public static ActiveRecord<SrPesquisa> AR = new ActiveRecord<>(SrPesquisa.class);
	
	/**
	 * 
	 */	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_PESQUISA_SEQ", name = "srPesquisaSeq")
	@GeneratedValue(generator = "srPesquisaSeq")
	@Column(name = "ID_PESQUISA")
	private Long idPesquisa;

	@Column(name = "NOME_PESQUISA")
	private String nomePesquisa;

	@Column(name = "DESCR_PESQUISA")
	private String descrPesquisa;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	private SrPesquisa pesquisaInicial;

	@OneToMany(targetEntity = SrPesquisa.class, mappedBy = "pesquisaInicial", fetch=FetchType.LAZY)
	@OrderBy("hisDtIni desc")
	private List<SrPesquisa> meuPesquisaHistoricoSet;

	@OneToMany(targetEntity = SrPergunta.class, mappedBy = "pesquisa", fetch=FetchType.LAZY)
	@OrderBy("ordemPergunta")
	private Set<SrPergunta> perguntaSet;

	/**
	 * @return the idPesquisa
	 */
	public Long getIdPesquisa() {
		return idPesquisa;
	}

	/**
	 * @param idPesquisa the idPesquisa to set
	 */
	public void setIdPesquisa(Long idPesquisa) {
		this.idPesquisa = idPesquisa;
	}

	/**
	 * @return the nomePesquisa
	 */
	public String getNomePesquisa() {
		return nomePesquisa;
	}

	/**
	 * @param nomePesquisa the nomePesquisa to set
	 */
	public void setNomePesquisa(String nomePesquisa) {
		this.nomePesquisa = nomePesquisa;
	}

	/**
	 * @return the descrPesquisa
	 */
	public String getDescrPesquisa() {
		return descrPesquisa;
	}

	/**
	 * @param descrPesquisa the descrPesquisa to set
	 */
	public void setDescrPesquisa(String descrPesquisa) {
		this.descrPesquisa = descrPesquisa;
	}

	/**
	 * @return the pesquisaInicial
	 */
	public SrPesquisa getPesquisaInicial() {
		return pesquisaInicial;
	}

	/**
	 * @param pesquisaInicial the pesquisaInicial to set
	 */
	public void setPesquisaInicial(SrPesquisa pesquisaInicial) {
		this.pesquisaInicial = pesquisaInicial;
	}

	/**
	 * @return the meuPesquisaHistoricoSet
	 */
	public List<SrPesquisa> getMeuPesquisaHistoricoSet() {
		return meuPesquisaHistoricoSet;
	}

	/**
	 * @param meuPesquisaHistoricoSet the meuPesquisaHistoricoSet to set
	 */
	public void setMeuPesquisaHistoricoSet(List<SrPesquisa> meuPesquisaHistoricoSet) {
		this.meuPesquisaHistoricoSet = meuPesquisaHistoricoSet;
	}

	/**
	 * @return the perguntaSet
	 */
	public Set<SrPergunta> getPerguntaSet() {
		return perguntaSet;
	}

	/**
	 * @param perguntaSet the perguntaSet to set
	 */
	public void setPerguntaSet(Set<SrPergunta> perguntaSet) {
		this.perguntaSet = perguntaSet;
	}

	public SrPesquisa() {

	}

	public Long getId() {
		return this.idPesquisa;
	}

	public void setId(Long id) {
		idPesquisa = id;
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	public List<SrPesquisa> getHistoricoPesquisa() {
		if (pesquisaInicial != null)
			return pesquisaInicial.meuPesquisaHistoricoSet;
		return null;
	}

	public SrPesquisa getPesquisaAtual() {
		if (getHisDtFim() == null)
			return this;
		List<SrPesquisa> pesquisas = getHistoricoPesquisa();
		if (pesquisas == null)
			return null;
		return pesquisas.get(0);
	}
	
	public static List<SrPesquisa> listar(boolean mostrarDesativados) {
		if (!mostrarDesativados) {
			return SrPesquisa.AR.find("byHisDtFimIsNull").fetch();
		} else {
			StringBuilder str = new StringBuilder();
			str.append("SELECT p FROM SrPesquisa p where p.idPesquisa IN (");
			str.append("SELECT MAX(idPesquisa) FROM SrPesquisa GROUP BY hisIdIni)");
			
			return em()
					.createQuery(str.toString())
					.getResultList();
		}
	}

	// Edson: Não consegui fazer com que esse cascade fosse automático.
	@Override
	public void salvarComHistorico() throws Exception {
		super.salvarComHistorico();
		if (perguntaSet != null)
			for (SrPergunta pergunta : perguntaSet) {
				pergunta.setPesquisa(this);
				pergunta.save();
			}
	}

	public Set<SrPergunta> getPerguntaSetAtivas() {
		if (pesquisaInicial == null)
			return null;
		TreeSet<SrPergunta> listaCompleta = new TreeSet<SrPergunta>(
				new Comparator<SrPergunta>() {
					@Override
					public int compare(SrPergunta a1, SrPergunta a2) {
						return a1.getOrdemPergunta().compareTo(a2.getOrdemPergunta());
					}
				});
		for (SrPesquisa pesquisa : getHistoricoPesquisa())
			if (pesquisa.meuPesquisaHistoricoSet != null)
				if (pesquisa.perguntaSet != null)
					for (SrPergunta perg : pesquisa.perguntaSet)
						if (perg.getHisDtFim() == null)
							listaCompleta.add(perg);
		return listaCompleta;
	}

	public String getPergunta(Long idPergunta) throws Exception {
		SrPergunta pergunta = SrPergunta.AR.findById(idPergunta);
		return pergunta.getDescrPergunta();
	}

	// Edson: Não consegui fazer com que esse cascade fosse automático.
	@Override
	public void finalizar() throws Exception {
		super.finalizar();
		if (perguntaSet != null)
			for (SrPergunta pergunta : perguntaSet) {
				pergunta.finalizar();
			}
	}
}
