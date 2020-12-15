package br.gov.jfrj.siga.sr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.sr.util.Util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Entity
@Table(name = "sr_prioridade_solicitacao", schema = "sigasr")
public class SrPrioridadeSolicitacao extends Objeto {

    public static final ActiveRecord<SrPrioridadeSolicitacao> AR = new ActiveRecord<>(SrPrioridadeSolicitacao.class);
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(sequenceName = "SIGASR" + ".SR_PRIORIDADE_SOLICITACAO_SEQ", name = "srPrioridadeSolicitacaoSeq")
    @GeneratedValue(generator = "srPrioridadeSolicitacaoSeq")
    @Column(name = "ID_PRIORIDADE_SOLICITACAO")
    private Long idPrioridadeSolicitacao;

    @ManyToOne
    @JoinColumn(name = "ID_LISTA")
    private SrLista lista;

    @ManyToOne
    @JoinColumn(name = "ID_SOLICITACAO")
    private SrSolicitacao solicitacao;

    @Column(name = "NUM_POSICAO")
    private Long numPosicao;

    @Enumerated(EnumType.ORDINAL)
    private SrPrioridade prioridade;

    @Column(name = "NAO_REPOSICIONAR_AUTOMATICO")
    @Type(type = "yes_no")
    private Boolean naoReposicionarAutomatico;

    public SrPrioridadeSolicitacao() {
    }

    public SrPrioridadeSolicitacao(SrLista lista, SrSolicitacao solicitacao) {
        this.lista = lista;
        this.solicitacao = solicitacao;
    }

    public SrPrioridadeSolicitacao(SrLista lista, SrSolicitacao solicitacao, SrPrioridade prioridade, boolean naoReposicionarAutomatico) {
        this.lista = lista;
        this.solicitacao = solicitacao;
        this.prioridade = prioridade;
        this.naoReposicionarAutomatico = naoReposicionarAutomatico;
    }

    public Long getId() {
        return idPrioridadeSolicitacao;
    }

    public void setId(Long id) {
        this.idPrioridadeSolicitacao = id;
    }

    public SrLista getLista() {
        return lista;
    }

    public void setLista(SrLista lista) {
        this.lista = lista;
    }

    public Long getIdPrioridadeSolicitacao() {
        return idPrioridadeSolicitacao;
    }

    public void setIdPrioridadeSolicitacao(Long idPrioridadeSolicitacao) {
        this.idPrioridadeSolicitacao = idPrioridadeSolicitacao;
    }

    public SrPrioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(SrPrioridade prioridade) {
        this.prioridade = prioridade;
    }
    
    public String getPrioridadeString(){
        return prioridade == null ? "" : prioridade.getDescPrioridade();
    }

    public SrSolicitacao getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(SrSolicitacao solicitacao) {
        this.solicitacao = solicitacao;
    }

    public Long getNumPosicao() {
        return numPosicao;
    }

    public void setNumPosicao(Long numPosicao) {
        this.numPosicao = numPosicao;
    }

    public Boolean getNaoReposicionarAutomatico() {
        return naoReposicionarAutomatico;
    }

    public void setNaoReposicionarAutomatico(Boolean naoReposicionarAutomatico) {
        this.naoReposicionarAutomatico = naoReposicionarAutomatico;
    }

    public void incrementarPosicao() {
        this.numPosicao++;
    }

    public String toJson() {
        Gson gson = Util.createGson("lista", "solicitacao");

        JsonObject jsonObject = (JsonObject) gson.toJsonTree(this);
        jsonObject.add("solicitacao", jsonSolicitacao(gson));
        jsonObject.add("lista", jsonLista(gson));

        return jsonObject.toString();
    }

    private JsonElement jsonLista(Gson gson) {
        JsonObject lista = new JsonObject();
        lista.add("idLista", gson.toJsonTree(getLista().getIdLista()));
        lista.add("hisIdIni", gson.toJsonTree(getLista().getIdInicial()));

        return lista;
    }

    private JsonObject jsonSolicitacao(Gson gson) {
        JsonObject solicitacao = new JsonObject();
        solicitacao.add("idSolicitacao", gson.toJsonTree(getSolicitacao().getIdSolicitacao()));
        solicitacao.add("hisIdIni", gson.toJsonTree(getSolicitacao().getIdInicial()));
        return solicitacao;
    }

}