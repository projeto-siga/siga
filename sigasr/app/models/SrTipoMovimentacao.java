package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import play.db.jpa.GenericModel;

@Entity
@Table(name = "SR_TIPO_MOVIMENTACAO", schema = "SIGASR")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class SrTipoMovimentacao extends GenericModel {

	final static public long TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO = 1;

	final static public long TIPO_MOVIMENTACAO_ANDAMENTO = 2;

	final static public long TIPO_MOVIMENTACAO_INCLUSAO_LISTA = 3;

	final static public long TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO = 4;

	final static public long TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO = 5;

	final static public long TIPO_MOVIMENTACAO_CANCELAMENTO_DE_INCLUSAO_LISTA = 6;

	final static public long TIPO_MOVIMENTACAO_FECHAMENTO = 7;

	final static public long TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO = 8;
	
	final static public long TIPO_MOVIMENTACAO_INICIO_PENDENCIA = 9;
	
	final static public long TIPO_MOVIMENTACAO_REABERTURA = 10;
	
	final static public long TIPO_MOVIMENTACAO_FIM_PENDENCIA = 11;
	
	final static public long TIPO_MOVIMENTACAO_ANEXACAO_ARQUIVO = 12;

	final static public long TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA = 13;
	
	final static public long TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO = 14;
	
	final static public long TIPO_MOVIMENTACAO_FECHAMENTO_PARCIAL = 15;
	
	final static public long TIPO_MOVIMENTACAO_AVALIACAO = 16;
	
	final static public long TIPO_MOVIMENTACAO_INICIO_CONTROLE_QUALIDADE = 17;

	@Id
	@Column(name = "ID_TIPO_MOVIMENTACAO")
	public long idTipoMov;

	@Column(name = "NOME_TIPO_MOVIMENTACAO", nullable = false)
	public String nome;

	public SrTipoMovimentacao(long idTipoMov, String nome) {
		super();
		this.idTipoMov = idTipoMov;
		this.nome = nome;
	}

	public SrTipoMovimentacao getSrTipoMovimentacao() {
		return getSrTipoMovimentacao();
	}

	public java.lang.Long getIdTipoMov() {
		return idTipoMov;
	}
}
