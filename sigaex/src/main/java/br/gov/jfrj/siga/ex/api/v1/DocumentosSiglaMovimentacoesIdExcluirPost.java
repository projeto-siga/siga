package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaMovimentacoesIdExcluirPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaMovimentacoesIdExcluirPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaMovimentacoesIdExcluirPost;
import br.gov.jfrj.siga.ex.bl.Ex;

public class DocumentosSiglaMovimentacoesIdExcluirPost implements IDocumentosSiglaMovimentacoesIdExcluirPost {

	@Override
	public void run(DocumentosSiglaMovimentacoesIdExcluirPostRequest req,
			DocumentosSiglaMovimentacoesIdExcluirPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ctx.assertAcesso("");
				ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp,
						"Documento cuja movimentação será excluída");
				ExMovimentacao mov = ApiContext.getMov(mob, req.id);

				if (!Ex.getInstance().getComp().podeExcluirAnexo(ctx.getTitular(), ctx.getLotaTitular(), mob, mov)) {
					throw new PresentableUnloggedException("O anexo do documento " + mob.getSigla()
							+ " não pode ser excluído por " + ctx.getTitular().getSiglaCompleta() + "/"
							+ ctx.getLotaTitular().getSiglaCompleta());
				}
				Ex.getInstance().getBL().excluirMovimentacao(mov);

				resp.sigla = mob.doc().getCodigo();
				resp.status = "OK";
			} catch (Exception e) {
				ctx.rollback(e);
				throw e;
			}
		}
	}

	@Override
	public String getContext() {
		return "Excluir movimentação";
	}

}
