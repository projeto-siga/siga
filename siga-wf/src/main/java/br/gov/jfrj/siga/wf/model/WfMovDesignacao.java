package br.gov.jfrj.siga.wf.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeDesignacao;

@Entity
@DiscriminatorValue("D")
public class WfMovDesignacao extends WfMov {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PESS_ID_DE")
	private DpPessoa pessoaDe;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PESS_ID_PARA")
	private DpPessoa pessoaPara;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOTA_ID_DE")
	private DpLotacao lotaDe;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOTA_ID_PARA")
	private DpLotacao lotaPara;

	@Column(name = "MOVI_TP_DESIGNACAO")
	private WfTipoDeDesignacao tipo;
}
