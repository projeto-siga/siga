package br.gov.jfrj.siga.ex.api.v1;

import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import com.crivano.swaggerservlet.SwaggerContext;
import com.crivano.swaggerservlet.SwaggerServlet;
import com.crivano.swaggerservlet.SwaggerUtils;
import com.crivano.swaggerservlet.dependency.TestableDependency;

import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.idp.jwt.AuthJwtFormFilter;
import br.gov.jfrj.siga.model.ContextoPersistencia;

public class ExApiV1Servlet extends SwaggerServlet {
	private static final long serialVersionUID = 1756711359239182178L;

	public static ExecutorService executor = null;

	@Override
	public void initialize(ServletConfig config) throws ServletException {
		setAPI(IExApiV1.class);

		setActionPackage("br.gov.jfrj.siga.ex.api.v1");

		addPublicProperty("env", "desenv");
		addPublicProperty("wootric.token", null);
		addPublicProperty("base.url", "http://localhost:8080/sigaex/api/v1");
		addPublicProperty("recaptcha.site.key", null);
		addPrivateProperty("recaptcha.secret.key", null);

		addRestrictedProperty("upload.dir.temp");

		addPrivateProperty("api.secret", null);

//		addPrivateProperty("jwt.secret", System.getProperty("idp.jwt.modulo.pwd.sigaidp"));
//		addPublicProperty("jwt.issuer", "siga");
//		addPublicProperty("cookie.name", "siga-jwt");
//		addPublicProperty("cookie.domain", null);
//		addPublicProperty("cookie.expire.seconds", Long.toString(20 * 60L)); // Expira em 20min
//		addPublicProperty("cookie.renew.seconds", Long.toString(15 * 60L)); // Renova 15min antes de expirar

		addRestrictedProperty("datasource.url", null);
		if (getProperty("datasource.url") != null) {
			addRestrictedProperty("datasource.username");
			addPrivateProperty("datasource.password");
			addRestrictedProperty("datasource.name", null);
		} else {
			addRestrictedProperty("datasource.username", null);
			addPrivateProperty("datasource.password", null);
			addRestrictedProperty("datasource.name", "java:/jboss/datasources/SigaExDS");
		}

		// Redis
		//
		addRestrictedProperty("redis.database", "10");
		addPrivateProperty("redis.password", null);
		addRestrictedProperty("redis.slave.port", "0");
		addRestrictedProperty("redis.slave.host", null);
		addRestrictedProperty("redis.master.host", "localhost");
		addRestrictedProperty("redis.master.port", "6379");

		if (SwaggerServlet.getProperty("redis.password") != null)
			SwaggerUtils.setCache(new MemCacheRedis());

		// Threadpool
		addPublicProperty("threadpool.size", "10");
		executor = Executors.newFixedThreadPool(new Integer(SwaggerServlet.getProperty("threadpool.size")));

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

		class FileSystemWriteDependency extends TestableDependency {
			private static final String TESTING = "testing...";
			String path;

			FileSystemWriteDependency(String service, String path, boolean partial, long msMin, long msMax) {
				super("filesystem", service, partial, msMin, msMax);
				this.path = path;
			}

			@Override
			public String getUrl() {
				return path;
			}

			@Override
			public boolean test() throws Exception {
				Path file = Paths.get(path + "/test.temp");
				Files.write(file, TESTING.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
				String s = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
				return s != null;
			}
		}

		addDependency(
				new FileSystemWriteDependency("upload.dir.temp", getProperty("upload.dir.temp"), false, 0, 10000));

		addDependency(new HttpGetDependency("rest", "www.google.com/recaptcha",
				"https://www.google.com/recaptcha/api/siteverify", false, 0, 10000));

		addDependency(new TestableDependency("database", "sigaexds", false, 0, 10000) {

			@Override
			public String getUrl() {
				return getProperty("datasource.name");
			}

			@Override
			public boolean test() throws Exception {
				try (ApiContext ctx = new ApiContext(true)) {
					return ExDao.getInstance().dt() != null;
				}
			}

			@Override
			public boolean isPartial() {
				return false;
			}
		});

		if (SwaggerServlet.getProperty("redis.password") != null)
			addDependency(new TestableDependency("cache", "redis", false, 0, 10000) {

				@Override
				public String getUrl() {
					return "redis://" + MemCacheRedis.getMasterHost() + ":" + MemCacheRedis.getMasterPort() + "/"
							+ MemCacheRedis.getDatabase() + " (" + "redis://" + MemCacheRedis.getSlaveHost() + ":"
							+ MemCacheRedis.getSlavePort() + "/" + MemCacheRedis.getDatabase() + ")";
				}

				@Override
				public boolean test() throws Exception {
					String uuid = UUID.randomUUID().toString();
					MemCacheRedis mc = new MemCacheRedis();
					mc.store("test", uuid.getBytes());
					String uuid2 = new String(mc.retrieve("test"));
					return uuid.equals(uuid2);
				}
			});

	}

	@Override
	public String getService() {
		return "sigaex";
	}

	@Override
	public String getUser() {
		return ContextoPersistencia.getUserPrincipal();
	}

	public static <T> Future<T> submitToExecutor(Callable<T> task) {
		return executor.submit(task);
	}

	@Override
	public void invoke(SwaggerContext context) throws Exception {
		try {
			if (!context.getAction().getClass().isAnnotationPresent(AcessoPublico.class)) {
				try {
					String token = AuthJwtFormFilter.extrairAuthorization(context.getRequest());
					Map<String, Object> decodedToken = AuthJwtFormFilter.validarToken(token);
					final long now = System.currentTimeMillis() / 1000L;
					if ((Integer) decodedToken.get("exp") < now + AuthJwtFormFilter.TIME_TO_RENEW_IN_S) {
						// Seria bom incluir o attributo HttpOnly
						String tokenNew = AuthJwtFormFilter.renovarToken(token);
						Map<String, Object> decodedNewToken = AuthJwtFormFilter.validarToken(token);
						Cookie cookie = AuthJwtFormFilter.buildCookie(tokenNew);
						context.getResponse().addCookie(cookie);
					}
					ContextoPersistencia.setUserPrincipal((String) decodedToken.get("sub"));
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

}
