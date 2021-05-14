package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaTornarSemEfeitoPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaTornarSemEfeitoPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaTornarSemEfeitoPost;
import br.gov.jfrj.siga.ex.bl.Ex;

public class DocumentosSiglaTornarSemEfeitoPost implements IDocumentosSiglaTornarSemEfeitoPost {

	@Override
	public void run(DocumentosSiglaTornarSemEfeitoPostRequest req, DocumentosSiglaTornarSemEfeitoPostResponse resp)
			throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ApiContext.assertAcesso("");

				ExMobil mob = ApiContext.getMob(req.sigla);

				if (!Ex.getInstance().getComp().podeTornarDocumentoSemEfeito(ctx.getTitular(), ctx.getLotaTitular(),
						mob)) {
					throw new PresentableUnloggedException("O documento " + mob.getSigla()
							+ " não pode ser tornado sem efeito por " + ctx.getTitular().getSiglaCompleta() + "/"
							+ ctx.getLotaTitular().getSiglaCompleta());
				}

				if (req.motivo == null || req.motivo.isEmpty())
					throw new PresentableUnloggedException("Favor informar o motivo");

				ApiContext.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

				Ex.getInstance().getBL().tornarDocumentoSemEfeito(ctx.getCadastrante(), ctx.getLotaCadastrante(),
						mob.doc(), req.motivo);

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
