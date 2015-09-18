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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.cfg.Configuration;

import br.gov.jfrj.siga.cp.bl.CpAmbienteEnumBL;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

import com.google.enterprise.adaptor.AbstractAdaptor;
import com.google.enterprise.adaptor.Acl;
import com.google.enterprise.adaptor.Adaptor;
import com.google.enterprise.adaptor.AdaptorContext;
import com.google.enterprise.adaptor.Config;
import com.google.enterprise.adaptor.DocId;
import com.google.enterprise.adaptor.DocIdPusher;
import com.google.enterprise.adaptor.GroupPrincipal;
import com.google.enterprise.adaptor.PollingIncrementalLister;
import com.google.enterprise.adaptor.Request;
import com.google.enterprise.adaptor.Response;

/**
 * Adaptador Google Search Appliance para movimentações do SIGA-DOC.
 * <p>
 * Example command line:
 * <p>
 *
 * /java/bin/java \ -cp
 * adaptor-withlib.jar:adaptor-examples.jar:mysql-5.1.10.jar \
 * br.gov.jfrj.siga.ex.gsa.ExMovimentacaoAdaptor \ -Dgsa.hostname=myGSA
 * -Dservidor=desenv \ -Djournal.reducedMem=true
 */
public abstract class ExAdaptor extends AbstractAdaptor implements Adaptor,
		PollingIncrementalLister {
	protected static final Logger log = Logger.getLogger(ExAdaptor.class
			.getName());
	protected Charset encoding = Charset.forName("UTF-8");

	protected Date dateLastUpdated;
	protected String permalink;

	@Override
	public void initConfig(Config config) {
		config.overrideKey("feed.name", getFeedName());
		String serverPort = "6677";
		String serverDashboardPort = "5677";
		config.overrideKey("server.port", Integer.toString((Integer
				.parseInt(serverPort) + getServerPortIncrement())));
		config.overrideKey("server.dashboardPort", Integer.toString((Integer
				.parseInt(serverDashboardPort) + getServerPortIncrement())));
	}

	@Override
	public void init(AdaptorContext context) throws Exception {
		Configuration cfg;
		String servidor = context.getConfig().getValue("servidor");
		permalink = context.getConfig().getValue("url.permalink");
		if (servidor.equals("prod"))
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.PRODUCAO);
		else if (servidor.equals("homolo"))
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.HOMOLOGACAO);
		else if (servidor.equals("treina"))
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.TREINAMENTO);
		else
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.DESENVOLVIMENTO);

		HibernateUtil.configurarHibernate(cfg);
		context.setPollingIncrementalLister(this);
	}

	/** Get all doc ids from database. */
	public void getDocIds(DocIdPusher pusher) throws IOException,
			InterruptedException {
		this.dateLastUpdated = ExDao.getInstance().dt();
		pushDocIds(pusher, new Date(0L));
	}

	@Override
	public void getModifiedDocIds(DocIdPusher pusher) throws IOException,
			InterruptedException {
		Date dt = ExDao.getInstance().dt();
		pushDocIds(pusher, this.dateLastUpdated);
		this.dateLastUpdated = dt;
	}

	private void pushDocIds(DocIdPusher pusher, Date date)
			throws InterruptedException {
		BufferingPusher outstream = new BufferingPusher(pusher);
		ExDao dao = ExDao.getInstance();
		Query q = dao.getSessao().createQuery(getIdsHql());
		q.setDate("dt", date);
		q.setMaxResults(2000);
		Iterator i = q.iterate();
		while (i.hasNext()) {
			DocId id = new DocId("" + i.next());
			outstream.add(id);
		}
		outstream.forcePush();
	}

	public abstract String getIdsHql();

	public abstract String getFeedName();

	public abstract int getServerPortIncrement();
}