package br.gov.jfrj.siga.ex.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class FreemarkerDefault {
	public static String getDefaultTemplate() {
		InputStream stream = FreemarkerDefault.class.getClassLoader()
				.getResourceAsStream(
						"br/gov/jfrj/siga/ex/util/" + "default.ftl");
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
