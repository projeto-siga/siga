package br.gov.jfrj.siga.sr.model;

import javax.persistence.EntityManager;

import junit.framework.TestCase;

import org.junit.Test;

import br.gov.jfrj.siga.sr.model.SrAcordo;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.util.TestUtil;

public class SrAcordoTest extends TestCase {

	private EntityManager em;
	
	protected void setUp() throws Exception {
		/*super.setUp();
		TestUtil.setup(em);*/
	}
	
	public void testarAtribuicaoBasicaAcordoSemEscalonamento() throws Exception {
		/*TestUtil.design();
		SrAcordo SLA = TestUtil.SLA();
		
		SrSolicitacao s = new SrSolicitacao();
		s.setCadastrante(TestUtil.funcionarioMenor());
		s.setItemConfiguracao(TestUtil.sigadoc());
		s.setAcao(TestUtil.acaoCriarSoft());
		s.salvarComHistorico();
		
		assertTrue(s.getAcordos().contains(SLA));	*/	
	}
	
	protected void tearDown() throws Exception {
		/*TestUtil.tearDown(em);
		super.tearDown();*/
	}

}
