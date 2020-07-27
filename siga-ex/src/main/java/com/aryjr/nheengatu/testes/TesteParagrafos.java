/*
 * The Nheengatu Project : a free Java library for HTML  abstraction.
 *
 * Project Info:  http://www.aryjr.com/nheengatu/
 * Project Lead:  Ary Rodrigues Ferreira Junior
 *
 * (C) Copyright 2005, 2006 by Ary Junior
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.aryjr.nheengatu.testes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;

import com.aryjr.nheengatu.html.HTMLDocument;
import com.aryjr.nheengatu.html.Tag;
import com.aryjr.nheengatu.pdf.HTML2PDFParser;
import com.aryjr.nheengatu.pdf.PDFDocument;

/**
 * 
 * Teste para o pacote val-html-para-pdf.
 * 
 * @version $Id: TesteParagrafos.java,v 1.1 2007/12/26 15:57:41 tah Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class TesteParagrafos {

	public static void main(final String[] args) {
		new TesteParagrafos();
	}

	public TesteParagrafos() {
		final HTMLDocument html = new HTMLDocument("teste-paragrafos");
		html.setPath("/TEMP/");
		html.breakLines(true);
		final Tag titulo = new Tag("center", new Tag("h1", "Projeto Nheengatu"));
		html.getBody().addTag(titulo);
		final Tag p1 = new Tag(
				"p",
				"O objetivo principal do Projeto Nheengatu é utilizar ao máximo o HTML, a linguagem de marcação, acredito eu, mais utilizada do mundo. Baseada no DTD SGML, o HTML está na marcação de procedimentos em documentos de hipertexto espalhados por toda a Internet. O HTML é uma linguagem comum, como o Nheengatu. O Projeto Nheengatu é uma biblioteca simples de classes Java utilizada para a abstração de um conjunto de tags e textos HTML provendo sua exibição em um arquivo PDF, uma planilha do Calc, uma Imagem, etc. É isso aí, o HTML além do browser! No exemplo abaixo, temos uma tabela do HTML utilizada na camada de apresentação de uma aplicação J2EE para exibir um conjunto de registros devidamente marcados. Ao se clicar no link \"Imprimir\" um Servlet será invocado e retornará para o cliente um documento PDF, onde a marcação do HTML foi reutilizada. Para incluir este recurso na minha aplicação, bastou inserir dois comentários no início e no fim do código HTML a ser exibido no documento PDF como no exemplo abaixo:");
		p1.setPropertyValue("style", "font-size: 15; font-family: Times New Roman");
		html.getBody().addTag(p1);
		final Tag p2 = new Tag("p", "&lt;!-- NHEENGATU_HTML2PDF-BEGIN -->");
		p2.setPropertyValue("style", "font-size: 12; font-family: Times New Roman");
		p2.addTag(new Tag("br"));
		p2.addText("&lt;table align=\"center\" bgcolor=\"#ffffff\" width=\"400\">");
		p2.addTag(new Tag("br"));
		p2.addText("...");
		p2.addTag(new Tag("br"));
		p2.addText("&lt;/table>");
		p2.addTag(new Tag("br"));
		p2.addText("&lt;!-- NHEENGATU_HTML2PDF-END -->");
		html.getBody().addTag(p2);
		final Tag p3 = new Tag(
				"p",
				"Para isto utilizei o TagSoup para parsear o HTML e o iText para gerar o PDF. Se você está interessado em me ajudar email-me em <a href=\"mailto:junior@valoriza.com.br\">junior@valoriza.com.br</a>. Gostaria de saber se este projeto pode lhe ser útil. Para mim está quebrando o maior galho, pois estou desenvolvendo um site em JSP onde tenho muitas tabelas parecidas com a do exemplo abaixo. Ainda há vários problemas como os rowspans que não existem no iText, nem todas as tags são suportadas, fontes, tamanho do texto, entre outros. Este projeto me foi doado pela empresa onde trabalho com a condição de torná-lo público sob a GNU GPL e quero registrá-lo no SourceForge.net ou no java.net. Bem, se você estiver interessado, envie um e-mail. Ah, o exemplo por enquanto não está solicitando o arquivo PDF ao Servlet pois não tenho o Tomcat neste servidor aqui ainda.");
		p3.setPropertyValue("style", "font-size: 15; font-family: Times New Roman");
		p3.addTag(new Tag("br"));
		p3.addTag(new Tag("br"));
		p3.addText("[]s");
		p3.addTag(new Tag("br"));
		p3.addTag(new Tag("br"));
		p3.addText("Ary Junior");
		html.getBody().addTag(p3);
		// Agora salvando os documentos
		try {
			html.generateFile();
			criaDocumentoPDF(html);
			criaDocumentoWriter(html);
		} catch (final Exception e) {
			System.out.println("Exception ao criar os documentos.");
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	public void TesteParagrafosAntigo() {
		final HTMLDocument html = new HTMLDocument("teste-paragrafos");
		html.setPath("/TEMP/");
		html.breakLines(true);
		final Tag titulo = new Tag("center", new Tag("h1", "Projeto Nheengatu"));
		html.getBody().addTag(titulo);
		final Tag p1 = new Tag(
				"p",
				"O objetivo principal do Projeto Nheengatu é utilizar ao máximo o HTML, a linguagem de marcação, acredito eu, mais utilizada do mundo. Baseada no DTD SGML, o HTML está na marcação de procedimentos em documentos de hipertexto espalhados por toda a Internet. O HTML é uma linguagem comum, como o Nheengatu. O Projeto Nheengatu é uma biblioteca simples de classes Java utilizada para a abstração de um conjunto de tags e textos HTML provendo sua exibição em um arquivo PDF, uma planilha do Calc, uma Imagem, etc. É isso aí, o HTML além do browser! No exemplo abaixo, temos uma tabela do HTML utilizada na camada de apresentação de uma aplicação J2EE para exibir um conjunto de registros devidamente marcados. Ao se clicar no link \"Imprimir\" um Servlet será invocado e retornará para o cliente um documento PDF, onde a marcação do HTML foi reutilizada. Para incluir este recurso na minha aplicação, bastou inserir dois comentários no início e no fim do código HTML a ser exibido no documento PDF como no exemplo abaixo:");
		p1.setPropertyValue("style", "font-size: 15; font-family: Times New Roman");
		html.getBody().addTag(p1);
		final Tag p2 = new Tag("p", "&lt;!-- NHEENGATU_HTML2PDF-BEGIN -->");
		p2.setPropertyValue("style", "font-size: 12; font-family: Times New Roman");
		p2.addTag(new Tag("br"));
		p2.addText("&lt;table align=\"center\" bgcolor=\"#ffffff\" width=\"400\">");
		p2.addTag(new Tag("br"));
		p2.addText("...");
		p2.addTag(new Tag("br"));
		p2.addText("&lt;/table>");
		p2.addTag(new Tag("br"));
		p2.addText("&lt;!-- NHEENGATU_HTML2PDF-END -->");
		html.getBody().addTag(p2);
		final Tag p3 = new Tag(
				"p",
				"Para isto utilizei o TagSoup para parsear o HTML e o iText para gerar o PDF. Se você está interessado em me ajudar email-me em <a href=\"mailto:junior@valoriza.com.br\">junior@valoriza.com.br</a>. Gostaria de saber se este projeto pode lhe ser útil. Para mim está quebrando o maior galho, pois estou desenvolvendo um site em JSP onde tenho muitas tabelas parecidas com a do exemplo abaixo. Ainda há vários problemas como os rowspans que não existem no iText, nem todas as tags são suportadas, fontes, tamanho do texto, entre outros. Este projeto me foi doado pela empresa onde trabalho com a condição de torná-lo público sob a GNU GPL e quero registrá-lo no SourceForge.net ou no java.net. Bem, se você estiver interessado, envie um e-mail. Ah, o exemplo por enquanto não está solicitando o arquivo PDF ao Servlet pois não tenho o Tomcat neste servidor aqui ainda.");
		p3.setPropertyValue("style", "font-size: 15; font-family: Times New Roman");
		p3.addTag(new Tag("br"));
		p3.addTag(new Tag("br"));
		p3.addText("[]s");
		p3.addTag(new Tag("br"));
		p3.addTag(new Tag("br"));
		p3.addText("Ary Junior");
		html.getBody().addTag(p3);
		// Agora salvando os documentos
		try {
			html.generateFile();
			criaDocumentoPDF(html);
			criaDocumentoWriter(html);
		} catch (final Exception e) {
			System.out.println("Exception ao criar os documentos.");
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	private InputStream extract(final String sSource, final String sBegin, final String sEnd) {
		final Integer iBegin = sSource.indexOf(sBegin);
		final Integer iEnd = sSource.indexOf(sEnd);

		if (iBegin == -1 || iEnd == -1)
			return null;

		final String sResult = sSource.substring(iBegin + sBegin.length(), iEnd);
		return new StringBufferInputStream(sResult);
	}

	private void criaDocumentoPDF(final HTMLDocument html) throws IOException {
		/*
		 * { PDFDocument pdf = new PDFDocument("teste-paragrafos");
		 * pdf.setPath("/TEMP/"); pdf.setBody(html.getBody());
		 * pdf.generateFile(); }
		 */
		{
			final HTML2PDFParser parser = new HTML2PDFParser();

			// TODO pdf.setTitle(html.getHead().getTitle());
			parser.setDocumentPath("/TEMP/");
			parser.setDocumentName("myPape3");

			final FileInputStream fisHtml = new FileInputStream("/TEMP/pape.html");
			final StringBuffer sbHtml = new StringBuffer();
			String sHtml;

			int i;
			while (true) {
				i = fisHtml.read();
				if (i == -1)
					break;
				sbHtml.append((char) i);
			}

			fisHtml.close();

			sHtml = sbHtml.toString();

			parser.parse(new StringBufferInputStream(sHtml), extract(sHtml, "<!-- INICIO PRIMEIRO CABECALHO",
					"FIM PRIMEIRO CABECALHO -->"), extract(sHtml, "<!-- INICIO PRIMEIRO RODAPE",
					"FIM PRIMEIRO RODAPE -->"), extract(sHtml, "<!-- INICIO CABECALHO", "FIM CABECALHO -->"), extract(
					sHtml, "<!-- INICIO RODAPE", "FIM RODAPE -->"));

			// parser.parse(new FileInputStream("/TEMP/pape.html"), new
			// FileInputStream("/TEMP/h_pape.html"),
			// new FileInputStream("/TEMP/f_pape.html"), new
			// FileInputStream("/TEMP/h2_pape.html"),
			// new FileInputStream("/TEMP/f2_pape.html"));

			final PDFDocument pdf = parser.getPdf();
			try {
				pdf.generateFile(new FileOutputStream("/TEMP/myPape3.pdf"));
			} catch (final IOException ioe) {
				ioe.printStackTrace();
			}
		}
		Runtime.getRuntime().exec(
				"\"C:/Arquivos de programas/Adobe/Acrobat 6.0/Reader/AcroRd32.exe\" c:/TEMP/myPape3.pdf");
	}

	private void criaDocumentoWriter(final HTMLDocument html) throws IOException {
		// WriterDocument writer = new WriterDocument("teste-paragrafos");
		// writer.setPath("/TEMP/");
		// writer.setBody(html.getBody());
		// writer.generateFile();
		// Runtime.getRuntime().exec("/opt/KuruminOffice1.1.2/program/swriter
		// /home/kurumin/cvs/java/Nheengatu/teste-paragrafos.sxw");
	}

}
/**
 * 
 * $Log: TesteParagrafos.java,v $
 * Revision 1.1  2007/12/26 15:57:41  tah
 * *** empty log message ***
 *
 * Revision 1.3  2006/07/05 16:00:48  nts
 * Refatorando para melhorar qualidade do código
 *
 * Revision 1.2  2006/04/11 19:43:47  tah
 * *** empty log message ***
 * Revision 1.1 2006/04/03 21:30:42 tah
 * Utilizando o nheengatu Revision 1.4 2006/01/01 13:45:31 aryjr Feliz 2006!!!
 * 
 * Revision 1.3 2005/12/19 15:17:05 aryjr Breaking lines when Images and text
 * are inside the same paragraphs not yet solved.
 * 
 * Revision 1.2 2005/12/16 14:06:29 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.1 2005/11/14 12:17:28 aryjr Renomeando os pacotes.
 * 
 * Revision 1.2 2005/11/11 21:09:50 aryjr Retomando o desenvolvimento e
 * trabalhando no suporte ao CSS2.
 * 
 * Revision 1.1 2005/09/10 23:43:37 aryjr Passando para o java.net.
 * 
 * Revision 1.7 2005/07/02 01:18:57 aryjunior Site do projeto.
 * 
 * Revision 1.6 2005/06/20 03:28:35 aryjunior Iniciando a geracao de documentos
 * do Writer do OpenOffice.
 * 
 * Revision 1.5 2005/06/04 13:29:25 aryjunior LGPL.
 * 
 * Revision 1.4 2005/05/30 14:31:24 aryjunior Ha uma pendencia com relacao aos
 * tamanhos das fontes.
 * 
 * Revision 1.3 2005/05/30 01:55:57 aryjunior Alguns detalhes no cabecalho dos
 * arquivos e fazendo alguns testes com tabelas ainhadas.
 * 
 * Revision 1.2 2005/05/28 23:21:41 aryjunior Corrigindo o cabecalho.
 * 
 * Revision 1.1.1.1 2005/05/28 21:10:32 aryjunior Initial import.
 * 
 */
