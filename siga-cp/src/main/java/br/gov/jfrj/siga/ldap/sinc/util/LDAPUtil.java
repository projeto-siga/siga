package br.gov.jfrj.siga.ldap.sinc.util;

import java.util.List;

import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.ldap.AdObjeto;
import br.gov.jfrj.siga.ldap.AdUsuario;
import br.gov.jfrj.siga.ldap.sinc.LdapBL;
import br.gov.jfrj.siga.ldap.sinc.SincProperties;

public class LDAPUtil {

	private static LdapBL ldap;
//	private static final SincProperties conf = SincProperties.getInstancia();

	public static void main(String[] args) {
//		removerItemCampoMultivalorado("proxyAddresses","@corp.jfrj.gov.br");
//		definirAtributoParaTodosUsuarios("msExchRecipientDisplayType","1073741824");
//		formartarDisplayNamesParaTodosUsuarios();
	}

//	private static void removerItemCampoMultivalorado(String atributoMV,
//			String contendoValor) {
//		try{
//			SincProperties conf = new SincProperties("siga.cp.sinc.ldap.jfrj.prod");
//			ldap = LdapBL.getInstance(conf);
//			List<AdObjeto> listaAD = ldap.pesquisarObjeto(conf.getDnGestaoIdentidade());
//			for (AdObjeto adObjeto : listaAD) {
//				if (adObjeto instanceof AdUsuario) {
//					ldap.removerItemCampoMultivalorado(adObjeto.getNomeCompleto(), atributoMV, contendoValor,"smtp:" +((AdUsuario)adObjeto).getSigla()+contendoValor);
//				}
//			}
//		}catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	public static void definirAtributoParaTodosUsuarios(String atributo, Object valor) {
//		try {
//			SincProperties conf = new SincProperties("siga.cp.sinc.ldap.jfrj.prod");
//			ldap = LdapBL.getInstance(conf);
//			List<AdObjeto> listaAD = ldap.pesquisarObjetoSemIgnorarNada(conf.getDnGestaoIdentidade());
//			for (AdObjeto adObjeto : listaAD ) {
//				if (adObjeto instanceof AdUsuario) {
//					
//					try{
//						System.out.print("Definindo " + atributo + " com " + valor + " para " + adObjeto.getNomeCompleto() + "...");
//						ldap.alterarAtributo(adObjeto.getNomeCompleto(), atributo, valor);
//						System.out.println("OK!");
//					}catch (Exception e) {
//						System.out.println("...FALHOU!");
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}
//	
//	public static void formartarDisplayNamesParaTodosUsuarios(){
//		try {
//			SincProperties conf = new SincProperties("siga.cp.sinc.ldap.jfrj.prod");
//			ldap = LdapBL.getInstance(conf);
//			List<AdObjeto> listaAD = ldap.pesquisarObjetoSemIgnorarNada(conf.getDnGestaoIdentidade());
//			for (AdObjeto adObjeto : listaAD ) {
//				if (adObjeto instanceof AdUsuario) {
//					try{
//						String displayNameOriginal = ldap.getAtributo(adObjeto.getNomeCompleto(), "displayName");
//						String displayNameNovo = Texto.inciaisMaiuscula(displayNameOriginal);
//						System.out.print("Definindo dysplayName de [" + displayNameOriginal + "] para [" + displayNameNovo + "]...");
//						ldap.alterarAtributo(adObjeto.getNomeCompleto(), "displayName", displayNameNovo);
//						System.out.println("OK!");
//					}catch (Exception e) {
//						System.out.println("...FALHOU!");
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//
//	}
}
