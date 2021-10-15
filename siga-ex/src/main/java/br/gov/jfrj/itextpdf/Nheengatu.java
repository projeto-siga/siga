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
package br.gov.jfrj.itextpdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.aryjr.nheengatu.pdf.HTML2PDFParser;
import com.aryjr.nheengatu.pdf.PDFDocument;

import br.gov.jfrj.siga.base.util.Texto;

public class Nheengatu implements ConversorHtml {

	private final HTML2PDFParser parser;

	public Nheengatu() {
		parser = new HTML2PDFParser();
	}

	public byte[] converter(String sHtml, byte output) throws Exception {
		// TODO Auto-generated method stub
		try (ByteArrayOutputStream bo = new ByteArrayOutputStream()) {
		parser.parse(new ByteArrayInputStream(sHtml.getBytes("utf-8")),
				extract(sHtml, "<!-- INICIO PRIMEIRO CABECALHO",
						"FIM PRIMEIRO CABECALHO -->"), extract(sHtml,
						"<!-- INICIO PRIMEIRO RODAPE",
						"FIM PRIMEIRO RODAPE -->"), extract(sHtml,
						"<!-- INICIO CABECALHO", "FIM CABECALHO -->"), extract(
						sHtml, "<!-- INICIO RODAPE", "FIM RODAPE -->"));

		
		
		final PDFDocument pdf = parser.getPdf();
		// pdf.generateFile(response.getOutputStream());

//		System.out.println("Processamento: terminou nheengatu extract cabecalhos");
		
		pdf.generateFile(bo);

//		System.out.println("Processamento: terminou nheengatu generate file");
		
		// System.out.println(System.currentTimeMillis() + " - FIM
		// generatePdf");
		return bo.toByteArray();
		} catch(Throwable t){
//			System.out.println("Processamento: stacktrace nheengatu.converter");
//			System.out.println("mensagem::::" + t.getMessage());
//			System.out.println("causa::::" + t.getCause());
//			t.printStackTrace();
			throw new Exception(t);
		}
	}

	private static InputStream extract(final String sSource,
			final String sBegin, final String sEnd)
			throws UnsupportedEncodingException {
		String sResult = Texto.extrai(sSource, sBegin, sEnd);
		if (sResult == null)
			return null;
		return new ByteArrayInputStream(sResult.getBytes("utf-8"));
	}

}
