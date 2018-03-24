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
package br.gov.jfrj.siga.ex.elastic;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.cfg.Configuration;

import br.gov.jfrj.siga.cp.bl.CpAmbienteEnumBL;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

/**
 * Adaptador Google Search Appliance para movimentações do SIGA-DOC.
 * <p>
 * Example command line:
 * <p>
 *
 * java \ -jar siga-ex-gsa.one-jar -Dgsa.hostname=myGSA -Dservidor=desenv \
 * -Djournal.reducedMem=true
 */
public abstract class ExAdaptor {
	protected static final Logger log = Logger.getLogger(ExAdaptor.class
			.getName());
	protected Charset encoding = Charset.forName("UTF-8");
	protected Date dateLastUpdated;
	protected String permalink;
	protected Properties adaptorProperties;
	static final String DEFAULT_CONFIG_FILE = "adaptor-config.properties";

	public void init() {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream("siga.properties"));
			for (Object key : props.keySet()) {
				if (key instanceof String)
					System.setProperty((String) key,
							props.getProperty((String) key));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Configuration cfg;
		String servidor = adaptorProperties.getProperty("servidor");
		permalink = adaptorProperties.getProperty("url.permalink");
		try {
			if (servidor.equals("prod")) {
				cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.PRODUCAO);
			} else if (servidor.equals("homolo")) {
				cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.HOMOLOGACAO);
			} else if (servidor.equals("treina")) {
				cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.TREINAMENTO);
			} else {
				cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.DESENVOLVIMENTO);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		HibernateUtil.configurarHibernate(cfg);
	}

	/**
	 * Get all doc ids from database.
	 **/
	public void pushAllItems() throws IOException, InterruptedException {
		this.dateLastUpdated = ExDao.getInstance().dt();
		pushItems(new Date(0L));
	}

	public void getModifiedItems() throws IOException, InterruptedException {
		String path = getLastModifiedFileName();
		Date dt = null;
		try {
			dt = ExDao.getInstance().dt();
			this.dateLastUpdated = getLastModified(path, dt);
			pushItems(this.dateLastUpdated);
			saveLastModified(path, this.dateLastUpdated);
		} finally {
			ExDao.freeInstance();
		}
	}

	protected void pushItems(Date date) throws InterruptedException {
		List<Long> l = new ArrayList<>();
		try {
			ExDao dao = ExDao.getInstance();
			Query q = dao.getSessao().createQuery(getIdsHql());
			q.setDate("dt", date);
			@SuppressWarnings("rawtypes")
			Iterator i = q.iterate();
			while (i.hasNext()) {
				Long id = (Long) i.next();
				l.add(id);
			}
		} finally {
			ExDao.freeInstance();
		}

		for (long id : l) {
			Response resp = new Response();
			try {
				pushItem(id, resp);
				System.out.println(resp.toJson());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public abstract String getIdsHql();

	public abstract void pushItem(Long id, Response resp) throws IOException;

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
				log.warning("Arquivo de propriedades " + DEFAULT_CONFIG_FILE
						+ "não encontrado!");
				log.warning("Propriedades do adaptor do Siga não serão carregadas.");
			} catch (IOException e) {
				log.warning("Não foi possível carregar as propriedades do arquivo "
						+ DEFAULT_CONFIG_FILE);
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

	protected abstract String getLastModifiedFileName();

	/**
	 * Obtém a data da última execução, caso o arquivo ou a data não existam,
	 * salva a data informada por parâmetro e a define como a data da última
	 * execução.
	 * 
	 * @param lastModifiedDefault
	 * @param path
	 */
	protected Date getLastModified(String path, Date lastModifiedDefault) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		File lastModifiedFile = new File(path);
		if (lastModifiedFile.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(
					lastModifiedFile))) {
				String line;
				while ((line = br.readLine()) != null) {
					log.fine("A data da última  atualização é " + line);
					return dateFormat.parse(line);
				}
				throw new RuntimeException(
						"Não foi possível obter a data da última atualização");
			} catch (Exception e) {
				log.severe("Erro ao obter a data das ultimas alterações!");
				throw new RuntimeException(e);
			}
		} else {
			String dateToSave = dateFormat.format(lastModifiedDefault);
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
			log.fine("A data da última atualização é " + dateToSave);
			return lastModifiedDefault;
		}

	}

	/**
	 * Salva data no arquivo informado
	 * 
	 * @param lastModified
	 * @param path
	 */
	protected void saveLastModified(String path, Date lastModified) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
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

	public static void run(ExMovimentacaoAdaptor exMovimentacaoAdaptor,
			String[] args) {
		// TODO Auto-generated method stub
	}

}