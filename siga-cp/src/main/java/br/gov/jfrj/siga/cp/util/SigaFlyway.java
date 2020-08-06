package br.gov.jfrj.siga.cp.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;

public class SigaFlyway {

	public static void migrate(String dataSource, String location) throws NamingException {
		if ("true".equals(System.getProperty("siga.flyway.mysql.migrate", "false"))) {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:");
			DataSource ds = (DataSource) envContext.lookup(dataSource);
			Flyway flyway = Flyway.configure().dataSource(ds).locations(location).placeholderReplacement(false).load();
			flyway.migrate();
		}
	}

}
