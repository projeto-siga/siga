package br.gov.jfrj.siga.wf.util;

import java.util.Objects;

import com.crivano.jflow.model.Responsible;

import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;

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
	
	public String getTooltip() {
		if (pessoa != null)
			return pessoa.getNomePessoa();
		if (lotacao != null)
			return lotacao.getNomeLotacao();
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
	
	@Override
	public int hashCode() {
		return Objects.hash(lotacao, pessoa);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WfResp other = (WfResp) obj;
		return Utils.equivale(lotacao, other.lotacao) && Utils.equivale(pessoa, other.pessoa);
	}

}
