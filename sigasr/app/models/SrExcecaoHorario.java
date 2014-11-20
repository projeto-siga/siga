package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.DatatypeConverter;

import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "SR_EXCECAO_HORARIO", schema = "SIGASR")
public class SrExcecaoHorario extends Objeto{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_EXCECAO_HORARIO_SEQ", name = "srExcecaoHorarioSeq")
	@GeneratedValue(generator = "srExcecaoHorarioSeq")
	@Column(name = "ID_EXCECAO_HORARIO", nullable = false)
	public Long idExcecaoHorario;
	
	@ManyToOne
	@JoinColumn(name="ID_EQUIPE", nullable = false)
	public SrEquipe equipe;
	
	@Column(name = "DIA_SEMANA")
	@Enumerated(EnumType.ORDINAL)
	public SrSemana diaSemana;
	
	@Column(name = "DT_ESPECIFICA")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataEspecifica;
	
	@Column(name = "HORA_INI")
	@Temporal(TemporalType.TIMESTAMP)
	public Date horaIni;
	
	@Column(name = "HORA_FIM")
	@Temporal(TemporalType.TIMESTAMP)
	public Date horaFim;
	
	@Column(name = "INTER_INI")
	@Temporal(TemporalType.TIMESTAMP)
	public Date interIni;
	
	@Column(name = "INTER_FIM")
	@Temporal(TemporalType.TIMESTAMP)
	public Date interFim;
	
	@Transient
	private String strHoraIni;
	
	@Transient
	private String strHoraFim;
	
	@Transient
	private String strInterIni;
	
	@Transient
	private String strInterFim;

	public String getStrHoraIni() {
		return strHoraIni;
	}

	public void setStrHoraIni(String strHoraIni) {
		this.strHoraIni = strHoraIni;
		
		this.horaIni = DatatypeConverter.parseTime(strHoraIni).getTime();
	}

	public String getStrHoraFim() {
		return strHoraFim;
	}

	public void setStrHoraFim(String strHoraFim) {
		this.strHoraFim = strHoraFim;
		
		this.horaFim = DatatypeConverter.parseTime(strHoraFim).getTime();
	}

	public String getStrInterIni() {
		return strInterIni;
	}

	public void setStrInterIni(String strInterIni) {
		this.strInterIni = strInterIni;
		
		this.interIni = DatatypeConverter.parseTime(strInterIni).getTime();
	}

	public String getStrInterFim() {
		return strInterFim;
	}

	public void setStrInterFim(String strInterFim) {
		this.strInterFim = strInterFim;
		
		this.interFim = DatatypeConverter.parseTime(strInterFim).getTime();
	}
	
}
