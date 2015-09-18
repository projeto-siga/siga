package br.gov.jfrj.siga.vraptor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.SigaHTTP;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.GenericoSelecao;

@Resource
public class PrincipalController extends SigaController {
	
	public PrincipalController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
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
			String urlBase = getRequest().getScheme() + "://" + getRequest().getServerName() + ":" + getRequest().getServerPort();
			result.redirectTo(urlBase + sel.getDescricao());
		}
	}
	
	@Get("permalink/{sigla}/{parte}")
	public void permalink(final String sigla, final String parte) {
		String urlBase = getRequest().getScheme() + "://" + getRequest().getServerName() + ":" + getRequest().getServerPort();
		result.redirectTo(urlBase + "/sigaex/app/expediente/mov/exibir?id=" + parte);
	}
	
	@Get("app/generico/selecionar")
	public void selecionar(final String sigla, final String matricula) {
		try {
			DpPessoa pes = getTitular();
			DpLotacao lot = getLotaTitular();
			String incluirMatricula = "";
			if (matricula != null) {
				pes = daoPes(matricula);
				lot = pes.getLotacao();
//				testes = "/testes";
				incluirMatricula = "&matricula=" + matricula;
			} else {
				incluirMatricula = "&matricula=" + getTitular().getSiglaCompleta();
			}

			final GenericoSelecao sel = buscarGenericoPorSigla(sigla, pes, lot,
					incluirMatricula);

			result.include("sel", sel);
			result.include("request", getRequest());
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_retorno.jsp");

		} catch (Exception e) {
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_vazio.jsp");
		}
	}

	private GenericoSelecao buscarGenericoPorSigla(final String sigla,
			DpPessoa pes, DpLotacao lot, String incluirMatricula) {
		if (incluirMatricula == null)
			incluirMatricula = "";
		final GenericoSelecao sel = new GenericoSelecao();
		// TODO não precisa pegar isso de um properties, isso existe no proprio request getServerName, getPort...
		String testes = "";

		//String urlBase = "http://"+ SigaBaseProperties.getString(SigaBaseProperties.getString("ambiente") + ".servidor.principal")+ getRequest().getServerPort();
		String urlBase = getRequest().getScheme() + "://" + getRequest().getServerName() + ":" + getRequest().getServerPort();
		
		String URLSelecionar = "";
		String uRLExibir = "";

		final List<String> orgaos = new ArrayList<String>();
		String copiaSigla = sigla.toUpperCase();
		for (CpOrgaoUsuario o : dao().consultaCpOrgaoUsuario()) {
			orgaos.add(o.getSiglaOrgaoUsu());
			orgaos.add(o.getAcronimoOrgaoUsu());
		}
		for (String s : orgaos)
			if (copiaSigla.startsWith(s)) {
				copiaSigla = copiaSigla.substring(s.length());
				break;
			}
		if (copiaSigla.startsWith("-"))
			copiaSigla = copiaSigla.substring(1);

		//alterada a condição que verifica se é uma solicitação do siga-sr
		//dessa forma a regex verifica se a sigla começa com SR ou sr e termina com números
		//necessário para não dar conflito caso exista uma lotação que inicie com SR
		if (copiaSigla.startsWith("SR")) {
//			if (copiaSigla.matches("^[SR|sr].*[0-9]+$")) {
			if (pes == null || lot == null || Cp.getInstance()
					.getConf()
					.podeUtilizarServicoPorConfiguracao(pes, lot, "SIGA;SR"))
				URLSelecionar = urlBase + "/sigasr" + testes+ "/app/solicitacao/selecionar?sigla=" + sigla + incluirMatricula;
		}
		//alterado formato da sigla de requisições, missões e serviços
		//else if (copiaSigla.startsWith("MTP")
		//		|| copiaSigla.startsWith("RTP")
		//		|| copiaSigla.startsWith("STP")) {
		else if (copiaSigla.startsWith("TP") &&
				(copiaSigla.endsWith("M") ||
				 copiaSigla.endsWith("S") ||
				 copiaSigla.endsWith("R"))) {
			if (pes == null || lot == null || Cp.getInstance()
					.getConf()
					.podeUtilizarServicoPorConfiguracao(pes, lot, "SIGA;TP")) {
				URLSelecionar = urlBase + "/sigatp"
						+ "/app/documento/selecionar?sigla=" + sigla
						+ incluirMatricula;
			}
		}
		else
			URLSelecionar = urlBase 
					+ "/sigaex" + (testes.length() > 0 ? testes : "/app/expediente") + "/selecionar?sigla=" + sigla+ incluirMatricula;

		final SigaHTTP http = new SigaHTTP();
		String[] response = http.get(URLSelecionar, getRequest(), null).split(";");

		if (response.length == 1 && Integer.valueOf(response[0]) == 0) {
			//verificar se após a retirada dos prefixos referente 
			//ao orgão (sigla_orgao_usu = RJ ou acronimo_orgao_usu = JFRJ) e não achar resultado com as opções anteriores 
			//a string copiaSigla somente possui números
			if (copiaSigla.matches("(^[0-9]+$)")) {
				URLSelecionar = urlBase 
						+ "/siga/app"+ (testes.length() > 0 ? testes : "/pessoa") + "/selecionar?sigla=" + sigla+ incluirMatricula;
			}
			//encontrar lotações
			else {
				URLSelecionar = urlBase 
					+ "/siga/app"+ (testes.length() > 0 ? testes : "/lotacao")+ "/selecionar?sigla=" + sigla+ incluirMatricula;
			}
			
			response = http.get(URLSelecionar, getRequest(), null).split(";");
			
			if (copiaSigla.matches("(^[0-9]+$)")) 
				uRLExibir = "/siga/app/pessoa/exibir?sigla="+ response[2];
			else
				uRLExibir = "/siga/app/lotacao/exibir?sigla="+ response[2];
		}
		else {
			if (copiaSigla.startsWith("SR"))
//					if (copiaSigla.matches("^[SR|sr].*[0-9]+$"))
					uRLExibir = "/sigasr/app/solicitacao/exibir/" + response[1];
			//alterado formato da sigla de requisições, missões e serviços
			//else if (copiaSigla.startsWith("MTP")
			//		|| copiaSigla.startsWith("STP")
			//		|| copiaSigla.startsWith("RTP"))
			else if (copiaSigla.startsWith("TP") &&
					(copiaSigla.endsWith("M") ||
					 copiaSigla.endsWith("S") ||
					 copiaSigla.endsWith("R")))
				uRLExibir = "/sigatp/app/documento/exibir?sigla=" + response[2];
			else
				uRLExibir = "/sigaex/app/expediente/doc/exibir?sigla="+ response[2];
		}
		
		sel.setId(Long.valueOf(response[1]));
		sel.setSigla(response[2]);
		sel.setDescricao(uRLExibir);
		return sel;
	}
	
}