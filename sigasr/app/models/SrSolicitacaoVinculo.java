package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "SR_SOLICITACAO_VINCULO", schema = "SIGASR")
public class SrSolicitacaoVinculo extends Objeto {

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_SOLICITACAO_VINCULO_SEQ", name = "srSolicitacaoVinculoSeq")
	@GeneratedValue(generator = "srSolicitacaoVinculoSeq")
	@Column(name = "ID_SOLICITACAO_VINCULO")
	public Long idSolicitacaoVinculo;
	
	@ManyToOne
	@JoinColumn(name = "ID_SOLICITACAO_A")
	public SrSolicitacao solicitacaoA;
	
	@ManyToOne
	@JoinColumn(name = "ID_SOLICITACAO_B")
	public SrSolicitacao solicitacaoB;
	
	public SrSolicitacaoVinculo() {
	}
	
	public SrSolicitacaoVinculo(SrSolicitacao solicitacaoA, SrSolicitacao solicitacaoB) {
		this.solicitacaoA = solicitacaoA;
		this.solicitacaoB = solicitacaoB;
	}
}
