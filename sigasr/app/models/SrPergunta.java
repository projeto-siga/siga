package models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Historico;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.JPABase;
import play.db.jpa.Model;

@Entity
@Table(name = "SR_PERGUNTA", schema = "SIGASR")
public class SrPergunta extends HistoricoSuporte {

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_PERGUNTA_SEQ", name = "srPerguntaSeq")
	@GeneratedValue(generator = "srPerguntaSeq")
	@Column(name = "ID_PERGUNTA")
	public Long idPergunta;

	@Column(name = "DESCR_PERGUNTA")
	public String descrPergunta;

	@ManyToOne()
	@JoinColumn(name = "ID_PESQUISA")
	public SrPesquisa pesquisa;

	@ManyToOne()
	@JoinColumn(name = "ID_TIPO_PERGUNTA")
	public SrTipoPergunta tipoPergunta;

	@Column(name = "ORDEM_PERGUNTA")
	public Long ordemPergunta;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrPergunta perguntaInicial;

	@OneToMany(targetEntity = SrPergunta.class, mappedBy = "perguntaInicial", cascade = CascadeType.PERSIST)
	@OrderBy("hisDtIni desc")
	public List<SrPergunta> meuPerguntaHistoricoSet;

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
