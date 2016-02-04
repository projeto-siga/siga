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
	private String titulo;
	private String hash;
	private String responsavel;
	private String mensagem;
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

	@XmlAttribute(name = "depende")
	public void setDepende(String depende) {
		if (this.dependencias.size() > 0)
			return;
		for (String s : depende.split(";")) {
			ExDependencia d = new ExDependencia();
			d.setId(s.trim());
			this.dependencias.add(d);
		}
	}

	public List<ExDependencia> getDependencias() {
		return dependencias;
	}

	@XmlElement(name = "depende")
	public void setDependencias(List<ExDependencia> dependencias) {
		this.dependencias = dependencias;
	}

	public String getDescricaoMov() {

		String deps = "";
		for (ExDependencia d : this.dependencias) {
			if (deps.length() > 0)
				deps += ";";
			deps += d.getId();
		}

		return "id=\""
				+ getId()
				+ (deps.length() > 0 ? "\" depende=\"" + deps : "")
				+ "\" titulo=\""
				+ getTitulo()
				+ "\" ativo=\""
				+ (isAtivo() ? 1 : 0)
				+ "\" preenchido=\""
				+ (isPreenchido() ? 1 : 0)
				+ "\" responsavel=\""
				+ getResponsavel()
				+ "\""
				+ (getMensagem() != null ? " mensagem=\"" + getMensagem()
						+ "\"" : "");
	}

	public String getString() {
		return getTitulo() + (isAtivo() ? ", Ativa" : "")
				+ (isPreenchido() ? ", Preenchida" : "") + ", Responsavel: "
				+ getResponsavel() + "";
	}

	public boolean isMesmaId(String descricaoMov) throws JAXBException {
		return getId().equals(create(descricaoMov).getId());
	}

	public static ExParte unmarshallParte(String s) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(ExParte.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		ExParte parte = (ExParte) jaxbUnmarshaller
				.unmarshal(new StringReader(s));
		return parte;
	}

	public static ExParte create(String descricaoMov) {
		ExParte p;
		try {
			p = unmarshallParte("<parte "
					+ (descricaoMov == null ? "" : descricaoMov) + "/>");
		} catch (JAXBException e) {
			throw new RuntimeException("Erro ao converter string em ExParte.",
					e);
		}
		return p;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public boolean isPendente() {
		return isAtivo() && !isPreenchido();
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

	public String getResponsavel() {
		return responsavel;
	}

	@XmlAttribute(name = "responsavel")
	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getTitulo() {
		return titulo;
	}

	@XmlAttribute(name = "titulo")
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	@XmlAttribute(name = "mensagem")
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}
