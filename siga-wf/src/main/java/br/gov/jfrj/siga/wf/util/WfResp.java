package br.gov.jfrj.siga.wf.util;

import com.crivano.jflow.model.Responsible;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class WfResp implements Responsible {

	private DpPessoa pessoa;

	private DpLotacao lotacao;

	public WfResp(DpPessoa pessoa, DpLotacao lotacao) {
		super();
		this.pessoa = pessoa;
		this.lotacao = lotacao;
	}

	@Override
	public String getInitials() {
		if (pessoa != null)
			return pessoa.getSobrenomeEIniciais();
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

	public DpPessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(DpPessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DpLotacao getLotacao() {
		return lotacao;
	}

	public void setLotacao(DpLotacao lotacao) {
		this.lotacao = lotacao;
	}
}
