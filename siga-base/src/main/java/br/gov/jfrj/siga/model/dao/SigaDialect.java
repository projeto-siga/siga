package br.gov.jfrj.siga.model.dao;

import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.Oracle8iDialect;
import org.hibernate.dialect.PostgreSQL81Dialect;

public enum SigaDialect {

	ORACLE(Oracle8iDialect.class),
	MYSQL(MySQLDialect.class),
	POSTGRESQL(PostgreSQL81Dialect.class),
	;

	private static final String SIGA_HIBERNATE_DIALECT_PROPERTY = "siga.hibernate.dialect";

	private Class<? extends Dialect> baseDialect;

	private SigaDialect(Class<? extends Dialect> baseDialect) {
		this.baseDialect = baseDialect;
	}

	public static SigaDialect from(Class<?> dialect) {
		if (dialect == null) {
			throw new IllegalStateException("Dialeto não informado (nulo)");
		}
		return Stream.of(values())
				.filter(d -> d.baseDialect.isAssignableFrom(dialect))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Não foi possível identificar um Dialeto compatível com o SIGA para o tipo " + dialect.getName()));
	}

	public static SigaDialect from(String dialectProperty) {
		if (StringUtils.isEmpty(dialectProperty)) {
			throw new IllegalStateException("Dialeto não informado (propriedade de sistema \"" + SIGA_HIBERNATE_DIALECT_PROPERTY + "\" está nula ou vazia)");
		}
		try {
			final Class<?> dialectClass = Class.forName(StringUtils.trim(dialectProperty));
			return from(dialectClass);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Não foi possível identificar um Dialeto compatível com o SIGA para a property " + dialectProperty);
		}
	}

	public static SigaDialect fromSystemProperty() {
		final String dialectProperty = System.getProperty(SIGA_HIBERNATE_DIALECT_PROPERTY);
		return from(dialectProperty);
	}

}
