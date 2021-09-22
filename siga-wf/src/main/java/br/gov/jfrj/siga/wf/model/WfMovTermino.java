package br.gov.jfrj.siga.wf.model;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

@Entity
@DiscriminatorValue("F")
public class WfMovTermino extends WfMovTransicao {

	public WfMovTermino() {
		super();
	}

	public WfMovTermino(WfProcedimento pi, Date dtIni, DpPessoa titular, DpLotacao lotaTitular, CpIdentidade identidade,
			Integer de) {
		super(pi, dtIni, titular, lotaTitular, identidade, de, null);
	}

	@Override
	public String getEvento() {
		return "Término";
	}

}
