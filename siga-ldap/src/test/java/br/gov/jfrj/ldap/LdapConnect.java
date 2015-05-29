package br.gov.jfrj.ldap;

public class LdapConnect {
	
	public static void main(String[] args) {
		System.out.println("Use os seguintes argumentos: servidor porta usuario senha caminhoKeystore");
		LdapDaoImpl ldap = new LdapDaoImpl(true);
		try{
			ldap.conectarComSSL(args[0], args[1], args[2], args[3], args[4]);	
		}catch (Exception e) {
			e.printStackTrace();
			return;
		}
		System.out.println("OK!");
	}
}
