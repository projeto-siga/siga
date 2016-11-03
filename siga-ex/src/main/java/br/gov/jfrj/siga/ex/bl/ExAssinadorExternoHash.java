package br.gov.jfrj.siga.ex.bl;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExAssinadorExternoHash {
	private String sha1;
	private String sha256;
	private String doc;
	private String policy;
	private String secret;

	public String getSha1() {
		return sha1;
	}

	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}

	public String getSha256() {
		return sha256;
	}

	public void setSha256(String sha256) {
		this.sha256 = sha256;
	}

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
}
