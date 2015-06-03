package br.gov.jfrj.siga.sr.model;

import java.util.HashMap;
import java.util.Map;

public class SrOperacao implements Comparable<SrOperacao> {

    private String icone;
    private String nome;
    private String url;
    private String nomeModal;
    private boolean pode;
    private String msgConfirmacao;
    private String pre;
    private String pos;
    private Map<String, Object> params;
    
    public SrOperacao(String icone, String nome, boolean pode, String url) {
        this(icone, nome, url, pode, null, null, null, null);
    }
    
    public SrOperacao(String icone, String nome, boolean pode) {
        this(icone, nome, null, pode, null, null, null, null);
    }
    
    public SrOperacao(String icone, String nome, boolean pode, String url, String parametros) {
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

    public SrOperacao(String icone, String nome, String url, boolean pode, String msgConfirmacao, Map<String, Object> map, String pre, String pos) {
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

    public String getIcone() {
		return icone;
	}

	public void setIcone(String icone) {
		this.icone = icone;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeModal() {
		return nomeModal;
	}

	public void setNomeModal(String nomeModal) {
		this.nomeModal = nomeModal;
	}

	public boolean isPode() {
		return pode;
	}

	public void setPode(boolean pode) {
		this.pode = pode;
	}

	public String getMsgConfirmacao() {
		return msgConfirmacao;
	}

	public void setMsgConfirmacao(String msgConfirmacao) {
		this.msgConfirmacao = msgConfirmacao;
	}

	public String getPre() {
		return pre;
	}

	public void setPre(String pre) {
		this.pre = pre;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}
	
    public String getNomeNbsp() {
        return nome.replace(" ", "&nbsp;");
    }

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Map<String, Object> getParams() {
        return params;
    }

	public void setParams(Map<String, Object> params) {
		this.params = params;
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

    public String getUrl() {
        return this.url;
    }

    @Override
    public String toString() {
        return pre + " " + nome + " " + pos + " <" + getUrl() + "?" + getParams() + ">";
    }

    @Override
    public int compareTo(SrOperacao o) {
        return nome.compareTo(o.nome);
    }
    
}
