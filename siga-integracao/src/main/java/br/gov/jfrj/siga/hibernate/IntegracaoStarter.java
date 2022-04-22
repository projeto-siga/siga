package br.gov.jfrj.siga.hibernate;

import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Startup
@Singleton
@ApplicationScoped
public class IntegracaoStarter {

    private final static Logger log = Logger.getLogger(IntegracaoStarter.class);
    public static EntityManagerFactory emf;

    @PostConstruct
    public void init() {
        log.info("INICIANDO SIGA-INTEGRACAO.WAR");
        CpTipoDeConfiguracao.mapear(CpTipoDeConfiguracao.values());

        emf = Persistence.createEntityManagerFactory("default");
    }

}
