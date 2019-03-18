package br.gov.jfrj.relatorio.dinamico;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Coluna {
	private String titulo;
	private String nome;
	private Integer tamanho;
	private Integer alinhamento;  
	private boolean agrupado;
	private boolean hyperlink;
	private Class tipo;
	private String padrao;
	private DecimalFormat formato;
	
	public String getPadrao() {
		return padrao;
	}

	public void setPadrao(String padrao) {
		this.padrao = padrao;
	}
	
	public void setFormato(DecimalFormat formato) {
		this.formato = formato;
		
	}
	
	public DecimalFormat getFormato() {
		return formato;
		
	}

	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String titulo) {
		this.nome = nome;
	}
	
	public Integer getTamanho() {
		return tamanho;
	}

	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}

	public Integer getAlinhamento() {
		return alinhamento;
	}

	public void setAlinhamento(Integer alinhamento) {
		this.alinhamento = alinhamento;
	}

	public boolean isAgrupado() {
		return agrupado;
	}

	public void setAgrupado(boolean agrupado) {
		this.agrupado = agrupado;
	}

	public boolean isHyperlink() {
		return hyperlink;
	}

	public void setHyperlink(boolean hyperlink) {
		this.hyperlink = hyperlink;
	}

	public Class getTipo() {
		return tipo;
	}

	public void setTipo(Class tipo) {
		this.tipo = tipo;
	}


	

}