package br.gov.jfrj.siga.wf.api.v1;

import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.crivano.swaggerservlet.SwaggerServlet;
import com.crivano.swaggerservlet.SwaggerUtils;
import com.crivano.swaggerservlet.dependency.TestableDependency;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.Prop.IPropertyProvider;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.wf.dao.WfDao;

public class WfApiV1Servlet extends SwaggerServlet implements IPropertyProvider {
	private static final long serialVersionUID = 1756711359239182178L;

	public static ExecutorService executor = null;

	@Override
	public void initialize(ServletConfig config) throws ServletException {
		setAPI(IWfApiV1.class);

		setActionPackage("br.gov.jfrj.siga.wf.api.v1");

		Prop.setProvider(this);
		Prop.defineGlobalProperties();
		defineProperties();

		// Threadpool
		if (Prop.get("redis.password") != null)
			SwaggerUtils.setCache(new MemCacheRedis());
		executor = Executors.newFixedThreadPool(Prop.getInt("threadpool.size"));

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

		addDependency(new TestableDependency("database", "sigawfds", false, 0, 10000) {

			@Override
			public String getUrl() {
				return Prop.get("datasource.name");
			}

			@Override
			public boolean test() throws Exception {
				try (WfApiV1Context ctx = new WfApiV1Context()) {
					ctx.init(null);
					return WfDao.getInstance().dt() != null;
				}
			}

			@Override
			public boolean isPartial() {
				return false;
			}
		});

		if (Prop.get("redis.password") != null)
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

	private void defineProperties() {
		addPublicProperty("mail.link.tarefa", null);
		addPublicProperty("mail.link.documento", null);
		addPublicProperty("rel.estatisticas.gerais.media.truncada.min", "5");
		addPublicProperty("rel.estatisticas.gerais.media.truncada.max", "25");

		addRestrictedProperty("datasource.url", null);
		if (Prop.get("datasource.url") != null) {
			addRestrictedProperty("datasource.username");
			addPrivateProperty("datasource.password");
			addRestrictedProperty("datasource.name", null);
		} else {
			addRestrictedProperty("datasource.username", null);
			addPrivateProperty("datasource.password", null);
			addRestrictedProperty("datasource.name", "java:/jboss/datasources/SigaWfDS");
		}

		// Redis
		//
		addRestrictedProperty("redis.database", "10");
		addPrivateProperty("redis.password", null);
		addRestrictedProperty("redis.slave.port", "0");
		addRestrictedProperty("redis.slave.host", null);
		addRestrictedProperty("redis.master.host", "localhost");
		addRestrictedProperty("redis.master.port", "6379");

		addPublicProperty("threadpool.size", "10");
	}

	@Override
	public String getService() {
		return "sigawf";
	}

	@Override
	public String getUser() {
		return ContextoPersistencia.getUserPrincipal();
	}

	public static <T> Future<T> submitToExecutor(Callable<T> task) {
		return executor.submit(task);
	}

	@Override
	public String getProp(String nome) {
		return getProperty(nome);
	}

}
