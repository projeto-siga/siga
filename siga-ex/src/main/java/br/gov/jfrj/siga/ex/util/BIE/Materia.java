package br.gov.jfrj.siga.ex.util.BIE;

import java.util.Date;

public class Materia {

	private Localidade localidade;
	
	private Unidade unidade;

	private TipoMateria tipoMateria;

	private String numero;

	private String abertura;

	private String conteudo;

	private String fecho;
	
	private Date dt;

	public Materia() {

	}

	public Localidade getLocalidade() {
		return localidade;

	}

	public TipoMateria getTipoMateria() {
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

	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
	}

	public void setTipoMateria(TipoMateria tipoMateria) {
		this.tipoMateria = tipoMateria;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

}
