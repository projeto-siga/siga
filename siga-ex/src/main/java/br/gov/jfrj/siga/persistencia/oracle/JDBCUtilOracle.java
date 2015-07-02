/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.persistencia.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Classe utilitaria para acesso ao Oracle.
 */
public final class JDBCUtilOracle {
	private static final Log log = LogFactory.getLog(JDBCUtilOracle.class);
	static {
		try {
			Class.forName(Messages.getString("Oracle.ForName"));
		} catch (final ClassNotFoundException ex) {
			JDBCUtilOracle.log.error(Messages.getString("Oracle.ClassNotFound"));
			System.err.print(Messages.getString("Oracle.ClassNotFound"));
			System.exit(-1);
		}
	}

	public static Connection getConnection() {
		JDBCUtilOracle.log.debug("chamado o método com parametro do arquivo properties");
		return JDBCUtilOracle.getConnection(Messages.getString("Oracle.URL"), Messages.getString("Oracle.usuario"), Messages
				.getString("Oracle.credencial"));
	}

	public static Connection getConnection(final String url, final String usuario, final String credencial) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, usuario, credencial);
		} catch (final SQLException e) {

			JDBCUtilOracle.log.debug(Messages.getString("Oracle.SQLErro"));
			System.err.print(Messages.getString("Oracle.SQLErro") + e.getMessage());
		}

		return conn;
	}

	public static Connection getConnectionPool() {
		JDBCUtilOracle.log.debug("chamado o método com parametro do arquivo properties");
		return JDBCUtilOracle.getConnectionPool(Messages.getString("Oracle.MeuConnection"));
	}

	public static Connection getConnectionPool(final String dataSource) {

		Context initContext = null;
		Context envContext = null;
		Connection conn = null;

		try {
			JDBCUtilOracle.log.debug("Criando variavel de contexto");
			initContext = new InitialContext();

			envContext = (Context) initContext.lookup("java:/comp/env");
			JDBCUtilOracle.log.debug("Criando datasource ");
			final DataSource ds = (DataSource) envContext.lookup(dataSource);
			conn = ds.getConnection();

		} catch (final NamingException e) {
			JDBCUtilOracle.log.error(Messages.getString("Oracle.LookUpErro"));
			System.err.print(Messages.getString("Oracle.LookUpErro") + e.getMessage());

		} catch (final SQLException ex) {
			System.err.print(Messages.getString("Oracle.SQLErro") + ex.getMessage());
		}
		return conn;
	}

	private JDBCUtilOracle() {
		// construtor privado
	}

}
