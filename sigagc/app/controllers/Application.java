package controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import models.GcInformacao;
import models.GcMovimentacao;
import models.GcTipoInformacao;
import models.GcTipoMovimentacao;
import play.db.jpa.JPA;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http;
import utils.GcBL;
import utils.GcDao;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.ConexaoHTTP;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.dao.ModeloDao;

//Obtaining Hibernate objects programmatically
//
//You can always get access to a Hibernate Session object from an EntityManager instance through the standard EntityManager.getDelegate() method. This is a JPA specification feature.
//
//  @PersistenceContext EntityManager manager;
//  ...
//{      org.hibernate.Session session = (Session)manager.getDelegate();  }
//The specification, however, does not provide a way to get at the underlying implementation of a Query. Hibernate should provide most of its extended functionality through JPA query hints. For example, lets say you wanted to enable a query cache:
//
//  javax.persistence.Query query = manager.createQuery(...);
//  query.setHint("org.hibernate.cacheable", true);

public class Application extends Controller {

	private static GcDao dao() {
		return GcDao.getInstance();
	}

	private static CpIdentidade idc() {
		return (CpIdentidade) renderArgs.get("identidadeCadastrante");
	}

	@Before
	static void addDefaults() throws AplicacaoException {
		ModeloDao.freeInstance();

		if (request.url.contains("proxy"))
			return;

		renderArgs.put("_base", "http://localhost:8080");

		HashMap<String, String> atributos = new HashMap<String, String>();
		for (Http.Header h : request.headers.values())
			if (!h.name.equals("content-type"))
				atributos.put(h.name, h.value());

		String paginaVazia = ConexaoHTTP.get(
				"http://localhost:8080/siga/pagina_vazia.action?popup=false",
				atributos);

		String[] pageText = paginaVazia.split("<!-- insert body -->");
		String[] cabecalho = pageText[0].split("<!-- insert menu -->");
		// var codigoMenu;
		// if (popup){
		// codigoMenu = "";
		// cabecalho[1] = "";
		// } else {
		// codigoMenu = document.getElementById('codigoMenu').innerHTML;
		// }
		renderArgs.put("_cabecalho", pageText[0]);
		renderArgs.put("_rodape", pageText[1]);

		String[] IDs = ConexaoHTTP.get(
				"http://localhost:8080/siga/usuario_autenticado.action",
				atributos).split(";");

		renderArgs.put("cadastrante",
				JPA.em().find(DpPessoa.class, Long.parseLong(IDs[0])));

		if (IDs[1] != null && !IDs[1].equals(""))
			renderArgs.put("lotaCadastrante",
					JPA.em().find(DpLotacao.class, Long.parseLong(IDs[1])));

		if (IDs[2] != null && !IDs[2].equals(""))
			renderArgs.put("titular",
					JPA.em().find(DpPessoa.class, Long.parseLong(IDs[2])));

		if (IDs[3] != null && !IDs[3].equals(""))
			renderArgs.put("lotaTitular",
					JPA.em().find(DpLotacao.class, Long.parseLong(IDs[3])));

		if (IDs[4] != null && !IDs[4].equals(""))
			renderArgs.put("identidadeCadastrante",
					JPA.em().find(CpIdentidade.class, Long.parseLong(IDs[4])));

		int a = 0;

		dao();

		// } catch (Exception e) {
		// redirect("http://localhost:8080/siga");
		// }

	}

	public static void gadget() {
		Query query = JPA.em().createNamedQuery("contarGcMarcas");
		query.setParameter("idPessoaIni",
				((DpPessoa) renderArgs.get("cadastrante")).getIdInicial());
		query.setParameter("idLotacaoIni",
				((DpLotacao) renderArgs.get("lotaTitular")).getIdInicial());
		List contagens = query.getResultList();
		render(contagens);
	}

	public static void index() {
		listar();
	}

	public static void listar() {
		if (GcTipoInformacao.find("nome_tipo_informacao = ?",
				"Registro de Conhecimento").first() == null) {
			GcTipoInformacao tinf = new GcTipoInformacao();
			tinf.nome = "Registro de Conhecimento";
			tinf.save();

			// GcTipoInteracao tint = new GcTipoInteracao();
			// tint.desc_tipo_interacao = "Registro de Conhecimento";
			// tint.save();

		}
		List<GcInformacao> informacoes = GcInformacao.all().fetch();
		render(informacoes);
	}

	public static void exibir(long id, boolean fContabilizar) {
		GcInformacao informacao = GcInformacao.findById(id);
		render(informacao);
	}

	public static void editar(long id) {
		GcInformacao informacao = null;
		if (id != 0)
			informacao = GcInformacao.findById(id);
		else
			informacao = new GcInformacao();
		List<GcInformacao> tiposInformacao = GcTipoInformacao.all().fetch();
		render(informacao, tiposInformacao);
	}

	public static void fechar(long id) throws Exception {
		GcInformacao inf = GcInformacao.findById(id);
		GcBL.movimentar(inf, GcTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO,
				null, null, null, null, null, null, null);
		GcBL.gravar(inf, idc());
		exibir(id, false);
	}

	public static void gravar(GcInformacao informacao) {
		if (informacao.autor == null) {
			informacao.autor = (DpPessoa) renderArgs.get("cadastrante");
			informacao.lotacao = informacao.autor.getLotacao();
			informacao.ou = informacao.autor.getOrgaoUsuario();
		}
		if (informacao.tipo == null)
			informacao.tipo = GcTipoInformacao.all().first();
		GcBL.gravar(informacao, idc());
		index();
	}

	public static void remover(long id) {
		GcInformacao informacao = GcInformacao.findById(id);
		informacao.delete();
		index();
	}

	public static void proxy(String url) throws Exception {
		URL u = new URL(url);
		URLConnection conn = u.openConnection();
		for (Http.Header h : request.headers.values())
			conn.setRequestProperty(h.name, h.value());

		// BufferedReader in = new BufferedReader(new InputStreamReader(u
		// .openStream()));
		BufferedReader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String inputLine;
		StringBuilder sb = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			sb.append(inputLine);
		}
		in.close();

		renderHtml(sb.toString());

		// byte ba[] = inputLine.getBytes("utf-8");
		// response.contentType = "text/html";
		// response.out.write(ba);
		// response.headers.put("Content-Length", new Http.Header(
		// "Content-Length", Integer.toString(ba.length)));
	}
}