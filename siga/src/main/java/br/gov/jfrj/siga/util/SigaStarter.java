package br.gov.jfrj.siga.util;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.api.v1.SigaApiV1Servlet;
import br.gov.jfrj.siga.base.UsuarioDeSistemaEnum;
import br.gov.jfrj.siga.cp.converter.ITipoDeConfiguracaoConverter;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.cp.util.SigaFlyway;

@Startup
@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
public class SigaStarter {

	private final static org.jboss.logging.Logger log = Logger.getLogger(SigaStarter.class);
	public static EntityManagerFactory emf;

	@PostConstruct
	public void init() {
		log.info("INICIANDO SIGA.WAR");
		CpTipoDeConfiguracao.mapear(CpTipoDeConfiguracao.values());

		try {
			SigaFlyway.migrate("java:/jboss/datasources/SigaCpDS", "classpath:db/mysql/sigacp", false);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
		SigaApiV1Servlet.migrationComplete = true;
		emf = Persistence.createEntityManagerFactory("default");
		Service.setUsuarioDeSistema(UsuarioDeSistemaEnum.SIGA);
	}
}