package br.gov.jfrj.siga.sr.api.v1;

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

public class SrApiV1Servlet extends SigaSwaggerServlet implements IPropertyProvider {
	private static final long serialVersionUID = 1756711359239182178L;

//	public static ExecutorService executor = null;

	@Override
	public void initialize(ServletConfig config) throws Exception {
        super.initialize(config);

        setAPI(ISrApiV1.class);

		setActionPackage("br.gov.jfrj.siga.sr.api.v1");

		Prop.setProvider(this);
		Prop.defineGlobalProperties();
		defineProperties();

		// Threadpool
//		if (SwaggerServlet.getProperty("redis.password") != null)
//			SwaggerUtils.setCache(new MemCacheRedis());
//		executor = Executors.newFixedThreadPool(new Integer(SwaggerServlet.getProperty("threadpool.size")));

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

//		class FileSystemWriteDependency extends TestableDependency {
//			private static final String TESTING = "testing...";
//			String path;
//
//			FileSystemWriteDependency(String service, String path, boolean partial, long msMin, long msMax) {
//				super("filesystem", service, partial, msMin, msMax);
//				this.path = path;
//			}
//
//			@Override
//			public String getUrl() {
//				return path;
//			}
//
//			@Override
//			public boolean test() throws Exception {
//				Path file = Paths.get(path + "/test.temp");
//				Files.write(file, TESTING.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
//				String s = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
//				return s != null;
//			}
//		}
//
//		addDependency(
//				new FileSystemWriteDependency("upload.dir.temp", getProperty("upload.dir.temp"), false, 0, 10000));

		addDependency(new HttpGetDependency("rest", "www.google.com/recaptcha",
				"https://www.google.com/recaptcha/api/siteverify", false, 0, 10000));

		addDependency(new TestableDependency("database", "sigaservicosds", false, 0, 10000) {

			@Override
			public String getUrl() {
				return getProperty("datasource.name");
			}

			@Override
			public boolean test() throws Exception {
				try (SrApiV1Context ctx = new SrApiV1Context()) {
					ctx.init(null);
					return CpDao.getInstance().dt() != null;
				}
			}

			@Override
			public boolean isPartial() {
				return false;
			}
		});

//		if (SwaggerServlet.getProperty("redis.password") != null)
//			addDependency(new TestableDependency("cache", "redis", false, 0, 10000) {
//
//				@Override
//				public String getUrl() {
//					return "redis://" + MemCacheRedis.getMasterHost() + ":" + MemCacheRedis.getMasterPort() + "/"
//							+ MemCacheRedis.getDatabase() + " (" + "redis://" + MemCacheRedis.getSlaveHost() + ":"
//							+ MemCacheRedis.getSlavePort() + "/" + MemCacheRedis.getDatabase() + ")";
//				}
//
//				@Override
//				public boolean test() throws Exception {
//					String uuid = UUID.randomUUID().toString();
//					MemCacheRedis mc = new MemCacheRedis();
//					mc.store("test", uuid.getBytes());
//					String uuid2 = new String(mc.retrieve("test"));
//					return uuid.equals(uuid2);
//				}
//			});

	}

	private void defineProperties() {
		addPublicProperty("datasource.name", "java:/jboss/datasources/SigaServicosDS");
		addPublicProperty("senha.usuario.expiracao.dias", null);
		addPublicProperty("corporativo.dadosrh.password", "3600");
		addPublicProperty("corporativo.dadosrh.situacoesParaImportar", "1,2,31,11,12,36,38");
	}

	@Override
	public String getService() {
		return "sigasr";
	}

	@Override
	public String getUser() {
		return ContextoPersistencia.getUserPrincipal();
	}

//	public static <T> Future<T> submitToExecutor(Callable<T> task) {
//		return executor.submit(task);
//	}

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
