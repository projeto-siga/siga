package br.gov.jfrj.siga.wf.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.logic.PodeSim;
import br.gov.jfrj.siga.wf.util.WfAcaoHelper;
import br.gov.jfrj.siga.wf.util.WfAcaoVO;

@Entity
@DiscriminatorValue("A")
public class WfMovAnotacao extends WfMov {

	public WfMovAnotacao() {
		super();
	}

	public WfMovAnotacao(WfProcedimento procedimento, String descr, Date dtIni, DpPessoa titular, DpLotacao lotaTitular,
			CpIdentidade identidade) {
		super(procedimento, descr, dtIni, titular, lotaTitular, identidade);
	}

	@Override
	public String getEvento() {
		return "Anotação";
	}

	@Override
	public String getDescricaoEvento() {
		return getDescr();
	}

	@Override
	public List<AcaoVO> getAcoes() {
		List<AcaoVO> set = new ArrayList<>();

		set.add(WfAcaoVO.builder().nome("Excluir")
				.acao("/app/procedimento/" + getProcedimento().getId() + "/anotacao/" + getId() + "/excluir")
				.exp(new PodeSim()).post(true).build());

		return set;
	}
}
