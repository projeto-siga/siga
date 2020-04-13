package br.gov.jfrj.siga.ex;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * The persistent class for the EX_SEQUENCIA database table.
 * 
 */
@Entity
@Table(name="EX_PROTOCOLO")
@NamedQueries({
	@NamedQuery(name="ExProtocolo.obterProtocoloPorDocumento", query="SELECT e FROM ExProtocolo e where e.exDocumento.idDoc = :idDoc and rownum = :rownum")
	
})

public class ExProtocolo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="EX_PROTOCOLO_GENERATOR", sequenceName="EX_PROTOCOLO_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EX_PROTOCOLO_GENERATOR")
	@Column(name="ID_PROTOCOLO")
	private long idProtocolo;

	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DOC")
	private ExDocumento exDocumento;
	
	@Column(name="NUMERO")
	private long numero;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA", length = 19)
	private java.util.Date data;
	
	@Column(length = 12)
	private String codigo;

	public ExProtocolo() {
	}

	public long getIdProtocolo() {
		return idProtocolo;
	}

	public void setIdProtocolo(long idProtocolo) {
		this.idProtocolo = idProtocolo;
	}

	public ExDocumento getExDocumento() {
		return exDocumento;
	}

	public void setExDocumento(ExDocumento exDocumento) {
		this.exDocumento = exDocumento;
	}

	public long getNumero() {
		return numero;
	}

	public void setNumero(long numero) {
		this.numero = numero;
	}

	public java.util.Date getData() {
		return data;
	}

	public void setData(java.util.Date data) {
		this.data = data;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	
}