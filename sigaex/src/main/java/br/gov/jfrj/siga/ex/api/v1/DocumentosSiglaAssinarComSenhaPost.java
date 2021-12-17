package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaAssinarComSenhaPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeAssinarComSenha;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaAssinarComSenhaPost extends DocumentosSiglaAssinarAutenticarComSenhaPost
		implements IDocumentosSiglaAssinarComSenhaPost {

	public DocumentosSiglaAssinarComSenhaPost() {
		super(false);
	}

	@Override
	protected void assertDocumento(DpPessoa titular, DpLotacao lotaTitular, ExMobil mob) throws Exception {
		// Usuário pode Assinar o documento?
		Ex.getInstance().getComp()
				.afirmar(
						"O documento " + mob.getSigla() + " não pode ser assinado com senha por "
								+ titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta(),
						ExPodeAssinarComSenha.class, titular, lotaTitular, mob);
	}

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		super.executar(req.sigla, (sigla, status) -> {
			resp.sigla = sigla;
			resp.status = status;
		}, ctx);
	}

	@Override
	public String getContext() {
		return "Registrar assinatura com senha";
	}

}
