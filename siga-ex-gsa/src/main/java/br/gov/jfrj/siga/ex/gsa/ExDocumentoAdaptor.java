package br.gov.jfrj.siga.ex.gsa;

//Copyright 2011 Google Inc. All Rights Reserved.
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.cfg.Configuration;

import br.gov.jfrj.siga.cp.bl.CpAmbienteEnumBL;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

import com.google.enterprise.adaptor.AbstractAdaptor;
import com.google.enterprise.adaptor.Adaptor;
import com.google.enterprise.adaptor.AdaptorContext;
import com.google.enterprise.adaptor.Config;
import com.google.enterprise.adaptor.DocId;
import com.google.enterprise.adaptor.DocIdPusher;
import com.google.enterprise.adaptor.Request;
import com.google.enterprise.adaptor.Response;

/**
 * Adaptador Google Search Appliance para documentos do SIGA-DOC.
 * <p>
 * Example command line:
 * <p>
 *
 * /java/bin/java \ -cp
 * adaptor-withlib.jar:adaptor-examples.jar:mysql-5.1.10.jar \
 * br.gov.jfrj.siga.ex.gsa.ExDocumentoAdaptor \ -Dgsa.hostname=myGSA
 * -Dservidor=desenv \ -Djournal.reducedMem=true
 */
public class ExDocumentoAdaptor extends AbstractAdaptor implements Adaptor {
	private static final Logger log = Logger.getLogger(ExDocumentoAdaptor.class
			.getName());
	private Charset encoding = Charset.forName("UTF-8");

	private int maxIdsPerFeedFile;
	private String dbname, tablename;

	@Override
	public void initConfig(Config config) {
		config.addKey("db.name", null);
		config.addKey("db.tablename", null);
	}

	@Override
	public void init(AdaptorContext context) throws Exception {
		Configuration cfg;
		String servidor = context.getConfig().getValue("servidor");
		if (servidor.equals("prod"))
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.PRODUCAO);
		else if (servidor.equals("homolo"))
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.HOMOLOGACAO);
		else if (servidor.equals("treina"))
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.TREINAMENTO);
		else
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.DESENVOLVIMENTO);

		HibernateUtil.configurarHibernate(cfg);
	}

	/** Get all doc ids from database. */
	public void getDocIds(DocIdPusher pusher) throws IOException,
			InterruptedException {
		BufferingPusher outstream = new BufferingPusher(pusher);
		ExDao dao = ExDao.getInstance();
		Query q = dao
				.getSessao()
				.createQuery(
						"select doc.idDoc from ExDocumento doc where doc.dtFinalizacao != null order by doc.idDoc");
		Iterator i = q.iterate();
		while (i.hasNext()) {
			DocId id = new DocId("" + i.next());
			outstream.add(id);
		}
		outstream.forcePush();
	}

	/** Gives the bytes of a document referenced with id. */
	public void getDocContent(Request req, Response resp) throws IOException {
		ExDao dao = ExDao.getInstance();

		DocId id = req.getDocId();
		long primaryKey;
		try {
			primaryKey = Long.parseLong(id.getUniqueId());
		} catch (NumberFormatException nfe) {
			resp.respondNotFound();
			return;
		}
		ExDocumento doc = ExDao.getInstance().consultar(primaryKey,
				ExDocumento.class, false);

		if (doc == null) {
			resp.respondNotFound();
			return;
		}

		resp.setCrawlOnce(true);
		resp.setLastModified(doc.getDtFinalizacao());
		try {
			resp.setDisplayUrl(new URI("http://siga/sigaex/app/exibir?sigla="
					+ doc.getCodigoCompacto()));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		resp.setContentType("application/pdf");
		resp.getOutputStream().write(doc.getPdf());
	}

	public static void main(String[] args) {
		AbstractAdaptor.main(new ExDocumentoAdaptor(), args);
	}

	/**
	 * Mechanism that accepts stream of DocId instances, bufferes them, and
	 * sends them when it has accumulated maximum allowed amount per feed file.
	 */
	private class BufferingPusher {
		DocIdPusher wrapped;
		ArrayList<DocId> saved;

		BufferingPusher(DocIdPusher underlying) {
			wrapped = underlying;
			saved = new ArrayList<DocId>(maxIdsPerFeedFile);
		}

		void add(DocId id) throws InterruptedException {
			saved.add(id);
			if (saved.size() >= maxIdsPerFeedFile) {
				forcePush();
			}
		}

		void forcePush() throws InterruptedException {
			wrapped.pushDocIds(saved);
			log.fine("sent " + saved.size() + " doc ids to pusher");
			saved.clear();
		}

		protected void finalize() throws Throwable {
			if (0 != saved.size()) {
				log.severe("still have saved ids that weren't sent");
			}
		}
	}
}