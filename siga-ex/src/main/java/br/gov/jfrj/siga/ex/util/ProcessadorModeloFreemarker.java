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
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpModelo;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ProcessadorModeloFreemarker implements ProcessadorModelo,
		TemplateLoader {

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
	}

	public String processarModelo(CpOrgaoUsuario ou, Map<String, Object> attrs,
			Map<String, Object> params) {
		// Create the root hash
		Map root = new HashMap();
		root.put("root", root);
		root.put("func", new FuncoesEL());
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

		String sTemplate = "[#compress]\n[#include \"DEFAULT\"][#include \"GERAL\"]\n";
		if (ou != null) {
			sTemplate += "[#include \"" + ou.getAcronimoOrgaoUsu() + "\"]";
		}
		if (attrs.get("template") != null)
			sTemplate += "\n" + (String) attrs.get("template");
		sTemplate += "\n[/#compress]";

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); Writer out = new OutputStreamWriter(baos)) {
			Template temp = new Template((String) attrs.get("nmMod"),
					new StringReader(sTemplate), cfg);
			temp.process(root, out);
			out.flush();
			return baos.toString();
		} catch (TemplateException e) {
			if (e.getCauseException() != null
					&& e.getCauseException() instanceof AplicacaoException)
				throw (AplicacaoException) e.getCauseException();
			return ("Erro executando template FreeMarker\n\n" + e.getMessage() + "\n" + e.getFTLInstructionStack())
					.replace("\n", "<br/>").replace("\r", "");
		} catch (IOException e) {
			return "Erro executando template FreeMarker\n\n" + e.getMessage();
		} finally {
			root = null;
		}
	}

	public void closeTemplateSource(Object arg0) throws IOException {
	}

	static LoadingCache<String, String> cache = CacheBuilder.newBuilder().maximumSize(1000)
			.expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<String, String>() {
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
