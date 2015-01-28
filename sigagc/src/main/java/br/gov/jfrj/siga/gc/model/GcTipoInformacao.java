package br.gov.jfrj.siga.gc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "GC_TIPO_INFORMACAO", schema = "SIGAGC")
public class GcTipoInformacao extends Objeto {
	final static public long TIPO_INFORMACAO_REGISTRO_DE_CONHECIMENTO = 1;
	final static public long TIPO_INFORMACAO_ERRO_CONHECIDO = 2;
	final static public long TIPO_INFORMACAO_PROCEDIMENTO = 3;
	final static public long TIPO_INFORMACAO_PONTO_DE_ENTRADA = 4;

	public static ActiveRecord<GcTipoInformacao> AR = new ActiveRecord<>(
			GcTipoInformacao.class);
	
	@Id
	@Column(name = "ID_TIPO_INFORMACAO")
	public long id;

	@Column(name = "NOME_TIPO_INFORMACAO", nullable = false)
	public String nome;
	
	@ManyToOne()
	@JoinColumn(name = "ARQUIVO")
	public GcArquivo arq;

	public GcTipoInformacao(long id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}

	public GcTipoInformacao() {
		super();
	}

	public long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public GcArquivo getArq() {
		return arq;
	}
}
