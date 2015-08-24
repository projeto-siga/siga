package br.gov.jfrj.siga.sr.model;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public abstract class SrEtapa extends AbstractSrEtapa {

	public SrEtapa() {
	}

	private DpLotacao lotaResponsavel;
	private DpPessoa responsavel;

	public DpLotacao getLotaResponsavel() {
		return lotaResponsavel;
	}

	public void setLotaResponsavel(DpLotacao lotaResponsavel) {
		this.lotaResponsavel = lotaResponsavel;
	}

	public DpPessoa getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(DpPessoa responsavel) {
		this.responsavel = responsavel;
	}

}
