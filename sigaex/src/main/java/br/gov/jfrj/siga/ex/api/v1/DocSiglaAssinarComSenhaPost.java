package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaAssinarComSenhaPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaAssinarComSenhaPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocSiglaAssinarComSenhaPost;

public class DocSiglaAssinarComSenhaPost extends DocSiglaAssinarAutenticarComSenhaPost
		implements IDocSiglaAssinarComSenhaPost {

	public DocSiglaAssinarComSenhaPost() {
		super(false, "assinado");
	}

	@Override
	public void run(DocSiglaAssinarComSenhaPostRequest req, DocSiglaAssinarComSenhaPostResponse resp) throws Exception {
		super.executar(req.sigla);
		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Registrar assinatura com senha";
	}

}
