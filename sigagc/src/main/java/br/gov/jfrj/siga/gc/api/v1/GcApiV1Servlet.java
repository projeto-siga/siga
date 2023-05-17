package br.gov.jfrj.siga.gc.api.v1;

import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.http.Cookie;

import com.crivano.swaggerservlet.SwaggerContext;
import com.crivano.swaggerservlet.dependency.TestableDependency;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.Prop.IPropertyProvider;
import br.gov.jfrj.siga.context.SigaSwaggerServlet;
import br.gov.jfrj.siga.cp.auth.AutenticadorFabrica;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.idp.jwt.AuthJwtFormFilter;
import br.gov.jfrj.siga.model.ContextoPersistencia;

public class GcApiV1Servlet extends SigaSwaggerServlet implements IPropertyProvider {
	private static final long serialVersionUID = 1756711359239182178L;

	@Override
	public void initialize(ServletConfig config) throws Exception {
        super.initialize(config);
		setAPI(IGcApiV1.class);

		setActionPackage("br.gov.jfrj.siga.gc.api.v1");

		Prop.setProvider(this);
		Prop.defineGlobalProperties();
		defineProperties();

		class HttpGetDependency extends TestableDependency {
			String testsite;

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

		addDependency(new TestableDependency("database", "sigagcds", false, 0, 10000) {

			@Override
			public String getUrl() {
				return getProperty("datasource.name");
			}

			@Override
			public boolean test() throws Exception {
				try (GcApiV1Context ctx = new GcApiV1Context()) {
					ctx.init(null);
					return CpDao.getInstance().dt() != null;
				}
			}

			@Override
			public boolean isPartial() {
				return false;
			}
		});

	}

	private void defineProperties() {
		addPublicProperty("datasource.name", "java:/jboss/datasources/SigaGcDS");
	}

	@Override
	public String getService() {
		return "sigagc";
	}

	@Override
	public String getUser() {
		return ContextoPersistencia.getUserPrincipal();
	}

	@Override
	public void invoke(SwaggerContext context) throws Exception {
		try {
			if (!context.getAction().getClass().isAnnotationPresent(AcessoPublico.class)) {
				try {
				    ContextoPersistencia.setUserPrincipal(AutenticadorFabrica.getInstance().obterPrincipal(context.getRequest(), context.getResponse()));
				} catch (Exception e) {
					if (!context.getAction().getClass().isAnnotationPresent(AcessoPublicoEPrivado.class))
						throw e;
				}
			}
			super.invoke(context);
		} finally {
			ContextoPersistencia.removeUserPrincipal();
		}
	}

	@Override
	public String getProp(String nome) {
		return getProperty(nome);
	}

}
