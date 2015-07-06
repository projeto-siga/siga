package br.gov.jfrj.siga.util;

import java.io.IOException;
import java.util.ArrayList;

import junit.framework.TestCase;

public class IndentTest extends TestCase {

	public void testIndentacao() throws Exception {
		assertEquals("<p>\n  oi\n</p>", FreemarkerIndent.indent("oi"));

		assertEquals("[#entrevista]\n[/#entrevista]",
				FreemarkerIndent.indent("[#entrevista][/#entrevista]"));

		assertEquals("[#if]\n  <p>\n    oi\n  </p>\n[/#if]",
				FreemarkerIndent.indent("[#if]oi[/#if]"));

		assertEquals("[#if]\n[#else]\n[/#if]",
				FreemarkerIndent.indent("[#if][#else][/#if]"));
		
		assertEquals("[#if]\n  [@br/]\n[#else]\n  [@br/]\n[/#if]",
				FreemarkerIndent.indent("[#if][@br/][#else][@br/][/#if]"));

	}

	public void testFtlParser() {
		assertEquals("oi", new FreemarkerMarker("oi").run());
		assertEquals("{{fm}}[#--comment--]{{/fm}}", new FreemarkerMarker(
				"[#--comment--]").run());
		assertEquals("{{fm}}[#teste]{{/fm}}",
				new FreemarkerMarker("[#teste]").run());
	}

	public void testFMSkip() throws IOException {
		assertEquals(
				"[#assign pess = func.pessoa(.vars['prop' + '_pessoaSel.id']?number) /]",
				FreemarkerIndent
						.indent("[#assign pess = func.pessoa(.vars['prop' + '_pessoaSel.id']?number) /]"));

		assertEquals("[#assign p = .vars[.vars['x']] /]",
				FreemarkerIndent.indent("[#assign p = .vars[.vars['x']] /]"));

	}

	public void testConvertFtl2Html() {
		assertEquals(
				"<div class=\"ftl\"><div data-ftl=\"1\"><span>0</span></div>",
				FreemarkerIndent.convertFtl2Html("[#entrevista]",
						new ArrayList<String>()));
	}

	public void testConvertFtl2Html2Ftl() {
		ArrayList<String> lftl = new ArrayList<String>();
		String ftl = "[#test]";
		String html = FreemarkerIndent.convertFtl2Html(ftl, lftl);
		String ftl2 = FreemarkerIndent.convertHtml2Ftl(html, lftl);
		assertEquals(ftl, ftl2);
	}
}
