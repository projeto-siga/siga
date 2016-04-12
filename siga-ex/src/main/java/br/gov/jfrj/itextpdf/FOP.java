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
import java.io.File;
import java.io.IOException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.xml.sax.SAXException;

public class FOP implements ConversorHtml {

	private String xslFile;
	
	public FOP(){
		this("xhtml2foNovo.xsl");
	}
	
	public FOP(String xslFile){
		this.xslFile = xslFile;
	}
	
	private static Configuration CFG;
	
	static{
		try {
			DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
			CFG = cfgBuilder.build(FOP.class.getResourceAsStream("fop-cfg.xml"));
			int a = 0;
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Result obtemCanalizadorBAOSFop(ByteArrayOutputStream baos,
			byte outputMode) throws Exception {
		
		FopFactory fopFactory = FopFactory.newInstance();
		fopFactory.setUserConfig(CFG);

		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

		String format;

		if (outputMode == 0)
			format = MimeConstants.MIME_PDF;
		else
			format = MimeConstants.MIME_RTF;

		Fop fop = fopFactory.newFop(format, foUserAgent, baos);

		return new SAXResult(fop.getDefaultHandler());

	}

	public Result obtemCanalizadorXSLFO(ByteArrayOutputStream baos)
			throws Exception {
		
		return new StreamResult(baos);
	}

	public void transforma(Source src, Result res) throws Exception {
		TransformerFactory factory = TransformerFactory.newInstance();
		
		Transformer transformer = factory.newTransformer(new StreamSource(this
				.getClass().getResourceAsStream(xslFile)));

		
		// Set the value of a <param> in the stylesheet
		transformer.setParameter("versionParam", "2.0");

		transformer.transform(src, res);

	}

	private String substituiComentarios(String html) {
		html = html.replace("<!-- INICIO PRIMEIRO CABECALHO",
				"<div id=\"cabecalho-primeira-pagina\">");
		html = html.replace("FIM PRIMEIRO CABECALHO -->", "</div>");
		html = html.replace("<!-- INICIO CABECALHO", "<div id=\"cabecalho\">");
		html = html.replace("FIM CABECALHO -->", "</div><div id=\"corpo\">");
		html = html.replace("<!-- INICIO PRIMEIRO RODAPE",
				"</div><div id=\"rodape-primeira-pagina\">");
		html = html.replace("FIM PRIMEIRO RODAPE -->", "</div>");
		html = html.replace("<!-- INICIO RODAPE", "<div id=\"rodape\">");
		html = html.replace("FIM RODAPE -->", "</div>");
		return html;
	}

	public byte[] converter(String html, byte outputMode) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			
			//System.setProperty("http.proxyHost", "10.10.1.55");
			//System.setProperty("http.proxyPort", "8080");

			// fonte: xslt
			html = substituiComentarios(html);
			html = html.replace("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"", "\"xhtml.dtd\"");
			html = html.replace("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"", "\"xhtml.dtd\"");
			Source src = new StreamSource(new ByteArrayInputStream(html
					.getBytes("UTF-8")));

			// destino: ponte FOP que joga o resultado no BAOS
			Result res = obtemCanalizadorBAOSFop(out, outputMode);
			//Result res = obtemCanalizadorXSLFO(out);

			// Esse metodo pega a fonte, transforma em xsl-fo e joga no destino.
			// O destino poderia ser um StreamResult simples criado em cima do
			// BAOS, e entao seria uma ponte que serviria pra jogar o xsl-fo pro
			// BAOS
			// Como o destino foi definido como sendo uma ponte FOP, complexa,
			// que leva
			// ao BAOS, o resultado cai em pdf no baos, nao em xsl-fo
			transforma(src, res);

			String s = new String(out.toByteArray());

			byte[] ba;
			if(outputMode == 0)
				ba = out.toByteArray();
			else {
				s = s.replace("\\sect }", "}");
				ba = s.getBytes();
			}
			
			return ba;
		} catch (Throwable e) {
			e.printStackTrace(System.err);
		}
		return null;
	}
}
