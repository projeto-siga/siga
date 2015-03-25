package br.gov.jfrj.siga.sr.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "SR_PERGUNTA", schema = "SIGASR")
public class SrPergunta extends HistoricoSuporte {

	public static ActiveRecord<SrPergunta> AR = new ActiveRecord<>(SrPergunta.class);
	
	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_PERGUNTA_SEQ", name = "srPerguntaSeq")
	@GeneratedValue(generator = "srPerguntaSeq")
	@Column(name = "ID_PERGUNTA")
	private Long idPergunta;

	@Column(name = "DESCR_PERGUNTA")
	private String descrPergunta;

	@ManyToOne()
	@JoinColumn(name = "ID_PESQUISA")
	private SrPesquisa pesquisa;

	@ManyToOne()
	@JoinColumn(name = "ID_TIPO_PERGUNTA")
	private SrTipoPergunta tipoPergunta;

	@Column(name = "ORDEM_PERGUNTA")
	private Long ordemPergunta;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	private SrPergunta perguntaInicial;

	@OneToMany(targetEntity = SrPergunta.class, mappedBy = "perguntaInicial")
	@OrderBy("hisDtIni desc")
	private List<SrPergunta> meuPerguntaHistoricoSet;

	/**
	 * @return the idPergunta
	 */
	public Long getIdPergunta() {
		return idPergunta;
	}

	/**
	 * @param idPergunta the idPergunta to set
	 */
	public void setIdPergunta(Long idPergunta) {
		this.idPergunta = idPergunta;
	}

	/**
	 * @return the descrPergunta
	 */
	public String getDescrPergunta() {
		return descrPergunta;
	}

	/**
	 * @param descrPergunta the descrPergunta to set
	 */
	public void setDescrPergunta(String descrPergunta) {
		this.descrPergunta = descrPergunta;
	}

	/**
	 * @return the pesquisa
	 */
	public SrPesquisa getPesquisa() {
		return pesquisa;
	}

	/**
	 * @param pesquisa the pesquisa to set
	 */
	public void setPesquisa(SrPesquisa pesquisa) {
		this.pesquisa = pesquisa;
	}

	/**
	 * @return the tipoPergunta
	 */
	public SrTipoPergunta getTipoPergunta() {
		return tipoPergunta;
	}

	/**
	 * @param tipoPergunta the tipoPergunta to set
	 */
	public void setTipoPergunta(SrTipoPergunta tipoPergunta) {
		this.tipoPergunta = tipoPergunta;
	}

	/**
	 * @return the ordemPergunta
	 */
	public Long getOrdemPergunta() {
		return ordemPergunta;
	}

	/**
	 * @param ordemPergunta the ordemPergunta to set
	 */
	public void setOrdemPergunta(Long ordemPergunta) {
		this.ordemPergunta = ordemPergunta;
	}

	/**
	 * @return the perguntaInicial
	 */
	public SrPergunta getPerguntaInicial() {
		return perguntaInicial;
	}

	/**
	 * @param perguntaInicial the perguntaInicial to set
	 */
	public void setPerguntaInicial(SrPergunta perguntaInicial) {
		this.perguntaInicial = perguntaInicial;
	}

	/**
	 * @return the meuPerguntaHistoricoSet
	 */
	public List<SrPergunta> getMeuPerguntaHistoricoSet() {
		return meuPerguntaHistoricoSet;
	}

	/**
	 * @param meuPerguntaHistoricoSet the meuPerguntaHistoricoSet to set
	 */
	public void setMeuPerguntaHistoricoSet(List<SrPergunta> meuPerguntaHistoricoSet) {
		this.meuPerguntaHistoricoSet = meuPerguntaHistoricoSet;
	}

	public SrPergunta() {

	}

	public Long getId() {
		return this.idPergunta;
	}

	public void setId(Long id) {
		idPergunta = id;
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	public List<SrPergunta> getHistoricoPergunta() {
		if (perguntaInicial != null)
			return perguntaInicial.meuPerguntaHistoricoSet;
		return null;
	}

	public SrPergunta getPerguntaAtual() {
		List<SrPergunta> perguntas = getHistoricoPergunta();
		if (perguntas == null)
			return null;
		return perguntas.get(0);
	}
	
}
