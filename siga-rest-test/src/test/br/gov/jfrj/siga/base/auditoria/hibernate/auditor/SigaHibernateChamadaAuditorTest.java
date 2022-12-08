package br.gov.jfrj.siga.base.auditoria.hibernate.auditor;

import org.hibernate.cfg.Configuration;
import org.junit.Test;

import br.gov.jfrj.siga.base.auditoria.hibernate.auditor.SigaHibernateChamadaAuditor;
import static org.junit.Assert.*;

public class SigaHibernateChamadaAuditorTest {
	
	@Test
	public void deveRetornarNotNullAoConfigurarListenersDeAuditoria() throws Exception {
		Configuration cfg = new Configuration();
		assertNotNull( new SigaHibernateChamadaAuditor( cfg ).configuraListenersDeAuditoria() );
	}

}
