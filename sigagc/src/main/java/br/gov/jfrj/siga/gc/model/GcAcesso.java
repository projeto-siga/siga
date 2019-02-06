package br.gov.jfrj.siga.gc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "GC_ACESSO", schema = "SIGAGC")
public class GcAcesso extends Objeto {
	private static final long serialVersionUID = -6824464659652929435L;
	public static final long ACESSO_PUBLICO = 1;
	public static final long ACESSO_ORGAO_USU = 2;
	public static final long ACESSO_LOTACAO_E_SUPERIORES = 3;
	public static final long ACESSO_LOTACAO_E_INFERIORES = 4;
	public static final long ACESSO_LOTACAO = 5;
	public static final long ACESSO_PESSOAL = 6;
	public static final long ACESSO_LOTACAO_E_GRUPO = 7;

	public static ActiveRecord<GcAcesso> AR = new ActiveRecord<>(
			GcAcesso.class);
	
	@Id
	@Column(name = "ID_ACESSO")
	public long id;

	@Column(name = "NOME_ACESSO", nullable = false)
	public String nome;

	public GcAcesso() {
		super();
	}

	public GcAcesso(long id, String nome) {
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
