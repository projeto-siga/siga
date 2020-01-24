package br.gov.jfrj.siga.wf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.crivano.jflow.model.Responsible;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

@Entity
@BatchSize(size = 500)
@Table(name = "WF_RESPONSAVEL", catalog = "WF")
public class WfResponsavel implements Responsible {

	@Id
	@Column(name = "RESP_ID", unique = true, nullable = false)
	private java.lang.Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFR_ID")
	private WfDefinicaoDeResponsavel definicaoDeResponsavel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PESS_ID")
	private DpPessoa pessoa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOTA_ID")
	private DpLotacao lotacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORGU_ID")
	private CpOrgaoUsuario orgaoUsuario;

	public WfResponsavel(DpPessoa pessoa, DpLotacao lotacao) {
		super();
		this.pessoa = pessoa;
		this.lotacao = lotacao;
	}

	@Override
	public String getInitials() {
		if (pessoa != null)
			return pessoa.getSigla();
		if (lotacao != null)
			return lotacao.getSiglaCompleta();
		return null;
	}

	public String getCodigo() {
		if (pessoa != null)
			return pessoa.getSiglaCompleta();
		if (lotacao != null)
			return "@" + lotacao.getSiglaCompleta();
		return null;
	}

	public CpOrgaoUsuario getOrgaoUsuario() {
		return orgaoUsuario;
	}

	public void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
		this.orgaoUsuario = orgaoUsuario;
	}

}
