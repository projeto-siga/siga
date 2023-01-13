package br.gov.jfrj.siga.arquivo;

import java.util.concurrent.ExecutorService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import com.crivano.swaggerservlet.SwaggerServlet;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.Prop.IPropertyProvider;

public class ArqApiV1Servlet extends SwaggerServlet implements IPropertyProvider {
	private static final long serialVersionUID = 1756711359239182178L;

	public static ExecutorService executor = null;
	
	public ArqApiV1Servlet () throws ServletException {
		setActionPackage("br.gov.jfrj.siga.arquivo");
		servletContext = "sigaarq";
		Prop.setProvider(this);
		Prop.defineGlobalProperties();
		defineProperties();
	}
	
	@Override
	public void initialize(ServletConfig config) throws ServletException {
		setActionPackage("br.gov.jfrj.siga.arquivo");
		Prop.setProvider(this);
		Prop.defineGlobalProperties();
		defineProperties();
	}

	private void defineProperties() {
		addRestrictedProperty("armazenamento.arquivo.formatolivre.usuario", 
			Prop.get("/siga.armazenamento.arquivo.usuario"));
		addRestrictedProperty("armazenamento.arquivo.formatolivre.senha",
			Prop.get("/siga.armazenamento.arquivo.senha"));
		addRestrictedProperty("armazenamento.arquivo.formatolivre.bucket", 
			Prop.get("/siga.armazenamento.arquivo.bucket"));
		addRestrictedProperty("allowed.origin.urls", 
			Prop.get("/siga.base.url"));
	}

	@Override
	public String getService() {
		return "sigaarq";
	}

	@Override
	public String getProp(String nome) {
		return getProperty(nome);
	}
	
}