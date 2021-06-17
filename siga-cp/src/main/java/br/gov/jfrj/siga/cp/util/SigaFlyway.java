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

	public static void migrate(String dataSourceJndi, String module, String schema) {
		if (!isFlywayAtivo()) {
			return;
		}

		final String location = getDatabaseMigrationsLocation(module);
		try {
			final Context initContext = new InitialContext();
			final Context envContext = (Context) initContext.lookup("java:");
			final DataSource dataSource = (DataSource) envContext.lookup(dataSourceJndi);

			Flyway flyway = Flyway.configure()
					.schemas(schema)
					.createSchemas(false)
					.dataSource(dataSource)
					.locations(location)
					.placeholderReplacement(false)
					.load();

			flyway.migrate();
		} catch (NamingException e) {
			throw new IllegalStateException("Flyway DataSource not available for module " + module, e);
		}
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