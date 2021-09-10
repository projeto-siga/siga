package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaArquivarCorrentePost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaArquivarCorrentePost implements IDocumentosSiglaArquivarCorrentePost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		DpPessoa cadastrante = ctx.getCadastrante();
		DpPessoa titular = cadastrante;
		DpLotacao lotaCadastrante = cadastrante.getLotacao();
		DpLotacao lotaTitular = ctx.getLotaTitular();

		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Arquivar");

		if (!Ex.getInstance().getComp().podeArquivarCorrente(titular, lotaTitular, mob)) {
			throw new PresentableUnloggedException(
					"O documento " + mob.getSigla() + " não pode ser arquivado no corrente por "
							+ titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta());
		}

		ctx.assertAcesso(mob, titular, lotaTitular);

		Ex.getInstance().getBL().arquivarCorrente(cadastrante, lotaCadastrante, mob, null, null, titular, false);

		resp.sigla = mob.doc().getCodigo();
		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Arquivar no Corrente";
	}

}
