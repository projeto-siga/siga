package br.gov.jfrj.relatorio.dinamico;

import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;

public class Coluna extends AbstractColumn{
	private String titulo;
	private String nome;
	private Integer tamanho;
	private Integer alinhamento;  
	private boolean agrupado;
	private boolean hyperlink;
	private Class tipo;

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

	@Override
	public String getGroupVariableName(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInitialExpression(DJCalculation arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTextForExpression() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValueClassNameForExpression() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVariableClassName(DJCalculation arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}