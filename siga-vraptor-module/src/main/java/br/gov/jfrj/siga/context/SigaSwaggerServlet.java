package br.gov.jfrj.siga.context;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.crivano.swaggerservlet.SwaggerServlet;

public class SigaSwaggerServlet extends SwaggerServlet {

    @Override
    protected void initialize(ServletConfig config) throws Exception {
        super.initialize(config);

        showPackageErrors("br.gov");
        showPackageErrors("com.crivano");
    }

}
