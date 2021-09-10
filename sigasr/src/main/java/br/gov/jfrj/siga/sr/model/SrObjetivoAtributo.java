package br.gov.jfrj.siga.sr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "sr_objetivo_atributo", schema = "sigasr")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class SrObjetivoAtributo extends Objeto {

    public static final ActiveRecord<SrObjetivoAtributo> AR = new ActiveRecord<>(SrObjetivoAtributo.class);

    private static final long serialVersionUID = 1L;

    final static public long OBJETIVO_SOLICITACAO = 1;

    final static public long OBJETIVO_ACORDO = 2;

    final static public long OBJETIVO_INDICADOR = 3;

    @Id
    @Column(name = "ID_OBJETIVO")
    private long idObjetivo;

    @Column(name = "DESCR_OBJETIVO", nullable = false)
    private String descrObjetivo;

    public Long getId() {
        return this.getIdObjetivo();
    }

    public String getDescricao() {
        return this.getDescrObjetivo();
    }

    public long getIdObjetivo() {
        return idObjetivo;
    }

    public void setIdObjetivo(long idObjetivo) {
        this.idObjetivo = idObjetivo;
    }

    public String getDescrObjetivo() {
        return descrObjetivo;
    }

    public void setDescrObjetivo(String descrObjetivo) {
        this.descrObjetivo = descrObjetivo;
    }

}
