package br.gov.jfrj.siga.sr.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.vraptor.entity.ObjetoVraptor;

@Entity
@Table(name = "SR_TIPO_PERGUNTA", schema = "SIGASR")
public class SrTipoPergunta extends ObjetoVraptor {

    private static final long serialVersionUID = -9170359662414485419L;

    final static public long TIPO_PERGUNTA_TEXTO_LIVRE = 1;
    final static public long TIPO_PERGUNTA_NOTA_1_A_5 = 2;

    public static final ActiveRecord<SrTipoPergunta> AR = new ActiveRecord<>(SrTipoPergunta.class);

    @Id
    @Column(name = "ID_TIPO_PERGUNTA")
    private Long idTipoPergunta;

    @Column(name = "NOME_TIPO_PERGUNTA")
    private String nomeTipoPergunta;

    @Column(name = "DESCR_TIPO_PERGUNTA")
    private String descrTipoPergunta;

    public SrTipoPergunta() {
    }

    @SuppressWarnings("unchecked")
    public static List<SrTipoPergunta> buscarTodos() {
        return SrTipoPergunta.AR.all().query.getResultList();
    }

    public Long getIdTipoPergunta() {
        return idTipoPergunta;
    }

    public void setIdTipoPergunta(Long idTipoPergunta) {
        this.idTipoPergunta = idTipoPergunta;
    }

    public String getNomeTipoPergunta() {
        return nomeTipoPergunta;
    }

    public void setNomeTipoPergunta(String nomeTipoPergunta) {
        this.nomeTipoPergunta = nomeTipoPergunta;
    }

    public String getDescrTipoPergunta() {
        return descrTipoPergunta;
    }

    public void setDescrTipoPergunta(String descrTipoPergunta) {
        this.descrTipoPergunta = descrTipoPergunta;
    }

    @Override
    public String toString() {
        return "SrTipoPergunta [idTipoPergunta=" + idTipoPergunta + ", nomeTipoPergunta=" + nomeTipoPergunta + ", descrTipoPergunta=" + descrTipoPergunta + "]";
    }

    @Override
    protected Long getId() {
        return this.idTipoPergunta;
    }

}