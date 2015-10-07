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
package br.gov.jfrj.siga;

import java.util.Date;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.bouncycastle.util.encoders.Base64;

public class ServiceTest extends TestCase {
	
	public void testCriarInstanciaDeProcesso() throws Exception {
		if (true)
			return;
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		{ // Adiciona no contexto a via Geral
			keys.add("doc_document");
			values.add("RJ-MEM-2007/00595-A");
		}
		for (int n = 0; n < 3; n++) { // Adiciona no contexto a via 'n'
			keys.add("doc_" + (char) ('a' + n));
			values.add("RJ-MEM-2007/00595" + "-" + (char) ('A' + n));
		}
		Service.getWfService().criarInstanciaDeProcesso("testeNovo",
				"RJ13635@RJSESIE", "RJ13635@RJSESIE", keys, values);
	}

	public void testTransferir() throws Exception {
		if (true)
			return;
		assertTrue(Service.getExService().isAssinado("RJ-MEM-2007/00595-A",
				"RJ13635@RJSESIE"));

		// Service.getExService().transferir("RJ-MEM-2007/00595-A", "@RJSESIE",
		// "RJ13635@RJSESIE");
	}

	public void testExBuscarPorCodigo() throws Exception {
		if (true)
			return;
		String s = Service.getExService().buscarPorCodigo("sec25");
		assertEquals(s, "RJ-SEC-2012/00025");
	}
}
