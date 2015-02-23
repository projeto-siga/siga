package util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.Interval;

import models.SrSolicitacao;

public class Cronometro {

	private String descricao;

	private Date inicio;

	private Date fim;
	
	private Long decorrido;
	
	private Long restante;

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

	public String getInicioString() {
		if (inicio != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return df.format(inicio);
		}
		return "";
	}
	
	public Date getInicio() {
		return inicio;
	}

	public Cronometro setInicio(Date inicio) {
		this.inicio = inicio;
		return this;
	}

	public String getFimString() {
		if (fim != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return df.format(fim);
		}
		return "";
	}
	
	public Date getFim() {
		return fim;
	}

	public Cronometro setFim(Date fim) {
		this.fim = fim;
		return this;
	}
	
	public Long getDecorrido() {
		return decorrido;
	}

	public void setDecorrido(Long decorrido) {
		this.decorrido = decorrido;
	}

	public Long getRestante() {
		return restante;
	}

	public void setRestante(Long restante) {
		this.restante = restante;
	}

}
