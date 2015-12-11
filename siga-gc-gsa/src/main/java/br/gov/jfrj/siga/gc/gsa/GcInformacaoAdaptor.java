/*******************************************************************************
 * Copyright (c) 2006 - 2015 SJRJ.
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
package br.gov.jfrj.siga.gc.gsa;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import org.hibernate.cfg.Configuration;

import com.google.enterprise.adaptor.AbstractAdaptor;
import com.google.enterprise.adaptor.Adaptor;
import com.google.enterprise.adaptor.AdaptorContext;
import com.google.enterprise.adaptor.Config;
import com.google.enterprise.adaptor.ConfigUtils;
import com.google.enterprise.adaptor.DocId;
import com.google.enterprise.adaptor.DocIdPusher;
import com.google.enterprise.adaptor.Request;
import com.google.enterprise.adaptor.Response;

/**
 * Adaptador Google Search Appliance para movimentações do SIGA-DOC.
 * <p>
 * Example command line:
 * <p>
 *
 * java \ -jar siga-ex-gsa.one-jar -Dgsa.hostname=myGSA -Dservidor=desenv \
 * -Djournal.reducedMem=true
 */
public class GcInformacaoAdaptor extends AbstractAdaptor implements Adaptor {
	protected static final Logger log = Logger
			.getLogger(GcInformacaoAdaptor.class.getName());
	protected Charset encoding = Charset.forName("UTF-8");
	protected Date dateLastUpdated;
	protected String permalink;
	protected Properties adaptorProperties;
	private String jdbcConnection;
	private String jdbcUser;
	private String jdbcPassword;
	static final String DEFAULT_CONFIG_FILE = "adaptor-config.properties";

	public Connection getConnection() throws SQLException {

		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", jdbcUser);
		connectionProps.put("password", jdbcPassword);

		conn = DriverManager.getConnection(jdbcConnection, connectionProps);
		System.out.println("Connected to database");
		return conn;
	}

	public Date getSysdate() {
		Date currentDate = null;
		Connection conn = null;
		Statement stmt = null;
		String query = "SELECT SYSDATE FROM DUAL";
		try {
			conn = getConnection();
			stmt = conn.createStatement();

			// Execute a SELECT query on Oracle Dummy DUAL Table. Useful for
			// retrieving system values
			// Enables us to retrieve values as if querying from a table
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				currentDate = rs.getDate(1); // get first column returned
				System.out.println("Current Date from Oracle is : "
						+ currentDate);
			}
			rs.close();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return currentDate;
	}

	@Override
	public void initConfig(Config config) {
		loadPropertyFromSysEnv(config, "jdbc.connection");
		loadPropertyFromSysEnv(config, "jdbc.user");
		loadPropertyFromSysEnv(config, "jdbc.password");
	}

	private void loadPropertyFromSysEnv(Config config, String name) {
		if (System.getProperty(name) != null)
			ConfigUtils.setValue(name, System.getProperty(name), config);
	}
	
	@Override
	public void init(AdaptorContext context) throws Exception {
		Configuration cfg;
		String servidor = context.getConfig().getValue("servidor");
		permalink = context.getConfig().getValue("url.permalink");

		jdbcConnection = context.getConfig().getValue("jdbc.connection");
		jdbcUser = context.getConfig().getValue("jdbc.user");
		jdbcPassword = context.getConfig().getValue("jdbc.password");
	}

	/**
	 * Get all doc ids from database.
	 **/
	public void getDocIds(DocIdPusher pusher) throws InterruptedException {
		this.dateLastUpdated = getSysdate();
		pushDocIds(pusher, new Date(0L));
	}

	protected void pushDocIds(DocIdPusher pusher, Date date)
			throws InterruptedException {
		Connection conn = null;
		Statement stmt = null;
		String query = "select id_informacao from sigagc.gc_informacao where numero is not null and his_dt_fim is null";
		try {
			BufferingPusher outstream = new BufferingPusher(pusher);

			conn = getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				DocId id = new DocId("" + rs.getLong("ID_INFORMACAO"));
				outstream.add(id);
			}
			outstream.forcePush();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void addMetadata(Response resp, String title, String value) {
		if (value == null)
			return;
		value = value.trim();
		value = escapeHtml(value);
		resp.addMetadata(title, value);
	}

	/** Gives the bytes of a document referenced with id. */
	public void getDocContent(Request req, Response resp) throws IOException {
		DocId id = req.getDocId();
		log.fine("obtendo id = " + id);
		long primaryKey;
		try {
			primaryKey = Long.parseLong(id.getUniqueId());
		} catch (NumberFormatException nfe) {
			resp.respondNotFound();
			return;
		}

		Connection conn = null;
		PreparedStatement stmt = null;
		String query = "select ACRONIMO_ORGAO_USU, ANO, NUMERO, NOME_TIPO_INFORMACAO, TITULO, NOME_ACESSO, HIS_DT_INI, CONTEUDO_TIPO, CONTEUDO, "
				+ "(select nome_pessoa from corporativo.dp_pessoa pes where pes.id_pessoa = inf.id_pessoa_titular) SUBSCRITOR, "
				+ "(select sigla_lotacao from corporativo.dp_lotacao lot where lot.id_lotacao = inf.id_lotacao_titular) SUBSCRITOR_LOTACAO, "
				+ "(select nome_pessoa from corporativo.dp_pessoa pes, corporativo.cp_identidade idn where idn.id_pessoa = pes.id_pessoa and idn.id_identidade = inf.his_idc_ini) CADASTRANTE, "
				+ "(select sigla_lotacao from corporativo.dp_lotacao lot, corporativo.dp_pessoa pes, corporativo.cp_identidade idn where lot.id_lotacao = pes.id_lotacao and idn.id_pessoa = pes.id_pessoa and idn.id_identidade = inf.his_idc_ini) CADASTRANTE_LOTACAO "
				+ "from sigagc.gc_informacao inf, sigagc.gc_arquivo arq, corporativo.cp_orgao_usuario ou, sigagc.gc_tipo_informacao tp, sigagc.gc_acesso ac  "
				+ "where inf.id_arquivo = arq.id_conteudo and inf.id_orgao_usuario = ou.id_orgao_usu and inf.id_tipo_informacao = tp.id_tipo_informacao and inf.id_acesso = ac.id_acesso and numero is not null and ac.id_acesso = 1 "
				+ "and inf.id_informacao = ?";
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(query);
			stmt.setLong(1, primaryKey);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				resp.respondNotFound();
				return;
			}

			// Add Metadata
			//
			addMetadata(resp, "orgao", rs.getString("ACRONIMO_ORGAO_USU"));
			String codigo = rs.getString("ACRONIMO_ORGAO_USU") + "-GC-"
					+ rs.getInt("ANO") + "/"
					+ Long.toString(rs.getLong("NUMERO") + 100000).substring(1);
			addMetadata(resp, "codigo", codigo);
			addMetadata(resp, "origem", "Conhecimento");
			addMetadata(resp, "especie", rs.getString("NOME_TIPO_INFORMACAO"));
			addMetadata(resp, "descricao", rs.getString("TITULO"));
			addMetadata(resp, "acesso", rs.getString("NOME_ACESSO"));
			addMetadata(resp, "data", getDtYYYYMMDD(rs.getDate("HIS_DT_INI")));
			addMetadata(resp, "subscritor_lotacao",
					rs.getString("SUBSCRITOR_LOTACAO"));
			addMetadata(resp, "subscritor", rs.getString("SUBSCRITOR"));
			addMetadata(resp, "cadastrante_lotacao",
					rs.getString("CADASTRANTE_LOTACAO"));
			addMetadata(resp, "cadastrante", rs.getString("CADASTRANTE"));

			// Add Acl
			//
			// List<GroupPrincipal> groups = new ArrayList<>();
			// groups.add(new GroupPrincipal(s));
			// Acl acl = new Acl.Builder().setPermitGroups(groups)
			// .setEverythingCaseInsensitive().build();
			// resp.setAcl(acl);

			// Add Atributos essenciais
			//
			resp.setCrawlOnce(false);
			resp.setLastModified(rs.getDate("HIS_DT_INI"));
			try {
				String numero = Long.toString(rs.getLong("NUMERO") + 100000)
						.substring(1);
				resp.setDisplayUrl(new URI(permalink
						+ rs.getString("ACRONIMO_ORGAO_USU") + "GC"
						+ rs.getInt("ANO") + numero));
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}

			// Add Conteudo
			//
			if ("text/html".equals(rs.getString("CONTEUDO_TIPO"))) {
				String html = new String(rs.getBytes("CONTEUDO"), "UTF-8");
				if (html != null) {
					resp.setContentType("text/html");
					resp.getOutputStream().write(html.getBytes());
					return;
				}
			} else {
				resp.respondNotFound();
				return;
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getDtYYYYMMDD(Date dt) {
		if (dt != null) {
			final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			return df.format(dt);
		}
		return "";
	}

	public static void main(String[] args) {
		AbstractAdaptor.main(new GcInformacaoAdaptor(), args);
	}

}