package br.gov.jfrj.siga.ex.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;

import br.gov.jfrj.siga.ex.bl.Ex;

public class FreemarkerDefault {
	public static String getDefaultTemplate() {
		try {
			String pathname = Ex
					.getInstance()
					.getProp()
					.obterPropriedade("siga.ex.debug.default.template.pathname");
			if (pathname != null)
				return new String(Files.readAllBytes(Paths.get(pathname)),
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
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}
}
