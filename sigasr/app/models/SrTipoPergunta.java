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
import play.db.jpa.Model;

@Entity
@Table(name = "SR_TIPO_PERGUNTA", schema = "SIGASR")
public class SrTipoPergunta extends GenericModel {

	final static public long TIPO_PERGUNTA_TEXTO_LIVRE = 1;

	final static public long TIPO_PERGUNTA_NOTA_1_A_5 = 2;
	
	@Id
	@Column(name = "ID_TIPO_PERGUNTA")
	public Long idTipoPergunta;

	@Column(name = "NOME_TIPO_PERGUNTA")
	public String nomeTipoPergunta;
	
	@Column(name = "DESCR_TIPO_PERGUNTA")
	public String descrTipoPergunta;

	public SrTipoPergunta() {

	}

	public Long getId() {
		return this.idTipoPergunta;
	}

	public void setId(Long id) {
		idTipoPergunta = id;
	}

}
