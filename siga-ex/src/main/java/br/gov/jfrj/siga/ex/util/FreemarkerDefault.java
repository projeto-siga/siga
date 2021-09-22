package br.gov.jfrj.siga.ex.util;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.ex.bl.Ex;

public class FreemarkerDefault {
	public static String getDefaultTemplate() {
		try {
			String pathname = Prop.get("debug.default.template.pathname");
			if (pathname != null)
				return new String(Files.readAllBytes(Paths.get(pathname)), "UTF-8");
		} catch (Exception e1) {
		}

		String template;
		try (InputStream stream = FreemarkerDefault.class.getClassLoader()
				.getResourceAsStream("br/gov/jfrj/siga/ex/util/" + "default.ftl")) {
			template = IOUtils.toString(stream, "UTF-8");

			String pathBrasao = Prop.get("modelos.cabecalho.brasao") + "";
			if (pathBrasao != null)
				template = template.replaceAll("\\[#assign _pathBrasao = \"([^\"]+)\" /\\]",
						"[#assign _pathBrasao = \"" + pathBrasao + "\" /]");
			
			String widthBrasao = Prop.get("modelos.cabecalho.brasao.width") + "";
			if (widthBrasao != null)
				template = template.replaceAll("\\[#assign _widthBrasao = \"([^\"]+)\" /\\]",
						"[#assign _widthBrasao = \"" + widthBrasao + "\" /]");
			
			String heightBrasao = Prop.get("modelos.cabecalho.brasao.height") + "";
			if (heightBrasao != null)
				template = template.replaceAll("\\[#assign _heightBrasao = \"([^\"]+)\" /\\]",
						"[#assign _heightBrasao = \"" + heightBrasao + "\" /]");

			String tituloGeral = Prop.get("modelos.cabecalho.titulo");
			if (tituloGeral != null)
				template = template.replaceAll("\\[#assign _tituloGeral = \"[^\"]+\" /\\]",
						"[#assign _tituloGeral = \"" + tituloGeral + "\" /]");

			String subtituloGeral = Prop.get("modelos.cabecalho.subtitulo");
			if (subtituloGeral != null)
				template = template.replaceAll("\\[#assign _subtituloGeral = \"[^\"]+\" /\\]",
						"[#assign _subtituloGeral = \"" + subtituloGeral + "\" /]");

			return template;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
