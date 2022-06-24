package br.gov.jfrj.siga.wf.api.v1.apio;

import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.DefinicaoDeDesvio;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeDesvio;

public class DefinicaoDeDesvioAPIO extends DefinicaoDeDesvio {
	public static DefinicaoDeDesvioAPIO of(WfDefinicaoDeDesvio dd) {
		if (dd == null)
			return null;
		DefinicaoDeDesvioAPIO o = new DefinicaoDeDesvioAPIO();

		o.definicaoDeDesvioId = LongAPIO.of(dd.getId());
		o.definicaoDeDesvioOrdem = LongAPIO.of((long) dd.getOrdem());
		o.definicaoDeDesvioNome = dd.getNome();
		o.definicaoDeDesvioCondicao = dd.getCondicao();
		o.definicaoDeTarefaSeguinteId = dd.getSeguinteIde();
		o.definicaoDeTarefaUltimo = dd.isUltimo();
		return o;
	}
}
