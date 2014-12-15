package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import models.vo.SrGestorItemVO;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.sinc.lib.NaoRecursivo;

@Entity
@Table(name = "SR_GESTOR_ITEM", schema = "SIGASR")
public class SrGestorItem extends Objeto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_GESTOR_ITEM_SEQ", name = "srGestorItemSeq")
	@GeneratedValue(generator = "srGestorItemSeq")
	@Column(name = "ID_GESTOR_ITEM")
	public Long idGestorItem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PESSOA")
	@NaoRecursivo
	private DpPessoa dpPessoa;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOTACAO")
	@NaoRecursivo
	private DpLotacao dpLotacao;

	@ManyToOne()
	@JoinColumn(name = "ID_ITEM_CONFIGURACAO")
	public SrItemConfiguracao itemConfiguracao;
	
	public DpPessoa getDpPessoa() {
		return dpPessoa;
	}
	
	public void setDpPessoa(DpPessoa dpPessoa) {
		this.dpPessoa = dpPessoa;
	}
	
	public DpLotacao getDpLotacao() {
		return dpLotacao;
	}
	
	public void setDpLotacao(DpLotacao dpLotacao) {
		this.dpLotacao = dpLotacao;
	}
	
	public SrGestorItemVO toVO() {
		if (this.dpPessoa != null && this.dpPessoa.getId() != null && (this.dpPessoa.getSigla() == null || this.dpPessoa.getDescricao() == null))
			this.dpPessoa = DpPessoa.findById(this.dpPessoa.getId());
		
		if (this.dpLotacao != null && this.dpLotacao.getId() != null && (this.dpLotacao.getSigla() == null || this.dpLotacao.getDescricao() == null))
			this.dpLotacao = DpLotacao.findById(this.dpLotacao.getId());
		
		return new SrGestorItemVO(this.idGestorItem, this.dpPessoa, this.dpLotacao);
	}
	
}
