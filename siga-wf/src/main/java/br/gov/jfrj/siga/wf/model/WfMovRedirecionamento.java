package br.gov.jfrj.siga.wf.model;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

@Entity
@DiscriminatorValue("R")
public class WfMovRedirecionamento extends WfMovTransicao {

	public WfMovRedirecionamento() {
		super();
	}

	public WfMovRedirecionamento(WfProcedimento pi, Date dtIni, DpPessoa titular, DpLotacao lotaTitular,
			CpIdentidade identidade, Integer de, Integer para) {
		super(pi, dtIni, titular, lotaTitular, identidade, de, para);
	}

	@Override
	public String getEvento() {
		return "Redirecionamento";
	}

}
