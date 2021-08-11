package br.gov.jfrj.siga.ex;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the EX_TIPO_SEQUENCIA database table.
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "siga.ex_tipo_sequencia")
public class ExTipoSequencia implements Serializable {
	
	
	@Id
	@SequenceGenerator(name="EX_TIPO_SEQUENCIA_NUMERACAO_GENERATOR", sequenceName="EX_TIPO_SEQUENCIA_SEQ")
	@GeneratedValue(generator="EX_TIPO_SEQUENCIA_NUMERACAO_GENERATOR")
	@Column(name="ID_TIPO_SEQ")
	private long idTipoSequencia;

	@Column(name="NOME")
	private String nome;
	
	@Column(name="ZERAR_INICIO_ANO")
	private String zerarInicioAno;

	
	public Long getidTipoSequencia() {
		return this.idTipoSequencia;
	}

	public void setidTipoSequencia(Long idTipoSequencia) {
		this.idTipoSequencia = idTipoSequencia;
	}
	
	public String getnome() {
		return this.nome;
	}

	public void setnome(String nome) {
		this.nome = nome;
	}
	
	public String getZerarInicioAno() {
		return zerarInicioAno;
	}

	public void setZerarInicioAno(String zerarInicioAno) {
		this.zerarInicioAno = zerarInicioAno;
	}

}