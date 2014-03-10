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
import javax.persistence.Enumerated;
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
@Table(name = "SR_RESPOSTA", schema = "SIGASR")
public class SrResposta extends GenericModel {

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_RESPOSTA_SEQ", name = "srRespostaSeq")
	@GeneratedValue(generator = "srRespostaSeq")
	@Column(name = "ID_RESPOSTA")
	public Long idResposta;

	@Column(name = "DESCR_RESPOSTA")
	public String descrResposta;

	@Enumerated()
	@Column(name = "VALOR_RESPOSTA")
	public SrFormaAcompanhamento valorResposta;

	@ManyToOne()
	@JoinColumn(name = "ID_PERGUNTA")
	public SrPergunta pergunta;
	
	@ManyToOne()
	@JoinColumn(name = "ID_MOVIMENTACAO")
	public SrMovimentacao movimentacao;

	@ManyToOne()
	@JoinColumn(name = "ID_AVALIACAO")
	public SrMovimentacao avaliacao;

	public SrResposta() {

	}

	public Long getId() {
		return this.idResposta;
	}

	public void setId(Long id) {
		idResposta = id;
	}

}
