package br.gov.jfrj.siga.ldap;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.directory.InitialDirContext;

public class SigaLDAP {

	public static boolean authenticateJndi(String username, String password, String url, String domain) {
		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		props.put(Context.SECURITY_AUTHENTICATION, "simple");
		props.put(Context.PROVIDER_URL, url);
		props.put(Context.SECURITY_PRINCIPAL, domain + "\\" + username);
		props.put(Context.SECURITY_CREDENTIALS, password);

		try {
			InitialDirContext context = new InitialDirContext(props);
			return true; // User authenticated
		} catch (Exception e) {
			return false; // User could NOT be authenticated
		} // finally { }
	}

}
