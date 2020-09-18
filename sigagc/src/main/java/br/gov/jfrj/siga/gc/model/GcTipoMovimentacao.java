package br.gov.jfrj.siga.gc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "GC_TIPO_MOVIMENTACAO", schema = "SIGAGC")
public class GcTipoMovimentacao extends Objeto {
	private static final long serialVersionUID = -4444112339088302985L;

	public static ActiveRecord<GcTipoMovimentacao> AR = new ActiveRecord<>(
			GcTipoMovimentacao.class);

	final static public long TIPO_MOVIMENTACAO_CRIACAO = 1;

	final static public long TIPO_MOVIMENTACAO_FECHAMENTO = 2;

	final static public long TIPO_MOVIMENTACAO_CANCELAMENTO = 3;

	final static public long TIPO_MOVIMENTACAO_PEDIDO_DE_REVISAO = 4;

	final static public long TIPO_MOVIMENTACAO_REVISADO = 5;

	final static public long TIPO_MOVIMENTACAO_NOTIFICAR = 6;

	final static public long TIPO_MOVIMENTACAO_CIENTE = 7;

	final static public long TIPO_MOVIMENTACAO_CLASSIFICACAO = 8;

	//Edson: eliminada no desenvolvimento da vinculação de perfil:
	//final static public long TIPO_MOVIMENTACAO_INTERESSADO = 9;

	final static public long TIPO_MOVIMENTACAO_EDICAO = 10;

	public static final long TIPO_MOVIMENTACAO_VISITA = 11;

	public static final long TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO = 12;

	public static final long TIPO_MOVIMENTACAO_ANEXAR_ARQUIVO = 13;

	public static final long TIPO_MOVIMENTACAO_DUPLICAR = 14;
	
	final static public long TIPO_MOVIMENTACAO_VINCULAR_PAPEL = 15;

	@Id
	@Column(name = "ID_TIPO_MOVIMENTACAO")
	private long id;

	@Column(name = "NOME_TIPO_MOVIMENTACAO", nullable = false)
	private String nome;

	public void setId(long id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public GcTipoMovimentacao() {
		super();
	}

	public GcTipoMovimentacao(long id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}

	public long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}
}
