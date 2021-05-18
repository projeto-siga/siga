package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaMovimentacoesIdAutenticarComSenhaPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaMovimentacoesIdAutenticarComSenhaPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaMovimentacoesIdAutenticarComSenhaPost;
import br.gov.jfrj.siga.ex.bl.Ex;

public class DocumentosSiglaMovimentacoesIdAutenticarComSenhaPost
		implements IDocumentosSiglaMovimentacoesIdAutenticarComSenhaPost {
	@Override
	public void run(DocumentosSiglaMovimentacoesIdAutenticarComSenhaPostRequest req,
			DocumentosSiglaMovimentacoesIdAutenticarComSenhaPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ApiContext.assertAcesso("");
				ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp,
						"Documento cujo anexo receberá a autenticação");
				ExMovimentacao mov = ApiContext.getMov(mob, req.id);

				Ex.getInstance().getBL().assinarMovimentacaoComSenha(ctx.getCadastrante(), ctx.getLotaTitular(), mov,
						null, ctx.getCadastrante().getSiglaCompleta(), null, false, false,
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA);

				resp.status = "OK";
			} catch (Exception e) {
				ctx.rollback(e);
				throw e;
			}
		}
	}

	@Override
	public String getContext() {
		return "Autenticar movimentação com senha";
	}
}
