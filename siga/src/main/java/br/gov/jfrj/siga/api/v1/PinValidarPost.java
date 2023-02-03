package br.gov.jfrj.siga.api.v1;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.commons.lang.StringUtils;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPinValidarPost;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class PinValidarPost implements IPinValidarPost {
	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		
		byte[] credDecoded = Base64.getDecoder().decode(req.pinBasicAuth);
		String credentials = new String(credDecoded, StandardCharsets.UTF_8);
		final String[] values = credentials.split(":", 2);

		String cpf = values[0];
		String pin = values[1];
		
		if (!StringUtils.leftPad(ctx.getCadastrante().getCpfPessoa().toString(), 11, "0").equals(cpf)) {
			throw new RegraNegocioException("Não é possível validar PIN: Usuário divergente do Autenticado.");
		}
		
		//Se Válido devolve um HTTP_OK. Se não válido devolve 500 com o erro violado.
		Cp.getInstance().getBL().validaPinIdentidade(pin, ctx.getIdentidadeCadastrante());
		

	}

	@Override
	public String getContext() {
		return "autenticar PIN";
	}
}
