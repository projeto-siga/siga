package br.gov.jfrj.siga.ex.api.v1;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaAnotarPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaAnotarPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocSiglaAnotarPost;
import br.gov.jfrj.siga.ex.api.v1.TokenCriarPost.Usuario;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DocSiglaAnotarPost implements IDocSiglaAnotarPost {

	@Override
	public void run(DocSiglaAnotarPostRequest req,
			DocSiglaAnotarPostResponse resp) throws Exception {
		String authorization = TokenCriarPost.assertAuthorization();
		Usuario u = TokenCriarPost.assertUsuario();

		try {
			DpPessoa cadastrante = ExDao.getInstance().getPessoaPorPrincipal(u.usuario);
			DpLotacao lotaCadastrante = cadastrante.getLotacao();
			DpPessoa titular = cadastrante;
			DpLotacao lotaTitular = cadastrante.getLotacao();

			ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
			flt.setSigla(req.sigla);
			ExMobil mob = ExDao.getInstance().consultarPorSigla(flt);
			ExDocumento doc = mob.doc();

			Utils.assertAcesso(mob, titular, lotaTitular);

			Ex.getInstance()
					.getBL()
					.anotar(cadastrante, lotaCadastrante, mob, null, null,
							null, null, cadastrante, req.anotacao, null);
			ExDao.commitTransacao();
			resp.status = "OK";
		} catch (Exception ex) {
			ExDao.rollbackTransacao();
		}
	}


	@Override
	public String getContext() {
		return "Anotar documento";
	}
}
