package br.gov.jfrj.siga.integracao.api.v1;

import br.gov.jfrj.siga.base.Prop;
import com.crivano.swaggerservlet.SwaggerServlet;
import com.crivano.swaggerservlet.dependency.TestableDependency;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.net.URL;
import java.net.URLConnection;

public class IntegracaoApiV1Servlet extends SwaggerServlet implements Prop.IPropertyProvider {
    private static final long serialVersionUID = 105585066035160075L;

    @Override
    protected void initialize(ServletConfig config) throws ServletException {
        setAPI(IIntegracaoApiV1.class);

        setActionPackage("br.gov.jfrj.siga.integracao.api.v1");

        Prop.setProvider(this);
        Prop.defineGlobalProperties();
        defineProperties();

        class HttpGetDependency extends TestableDependency {
            final String testsite;

            HttpGetDependency(String category, String service, String testsite, boolean partial, long msMin,
                              long msMax) {
                super(category, service, partial, msMin, msMax);
                this.testsite = testsite;
            }

            @Override
            public String getUrl() {
                return testsite;
            }

            @Override
            public boolean test() throws Exception {
                final URL url = new URL(testsite);
                final URLConnection conn = url.openConnection();
                conn.connect();
                return true;
            }
        }
    }

    private void defineProperties() {
        addPublicProperty("datasource.name", "java:/jboss/datasources/SigaGcDS");
    }

    @Override
    public String getProp(String nome) {
        return getProperty(nome);
    }
}
