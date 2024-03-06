
package br.gov.jfrj.siga.ex.api.v1;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IBiListarGet;
import br.gov.jfrj.siga.hibernate.ExDao;

@AcessoPublico
public class BiListarGet implements IBiListarGet {
	private static Pattern regexReal = Pattern
			.compile("^(?:R\\$)?(?<valor>(([1-9]\\d{0,2}(\\.\\d{3})*)|(([1-9]\\d*)?\\d))(,\\d\\d))?$");

	public static class DocData {
		String codigo;
		Map<String, String> campo = new HashMap<>();

		public String getColuna(String col) {
			switch (col) {
			case "codigo":
				return codigo;
			}
			return null;
		}
	}

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {

		// Verifica o acesso
		{
			String pwd = Prop.get("/siga.bi.password");
			if (pwd == null)
				throw new RuntimeException("BI deve ser ativado informando a propriedade siga.bi.password");
			String auth = SwaggerServlet.getHttpServletRequest().getHeader("Authorization");
			if (!pwd.equals(auth))
				throw new RuntimeException(
						"Propriedade siga.bi.password não confere com o valor recebido no cabeçalho Authorization");
		}

		// Modelo
		ExModelo modelo = null;
		if (req.modelo != null && !req.modelo.trim().isEmpty()) {
			modelo = ExDao.getInstance().consultarExModelo(null, req.modelo);
			if (modelo == null)
				throw new SwaggerException("Não existe modelo com o nome especificado (" + req.modelo + ")", 400, null,
						req, resp, null);
		}

		// Prepara a lista ordenada de campos e o mapa de títulos dos campos
		//
		List<String> campos = new ArrayList<>();
		Map<String, String> titulosDosCampos = new HashMap<>();
		if (req.campos != null) {
			for (String s : req.campos.split(",")) {
				String split[] = s.split("=", 2);
				String identificadorDoCampo = split.length == 2 ? split[1] : split[0];
				String tituloDoCampo = split[0];
				campos.add(identificadorDoCampo);
				titulosDosCampos.put(identificadorDoCampo, tituloDoCampo);
			}
		}

		// Carrega todos os documentos para mapas. Aqui poderíamos otimizar para não
		// gerar dados de campos que não serão utilizados depois.
		//
		SortedSet<String> todosOsCampos = new TreeSet<>();
		List<DocData> lista = new ArrayList<>();
		int LINES = 1000;
		List<ExDocumento> l = ExDao.getInstance().consultarDocumentosPorModeloEData(modelo, null, null);
		if (l.isEmpty())
			throw new SwaggerException("Nenhum documento foi encontrado com os argumentos informados.", 404, null, req,
					resp, null);
		for (ExDocumento doc : l) {
			if (doc.isCancelado() || doc.isSemEfeito())
				continue;
			DocData dd = new DocData();
			dd.codigo = doc.getCodigo();
			for (Entry<String, String> entry : doc.getForm().entrySet()) {
				if (entry.getValue() == null || entry.getValue().trim().isEmpty())
					continue;
				String key = "campo_" + Texto.slugify(entry.getKey(), true, true);
				dd.campo.put(key, entry.getValue());
				todosOsCampos.add(key);
			}
			lista.add(dd);
		}

		// Prepara a lista ordenada de campos e o mapa de títulos dos campos quando o
		// parâmetros "campos" não foi informado
		//
		if (req.campos == null) {
			campos.add("codigo");
			campos.addAll(todosOsCampos);
			for (String s : campos) {
				titulosDosCampos.put(s, s);
			}
		}

		// Gera o CSV
		//
		String delimitador = req.delimitador == null ? "," : req.delimitador;
		String quebraDeLinha = req.quebraDeLinha == null ? "\r\n" : req.quebraDeLinha;
		StringBuilder sb = new StringBuilder();

		// Cabecalho
		for (String s : campos) {
			if (sb.length() > 0)
				sb.append(delimitador);
			sb.append(titulosDosCampos.get(s));
		}
		sb.append(quebraDeLinha);

		// Cada linha
		for (DocData dd : lista) {
			boolean primeiro = true;
			for (String s : campos) {
				if (!titulosDosCampos.containsKey(s))
					continue;
				if (!primeiro)
					sb.append(delimitador);
				add(dd, s, sb);
				primeiro = false;
			}
			sb.append(quebraDeLinha);
		}

		byte[] ab = sb.toString().getBytes(StandardCharsets.UTF_8);

		resp.contentdisposition = "attachment;filename=" + Texto.slugify(req.modelo, true, false) + ".csv";
		resp.contentlength = (long) ab.length;
		resp.contenttype = "text/csv";
		resp.inputstream = new ByteArrayInputStream(ab);
	}

	private static void add(DocData dd, String campo, StringBuilder sb) {
		String v;
		if (campo.startsWith("campo_")) {
			v = dd.campo.get(campo);
			if (v != null) {
				Matcher m = regexReal.matcher(v);
				if (m.matches())
					v = m.group("valor").replace(".", "").replace(",", ".");
			}
		} else
			v = dd.getColuna(campo);
		
		if (v!=null) {
			Document doc = Jsoup.parse(v);
			Element p= doc.select("p").first();
			 
			v = (p!=null ? p.text():v); //some bold text
		}
		
		sb.append(v == null ? "" : v.trim().replaceAll("\n|\r\n", "%0A"));

	}

	@Override
	public String getContext() {
		return "localizar documento mais recente";
	}

}
