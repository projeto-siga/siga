package br.gov.jfrj.siga.wf.util;

import java.util.TreeMap;

import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.base.AcaoVO;

public class WfAcaoHelper {

	public static AcaoVO criarAcao(String icone, String nome, String nameSpace, String acao, Expression pode) {
		boolean f = pode.eval();
		return new AcaoVO(icone, nome, nameSpace, acao, f, formatarExplicacao(pode, f), null, null, null, null, null,
				null);
	}

	public static AcaoVO criarAcao(String icone, String nome, String nameSpace, String acao, Expression pode,
			String msgConfirmacao, TreeMap<String, String> params, String pre, String pos, String classe,
			String modal) {
		boolean f = pode.eval();
		return new AcaoVO(icone, nome, nameSpace, acao, f, formatarExplicacao(pode, f), msgConfirmacao, params, pre,
				pos, classe, modal);
	}

	public static String formatarExplicacao(Expression exp, boolean f) {
		String s = exp.explain(f).replace("_not_", "não está");
		s = s.replace("_and_", "e").replace("_or_", "ou");
		s = "Está " + (f ? "h" : "des") + "abilitado porque " + s;
		return s;
	};

}
