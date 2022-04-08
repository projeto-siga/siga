
package br.gov.jfrj.siga.wf.api.v1;

import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.IDefinicoesDeProcedimentosDefinicaoDeProcedimentoSiglaDesativarPost;

public class DefinicoesDeProcedimentosDefinicaoDeProcedimentoSiglaDesativarPost
		implements IDefinicoesDeProcedimentosDefinicaoDeProcedimentoSiglaDesativarPost {

	@Override
	public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception {
	}

	@Override
	public String getContext() {
		return "desativar definição de procedimento";
	}

}
