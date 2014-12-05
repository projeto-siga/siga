package util;

import java.text.SimpleDateFormat;
import java.util.Date;

import models.SrSolicitacao;

public class Cronometro {

	private String descricao;

	private String inicio;

	private Long decorrido;

	private String prazo;
	
	private boolean ligado;

	public boolean isLigado() {
		return ligado;
	}

	public Cronometro setLigado(boolean ligado) {
		this.ligado = ligado;
		return this;
	}

	public String getDescricao() {
		return descricao;
	}

	public Cronometro setDescricao(String descricao) {
		this.descricao = descricao;
		return this;
	}

	public String getInicio() {
		return inicio;
	}

	public String getPrazo() {
		return prazo;
	}

	public Long getDecorrido() {
		return decorrido;
	}

	public Cronometro setInicio(String inicio) {
		this.inicio = inicio;
		return this;
	}

	public Cronometro setDecorrido(Long decorrido) {
		this.decorrido = decorrido;
		return this;
	}

	public Cronometro setPrazo(String prazo) {
		this.prazo = prazo;
		return this;
	}
}
