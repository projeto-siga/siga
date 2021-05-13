package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaMovimentacaoIdCancelarPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaMovimentacaoIdCancelarPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaMovimentacaoIdCancelarPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;

public class DocumentosSiglaMovimentacaoIdCancelarPost implements IDocumentosSiglaMovimentacaoIdCancelarPost {

	@Override
	public void run(DocumentosSiglaMovimentacaoIdCancelarPostRequest req,
			DocumentosSiglaMovimentacaoIdCancelarPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ApiContext.assertAcesso("");
				ExBL bl = Ex.getInstance().getBL();

				ExMobil mob = ApiContext.getMob(req.sigla);

				if (req.id == null || req.id.length() == 0 || "-".equals(req.id)) {
					bl.validarCancelamentoDeUltimaMovimentacao(ctx.getTitular(), ctx.getLotaTitular(), mob);
					bl.cancelarMovimentacao(ctx.getCadastrante(), ctx.getLotaTitular(), mob);
					return;
				}

				ExMovimentacao mov = ApiContext.getMov(mob, req.id);

				ApiContext.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

				bl.cancelar(ctx.getTitular(), ctx.getLotaTitular(), mob, mov, null, null, null, null);

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
		return "Arquivar no Corrente";
	}

}
