package br.gov.jfrj.siga.sr.model;

import java.util.HashMap;
import java.util.Map;

public class SrOperacao implements Comparable<SrOperacao> {

	String icone;
	String nome;
	String url;
	String nomeModal;
	boolean pode;
	String msgConfirmacao;
	String pre;
	String pos;
	Map<String, Object> params;

	public Map<String, Object> getParams() {
		return params;
	}

	public String getNomeNbsp() {
		return nome.replace(" ", "&nbsp;");
	}

	public boolean isPopup() {
		return params.containsKey("popup");
	}

	public boolean isAjax() {
		return params.containsKey("ajax");
	}
	
	public boolean isModal() {
		return params.containsKey("modal");
	}

	public SrOperacao(String icone, String nome, boolean pode) {
		this(icone, nome, null, pode, null, null, null, null);
	}

	public SrOperacao(String icone, String nome, boolean pode, String url) {
		this(icone, nome, url, pode, null, null, null, null);
	}

	public SrOperacao(String icone, String nome, boolean pode, String url,
			String parametros) {
		this(icone, nome, pode, url);
		if (params == null)
			params = new HashMap<String, Object>();
		if (parametros != null)
			for (final String s : parametros.split("&")) {
				final String parametro[] = s.split("=");
				if (parametro.length == 2)
					params.put(parametro[0], parametro[1]);
			}
	}

	public SrOperacao(String icone, String nome, String url, boolean pode,
			String msgConfirmacao, Map<String, Object> map, String pre,
			String pos) {
		super();
		this.icone = icone;
		this.nome = nome;
		this.url = url;
		this.pode = pode;
		this.msgConfirmacao = msgConfirmacao;
		this.params = map;
		this.pre = pre;
		this.pos = pos;
	}

	public String getUrl() {
		return this.url;
	}

	@Override
	public String toString() {
		return pre + " " + nome + " " + pos + " <" + getUrl() + "?"
				+ getParams() + ">";
	}

	@Override
	public int compareTo(SrOperacao o) {
		return nome.compareTo(o.nome);
	}
}
