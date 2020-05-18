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

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import br.gov.jfrj.itextpdf.ConversorHtml;
import br.gov.jfrj.itextpdf.FOP;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.TextElementArray;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.rtf.RtfWriter2;

public class GeradorRTF {

	private KXmlParser parser = new KXmlParser();
	String html = new String();
	private StringBuffer rtf = new StringBuffer();
	private Document document = new Document(PageSize.A4);

	private void incluirLista() {

	}

	private void incluirTabela() {

	}

	private TextElementArray percorreProximoBloco(TextElementArray element,
			int level, boolean considerarPTags) throws Exception {
		String tagName = parser.getName();
		while (!(parser.getEventType() == XmlPullParser.END_TAG
				&& parser.getName() != null && parser.getName().equals(tagName))) {

			parser.nextToken();

			// insere um texto comum
			if (parser.getEventType() == XmlPullParser.TEXT)
				element.add(parser.getText().toUpperCase());

			// Insere um parágrafo
			if (parser.getEventType() == XmlPullParser.START_TAG)
				if (parser.getName().toUpperCase().equals("P")
						&& considerarPTags
				/*
				 * || parser.getName().toUpperCase().equals("UL") ||
				 * parser.getName().toUpperCase().equals("OL") ||
				 * parser.getName().toUpperCase().equals("DL")
				 */) {
					Paragraph paragrafo = new Paragraph();
					paragrafo.add("\t");
					// paragrafo.setFirstLineIndent(((Paragraph)element).indentationLeft()
					// + 29);
					// paragrafo.setSpacingAfter(200);
					// paragrafo.setSpacingBefore(200);
					paragrafo = (Paragraph) percorreProximoBloco(paragrafo,
							level + 1, true);
					paragrafo.add("\n");
					element.add(paragrafo);
				}

			if (parser.getEventType() == XmlPullParser.START_TAG)
				if (parser.getName().toUpperCase().equals("TABLE")
				/*
				 * || parser.getName().toUpperCase().equals("UL") ||
				 * parser.getName().toUpperCase().equals("OL") ||
				 * parser.getName().toUpperCase().equals("DL")
				 */) {
					Paragraph paragrafo = new Paragraph();
					paragrafo.add("\t");
					// paragrafo.setFirstLineIndent(((Paragraph)element).indentationLeft()
					// + 29);
					// paragrafo.setSpacingAfter(200);
					// paragrafo.setSpacingBefore(200);
					paragrafo = (Paragraph) percorreProximoBloco(paragrafo,
							level + 1, false);
					paragrafo.add("\n");
					element.add(paragrafo);
				}

			// Insere um item de lista
			if (parser.getEventType() == XmlPullParser.START_TAG)
				if (parser.getName().toUpperCase().equals("LI")) {
					Paragraph paragrafo = new Paragraph();
					for (int k = 0; k <= level + 1; k++)
						paragrafo.add("\t");
					paragrafo = (Paragraph) percorreProximoBloco(paragrafo,
							level + 1, false);
					element.add(paragrafo);

				}

			// Pula linha ao fim da lista
			if (parser.getEventType() == XmlPullParser.END_TAG)
				if (parser.getName().toUpperCase().equals("UL")
						|| parser.getName().toUpperCase().equals("OL")
						|| parser.getName().toUpperCase().equals("DL")) {
					element.add("\n");
				}

			if (parser.getEventType() == XmlPullParser.START_TAG)
				if (parser.getName().toUpperCase().equals("TD")) {
					element.add("\t");
					percorreProximoBloco(element, level + 1, false);
				}

			if (parser.getEventType() == XmlPullParser.START_TAG)
				if (parser.getName().toUpperCase().equals("TR")) {
					element.add("\n\n");
				}

			if (parser.getEventType() == XmlPullParser.START_TAG)
				if (parser.getName().toUpperCase().equals("BR")) {
					element.add("\n\n\t");
				}
		}
		return element;
	}

	private void seekTag(String name) throws Exception {
		while (!(parser.getEventType() == XmlPullParser.START_TAG
				&& parser.getName() != null && parser.getName().equals(name))) {
			parser.nextToken();
			String texto = parser.getText();
			String nome = parser.getName();
		}
	}

	public byte[] geraRTFFOP(ExDocumento doc) throws Exception {
		try {
			html = doc.getConteudoBlobHtmlString();
			String htmlDocPrincipal;

			String inicioNumero = "<!-- INICIO NUMERO -->";
			String fimNumero = "<!-- FIM NUMERO -->";
			if (!html.contains(inicioNumero)) {
				inicioNumero = "<!-- INICIO NUMERO";
				fimNumero = "FIM NUMERO -->";
			}

			assertContemTags(doc.getCodigo(), html, "número", inicioNumero, fimNumero);
				
			htmlDocPrincipal = html.substring(html.indexOf(inicioNumero)
					+ inicioNumero.length(), html.indexOf(fimNumero))
					+ "<br />";

			String htmlTitulo = "";
			String inicioTituloForm = "<!-- INICIO TITULO";
			String fimTituloForm = "FIM TITULO -->";

			assertContemTags(doc.getCodigo(), html, "título", inicioTituloForm, fimTituloForm);
			if (html.contains(inicioTituloForm))
				htmlTitulo = html.substring(html.indexOf(inicioTituloForm)
						+ inicioTituloForm.length(),
						html.indexOf(fimTituloForm));

			String inicioMiolo = "<!-- INICIO MIOLO -->";
			String fimMiolo = "<!-- FIM MIOLO -->";
			assertContemTags(doc.getCodigo(), html, "conteúdo do documento", inicioMiolo, fimMiolo);
			html = htmlTitulo
					+ html.substring(
							html.indexOf(inicioMiolo) + inicioMiolo.length(),
							html.indexOf(fimMiolo));

			for (ExMobil mob : doc.getExMobilSet()) {
				if (mob.getExDocumentoFilhoSet() != null) {
					String inicioTitulo = "<!-- INICIO NUMERO -->";
					String fimTitulo = "<!-- FIM NUMERO -->";
					for (ExDocumento docFilho : mob.getExDocumentoFilhoSet()) {
						// Verifica se docFilho é do tipo anexo
						if (docFilho.getExFormaDocumento().getIdFormaDoc() == 60) {
							String htmlFilho = docFilho
									.getConteudoBlobHtmlString();
							assertContemTags(docFilho.getCodigo(), htmlFilho, "título", inicioTitulo, fimTitulo);
							html = html
									+ htmlFilho.substring(
											htmlFilho.indexOf(inicioTitulo)
													+ inicioTitulo.length(),
											htmlFilho.indexOf(fimTitulo));
							assertContemTags(docFilho.getCodigo(), htmlFilho, "conteúdo do documento", inicioMiolo, fimMiolo);
							html = html
									+ htmlFilho.substring(
											htmlFilho.indexOf(inicioMiolo)
													+ inicioMiolo.length(),
											htmlFilho.indexOf(fimMiolo));
						}
					}
				}
			}

			html = (new ProcessadorHtml()).canonicalizarHtml(html, true, false,
					true, true, false);
			html = "<?xml version='1.0' encoding='utf-8' ?> <!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"> <html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\"><body>"
					+ html + "</body></html>";

			html = removerTabela(html);

			byte[] baHtml = html.getBytes();
			if (baHtml.length > 1.0 * 1024 * 1024)
				throw new AplicacaoException(
						"O tamanho do arquivo a ser publicado é maior do que a capacidade suportada.");
			ConversorHtml conversor = new FOP("xhtml2foNovoSemStatic.xsl");
			return conversor.converter(html, ConversorHtml.RTF);
		} catch (Exception e) {
			throw new AplicacaoException(
					"Não foi possível ler o conteúdo do documento: "
							+ e.getMessage());
		}
	}

	private void assertContemTags(String sigla, String html, String trecho, String inicio, String fim) {
		if (!html.contains(inicio) || !html.contains(fim) ) 
			throw new AplicacaoException(
					"O HTML do documento " + sigla + " deve conter tags para marcar o início e o término do " + trecho + ", utilizar: '" + inicio + "' e '\" + fim + \"'.");
	}

	private String removerTabela(String html) {
		final String tags[] = { "<table", "</table", "<tr", "</tr", "<td",
				"</td", "<th", "</th", "<tbody", "</tbody", "<tfoot",
				"</tfoot", "<thead", "</thead", "<caption", "</caption" };

		for (String tag : tags) {
			if (html.contains(tag)) {
				while (html.contains(tag)) {
					int indiceInicial = html.indexOf(tag);
					int indiceEncerramento = html.indexOf(">", indiceInicial);

					String textoParaRemover = html.substring(indiceInicial,
							indiceEncerramento + 1);

					html = html.replaceAll(textoParaRemover, "");
				}
			}
		}

		return html;
	}

	public byte[] geraRTF(ExDocumento doc) throws Exception {

		html = doc.getConteudoBlobHtmlString();
		html = (new ProcessadorHtml()).canonicalizarHtml(html, true, false,
				true, true, false);
		html = html.replace("<!-- INICIO MIOLO -->", "<MIOLO>");
		html = html.replace("<!-- FIM MIOLO -->", "</MIOLO>");

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			RtfWriter2.getInstance(document, baos);
			document.open();
			document.setMargins(29, 340, 29, 29);
			parser.setInput(new StringReader(html));

			BaseFont arial = BaseFont.createFont(
					"C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, true);
			Font fonte = new Font(arial);
			fonte.setSize(7);
			Paragraph paragrafoBase = new Paragraph("", fonte);

			paragrafoBase.add("\t"
					+ doc.getOrgaoUsuario().getDescricaoMaiusculas());
			paragrafoBase.add("\n\n");
			paragrafoBase.add("\t"
					+ doc.getExFormaDocumento().getDescricao().toUpperCase()
					+ " " + doc.getCodigoString());
			if (doc.getDtDocDDMMYY() != null
					&& doc.getDtDocDDMMYY().length() > 0)
				paragrafoBase.add(" DE " + doc.getDtDocDDMMYY());
			paragrafoBase.add("\n\n");

			try {

				seekTag("MIOLO");
				paragrafoBase = (Paragraph) percorreProximoBloco(paragrafoBase,
						0, true);

				document.add(paragrafoBase);
			} catch (XmlPullParserException xppe) {
				int a = 2;
			} catch (IOException ioe) {
				int a = 2;
			} finally {
				document.close();
			}

			/*
			 * Pattern p1 = Pattern .compile("^.<!-- INICIO MIOLO -->.<!-- FIM
			 * MIOLO -->."); final Matcher m = p1.matcher(html);
			 */

			byte[] retorno = baos.toByteArray();

			return retorno;
		}

	}

}
