package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaDesapensarPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaDesapensarPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaDesapensarPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocumentosSiglaDesapensarPost implements IDocumentosSiglaDesapensarPost {

	@Override
	public void run(DocumentosSiglaDesapensarPostRequest req, DocumentosSiglaDesapensarPostResponse resp)
			throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ctx.assertAcesso("");

				ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Desapensar");

				if (!Ex.getInstance().getComp().podeDesapensar(ctx.getTitular(), ctx.getLotaTitular(), mob)) {
					throw new PresentableUnloggedException("O documento " + mob.getSigla()
							+ " não pode ser desapensado por " + ctx.getTitular().getSiglaCompleta() + "/"
							+ ctx.getLotaTitular().getSiglaCompleta());
				}

				ctx.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

				Ex.getInstance().getBL().desapensarDocumento(ctx.getCadastrante(), ctx.getLotaCadastrante(), mob, null,
						null, ctx.getTitular());

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
