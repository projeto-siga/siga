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
package br.gov.jfrj.siga.ex.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

import br.gov.jfrj.siga.ex.bl.ExParte;
import br.gov.jfrj.siga.ex.bl.ExPartes;

public class DocumentoColaborativoTest extends TestCase {

	static final String PARTE1 = "<parte id=\"pedido\" hash=\"hash\" preenchido=\"true\"></parte>";
	static final String PARTE2 = "<parte id=\"diarias\" hash=\"hash\" preenchido=\"false\"><depende id=\"pedido\" hash=\"hash\"/></parte>";
	static final String PARTE3 = "<parte id=\"transporte\" hash=\"hash\" preenchido=\"false\"><depende id=\"pedido\" hash=\"hash\"/></parte>";
	static final String PARTE4 = "<parte id=\"despacho\" hash=\"hash\" preenchido=\"false\"><depende id=\"pedido\" hash=\"hash\"/><depende id=\"diarias\" hash=\"hash\"/><depende id=\"transporte\" hash=\"hash\"/></parte>";

	static final String PARTES1 = "<partes>" + PARTE1 + "</partes>";
	static final String PARTES1234 = "<partes>" + PARTE1 + PARTE2 + PARTE3
			+ PARTE4 + "</partes>";

	public DocumentoColaborativoTest() throws Exception {
	}

	public void testParseParteSemDependencias() throws Exception {
		ExParte parte = ExParte.unmarshallParte(PARTE1);
		assertEquals("pedido", parte.getId());
		assertEquals("hash", parte.getHash());
		assertTrue(parte.isPreenchido());
		assertEquals(0, parte.getDependencias().size());
	}

	public void testParseParteComUmaDependencia() throws Exception {
		ExParte parte = ExParte.unmarshallParte(PARTE2);
		assertEquals("diarias", parte.getId());
		assertEquals("hash", parte.getHash());
		assertFalse(parte.isPreenchido());
		assertEquals(1, parte.getDependencias().size());
		assertEquals("pedido", parte.getDependencias().get(0).getId());
		assertEquals("hash", parte.getDependencias().get(0).getHash());
	}

	public void testParseParteComDependencias() throws Exception {
		ExParte parte = ExParte.unmarshallParte(PARTE4);
		assertEquals("despacho", parte.getId());
		assertEquals(3, parte.getDependencias().size());
	}

	public void testParsePartes() throws Exception {
		ExPartes partes = ExPartes.unmarshall(PARTES1);
		assertEquals(1, partes.getPartes().size());
	}

	public void testCalcularAtivacaoDePartes() throws Exception {
		ExPartes partes = ExPartes.unmarshall(PARTES1234);
		partes.calcular();
		assertTrue(partes.getPartes().get(0).isAtivo());
		assertTrue(partes.getPartes().get(1).isAtivo());
		assertTrue(partes.getPartes().get(2).isAtivo());
		assertFalse(partes.getPartes().get(3).isAtivo());

		partes.getPartes().get(1).setPreenchido(true);
		partes.getPartes().get(2).setPreenchido(true);
		partes.calcular();
		assertTrue(partes.getPartes().get(0).isAtivo());
		assertTrue(partes.getPartes().get(1).isAtivo());
		assertTrue(partes.getPartes().get(2).isAtivo());
		assertTrue(partes.getPartes().get(3).isAtivo());

		partes.getPartes().get(0).setPreenchido(false);
		partes.calcular();
		assertTrue(partes.getPartes().get(0).isAtivo());
		assertFalse(partes.getPartes().get(1).isAtivo());
		assertFalse(partes.getPartes().get(2).isAtivo());
		assertFalse(partes.getPartes().get(3).isAtivo());
	}

	private String indent(String s) throws IOException {
		s = s.replaceAll("\\[\\#", "<p class=\"indent\">[#");
		
		final Tidy tidy = new Tidy();
		tidy.setXmlOut(true);
		tidy.setXmlPi(true);
		tidy.setXmlPIs(true);
		tidy.setXHTML(true);
		tidy.setFixComments(true);
		tidy.setFixBackslash(true);
		tidy.setTidyMark(false);
		tidy.setCharEncoding(Configuration.LATIN1);
		tidy.setRawOut(false);
		tidy.setIndentAttributes(false);
		tidy.setEncloseBlockText(true);
		tidy.setEncloseText(true);
		tidy.setMakeClean(true);
		tidy.setShowWarnings(false);
		tidy.setSmartIndent(false);
		tidy.setSpaces(0);
		tidy.setTidyMark(false);
		tidy.setIndentContent(true);
		
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		tidy.parse(new ByteArrayInputStream(s.getBytes("iso-8859-1")), os);
		os.flush();
		String sResult = new String(os.toByteArray(), "iso-8859-1");
		sResult = ProcessadorHtml.bodyOnly(sResult);
		return sResult;
	}
	
	public void testIndentacao() throws Exception {
		String s = "[#macro parte depende=\"\"][/#macro]";
		String sResult = indent(s);
//    <div>[#if !esconder]
//        <div>[#if root[id]??]
//          <div>[#local preenchido = root[id]/]</div>
//        <div>[#else]</div>
//          <div/>[#local preenchido = false/]
//        </div>[/#if]
//      </div>[/#if]
//    <div/>[#else]
//        <div/>[#nested]
//    </div>[/#if]
//</div>[/#macro]

		
		System.out.println(sResult);
	}
}
