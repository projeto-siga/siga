package br.gov.jfrj.siga.base.auditoria.filter;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class ThreadFilterTest {
	
	private ThreadFilter threadFilter;
	
	@Before
	public void setUp(){
		this.threadFilter = new TestThreadFilter();
	}

	@Test
	public void deveRetornarActionSemContexto() throws Exception {
		String uri = "/siga/principal.action";
		String contexto = "/siga";
		String actionEsperada = "principal.action";
		String actionResultado = this.threadFilter.getAction(uri, contexto);
		
		assertEquals( actionEsperada, actionResultado);
	}
	
	@Test
	public void deveRetornarActionSemContextoRemovendoOContextoSomenteDoInicioDaString() throws Exception {
		String uri = "/siga/principal.action/siga";
		String contexto = "/siga";
		String actionEsperada = "principal.action/siga";
		String actionResultado = this.threadFilter.getAction(uri, contexto);
		
		assertEquals( actionEsperada, actionResultado);
	}
	
	class TestThreadFilter extends ThreadFilter {}
	
}
