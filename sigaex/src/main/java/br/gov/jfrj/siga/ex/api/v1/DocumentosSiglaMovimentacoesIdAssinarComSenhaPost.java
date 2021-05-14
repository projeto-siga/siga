package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaMovimentacoesIdAssinarComSenhaPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaMovimentacoesIdAssinarComSenhaPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaMovimentacoesIdAssinarComSenhaPost;
import br.gov.jfrj.siga.ex.bl.Ex;

public class DocumentosSiglaMovimentacoesIdAssinarComSenhaPost
		implements IDocumentosSiglaMovimentacoesIdAssinarComSenhaPost {
	@Override
	public void run(DocumentosSiglaMovimentacoesIdAssinarComSenhaPostRequest req,
			DocumentosSiglaMovimentacoesIdAssinarComSenhaPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ApiContext.assertAcesso("");
				ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp,
						"Documento cujo anexo receberá a assinatura");
				ExMovimentacao mov = ApiContext.getMov(mob, req.id);

				Ex.getInstance().getBL().assinarMovimentacaoComSenha(ctx.getCadastrante(), ctx.getLotaTitular(), mov,
						null, ctx.getCadastrante().getSiglaCompleta(), null, false, false,
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA);

				resp.status = "OK";
			} catch (Exception e) {
				ctx.rollback(e);
				throw e;
			}
		}
	}

	@Override
	public String getContext() {
		return "Assinar movimentação com senha";
	}
}
