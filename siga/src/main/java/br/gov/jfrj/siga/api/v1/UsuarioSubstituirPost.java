package br.gov.jfrj.siga.api.v1;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IUsuarioSubstituirPost;
import br.gov.jfrj.siga.cp.bl.CpSubstituicaoBL;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class UsuarioSubstituirPost implements IUsuarioSubstituirPost {
	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		if (req.substituicaoId != null)
			CpSubstituicaoBL.substituir(ctx.getCadastrante(), Long.parseLong(req.substituicaoId));
		else
			CpSubstituicaoBL.finalizarSubstituicao(ctx.getCadastrante());
	}

	@Override
	public String getContext() {
		return "Substituir";
	}
}
