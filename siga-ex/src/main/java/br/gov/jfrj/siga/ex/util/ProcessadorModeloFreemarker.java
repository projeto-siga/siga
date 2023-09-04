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
package br.gov.jfrj.siga.ex.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.parser.Parser;

import com.crivano.jmodel.Utils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaFormats;
import br.gov.jfrj.siga.cp.CpModelo;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.calc.diarias.FreemarkerFormatFactoryMoeda;
import br.gov.jfrj.siga.hibernate.ExDao;
import freemarker.cache.TemplateLoader;
import freemarker.core.ParseException;
import freemarker.core.TemplateNumberFormatFactory;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class ProcessadorModeloFreemarker implements ProcessadorModelo,
		TemplateLoader {
    
    final static Pattern patternFreemarkerMarkdown = Pattern.compile(
            "\\[@markdown\\](?<markdown>.+)\\[/@markdown\\]\\s*",
            Pattern.MULTILINE | Pattern.DOTALL);

	private Configuration cfg;

	public ProcessadorModeloFreemarker() {
		super();
		cfg = new Configuration();
		String s = cfg.getVersionNumber();
		// Specify the data source where the template files come from.
		cfg.setTemplateLoader(this);
		// Specify how templates will see the data-model.
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setWhitespaceStripping(true);
		cfg.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
		cfg.setNumberFormat("0.######");
		cfg.setLocalizedLookup(false);
		cfg.setLogTemplateExceptions(false);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		
		Map<String, TemplateNumberFormatFactory> customNumberFormats = new HashMap<>();
		customNumberFormats.put("moeda", FreemarkerFormatFactoryMoeda.INSTANCE);
		cfg.setCustomNumberFormats(customNumberFormats);
	}

	public String processarModelo(CpOrgaoUsuario ou, Map<String, Object> attrs,
			Map<String, Object> params) {
		// Create the root hash
		Map root = new HashMap();
		root.put("root", root);
		root.put("func", new FuncoesEL());
		root.put("fmt", new SigaFormats());
		root.put("exbl", Ex.getInstance().getBL());

		root.putAll(attrs);
		root.put("param", params);
		
		if (attrs.containsKey("entrevista"))
			root.put("gerar_entrevista", true);
		if (attrs.containsKey("processar_modelo"))
			root.put("gerar_documento", true);
		if (attrs.containsKey("formulario"))
			root.put("gerar_formulario", true);
		if (attrs.containsKey("resumo"))
			root.put("gerar_resumo", true);
		if (attrs.containsKey("finalizacao"))
			root.put("gerar_finalizacao", true);
		if (attrs.containsKey("gravacao"))
			root.put("gerar_gravacao", true);
		if (attrs.containsKey("assinatura"))
			root.put("gerar_assinatura", true);
		if (attrs.containsKey("pre_assinatura"))
			root.put("gerar_pre_assinatura", true);
		if (attrs.containsKey("partes"))
			root.put("gerar_partes", true);
		if (attrs.containsKey("descricao"))
			root.put("gerar_descricao", true);
		if (attrs.containsKey("descricaodefault"))
			root.put("gerar_descricaodefault", true);

		String template = (String) attrs.get("template");
		
		if (template != null) {
    		// Se o template não contem entrevista nem documento, processar com JModel
            if (template.contains("[@markdown")) {
                Matcher matcher = patternFreemarkerMarkdown.matcher(template);
                StringBuffer sb = new StringBuffer();
                while (matcher.find()) {
                    String rep = matcher.group("markdown");
                    matcher.appendReplacement(sb, markdownToFreemarker(rep));
                }
                matcher.appendTail(sb);
                template = sb.toString();
            } else if (!template.contains("[@entrevista") && !template.contains("[@documento") && !template.contains("[@descricao") && !template.contains("[@assinatura") && 
                    !template.contains("[@interview") && !template.contains("[@document") && !template.contains("[@description") && 
                    !template.contains("[@markdown") && !template.contains("[@dadosComplementares") && !template.contains("[@extensaoBuscaTextual")) {
    		    template = markdownToFreemarker(template);
    		}
		}

		String templateFinal = "[#compress]\n[#include \"DEFAULT\"][#include \"GERAL\"]\n";
		if (ou != null) {
			templateFinal += "[#include \"" + ou.getAcronimoOrgaoUsu() + "\"]";
		}
		if (attrs.get("template") != null) {
            templateFinal += "\n" + template;
        }
		templateFinal += "\n[/#compress]";

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); Writer out = new OutputStreamWriter(baos, StandardCharsets.UTF_8)) {
			Template temp = new Template((String) attrs.get("nmMod"),
					new StringReader(templateFinal), cfg);
			temp.process(root, out);
			out.flush();
			String processed = baos.toString(StandardCharsets.UTF_8.name());
			
			// Reprocessar para substituir variáveis declaradas nos campos da entrevista
			if (root.get("gerar_documento") != null || root.get("gerar_descricao") != null) {
				baos.reset();

				// Altera para desfazer o HTML encoding de dentro das operações do Freemarker
				Pattern p = Pattern.compile("\\$\\{([^\\}]+)\\}");
				Matcher m = p.matcher(processed);
				StringBuffer sb = new StringBuffer();
				while (m.find()) {
				    String group = m.group(1);
					String unescapeEntities = "\\${" + Parser.unescapeEntities(group, true) + "}";
					m.appendReplacement(sb, unescapeEntities);
				}
				m.appendTail(sb);
				processed = sb.toString();

				// Altera para que não seja necessário aplicar o (...)! manualmente
				processed = processed.replaceAll("\\$\\{([^\\}]+)\\}", "\\${($1)!}");
				
				temp = new Template(((String) attrs.get("nmMod")) + " (post-processing)", new StringReader(processed), cfg);
				temp.process(root, out);
				out.flush();
				processed = baos.toString(StandardCharsets.UTF_8.name());
			}
			
			return processed;
        } catch (TemplateException e) {
            if (e.getCause() != null && e.getCause() instanceof AplicacaoException)
                throw (AplicacaoException) e.getCause();
            throw new RuntimeException("Erro executanto template Freemarker: <pre>\n\n" + com.crivano.jmodel.Utils.escapeHTML(e.getLocalizedMessage())
                    + "\n\n---\n" + com.crivano.jmodel.Utils.addLineNumbers(templateFinal, e.getLineNumber(),
                            e.getEndLineNumber(), e.getColumnNumber(), e.getEndColumnNumber())
                    + "\n---\n\n" + "</pre>", e);
        } catch (ParseException e) {
            if (e.getCause() != null && e.getCause() instanceof AplicacaoException)
                throw (AplicacaoException) e.getCause();
            throw new RuntimeException("Erro compilando template Freemarker: <pre>\n\n" + com.crivano.jmodel.Utils.escapeHTML(e.getLocalizedMessage())
                    + "\n\n---\n" + com.crivano.jmodel.Utils.addLineNumbers(templateFinal, e.getLineNumber(),
                            e.getEndLineNumber(), e.getColumnNumber(), e.getEndColumnNumber())
                    + "\n---\n\n" + "</pre>", e);
        } catch (IOException e) {
            return "Erro executando template FreeMarker\n\n" + e.getMessage();
		} finally {
			root = null;
		}
	}

    private String markdownToFreemarker(String rep) {
        String s = com.crivano.jmodel.Template.markdownToFreemarker(null, rep, null);
        s = s.replace("<table>", "<table class=\"table table-sm table-striped doc-table\">");
        return s;
    }

	public void closeTemplateSource(Object arg0) throws IOException {
	}

	static LoadingCache<String, String> cache = CacheBuilder.newBuilder().maximumSize(1000)
			.expireAfterWrite(Prop.get("debug.default.template.pathname") == null ? 5 : 1,
					Prop.get("debug.default.template.pathname") == null ? TimeUnit.MINUTES : TimeUnit.SECONDS)
			.build(new CacheLoader<String, String>() {
				public String load(String source) throws Exception {
					CpModelo mod;
					if ("DEFAULT".equals(source)) {
						return FreemarkerDefault.getDefaultTemplate();
					} else if ("GERAL".equals(source)) {
						mod = ExDao.getInstance().consultaCpModeloGeral();
					} else {
						mod = ExDao.getInstance().consultaCpModeloPorNome(source);
					}

					String conteudoBlob = "";
					if (mod != null)
						conteudoBlob = mod.getConteudoBlobString() == null ? "" : mod.getConteudoBlobString();
					return conteudoBlob;
				}
			});
	
	public Object findTemplateSource(String source) throws IOException {
		try {
			return cache.get(source);
		} catch (ExecutionException e) {
			throw new IOException("Não foi possível obter o template: " + source, e);
		}
	}

	public long getLastModified(Object arg0) {
		return 0;
	}

	public Reader getReader(Object arg0, String arg1) throws IOException {
		return new StringReader((String) arg0);
	}

}
