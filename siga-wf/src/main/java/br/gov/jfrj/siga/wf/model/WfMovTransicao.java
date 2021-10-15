package br.gov.jfrj.siga.wf.model;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

@Entity
@DiscriminatorValue("T")
public class WfMovTransicao extends WfMov {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFT_ID_DE")
	private WfDefinicaoDeTarefa definicaoDeTarefaDe;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFT_ID_PARA")
	private WfDefinicaoDeTarefa definicaoDeTarefaPara;

	public WfMovTransicao() {
		super();
	}

	public WfMovTransicao(WfProcedimento pi, Date dtIni, DpPessoa titular, DpLotacao lotaTitular,
			CpIdentidade identidade, Integer de, Integer para) {
		super(pi, null, dtIni, titular, lotaTitular, identidade);
		if (de != null)
			definicaoDeTarefaDe = pi.getTaskDefinitionByIndex(de);
		if (para != null)
			definicaoDeTarefaPara = pi.getTaskDefinitionByIndex(para);
	}

	@Override
	public String getEvento() {
		return "Transição";
	}

	@Override
	public String getDescricaoEvento() {
		return (definicaoDeTarefaDe == null ? "início" : definicaoDeTarefaDe.getNome()) + " -> "
				+ (definicaoDeTarefaPara == null ? "fim" : definicaoDeTarefaPara.getNome());
	}

	public WfDefinicaoDeTarefa getDefinicaoDeTarefaDe() {
		return definicaoDeTarefaDe;
	}

	public WfDefinicaoDeTarefa getDefinicaoDeTarefaPara() {
		return definicaoDeTarefaPara;
	}

}
