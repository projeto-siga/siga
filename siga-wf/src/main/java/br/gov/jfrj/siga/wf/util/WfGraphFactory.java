package br.gov.jfrj.siga.wf.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Scanner;

import org.kxml2.io.KXmlParser;
import org.kxml2.io.KXmlSerializer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

public class WfGraphFactory {
	XmlPullParser parser;

	XmlSerializer serializer;

	public WfGraphFactory() {
		parser = new KXmlParser();
		serializer = new KXmlSerializer();
	}

	public String gerarDOT(final String sXML, final String currentNode,
			final Map<String, String> map) throws Exception {
		final StringBuilder sbTransition = new StringBuilder();
		// final StringBuilder sbStart = new StringBuilder();
		// final StringBuilder sbEnd = new StringBuilder();
		// final StringBuilder sbFork = new StringBuilder();
		// final StringBuilder sbJoin = new StringBuilder();
		// final Writer os = new StringWriter();
		sbTransition.append("node[shape=rectangle];");

		try {
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

			parser.setInput(new StringReader(sXML));

			String current = null;

			while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
				switch (parser.getEventType()) {
				case XmlPullParser.START_DOCUMENT:
					final String sGraphName = parser.getName();
					// sbTransition.append(sGraphName);
					break;

				case XmlPullParser.START_TAG:
					String sElement = parser.getName();

					if ("swimlane".equals(sElement)) {
					} else if ("start-state".equals(sElement)) {
						current = parser.getAttributeValue(null, "name");
						gerarNode(current, currentNode, map, "oval", sbTransition);
					} else if ("transition".equals(sElement)) {
						String to = parser.getAttributeValue(null, "to");
						String name = parser.getAttributeValue(null, "name");
						sbTransition.append("\n\"");
						sbTransition.append(current);
						sbTransition.append("\"->\"");
						sbTransition.append(to);
						sbTransition.append("\"");
						if (name != null && !name.startsWith("_")) {
							sbTransition.append("[edgetooltip=\"");
							sbTransition.append(name);
							sbTransition.append("\"]");
						}
						sbTransition.append(";");
					} else if ("node".equals(sElement)) {
						current = parser.getAttributeValue(null, "name");
						gerarNode(current, currentNode, null, "octagon",
								sbTransition);
					} else if ("decision".equals(sElement)) {
						current = parser.getAttributeValue(null, "name");
						gerarNode(current, currentNode, null, "diamond",
								sbTransition);
					} else if ("task-node".equals(sElement)) {
						current = parser.getAttributeValue(null, "name");
						gerarNode(current, currentNode, map, "rectangle",
								sbTransition);
					} else if ("mail-node".equals(sElement)) {
						current = parser.getAttributeValue(null, "name");
						gerarNode(current, currentNode, map, "note",
								sbTransition);
					} else if ("fork".equals(sElement)) {
						current = parser.getAttributeValue(null, "name");
						gerarNode(current, currentNode, null, "trapezium",
								sbTransition);
					} else if ("join".equals(sElement)) {
						current = parser.getAttributeValue(null, "name");
						gerarNode(current, currentNode, null, "invtrapezium",
								sbTransition);
					} else if ("end-state".equals(sElement)) {
						current = parser.getAttributeValue(null, "name");
						gerarNode(current, currentNode, null, "oval",
								sbTransition);
					}
					break;
				}
				parser.nextToken();
			}

			String s = sbTransition.toString();
			return s;
		} catch (final Exception ex) {
			throw new Exception(ex);
		}
	}

	private void gerarNode(String current, final String currentNode,
			Map<String, String> map, final String shape, final StringBuilder sb) {
		sb.append("\n\"");
		sb.append(current);
		sb.append("\"");
		sb.append("[shape=\"");
		sb.append(shape);
		sb.append("\"]");
		sb.append("[label=<<font>");
		sb.append(current);
		sb.append("</font>");
		if (map != null && map.get(current) != null) {
			sb.append("<br/><font point-size=\"9\" color=\"gray\">");
			sb.append(map.get(current));
			sb.append("</font>");
		}
		sb.append(">]");
		if (current.equals(currentNode))
			sb.append("[color=\"red\"]");
		// "Início"[shape="oval"][label=<<font>Início</font><br/><font
		// point-size="9" color="gray">OI!</font>>];
		sb.append(";");
	}

	private static String readFile(String pathname) throws IOException {

		File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int) file.length());
		Scanner scanner = new Scanner(file, "UTF-8");
		String lineSeparator = System.getProperty("line.separator");

		try {
			while (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine() + lineSeparator);
			}
			return fileContents.toString();
		} finally {
			scanner.close();
		}
	}

	public static String readFileDel(String filePath) throws IOException {
		DataInputStream dis = new DataInputStream(new FileInputStream(filePath));
		try {
			long len = new File(filePath).length();
			if (len > Integer.MAX_VALUE)
				throw new IOException("File " + filePath + " too large, was "
						+ len + " bytes.");
			byte[] bytes = new byte[(int) len];
			dis.readFully(bytes);
			return new String(bytes, "UTF-8");
		} finally {
			dis.close();
		}
	}

	public String gerarURL(String sDOT) {
		StringBuilder sb = new StringBuilder();

		sb.append("https://chart.googleapis.com/chart?cht=gv&chs=600x450&chl=digraph{");
		sb.append(sDOT);
		sb.append("}");
		String s = sb.toString();
		return s;
	}

	public static void main(String[] args) throws Exception {
		String sXML = readFile("C:/Trabalhos/Java/siga-wf-pd/src/main/jpdl/AutorizacaoAusencia/processdefinition.xml");
		WfGraphFactory graph = new WfGraphFactory();
		String sDOT = graph.gerarDOT(sXML, "Início", null);
		String sURL = graph.gerarURL(sDOT);
		return;
	}
}
