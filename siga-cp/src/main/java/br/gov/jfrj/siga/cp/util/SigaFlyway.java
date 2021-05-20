package br.gov.jfrj.siga.cp.util;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.SigaHTTP;

public class SigaFlyway {
	private final static org.jboss.logging.Logger log = Logger.getLogger(SigaFlyway.class);

	public static void migrate(String dataSource, String location, boolean waitCorp) throws NamingException {
		if ("true".equals(System.getProperty("siga.flyway.mysql.migrate", "false"))) {
			if (waitCorp) {
				int sleep = 1;
				while (!isCorpMigrationComplete())
					try {
						log.info("AGUARDANDO MIGRATION");
						Thread.sleep(sleep * 1000);
						if (sleep < 4)
							sleep *= 2;
					} catch (InterruptedException e) {
					}
			}
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:");
			DataSource ds = (DataSource) envContext.lookup(dataSource);
			Flyway flyway = Flyway.configure().dataSource(ds).locations(location).placeholderReplacement(false).load();
			flyway.migrate();
		}
	}

	public static boolean isMigrationComplete(String url) {
		try {
			JSONObject obj = getAsJson(url, null, 1000, null);
			if (obj == null)
				throw new Exception("Teste para verificar se a migração está completa retornou resultado nulo");
			JSONArray dependencies = obj.getJSONArray("dependencies");

			for (int i = 0; i < dependencies.length(); i++) {
				JSONObject o = dependencies.getJSONObject(i);
				if (o.getString("service").endsWith("migration"))
					return o.getBoolean("available");
			}
			throw new Exception(
					"Teste para verificar se a migração não encontrou dependência terminada com 'migration'");
		} catch (Exception ex) {
			Throwable e = ex;
			while (e.getCause() != null && e.getCause() != e)
				e = e.getCause();
			if (e instanceof SocketTimeoutException)
				log.info("Teste se migração está completa não conseguiu se conectar na url " + url
						+ ", tentando novamente...");
			else
				log.info("Erro testando se migração está completa na url " + url, ex);
			return false;
		}
	}

	public static boolean isCorpMigrationComplete() {
		String property = System.getProperty("siga.base.url");
		if (property == null)
			property = "http://localhost:8080";
		String url = property + "/siga/api/v1/test";
		return isMigrationComplete(url);
	}

	public static JSONObject getAsJson(String URL, HashMap<String, String> header, Integer timeout, String payload)
			throws AplicacaoException {
		try {
			JSONObject obj = new JSONObject(new SigaHTTP().getNaWeb(URL, header, timeout, payload));
			return obj;
		} catch (Exception ioe) {
			throw new RuntimeException("Erro obtendo recurso externo", ioe);
		}
	}

	public static void stopJBoss() {
		try {
			MBeanServerConnection mbeanServerConnection = ManagementFactory.getPlatformMBeanServer();
			ObjectName mbeanName = new ObjectName("jboss.as:management-root=server");
			Object[] args = { false };
			String[] sigs = { "java.lang.Boolean" };
			mbeanServerConnection.invoke(mbeanName, "shutdown", args, sigs);
		} catch (InstanceNotFoundException | MBeanException | ReflectionException | IOException
				| MalformedObjectNameException e) {
			log.error("Erro interrompendo o JBoss", e);
		}
	}

}