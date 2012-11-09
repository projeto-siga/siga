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

	final static public long TIPO_MOVIMENTACAO_PEDIDO_DE_CIENCIA = 6;

	final static public long TIPO_MOVIMENTACAO_CIENTE = 7;

	final static public long TIPO_MOVIMENTACAO_CLASSIFICACAO = 8;

	final static public long TIPO_MOVIMENTACAO_DEFINICAO_DE_PERFIL = 9;

	final static public long TIPO_MOVIMENTACAO_ALTERACAO_TITULO = 10;

	final static public long TIPO_MOVIMENTACAO_ALTERACAO_CONTEUDO = 11;

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
