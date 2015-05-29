package br.gov.jfrj.siga.jee;

import java.security.Security;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Listener criado para adicionar o SecurityProvider do BouncyCastle aos Providers de Segurança da JVM.
 * As bibliotecas do BouncyCastle devem estar na pasta ${JBOSS_HOME}/server/[PROFILE]/lib
 * 
 * @author bruno.lacerda@avantiprima.com.br
 *
 */
public class BouncyCastleServletContextListener implements ServletContextListener {
	
	private BouncyCastleProvider bouncyCastleProvider;

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		this.bouncyCastleProvider = (BouncyCastleProvider) Security.getProvider( BouncyCastleProvider.PROVIDER_NAME );
		if ( this.bouncyCastleProvider == null ) {
			this.bouncyCastleProvider = new BouncyCastleProvider();
			Security.addProvider( this.bouncyCastleProvider );
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if ( this.bouncyCastleProvider != null ) {
			Security.removeProvider( this.bouncyCastleProvider.getName() );
		}
	}

}
