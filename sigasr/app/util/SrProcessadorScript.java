package util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bsh.EvalError;
import bsh.Interpreter;
import edu.emory.mathcs.backport.java.util.Arrays;

public class SrProcessadorScript {

	public String executar(Map<String, Object> attrs) throws Exception {
		Interpreter bsh = new Interpreter();
		String codigo = "import models.*;";
		for (String key : attrs.keySet()) {
			if (key.equals("codigo"))
				codigo = codigo + (String) attrs.get(key);
			else
				bsh.set(key, attrs.get(key));
		}
		return (String) bsh.eval(codigo);
	}
	
	public static void main(String[] args) throws Exception {
		SrProcessadorScript s = new SrProcessadorScript();
		StringBuffer sb = new StringBuffer();
		sb.append("SrSolicitacao sol = new SrSolicitacao();");
		sb.append("System.out.println(sol.isRascunho());");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("codigo", sb.toString());
		
		s.executar(map);
	}

}
