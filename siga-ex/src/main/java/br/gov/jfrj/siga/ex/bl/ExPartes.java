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

@XmlRootElement(name = "partes")
public class ExPartes {
	private List<ExParte> partes = new ArrayList<>();

	public void calcular() {
		for (ExParte p : partes) {
			p.setAtivo(true);
		}
		for (ExParte p : partes) {
			if (!p.isAtivo())
				continue;
			for (ExDependencia d : p.getDependencias()) {
				ExParte pd = getParte(d.getId());
				if (pd == null || !pd.isPreenchido()) {
					p.setAtivo(false);
					break;
				}
			}
		}
	}

	public static ExPartes unmarshall(String xml) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(ExPartes.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		ExPartes partes = (ExPartes) jaxbUnmarshaller
				.unmarshal(new StringReader(xml));
		return partes;
	}

	private ExParte getParte(String id) {
		for (ExParte p : partes)
			if (id.equals(p.getId()))
				return p;
		return null;
	}

	public List<ExParte> getPartes() {
		return partes;
	}

	@XmlElement(name = "parte")
	public void setPartes(List<ExParte> partes) {
		this.partes = partes;
	}
}
