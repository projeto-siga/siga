package br.gov.jfrj.siga.model;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;

@RequestScoped
public class EntityManagerProducer {
	@Produces
	@RequestScoped
	public EntityManager getInstance() {
		return ContextoPersistencia.em();
	}

}
