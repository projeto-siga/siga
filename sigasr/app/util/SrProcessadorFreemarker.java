package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import br.gov.jfrj.siga.base.AplicacaoException;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class SrProcessadorFreemarker implements TemplateLoader {

	private Configuration cfg;

	public SrProcessadorFreemarker() {
		super();
		cfg = new Configuration();
		// String s = cfg.getVersionNumber();
		// Specify the data source where the template files come from.
		cfg.setTemplateLoader(this);
		// Specify how templates will see the data-model.
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setWhitespaceStripping(true);
		cfg.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
		cfg.setNumberFormat("0.######");
		cfg.setLocalizedLookup(false);
	}

	public String processarModelo(Map<String, Object> attrs) throws Exception {
		// Create the root hash
		Map root = new HashMap();
		root.put("root", root);
		root.putAll(attrs);

		String sTemplate = "[#compress]" + (String) attrs.get("template")
				+ "\n[/#compress]";

		Template temp = new Template((String) attrs.get("nmMod"),
				new StringReader(sTemplate), cfg);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Writer out = new OutputStreamWriter(baos);
		try {
			temp.process(root, out);
		} catch (TemplateException e) {
			if (e.getCauseException() != null
					&& e.getCauseException() instanceof AplicacaoException)
				throw e.getCauseException();
			return (e.getMessage() + "\n" + e.getFTLInstructionStack())
					.replace("\n", "<br/>").replace("\r", "");
		} catch (IOException e) {
			return e.getMessage();
		}
		out.flush();
		return baos.toString();
	}

	public void closeTemplateSource(Object arg0) throws IOException {
	}

	@Override
	public Object findTemplateSource(String source) throws IOException {
		return "";
	}

	public long getLastModified(Object arg0) {
		return 0;
	}

	public Reader getReader(Object arg0, String arg1) throws IOException {
		return new StringReader((String) arg0);
	}

}
