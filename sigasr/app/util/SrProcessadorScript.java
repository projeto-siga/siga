package util;

import java.util.List;
import java.util.Map;

import bsh.EvalError;
import bsh.Interpreter;
import edu.emory.mathcs.backport.java.util.Arrays;

public class SrProcessadorScript {

	public String executar(Map<String, Object> attrs) throws Exception {
		Interpreter bsh = new Interpreter();
		String codigo = "";
		for (String key : attrs.keySet()) {
			if (key.equals("codigo"))
				codigo = (String) attrs.get(key);
			else
				bsh.set(key, attrs.get(key));
		}
		return (String) bsh.eval(codigo);
	}

}
