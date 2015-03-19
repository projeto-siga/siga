package br.gov.jfrj.webwork.action;

import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.http.HTTPException;

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
		this.modulos.put("sigatp", "/sigatp/gadget");
	}

	
	public String aCarregaModulo(){
		String url = this.modulos.get(this.getModulo());
		try{
			this.html = this.http.get(url, getRequest(), null);
		}catch(HTTPException e){
			int statusCode = e.getStatusCode();
			html = "<span style='color:red' class='error'> "+statusCode+" - Módulo indisponível. </span>";
		}
		tratarErro();
		
		return SUCCESS;
	}


	private void tratarErro() {
		if (this.html.contains("HTTP Status")){
			this.html = "<span style='color:red' class='error'> Módulo indisponível. </span>";
		}else if (http.isErrorPage(html) || http.isAuthPage(html)){
			this.html = "<span style='color:red' class='error'> Erro no carregamento do módulo. Por favor, tente atualizar a página (F5). Caso o erro persista entre em contato com o suporte.</span>";
		}else if (this.html.equals("")){
			this.html = "<span style='color:red' class='error'> 1001 - Erro no carregamento do módulo. Por favor, tente atualizar a página (F5). Caso o erro persista entre em contato com o suporte.</span>";
		}
	}


	public String getHtml() {
		return html;
	}


	public void setHtml(String html) {
		this.html = html;
	}
}
