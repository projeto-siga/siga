package br.gov.jfrj.siga.integracao.api.v1;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.model.ContextoPersistencia;

import com.crivano.swaggerservlet.SwaggerServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class IntegracaoApiV1Servlet extends SwaggerServlet implements Prop.IPropertyProvider {
    private static final long serialVersionUID = 105585066035160075L;

    @Override
    public void initialize(ServletConfig config) throws ServletException {
        setAPI(IIntegracaoApiV1.class);

        setActionPackage("br.gov.jfrj.siga.integracao.api.v1");

        Prop.setProvider(this);
        Prop.defineGlobalProperties();
        defineProperties();
    }

    private void defineProperties() {
        // Informações Siafem
        addPublicProperty("ws.siafem.nome.modelo", null);
        addPublicProperty("ws.siafem.url.wsdl", null);
        addPublicProperty("ws.siafem.url.namespace", null);
        addPublicProperty("ws.siafem.service.localpart", null);
        addPublicProperty("ws.siafem.service.localpartsoap", null);

    }

    @Override
    public String getService() {
        return "siga-integracao";
    }

    @Override
    public String getUser() {
        return ContextoPersistencia.getUserPrincipal();
    }

    @Override
    public String getProp(String nome) {
        return getProperty(nome);
    }
}
