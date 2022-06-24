
package br.gov.jfrj.siga.wf.api.v1;

import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.IProcedimentosProcedimentoSiglaGet;
import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.Procedimento;
import br.gov.jfrj.siga.wf.api.v1.apio.ProcedimentoAPIO;
import br.gov.jfrj.siga.wf.model.WfProcedimento;

public class ProcedimentosProcedimentoSiglaGet implements IProcedimentosProcedimentoSiglaGet {

	@Override
	public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception {
		WfProcedimento procedimento = WfProcedimento.findBySigla(req.procedimentoSigla);
		Procedimento r = new ProcedimentoAPIO(procedimento, true, ctx.getTitular(), ctx.getLotaTitular(),
				ctx.getUtil());
		resp.procedimento = r;
	}

	@Override
	public String getContext() {
		return "obter procedimento";
	}

}
