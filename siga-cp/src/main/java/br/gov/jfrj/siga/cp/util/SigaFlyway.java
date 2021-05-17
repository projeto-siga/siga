package br.gov.jfrj.siga.cp.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.lang3.BooleanUtils;
import org.flywaydb.core.Flyway;

public class SigaFlyway {

	private static final String PROP_DATABASE = "siga.flyway.db";
	private static final String PROP_DATABASE_DEFAULT = "mysql";
	private static final String PROP_FLYWAY_MIGRATE = "siga.flyway.migrate";
	private static final String PROP_FLYWAY_MIGRATE_DEFAULT = "false";

	public static void migrate(String dataSource, String module) throws NamingException {
		if (!isFlywayAtivo()) {
			return;
		}

		final String location = getDatabaseMigrationsLocation(module);
		final Context initContext = new InitialContext();
		final Context envContext = (Context) initContext.lookup("java:");
		final DataSource ds = (DataSource) envContext.lookup(dataSource);

		Flyway.configure()
				.dataSource(ds)
				.locations(location)
				.placeholderReplacement(false)
				.load()
				.migrate();
	}

	private static String getDatabaseMigrationsLocation(String modulo) {
		final String database = System.getProperty(PROP_DATABASE, PROP_DATABASE_DEFAULT);
		return "classpath:db/" + database + "/" + modulo;
	}

	private static boolean isFlywayAtivo() {
		final String migrate = System.getProperty(PROP_FLYWAY_MIGRATE, PROP_FLYWAY_MIGRATE_DEFAULT);
		return BooleanUtils.toBoolean(migrate);
	}

}