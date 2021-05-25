package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.itextpdf.Status;
import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.context.NaoAtualizarCacheDeConfiguracoes;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IStatusChaveGet;

@AcessoPublico
@NaoAtualizarCacheDeConfiguracoes
public class StatusChaveGet implements IStatusChaveGet {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		Status as = Status.get(req.chave);
		if (as == null)
			return;
		if (as.ex != null)
			throw as.ex;
		resp.mensagem = as.mensagem;
		resp.indice = (double) as.indice;
		resp.contador = (double) as.contador;
		if (as.bytes != null)
			resp.bytes = (double) as.bytes;
	}

	@Override
	public String getContext() {
		return "obter status da geração do PDF completo";
	}

}
