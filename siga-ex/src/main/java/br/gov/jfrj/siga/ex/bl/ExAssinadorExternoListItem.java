package br.gov.jfrj.siga.ex.bl;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExAssinadorExternoListItem {
	private String id;
	private String secret;
	private String code;
	private String descr;
	private String kind;
	private String origin;
	private String urlView;
	private String urlHash;
	private String urlSave;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getUrlHash() {
		return urlHash;
	}

	public void setUrlHash(String urlHash) {
		this.urlHash = urlHash;
	}

	public String getUrlSave() {
		return urlSave;
	}

	public void setUrlSave(String urlSave) {
		this.urlSave = urlSave;
	}

	public String getUrlView() {
		return urlView;
	}

	public void setUrlView(String urlView) {
		this.urlView = urlView;
	}

}
