package br.gov.jfrj.siga.pp.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;

/**
 * @author Herval 11267
 */
@Entity(name = "Peritos")
@Table(name = "Peritos", schema = "SIGAPMP")
public class Peritos extends Objeto {

    private static final long serialVersionUID = -4200845060498917465L;

    public static final ActiveRecord<Peritos> AR = new ActiveRecord<>(Peritos.class);

    @Id()
    @Column(name = "cpf_perito", length = 50, nullable = false)
    private String cpf_perito;

    @Column(name = "nome_perito", length = 200, nullable = true)
    private String nome_perito;

    public Peritos() {
    }
    
    public Peritos(String cpf_perito_construt, String nome_perito_construt) {
        this.cpf_perito = cpf_perito_construt;
        this.nome_perito = nome_perito_construt;
    }

    public String getCpf_perito() {
        return cpf_perito;
    }

    public void setCpf_perito(String cpf_perito) {
        this.cpf_perito = cpf_perito;
    }

    public String getNome_perito() {
        return nome_perito;
    }

    public void setNome_perito(String nome_perito) {
        this.nome_perito = nome_perito;
    }
    
}
