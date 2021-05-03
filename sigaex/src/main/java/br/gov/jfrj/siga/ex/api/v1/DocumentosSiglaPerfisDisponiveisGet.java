package br.gov.jfrj.siga.ex.api.v1;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaPerfisDisponiveisGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaPerfisDisponiveisGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaPerfisDisponiveisGet;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.PerfilItem;
import br.gov.jfrj.siga.hibernate.ExDao;

@AcessoPublico
public class DocumentosSiglaPerfisDisponiveisGet implements IDocumentosSiglaPerfisDisponiveisGet {

	@Override
	public void run(DocumentosSiglaPerfisDisponiveisGetRequest req, DocumentosSiglaPerfisDisponiveisGetResponse resp)
			throws Exception {
		try (ApiContext ctx = new ApiContext(false, false)) {
			List<ExPapel> perfis = ExDao.getInstance().listarExPapel();

			resp.list = new ArrayList<>();
			for (ExPapel m : perfis) {
				PerfilItem mr = new PerfilItem();
				mr.idPerfil = m.getIdPapel().toString();
				mr.nome = m.getDescPapel();
				resp.list.add(mr);
			}
		}
	}

	@Override
	public String getContext() {
		return "obter lista de perfis ";
	}

}