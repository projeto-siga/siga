package br.gov.jfrj.siga.bluc.service;

import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EnvelopeResponse {
	private String cn;
	private String policy;
	private String policyversion;
	private String policyoid;
	private String envelope;
	private String errormsg;

	private Map<String, String> certdetails = new TreeMap<>();

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getPolicyversion() {
		return policyversion;
	}

	public void setPolicyversion(String policyversion) {
		this.policyversion = policyversion;
	}

	public String getPolicyoid() {
		return policyoid;
	}

	public void setPolicyoid(String policyoid) {
		this.policyoid = policyoid;
	}

	
	public String getEnvelope() {
		return envelope;
	}

	public void setEnvelope(String envelope) {
		this.envelope = envelope;
	}

	public Map<String, String> getCertdetails() {
		return certdetails;
	}

	public void setCertdetails(Map<String, String> certdetails) {
		this.certdetails = certdetails;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

}