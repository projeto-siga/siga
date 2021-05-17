package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaArquivarCorrentePostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaArquivarCorrentePostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaArquivarCorrentePost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocumentosSiglaArquivarCorrentePost implements IDocumentosSiglaArquivarCorrentePost {

	@Override
	public void run(DocumentosSiglaArquivarCorrentePostRequest req, DocumentosSiglaArquivarCorrentePostResponse resp)
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

				if (!Ex.getInstance().getComp().podeArquivarCorrente(titular, lotaTitular, mob)) {
					throw new PresentableUnloggedException(
							"O documento " + mob.getSigla() + " não pode ser arquivado no corrente por "
									+ titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta());
				}

				ApiContext.assertAcesso(mob, titular, lotaTitular);

				Ex.getInstance().getBL().arquivarCorrente(cadastrante, lotaCadastrante, mob, null, null, titular,
						false);

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
