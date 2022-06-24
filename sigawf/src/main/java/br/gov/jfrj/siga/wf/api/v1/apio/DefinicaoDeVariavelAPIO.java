package br.gov.jfrj.siga.wf.api.v1.apio;

import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.DefinicaoDeVariavel;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeVariavel;

public class DefinicaoDeVariavelAPIO extends DefinicaoDeVariavel {
	public static DefinicaoDeVariavelAPIO of(WfDefinicaoDeVariavel dv) {
		if (dv == null)
			return null;
		DefinicaoDeVariavelAPIO o = new DefinicaoDeVariavelAPIO();
		o.definicaoDeVariavelId = LongAPIO.of(dv.getId());
		o.definicaoDeVariavelOrdem = LongAPIO.of((long) dv.getOrdem());
		o.definicaoDeVariavelIdentificador = dv.getIdentificador();
		o.definicaoDeVariavelNome = dv.getNome();
		o.definicaoDeVariavelTipo = EnumAPIO.of(dv.getTipo());
		o.definicaoDeVariavelAcesso = EnumAPIO.of(dv.getAcesso());
		return o;
	}
}
