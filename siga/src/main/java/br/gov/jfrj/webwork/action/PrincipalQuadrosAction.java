package br.gov.jfrj.webwork.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import br.gov.jfrj.siga.base.SigaHTTP;
import br.gov.jfrj.siga.libs.webwork.SigaActionSupport;

public class PrincipalQuadrosAction extends SigaActionSupport {
	
	private Map<String, String> modulos;
	private SigaHTTP http;
	private String html;
	private String modulo;
	
	public String getModulo() {
		return modulo;
	}


	public void setModulo(String modulo) {
		this.modulo = modulo;
	}


	public PrincipalQuadrosAction() {
		http = new SigaHTTP();
		this.modulos = new HashMap<String, String>();
		this.modulos.put("sigaex", "/sigaex/expediente/doc/gadget.action?idTpFormaDoc=1&apenasQuadro=true");
		this.modulos.put("processos", "/sigaex/expediente/doc/gadget.action?idTpFormaDoc=2");
		this.modulos.put("sigawf", "/sigawf/inbox.action");
		this.modulos.put("sigasr", "/sigasr/solicitacao/gadget");
		this.modulos.put("sigagc", "/sigagc/app/gadget");
	}

	
	public String aCarregaModulo(){
		String url = this.modulos.get(this.getModulo());
//		url = addCache(url);
		this.html = this.http.get(url, getRequest(), null);
		
		tratarErro();
		
		return SUCCESS;
	}


	private String addCache(String url) {
		int random = new Random().nextInt(10000);
		if (url.contains("?")){
			url += "&_="+random;
		}else{
			url += "?_=" + random;
		}
		
		return url;
	}


	private void tratarErro() {
		if (this.html.contains("HTTP Status 404")){
			this.html = "<span style='color:red' class='error'> 404 - Módulo indisponível. </span>";
		}else if (this.html.contains("HTTP Status 500")){
			this.html = "<span style='color:red' class='error'> 500 - Módulo indisponível. </span>";
		}else if (this.html.contains("HTTP Status")){
			this.html = "<span style='color:red' class='error'> Módulo indisponível. </span>";
		}else if (this.html.contains("<title>") && (this.html.contains("Não Foi Possível Completar a Operação") || (this.html.contains("Senha")))){
			this.html = "<span style='color:red' class='error'> Erro no carregamento do módulo. Por favor, atualize a página (F5). Caso o erro persista entre em contato com o suporte.</span>";
		}
	}


	public String getHtml() {
		return html;
	}


	public void setHtml(String html) {
		this.html = html;
	}
	
}
