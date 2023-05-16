package br.gov.jfrj.siga.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import br.com.caelum.vraptor.jpa.EntityManagerFactoryCreator;

public class SigaEntityManagerFactoryCreator extends EntityManagerFactoryCreator {

    @Inject
    private SigaEntityManagerFactory jemf;

    @ApplicationScoped
    @Specializes
    @Produces
    public EntityManagerFactory getEntityManagerFactory() {
        return jemf.getEntityManagerFactory();
    }

    public void destroy(@Disposes EntityManagerFactory factory) {
        if (factory.isOpen()) {
            factory.close();
        }
    }
}
