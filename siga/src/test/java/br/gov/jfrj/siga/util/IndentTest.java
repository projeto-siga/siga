package br.gov.jfrj.siga.util;

import java.io.IOException;
import java.util.ArrayList;

import junit.framework.TestCase;

public class IndentTest extends TestCase {

	public void testIndentacao() throws Exception {
		assertEquals("oi", FreemarkerIndent.indent("oi"));

		assertEquals("[#entrevista]\n[/#entrevista]",
				FreemarkerIndent.indent("[#entrevista][/#entrevista]"));

		assertEquals("[#if]\n  oi\n[/#if]",
				FreemarkerIndent.indent("[#if]oi[/#if]"));
	}

	public void testIndentacaoUnindent() throws Exception {
		assertEquals("[#if]\n[#else]\n[/#if]",
				FreemarkerIndent.indent("[#if][#else][/#if]"));
	}

	public void testIndentacaoUnindentComplexo() throws Exception {
		assertEquals("[#if]\n  [@br/]\n[#else]\n  [@br/]\n[/#if]",
				FreemarkerIndent.indent("[#if][@br/][#else][@br/][/#if]"));
	}

	public void testIndentacaoComentarios() throws Exception {
		assertEquals("<!--Teste-->oi",
				FreemarkerIndent.indent("<!--Teste-->oi"));
	}
	
	public void testIndentacaoInlineIf() throws Exception {
		assertTrue(FreemarkerIndent.indent("<div>oi1[#if]oi2[/#if]oi3</div>")
				.matches("<div>\r?\n\\x20{2}oi1\r?\n\\x20{2}\\[\\#if\\]\r?\n\\x20{4}oi2\r?\n\\x20{2}\\[/\\#if\\]\r?\n\\x20{2}oi3\r?\n</div>"));
	}
	
	public void testIndentacaoProblemaDe1CharAMais() throws Exception {
		assertTrue(FreemarkerIndent.indent("<div>\n  oi1\n  [#if]\n    oi2\n  [/#if]\n  oi3\n</div>")
				.matches("<div>\\r?\\n\\x20{2}oi1\\r?\\n\\x20{2}\\[\\#if\\]\\r?\\n\\x20{4}oi2\\r?\\n\\x20{2}\\[/\\#if\\]\\r?\\n\\x20{2}oi3\\r?\\n</div>"));
	}
	
	

	public void testIndentacaoDentroDeDiv() throws Exception {
		assertTrue(FreemarkerIndent.indent("<div>[#if]oi[/#if]</div>")
				.matches("<div>\r?\n\\x20{2}\\[\\#if\\]\r?\n\\x20{4}oi\r?\n\\x20{2}\\[/\\#if\\]\r?\n</div>"));
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
		assertEquals("<!--fm-open=\"1\"-->", FreemarkerIndent.convertFtl2Html(
				"[#entrevista]", new ArrayList<String>()));
		assertEquals("<!--fm-close=\"1\"-->", FreemarkerIndent.convertFtl2Html(
				"[/#entrevista]", new ArrayList<String>()));
		assertEquals("<!--fm-selfcontained=\"1\"-->",
				FreemarkerIndent.convertFtl2Html("[#entrevista/]",
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
