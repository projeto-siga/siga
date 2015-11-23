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
package br.gov.jfrj.siga.ex.gsa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.cfg.Configuration;

import com.google.enterprise.adaptor.AbstractAdaptor;
import com.google.enterprise.adaptor.Adaptor;
import com.google.enterprise.adaptor.AdaptorContext;
import com.google.enterprise.adaptor.DocId;
import com.google.enterprise.adaptor.DocIdPusher;
import com.google.enterprise.adaptor.PollingIncrementalLister;
import com.google.enterprise.adaptor.Response;

import br.gov.jfrj.siga.cp.bl.CpAmbienteEnumBL;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

/**
 * Adaptador Google Search Appliance para movimentações do SIGA-DOC.
 * <p>
 * Example command line:
 * <p>
 *
 * java \ -jar siga-ex-gsa.one-jar
 * -Dgsa.hostname=myGSA -Dservidor=desenv \ -Djournal.reducedMem=true
 */
public abstract class ExAdaptor extends AbstractAdaptor implements Adaptor, PollingIncrementalLister {
	protected static final Logger log = Logger.getLogger(ExAdaptor.class.getName());
	protected Charset encoding = Charset.forName("UTF-8");
	protected Date dateLastUpdated;
	protected String permalink;
	protected Properties adaptorProperties;
	static final String DEFAULT_CONFIG_FILE = "adaptor-config.properties";

	@Override
	public void init(AdaptorContext context) throws Exception {
		Configuration cfg;
		String servidor = context.getConfig().getValue("servidor");
		permalink = context.getConfig().getValue("url.permalink");
		if (servidor.equals("prod")){
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.PRODUCAO);
		}			
		else if (servidor.equals("homolo")){
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.HOMOLOGACAO);
		}			
		else if (servidor.equals("treina")){
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.TREINAMENTO);
		}			
		else{
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.DESENVOLVIMENTO);
		}
		HibernateUtil.configurarHibernate(cfg);
		context.setPollingIncrementalLister(this);
	}

	/** 
	 * Get all doc ids from database. 
	 **/
	public void getDocIds(DocIdPusher pusher) throws IOException,
	InterruptedException {
		this.dateLastUpdated = ExDao.getInstance().dt();
		pushDocIds(pusher, new Date(0L));
	}

	@Override
	public void getModifiedDocIds(DocIdPusher pusher) throws IOException,
	InterruptedException {
		try {
			Date dt = ExDao.getInstance().dt();
			pushDocIds(pusher, this.dateLastUpdated);
			this.dateLastUpdated = dt;
		} finally {
			ExDao.freeInstance();
		}
	}

	private void pushDocIds(DocIdPusher pusher, Date date)
			throws InterruptedException {
		try {
			BufferingPusher outstream = new BufferingPusher(pusher);
			ExDao dao = ExDao.getInstance();
			Query q = dao.getSessao().createQuery(getIdsHql());
			q.setDate("dt", date);
			//q.setMaxResults(500000);// propriedade pode ser utilizada em desenvolvimento para limitar o numero de registros.
			@SuppressWarnings("rawtypes")
			Iterator i = q.iterate();
			while (i.hasNext()) {
				DocId id = new DocId("" + i.next());
				outstream.add(id);
			}
			outstream.forcePush();
		} finally {
			ExDao.freeInstance();
		}
	}

	public abstract String getIdsHql();
	
	public void addMetadata(Response resp, String title, String value) {
		if (value == null)
			return;
		value = value.trim();
		resp.addMetadata(title, value);
	}

	protected void loadSigaAllProperties(){
		if(null == adaptorProperties){
			InputStream propStream = null;
			try {
				propStream = new FileInputStream(new File(DEFAULT_CONFIG_FILE));
				this.adaptorProperties = new Properties();
				this.adaptorProperties.load(propStream);
			} catch (FileNotFoundException e) {
				log.warning("Arquivo de propriedades "+DEFAULT_CONFIG_FILE+"não encontrado!");
				log.warning("Propriedades do adaptor do Siga não serão carregadas.");
			} catch (IOException e) {
				log.warning("Não foi possível carregar as propriedades do arquivo "+DEFAULT_CONFIG_FILE);
				log.warning("Propriedades do adaptor do Siga não serão carregadas.");
			}finally {
				if(propStream != null){
					try {
						propStream.close();
					} catch (IOException e) {
						log.severe("Erro ao finalizar Stream!");
					}
				}
			}
		}
	}

}