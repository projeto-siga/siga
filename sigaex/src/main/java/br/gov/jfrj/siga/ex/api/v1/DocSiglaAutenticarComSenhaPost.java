package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaAutenticarComSenhaPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaAutenticarComSenhaPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocSiglaAutenticarComSenhaPost;

public class DocSiglaAutenticarComSenhaPost extends DocSiglaAssinarAutenticarComSenhaPost
		implements IDocSiglaAutenticarComSenhaPost {

	public DocSiglaAutenticarComSenhaPost() {
		super(true, "autenticado");
	}

	@Override
	public void run(DocSiglaAutenticarComSenhaPostRequest req, DocSiglaAutenticarComSenhaPostResponse resp)
			throws Exception {
		super.executar(req.sigla);
		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Registrar autenticação com senha";
	}

}
