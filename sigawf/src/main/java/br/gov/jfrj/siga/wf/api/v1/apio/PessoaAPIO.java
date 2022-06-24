package br.gov.jfrj.siga.wf.api.v1.apio;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.Pessoa;

public class PessoaAPIO extends Pessoa {
	public static PessoaAPIO of(DpPessoa p) {
		if (p == null)
			return null;
		PessoaAPIO o = new PessoaAPIO();
		o.id = LongAPIO.of(p.getId());
		o.sigla = p.getSigla();
		o.descr = p.getDescricao();
		return o;
	}
}
