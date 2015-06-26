package br.gov.jfrj.siga.ex.bl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "parte")
public class ExParte {
	private String id;
	private String hash;
	private boolean preenchido;
	private boolean ativo;
	private List<ExDependencia> dependencias = new ArrayList<>();

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

	public List<ExDependencia> getDependencias() {
		return dependencias;
	}

	@XmlElement(name = "depende")
	public void setDependencias(List<ExDependencia> dependencias) {
		this.dependencias = dependencias;
	}

	public String getDescricaoMov() {
		return "id=\"" + getId() + "\" ativo=\"" + (isAtivo() ? 1 : 0)
				+ "\" preenchida=\"" + (isPreenchido() ? 1 : 0) + "\"";
	}

	public boolean isMesmaId(String descricaoMov) throws JAXBException {
		ExParte p = unmarshallParte("<parte "
				+ (descricaoMov == null ? "" : descricaoMov) + "/>");
		return getId().equals(p.getId());
	}

	public static ExParte unmarshallParte(String s) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(ExParte.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		ExParte parte = (ExParte) jaxbUnmarshaller
				.unmarshal(new StringReader(s));
		return parte;
	}

	public boolean isAtivo() {
		return ativo;
	}

	@XmlAttribute(name = "ativo")
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	// public void setAtivoString(String ativo) {
	// this.ativo = "Sim".equals(ativo);
	// }

	public boolean isPreenchido() {
		return preenchido;
	}

	// public void setPreenchido(String preenchido) {
	// this.preenchido = "Sim".equals(preenchido);
	// }

	@XmlAttribute
	public void setPreenchido(boolean preenchido) {
		this.preenchido = preenchido;
	}

}
