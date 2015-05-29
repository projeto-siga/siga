/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.model.prop.test;

import java.util.ArrayList;

import junit.framework.TestCase;
import br.gov.jfrj.siga.model.prop.test.propriedades1.Propriedades1;
import br.gov.jfrj.siga.model.prop.test.propriedades2.Propriedades2;
import br.gov.jfrj.siga.model.prop.test.propriedades3.Propriedades3;

public class PropriedadesBaseTest extends TestCase {

	public void testObterPropriedade() throws Exception {
		/*
		 * Hierarquia Propriedades1 -> Propriedades2 -> Propriedades3
		 */
		Propriedades1 prp1 = new Propriedades1();
		assertEquals("prop1", prp1.obterPropriedade("soTemNaUm"));
		assertNull(prp1.obterPropriedade("soTemNaDois"));
		assertEquals("prop1", prp1.obterPropriedade("temNaUmEDois"));
		assertNull(prp1.obterPropriedade("soTemNaTres"));
		assertEquals("prop1",prp1.obterPropriedade("temNaUmEDoisETres"));
		assertNull(prp1.obterPropriedade("NaoTemNaUmNemDoisNemTres"));
		ArrayList<String> lst1 = prp1.obterPropriedadeLista("paramlista");
		Object [] arr1 =  lst1.toArray();
		assertEquals("zeroNoUm",(String) arr1[0]);
		assertEquals("umNoUm",(String) arr1[1]);
		assertEquals("doisNoUm",(String) arr1[2]);
		assertEquals("trêsNoUm",(String) arr1[3]); 
		//
		Propriedades2 prp2 = new Propriedades2();
		assertEquals("prop1",prp2.obterPropriedade("soTemNaUm"));
		assertEquals("prop2",prp2.obterPropriedade("soTemNaDois"));
		assertEquals("prop2",prp2.obterPropriedade("temNaUmEDois"));
		assertNull(prp2.obterPropriedade("soTemNaTres"));
		assertEquals("prop2",prp2.obterPropriedade("temNaUmEDoisETres"));
		assertNull(prp2.obterPropriedade("NaoTemNaUmNemDoisNemTres"));
		ArrayList<String> lst2 = prp2.obterPropriedadeLista("paramlista");
		Object [] arr2 =  lst2.toArray();
		assertEquals("zeroNoUm",(String) arr2[0]);
		assertEquals("umNoUm",(String) arr2[1]);
		assertEquals("doisNoDois",(String) arr2[2]);
		assertEquals("trêsNoDois",(String) arr2[3]); 
		//
		Propriedades3 prp3 = new Propriedades3();
		assertEquals("prop1",prp3.obterPropriedade("soTemNaUm"));
		assertEquals("prop2",prp3.obterPropriedade("soTemNaDois"));
		assertEquals("prop2",prp3.obterPropriedade("temNaUmEDois"));
		assertEquals("prop3",prp3.obterPropriedade("soTemNaTres"));
		assertEquals("prop3",prp3.obterPropriedade("temNaUmEDoisETres"));
		assertNull(prp3.obterPropriedade("NaoTemNaUmNemDoisNemTres"));
		assertEquals("",prp3.obterPropriedade("vazia"));
		ArrayList<String> lst3 = prp3.obterPropriedadeLista("paramlista");
		Object [] arr3 =  lst3.toArray();
		assertEquals("zeroNoUm",(String) arr3[0]);
		assertEquals("umNoUm",(String) arr3[1]);
		assertEquals("doisNoDois",(String) arr3[2]);
		assertEquals("trêsNoTrês",(String) arr3[3]);
		assertEquals("quatroNoTrês",(String) arr3[4]);
		prp3.setPrefixo("aaaa");
		System.out.println("->" + prp3.getPrefixo());
	}

}
