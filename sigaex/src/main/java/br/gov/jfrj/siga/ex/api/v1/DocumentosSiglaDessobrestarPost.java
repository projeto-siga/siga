package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaDessobrestarPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaDessobrestarPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaDessobrestarPost;
import br.gov.jfrj.siga.ex.bl.Ex;

public class DocumentosSiglaDessobrestarPost implements IDocumentosSiglaDessobrestarPost {

	@Override
	public void run(DocumentosSiglaDessobrestarPostRequest req, DocumentosSiglaDessobrestarPostResponse resp)
			throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ctx.assertAcesso("");

				DpPessoa cadastrante = ctx.getCadastrante();
				DpPessoa titular = cadastrante;
				DpLotacao lotaCadastrante = cadastrante.getLotacao();
				DpLotacao lotaTitular = ctx.getLotaTitular();

				ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Dessobrestar");

				if (!Ex.getInstance().getComp().podeDesobrestar(titular, lotaTitular, mob)) {
					throw new PresentableUnloggedException(
							"O documento " + mob.getSigla() + " não pode ser dessobrestado por "
									+ titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta());
				}

				ctx.assertAcesso(mob, titular, lotaTitular);

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
