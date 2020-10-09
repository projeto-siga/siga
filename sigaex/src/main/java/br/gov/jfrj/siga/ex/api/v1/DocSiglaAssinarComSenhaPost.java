package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaAssinarComSenhaPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaAssinarComSenhaPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocSiglaAssinarComSenhaPost;
import br.gov.jfrj.siga.ex.bl.Ex;

public class DocSiglaAssinarComSenhaPost extends DocSiglaAssinarAutenticarComSenhaPost
		implements IDocSiglaAssinarComSenhaPost {

	public DocSiglaAssinarComSenhaPost() {
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
	public void run(DocSiglaAssinarComSenhaPostRequest req, DocSiglaAssinarComSenhaPostResponse resp) throws Exception {
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
