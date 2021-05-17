package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaSobrestarPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaSobrestarPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaSobrestarPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocumentosSiglaSobrestarPost implements IDocumentosSiglaSobrestarPost {

	@Override
	public void run(DocumentosSiglaSobrestarPostRequest req, DocumentosSiglaSobrestarPostResponse resp)
			throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ApiContext.assertAcesso("");
				SigaObjects so = ApiContext.getSigaObjects();

				DpPessoa cadastrante = so.getCadastrante();
				DpPessoa titular = cadastrante;
				DpLotacao lotaCadastrante = cadastrante.getLotacao();
				DpLotacao lotaTitular = so.getLotaTitular();

				ExMobil mob = ApiContext.getMob(req.sigla);

				if (!Ex.getInstance().getComp().podeSobrestar(titular, lotaTitular, mob)) {
					throw new PresentableUnloggedException(
							"O documento " + mob.getSigla() + " não pode ser sobrestado por "
									+ titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta());
				}

				ApiContext.assertAcesso(mob, titular, lotaTitular);

				Ex.getInstance().getBL().sobrestar(cadastrante, lotaCadastrante, mob, null, null, titular);

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
