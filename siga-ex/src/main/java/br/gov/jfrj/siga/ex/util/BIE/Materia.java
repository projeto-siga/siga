package br.gov.jfrj.siga.ex.util.BIE;

import java.util.Date;

public class Materia {

	private String localidade;

	private String tipoMateria;

	private String numero;

	private String abertura;

	private String conteudo;

	private String fecho;
	
	private Date dt;
	
	private String codigo;

	public Materia() {

	}

	public String getLocalidade() {
		return localidade;

	}

	public String getTipoMateria() {
		return tipoMateria;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getAbertura() {
		return abertura;
	}

	public void setAbertura(String abertura) {
		this.abertura = abertura;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public String getFecho() {
		return fecho;
	}

	public void setFecho(String fecho) {
		this.fecho = fecho;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public void setTipoMateria(String tipoMateria) {
		this.tipoMateria = tipoMateria;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
}
