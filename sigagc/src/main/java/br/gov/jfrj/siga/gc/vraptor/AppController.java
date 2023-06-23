package br.gov.jfrj.siga.gc.vraptor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.download.ByteArrayDownload;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.view.HttpResult;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.model.CpPerfilSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.gc.model.GcAcesso;
import br.gov.jfrj.siga.gc.model.GcArquivo;
import br.gov.jfrj.siga.gc.model.GcInformacao;
import br.gov.jfrj.siga.gc.model.GcMovimentacao;
import br.gov.jfrj.siga.gc.model.GcPapel;
import br.gov.jfrj.siga.gc.model.GcTag;
import br.gov.jfrj.siga.gc.model.GcTipoInformacao;
import br.gov.jfrj.siga.gc.model.GcTipoMovimentacao;
import br.gov.jfrj.siga.gc.util.GcArvore;
import br.gov.jfrj.siga.gc.util.GcBL;
import br.gov.jfrj.siga.gc.util.GcCloud;
import br.gov.jfrj.siga.gc.util.GcGraficoEvolucao;
import br.gov.jfrj.siga.gc.util.GcGraficoEvolucaoItem;
import br.gov.jfrj.siga.gc.util.GcInformacaoFiltro;
import br.gov.jfrj.siga.gc.util.GcLabelValue;
import br.gov.jfrj.siga.gc.util.diff_match_patch;
import br.gov.jfrj.siga.gc.util.diff_match_patch.Diff;
import br.gov.jfrj.siga.gc.util.diff_match_patch.Operation;
import br.gov.jfrj.siga.model.DadosRI;
import br.gov.jfrj.siga.vraptor.RequestParamsPermissiveCheck;
import br.gov.jfrj.siga.vraptor.SigaIdDescr;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.vraptor.Transacional;

@Controller
public class AppController extends GcController {

	private GcBL bl;
	private Correio correio;

	/**
	 * @deprecated CDI eyes only
	 */
	public AppController() {
		super();
		this.bl = null;
		this.correio = null;
	}

	@Inject
	public AppController(HttpServletRequest request, CpDao dao, Result result, GcBL bl, SigaObjects so,
			EntityManager em, Correio correio) {
		super(request, dao, result, so, em);
		this.bl = bl;
		this.correio = correio;
	}

	private static final String CATEGORIA_OUTRAS = "[Outros]";
	private static final String HTTP_LOCALHOST_8080 = "http://localhost:8080";
	private static final int CONTROLE_HASH_TAG = 1;

	@Path("/app/gadget")
	public void gadget() {
		Query query = em().createNamedQuery("contarGcMarcas");
		query.setParameter("idPessoaIni", getCadastrante().getIdInicial());
		query.setParameter("idLotacaoIni", getLotaTitular().getIdInicial());
		query.setParameter("dbDatetime", dao().consultarDataEHoraDoServidor());
		List contagens = query.getResultList();
		result.include("contagens", contagens);
	}

	private static List<GcLabelValue> lCachePontoDeEntrada = null;
	private static Date dtCachePontoDeEntrada = null;

	public void pontoDeEntrada(String texto) {
		pontosDeEntradaAtualizarCache();

		List<GcLabelValue> l = new ArrayList<GcLabelValue>();

		if (texto != null && texto.trim().length() > 0) {
			texto = GcLabelValue.removeAcento(texto).trim().toLowerCase();
			texto = texto.replace("  ", " ");
			String[] palavras = texto.split(" ");
			for (GcLabelValue lv : lCachePontoDeEntrada) {
				if (lv.fts(palavras))
					l.add(lv);
			}
		}

		if (l.size() == 0) {
			result.use(Results.http()).body("[]");
			return;
		}

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z").create();
		result.use(Results.http()).body(gson.toJson(l));
	}

	public void pontosDeEntrada(String texto) {
		pontosDeEntradaAtualizarCache();

		Map<String, SortedSet<GcLabelValue>> map = new TreeMap<String, SortedSet<GcLabelValue>>();
		for (GcLabelValue lv : lCachePontoDeEntrada) {
			String[] a = lv.getLabel().split(": ");
			if (a.length == 2) {
				if (!map.containsKey(a[0]))
					map.put(a[0], new TreeSet<GcLabelValue>());
				map.get(a[0]).add(lv);
			} else if (a.length == 1) {
				if (!map.containsKey(CATEGORIA_OUTRAS))
					map.put(CATEGORIA_OUTRAS, new TreeSet<GcLabelValue>());
				map.get(CATEGORIA_OUTRAS).add(lv);
			}
		}
		result.include("map", map);
	}

	private void pontosDeEntradaAtualizarCache() {
		if (lCachePontoDeEntrada == null || dtCachePontoDeEntrada == null || dtCachePontoDeEntrada.before(new Date())) {
			synchronized (GcLabelValue.class) {
				if (lCachePontoDeEntrada == null || dtCachePontoDeEntrada == null
						|| dtCachePontoDeEntrada.before(new Date())) {
					lCachePontoDeEntrada = new ArrayList<GcLabelValue>();
					Query q = em().createNamedQuery("pontosDeEntrada");
					q.setParameter("texto", "%");
					List<Object[]> lista = q.getResultList();
					for (Object[] ao : lista) {
						GcInformacao i = (GcInformacao) ao[0];
						GcArquivo a = (GcArquivo) ao[1];
						lCachePontoDeEntrada.add(new GcLabelValue(a.getTitulo(), i.getSigla()));
					}
					dtCachePontoDeEntrada = new Date(new Date().getTime() + 60000);
				}
			}
		}
	}

	@Path("/public/app/knowledge")
	public void publicKnowledge(Long id, String[] tags, String label, String msgvazio, String urlvazio, String titulo,
			boolean popup, String estiloBusca, Boolean podeCriar, String pagina) throws Exception {
		renderKnowledge(id, tags, label, null, msgvazio, urlvazio, titulo, true, popup, estiloBusca, podeCriar, pagina);
	}

	public void knowledge(Long id, String[] tags, String label, String msgvazio, String urlvazio, String titulo,
			boolean testarAcesso, boolean popup, String estiloBusca, Boolean podeCriar, String pagina)
			throws Exception {
		renderKnowledge(id, tags, label, null, msgvazio, urlvazio, titulo, testarAcesso, popup, estiloBusca, podeCriar,
				pagina);
	}

	@RequestParamsPermissiveCheck
	@Path("/app/knowledgeInplace")
	public void knowledgeInplace(Long id, String[] tags, String label, String msgvazio, String urlvazio, String titulo,
			boolean testarAcesso, boolean popup, String estiloBusca, Boolean podeCriar, String pagina)
			throws Exception {
		renderKnowledge(id, tags, label, "inplace", msgvazio, urlvazio, titulo, testarAcesso, popup, estiloBusca,
				podeCriar, pagina);
	}

	@RequestParamsPermissiveCheck
	@Path("/app/knowledgeInplaceMinimal")
	public void knowledgeInplaceMinimal(Long id, String[] tags, String label, String msgvazio, String urlvazio,
			String titulo, boolean testarAcesso, boolean popup, String estiloBusca, Boolean podeCriar, String pagina)
			throws Exception {
		renderKnowledge(id, tags, label, "inplace", msgvazio, urlvazio, titulo, testarAcesso, popup, estiloBusca,
				podeCriar, pagina);
	}

	public void knowledgeSidebar(Long id, String[] tags, String label, String msgvazio, String urlvazio, String titulo,
			boolean testarAcesso, boolean popup, String estiloBusca, Boolean podeCriar, String pagina)
			throws Exception {
		renderKnowledge(id, tags, label, "sidebar", msgvazio, urlvazio, titulo, testarAcesso, popup, estiloBusca,
				podeCriar, pagina);
	}

	public void knowledgeSidebarSr(Long id, String[] tags, String label, String msgvazio, String urlvazio,
			String titulo, boolean testarAcesso, boolean popup, String estiloBusca, Boolean podeCriar, String pagina)
			throws Exception {
		renderKnowledge(id, tags, label, "sidebar", msgvazio, urlvazio, titulo, testarAcesso, popup, estiloBusca,
				podeCriar, pagina);
	}

	private void renderKnowledge(Long id, String[] tags, String label, String estilo, String msgvazio, String urlvazio,
			String titulo, boolean testarAcesso, boolean popup, String estiloBusca, Boolean podeCriar, String pagina)
			throws UnsupportedEncodingException, Exception {
		if (label == null || label.isEmpty()) {
			if ("inplace".equals(estilo))
				label = "Conhecimento Específico";
		}

		int index = Integer.MAX_VALUE;
		Long idOutroConhecimento = 0l;
		GcInformacao info = null;
		Set<GcTag> set = null;
		if (tags != null)
			set = bl.buscarTags(tags, true);
		if ("inplace".equals(estilo) && estiloBusca == null)
			estiloBusca = "ExatoOuNada";
		estiloBusca = estiloBusca != null ? estiloBusca.substring(0, 1).toUpperCase() + estiloBusca.substring(1) : "";
		List<Object[]> conhecimentos = new ArrayList<Object[]>();
		if (set != null) {
			Query query = em().createNamedQuery("buscarConhecimento" + estiloBusca);
			query.setParameter("tags", set);
			if (estiloBusca == null || estiloBusca.isEmpty() || estiloBusca.equals("AlgumIgualNenhumDiferente")) {
				query.setParameter("lotacaoIni", getLotaTitular().getIdInicial());
				query.setParameter("pessoaIni", getTitular().getIdInicial());
			}
			if ("ExatoOuNada".equals(estiloBusca))
				query.setParameter("numeroDeTags", (long) tags.length);

			List<Object[]> conhecimentosCandidatos = query.getResultList();
			for (Object[] o : conhecimentosCandidatos) {
				idOutroConhecimento = Long.parseLong(o[0].toString());
				if (idOutroConhecimento.equals(id))
					continue;

				// Nato: Como a busca ExatoOuNada não estava funcionando bem, inclui
				// esse IF
				if ("ExatoOuNada".equals(estiloBusca)) {
					if (tags.length != (long) o[3])
						continue;

					if (info == null)
						info = GcInformacao.AR.findById(idOutroConhecimento);

					HashSet<String> infoTags = new HashSet<String>();
					for (GcTag t : info.getTags())
						infoTags.add(t.toString());
					for (String s : tags)
						infoTags.remove(s);
					if (infoTags.size() != 0)
						continue;
				}

				if (testarAcesso) {
					if (info == null)
						info = GcInformacao.AR.findById(idOutroConhecimento);
					if (!info.acessoPermitido(getTitular(), getLotaTitular(), info.getVisualizacao().getId()))
						continue;
				}
						

				// o[3] = URLEncoder.encode(info.getSigla(), "UTF-8");
				if (info == null)
					info = GcInformacao.AR.findById(idOutroConhecimento);
				// Nato: O ideal seria não ter que carregar a info só para gerar a ID
				o[3] = info.getSiglaCompacta();
				if (o[2] != null && o[2] instanceof byte[]) {
					String s = new String((byte[]) o[2], Charset.forName("utf-8"));
					s = bl.ellipsize(s, 100);
					o[2] = s;
				}
				conhecimentos.add(o);
			}
		}
		if (conhecimentos.size() == 1 && "inplace".equals(estilo)) {
			GcInformacao inf = GcInformacao.AR.findById(conhecimentos.get(0)[0]);
			conhecimentos.get(0)[1] = inf.getArq().getTitulo();
			conhecimentos.get(0)[2] = inf.getConteudoHTML();
		}

		if (conhecimentos.size() == 0)
			conhecimentos = null;

		String referer = null;
		try {
			referer = getRequest().getHeader("referer");
		} catch (Exception e) {

		}

		StringBuilder classificacao = new StringBuilder();
		if (tags != null && tags.length > 0) {
			for (String s : tags) {
				if (classificacao.length() > 0)
					classificacao.append(", ");
				classificacao.append(URLEncoder.encode(s, "UTF-8"));
			}
		}
		// Necessário para criar um novo conhecimento a partir de um já
		// existente, a classificação
		// é passada como queryString. Sem fazer isso, as hashTags não são
		// passadas.
		// classificacao = URLEncoder.encode(classificacao, "UTF-8");
		// if (msgvazio != null) {
		// msgvazio = msgvazio.replace("*aqui*", "<a href=\"" + urlvazio +
		// "\">aqui</a>");
		// }

		if (podeCriar == null)
			podeCriar = true;

		result.include("conhecimentos", conhecimentos);
		result.include("classificacao", classificacao.toString());
		result.include("msgvazio", msgvazio);
		result.include("urlvazio", urlvazio);
		result.include("titulo", titulo);
		result.include("referer", referer);
		result.include("popup", popup);
		result.include("podeCriar", podeCriar);
		result.include("pagina", pagina);
		result.include("label", label);
	}

	@Transacional
	public void updateTag(String before, String after) {

		// Edson: Atualizando tags de classificacao:
		em().createQuery("update GcTag set titulo = '" + after + "' where titulo = '" + before + "'").executeUpdate();

		// Edson: Atualizando tags de ancora. O problema aqui eh que,
		// muitas vezes, o before aparece em tags ancora acompanhado de
		// outra classificacao. Por exemplo, cadeira-consertar, onde
		// before eh consertar. Os patterns abaixo funcionam, mas nao
		// sempre. Por exemplo, se houver uma tag cadeira-tentar-consertar
		// alem da cadeira-consertar, ela vai ser erroneamente atualizada.
		List<GcTag> tags = em().createQuery("from GcTag where titulo like '%" + before + "%' and tipo.id = 3")
				.getResultList();
		for (GcTag t : tags) {
			t.setTitulo(t.getTitulo().replaceAll("^" + before + "(-.+|$)", after + "$1"));
			t.setTitulo(t.getTitulo().replaceAll("(.+-|^)" + before + "$", "$1" + after));
			t.save();
		}

		// Edson: Atualizando os arquivos:
		List<GcArquivo> arqs = em().createQuery("select arq from GcInformacao inf inner join inf.arq arq"
				+ " where inf.hisDtFim is null and arq.classificacao like '%" + before + "%'").getResultList();
		for (GcArquivo arq : arqs) {
			if (arq.getClassificacao().startsWith("^")) {
				arq.setClassificacao(arq.getClassificacao().replace(":" + before, ":" + after));
				arq.setClassificacao(arq.getClassificacao().replaceAll("-" + before + "$", "-" + after));
			} else {
				arq.setClassificacao(arq.getClassificacao().replaceAll("(@" + before + ")(,|$)", "@" + after + "$2"));
			}
			arq.save();
		}

		result.use(Results.http()).body("OK");
	}

	public void index() throws Exception {
		result.redirectTo(this).estatisticaGeral();
	}

	@Get("/app/estatisticaGeral")
	@Transacional
	public void estatisticaGeral() throws Exception {
		// List<GcInformacao> lista = GcInformacao.all().fetch();

		Query query1 = em().createNamedQuery("maisRecentes");
		query1.setMaxResults(5);
		List<Object[]> listaMaisRecentes = query1.getResultList();
		if (listaMaisRecentes.size() == 0)
			listaMaisRecentes = null;

		Query query2 = em().createNamedQuery("maisVisitados");
		query2.setMaxResults(5);
		List<Object[]> listaMaisVisitados = query2.getResultList();
		if (listaMaisVisitados.size() == 0)
			listaMaisVisitados = null;

		Query query3 = em().createNamedQuery("principaisAutores");
		query3.setMaxResults(5);
		List<Object[]> listaPrincipaisAutores = query3.getResultList();
		if (listaPrincipaisAutores.size() == 0)
			listaPrincipaisAutores = null;

		Query query4 = em().createNamedQuery("principaisLotacoes");
		query4.setMaxResults(5);
		List<Object[]> listaPrincipaisLotacoes = query4.getResultList();
		if (listaPrincipaisLotacoes.size() == 0)
			listaPrincipaisLotacoes = null;

		GcCloud cloud = new GcCloud(150.0, 60.0);
		Query query5 = em().createNamedQuery("principaisTags");
		query5.setMaxResults(50);
		List<Object[]> listaPrincipaisTags = query5.getResultList();
		if (listaPrincipaisTags.size() == 0)
			listaPrincipaisTags = null;
		else {
			for (Object[] t : listaPrincipaisTags) {
				cloud.criarCloud(t, null);
			}
		}
		GcGraficoEvolucao set = new GcGraficoEvolucao();
		Query query6 = em().createNamedQuery("evolucaoNovos");
		List<Object[]> listaNovos = query6.getResultList();
		for (Object[] novos : listaNovos) {
			set.add(new GcGraficoEvolucaoItem((Integer) novos[0], (Integer) novos[1], (Long) novos[2], 0, 0));
		}

		Query query7 = em().createNamedQuery("evolucaoVisitados");
		List<Object[]> listaVisitados = query7.getResultList();
		for (Object[] visitados : listaVisitados) {
			set.add(new GcGraficoEvolucaoItem((Integer) visitados[0], (Integer) visitados[1], 0, (Long) visitados[2],
					0));
		}
		String evolucao = set.criarGrafico();
		result.include("listaMaisRecentes", listaMaisRecentes);
		result.include("listaMaisVisitados", listaMaisVisitados);
		result.include("listaPrincipaisAutores", listaPrincipaisAutores);
		result.include("listaPrincipaisLotacoes", listaPrincipaisLotacoes);
		result.include("listaPrincipaisTags", listaPrincipaisTags);
		result.include("cloud", cloud);
		result.include("evolucao", evolucao);
		result.use(Results.page()).defaultView();
	}

	@Transacional
	public void estatisticaLotacao() throws Exception {

		DpLotacao lotacao = getLotaTitular();

		Query query1 = em().createNamedQuery("maisRecentesLotacao");
		query1.setParameter("idlotacaoInicial", lotacao.getIdLotacaoIni());
		query1.setMaxResults(5);
		List<Object[]> listaMaisRecentes = query1.getResultList();
		if (listaMaisRecentes.isEmpty())
			listaMaisRecentes = null;

		Query query2 = em().createNamedQuery("maisVisitadosLotacao");
		query2.setParameter("idlotacaoInicial", lotacao.getIdLotacaoIni());
		query2.setMaxResults(5);
		List<Object[]> listaMaisVisitados = query2.getResultList();
		if (listaMaisVisitados.isEmpty())
			listaMaisVisitados = null;

		Query query3 = em().createNamedQuery("principaisAutoresLotacao");
		query3.setParameter("idlotacaoInicial", lotacao.getIdLotacaoIni());
		query3.setMaxResults(5);
		List<Object[]> listaPrincipaisAutores = query3.getResultList();
		if (listaPrincipaisAutores.isEmpty())
			listaPrincipaisAutores = null;

		GcCloud cloud = new GcCloud(150.0, 60.0);
		Query query4 = em().createNamedQuery("principaisTagsLotacao");
		query4.setParameter("idlotacaoInicial", lotacao.getIdLotacaoIni());
		query4.setMaxResults(50);
		List<Object[]> listaPrincipaisTags = query4.getResultList();
		if (listaPrincipaisTags.isEmpty())
			listaPrincipaisTags = null;
		else {
			for (Object[] t : listaPrincipaisTags) {
				cloud.criarCloud(t, lotacao.getId());
			}
		}
		GcGraficoEvolucao set = new GcGraficoEvolucao();
		Query query5 = em().createNamedQuery("evolucaoNovosLotacao");
		query5.setParameter("idlotacaoInicial", lotacao.getIdLotacaoIni());
		List<Object[]> listaNovos = query5.getResultList();
		for (Object[] novos : listaNovos) {
			set.add(new GcGraficoEvolucaoItem((Integer) novos[0], (Integer) novos[1], (Long) novos[2], 0, 0));
		}

		Query query6 = em().createNamedQuery("evolucaoVisitadosLotacao");
		query6.setParameter("idlotacaoInicial", lotacao.getIdLotacaoIni());
		List<Object[]> listaVisitados = query6.getResultList();
		for (Object[] visitados : listaVisitados) {
			set.add(new GcGraficoEvolucaoItem((Integer) visitados[0], (Integer) visitados[1], 0, (Long) visitados[2],
					0));
		}
		String evolucao = set.criarGrafico();

		result.include("lotacao", lotacao);
		result.include("listaMaisRecentes", listaMaisRecentes);
		result.include("listaMaisVisitados", listaMaisVisitados);
		result.include("listaPrincipaisAutores", listaPrincipaisAutores);
		result.include("listaPrincipaisTags", listaPrincipaisTags);
		result.include("cloud", cloud);
		result.include("evolucao", evolucao);
		result.use(Results.page()).defaultView();
	}

	@Path({ "/app/listar", "/app/informacao/buscar" })
	public void listar(GcInformacaoFiltro filtro, int estatistica, boolean popup, String propriedade, String sigla)
			throws Exception {
		List<GcInformacao> lista;

		if (sigla != null) {
			// Edson: se a sigla for na verdade um texto, seta como título
			try {
				GcInformacao.findBySigla(sigla);
			} catch (NumberFormatException nfe) {
				filtro.setTitulo(sigla);
				filtro.pesquisa = true;
			} catch (AplicacaoException ae) {

			}
		}

		if (filtro.pesquisa)
			lista = filtro.buscar();
		else
			lista = new ArrayList<GcInformacao>();

		// Montando o filtro...
		// String[] tipos = new String[] { "Pessoa", "LotaÃ§ão" };
		List<CpMarcador> marcadores = em().createQuery("select distinct cpMarcador from GcMarca").getResultList();
		em().createQuery("select cp from CpOrgaoUsuario cp").getResultList();
		List<CpOrgaoUsuario> orgaosusuarios = CpOrgaoUsuario.AR.all().fetch();

		List<GcTipoInformacao> tiposinformacao = GcTipoInformacao.AR.all().fetch();
		if (filtro.lotacao != null) {
			DpLotacao lotacaoIni = DpLotacao.AR.findById(filtro.lotacao.getIdLotacaoIni());
			filtro.getLotacao().getLotacaoInicial().setLotacoesPosteriores(lotacaoIni.getLotacoesPosteriores());
		}

		List<SigaIdDescr> anos = new ArrayList<SigaIdDescr>();
		int ano = bl.dt().getYear() + 1900;
		for (int i = 0; i < 10; i++) {
			anos.add(new SigaIdDescr(ano - i, ano - i));
		}

		if (filtro == null)
			filtro = new GcInformacaoFiltro();

		result.include("lista", lista);
		result.include("marcadores", marcadores);
		result.include("filtro", filtro);
		result.include("orgaosusuarios", orgaosusuarios);
		result.include("tiposinformacao", tiposinformacao);
		result.include("anos", anos);
		result.include("estatistica", estatistica);
		result.include("propriedade", propriedade);
		result.include("popup", popup);
	}

	public void navegar() {
		GcArvore arvore = new GcArvore();

		// List<GcInformacao> infs = GcInformacao.all().fetch();
		// não exibe conhecimentos cancelados
		// BJN: evitando o erro "Error while executing query from GcInformacao where
		// hisDtFim is null: Unable to find br.gov.jfrj.siga.cp.CpPerfil with id 2471"
		List<GcInformacao> infs = new ArrayList<GcInformacao>();
		try {
			infs = GcInformacao.AR.find("byHisDtFimIsNull").fetch();
		} catch (Exception e) {
		}

		for (GcInformacao inf : infs) {
			for (GcTag tag : inf.getTags()) {
				arvore.add(tag, inf);
			}
		}

		arvore.build();

		result.include("arvore", arvore);
	}

	public void buscar(String texto, String classificacao) {
		GcArvore arvore = new GcArvore();
		// List<GcInformacao> infs = GcInformacao.all().fetch();
		// não exibe conhecimentos cancelados
		// BJN: evitando o erro "Error while executing query from GcInformacao where
		// hisDtFim is null: Unable to find br.gov.jfrj.siga.cp.CpPerfil with id 2471"
		List<GcInformacao> infs = new ArrayList<GcInformacao>();
		try {
			infs = GcInformacao.AR.find("byHisDtFimIsNull").fetch();
		} catch (Exception e) {
		}

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
				for (GcTag tag : inf.getTags()) {
					arvore.add(tag, inf);
				}
			} else {
				GcTag EmptyTag = new GcTag(null, "", "Conhecimentos_Sem_Classificacao");
				arvore.add(EmptyTag, inf);
			}
		}

		if (classificacao == null || classificacao.isEmpty() || classificacao == "") {
			arvore.build();
		} else {
			arvore.build(classificacao);
		}
		// render(arvore);
		// render(arvore, texto);
		result.include("arvore", arvore);
		result.include("texto", texto);
		result.include("classificacao", classificacao);
	}

	/*
	 * public void buscar(String texto) { GcArvore arvore = new GcArvore();
	 * //List<GcInformacao> infs = GcInformacao.all().fetch(); //não exibe
	 * conhecimentos cancelados List<GcInformacao> infs =
	 * GcInformacao.find("byHisDtFimIsNull").fetch();
	 * 
	 * if (texto != null && texto.trim().length() > 0) { texto =
	 * texto.trim().toLowerCase(); texto = texto.replace("  ", " "); String[]
	 * palavras = texto.split(" ");
	 * 
	 * List<GcInformacao> infsFiltrada = new ArrayList<GcInformacao>();
	 * 
	 * for (GcInformacao inf : infs) { if (inf.fts(palavras)) infsFiltrada.add(inf);
	 * }
	 * 
	 * infs = infsFiltrada; }
	 * 
	 * for (GcInformacao inf : infs) { if(!inf.getTags().isEmpty()){ for (GcTag tag
	 * : inf.tags) { arvore.add(tag, inf); } } else{ GcTag EmptyTag = new
	 * GcTag(null, "", "Conhecimentos_Sem_Classificacao"); arvore.add(EmptyTag,
	 * inf); } }
	 * 
	 * arvore.build();
	 * 
	 * render(arvore, texto); }
	 */

	// public void listar() {
	// List<GcInformacao> informacoes = GcInformacao.all().fetch();
	// render(informacoes);
	// }

	/*
	 * public static void exibirPontoDeEntrada(String sigla) throws Exception {
	 * GcInformacao informacao = GcInformacao.findBySigla(sigla); DpPessoa titular =
	 * titular(); DpLotacao lotaTitular = lotaTitular(); CpIdentidade idc = idc();
	 * 
	 * if (informacao.acessoPermitido(titular, lotaTitular,
	 * informacao.visualizacao.id)) { String conteudo =
	 * Util.marcarLinkNoConteudo(informacao.arq .getConteudoTXT()); if (conteudo !=
	 * null) informacao.arq.setConteudoTXT(conteudo); GcBL.logarVisita(informacao,
	 * idc, titular, lotaTitular); render(informacao); } else throw new
	 * AplicacaoException( "RestriÃ§Ã£o de Acesso (" + informacao.visualizacao.nome
	 * +
	 * ") : O usuÃ¡rio nÃ£o tem permissÃ£o para visualizar o conhecimento solicitado."
	 * ); }
	 */

	@Path("/app/novo")
	public void novo(String classificacao, String inftitulo, String origem, String conteudo, GcTipoInformacao tipo)
			throws Exception {
		result.forwardTo(this).editar(null, classificacao, inftitulo, origem, conteudo, tipo);
	}

	@Path({ "/app/editar/{sigla}", "/app/editar/" })
	public void editar(String sigla, String classificacao, String inftitulo, String origem, String conteudo,
			GcTipoInformacao tipo) throws Exception {
		GcInformacao informacao = null;
		DpPessoa titular = getTitular();
		DpLotacao lotaTitular = getLotaTitular();

		if (sigla != null) 
			informacao = GcInformacao.findBySigla(sigla);
		else
			informacao = new GcInformacao();

		if (informacao.getAutor() == null || informacao.podeRevisar(titular, lotaTitular) != null
				|| informacao.acessoPermitido(titular, lotaTitular, informacao.getEdicao().getId())) {
			List<GcTipoInformacao> tiposInformacao = GcTipoInformacao.AR.find("order by id").fetch();
			List<GcAcesso> acessos = GcAcesso.AR.find("order by id").fetch();
			if (inftitulo == null)
				inftitulo = (informacao.getArq() != null) ? informacao.getArq().getTitulo() : null;

			if (conteudo == null)
				conteudo = (informacao.getArq() != null) ? informacao.getArq().getConteudoTXT() : null;

			if (tipo.getId() == null)
				tipo = (informacao.getTipo() != null) ? informacao.getTipo() : tiposInformacao.get(0);

			if (informacao.getArq() == null)
				conteudo = (tipo.getArq() != null) ? tipo.getArq().getConteudoTXT() : null;

			if (conteudo != null && !conteudo.trim().startsWith("<")) {
				conteudo = bl.escapeHashTag(conteudo);
				if (informacao.getArq() != null) {
					informacao.getArq().setConteudoTXT(conteudo);
					conteudo = informacao.getConteudoHTML();
				}

			}
			// Evita que o título do conhecimento apareça cortado ao editá-lo, quando este
			// contiver aspas.
			if (inftitulo != null && inftitulo.contains("\""))
				inftitulo = inftitulo.replaceAll("\"", "&quot;");

			if (classificacao == null || classificacao.isEmpty())
				classificacao = (informacao.getArq() != null) ? informacao.getArq().getClassificacao() : null;
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
			if (informacao.getAutor() == null) {
				informacao.setAutor(titular);
			}
			if (informacao.getLotacao() == null) {
				informacao.setLotacao(lotaTitular);
			}

			boolean editarClassificacao = false;
			try {
				assertAcesso("EDTCLASS:Editar classificaÃ§Ã£o");
				editarClassificacao = true;
			} catch (Exception e) {
				//
			}

			result.include("informacao", informacao);
			result.include("grupo", informacao.getGrupo());
			result.include("tiposInformacao", tiposInformacao);
			result.include("acessos", acessos);
			result.include("inftitulo", inftitulo);
			result.include("conteudo", conteudo);
			result.include("classificacao", classificacao);
			result.include("origem", origem);
			result.include("tipo", tipo);
			result.include("editarClassificacao", editarClassificacao);
		} else
			throw new AplicacaoException("Restrição de Acesso (" + informacao.getEdicao().getNome()
					+ ") : O usuário não tem permissão para editar o conhecimento solicitado.");
	}

	private static String getRecaptchaSiteKey() {
		String pwd = null;
		try {
			pwd = Prop.get("/siga.recaptcha.key");
			if (pwd == null)
				throw new AplicacaoException("Erro obtendo propriedade siga.recaptcha.key");
			return pwd;
		} catch (Exception e) {
			throw new AplicacaoException("Erro obtendo propriedade siga.recaptcha.key", 0, e);
		}
	}

	private static String getRecaptchaSitePassword() {
		String pwd = null;
		try {
			pwd = Prop.get("/siga.recaptcha.pwd");
			if (pwd == null)
				throw new AplicacaoException("Erro obtendo propriedade siga.recaptcha.pwd");
			return pwd;
		} catch (Exception e) {
			throw new AplicacaoException("Erro obtendo propriedade siga.recaptcha.pwd", 0, e);
		}
	}

	public void selecaoInplace() throws Exception {
		int a = 0;
	}

	@Path({ "/public/app/exibir/{sigla}", "/public/app/exibir" })
	public void exibirPublicoExterno(String sigla) throws Exception {

		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		if (!informacao.acessoExternoPublicoPermitido())
			throw new AplicacaoException(
					"Restrição de Acesso: O conhecimento solicitado não é acessível pelo público externo.");

		// 2 lazies abaixo...
		for (GcTag t : informacao.getTags())
			;
		for (GcMovimentacao m : informacao.getMovs())
			;

		String conteudo = bl.marcarLinkNoConteudo(informacao, informacao.getArq().getConteudoTXT()
				.replace("/sigagc/app/baixar?id=", "/sigagc/public/app/baixar/" + informacao.getId() + "/"));

		conteudo = conteudo.replace("/sigagc/app/exibir", "/sigagc/public/app/exibir");

		em().detach(informacao);
		// if (conteudo != null)
		// informacao.arq.setConteudoTXT(conteudo);

		result.include("informacao", informacao);
		result.include("conteudo", conteudo);
	}

	@Transacional
	@Path({ "/app/exibir/{sigla}", "/app/exibir" })
	public void exibir(String sigla, String mensagem, boolean historico, boolean movimentacoes) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		DpPessoa titular = getTitular();
		DpLotacao lotaTitular = getLotaTitular();
		CpIdentidade idc = getIdentidadeCadastrante();
		GcMovimentacao movNotificacao = informacao.podeTomarCiencia(titular, lotaTitular);

		if (!informacao.acessoPermitido(titular, lotaTitular, informacao.getVisualizacao().getId())
				&& (informacao.podeRevisar(titular, lotaTitular) == null) && (!jaRevisou(sigla))
				&& movNotificacao == null && (!jaFoiNotificado(sigla)))
			throw new AplicacaoException("Restrição de Acesso (" + informacao.getVisualizacao().getNome()
					+ ") : O usuário não tem permissão para visualizar o conhecimento solicitado.");

		if (movNotificacao != null)
			bl.notificado(informacao, idc, titular, lotaTitular, movNotificacao);
		bl.logarVisita(informacao, idc, titular, lotaTitular);

		if (historico) {
			diff_match_patch diff = new diff_match_patch();

			String txtAnterior = "";
			String tituloAnterior = "";

			SortedSet<GcMovimentacao> list = new TreeSet<GcMovimentacao>();
			HashMap<GcMovimentacao, String> mapTitulo = new HashMap<GcMovimentacao, String>();
			HashMap<GcMovimentacao, String> mapTxt = new HashMap<GcMovimentacao, String>();
			if (informacao.getMovs() != null) {
				GcMovimentacao[] array = informacao.getMovs().toArray(new GcMovimentacao[informacao.getMovs().size()]);
				ArrayUtils.reverse(array);
				for (GcMovimentacao mov : array) {
					Long t = mov.getTipo().getId();

					if (mov.isCancelada())
						continue;

					if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_EDICAO
							|| t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO) {
						// Titulo
						String titulo = mov.getArq().getTitulo();
						LinkedList<Diff> tituloDiffs = diff.diff_main(tituloAnterior, titulo, true);
						String tituloDiffHtml = diff.diff_prettyHtml(tituloDiffs);
						boolean tituloAlterado = tituloDiffs == null || tituloDiffs.size() != 1
								|| tituloDiffs.size() == 1 && tituloDiffs.get(0).operation != Operation.EQUAL;

						// Texto
						String txt = mov.getArq().getConteudoTXT();
						LinkedList<Diff> txtDiffs = diff.diff_main(txtAnterior, txt, true);
						String txtDiffHtml = diff.diff_prettyHtml(txtDiffs);
						boolean txtAlterado = txtDiffs == null || txtDiffs.size() != 1
								|| txtDiffs.size() == 1 && txtDiffs.get(0).operation != Operation.EQUAL;

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
				result.include("list", list);
				result.include("mapTitulo", mapTitulo);
				result.include("mapTxt", mapTxt);
			}
		}

		for (GcTag t : informacao.getTags())
			;

		String conteudo = bl.marcarLinkNoConteudo(informacao, informacao.getArq().getConteudoTXT());
		em().detach(informacao);
		// if (conteudo != null)
		// informacao.arq.setConteudoTXT(conteudo);

		result.include("informacao", informacao);
		result.include("mensagem", mensagem);
		result.include("movimentacoes", movimentacoes);
		result.include("historico", historico);
		result.include("conteudo", conteudo);

	}

	public void exibirPontoDeEntrada(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		DpPessoa titular = getTitular();
		DpLotacao lotaTitular = getLotaTitular();
		CpIdentidade idc = getIdentidadeCadastrante();

		if (informacao.acessoPermitido(titular, lotaTitular, informacao.getVisualizacao().getId())) {
			String conteudo = bl.marcarLinkNoConteudo(informacao, informacao.getArq().getConteudoTXT());
			// if (conteudo != null)
			// informacao.arq.setConteudoTXT(conteudo);
			bl.logarVisita(informacao, idc, titular, lotaTitular);
			result.include("informacao", informacao);
			result.include("conteudo", conteudo);
		} else
			throw new AplicacaoException("Restrição de Acesso (" + informacao.getVisualizacao().getNome()
					+ ") : O usuário não tem permissão para visualizar o conhecimento solicitado.");
	}

	@Path("/app/historico/{sigla}")
	public void historico(String sigla) throws Exception {
		result.forwardTo(this).exibir(sigla, null, true, false);
	}

	@Path("/app/movimentacoes/{sigla}")
	public void movimentacoes(String sigla) throws Exception {
		result.forwardTo(this).exibir(sigla, null, false, true);
	}

	@Transacional
	@Path("/app/fechar/{sigla}")
	public void fechar(String sigla) throws Exception {
		GcInformacao inf = GcInformacao.findBySigla(sigla);
		if (inf.acessoPermitido(getTitular(), getLotaTitular(), inf.getEdicao().getId())) {
			bl.movimentar(inf, GcTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO, null, null, null, null, null, null,
					null, null, null);
			bl.atualizarInformacaoPorMovimentacoes(inf);
			bl.gravar(inf, getIdentidadeCadastrante(), getTitular(), getLotaTitular());
			result.redirectTo(this).exibir(inf.getSiglaCompacta(), null, false, false);
		} else
			throw new AplicacaoException("Restrição de Acesso (" + inf.getEdicao().getNome()
					+ ") : O usuário não tem permissão para finalizar o conhecimento solicitado.");
	}

	@Transacional
	@Path("/app/duplicar/{sigla}")
	public void duplicar(String sigla) throws Exception {
		GcInformacao infDuplicada = GcInformacao.findBySigla(sigla);

		GcMovimentacao movLocalizada = bl.movimentar(infDuplicada, GcTipoMovimentacao.TIPO_MOVIMENTACAO_DUPLICAR, null,
				null, null, null, null, null, null, null, null);
		bl.gravar(infDuplicada, getIdentidadeCadastrante(), getTitular(), getLotaTitular());

		GcInformacao inf = new GcInformacao();
		GcArquivo arq = new GcArquivo();

		arq = infDuplicada.getArq().duplicarConteudoInfo();
		inf.setAutor(getTitular());
		inf.setLotacao(getLotaTitular());
		inf.setOu(inf.getAutor().getOrgaoUsuario());
		inf.setTipo(GcTipoInformacao.AR.findById(infDuplicada.getTipo().getId()));
		inf.setVisualizacao(GcAcesso.AR.findById(infDuplicada.getVisualizacao().getId()));
		inf.setEdicao(GcAcesso.AR.findById(infDuplicada.getEdicao().getId()));

		GcMovimentacao movCriada = bl.movimentar(inf, GcTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO, null, null, null,
				arq.getTitulo(), arq.getConteudoTXT(), arq.getClassificacao(), movLocalizada, null, null);
		bl.gravar(inf, getIdentidadeCadastrante(), getTitular(), getLotaTitular());

		movLocalizada.setMovRef(movCriada);
		bl.gravar(infDuplicada, getIdentidadeCadastrante(), getTitular(), getLotaTitular());

		if (infDuplicada.isContemArquivos()) {
			for (GcMovimentacao movDuplicado : infDuplicada.getMovs()) {
				if (movDuplicado.isCancelada())
					continue;
				if (movDuplicado.getTipo().getId() == GcTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXAR_ARQUIVO
						&& movDuplicado.getMovCanceladora() == null) {
					GcMovimentacao m = bl.movimentar(inf, movDuplicado.getArq(),
							GcTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXAR_ARQUIVO);
					m.setMovRef(movLocalizada);
					bl.gravar(inf, getIdentidadeCadastrante(), getTitular(), getLotaTitular());
				}
			}
		}
		result.redirectTo(this).exibir(inf.getSiglaCompacta(), null, false, false);
	}

	@Transacional
	@RequestParamsPermissiveCheck
	public void gravar(GcInformacao informacao, String inftitulo, String conteudo, String classificacao, String origem,
			GcTipoInformacao tipo, GcAcesso visualizacao, GcAcesso edicao, CpPerfil grupo) throws Exception {

		if (informacao.getId() != null) {
			informacao = em().find(GcInformacao.class, informacao.getId());
		}

		// DpPessoa pessoa = (DpPessoa) renderArgs.get("cadastrante");
		DpPessoa pessoa = getTitular();
		DpLotacao lotacao = getLotaTitular();

		GcMovimentacao movAserReordenada;

		if (informacao.getAutor() == null) {
			informacao.setAutor(pessoa);
			informacao.setLotacao(lotacao);
		}
		if (informacao.getOu() == null) {
			if (informacao.getAutor() != null)
				informacao.setOu(informacao.getAutor().getOrgaoUsuario());
			else if (informacao.getLotacao() != null)
				informacao.setOu(informacao.getLotacao().getOrgaoUsuario());
			else if (pessoa != null)
				informacao.setOu(pessoa.getOrgaoUsuario());
		}
		
		if (tipo.getId() != null)
			informacao.setTipo(tipo);
		if (edicao.getId() != null)
			informacao.setEdicao(edicao);
		if (visualizacao.getId() != null)
			informacao.setVisualizacao(visualizacao);

		// Atualiza a classificação com as hashTags encontradas
		if (conteudo != null) {
			classificacao = bl.findHashTag(informacao, conteudo, classificacao, CONTROLE_HASH_TAG);
		}

		if (informacao.getEdicao().getId() == GcAcesso.ACESSO_LOTACAO_E_GRUPO
				|| informacao.getVisualizacao().getId() == GcAcesso.ACESSO_LOTACAO_E_GRUPO) {
			if (grupo == null || grupo.getId() == null)
				throw new Exception("Para acesso do tipo 'Grupo', e necessário informar um grupo para restrição.");
			informacao.setGrupo(grupo);
		} else
			informacao.setGrupo(null);

		if (informacao.getId() != null)
			movAserReordenada = bl.movimentar(informacao, GcTipoMovimentacao.TIPO_MOVIMENTACAO_EDICAO, null, null, null,
					inftitulo, conteudo, classificacao, null, null, null);
		else
			movAserReordenada = bl.movimentar(informacao, GcTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO, null, null,
					null, inftitulo, conteudo, classificacao, null, null, null);

		bl.gravar(informacao, getIdentidadeCadastrante(), getTitular(), getLotaTitular());

		bl.atualizarListaMovimentacoes(informacao, movAserReordenada);
		bl.atualizarInformacaoPorMovimentacoes(informacao);

		if (origem != null && origem.trim().length() != 0) {
			if (informacao.podeFinalizar(pessoa, lotacao)) {
				bl.movimentar(informacao, GcTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO, null, null, null, null, null,
						null, null, null, null);
				bl.gravar(informacao, getIdentidadeCadastrante(), pessoa, lotacao);
			}
			result.redirectTo(origem);
		} else
			result.redirectTo(this).exibir(informacao.getSiglaCompacta(), null, false, false);
	}

	@Transacional
	@Path("/app/remover/{sigla}")
	public void remover(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);

		if (informacao.getElaboracaoFim() != null)
			throw new AplicacaoException("Não é permitido remover informações que já foram finalizadas.");
		em().createQuery("delete from GcMarca where inf.id = :id").setParameter("id", informacao.getId())
				.executeUpdate();
		em().createQuery("delete from GcMovimentacao where inf.id = :id").setParameter("id", informacao.getId())
				.executeUpdate();
		informacao.delete();
		redirectToHome();
	}

	@Path("/app/notificar/{sigla}")
	public void notificar(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		result.include("informacao", informacao);
	}

	@Transacional
	public void notificarGravar(GcInformacao informacao, DpPessoa pessoa, DpLotacao lotacao, String email)
			throws Exception {
		// Nato: precisei fazer isso pq não vem attached e depois será feito um
		// lazy-load. Precisamos melhorar depois.
		informacao = GcInformacao.AR.findById(informacao.getId());

		// Nato: precisei fazer isso pq o vraptor injeta a entidade vazia. O
		// ideal seria injetar null se nenhum parâmetro for especificado.
		if (pessoa != null && pessoa.getId() == null)
			pessoa = null;

		if (pessoa != null && pessoa.getId() != null)
			pessoa = dao().consultar(pessoa.getId(), DpPessoa.class, false);

		if (lotacao != null && lotacao.getId() == null)
			lotacao = null;

		if (lotacao != null && lotacao.getId() != null)
			lotacao = dao().consultar(lotacao.getId(), DpLotacao.class, false);

		if (pessoa != null || lotacao != null || email != null) {

			if (pessoa != null && lotacao == null)
				lotacao = pessoa.getLotacao();

			GcMovimentacao n = informacao.podeTomarCiencia(pessoa, lotacao);
			if (n != null)
				throw new AplicacaoException(
						"Já há uma notificação pendente para a pessoa/lotação, feita em " + n.getHisDtIni());

			bl.movimentar(informacao, GcTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICAR, pessoa, lotacao, email, null,
					null, null, null, null, null);
			bl.gravar(informacao, getIdentidadeCadastrante(), getTitular(), getLotaTitular());
			correio.notificar(informacao, pessoa, lotacao, email);
			result.redirectTo(this).exibir(informacao.getSiglaCompacta(), "Notificacao realizada com sucesso!", false,
					false);
		} else
			throw new AplicacaoException("Para notificar é necessario selecionar uma Pessoa ou Lotacao.");
	}

	private List<GcPapel> getListaGcPapel() {
		return GcPapel.AR.all().fetch();
	}

	@Get("/app/vincular_papel/{sigla}")
	public void vincularPapel(final String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);

		if (!informacao.podeVincularPapel(getTitular(), getLotaTitular())) {
			throw new AplicacaoException("Definição de perfil não permitida");
		}

		result.include("informacao", informacao);
		result.include("listaPapel", this.getListaGcPapel());
		result.include("pessoaSel", new DpPessoaSelecao());
		result.include("lotacaoSel", new DpLotacaoSelecao());
		result.include("grupoSel", new CpPerfilSelecao());
	}

	@Transacional
	public void vincularPapelGravar(GcInformacao informacao, DpPessoaSelecao pessoaSel, DpLotacaoSelecao lotacaoSel,
			CpPerfilSelecao grupoSel, GcPapel papel) throws Exception {
		informacao = GcInformacao.AR.findById(informacao.getId());

		bl.vincularPapel(informacao, getIdentidadeCadastrante(), getTitular(), getLotaTitular(),
				pessoaSel.buscarObjeto(), lotacaoSel.buscarObjeto(), grupoSel.buscarObjeto(), papel, correio);

		result.redirectTo(this).exibir(informacao.getSiglaCompacta(), "Vinculação de perfil realizada com sucesso!",
				false, false);
	}

	@Path("/app/solicitarRevisao/{sigla}")
	public void solicitarRevisao(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		result.include("informacao", informacao);
	}

	@Transacional
	public void solicitarRevisaoGravar(GcInformacao informacao, DpPessoa pessoa, DpLotacao lotacao) throws Exception {

		// Nato: precisei fazer isso pq não vem attached e depois será feito um
		// lazy-load. Precisamos melhorar depois.
		informacao = GcInformacao.AR.findById(informacao.getId());

		// Nato: precisei fazer isso pq o vraptor injeta a entidade vazia. O
		// ideal seria injetar null se nenhum parâmetro for especificado.
		if (pessoa != null && pessoa.getId() == null)
			pessoa = null;
		if (lotacao != null && lotacao.getId() == null)
			lotacao = null;

		if (pessoa != null || lotacao != null) {
			if (pessoa != null && lotacao == null)
				lotacao = pessoa.getLotacao();

			GcMovimentacao n = informacao.podeRevisar(pessoa, lotacao);
			if (n != null)
				throw new AplicacaoException(
						"Já há um pedido de revisão pendente para a pessoa/lotação, feita em " + n.getHisDtIni());
			bl.movimentar(informacao, GcTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_DE_REVISAO, pessoa, lotacao, null,
					null, null, null, null, null, null);
			bl.gravar(informacao, getIdentidadeCadastrante(), getTitular(), getLotaTitular());
			result.redirectTo(this).exibir(informacao.getSiglaCompacta(),
					"Solicitação de revisão realizada com sucesso!", false, false);
		} else
			throw new AplicacaoException("Para solicitar revisão é necessário selecionar uma Pessoa ou Lotação.");
	}

	@Path("/app/anexar/{sigla}")
	public void anexar(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		result.include("informacao", informacao);
		String extensaoTodos = Prop.get("/sigagc.todos.extensoes.anexo.permitidas");
		result.include("extensaoTodos", extensaoTodos);
		String extensaoImagem = Prop.get("/sigagc.imagem.extensoes.anexo.permitidas");
		result.include("extensaoImagem", extensaoImagem);
		String extensaoDocumento = Prop.get("/sigagc.documento.extensoes.anexo.permitidas");
		result.include("extensaoDocumento", extensaoDocumento);
	}

	@Transacional
	public void gravarArquivo(GcInformacao informacao, String titulo, UploadedFile upload, UploadedFile file,
			String CKEditorFuncNum, String origem) throws Exception {
		// Nato: precisei fazer isso pq não vem attached e depois será feito um
		// lazy-load. Precisamos melhorar depois.
		informacao = GcInformacao.AR.findById(informacao.getId());

		if (file != null) {
			upload = file;
		}
		if (upload != null) {
			if (upload.getSize() > 2097152)
				throw new AplicacaoException("O tamanho do arquivo é maior que o " + "máximo permitido (2MB)");
			if (!(upload.getSize() > 0))
				throw new AplicacaoException(
						"Nao é permitido anexar se nenhum arquivo estiver selecionado. Favor selecionar arquivo.");
		}
		byte arquivo[] = IOUtils.toByteArray(upload.getFile());
		String tituloArquivo = upload.getFileName();
		if (origem.equals("editar")) {
			long id = bl.gravarArquivoSemMovimentacao(arquivo, tituloArquivo, upload.getContentType().toLowerCase());
			String url = "/sigagc/app/baixar?id=" + id;
			String js = "<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction('" + CKEditorFuncNum
					+ "','" + url + "');</script>";

			// HttpResult response = result.use(Results.http());
			// response.addHeader("Content-Type", "text/html");

			// result.use(Results.http()).body(js);

			result.use(Results.http()).addHeader("Content-Type", "text/html").body(js);
		} else {
			if (titulo != null && titulo.trim().length() > 0)
				tituloArquivo = titulo;
			bl.gravarArquivoComMovimentacao(informacao, getIdentidadeCadastrante(), getTitular(), getLotaTitular(),
					tituloArquivo, arquivo);
			result.use(Results.http()).body("success");
		}
	}

	@Transacional
	@Path("/app/removerAnexo")
	public void removerAnexo(String sigla, long idArq, long idMov) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		GcMovimentacao mov = GcMovimentacao.AR.findById(idMov);

		if (mov.getArq().getId() == idArq)
			bl.cancelarMovimentacao(informacao, mov, getIdentidadeCadastrante(), getTitular(), getLotaTitular());
		/*
		 * for (GcMovimentacao mov : informacao.movs) { if (mov.isCancelada()) continue;
		 * if (mov.tipo.id == GcTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXAR_ARQUIVO &&
		 * mov.arq.id == id) { movLocalizada = mov; break; } } GcMovimentacao m =
		 * bl.movimentar(informacao,
		 * GcTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO, null,
		 * null, null, null, null, movLocalizada, null, null, null);
		 * movLocalizada.movCanceladora = m; bl.gravar(informacao,
		 * getIdentidadeCadastrante(), titular(), lotaTitular());
		 */
		result.include("informacao", informacao);
	}

	@Path({ "/public/app/baixar/{idInformacao}/{id}", "/public/app/baixar" })
	public Download baixarSemAutenticacao(Long id, Long idInformacao) throws Exception {
		GcArquivo arq = GcArquivo.AR.findById(id);

		if (arq == null)
			throw new Exception("Arquivo não encontrado.");

		// TODO verificar se o conhecimento pai eh sem autenticacao
		GcInformacao infoMae = GcInformacao.AR.findById(idInformacao);
		// GcInformacao infoMae = GcMovimentacao.buscarInformacaoPorAnexo(arq,
		// idInformacao);
		if (infoMae == null || !(infoMae.acessoExternoPublicoPermitido()))
			throw new Exception("Arquivo não pode ser acessado sem autenticação.");

		return new ByteArrayDownload(arq.getConteudo(), arq.getMimeType(), arq.getTitulo(), false);

	}

	@Path({ "/app/baixar/{id}", "/app/baixar" })
	public Download baixar(Long id) throws Exception {
		GcArquivo arq = GcArquivo.AR.findById(id);
		if (arq != null)
			return new ByteArrayDownload(arq.getConteudo(), arq.getMimeType(), arq.getTitulo(), false);
		throw new Exception("Arquivo não encontrado.");
	}

	@Transacional
	@Path("/app/revisado/{sigla}")
	public void revisado(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		boolean temPedidoDeRevisao = false;
		if (informacao.getMovs() != null) {
			DpPessoa titular = getTitular();
			DpLotacao lotaTitular = getLotaTitular();

			for (GcMovimentacao mov : informacao.getMovs()) {
				if (mov.isCancelada())
					continue;
				if (mov.getTipo().getId() == GcTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_DE_REVISAO
						&& (titular.equivale(mov.getPessoaAtendente())
								|| lotaTitular.equivale(mov.getLotacaoAtendente()))) {
					temPedidoDeRevisao = true;

					bl.cancelarMovimentacao(informacao, mov, getIdentidadeCadastrante(), getTitular(),
							getLotaTitular());

					GcMovimentacao m = bl.movimentar(informacao, GcTipoMovimentacao.TIPO_MOVIMENTACAO_REVISADO, null,
							null, null, null, null, null, mov, null, null);

					bl.gravar(informacao, getIdentidadeCadastrante(), titular, lotaTitular);
					break;
				}

			}
			result.redirectTo(this).exibir(informacao.getSiglaCompacta(), "Conhecimento revisado com sucesso!", false,
					false);
		}
		if (!temPedidoDeRevisao)
			throw new AplicacaoException(
					"Não há pedido de revisão pendente para " + getIdentidadeCadastrante().getDpPessoa().getSigla());
	}

	// Verifica se usuário já revisou o conhecimento, para liberar acesso ao
	// conhecimento
	public boolean jaRevisou(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		boolean jaRevisou = false;
		if (informacao.getMovs() != null) {
			DpPessoa titular = getTitular();
			DpLotacao lotaTitular = getLotaTitular();
			SortedSet<GcMovimentacao> movsCopy = new TreeSet<GcMovimentacao>();
			movsCopy.addAll(informacao.getMovs());
			for (GcMovimentacao mov : movsCopy) {
				if (mov.isCancelada())
					continue;
				if (mov.getTipo().getId() == GcTipoMovimentacao.TIPO_MOVIMENTACAO_REVISADO
						&& (titular.equivale(mov.getPessoaTitular()) || lotaTitular.equivale(mov.getLotacaoTitular())))
					jaRevisou = true;
			}
		}
		return jaRevisou;
	}

	// Verifica se usuário já foi notificado sobre o conhecimento, para liberar
	// acesso ao conhecimento
	public boolean jaFoiNotificado(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		boolean jaFoiNotificado = false;
		if (informacao.getMovs() != null) {
			DpPessoa titular = getTitular();
			DpLotacao lotaTitular = getLotaTitular();
			SortedSet<GcMovimentacao> movsCopy = new TreeSet<GcMovimentacao>();
			movsCopy.addAll(informacao.getMovs());
			for (GcMovimentacao mov : movsCopy) {
				if (mov.isCancelada())
					continue;
				if (mov.getTipo().getId() == GcTipoMovimentacao.TIPO_MOVIMENTACAO_CIENTE
						&& (titular.equivale(mov.getPessoaTitular()) || lotaTitular.equivale(mov.getLotacaoTitular())))
					jaFoiNotificado = true;
			}
		}
		return jaFoiNotificado;
	}

	@Transacional
	@Path("/app/marcarComoInteressado/{sigla}")
	public void marcarComoInteressado(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		bl.interessado(informacao, getIdentidadeCadastrante(), getTitular(), getLotaTitular(), true);
		result.redirectTo(this).exibir(informacao.getSiglaCompacta(), null, false, false);
	}

	@Transacional
	@Path("/app/desmarcarComoInteressado/{sigla}")
	public void desmarcarComoInteressado(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		bl.interessado(informacao, getIdentidadeCadastrante(), getTitular(), getLotaTitular(), false);
		result.redirectTo(this).exibir(informacao.getSiglaCompacta(), null, false, false);
	}

	@Transacional
	@Path("/app/cancelar/{sigla}")
	public void cancelar(String sigla) throws Exception {
		GcInformacao informacao = GcInformacao.findBySigla(sigla);
		bl.cancelar(informacao, getIdentidadeCadastrante(), getTitular(), getLotaTitular());
		bl.atualizarInformacaoPorMovimentacoes(informacao);
		bl.atualizarMarcas(informacao);
		result.redirectTo(this).exibir(informacao.getSiglaCompacta(), null, false, false);
	}

	@Path("/app/tag/selecionar")
	public void selecionarTag(String sigla) throws Exception {
		GcTag sel = (GcTag) new GcTag().selecionar(sigla);
		result.include("sel", sel);
		// render("@siga-play-module.selecionar", sel);
	}

	@Path("/public/app/selecionar")
	public void selecionarPublico(String sigla, boolean retornarCompacta, String matricula) throws Exception {
		try {
			CpOrgaoUsuario ouDefault = null;
			if (matricula != null) {
				DpPessoa pes = dao().getPessoaFromSigla(matricula);
				if (pes != null)
					ouDefault = pes.getOrgaoUsuario();

			}
			GcInformacao inf = GcInformacao.findBySigla(sigla, ouDefault);

			if (inf != null) {
				result.use(Results.http()).body("1;" + inf.getId() + ";" + inf.getSigla() + ";" + "/sigagc/app/exibir/"
						+ inf.getSiglaCompacta());
				return;
			}
		} catch (Exception ex) {

		}
		result.use(Results.http()).body("0");
	}

	@Path({ "/app/selecionar", "/app/informacao/selecionar" })
	public void selecionar(String sigla, boolean retornarCompacta) throws Exception {
		GcInformacao inf;

		try {
			inf = GcInformacao.findBySigla(sigla, getLotaTitular().getOrgaoUsuario());
		} catch (Exception e) {
			inf = GcInformacao.findByTitulo(sigla);
		}

		if (inf != null) {
			result.use(Results.http()).body("1;" + inf.getId() + ";" + inf.getSigla() + ";" + inf.getArq().getTitulo());
		} else {
			result.use(Results.http()).body("0");
		}
	}

	@Path("/app/tag/buscar")
	public void buscarTag(String sigla, GcTag filtro) {
		List<GcTag> itensTemp = null;
		Set<GcTag> itens = null;

		Query query = em().createNamedQuery("listarTagCategorias");
		List<String> l = query.getResultList();
		List<SigaIdDescr> listaTagCategorias = new ArrayList<SigaIdDescr>();
		for (Object s : l) {
			listaTagCategorias.add(new SigaIdDescr(s, s));
		}

		try {
			if (filtro == null)
				filtro = new GcTag();
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			itensTemp = (List<GcTag>) filtro.buscar();
			itens = new TreeSet<GcTag>(itensTemp);

		} catch (Exception e) {
			itensTemp = new ArrayList<GcTag>();
		}

		result.include("itens", itens);
		result.include("filtro", filtro);
		result.include("listaTagCategorias", listaTagCategorias);
	}

	// @Path("/public/app/dadosRI")
	public void dadosRI(Long ultimaAtualizacao, Long desempate) throws UnsupportedEncodingException {
		Date dtUltimaAtualizacao = new Date(0L);
		Long idDesempate = 0L;
		if (ultimaAtualizacao != null)
			dtUltimaAtualizacao = new Date(ultimaAtualizacao);
		if (desempate != null)
			idDesempate = desempate;

		Query query = em().createNamedQuery("dadosParaRecuperacaoDeInformacao");
		query.setParameter("dt", dtUltimaAtualizacao, TemporalType.TIMESTAMP);
		query.setParameter("desempate", idDesempate);
		query.setMaxResults(10);
		List<Object[]> lista = query.getResultList();
		if (lista.size() == 0) {
			result.use(Results.http()).body("[]");
			return;
		}

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
			dri.titulo = a.getTitulo();
			dri.ultimaAtualizacao = dt;
			dri.idDesempate = idMov;
			dri.ativo = ativo;
			if (ativo) {
				dri.conteudo = new String(a.getConteudo(), "utf-8");
			}
			resultado.add(dri);
		}
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z").create();
		result.use(Results.http()).body(gson.toJson(resultado));
	}

	@Transacional
	@Path("/app/desfazer/{sigla}/{movId}")
	public void desfazer(String sigla, long movId) throws Exception {
		GcInformacao info = GcInformacao.findBySigla(sigla);
		GcMovimentacao mov = GcMovimentacao.AR.findById(movId);

		bl.cancelarMovimentacao(info, mov, getIdentidadeCadastrante(), getTitular(), getLotaTitular());
		result.redirectTo(this).exibir(info.getSiglaCompacta(), null, false, false);
	}

	@Get("/public/app/testes/gadgetTest")
	public void test(final String matricula) throws Exception {
		if (matricula == null) {
			result.use(Results.http()).body("ERRO: É necessário especificar o parâmetro 'matricula'.")
					.setStatusCode(400);
			return;
		}

		final DpPessoa pes = daoPes(matricula);

		if (pes == null) {
			result.use(Results.http())
					.body("ERRO: Não foi localizada a pessoa referenciada pelo parâmetro 'matricula'.")
					.setStatusCode(400);
			return;
		}

		setTitular(pes);
		setLotaTitular(pes.getLotacao());
		result.redirectTo(this.getClass()).gadget();
	}

}