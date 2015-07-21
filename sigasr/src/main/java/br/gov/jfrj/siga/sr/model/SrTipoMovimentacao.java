package br.gov.jfrj.siga.sr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.vraptor.entity.ObjetoVraptor;

@Entity
@Table(name = "SR_TIPO_MOVIMENTACAO", schema = "SIGASR")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class SrTipoMovimentacao extends ObjetoVraptor {

    private static final long serialVersionUID = 1L;

    public static final long TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO = 1;

    public static final long TIPO_MOVIMENTACAO_ANDAMENTO = 2;

    public static final long TIPO_MOVIMENTACAO_INCLUSAO_LISTA = 3;

    //public static final long TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO = 4;

    //public static final long TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO = 5;

    public static final long TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA = 6;

    public static final long TIPO_MOVIMENTACAO_FECHAMENTO = 7;

    public static final long TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO = 8;

    public static final long TIPO_MOVIMENTACAO_INICIO_PENDENCIA = 9;

    public static final long TIPO_MOVIMENTACAO_REABERTURA = 10;

    public static final long TIPO_MOVIMENTACAO_FIM_PENDENCIA = 11;

    public static final long TIPO_MOVIMENTACAO_ANEXACAO_ARQUIVO = 12;

    public static final long TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA = 13;

    public static final long TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO = 14;

    // final static public long TIPO_MOVIMENTACAO_FECHAMENTO_PARCIAL = 15;

    public static final long TIPO_MOVIMENTACAO_AVALIACAO = 16;

    // final static public long TIPO_MOVIMENTACAO_INICIO_CONTROLE_QUALIDADE = 17;

    public static final long TIPO_MOVIMENTACAO_JUNTADA = 18;

    public static final long TIPO_MOVIMENTACAO_DESENTRANHAMENTO = 19;

    public static final long TIPO_MOVIMENTACAO_VINCULACAO = 20;

    public static final long TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE = 21;

    // final static public long TIPO_MOVIMENTACAO_RASCUNHO = 22;

    // final static public long TIPO_MOVIMENTACAO_EXCLUSAO = 23;

    public static final long TIPO_MOVIMENTACAO_ESCALONAMENTO = 24;

    public static final ActiveRecord<SrTipoMovimentacao> AR = new ActiveRecord<>(SrTipoMovimentacao.class);

    @Id
    @Column(name = "ID_TIPO_MOVIMENTACAO")
    private long idTipoMov;

    @Column(name = "NOME_TIPO_MOVIMENTACAO", nullable = false)
    private String nome;

    public SrTipoMovimentacao(long idTipoMov, String nome) {
        super();
        this.setIdTipoMov(idTipoMov);
        this.setNome(nome);
    }

    public SrTipoMovimentacao() {
        super();
    }

    public Long getIdTipoMov() {
        return idTipoMov;
    }

    @Override
    protected Long getId() {
        return this.getIdTipoMov();
    }

    public void setIdTipoMov(long idTipoMov) {
        this.idTipoMov = idTipoMov;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
