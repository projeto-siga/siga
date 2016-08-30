package br.gov.jfrj.siga.model.prop.test;

import junit.framework.TestCase;
import br.gov.jfrj.siga.model.prop.VarredorPropriedades;

public class VarredorPropriedadesTest extends TestCase{

	public void testVarredura(){
		String prefixo[] = new String[]{"siga","cp","sinc","ldap"};
		String nomePropriedade = "servidor";
		VarredorPropriedades v = new VarredorPropriedades(prefixo,nomePropriedade);
		assertEquals("siga.cp.sinc.ldap.servidor", v.getNext());
		assertEquals("cp.sinc.ldap.servidor",v.getNext());
		assertEquals("sinc.ldap.servidor",v.getNext());
		assertEquals("ldap.servidor",v.getNext());
		assertEquals("servidor",v.getNext());
		assertEquals(null, v.getNext());
		
		v.reset();

		assertEquals("siga.cp.sinc.ldap.servidor",v.getNext());
		assertEquals(false, v.isVarreduraCompleta());
		assertEquals("cp.sinc.ldap.servidor",v.getNext());
		assertEquals(false, v.isVarreduraCompleta());
		assertEquals("sinc.ldap.servidor",v.getNext());
		assertEquals(false, v.isVarreduraCompleta());
		assertEquals("ldap.servidor",v.getNext());
		assertEquals(false, v.isVarreduraCompleta());
		assertEquals("servidor",v.getNext());
		assertEquals(null, v.getNext());
		
		assertEquals(true, v.isVarreduraCompleta());
		
		v.reset();

		assertEquals(0, v.getIndiceAtual());
		assertEquals("siga.cp.sinc.ldap.servidor",v.getNext());
		assertEquals(1, v.getIndiceAtual());
		assertEquals("cp.sinc.ldap.servidor",v.getNext());
		assertEquals(2, v.getIndiceAtual());
		assertEquals("sinc.ldap.servidor",v.getNext());
		assertEquals(3, v.getIndiceAtual());
		assertEquals("ldap.servidor",v.getNext());
		assertEquals(4, v.getIndiceAtual());
		assertEquals("servidor",v.getNext());
		assertEquals(5, v.getIndiceAtual());
		assertEquals(null, v.getNext());
	}
}
