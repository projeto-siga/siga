package br.gov.jfrj.siga.ex.bl;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "depende")
public class ExDependencia {
	private String id;
	private String hash;

	public String getId() {
		return id;
	}

	@XmlAttribute
	public void setId(String id) {
		this.id = id;
	}

	public String getHash() {
		return hash;
	}

	@XmlAttribute
	public void setHash(String hash) {
		this.hash = hash;
	}
}
