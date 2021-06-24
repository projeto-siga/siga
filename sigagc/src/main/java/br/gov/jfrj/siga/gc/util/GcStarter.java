package br.gov.jfrj.siga.gc.util;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;

/**
 * 
 * @author Rodrigo Ramalho hodrigohamalho@gmail.com
 *
 */
@ApplicationScoped
public class GcStarter {

	private final static org.jboss.logging.Logger log = Logger.getLogger(GcStarter.class);
	public static EntityManagerFactory emf;

	@PostConstruct
	public void init() {
		log.info("INICIANDO SIGAWF.WAR");
		CpTipoDeConfiguracao.mapear(CpTipoDeConfiguracao.values());

		emf = Persistence.createEntityManagerFactory("default");
	}
}
