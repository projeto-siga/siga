package br.gov.jfrj.siga.ex.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;

public class FreemarkerDefault {
	public static String getDefaultTemplate() {
		try {
			return new String(
					Files.readAllBytes(Paths
							.get("/Users/nato/Repositories/projeto-siga/siga/siga-ex/src/main/java/br/gov/jfrj/siga/ex/util/default.ftl")),
					"UTF-8");
		} catch (Exception e1) {
		}

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
