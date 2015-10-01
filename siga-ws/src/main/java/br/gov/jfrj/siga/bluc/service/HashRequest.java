package br.gov.jfrj.siga.bluc.service;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class HashRequest {
	private String policy;
	private String sha1;
	private String sha256;
	private String time;
	private String certificate;
	private String crl;

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getCrl() {
		return crl;
	}

	public void setCrl(String crl) {
		this.crl = crl;
	}

}