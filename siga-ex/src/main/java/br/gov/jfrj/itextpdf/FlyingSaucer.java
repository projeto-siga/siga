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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.ITextUserAgent;
import org.xml.sax.InputSource;

import com.crivano.swaggerservlet.SwaggerUtils;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaHTTP;

public class FlyingSaucer implements ConversorHtml {
	private static Logger logger = Logger.getLogger(FlyingSaucer.class.getCanonicalName());

	private String cleanHtml(String data) throws UnsupportedEncodingException {
		logger.fine("transformando HTML em XHTML");
		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.setCharEncoding(Configuration.UTF8);
		// tidy.setInputEncoding("UTF-8");
		// tidy.setOutputEncoding("UTF-8");
		tidy.setSmartIndent(true);
		tidy.setTidyMark(false);
		// tidy.setShowErrors(0);
		tidy.setQuiet(true);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes("UTF-8"));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		tidy.parseDOM(inputStream, outputStream);
		logger.fine("retornando XHTML");
		String s = outputStream.toString("UTF-8");
		s = s.replace(
				" PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"",
				"");
		s = s.replace(" xmlns=\"http://www.w3.org/1999/xhtml\"", "");
		return s;
	}

	private static class ResourceLoaderUserAgent extends ITextUserAgent {
		public ResourceLoaderUserAgent(ITextOutputDevice outputDevice) {
			super(outputDevice);
		}

		protected InputStream resolveAndOpenStream(String uri) {
			logger.fine("buscando recurso externo: " + uri);

			byte ab[] = null; // (byte[]) Dao.syncCache().get(entry);
			if (ab == null) {
				try {
					logger.fine("fazendo o download de recurso externo: " + uri);
					InputStream is = new SigaHTTP().fetch(uri, null, null, null);
					ab = SigaHTTP.convertStreamToByteArray(is, 16000);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
//				Dao.syncCache().put(entry, ab);
			}

			logger.fine("retornando recurso externo: " + uri);
			ByteArrayInputStream bais = new ByteArrayInputStream(ab);
			return bais;
		}
	}

	@Override
	public byte[] converter(String sHtml, byte outputMode) throws Exception {
		sHtml = substituiComentarios(sHtml);

		sHtml = substituiEstilos(sHtml);

		sHtml = corrigirDuplaQuebraDePagina(sHtml);

		sHtml = corrigirEscolhaDeFonts(sHtml);

		sHtml = corrigirNBSP(sHtml);

		sHtml = cleanHtml(sHtml);

		logger.fine(sHtml);

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ITextRenderer renderer = new ITextRenderer();
			ResourceLoaderUserAgent callback = new ResourceLoaderUserAgent(renderer.getOutputDevice());
			callback.setSharedContext(renderer.getSharedContext());
			renderer.getSharedContext().setUserAgentCallback(callback);

			try (StringReader reader = new StringReader(sHtml)) {
				javax.xml.parsers.DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

				factory.setNamespaceAware(true);
				factory.setValidating(false);
				factory.setAttribute("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
				javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();

				org.xml.sax.InputSource inputSource = new InputSource(reader);
				Document doc = builder.parse(inputSource);
				renderer.setDocument(doc, ""); // Prop.get("/siga.base.url"));
			}

			// renderer.setDocument(sHtml.getBytes(StandardCharsets.UTF_8));
			renderer.layout();
			renderer.createPDF(baos);
			logger.fine("PDF gerado");

			return baos.toByteArray();
		}
	}

	private static class Extraido {
		String strExtraida;
		String strRestante;
	}

	private static Extraido extrair(String str, String ini, String fim, String substituto) {
		Extraido e = new Extraido();
		int iIni = str.indexOf(ini);
		int iFim = str.indexOf(fim);
		if (iIni == -1 || iFim == -1)
			return null;
		String sPre = str.substring(0, iIni);
		String sSuf = str.substring(iFim + fim.length());
		e.strExtraida = str.substring(iIni + ini.length(), iFim);
		e.strRestante = sPre + substituto + sSuf;
		return e;
	}

	private static String substituiComentarios(String html) {
		Extraido primeiroCabecalho = extrair(html, "<!-- INICIO PRIMEIRO CABECALHO", "FIM PRIMEIRO CABECALHO -->", "");
		Extraido cabecalho = extrair(html, "<!-- INICIO CABECALHO", "FIM CABECALHO -->", "");
		Extraido primeiroRodape = extrair(html, "<!-- INICIO PRIMEIRO RODAPE", "FIM PRIMEIRO RODAPE -->", "");
		Extraido rodape = extrair(html, "<!-- INICIO RODAPE", "FIM RODAPE -->", "");

		String strPrimeiroCabecalho = primeiroCabecalho == null ? ""
				: "<div class=\"xdoc-first-header\">" + primeiroCabecalho.strExtraida + "</div>";
		String strCabecalho = cabecalho == null ? "" : "<div class=\"doc-header\">" + cabecalho.strExtraida + "</div>";

		String strPrimeiroRodape = primeiroRodape == null ? ""
				: "<div class=\"doc-first-footer\">" + primeiroRodape.strExtraida + "</div>";
		String strRodape = rodape == null ? ""
				: "<div class=\"doc-footer\">" + corrigirRodape(rodape.strExtraida) + "</div>";

		if (cabecalho != null) {
			strCabecalho += strPrimeiroRodape + strRodape;
		} else if (primeiroCabecalho != null) {
			strPrimeiroCabecalho += strPrimeiroRodape + strRodape;
		} else if (primeiroRodape != null || rodape != null) {
			throw new RuntimeException("Não é possível injetar rodapé quando não existe cabeçalho");
		}

		if (primeiroCabecalho != null) {
			strPrimeiroCabecalho = corrigirEstiloEmCabecalho(strPrimeiroCabecalho);
			primeiroCabecalho = extrair(html, "<!-- INICIO PRIMEIRO CABECALHO", "FIM PRIMEIRO CABECALHO -->",
					strPrimeiroCabecalho);
			html = primeiroCabecalho.strRestante;
		}

		if (cabecalho != null) {
			strCabecalho = corrigirEstiloEmCabecalho(strCabecalho);
			cabecalho = extrair(html, "<!-- INICIO CABECALHO", "FIM CABECALHO -->", strCabecalho);
			html = cabecalho.strRestante;
		}

		if (primeiroRodape != null) {
			primeiroRodape = extrair(html, "<!-- INICIO PRIMEIRO RODAPE", "FIM PRIMEIRO RODAPE -->", "");
			html = primeiroRodape.strRestante;
		}

		if (rodape != null) {
			rodape = extrair(html, "<!-- INICIO RODAPE", "FIM RODAPE -->", "");
			html = rodape.strRestante;
		}

		return html;
	}

	private static String corrigirRodape(String html) {
		// Substitui o rodapé antigo por uma versão compatível com o FlyingSaucer,
		// somente se for detectado o número de página no padrão antigo: "#pg"
		if (html.contains("#pg"))
			return "<div style=\"width: 100%; text-align: right;\"><span id=\"pagenumber\">&nbsp;</span></div>";
		return html;
	}

	private static String corrigirEstiloEmCabecalho(String html) {
		// O cabeçalho antigo do Siga utiliza AvantGarde, substituiremos apenas se for
		// detectado um cabeçalho antigo
		if (html.contains("AvantGarde Bk BT")) {
			html = html.replace("font-family: AvantGarde Bk BT, Arial;",
					"margin: 0; padding: 0; font-family: AvantGarde Bk BT, Arial;");
			html = html.replace("cellpadding=\"2\"", "cellpadding=\"0\"");

			html = html.replace("<p ", "<span ");
			html = html.replace("</p>", "</span>");
		}
		return html;
	}

	private static String corrigirDuplaQuebraDePagina(String html) {
		// Remove abre e fecha <br>, pois isso causa uma duplicidade de quebras de linha
		// no Flying Saucer
		html = html.replace("<br></br>", "<br/>");
		return html;
	}
	
	private String corrigirNBSP(String html) {
		// Remove &nbsp; pois isso não está sendo corretamente renderizado pelo no Flying Saucer
		html = html.replace("&nbsp;", " ");
		return html;
	}

	private static String corrigirEscolhaDeFonts(String html) {
		// Arial e AvantGarde serão substituídas por Helvetica pois é um fonte nativo do
		// padrão PDF
		html = html.replace("font-family: AvantGarde Bk BT, Arial", "font-family: Helvetica");
		html = html.replace("font-family: Arial", "font-family: Helvetica");
		html = html.replace("font-family:Arial", "font-family: Helvetica");
		return html;
	}

	private static String substituiEstilos(String html) {
		// Substitui os estilos presentes no HTML por estilos encontrados no arquivo
		// pagina.html, somente se detectar que não se trata de uma definição completa
		// de estilos, e nesse caso deve ser um HTML no padrão do Nheengatu. A definição
		// completa sempre terá atributos para a primeira página: ":first".
		String strEstilosPadrao = null;
		String padrao = SwaggerUtils.convertStreamToString(FlyingSaucer.class.getResourceAsStream("pagina.html"));

		Extraido estilosPadrao = extrair(padrao, "<style>", "</style>", "");
		if (estilosPadrao != null) {
			strEstilosPadrao = estilosPadrao.strExtraida;
		} else {
			estilosPadrao = extrair(padrao, "<style type=\"text/css\">", "</style>", "");
			if (estilosPadrao != null)
				strEstilosPadrao = estilosPadrao.strExtraida;
		}

		if (strEstilosPadrao == null)
			return html;

		Extraido estilos = extrair(html, "<style>", "</style>", "<style>" + strEstilosPadrao + "</style>");
		// Quando o <style> já contém um "page :first", então não há necessidade de
		// fazer a substituição
		if (estilos != null && !estilos.strExtraida.contains(":first"))
			return estilos.strRestante;

		estilos = extrair(html, "<style type=\"text/css\">", "</style>", "<style>" + strEstilosPadrao + "</style>");
		if (estilos != null && !estilos.strExtraida.contains(":first"))
			return estilos.strRestante;

		return html;
	}

	// Esta rotina pode ser usada para testar a produção de PDFs sem necessitar que
	// o JBoss seja iniciado
	public static void main(String[] args) throws Exception {
		Prop.setProvider(new Prop.IPropertyProvider() {

			public String getProp(String nome) {
				return null;
			}

			public void addRestrictedProperty(String name, String defaultValue) {
			}

			public void addRestrictedProperty(String name) {
			}

			public void addPublicProperty(String name, String defaultValue) {
			}

			public void addPublicProperty(String name) {
			}

			public void addPrivateProperty(String name, String defaultValue) {
			}

			public void addPrivateProperty(String name) {
			}
		});
		Prop.defineGlobalProperties();
		// Extraido e = extrair("123456789", "sd23", "56", "-");
		FlyingSaucer h2p = new FlyingSaucer();
//		Nheengatu h2p = new Nheengatu();
		String html = SwaggerUtils.convertStreamToString(FlyingSaucer.class.getResourceAsStream("pagina.html"));
		byte[] ab = h2p.converter(html, (byte) 0);
		ab = Documento.stamp(ab, "TRF2-MEM-2020/11111", false, false, false, false, true,
				"https://siga.jfrj.jus.br/sigaex/public/app/autenticar?n=1111111-1111",
				"Assinado digitalmente por USUARIO TESTE. Documento No: 1111111-1111 - https://siga.jfrj.jus.br/sigaex/public/app/autenticar?n=1111111-1111",
				1, 1, 1, "Justiça Federal", "TRF2", "");
		try (FileOutputStream fos = new FileOutputStream("/Users/nato/Downloads/testedepdf.pdf")) {
			fos.write(ab);
		}
	}
}
