package br.gov.jfrj.siga.vraptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mashape.unirest.http.Unirest;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.ByteArrayDownload;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.Contexto;
import br.gov.jfrj.siga.base.SigaHTTP;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.GenericoSelecao;

@Resource
public class PrincipalController extends SigaController {
	HttpServletResponse response;
	
	public PrincipalController(HttpServletRequest request, HttpServletResponse response, Result result,
			CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
		this.response = response;
	}

	@Get("app/principal")
	public void principal() {
	}

	@Get("app/pagina_vazia")
	public void paginaVazia() {
	}

	@Get("app/usuario_autenticado")
	public void usuarioAutenticado() {
	}

	@Get("permalink/{sigla}")
	public void permalink(final String sigla) {
		GenericoSelecao sel = buscarGenericoPorSigla(sigla, null, null, null);
		if (sel == null || sel.getDescricao() == null)
			result.notFound();
		else {
			String urlBase = getRequest().getScheme() + "://"
					+ getRequest().getServerName() + ":"
					+ getRequest().getServerPort();
			result.redirectTo(urlBase + sel.getDescricao());
		}
	}

	@Get("permalink/{sigla}/{parte}")
	public void permalink(final String sigla, final String parte) {
		String urlBase = getRequest().getScheme() + "://"
				+ getRequest().getServerName() + ":"
				+ getRequest().getServerPort();
		result.redirectTo(urlBase + "/sigaex/app/expediente/mov/exibir?id="
				+ parte);
	}

	@Get("public/app/generico/selecionar")
	public void selecionar(final String sigla, final String matricula) {
		try {
			DpPessoa pes = getTitular();
			DpLotacao lot = getLotaTitular();
			String incluirMatricula = "";
			if (matricula != null) {
				pes = daoPes(matricula);
				lot = pes.getLotacao();
				incluirMatricula = "&matricula=" + matricula;
			} else {
				incluirMatricula = "&matricula="
						+ getTitular().getSiglaCompleta();
			}

			final GenericoSelecao sel = buscarGenericoPorSigla(sigla, pes, lot,
					incluirMatricula);

			if (sel.getId() == null) {
				if (Cp.getInstance().getProp().xjusUrl() != null) {
					sel.setId(-1L);
					sel.setSigla(sigla);
					sel.setDescricao("/siga/app/xjus?q=" + sigla);
				} else if (Cp.getInstance().getProp().gsaUrl() != null) {
						sel.setId(-1L);
						sel.setSigla(sigla);
						sel.setDescricao("/siga/app/busca?q=" + sigla);
				} else {
					throw new Exception("Elemento não encontrado");
				}
			}

			result.include("sel", sel);
			result.include("request", getRequest());
			result.use(Results.page()).forwardTo(
					"/WEB-INF/jsp/ajax_retorno.jsp");
		} catch (Exception e) {
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_vazio.jsp");
		}
	}

	private GenericoSelecao buscarGenericoPorSigla(String sigla, DpPessoa pes,
			DpLotacao lot, String incluirMatricula) {

		sigla = sigla.trim().toUpperCase();

		Map<String, CpOrgaoUsuario> mapAcronimo = new TreeMap<String, CpOrgaoUsuario>();
		for (CpOrgaoUsuario ou : CpDao.getInstance().listarOrgaosUsuarios()) {
			mapAcronimo.put(ou.getAcronimoOrgaoUsu(), ou);
			mapAcronimo.put(ou.getSiglaOrgaoUsu(), ou);
		}
		
		StringBuilder acronimos = new StringBuilder();
		for (String s : mapAcronimo.keySet()) {
			if (acronimos.length() > 0)
				acronimos.append("|");
			acronimos.append(s);
		}

		final Pattern p1 = Pattern
				.compile("^(?<orgao>"
						+ acronimos.toString()
						+ ")?-?(?:(?<especie>[A-Za-z]{3})|(?<modulo>SR|TMPSR|GC|TMPGC|TP))-?([0-9][0-9A-Za-z\\.\\-\\/]*)$");
		final Matcher m1 = p1.matcher(sigla);

		final GenericoSelecao sel = new GenericoSelecao();
		if (incluirMatricula == null)
			incluirMatricula = "";

		String urlBase = getRequest().getScheme() + "://"
				+ getRequest().getServerName() + ":"
				+ getRequest().getServerPort();
		List<String> lurls = new ArrayList<>();

		if (m1.find()) {
			String orgao = m1.group("orgao");
			String especie = m1.group("especie");
			String modulo = m1.group("modulo");

			if (especie != null) {
				// Documentos
				lurls.add(urlBase
						+ "/sigaex/public/app/expediente/selecionar?sigla="
						+ sigla + incluirMatricula
						+ ";/sigaex/app/expediente/doc/exibir?sigla=");
			} else if (modulo != null) {
				switch (modulo) {
				case "SR": // Solicitacoes
				case "TMPSR":
					lurls.add(urlBase
							+ "/sigasr/public/app/solicitacao/selecionar?sigla="
							+ sigla + incluirMatricula);
					break;
				case "GC": // Conhecimentos
				case "TMPGC":
					lurls.add(urlBase + "/sigagc/public/app/selecionar?sigla="
							+ sigla + incluirMatricula);
					break;
				case "TP": // Transportes
					lurls.add(urlBase + "/sigatp"
							+ "/app/documento/selecionar?sigla=" + sigla
							+ incluirMatricula
							+ ";/sigatp/app/documento/exibir?sigla=");
					break;
				}
			}
		} else {
			// Pessoas
			lurls.add(urlBase + "/siga/public/app/pessoa/selecionar?sigla=" + sigla
					+ incluirMatricula + ";/siga/app/pessoa/exibir?sigla=");

			// Lotacoes
			lurls.add(urlBase + "/siga/public/app/lotacao/selecionar?sigla=" + sigla
					+ incluirMatricula + ";/siga/app/lotacao/exibir?sigla=");
		}

		final SigaHTTP http = new SigaHTTP();
		for (String urls : lurls) {
			String[] aurls = urls.split(";"); // cada string pode conter a url
												// de busca ";" a url destino
			String[] response = null;
			try {
				response = http.get(aurls[0]).split(";");
			} catch (Exception e) {
			}

			if (response == null
					|| (response.length == 1 && (response[0].equals("") || Integer.valueOf(response[0]) == 0)))
				continue;

			sel.setId(Long.valueOf(response[1]));
			sel.setSigla(response[2]);
			if (aurls.length == 2)
				// A url de destino foi especificada
				sel.setDescricao(aurls[1] + sel.getSigla());
			else
				// Devemos usar o terceiro parâmetro como url de destino
				sel.setDescricao(response[3]);
			return sel;
		}
		return sel;
	}
	
	@Post
	@Consumes("text/vnd.graphviz")
	@Path("/public/app/graphviz/svg")
	public Download graphvizProxy(String dot) throws Exception {
		String url = (String) Contexto.resource("graphviz.url");
		if (url == null)
			throw new Exception("Parâmetro graphviz.url precisa ser informado");
		corsHeaders(response);
		
		String body = Unirest.post(url).header("Content-Type", "text/vnd.graphviz").body(dot).asString().getBody();
		return new ByteArrayDownload(body.getBytes(), "image/svg+xml", "graphic.svg");
	}
	
	public static void corsHeaders(HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods",
				"GET,POST,DELETE,PUT,OPTIONS");
		response.addHeader("Access-Control-Allow-Headers",
				"Content-Type,Authorization");
	}

}