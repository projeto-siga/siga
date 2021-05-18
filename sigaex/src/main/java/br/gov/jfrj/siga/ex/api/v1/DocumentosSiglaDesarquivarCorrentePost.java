package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaDesarquivarCorrentePostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaDesarquivarCorrentePostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaDesarquivarCorrentePost;
import br.gov.jfrj.siga.ex.bl.Ex;

public class DocumentosSiglaDesarquivarCorrentePost implements IDocumentosSiglaDesarquivarCorrentePost {

	@Override
	public void run(DocumentosSiglaDesarquivarCorrentePostRequest req,
			DocumentosSiglaDesarquivarCorrentePostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ctx.assertAcesso("");

				DpPessoa cadastrante = ctx.getCadastrante();
				DpPessoa titular = cadastrante;
				DpLotacao lotaCadastrante = cadastrante.getLotacao();
				DpLotacao lotaTitular = ctx.getLotaTitular();

				ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Desarquivar");

				if (!Ex.getInstance().getComp().podeDesarquivarCorrente(titular, lotaTitular, mob)) {
					throw new PresentableUnloggedException(
							"O documento " + mob.getSigla() + " não pode ser desarquivado do corrente por "
									+ titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta());
				}

				ctx.assertAcesso(mob, titular, lotaTitular);

				Ex.getInstance().getBL().desarquivarCorrente(cadastrante, lotaCadastrante, mob, null, titular);

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
		return "Desarquivar do Corrente";
	}

}
