package br.gov.jfrj.siga.ex.util;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;

import br.gov.jfrj.siga.base.Prop;

public class FreemarkerDefault {
	public static String getDefaultTemplate() {
		try {
			String pathname = Prop.get("debug.modelo.padrao.arquivo");
			if (pathname != null)
				return new String(Files.readAllBytes(Paths.get(pathname)),
						"UTF-8");
		} catch (Exception e1) {
		}

		String template;
		try (InputStream stream = FreemarkerDefault.class.getClassLoader()
				.getResourceAsStream(
						"br/gov/jfrj/siga/ex/util/" + "default.ftl")) {
			template = IOUtils.toString(stream, "UTF-8");
			return template;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
