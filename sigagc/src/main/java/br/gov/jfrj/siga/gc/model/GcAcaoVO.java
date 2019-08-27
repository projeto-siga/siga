package br.gov.jfrj.siga.gc.model;

import java.util.Map;

public class GcAcaoVO implements Comparable<GcAcaoVO> {
	String icone;
	String nome;
	String url;
	boolean pode;
	boolean modal;
	String msgConfirmacao;
	String pre;
	String pos;
	String idAjax;
	Map<String, Object> params;

	public Map<String, Object> getParams() {
		return params;
	}

	public String getPre() {
		return pre;
	}

	public String getPos() {
		return pos;
	}

	public String getIcone() {
		return icone;
	}

	public String getNome() {
		return nome;
	}

	public String getNomeNbsp() {
		return getNome().replace(" ", "&nbsp;");
	}

	public boolean isPode() {
		return pode;
	}

	public boolean isPopup() {
		return params.containsKey("popup");
	}

	public boolean isAjax() {
		return params.containsKey("ajax");
	}

	public String getMsgConfirmacao() {
		return msgConfirmacao;
	}

	public GcAcaoVO(String icone, String nome, String url, boolean pode,
			String msgConfirmacao, Map<String, Object> map, String pre,
			String pos) {
		super();
		this.icone = icone;
		this.nome = nome;
		this.url = url;
		// if (acao.equals("exibir"))
		// acao = "exibir_vo";

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
		return getPre() + " " + getNome() + " " + getPos() + " <" + getUrl()
				+ "?" + getParams() + ">";
	}

	@Override
	public int compareTo(GcAcaoVO o) {
		return nome.compareTo(o.nome);
	}
}
