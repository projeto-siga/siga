package br.gov.jfrj.siga.util;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import br.gov.jfrj.siga.model.ContextoPersistencia;

@Startup
@Singleton
public class SigaStarter {

	@PostConstruct
	public void init() {
		ContextoPersistencia.initEntityManagerFactory("default");
	}
}