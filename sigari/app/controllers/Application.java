package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import models.RiAtualizacao;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.TextFragment;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import play.Play;
import play.mvc.Before;
import play.mvc.Catch;
import play.mvc.Http;
import play.mvc.Scope.RenderArgs;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.ConexaoHTTP;
import br.gov.jfrj.siga.model.DadosRI;
import br.gov.jfrj.siga.model.dao.ModeloDao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

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

public class Application extends SigaApplication {

	private static final String HTTP_LOCALHOST_8080 = "http://localhost:8080";
	private static final int CONTROLE_HASH_TAG = 1;

	@Before
	public static void addDefaultsAlways() throws Exception {
		prepararSessao();
		// TAH: Copiar essa classe e fazer as alterações necessárias
		// SrConfiguracaoBL.get().limparCacheSeNecessario();
	}

	@Before(unless = "publicKnowledge")
	public static void addDefaults() throws Exception {

		try {
			obterCabecalhoEUsuario("#f1f4e2");
			assertAcesso("");
		} catch (Exception e) {
			tratarExcecoes(e);
		}

		try {
			assertAcesso("ADM:Administrar");
			renderArgs.put("exibirMenuAdministrar", true);
		} catch (Exception e) {
			renderArgs.put("exibirMenuAdministrar", false);
		}
	}

	public static void index() throws IOException, ParseException,
			InvalidTokenOffsetsException {
		buscar(null);
	}

	public static void estatisticaGeral() {
	}

	private static void addDoc(IndexWriter w, String title, String isbn)
			throws IOException {
		Document doc = new Document();
		doc.add(new TextField("title", title, Field.Store.YES));
		doc.add(new StringField("isbn", isbn, Field.Store.YES));
		w.addDocument(doc);
	}

	public static void inicializarAtualizacao() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0;; i++) {
			String s = Play.configuration.getProperty("atualizar." + i);
			if (s == null)
				break;
			String[] a = s.split(";");
			RiAtualizacao atu = RiAtualizacao.find("sigla", a[1]).first();
			if (atu == null) {
				atu = RiAtualizacao.find("nome", a[1]).first();
			}
			if (atu == null) {
				atu = RiAtualizacao.find("uri", a[1]).first();
			}
			if (atu == null) {
				atu = new RiAtualizacao();
				atu.sigla = a[0];
				atu.nome = a[1];
				atu.uri = a[2];
				atu.ultimaAtualizacao = null;
				atu.save();
				sb.append("Criado: " + atu.sigla + " - " + atu.nome + " - url: " + atu.uri);
			} else {
				atu.sigla = a[0];
				atu.nome = a[1];
				atu.uri = a[2];
				atu.save();
				sb.append("Atualizado: " + atu.sigla + " - " + atu.nome + " - url: " + atu.uri);
			}
			
		}
		renderText(sb.toString());
	}

	public static void atualizarIndice() throws IOException {
		Gson gson = new GsonBuilder()
		   .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z").create();

		StringBuilder sb = new StringBuilder();
		BrazilianAnalyzer analyzer = getLuceneAnalyzer();

		Directory index = getLuceneDirectory();

		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46,
				analyzer);

		List<RiAtualizacao> l = RiAtualizacao.findAll();

		ConexaoHTTP http = new ConexaoHTTP();
		ModeloDao.freeInstance();
		
		if (request.url.contains("proxy"))
			return;
		RenderArgs.current().put("_base", HTTP_LOCALHOST_8080);
		HashMap<String, String> atributos = new HashMap<String, String>();
		
		for (Http.Header h : request.headers.values())
			if (!h.name.equals("content-type"))
				atributos.put(h.name, h.value());

		String IDPSessionID = params.get("idp");
		
		for (RiAtualizacao atu : l) {
			String json = http.get(atu.uri + "?ultimaAtualizacao=" + (atu.ultimaAtualizacao == null ? "null" : atu.ultimaAtualizacao.getTime())  + "&desempate=" + (atu.idDesempate == null ? "null" : atu.idDesempate), IDPSessionID);

			//String json = ConexaoHTTP.get(atu.uri + "?ultimaAtualizacao=" + (atu.ultimaAtualizacao == null ? "null" : atu.ultimaAtualizacao.getTime())  + "&desempate=" + (atu.idDesempate == null ? "null" : atu.idDesempate));
			List<DadosRI> dris = (List<DadosRI>) gson.fromJson(json, new TypeToken<List<DadosRI>>() {}.getType());

			IndexWriter w = new IndexWriter(index, config);
			for (DadosRI dri : dris) {
				atualizarDadosRI(w, dri);
				atu.ultimaAtualizacao = dri.ultimaAtualizacao;
				atu.idDesempate = dri.idDesempate;
				if (dri.ativo) {
					sb.append("Criado/atualizado: " + atu.sigla + " - " + dri.sigla + " - " + dri.titulo + "\n");
				} else {
					sb.append("Removido: " + atu.sigla + " - " + dri.sigla + " - " + dri.titulo + "\n");
				}
			}
			w.close();
			atu.save();
		}
		renderText(sb.toString());
	}

	private static BrazilianAnalyzer getLuceneAnalyzer() {
		BrazilianAnalyzer analyzer = new BrazilianAnalyzer(Version.LUCENE_46);
		return analyzer;
	}

	private static Directory getLuceneDirectory() throws IOException {
		String s = Play.configuration.getProperty("diretorio");
		File file = new File(s);
		Directory index = new SimpleFSDirectory(file);
		return index;
	}

	private static void atualizarDadosRI(IndexWriter w, DadosRI dri)
			throws IOException {
		Term term = new Term("url", dri.uri);
		
		if (dri.ativo) {
			Document doc = new Document();
			doc.add(new StringField("sigla", dri.sigla, Field.Store.YES));
			doc.add(new TextField("titulo", dri.titulo, Field.Store.YES));
			doc.add(new StringField("url", dri.uri, Field.Store.YES));
			doc.add(new StringField("data",
					dri.ultimaAtualizacao.toGMTString(), Field.Store.YES));
			doc.add(new StringField("desempate",
					Long.toString(dri.idDesempate), Field.Store.YES));

			// FieldType type = new FieldType();
			// type.setIndexed(true);
			// type.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
			// type.setStored(true);
			// type.setStoreTermVectors(true);
			// type.setTokenized(true);
			// type.setStoreTermVectorOffsets(true);
			// Field field = new Field("conteudo", dri.conteudo, type);//with
			// term vector enabled
			// doc.add(field);
			doc.add(new TextField("conteudo", dri.conteudo, Field.Store.YES));
			w.updateDocument(term, doc);
		} else {
		w.deleteDocuments(term);
		}
	}

	public static void buscar(String texto) throws IOException, ParseException,
			InvalidTokenOffsetsException {
		StringBuilder sb = new StringBuilder();

		if (texto != null) {
			BrazilianAnalyzer analyzer = getLuceneAnalyzer();
			Query q = new QueryParser(Version.LUCENE_46, "conteudo",
					analyzer).parse(texto);

			int hitsPerPage = 10;
			IndexReader reader = IndexReader.open(getLuceneDirectory());
			IndexSearcher searcher = new IndexSearcher(reader);

			TopDocs hits = searcher.search(q, reader.maxDoc());

			SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
			Highlighter highlighter = new Highlighter(htmlFormatter,
					new QueryScorer(q));

			if (hits.scoreDocs.length > 0) {
				// sb.append(hits.totalHits + " resultados encontrados.");
				sb.append("<ol>");
				for (int i = 0; i < hits.scoreDocs.length; i++) {
					int id = hits.scoreDocs[i].doc;
					Document doc = searcher.doc(id);
					sb.append("<li style=\"padding-top:1em\">");
					sb.append("<a href=\"");
					sb.append(doc.get("url"));
					sb.append("\">");
					sb.append(doc.get("sigla"));
					sb.append(" - ");
					sb.append(doc.get("titulo"));
					sb.append("</a><br/>");
					String text = doc.get("conteudo");
					TokenStream tokenStream = TokenSources.getAnyTokenStream(
							searcher.getIndexReader(), hits.scoreDocs[i].doc,
							"conteudo", analyzer);
					TextFragment[] frag = highlighter.getBestTextFragments(
							tokenStream, text, true, 4);
					for (int j = 0; j < frag.length; j++) {
						if ((frag[j] != null) && (frag[j].getScore() > 0)) {
							sb.append((frag[j].toString()));
						}
					}
					sb.append("</li>");
				}
				sb.append("</ol>");

				reader.close();

			} else {
				sb.append(hits.totalHits + "Nenhum resultado encontrado.");
			}
		}

		String resultado = sb.toString();

		render(resultado, texto);
	}

	public static void proxy(String url) throws Exception {
		URL u = new URL(url);
		URLConnection conn = u.openConnection();
		for (Http.Header h : request.headers.values())
			conn.setRequestProperty(h.name, h.value());

		BufferedReader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String inputLine;
		StringBuilder sb = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			sb.append(inputLine);
		}
		in.close();

		renderHtml(sb.toString());
	}

	public static void selecionarSiga(String sigla, String tipo, String nome)
			throws Exception {
		proxy("http://localhost:8080/siga/" + tipo + "/selecionar.action?"
				+ "propriedade=" + tipo + nome + "&sigla="
				+ URLEncoder.encode(sigla, "UTF-8"));
	}

	public static void buscarSiga(String sigla, String tipo, String nome)
			throws Exception {
		proxy("http://localhost:8080/siga/" + tipo + "/buscar.action?"
				+ "propriedade=" + tipo + nome + "&sigla="
				+ URLEncoder.encode(sigla, "UTF-8"));
	}

	public static void buscarSigaFromPopup(String tipo) throws Exception {
		String paramString = "?";
		for (String s : request.params.all().keySet())
			if (!s.equals("body"))
				paramString += s + "="
						+ URLEncoder.encode(request.params.get(s), "UTF-8")
						+ "&";
		proxy("http://localhost:8080/siga/" + tipo + "/buscar.action"
				+ paramString);
	}

	protected static void assertAcesso(String path) throws Exception {
		SigaApplication.assertAcesso("RI:Módulo de Recuperação da Informação"
				+ path);
	}

	public static void erro(String message, String stackTrace) {
		render(message, stackTrace);
	}

	@Catch(value = AplicacaoException.class, priority = 1)
	public static void catchError(Throwable throwable) {
		while (throwable.getMessage() == null && throwable.getCause() != null)
			throwable = throwable.getCause();
		java.io.StringWriter sw = new java.io.StringWriter();
		java.io.PrintWriter pw = new java.io.PrintWriter(sw);
		throwable.printStackTrace(pw);
		String stackTrace = sw.toString();
		String message = throwable.getMessage();
		if (message == null)
			message = "Nenhuma informação disponível.";
		erro(message, stackTrace);
	}

}