package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name = "GC_TIPO_MOVIMENTACAO")
public class GcTipoMovimentacao extends GenericModel {
	final static public long TIPO_MOVIMENTACAO_CRIACAO = 1;

	final static public long TIPO_MOVIMENTACAO_FECHAMENTO = 2; 

	final static public long TIPO_MOVIMENTACAO_CANCELAMENTO = 3;

	final static public long TIPO_MOVIMENTACAO_PEDIDO_DE_REVISAO = 4;

	final static public long TIPO_MOVIMENTACAO_REVISADO = 5;

	final static public long TIPO_MOVIMENTACAO_NOTIFICAR = 6;

	final static public long TIPO_MOVIMENTACAO_CIENTE = 7;

	final static public long TIPO_MOVIMENTACAO_CLASSIFICACAO = 8;

	final static public long TIPO_MOVIMENTACAO_INTERESSADO = 9;

	final static public long TIPO_MOVIMENTACAO_EDICAO = 10;

	public static final long TIPO_MOVIMENTACAO_VISITA = 11;

	public static final long TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO = 12;

	public static final long TIPO_MOVIMENTACAO_ANEXAR_ARQUIVO = 13;
	
	@Id
	@Column(name = "ID_TIPO_MOVIMENTACAO")
	public long id;

	@Column(name = "NOME_TIPO_MOVIMENTACAO", nullable = false)
	public String nome;

	public GcTipoMovimentacao(long id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}
}
