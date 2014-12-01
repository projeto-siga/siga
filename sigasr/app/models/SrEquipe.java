package models;

import java.util.List;

import javax.persistence.CascadeType;
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

import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "SR_EQUIPE", schema = "SIGASR")
public class SrEquipe extends HistoricoSuporte {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_EQUIPE_SEQ", name = "srEquipeSeq")
	@GeneratedValue(generator = "srEquipeSeq")
	@Column(name = "ID_EQUIPE")
	public Long idEquipe;
	
	@ManyToOne
	@JoinColumn(name = "ID_LOTA_EQUIPE")
	public DpLotacao lotacao;
	
	@OneToMany(targetEntity = SrExcecaoHorario.class, mappedBy = "equipe", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	public List<SrExcecaoHorario> excecaoHorarioSet;

	@Override
	public Long getId() {
		return this.idEquipe;
	}

	@Override
	public void setId(Long id) {
		this.idEquipe = id;
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

}
