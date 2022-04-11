
package br.gov.jfrj.siga.wf.api.v1;

import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.ITiposDeVinculoComPrincipalGet;

public class TiposDeVinculoComPrincipalGet implements ITiposDeVinculoComPrincipalGet {

	@Override
	public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception {
	}

	@Override
	public String getContext() {
		return "listar tipos de vínculo com o principal";
	}

}
