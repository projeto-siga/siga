package util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.Interval;

import models.SrSolicitacao;

public class Cronometro {

	private String descricao;

	private Date inicio;

	private Date fim;

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
		if (inicio != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return df.format(inicio);
		}
		return "";
	}

	public Cronometro setInicio(Date inicio) {
		this.inicio = inicio;
		return this;
	}

	public String getFim() {
		if (fim != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return df.format(fim);
		}
		return "";
	}

	public Cronometro setFim(Date fim) {
		this.fim = fim;
		return this;
	}

	public Long getRemanescente() {
		if (!isLigado() || fim == null)
			return null;
		Date now = new Date();
		if (now.before(fim))
			return new Interval(new Date().getTime(), fim.getTime())
					.toDurationMillis();
		else
			return new Interval(fim.getTime(), new Date().getTime())
					.toDurationMillis() * -1;
	}

}
