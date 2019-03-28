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

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.cfg.Configuration;

import br.gov.jfrj.siga.cp.bl.CpAmbienteEnumBL;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

import com.google.enterprise.adaptor.AbstractAdaptor;
import com.google.enterprise.adaptor.Adaptor;
import com.google.enterprise.adaptor.AdaptorContext;
import com.google.enterprise.adaptor.DocId;
import com.google.enterprise.adaptor.DocIdPusher;
import com.google.enterprise.adaptor.PollingIncrementalLister;
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
		if (servidor.equals("prod")) {
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.PRODUCAO);
		} else if (servidor.equals("homolo")) {
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.HOMOLOGACAO);
		} else if (servidor.equals("treina")) {
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.TREINAMENTO);
		} else {
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.DESENVOLVIMENTO);
		}

		// Desabilitado para evitar o erro de compilação depois que foi feita a
		// troca do Hibernate para o JPA
		// HibernateUtil.configurarHibernate(cfg);
		context.setPollingIncrementalLister(this);
	}

	/**
	 * Get all doc ids from database.
	 **/
	public void getDocIds(DocIdPusher pusher) throws IOException, InterruptedException {
		this.dateLastUpdated = ExDao.getInstance().dt();
		pushDocIds(pusher, new Date(0L));
	}

	protected void pushDocIds(DocIdPusher pusher, Date date) throws InterruptedException {
		try {
			BufferingPusher outstream = new BufferingPusher(pusher);
			ExDao dao = ExDao.getInstance();
			Query q = dao.getSessao().createQuery(getIdsHql());
			q.setDate("dt", date);
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
		value = escapeHtml(value);
		resp.addMetadata(title, value);
	}

	protected void loadSigaAllProperties() {
		if (null == adaptorProperties) {
			InputStream propStream = null;
			try {
				propStream = new FileInputStream(new File(DEFAULT_CONFIG_FILE));
				this.adaptorProperties = new Properties();
				this.adaptorProperties.load(propStream);
			} catch (FileNotFoundException e) {
				log.warning("Arquivo de propriedades " + DEFAULT_CONFIG_FILE + "não encontrado!");
				log.warning("Propriedades do adaptor do Siga não serão carregadas.");
			} catch (IOException e) {
				log.warning("Não foi possível carregar as propriedades do arquivo " + DEFAULT_CONFIG_FILE);
				log.warning("Propriedades do adaptor do Siga não serão carregadas.");
			} finally {
				if (propStream != null) {
					try {
						propStream.close();
					} catch (IOException e) {
						log.severe("Erro ao finalizar Stream!");
					}
				}
			}
		}
	}

	/**
	 * Obtém a data da última execução, caso o arquivo ou a data não existam,
	 * salva a data informada por parâmetro e a define como a data da última
	 * execução.
	 * 
	 * @param lastModified
	 * @param path
	 */
	protected void getLastModified(Date lastModified, String path) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		File lastModifiedFile = new File(path);
		if (lastModifiedFile.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(lastModifiedFile))) {
				String line;
				while ((line = br.readLine()) != null) {
					this.dateLastUpdated = dateFormat.parse(line);
					log.fine("A data da última  atualização é " + line);
					break;
				}
			} catch (Exception e) {
				log.severe("Erro ao obter a data das ultimas alterações!");
			}
		} else {
			String dateToSave = dateFormat.format(lastModified);
			try {
				FileOutputStream output = new FileOutputStream(lastModifiedFile);
				output.write(dateToSave.getBytes());
				output.flush();
				output.close();
			} catch (FileNotFoundException e) {
				log.severe("Erro salvando arquivo no disco!");
				log.info("verifique suas permissões e configurações");
			} catch (IOException e) {
				log.severe("Erro ao escrever no arquivo!");
				log.severe("Erro: " + e.getMessage());
			}
			this.dateLastUpdated = lastModified;
			log.fine("A data da última  atualização é " + dateToSave);
		}

	}

	/**
	 * Salva data no arquivo informado
	 * 
	 * @param lastModified
	 * @param path
	 */
	protected void saveLastModified(Date lastModified, String path) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		File lastModifiedFile = new File(path);
		if (lastModifiedFile.exists()) {
			lastModifiedFile.delete();
		}
		String dateToSave = dateFormat.format(lastModified);
		try {
			FileOutputStream output = new FileOutputStream(lastModifiedFile);
			output.write(dateToSave.getBytes());
			output.flush();
			output.close();
		} catch (FileNotFoundException e) {
			log.severe("Erro salvando arquivo no disco!");
			log.info("verifique suas permissões e configurações");
		} catch (IOException e) {
			log.severe("Erro ao escrever no arquivo!");
			log.severe("Erro: " + e.getMessage());
		}
	}

}