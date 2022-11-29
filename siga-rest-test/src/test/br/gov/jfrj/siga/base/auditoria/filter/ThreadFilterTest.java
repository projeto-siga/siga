package br.gov.jfrj.siga.base.auditoria.filter;

import static junit.framework.Assert.*;

import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import br.gov.jfrj.siga.base.AplicacaoException;

public class ThreadFilterTest {

	private ThreadFilter threadFilter;

	@Before
	public void setUp() {
		this.threadFilter = new ThreadFilter() {
		};
	}

	@Test
	public void deveRetornarActionSemContexto() throws Exception {
		String uri = "/siga/principal.action";
		String contexto = "/siga";
		String actionEsperada = "principal.action";
		String actionResultado = this.threadFilter.getAction(uri, contexto);

		assertEquals(actionEsperada, actionResultado);
	}

	@Test
	public void deveRetornarActionSemContextoRemovendoOContextoSomenteDoInicioDaString()
			throws Exception {
		String uri = "/siga/principal.action/siga";
		String contexto = "/siga";
		String actionEsperada = "principal.action/siga";
		String actionResultado = this.threadFilter.getAction(uri, contexto);

		assertEquals(actionEsperada, actionResultado);
	}

	@Test
	public void deveRetornarMensagemDaCausaEMensagemDaAplicacaoExceptionQuandoAplicacaoExceptionEncapsularException()
			throws Exception {

		String msgCausa = "causa";
		Exception ex = new Exception(msgCausa);

		String msgAplicacaoException = "aplicacao exception";
		AplicacaoException aex = new AplicacaoException(msgAplicacaoException,
				0, ex);

		String retorno = this.threadFilter.montaMensagemErroExcecoes(aex);

		String msgEsperada = msgAplicacaoException + " Causa: " + msgCausa;

		assertEquals(msgEsperada, retorno);
	}

	@Test
	public void deveRetornarSomenteMensagemDaCasuaQuandoSomenteACausaForLancada()
			throws Exception {

		String msgCausa = "causa";
		Exception ex = new Exception(msgCausa);

		String retorno = this.threadFilter.montaMensagemErroExcecoes(ex);
		assertEquals(msgCausa, retorno);
	}

	@Test
	public void deveRetornarSomenteMensagemQuandoSomenteAplicacaoExceptionForLancada()
			throws Exception {

		String msgAplicacaoException = "aplicacao exception";
		AplicacaoException ex = new AplicacaoException(msgAplicacaoException);

		String retorno = this.threadFilter.montaMensagemErroExcecoes(ex);
		assertEquals(msgAplicacaoException, retorno);
	}

	@Test
	public void deveRetornarStringVaziaQuandoExcecaoForNula() throws Exception {
		String retorno = this.threadFilter.montaMensagemErroExcecoes(null);
		assertEquals("", retorno);
	}

	@Test
	public void naoDeveAdicionarTransactionFactoryClassSeAClasseNaoForDeclaradaNoSigaProperties() throws Exception {
		
		Configuration cfg = new Configuration();
		ThreadFilter tf = new ThreadFilter(){};
		tf.registerTransactionClasses( cfg );
		
		assertNull( cfg.getProperty( "hibernate.transaction.factory_class" ) );
	}
	
	@Test
	public void deveAdicionarTransactionFactoryClassSeAClasseForDeclaradaNoSigaProperties() throws Exception {
		
		System.setProperty( "hibernate.transaction.factory_class", "org.hibernate.transaction.JTATransactionFactory" );
		
		Configuration cfg = new Configuration();
		ThreadFilter tf = new ThreadFilter(){};
		tf.registerTransactionClasses( cfg );
		
		assertNotNull( cfg.getProperty( "hibernate.transaction.factory_class" ) );
	}
	
	@Test
	public void naoDeveAdicionarTransactionManagerLookupClassSeAClasseNaoForDeclaradaNoSigaProperties() throws Exception {
		
		Configuration cfg = new Configuration();
		ThreadFilter tf = new ThreadFilter(){};
		tf.registerTransactionClasses( cfg );
		
		assertNull( cfg.getProperty( "hibernate.transaction.manager_lookup_class" ) );
	}
	
	@Test
	public void deveAdicionarTransactionManagerLookupClassSeAClasseForDeclaradaNoSigaProperties() throws Exception {
		
		System.setProperty( "hibernate.transaction.manager_lookup_class", "org.hibernate.transaction.JBossTransactionManagerLookup" );
		
		Configuration cfg = new Configuration();
		ThreadFilter tf = new ThreadFilter(){};
		tf.registerTransactionClasses( cfg );
		
		assertNotNull( cfg.getProperty( "hibernate.transaction.manager_lookup_class" ) );
	}
	
	
}
