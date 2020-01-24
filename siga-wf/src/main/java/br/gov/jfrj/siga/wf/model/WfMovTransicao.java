package br.gov.jfrj.siga.wf.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("T")
public class WfMovTransicao extends WfMov {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFT_ID_DE")
	private WfDefinicaoDeTarefa definicaoDeTarefaDe;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFT_ID_PARA")
	private WfDefinicaoDeTarefa definicaoDeTarefaPara;

}
