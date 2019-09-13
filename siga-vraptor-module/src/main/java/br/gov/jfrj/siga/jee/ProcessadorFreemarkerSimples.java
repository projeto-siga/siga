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
package br.gov.jfrj.siga.jee;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import br.gov.jfrj.siga.cp.CpModelo;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ProcessadorFreemarkerSimples implements TemplateLoader {

	private Configuration cfg;

	public ProcessadorFreemarkerSimples() {
		super();
		cfg = new Configuration();
		String s = cfg.getVersionNumber();
		// Specify the data source where the template files come from.
		cfg.setTemplateLoader(this);
		// Specify how templates will see the data-model.
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setWhitespaceStripping(true);
		cfg.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
		cfg.setLocalizedLookup(false);
	}

	public String processarModelo(CpOrgaoUsuario ou, Map<String, Object> attrs,
			Map<String, Object> params) throws Exception {
		// Create the root hash
		Map root = new HashMap();
		root.put("root", root);

		root.putAll(attrs);
		root.put("param", params);

		String sTemplate = "[#compress]\n[#include \"DEFAULT\"][#include \"GERAL\"]\n";
		if (ou != null) {
			sTemplate += "[#include \"" + ou.getAcronimoOrgaoUsu() + "\"]";
		}
		sTemplate += "\n" + (String) attrs.get("template") + "\n[/#compress]";

		Template temp = new Template((String) attrs.get("nmMod"),
				new StringReader(sTemplate), cfg);

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Writer out = new OutputStreamWriter(baos)) {
			temp.process(root, out);
			out.flush();
			return baos.toString();
		} catch (TemplateException e) {
			return (e.getMessage() + "\n" + e.getFTLInstructionStack())
					.replace("\n", "<br/>").replace("\r", "");
		} catch (IOException e) {
			return e.getMessage();
		}
	}

	public void closeTemplateSource(Object arg0) throws IOException {
	}

	static LoadingCache<String, String> cache = CacheBuilder.newBuilder().maximumSize(1000)
			.expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<String, String>() {
				public String load(String source) throws Exception {
					CpModelo mod;
					if ("DEFAULT".equals(source)) {
						return FreemarkerSimplesDefault.getDefaultTemplate();
					} else if ("GERAL".equals(source)) {
						mod = CpDao.getInstance().consultaCpModeloGeral();
					} else {
						mod = CpDao.getInstance().consultaCpModeloPorNome(source);
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
