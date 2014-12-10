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
/*
 * Criado em  01/12/2005
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.gc;

import javax.persistence.EntityManager;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;

import org.hibernate.cfg.Configuration;

import br.com.caelum.vraptor.ioc.Component;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.ModeloDao;

/**
 * Classe que representa o DAO do sistema de workflow.
 * 
 * @author kpf
 * 
 */
@Component
public class GcDao {

	//private EntityManager em = new EntityManagerCreator().getInstance();

    private EntityManager em;
    private CpDao cpDao = null;

    public GcDao(EntityManager em){
        this.em = em;
    }
	
	// public static final String CACHE_GC = "gc";
	//
	// /**
	// * Retorna uma instância do DAO.
	// *
	// * @return
	// */
	// public static GcDao getInstance() {
	// return ModeloDao.getInstance(GcDao.class);
	// }
	//
	// /**
	// * Pesquisa as configurações que são semelhantes ao exemplo
	// *
	// * @param exemplo
	// * Uma configuração de exemplo para a pesquisa.
	// * @return Lista de configurações encontradas.
	// */
	// // public List<GcConfiguracao> consultar(final GcConfiguracao exemplo) {
	// // Query query = getSessao().getNamedQuery("consultarGcConfiguracoes");
	// //
	// // query.setLong("idTpConfiguracao", exemplo.getCpTipoConfiguracao()
	// // .getIdTpConfiguracao());
	// //
	// // query.setCacheable(true);
	// // query.setCacheRegion(CACHE_QUERY_CONFIGURACAO);
	// // return query.list();
	// // }
	//
	// static public Configuration criarHibernateCfg(String datasource)
	// throws Exception {
	// Configuration cfg = CpDao.criarHibernateCfg(datasource);
	//
	// return GcDao.configurarHibernate(cfg);
	// }
	//
	// static private Configuration configurarHibernate(Configuration cfg)
	// throws Exception {
	// // cfg.addClass(br.gov.jfrj.siga.gc.GcConfiguracao.class);
	//
	// cfg.addResource("org/jbpm/db/hibernate.queries.hbm.xml");
	// cfg.addResource("org/jbpm/db/hibernate.types.hbm.xml");
	// cfg.addResource("hibernate.extra.hbm.xml");
	//
	// cfg.addClass(org.jbpm.graph.action.MailAction.class);
	//
	// CacheManager manager = CacheManager.getInstance();
	// Cache cache;
	// CacheConfiguration config;
	//
	// if (!manager.cacheExists(CACHE_GC)) {
	// manager.addCache(CACHE_GC);
	// cache = manager.getCache(CACHE_GC);
	// config = cache.getCacheConfiguration();
	// config.setTimeToIdleSeconds(3600);
	// config.setTimeToLiveSeconds(36000);
	// config.setMaxElementsInMemory(10000);
	// config.setMaxElementsOnDisk(1000000);
	// }
	//
	// return cfg;
	// }
}
