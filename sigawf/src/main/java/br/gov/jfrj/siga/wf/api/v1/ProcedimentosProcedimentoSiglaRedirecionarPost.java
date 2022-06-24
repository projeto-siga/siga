
package br.gov.jfrj.siga.wf.api.v1;

import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.IProcedimentosProcedimentoSiglaRedirecionarPost;

public class ProcedimentosProcedimentoSiglaRedirecionarPost implements IProcedimentosProcedimentoSiglaRedirecionarPost {

	@Override
	public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception {
	}

	@Override
	public String getContext() {
		return "redirecionar procedimento";
	}

}
