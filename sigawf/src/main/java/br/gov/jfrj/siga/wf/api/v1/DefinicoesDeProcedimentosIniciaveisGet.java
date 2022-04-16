
package br.gov.jfrj.siga.wf.api.v1;

import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.IDefinicoesDeProcedimentosIniciaveisGet;

public class DefinicoesDeProcedimentosIniciaveisGet implements IDefinicoesDeProcedimentosIniciaveisGet {

	@Override
	public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception {
	}

	@Override
	public String getContext() {
		return "obter definições de procedimentos iniciáveis";
	}

}
