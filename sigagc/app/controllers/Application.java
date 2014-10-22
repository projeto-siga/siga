package controllers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import models.GcAcesso;
import models.GcArquivo;
import models.GcInformacao;
import models.GcMovimentacao;
import models.GcTag;
import models.GcTipoInformacao;
import models.GcTipoMovimentacao;
import notifiers.Correio;

import org.apache.commons.lang.ArrayUtils;

import play.Play;
import play.Play.Mode;
import play.data.Upload;
import play.db.jpa.JPA;
import play.mvc.Before;
import play.mvc.Catch;
import play.mvc.Http;
import utils.GcArvore;
import utils.GcBL;
import utils.GcCloud;
import utils.GcGraficoEvolucao;
import utils.GcGraficoEvolucaoItem;
import utils.GcInformacaoFiltro;
import utils.diff_match_patch;
import utils.diff_match_patch.Diff;
import utils.diff_match_patch.Operation;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.ConexaoHTTP;
import br.gov.jfrj.siga.cp.CpGrupoDeEmail;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.DadosRI;
import br.gov.jfrj.siga.model.dao.ModeloDao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
		Cp.getInstance().getConf().limparCacheSeNecessario();
	}

	@Before(unless = { "publicKnowledge", "dadosRI" })
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

	public static void gadget() {
		Query query = JPA.em().createNamedQuery("contarGcMarcas");
		query.setParameter("idPessoaIni",
				((DpPessoa) renderArgs.get("cadastrante")).getIdInicial());
		query.setParameter("idLotacaoIni",
				((DpLotacao) renderArgs.get("lotaTitular")).getIdInicial());
		List contagens = query.getResultList();
		render(contagens);
	}

	public static void publicKnowledge(Long id, String[] tags, String estilo,
			String msgvazio, String urlvazio, String titulo, boolean popup,
			String estiloBusca) throws Exception {
		renderKnowledge(id, tags, estilo, msgvazio, urlvazio, titulo, true,
				popup, estiloBusca);
	}

	public static void knowledge(Long id, String[] tags, String estilo,
			String msgvazio, String urlvazio, String titulo, boolean popup,
			String estiloBusca) throws Exception {
		renderKnowledge(id, tags, estilo, msgvazio, urlvazio, titulo, false,
				popup, estiloBusca);
	}

	private static void renderKnowledge(Long id, String[] tags, String estilo,
			String msgvazio, String urlvazio, String titulo,
			boolean testarAcessoPublico, boolean popup, String estiloBusca)
			throws UnsupportedEncodingException, Exception {
		int index = Integer.MAX_VALUE;
		Long idOutroConhecimento = 0l;
		GcInformacao info = null;
		Set<GcTag> set = null;
		if (tags != null)
			set = GcBL.buscarTags(tags, true);
		estiloBusca = estiloBusca != null ? estiloBusca.substring(0, 1)
				.toUpperCase() + estiloBusca.substring(1) : "";
		Query query = JPA.em().createNamedQuery(
				"buscarConhecimento" + estiloBusca);
		query.setParameter("tags", set);
		List<Object[]> conhecimentosCandidatos = query.getResultList();
		List<Object[]> conhecimentos = new ArrayList<Object[]>();
		for (Object[] o : conhecimentosCandidatos) {
			idOutroConhecimento = Long.parseLong(o[0].toString());
			if (idOutroConhecimento.equals(id))
				continue;

			info = GcInformacao.findById(idOutroConhecimento);

			if (testarAcessoPublico
					&& (info.visualizacao.id != GcAcesso.ACESSO_PUBLICO))
				continue;

			// o[3] = URLEncoder.encode(info.getSigla(), "UTF-8");
			o[3] = info.getSigla();
			if (o[2] != null && o[2] instanceof byte[]) {
				String s = new String((byte[]) o[2], Charset.forName("utf-8"));
				s = GcBL.ellipsize(s, 100);
				o[2] = s;
			}
			conhecimentos.add(o);
		}

		if (conhecimentos.size() == 1 && "inplace".equals(estilo)) {
			GcInformacao inf = GcInformacao.findById(conhecimentos.get(0)[0]);
			conhecimentos.get(0)[1] = inf.arq.titulo;
			conhecimentos.get(0)[2] = inf.getConteudoHTML();
		}

		if (conhecimentos.size() == 0)
			conhecimentos = null;

		String referer = null;
		try {
			referer = request.headers.get("referer").value();
		} catch (Exception e) {

		}

		String classificacao = "";
		if (tags != null && tags.length > 0) {
			for (String s : tags) {
				if (classificacao.length() > 0)
					classificacao += ", ";
				classificacao += s;
			}
		}
		// Necessário pq para criar um novo conhecimento a partir de um já
		// existente, a classificação
		// é passada como queryString. Sem fazer isso, as hashTags não são
		// passadas.
		// classificacao = URLEncoder.encode(classificacao, "UTF-8");
		// if (msgvazio != null) {
		// msgvazio = msgvazio.replace("*aqui*", "<a href=\"" + urlvazio +
		// "\">aqui</a>");
		// }

		if (estilo != null)
			render("@knowledge_" + estilo, conhecimentos, classificacao,
					msgvazio, urlvazio, titulo, referer, popup);
		else
			render("@knowledge", conhecimentos, classificacao, msgvazio,
					urlvazio, titulo, referer, popup);
	}

	public static void updateTag(String before, String after) {

		// Edson: Atualizando tags de classificacao:
		JPA.em()
				.createQuery(
						"update GcTag set titulo = '" + after
								+ "' where titulo = '" + before + "'")
				.executeUpdate();

		// Edson: Atualizando tags de ancora. O problema aqui eh que,
		// muitas vezes, o before aparece em tags ancora acompanhado de
		// outra classificacao. Por exemplo, cadeira-consertar, onde
		// before eh consertar. Os patterns abaixo funcionam, mas nao
		// sempre. Por exemplo, se houver uma tag cadeira-tentar-consertar
		// alem da cadeira-consertar, ela vai ser erroneamente atualizada.
		List<GcTag> tags = JPA
				.em()
				.createQuery(
						"from GcTag where titulo like '%" + before
								+ "%' and tipo.id = 3").getResultList();
		for (GcTag t : tags) {
			t.titulo = t.titulo.replaceAll("^" + before + "(-.+|$)", after
					+ "$1");
			t.titulo = t.titulo.replaceAll("(.+-|^)" + before + "$", "$1"
					+ after);
			t.save();
		}

		// Edson: Atualizando os arquivos:
		List<GcArquivo> arqs = JPA
				.em()
				.createQuery(
						"select arq from GcInformacao inf inner join inf.arq arq"
								+ " where inf.hisDtFim is null and arq.classificacao like '%"
								+ before + "%'").getResultList();
		for (GcArquivo arq : arqs) {
			if (arq.classificacao.startsWith("^")) {
				arq.classificacao = arq.classificacao.replace(":" + before, ":"
						+ after);
				arq.classificacao = arq.classificacao.replaceAll("-" + before
						+ "$", "-" + after);
			} else {
				arq.classificacao = arq.classificacao.replaceAll("(@" + before
						+ ")(,|$)", "@" + after + "$2");
			}
			arq.save();
		}

		renderText("OK");
	}

	public static void index() {
		estatisticaGeral();
	}

	public static void estatisticaGeral() {
		// List<GcInformacao> lista = GcInformacao.all().fetch();

		Query query1 = JPA.em().createNamedQuery("maisRecentes");
		query1.setMaxResults(5);
		List<Object[]> listaMaisRecentes = query1.getResultList();
		if (listaMaisRecentes.size() == 0)
			listaMaisRecentes = null;

		Query query2 = JPA.em().createNamedQuery("maisVisitados");
		query2.setMaxResults(5);
		List<Object[]> listaMaisVisitados = query2.getResultList();
		if (listaMaisVisitados.size() == 0)
			listaMaisVisitados = null;

		Query query3 = JPA.em().createNamedQuery("principaisAutores");
		query3.setMaxResults(5);
		List<Object[]> listaPrincipaisAutores = query3.getResultList();
		if (listaPrincipaisAutores.size() == 0)
			listaPrincipaisAutores = null;

		Query query4 = JPA.em().createNamedQuery("principaisLotacoes");
		query4.setMaxResults(5);
		List<Object[]> listaPrincipaisLotacoes = query4.getResultList();
		if (listaPrincipaisLotacoes.size() == 0)
			listaPrincipaisLotacoes = null;

		GcCloud cloud = new GcCloud(150.0, 60.0);
		Query query5 = JPA.em().createNamedQuery("principaisTags");
		query5.setMaxResults(50);
		List<Object[]> listaPrincipaisTags = query5.getResultList();
		if (listaPrincipaisTags.size() == 0)
			listaPrincipaisTags = null;
		else {
			for (Object[] t : listaPrincipaisTags) {
				cloud.criarCloud(t);
			}
		}
		GcGraficoEvolucao set = new GcGraficoEvolucao();
		Query query6 = JPA.em().createNamedQuery("evolucaoNovos");
		List<Object[]> listaNovos = query6.getResultList();
		for (Object[] novos : listaNovos) {
			set.add(new GcGraficoEvolucaoItem((Integer) novos[0],
					(Integer) novos[1], (Long) novos[2], 0, 0));
		}

		Query query7 = JPA.em().createNamedQuery("evolucaoVisitados");
		List<Object[]> listaVisitados = query7.getResultList();
		for (Object[] visitados : listaVisitados) {
			set.add(new GcGraficoEvolucaoItem((Integer) visitados[0],
					(Integer) visitados[1], 0, (Long) visitados[2], 0));
		}
		String evolucao = set.criarGrafico();

		render(listaMaisRecentes, listaMaisVisitados, listaPrincipaisAutores,
				listaPrincipaisLotacoes, listaPrincipaisTags, cloud, evolucao);
	}

	public static void estatisticaLotacao() {
		// List<GcInformacao> lista = GcInformacao.all().fetch();

		DpLotacao lotacao = lotaTitular();

		Query query1 = JPA.em().createNamedQuery("maisRecentesLotacao");
		// query1.setParameter("idLotacao", lotacao.getId());
		query1.setParameter("idlotacaoInicial", lotacao.getIdLotacaoIni());
		query1.setMaxResults(5);
		List<Object[]> listaMaisRecentes = query1.getResultList();
		if (listaMaisRecentes.size() == 0)
			listaMaisRecentes = null;

		Query query2 = JPA.em().createNamedQuery("maisVisitadosLotacao");
		query2.setParameter("idlotacaoInicial", lotacao.getIdLotacaoIni());
		query2.setMaxResults(5);
		List<Object[]> listaMaisVisitados = query2.getResultList();
		if (listaMaisVisitados.size() == 0)
			listaMaisVisitados = null;

		Query query3 = JPA.em().createNamedQuery("principaisAutoresLotacao");
		query3.setParameter("idlotacaoInicial", lotacao.getIdLotacaoIni());
		query3.setMaxResults(5);
		List<Object[]> listaPrincipaisAutores = query3.getResultList();
		if (listaPrincipaisAutores.size() == 0)
			listaPrincipaisAutores = null;

		GcCloud cloud = new GcCloud(150.0, 60.0);
		Query query4 = JPA.em().createNamedQuery("principaisTagsLotacao");
		query4.setParameter("idlotacaoInicial", lotacao.getIdLotacaoIni());
		query4.setMaxResults(50);
		List<Object[]> listaPrincipaisTags = query4.getResultList();
		if (listaPrincipaisTags.size() == 0)
			listaPrincipaisTags = null;
		else {
			for (Object[] t : listaPrincipaisTags) {
				cloud.criarCloud(t, lotacao.getId());
			}
		}
		GcGraficoEvolucao set = new GcGraficoEvolucao();
		Query query5 = JPA.em().createNamedQuery("evolucaoNovosLotacao");
		query5.setParameter("idlotacaoInicial", lotacao.getIdLotacaoIni());
		List<Object[]> listaNovos = query5.getResultList();
		for (Object[] novos : listaNovos) {
			set.add(new GcGraficoEvolucaoItem((Integer) novos[0],
					(Integer) novos[1], (Long) novos[2], 0, 0));
		}

		Query query6 = JPA.em().createNamedQuery("evolucaoVisitadosLotacao");
		query6.setParameter("idlotacaoInicial", lotacao.getIdLotacaoIni());
		List<Object[]> listaVisitados = query6.getResultList();
		for (Object[] visitados : listaVisitados) {
			set.add(new GcGraficoEvolucaoItem((Integer) visitados[0],
					(Integer) visitados[1], 0, (Long) visitados[2], 0));
		}
		String evolucao = set.criarGrafico();

		render(lotacao, listaMaisRecentes, listaMaisVisitados,
				listaPrincipaisAutores, listaPrincipaisTags, cloud, evolucao);
	}

	public static void listar(GcInformacaoFiltro filtro, int estatistica) {
		List<GcInformacao> lista;
		if (filtro.pesquisa)
			lista = filtro.buscar();
		else
			lista = new ArrayList<GcInformacao>();

		// Montando o filtro...
		// String[] tipos = new String[] { "Pessoa", "Lotação" };
		List<CpMarcador> marcadores = JPA.em()
				.createQuery("select distinct cpMarcador from GcMarca")
				.getResultList();

		List<CpOrgaoUsuario> orgaosusuarios = CpOrgaoUsuario.all().fetch();

		List<GcTipoInformacao> tiposinformacao = GcTipoInformacao.all().fetch();

		List<Integer> anos = new ArrayList<Integer>();
		int ano = GcBL.dt().getYear() + 1900;
		for (int i = 0; i < 10; i++) {
			anos.add(ano - i);
		}

		if (filtro == null)
			filtro = new GcInformacaoFiltro();

		render(lista, /* tipos, */marcadores, filtro, orgaosusuarios,
				tiposinformacao, anos, estatistica);

	}

	public static void navegar() {
		GcArvore arvore = new GcArvore();

		// List<GcInformacao> infs = GcInformacao.all().fetch();
		// não exibe conhecimentos cancelados
		List<GcInformacao> infs = GcInformacao.find("byHisDtFimIsNull").fetch();

		for (GcInformacao inf : infs) {
			for (GcTag tag : inf.tags) {
				arvore.add(tag, inf);
			}
		}

		arvore.build();

		render(arvore);
	}

	public static void buscar(String texto, String classificacao) {
		GcArvore arvore = new GcArvore();
		// List<GcInformacao> infs = GcInformacao.all().fetch();
		// não exibe conhecimentos cancelados
		List<GcInformacao> infs = GcInformacao.find("byHisDtFimIsNull").fetch();

		if (texto != null && texto.trim().length() > 0) {
			texto = texto.trim().toLowerCase();
			texto = texto.replace("  ", " ");
			String[] palavras = texto.split(" ");

			List<GcInformacao> infsFiltrada = new ArrayList<GcInformacao>();

			for (GcInformacao inf : infs) {
				if (inf.fts(palavras))
					infsFiltrada.add(inf);
			}

			infs = infsFiltrada;
		}

		for (GcInformacao inf : infs) {
			if (!inf.getTags().isEmpty()) {
				for (GcTag tag : inf.tags) {
					arvore.add(tag, inf);
				}
			} else {
				GcTag EmptyTag = new GcTag(null, "",
						"Conhecimentos_Sem_Classificacao");
				arvore.add(EmptyTag, inf);
			}
		}

		if (classificacao == null || classificacao.isEmpty()
				|| classificacao == "") {
			arvore.build();
		} else {
			arvore.build(classificacao);
		}
		// render(arvore);
		// render(arvore, texto);
		render(arvore, texto, classificacao);
	}

	/*
	 * public static void buscar(String texto) { GcArvore arvore = new
	 * GcArvore(); //List<GcInformacao> infs = GcInformacao.all().fetch();
	 * //não exibe conhecimentos cancelados List<GcInformacao> infs =
	 * GcInformacao.find("byHisDtFimIsNull").fetch();
	 * 
	 * if (texto != null && texto.trim().length() > 0) { texto =
	 * texto.trim().toLowerCase(); texto = texto.replace("  ", " "); String[]
	 * palavras = texto.split(" ");
	 * 
	 * List<GcInformacao> infsFiltrada = new ArrayList<GcInformacao>();
	 * 
	 * for (GcInformacao inf : infs) { if (inf.fts(palavras))
	 * infsFiltrada.add(inf); }
	 * 
	 * infs = infsFiltrada; }
	 * 
	 * for (GcInformacao inf : infs) { if(!inf.getTags().isEmpty()){ for (GcTag
	 * tag : inf.tags) { arvore.add(tag, inf); } } else{ GcTag EmptyTag = new
	 * GcTag(null, "", "Conhecimentos_Sem_Classificacao"); arvore.add(EmptyTag,
	 * inf); } }
	 * 
	 * arvore.build();
	 * 
	 * render(arvore, texto); }
	 */

	// public static void listar() {
	// List<GcInformacao> informacoes = GcInformacao.all().fetch();
	// render(informacoes);
	// }

	public static void exibir(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		DpPessoa titular = titular();
		DpLotacao lotaTitular = lotaTitular();
		CpIdentidade idc = idc();
		GcMovimentacao movNotificacao = informacao.podeTomarCiencia(titular,
				lotaTitular);

		if (informacao.acessoPermitido(titular, lotaTitular,
				informacao.visualizacao.id)
				|| informacao.podeRevisar(titular, lotaTitular)
				|| movNotificacao != null) {
			String conteudo = Util.marcarLinkNoConteudo(informacao.arq
					.getConteudoTXT());
			if (conteudo != null)
				informacao.arq.setConteudoTXT(conteudo);
			if (movNotificacao != null)
				GcBL.notificado(informacao, idc, titular, lotaTitular,
						movNotificacao);
			GcBL.logarVisita(informacao, idc, titular, lotaTitular);
			render(informacao);
		} else
			throw new AplicacaoException(
					"Restrição de Acesso ("
							+ informacao.visualizacao.nome
							+ ") : O usuário não tem permissão para visualizar o conhecimento solicitado.");
	}

	public static void editar(String sigla, String classificacao,
			String titulo, String origem, String conteudo, GcTipoInformacao tipo)
			throws Exception {
		GcInformacao informacao = null;
		DpPessoa titular = titular();
		DpLotacao lotaTitular = lotaTitular();

		// Edson: est� estranho referenciar o TMPGC-0. Ver solu��o melhor.
		if (sigla != null && !sigla.equals("TMPGC-0"))
			informacao = GcInformacao.findBySigla(sigla);
		else
			informacao = new GcInformacao();

		if (informacao.autor == null
				|| informacao.podeRevisar(titular, lotaTitular)
				|| informacao.acessoPermitido(titular, lotaTitular,
						informacao.edicao.id)) {
			List<GcTipoInformacao> tiposInformacao = GcTipoInformacao.all()
					.fetch();
			List<GcAcesso> acessos = GcAcesso.all().fetch();
			if (titulo == null)
				titulo = (informacao.arq != null) ? informacao.arq.titulo
						: null;

			if (conteudo == null)
				conteudo = (informacao.arq != null) ? informacao.arq
						.getConteudoTXT() : null;

			if (tipo == null || tipo.id == 0)
				tipo = (informacao.tipo != null) ? informacao.tipo
						: tiposInformacao.get(2);

			if (informacao.arq == null)
				conteudo = (tipo.arq != null) ? tipo.arq.getConteudoTXT()
						: null;

			if (conteudo != null && !conteudo.trim().startsWith("<")) {
				conteudo = Util.escapeHashTag(conteudo);
				informacao.arq.setConteudoTXT(conteudo);
				conteudo = informacao.getConteudoHTML();

			}

			if (classificacao == null || classificacao.isEmpty())
				classificacao = (informacao.arq != null) ? informacao.arq.classificacao
						: null;
			// inserir hashTag no conteudo quando um conhecimento for
			// relacionado
			else if (classificacao.contains("#") && conteudo == null) {
				conteudo = "";
				String[] listaClassificacao = classificacao.split(",");
				for (String somenteHashTag : listaClassificacao) {
					if (somenteHashTag.trim().startsWith("#"))
						conteudo += somenteHashTag.trim() + " ";
				}
			}
			if (informacao.autor == null) {
				informacao.autor = titular;
			}
			if (informacao.lotacao == null) {
				informacao.lotacao = lotaTitular;
			}

			render(informacao, tiposInformacao, acessos, titulo, conteudo,
					classificacao, origem, tipo);
		} else
			throw new AplicacaoException(
					"Restrição de Acesso ("
							+ informacao.edicao.nome
							+ ") : O usuário não tem permissão para editar o conhecimento solicitado.");
	}

	public static void historico(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);

		if (informacao.podeRevisar(titular(), lotaTitular())
				|| informacao.acessoPermitido(titular(), lotaTitular(),
						informacao.visualizacao.id)) {

			diff_match_patch diff = new diff_match_patch();

			String txtAnterior = "";
			String tituloAnterior = "";

			SortedSet<GcMovimentacao> list = new TreeSet<GcMovimentacao>();
			HashMap<GcMovimentacao, String> mapTitulo = new HashMap<GcMovimentacao, String>();
			HashMap<GcMovimentacao, String> mapTxt = new HashMap<GcMovimentacao, String>();
			if (informacao.movs != null) {
				GcMovimentacao[] array = informacao.movs
						.toArray(new GcMovimentacao[informacao.movs.size()]);
				ArrayUtils.reverse(array);
				for (GcMovimentacao mov : array) {
					Long t = mov.tipo.id;

					if (mov.isCancelada())
						continue;

					if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_EDICAO
							|| t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO) {
						// Titulo
						String titulo = mov.arq.titulo;
						LinkedList<Diff> tituloDiffs = diff.diff_main(
								tituloAnterior, titulo, true);
						String tituloDiffHtml = diff
								.diff_prettyHtml(tituloDiffs);
						boolean tituloAlterado = tituloDiffs == null
								|| tituloDiffs.size() != 1
								|| tituloDiffs.size() == 1
								&& tituloDiffs.get(0).operation != Operation.EQUAL;

						// Texto
						String txt = mov.arq.getConteudoTXT();
						LinkedList<Diff> txtDiffs = diff.diff_main(txtAnterior,
								txt, true);
						String txtDiffHtml = diff.diff_prettyHtml(txtDiffs);
						boolean txtAlterado = txtDiffs == null
								|| txtDiffs.size() != 1 || txtDiffs.size() == 1
								&& txtDiffs.get(0).operation != Operation.EQUAL;

						if (tituloAlterado || txtAlterado) {
							list.add(mov);
							if (tituloAlterado)
								mapTitulo.put(mov, tituloDiffHtml);
							if (txtAlterado)
								mapTxt.put(mov, txtDiffHtml);
						}
						txtAnterior = txt;
						tituloAnterior = titulo;
					}
				}
			}

			String conteudo = Util.marcarLinkNoConteudo(informacao.arq
					.getConteudoTXT());
			if (conteudo != null)
				informacao.arq.setConteudoTXT(conteudo);

			render(informacao, list, mapTitulo, mapTxt);
		} else
			throw new AplicacaoException(
					"Restrição de Acesso ("
							+ informacao.visualizacao.nome
							+ ") : O usuário não tem permissão para visualizar o conhecimento solicitado.");

	}

	public static void movimentacoes(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		if (informacao.podeRevisar(titular(), lotaTitular())
				|| informacao.acessoPermitido(titular(), lotaTitular(),
						informacao.visualizacao.id)) {
			String conteudo = Util.marcarLinkNoConteudo(informacao.arq
					.getConteudoTXT());
			if (conteudo != null)
				informacao.arq.setConteudoTXT(conteudo);
			render(informacao);
		} else
			throw new AplicacaoException(
					"Restrição de Acesso ("
							+ informacao.visualizacao.nome
							+ ") : O usuário não tem permissão para visualizar o conhecimento solicitado.");
	}

	public static void fechar(String sigla) throws Exception {
		GcInformacao inf = GcInformacao.findBySigla(sigla);
		if (inf.acessoPermitido(titular(), lotaTitular(), inf.edicao.id)) {
			GcBL.movimentar(inf,
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO, null,
					null, null, null, null, null, null, null, null);
			GcBL.gravar(inf, idc(), titular(), lotaTitular());
			exibir(inf.getSigla());
		} else
			throw new AplicacaoException(
					"Restrição de Acesso ("
							+ inf.edicao.nome
							+ ") : O usuário não tem permissão para finalizar o conhecimento solicitado.");
	}

	public static void duplicar(String sigla) throws Exception {
		GcInformacao infDuplicada = GcInformacao.findBySigla(sigla);

		GcMovimentacao movLocalizada = GcBL.movimentar(infDuplicada,
				GcTipoMovimentacao.TIPO_MOVIMENTACAO_DUPLICAR, null, null,
				null, null, null, null, null, null, null);
		GcBL.gravar(infDuplicada, idc(), titular(), lotaTitular());

		GcInformacao inf = new GcInformacao();
		GcArquivo arq = new GcArquivo();

		arq = infDuplicada.arq.duplicarConteudoInfo();
		inf.autor = titular();
		inf.lotacao = lotaTitular();
		inf.ou = inf.autor.getOrgaoUsuario();
		inf.tipo = GcTipoInformacao.findById(infDuplicada.tipo.id);
		inf.visualizacao = GcAcesso.findById(infDuplicada.visualizacao.id);
		inf.edicao = GcAcesso.findById(infDuplicada.edicao.id);

		GcMovimentacao movCriada = GcBL.movimentar(inf,
				GcTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO, null, null, null,
				arq.titulo, arq.getConteudoTXT(), arq.classificacao,
				movLocalizada, null, null);
		GcBL.gravar(inf, idc(), titular(), lotaTitular());

		movLocalizada.movRef = movCriada;
		GcBL.gravar(infDuplicada, idc(), titular(), lotaTitular());

		if (infDuplicada.isContemArquivos()) {
			for (GcMovimentacao movDuplicado : infDuplicada.movs) {
				if (movDuplicado.isCancelada())
					continue;
				if (movDuplicado.tipo.id == GcTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXAR_ARQUIVO
						&& movDuplicado.movCanceladora == null) {
					GcMovimentacao m = GcBL
							.movimentar(
									inf,
									movDuplicado.arq,
									GcTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXAR_ARQUIVO);
					m.movRef = movLocalizada;
					GcBL.gravar(inf, idc(), titular(), lotaTitular());
				}
			}
		}
		exibir(inf.getSigla());
	}

	public static void gravar(GcInformacao informacao, String titulo,
			String conteudo, String classificacao, String origem,
			GcTipoInformacao tipo) throws Exception {
		// DpPessoa pessoa = (DpPessoa) renderArgs.get("cadastrante");
		DpPessoa pessoa = titular();
		DpLotacao lotacao = lotaTitular();

		if (informacao.autor == null) {
			informacao.autor = pessoa;
			informacao.lotacao = lotacao;
		}
		if (informacao.ou == null) {
			if (informacao.autor != null)
				informacao.ou = informacao.autor.getOrgaoUsuario();
			else if (informacao.lotacao != null)
				informacao.ou = informacao.lotacao.getOrgaoUsuario();
			else if (pessoa != null)
				informacao.ou = pessoa.getOrgaoUsuario();
		}

		informacao.tipo = tipo;

		// Atualiza a classificação com as hashTags encontradas
		classificacao = Util.findHashTag(conteudo, classificacao,
				CONTROLE_HASH_TAG);

		if (informacao.id != 0)
			GcBL.movimentar(informacao,
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_EDICAO, null, null,
					null, titulo, conteudo, classificacao, null, null, null);
		else
			GcBL.movimentar(informacao,
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO, null, null,
					null, titulo, conteudo, classificacao, null, null, null);

		GcBL.gravar(informacao, idc(), titular(), lotaTitular());
		if (origem != null && origem.trim().length() != 0) {
			if (informacao.podeFinalizar(pessoa, lotacao)) {
				GcBL.movimentar(informacao,
						GcTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO, null,
						null, null, null, null, null, null, null, null);
				GcBL.gravar(informacao, idc(), pessoa, lotacao);
			}
			redirect(origem);
		} else
			exibir(informacao.getSigla());
	}

	public static void remover(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);

		if (informacao.elaboracaoFim != null)
			throw new AplicacaoException(
					"Não é permitido remover informações que já foram finalizadas.");
		JPA.em().createQuery("delete from GcMarca where inf.id = :id")
				.setParameter("id", informacao.id).executeUpdate();
		JPA.em().createQuery("delete from GcMovimentacao where inf.id = :id")
				.setParameter("id", informacao.id).executeUpdate();
		informacao.delete();
		index();
	}

	public static void notificar(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		render(informacao);
	}

	public static void notificarGravar(GcInformacao informacao, Long pessoa,
			Long lotacao, String email) throws Exception {
		if (pessoa != null || lotacao != null || email != null) {
			DpPessoa pesResponsavel = (DpPessoa) ((pessoa != null) ? DpPessoa
					.findById(pessoa) : null);
			DpLotacao lotResponsavel = (DpLotacao) ((lotacao != null) ? DpLotacao
					.findById(lotacao) : null);
			GcBL.movimentar(informacao,
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICAR,
					pesResponsavel, lotResponsavel, email, null, null, null,
					null, null, null);
			GcBL.gravar(informacao, idc(), titular(), lotaTitular());
			Correio.notificar(informacao, pesResponsavel, lotResponsavel, email);
			flash.success("Notificação realizada com sucesso!");
			exibir(informacao.getSigla());
		} else
			throw new AplicacaoException(
					"Para notificar é necessário selecionar uma Pessoa ou Lotação.");
	}

	public static void solicitarRevisao(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		render(informacao);
	}

	public static void solicitarRevisaoGravar(GcInformacao informacao,
			Long pessoa, Long lotacao) throws Exception {
		if (pessoa != null || lotacao != null) {
			DpPessoa pesResponsavel = (DpPessoa) ((pessoa != null) ? DpPessoa
					.findById(pessoa) : null);
			DpLotacao lotResponsavel = (DpLotacao) ((lotacao != null) ? DpLotacao
					.findById(lotacao) : null);
			GcBL.movimentar(informacao,
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_DE_REVISAO,
					pesResponsavel, lotResponsavel, null, null, null, null,
					null, null, null);
			GcBL.gravar(informacao, idc(), titular(), lotaTitular());
			flash.success("Solicitação de revisão realizada com sucesso!");
			exibir(informacao.getSigla());
		} else
			throw new AplicacaoException(
					"Para solicitar revisão é necessário selecionar uma Pessoa ou Lotação.");
	}

	public static void anexar(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		render(informacao);
	}

	public static void anexarGravar(GcInformacao informacao, String titulo,
			File fake) throws Exception {
		List<Upload> files = (List<Upload>) request.args.get("__UPLOADS");
		DpPessoa titular = titular();
		DpLotacao lotaTitular = lotaTitular();
		CpIdentidade idc = idc();
		if (files != null)
			for (Upload file : files) {
				if (file.getSize() > 2097152)
					throw new AplicacaoException(
							"O tamanho do arquivo � maior que o "
									+ "m�ximo permitido (2MB)");
				if (file.getSize() > 0) {
					/*
					 * ----Não pode ser usado porque o "plupload" retorna um
					 * mime type padrão "octet stream" String mimeType =
					 * file.getContentType().toLowerCase();
					 */
					byte anexo[] = file.asBytes();
					if (titulo == null || titulo.trim().length() == 0)
						titulo = file.getFileName();
					GcBL.movimentar(
							informacao,
							GcTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXAR_ARQUIVO,
							null, null, null, titulo, null, null, null, null,
							anexo);
					GcBL.gravar(informacao, idc, titular, lotaTitular);
					renderText("success");
				} else
					throw new AplicacaoException(
							"Não é permitido anexar se nenhum arquivo estiver selecionado. Favor selecionar arquivo.");
			}
	}

	public static void removerAnexo(String sigla, long idArq, long idMov)
			throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		GcMovimentacao mov = GcMovimentacao.findById(idMov);

		if (mov.arq.id == idArq)
			GcBL.cancelarMovimentacao(informacao, mov, idc(), titular(),
					lotaTitular());
		/*
		 * for (GcMovimentacao mov : informacao.movs) { if (mov.isCancelada())
		 * continue; if (mov.tipo.id ==
		 * GcTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXAR_ARQUIVO && mov.arq.id ==
		 * id) { movLocalizada = mov; break; } } GcMovimentacao m =
		 * GcBL.movimentar(informacao,
		 * GcTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO,
		 * null, null, null, null, null, movLocalizada, null, null, null);
		 * movLocalizada.movCanceladora = m; GcBL.gravar(informacao, idc(),
		 * titular(), lotaTitular());
		 */
		render(informacao);
	}

	public static void baixar(Long id) {
		GcArquivo arq = GcArquivo.findById(id);
		if (arq != null)
			renderBinary(new ByteArrayInputStream(arq.conteudo), arq.titulo,
					(long) arq.conteudo.length, arq.mimeType, true);
	}

	public static void revisado(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		if (informacao.movs != null) {
			DpPessoa titular = titular();
			DpLotacao lotaTitular = lotaTitular();
			for (GcMovimentacao mov : informacao.movs) {
				if (mov.isCancelada())
					continue;
				if (mov.tipo.id == GcTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_DE_REVISAO
						&& (titular.equivale(mov.pessoaAtendente) || lotaTitular
								.equivale(mov.lotacaoAtendente))) {
					GcMovimentacao m = GcBL
							.movimentar(
									informacao,
									GcTipoMovimentacao.TIPO_MOVIMENTACAO_REVISADO,
									null, null, null, null, null, null, mov,
									null, null);
					mov.movCanceladora = m;
					GcBL.gravar(informacao, idc(), titular, lotaTitular);
					flash.success("Conhecimento revisado com sucesso!");
					if (informacao.acessoPermitido(titular, lotaTitular,
							informacao.visualizacao.id))
						exibir(sigla);
					else {
						// buscar(null);
						buscar(null, null);
					}

				}
			}
		}
		throw new AplicacaoException(
				"Não há pedido de revisão pendente para "
						+ idc().getDpPessoa().getSigla());
	}

	public static void marcarComoInteressado(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		GcBL.interessado(informacao, idc(), titular(), lotaTitular(), true);
		exibir(sigla);
	}

	public static void desmarcarComoInteressado(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		GcBL.interessado(informacao, idc(), titular(), lotaTitular(), false);
		exibir(sigla);
	}

	public static void cancelar(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		GcBL.cancelar(informacao, idc(), titular(), lotaTitular());
		exibir(sigla);
	}

	public static void selecionarTag(String sigla) throws Exception {
		GcTag sel = (GcTag) new GcTag().selecionar(sigla);
		render("@siga-play-module.selecionar", sel);
	}

	public static void buscarTag(String sigla, GcTag filtro) {
		List<GcTag> itens = null;

		Query query = JPA.em().createNamedQuery("listarTagCategorias");
		List<Object[]> listaTagCategorias = query.getResultList();
		if (listaTagCategorias.size() == 0)
			listaTagCategorias = null;

		try {
			if (filtro == null)
				filtro = new GcTag();
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			itens = (List<GcTag>) filtro.buscar();
		} catch (Exception e) {
			itens = new ArrayList<GcTag>();
		}

		render(itens, filtro, listaTagCategorias);
	}

	public static void dadosRI(Long ultimaAtualizacao, Long desempate)
			throws UnsupportedEncodingException {
		Date dtUltimaAtualizacao = new Date(0L);
		Long idDesempate = 0L;
		if (ultimaAtualizacao != null)
			dtUltimaAtualizacao = new Date(ultimaAtualizacao);
		if (desempate != null)
			idDesempate = desempate;

		Query query = JPA.em().createNamedQuery(
				"dadosParaRecuperacaoDeInformacao");
		query.setParameter("dt", dtUltimaAtualizacao, TemporalType.TIMESTAMP);
		query.setParameter("desempate", idDesempate);
		query.setMaxResults(10);
		List<Object[]> lista = query.getResultList();
		if (lista.size() == 0)
			renderJSON("[]");

		List<DadosRI> resultado = new ArrayList<DadosRI>();
		for (Object[] ao : lista) {
			GcInformacao i = (GcInformacao) ao[0];
			GcArquivo a = (GcArquivo) ao[1];
			Date dt = (Date) ao[2];
			long idMov = (Long) ao[3];
			boolean ativo = !((Long) ao[4]).equals(3L);

			DadosRI dri = new DadosRI();
			dri.uri = "/sigagc/app/exibir?sigla=" + i.getSigla();
			dri.sigla = i.getSigla();
			dri.titulo = a.titulo;
			dri.ultimaAtualizacao = dt;
			dri.idDesempate = idMov;
			dri.ativo = ativo;
			if (ativo) {
				dri.conteudo = new String(a.conteudo, "utf-8");
			}
			resultado.add(dri);
		}
		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z").create();
		renderJSON(gson.toJson(resultado));
	}

	public static void proxy(String url) throws Exception {
		URL u = new URL(getBaseSiga() + url);
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

	public static void selecionarSiga(String sigla, String tipo, String nome)
			throws Exception {
		proxy("/" + tipo + "/selecionar.action?" + "propriedade=" + tipo + nome
				+ "&sigla=" + URLEncoder.encode(sigla, "UTF-8"));
	}

	public static void buscarSiga(String sigla, String tipo, String nome)
			throws Exception {
		proxy("/" + tipo + "/buscar.action?" + "propriedade=" + tipo + nome
				+ "&sigla=" + URLEncoder.encode(sigla, "UTF-8"));
	}

	public static void buscarSigaFromPopup(String tipo) throws Exception {
		String paramString = "?";
		for (String s : request.params.all().keySet())
			if (!s.equals("body"))
				paramString += s + "="
						+ URLEncoder.encode(request.params.get(s), "UTF-8")
						+ "&";
		proxy("/" + tipo + "/buscar.action" + paramString);
	}

	protected static void assertAcesso(String path) throws Exception {
		SigaApplication.assertAcesso("GC:Módulo de Gestão de Conhecimento"
				+ path);
	}

	public static void erro(String message, String stackTrace) {
		render(message, stackTrace);
	}

	@Catch(value = Throwable.class, priority = 1)
	public static void catchError(Throwable throwable) {
		if (!(throwable instanceof AplicacaoException)) {
			if (Play.mode.isDev())
				return;
		}

		// Flash.current().clear();
		// Flash.current().put("_cabecalho_pre",
		// renderArgs.get("_cabecalho_pre"));
		// Flash.current().put("_cabecalho_pos",
		// renderArgs.get("_cabecalho_pos"));
		// Flash.current().put("_rodape", renderArgs.get("_rodape"));
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

	public static void desfazer(String sigla, long movId) throws Exception {
		GcInformacao info = GcInformacao.findBySigla(sigla);
		GcMovimentacao mov = GcMovimentacao.findById(movId);

		GcBL.cancelarMovimentacao(info, mov, idc(), titular(), lotaTitular());
		movimentacoes(sigla);
	}

}