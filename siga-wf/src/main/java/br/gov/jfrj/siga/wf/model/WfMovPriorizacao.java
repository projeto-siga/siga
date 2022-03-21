package br.gov.jfrj.siga.wf.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.enm.WfPrioridade;

@Entity
@DiscriminatorValue("P")
public class WfMovPriorizacao extends WfMov {
	@Enumerated(EnumType.STRING)
	@Column(name = "DEFT_TP_PRIORIDADE_DE")
	private WfPrioridade prioridadeDe;

	@Enumerated(EnumType.STRING)
	@Column(name = "DEFT_TP_PRIORIDADE_PARA")
	private WfPrioridade prioridadePara;

	public WfMovPriorizacao() {
		super();
	}

	public WfMovPriorizacao(WfProcedimento pi, Date dtIni, DpPessoa titular, DpLotacao lotaTitular,
			CpIdentidade identidade, WfPrioridade de, WfPrioridade para) {
		super(pi, null, dtIni, titular, lotaTitular, identidade);
		if (de != null)
			prioridadeDe = de;
		if (para != null)
			prioridadePara = para;
	}

	@Override
	public String getEvento() {
		return "Priorização";
	}

	@Override
	public String getDescricaoEvento() {
		return (prioridadeDe == null ? "nenhuma" : prioridadeDe.getDescr()) + " -> "
				+ (prioridadePara == null ? "nenhuma" : prioridadePara.getDescr());
	}

	public WfPrioridade getPrioridadeDe() {
		return prioridadeDe;
	}

	public WfPrioridade getPrioridadePara() {
		return prioridadePara;
	}
}
