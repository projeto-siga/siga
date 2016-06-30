package br.gov.jfrj.siga.pp.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;

/**
 * @author Herval, Edson da Rocha, e Ruben
 */
@Entity(name = "Foruns")
@Table(name = "Foruns", schema = "SIGAPMP")
public class Foruns extends Objeto {
    private static final long serialVersionUID = -8184741586013224510L;
    public static final ActiveRecord<Foruns> AR = new ActiveRecord<>(Foruns.class);

    @Id()
    @Column(name = "cod_forum", length = 10, nullable = false)
    private int cod_forum;

    @OneToMany(mappedBy = "forumFk")
    // isso nao eh campo, mas um 'references'.
    private List<Locais> lstLocal;

    @OneToMany(mappedBy = "forumFk")
    // isso nao eh campo, mas um 'references'.
    private List<UsuarioForum> lstUsuarioForum;

    @Column(name = "descricao_forum", length = 40, nullable = true)
    private String descricao_forum;

    @Column(name = "mural", length = 1000, nullable = true)
    private String mural; // texto com tags html

    public Foruns() {
    }
    
    public Foruns(int cod_forum_construt, String descricao_construt, String mural_construt) {
        this.cod_forum = cod_forum_construt;
        this.descricao_forum = descricao_construt;
        this.mural = mural_construt;
    }

    public int getCod_forum() {
        return cod_forum;
    }

    public void setCod_forum(int cod_forum) {
        this.cod_forum = cod_forum;
    }

    public static Foruns findByCodigo(String codForum) {
        return AR.find("cod_forum ='" + codForum + "'").first();
        // (Foruns) Foruns.AR.find("cod_forum='"+forum_permitido+"'").first();
    }
    
    public List<Locais> getLstLocal() {
        return lstLocal;
    }

    public void setLstLocal(List<Locais> lstLocal) {
        this.lstLocal = lstLocal;
    }

    public List<UsuarioForum> getLstUsuarioForum() {
        return lstUsuarioForum;
    }

    public void setLstUsuarioForum(List<UsuarioForum> lstUsuarioForum) {
        this.lstUsuarioForum = lstUsuarioForum;
    }

    public String getDescricao_forum() {
        return descricao_forum;
    }

    public void setDescricao_forum(String descricao_forum) {
        this.descricao_forum = descricao_forum;
    }

    public String getMural() {
        return mural;
    }

    public void setMural(String mural) {
        this.mural = mural;
    }

}
