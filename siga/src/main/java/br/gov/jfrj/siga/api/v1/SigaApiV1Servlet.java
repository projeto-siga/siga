package br.gov.jfrj.siga.api.v1;

import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.UUID;

import javax.naming.NameNotFoundException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.sql.DataSource;

import com.crivano.swaggerservlet.SwaggerContext;
import com.crivano.swaggerservlet.SwaggerServlet;
import com.crivano.swaggerservlet.dependency.TestableDependency;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.XjusRecordServiceEnum;
import br.gov.jfrj.siga.base.Prop.IPropertyProvider;
import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.context.AcessoPublicoEPrivado;
import br.gov.jfrj.siga.context.ApiContextSupport;
import br.gov.jfrj.siga.cp.CpArquivoTipoArmazenamentoEnum;
import br.gov.jfrj.siga.cp.arquivo.Armazenamento;
import br.gov.jfrj.siga.cp.arquivo.ArmazenamentoFabrica;
import br.gov.jfrj.siga.cp.arquivo.ArmazenamentoTemporalidadeEnum;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.idp.jwt.AuthJwtFormFilter;
import br.gov.jfrj.siga.model.ContextoPersistencia;

public class SigaApiV1Servlet extends SwaggerServlet implements IPropertyProvider {
    private static final long serialVersionUID = 1756711359239182178L;
    public static boolean migrationComplete = false;
//	public static ExecutorService executor = null;

    @Override
    public void initialize(ServletConfig config) throws ServletException {
        setAPI(ISigaApiV1.class);

        setActionPackage("br.gov.jfrj.siga.api.v1");

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

        addDependency(new TestableDependency("database", "sigaexds", false, 0, 10000) {

            @Override
            public String getUrl() {
                return getProperty("datasource.name");
            }

            @Override
            public boolean test() throws Exception {
                try (SigaApiV1Context ctx = new SigaApiV1Context()) {
                    ctx.init(null);
                    return CpDao.getInstance().dt() != null;
                }
            }
        });

        CpArquivoTipoArmazenamentoEnum tipoArmazenamento = CpArquivoTipoArmazenamentoEnum
                .valueOf(Prop.get("/siga.armazenamento.arquivo.tipo"));
        switch (tipoArmazenamento) {
            case S3:
                addDependency(new TestableDependency("storage", getProperty("/siga.armazenamento.arquivo.bucket"), false,
                        0, 10000) {

                    @Override
                    public String getUrl() {
                        return getProperty("/siga.armazenamento.arquivo.url");
                    }

                    @Override
                    public boolean test() throws Exception {
                        Armazenamento storage = ArmazenamentoFabrica.getInstance(tipoArmazenamento);
                        String caminho = "test-" + UUID.randomUUID().toString() + "-" + ArmazenamentoTemporalidadeEnum.TEMPORARIO.getIdentificador() + ".txt";
                        storage.salvar(caminho, "text/plain", caminho.getBytes());
                        String conteudo = new String(storage.recuperar(caminho));
                        storage.apagar(caminho);
                        return caminho.equals(conteudo);
                    }

                    @Override
                    public boolean isPartial() {
                        return false;
                    }
                });
                break;
        }

        addDependency(new TestableDependency("database", "sigaexds-migration", false, 0, 10000) {

            @Override
            public String getUrl() {
                return getProperty("datasource.name") + "-migration";
            }

            @Override
            public boolean test() throws Exception {
                return migrationComplete;
            }

            @Override
            public boolean isPartial() {
                return true;
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
		addPublicProperty("datasource.name", "java:/jboss/datasources/SigaCpDS");
		addPublicProperty("senha.usuario.expiracao.dias", null);
        addPrivateProperty("sinc.password", null);
        addPublicProperty("quadro.quantitativo.exibir.documentos", "true");
	}

	@Override
	public String getService() {
		return "siga";
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
