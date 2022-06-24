package br.gov.jfrj.siga.wf.api.v1.apio;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.Lotacao;

public class LotacaoAPIO extends Lotacao {
	public static LotacaoAPIO of(DpLotacao p) {
		if (p == null)
			return null;
		LotacaoAPIO o = new LotacaoAPIO();
		o.id = LongAPIO.of(p.getId());
		o.sigla = p.getSigla();
		o.descr = p.getDescricao();
		return o;
	}
}
