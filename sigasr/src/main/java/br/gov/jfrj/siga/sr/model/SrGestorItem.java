package br.gov.jfrj.siga.sr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.base.util.Catalogs;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sinc.lib.NaoRecursivo;
import br.gov.jfrj.siga.sr.model.vo.SrGestorItemVO;
import br.gov.jfrj.siga.vraptor.entity.ObjetoVraptor;

@Entity
@Table(name = "SR_GESTOR_ITEM", schema = Catalogs.SIGASR)
public class SrGestorItem extends ObjetoVraptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = Catalogs.SIGASR +".SR_GESTOR_ITEM_SEQ",name = "srGestorItemSeq")
	@GeneratedValue(generator = "srGestorItemSeq")
	@Column(name = "ID_GESTOR_ITEM")
	private Long idGestorItem;
	
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
	private SrItemConfiguracao itemConfiguracao;
	
	
	public SrGestorItem() {
		super();
	}

	public SrGestorItemVO toVO() {
		if (this.dpPessoa != null && this.dpPessoa.getId() != null && (this.dpPessoa.getSigla() == null || this.dpPessoa.getDescricao() == null))
			this.dpPessoa = DpPessoa.findById(this.dpPessoa.getId());
		
		if (this.dpLotacao != null && this.dpLotacao.getId() != null && (this.dpLotacao.getSigla() == null || this.dpLotacao.getDescricao() == null))
			this.dpLotacao = DpLotacao.findById(this.dpLotacao.getId());
		
		return new SrGestorItemVO(this.idGestorItem, this.dpPessoa, this.dpLotacao);
	}

	public Long getIdGestorItem() {
		return idGestorItem;
	}

	public void setIdGestorItem(Long idGestorItem) {
		this.idGestorItem = idGestorItem;
	}

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

	public SrItemConfiguracao getItemConfiguracao() {
		return itemConfiguracao;
	}

	public void setItemConfiguracao(SrItemConfiguracao itemConfiguracao) {
		this.itemConfiguracao = itemConfiguracao;
	}

	@Override
	protected Long getId() {
		return this.idGestorItem;
	}
	
}
