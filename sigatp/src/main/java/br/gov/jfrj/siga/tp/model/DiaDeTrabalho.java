package br.gov.jfrj.siga.tp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;

@Entity
@Audited
@Table(name = "diadetrabalho", schema = "sigatp")
public class DiaDeTrabalho extends TpModel implements Comparable<DiaDeTrabalho> {

	private static final long serialVersionUID = 1L;
	public static ActiveRecord<DiaDeTrabalho> AR = new ActiveRecord<>(DiaDeTrabalho.class);

	@Id
	@GeneratedValue(generator = "hibernate_sequence_generator")
	@SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "sigatp.hibernate_sequence")
	private Long id;

	@Enumerated(EnumType.STRING)
	private DiaDaSemana diaEntrada;

	@NotNull
	// @As(binder=HourMinuteBinder.class)
	private Calendar horaEntrada;

	@Enumerated(EnumType.STRING)
	private DiaDaSemana diaSaida;

	@NotNull
	// @As(binder=HourMinuteBinder.class)
	private Calendar horaSaida;

	@NotNull
	@ManyToOne
	private EscalaDeTrabalho escalaDeTrabalho;

	public DiaDeTrabalho() {
		Inicializar();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DiaDaSemana getDiaEntrada() {
		return diaEntrada;
	}

	public void setDiaEntrada(DiaDaSemana diaEntrada) {
		this.diaEntrada = diaEntrada;
	}

	public Calendar getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(Calendar horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	public DiaDaSemana getDiaSaida() {
		return diaSaida;
	}

	public void setDiaSaida(DiaDaSemana diaSaida) {
		this.diaSaida = diaSaida;
	}

	public Calendar getHoraSaida() {
		return horaSaida;
	}

	public void setHoraSaida(Calendar horaSaida) {
		this.horaSaida = horaSaida;
	}

	public EscalaDeTrabalho getEscalaDeTrabalho() {
		return escalaDeTrabalho;
	}

	public void setEscalaDeTrabalho(EscalaDeTrabalho escalaDeTrabalho) {
		this.escalaDeTrabalho = escalaDeTrabalho;
	}

	private void Inicializar() {
		SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		this.id = new Long(0);
		this.diaEntrada = DiaDaSemana.SEGUNDA;
		this.diaSaida = DiaDaSemana.SEGUNDA;
		this.horaEntrada = Calendar.getInstance();
		this.horaSaida = Calendar.getInstance();
		try {
			this.horaEntrada.setTime(formatar.parse("01/01/1900 11:00"));
			this.horaSaida.setTime(formatar.parse("01/01/1900 19:00"));
		} catch (ParseException e) {
			//throw new RuntimeException(new I18nMessage("diaDeTrabalho", "diaDeTrabalho.inicializar.exception").getMessage());
			throw new RuntimeException(MessagesBundle.getMessage("diaDeTrabalho.inicializar.exception"));
			
		}
		escalaDeTrabalho = null;

	}

	public DiaDeTrabalho(EscalaDeTrabalho escala) {
		Inicializar();
		escalaDeTrabalho = escala;
	}

	public String getHoraEntradaFormatada() {
		SimpleDateFormat formatar = new SimpleDateFormat("HH:mm");
		return formatar.format(horaEntrada.getTime());
	}

	public String getHoraSaidaFormatada() {
		SimpleDateFormat formatar = new SimpleDateFormat("HH:mm");
		return formatar.format(horaSaida.getTime());
	}

	public String getHoraEntradaFormatada(String formato) {
		SimpleDateFormat formatar = new SimpleDateFormat(formato);
		return formatar.format(horaEntrada.getTime());
	}

	public String getHoraSaidaFormatada(String formato) {
		SimpleDateFormat formatar = new SimpleDateFormat(formato);
		return formatar.format(horaSaida.getTime());
	}

	@Override
	public int compareTo(DiaDeTrabalho o) {
		return (this.diaEntrada.getOrdem() + this.getHoraEntradaFormatada("HH:mm")).compareTo((o.diaEntrada.getOrdem() + o.getHoraSaidaFormatada("HH:mm")));
	}

	@Override
	public String toString() {
		StringBuffer saida = new StringBuffer();
		saida.append(diaEntrada.getNomeAbreviado() + " " + getHoraEntradaFormatada() + " / ");
		if (!diaEntrada.equals(diaSaida)) {
			saida.append(diaSaida.getNomeAbreviado() + " ");
		}

		saida.append(getHoraSaidaFormatada());
		return saida.toString();
	}

}
