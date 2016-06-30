package br.gov.jfrj.siga.pp.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;

/**
 * @author Herval, Ruben e Edson da Rocha
 */
@Entity(name = "Locais")
@Table(name = "Locais", schema = "SIGAPMP")
public class Locais extends Objeto {
    private static final long serialVersionUID = -255154478491666205L;
    public static final ActiveRecord<Locais> AR = new ActiveRecord<>(Locais.class);
    @Id
    @Column(name = "cod_local", length = 3, nullable = false)
    private String cod_local;

    @Column(name = "local", length = 30, nullable = true)
    private String local;

    @ManyToOne
    @JoinColumn(name = "cod_forum", nullable = true)
    // fk, e, tem que atribuir programaticamente como objeto.
    private Foruns forumFk; // Isso eh coluna, mas, tem que atribuir como objeto.

    @Column(name = "dias", length = 40, nullable = true)
    private String dias;

    @Column(name = "hora_ini", length = 8, nullable = true)
    private String hora_ini;

    @Column(name = "hora_fim", length = 8, nullable = true)
    private String hora_fim;

    @Column(name = "intervalo_atendimento", length = 10, nullable = true)
    private int intervalo_atendimento;

    @Column(name = "exibir", length = 2, nullable = false)
    private int exibir;

    @Column(name = "endereco", length = 100, nullable = true)
    private String endereco;

    @Column(name = "ordem_apresentacao", length = 2, nullable = false)
    private int ordem_apresentacao;

    public Locais() {
    }

    public String getCod_local() {
        return cod_local;
    }

    public void setCod_local(String cod_local) {
        this.cod_local = cod_local;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Foruns getForumFk() {
        return forumFk;
    }

    public void setForumFk(Foruns forumFk) {
        this.forumFk = forumFk;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public String getHora_ini() {
        return hora_ini;
    }

    public void setHora_ini(String hora_ini) {
        this.hora_ini = hora_ini;
    }

    public String getHora_fim() {
        return hora_fim;
    }

    public void setHora_fim(String hora_fim) {
        this.hora_fim = hora_fim;
    }

    public int getIntervalo_atendimento() {
        return intervalo_atendimento;
    }

    public void setIntervalo_atendimento(int intervalo_atendimento) {
        this.intervalo_atendimento = intervalo_atendimento;
    }

    public int getExibir() {
        return exibir;
    }

    public void setExibir(int exibir) {
        this.exibir = exibir;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getOrdem_apresentacao() {
        return ordem_apresentacao;
    }

    public void setOrdem_apresentacao(int ordem_apresentacao) {
        this.ordem_apresentacao = ordem_apresentacao;
    }

}