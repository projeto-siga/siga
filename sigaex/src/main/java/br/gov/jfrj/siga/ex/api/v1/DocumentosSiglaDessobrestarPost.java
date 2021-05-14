package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaDessobrestarPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaDessobrestarPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaDessobrestarPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocumentosSiglaDessobrestarPost implements IDocumentosSiglaDessobrestarPost {

	@Override
	public void run(DocumentosSiglaDessobrestarPostRequest req, DocumentosSiglaDessobrestarPostResponse resp)
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

				if (!Ex.getInstance().getComp().podeDesobrestar(titular, lotaTitular, mob)) {
					throw new PresentableUnloggedException(
							"O documento " + mob.getSigla() + " não pode ser dessobrestado por "
									+ titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta());
				}

				ApiContext.assertAcesso(mob, titular, lotaTitular);

				Ex.getInstance().getBL().desobrestar(cadastrante, lotaCadastrante, mob, null, titular);

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
