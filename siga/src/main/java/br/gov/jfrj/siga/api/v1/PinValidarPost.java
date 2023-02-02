package br.gov.jfrj.siga.api.v1;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPinValidarPost;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class PinValidarPost implements IPinValidarPost {
	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		
		final String pin = req.pin;
		resp.isValido = Cp.getInstance().getBL().validaPinIdentidade(pin, ctx.getIdentidadeCadastrante());

	}

	@Override
	public String getContext() {
		return "autenticar PIN";
	}
}
