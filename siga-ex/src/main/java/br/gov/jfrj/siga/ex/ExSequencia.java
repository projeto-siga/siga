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
 * The persistent class for the EX_SEQUENCIA database table.
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "siga.ex_sequencia")
@NamedQueries({
	@NamedQuery(name="ExSequencia.findAll", query="SELECT e FROM ExSequencia e"),
	@NamedQuery(name="ExSequencia.findById", query="SELECT e.idSequencia FROM ExSequencia e where e.idSequencia = :id "),
	@NamedQuery(name="ExSequencia.obterSequencia", query="SELECT e "
			+ "FROM ExSequencia e "
			+ "where e.tipoSequencia = :tipoSequencia "
			+ "and e.anoEmissao = :anoEmissao "
			+ "and e.flAtivo = :flAtivo"),
	@NamedQuery(name="ExSequencia.obterNumeroGerado", query="SELECT e.numero "
			+ "FROM ExSequencia e "
			+ "where e.tipoSequencia = :tipoSequencia "
			+ "and e.anoEmissao = :anoEmissao "
			+ "and e.flAtivo = :flAtivo"),
	@NamedQuery(name="ExSequencia.incrementaSequencia", query="UPDATE ExSequencia e SET e.numero = e.numero + :increment WHERE e.idSequencia = :id"),
	@NamedQuery(name="ExSequencia.existeRangeSequencia", query="SELECT e FROM ExSequencia e "
			+ "where e.tipoSequencia = :tipoSequencia "
			+ "Order by e.anoEmissao desc"),
	@NamedQuery(name="ExSequencia.mantemRangeNumero", query="UPDATE ExSequencia e SET e.numero = e.numero + :increment, e.anoEmissao = :anoEmissao,  e.flAtivo = :flAtivo WHERE e.idSequencia = :id")

	
})

public class ExSequencia implements Serializable {
	
	public enum ExSequenciaEnum {
		 
	    PROTOCOLO(1);
	 
	    private Integer valor;
	 
	    ExSequenciaEnum(Integer valor) {
	        this.valor = valor;
	    }
	 
	    public Integer getValor() {
	        return valor;
	    }
	}
	
	@Id
	@SequenceGenerator(name="EX_SEQUENCIA_NUMERACAO_GENERATOR", sequenceName="EX_SEQUENCIA_SEQ")
	@GeneratedValue(generator="EX_SEQUENCIA_NUMERACAO_GENERATOR")
	@Column(name="ID_SEQ")
	private long idSequencia;

	@Column(name="ANO_EMISSAO")
	private Long anoEmissao;

	@Column(name="FL_ATIVO")
	private String flAtivo;

	@Column(name="NUMERO")
	private Long numero;

	@Column(name="NR_FINAL")
	private Long nrFinal;

	@Column(name="NR_INICIAL")
	private Long nrInicial;
	
	@Column(name="TIPO_SEQUENCIA")
	private Integer tipoSequencia;
	
	@Column(name="ZERAR_INICIO_ANO")
	private String zerarInicioAno;

	public ExSequencia() {
	}

	public long getIdSequencia() {
		return this.idSequencia;
	}

	public void setIdSequencia(long idSequencia) {
		this.idSequencia = idSequencia;
	}

	public Long getAnoEmissao() {
		return this.anoEmissao;
	}

	public void setAnoEmissao(Long anoEmissao) {
		this.anoEmissao = anoEmissao;
	}

	public String getFlAtivo() {
		return this.flAtivo;
	}

	public void setFlAtivo(String flAtivo) {
		this.flAtivo = flAtivo;
	}

	public Long getNumero() {
		return this.numero;
	}

	public void setNUmero(Long numero) {
		this.numero = numero;
	}

	public Long getNrFinal() {
		return this.nrFinal;
	}

	public void setNrFinal(Long nrFinal) {
		this.nrFinal = nrFinal;
	}

	public Long getNrInicial() {
		return this.nrInicial;
	}

	public void setNrInicial(Long nrInicial) {
		this.nrInicial = nrInicial;
	}
	
	public void incrementaNumeroDocumento() {
		this.numero = numero + 1;
	}

	public Integer getTipoSequencia() {
		return tipoSequencia;
	}

	public void setTipoSequencia(Integer tipoSequencia) {
		this.tipoSequencia = tipoSequencia;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getZerarInicioAno() {
		return zerarInicioAno;
	}

	public void setZerarInicioAno(String zerarInicioAno) {
		this.zerarInicioAno = zerarInicioAno;
	}

}