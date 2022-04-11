
package br.gov.jfrj.siga.wf.api.v1;

import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.IDefinicoesDeProcedimentosDefinicaoDeProcedimentoSiglaRelatorioRelatorioIdPost;

public class DefinicoesDeProcedimentosDefinicaoDeProcedimentoSiglaRelatorioRelatorioIdPost
		implements IDefinicoesDeProcedimentosDefinicaoDeProcedimentoSiglaRelatorioRelatorioIdPost {

	@Override
	public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception {
	}

	@Override
	public String getContext() {
		return "relatório de métricas";
	}

}
