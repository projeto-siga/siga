package br.gov.jfrj.siga.jee;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class FreemarkerSimplesDefault {
	public static String getDefaultTemplate() {
		InputStream stream = FreemarkerSimplesDefault.class.getClassLoader()
				.getResourceAsStream(
						"br/gov/jfrj/siga/jee/" + "default.ftl");
		String template;
		try {
			template = IOUtils.toString(stream, "UTF-8");
			return template;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}
}
