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

@Entity
@Table(name = "SR_EXCECAO_HORARIO", schema = "SIGASR")
public class SrExcecaoHorario {
	
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
	@Temporal(TemporalType.TIME)
	public Date horaIni;
	
	@Column(name = "HORA_FIM")
	@Temporal(TemporalType.TIME)
	public Date horaFim;
	
	@Column(name = "INTER_INI")
	@Temporal(TemporalType.TIME)
	public Date interIni;
	
	@Column(name = "INTER_FIM")
	@Temporal(TemporalType.TIME)
	public Date interFim;
	
}
