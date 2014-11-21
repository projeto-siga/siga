package models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.SrProcessadorScript;

public class SrIndicador {

	/*
	 * @Lob
	 * 
	 * @Column(name = "CONTEUDO_BLOB", length = 8192) public String
	 * conteudoBlob;
	 */

	private String nomeIndicador;

	public String calcular() throws Exception{
		SrProcessadorScript s = new SrProcessadorScript();
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("codigo", getConteudoBlobString());
		attrs.put("lista",
				Arrays.asList(new String[] { "manga", "pêssego", "abacaxi" }));
		List<String> lista = Arrays.asList(new String[] { "manga", "pêssego",
				"abacaxi" });
		return s.executar(attrs);
	}

	public String getConteudoBlobString() {
		return "String concat = \"\"; for (String s : lista) concat += (concat.length() > 0 ? \", \" : \"\") + s; return concat;";
	}

	public String getNomeIndicador() {
		return "indicadorDeTeste";
	}

}
