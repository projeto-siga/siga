package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaAssinarComSenhaPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaAssinarComSenhaPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaAssinarComSenhaPost;
import br.gov.jfrj.siga.ex.bl.Ex;

public class DocumentosSiglaAssinarComSenhaPost extends DocumentosSiglaAssinarAutenticarComSenhaPost
		implements IDocumentosSiglaAssinarComSenhaPost {

	public DocumentosSiglaAssinarComSenhaPost() {
		super(false);
	}

	@Override
	protected void assertDocumento(DpPessoa titular, DpLotacao lotaTitular, ExMobil mob) throws Exception {
		// Usuário pode Assinar o documento?
		if (!Ex.getInstance().getComp().podeAssinarComSenha(titular, lotaTitular, mob)) {
			throw new PresentableUnloggedException(
					"O documento " + mob.getSigla() + " não pode ser assinado com senha por "
							+ titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta());
		}
	}

	@Override
	public void run(DocumentosSiglaAssinarComSenhaPostRequest req, DocumentosSiglaAssinarComSenhaPostResponse resp) throws Exception {
		super.executar(req.sigla, (sigla, status) -> {
			resp.sigla = sigla;
			resp.status = status;
		});
	}

	@Override
	public String getContext() {
		return "Registrar assinatura com senha";
	}

}
