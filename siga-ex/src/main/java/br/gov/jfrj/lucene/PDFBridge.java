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
package br.gov.jfrj.lucene;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import org.hibernate.search.bridge.StringBridge;
import org.kxml2.io.KXmlParser;
import org.pdfbox.cos.COSDocument;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;
import org.xmlpull.v1.XmlPullParser;

public class PDFBridge implements StringBridge {

	public String objectToString(Object arg0) {

		if (arg0 == null )
			return null;
		
		String retorno = "";
		
		PDDocument pdd = null;
		COSDocument cosDoc = null;
		
		try {
			byte[] conteudoPDF = (byte[]) arg0;
			ByteArrayInputStream is = new ByteArrayInputStream(conteudoPDF);
			PDFParser parser;
			parser = new PDFParser(is);
			parser.parse();
			cosDoc = parser.getDocument();
			PDFTextStripper stripper = new PDFTextStripper();
			pdd = new PDDocument(cosDoc);
			retorno = stripper.getText(pdd);
			
		} catch (Throwable t) {
			// TODO Auto-generated catch block
			t.printStackTrace();
		} finally {
			try{
			pdd.close();
			cosDoc.close();
			} catch(IOException ioe){
				//
			}
		}

		return retorno;
	}
}
