package br.gov.jfrj.siga.sr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "sr_resposta", schema = "sigasr")
public class SrResposta extends Objeto {
    private static final long serialVersionUID = 6748786172865091607L;
    public static final ActiveRecord<SrResposta> AR = new ActiveRecord<>(SrResposta.class);

    @Id
    @SequenceGenerator(sequenceName = "SIGASR" + ".SR_RESPOSTA_SEQ", name = "srRespostaSeq")
    @GeneratedValue(generator = "srRespostaSeq")
    @Column(name = "ID_RESPOSTA")
    private Long idResposta;

    @Column(name = "DESCR_RESPOSTA")
    private String descrResposta;

    @Column(name = "VALOR_RESPOSTA")
    @Enumerated()
    private SrGrauSatisfacao grauSatisfacao;

    @ManyToOne()
    @JoinColumn(name = "ID_PERGUNTA")
    private SrPergunta pergunta;

    @ManyToOne()
    @JoinColumn(name = "ID_MOVIMENTACAO")
    private SrMovimentacao movimentacao;

    public SrResposta() {

    }

    public Long getId() {
        return this.getIdResposta();
    }

    public void setId(Long id) {
        setIdResposta(id);
    }

    public SrGrauSatisfacao getGrauSatisfacao() {
        return grauSatisfacao;
    }

    public Long getIdResposta() {
        return idResposta;
    }

    public void setIdResposta(Long idResposta) {
        this.idResposta = idResposta;
    }

    public String getDescrResposta() {
        return descrResposta;
    }

    public void setDescrResposta(String descrResposta) {
        this.descrResposta = descrResposta;
    }

    public void setGrauSatisfacao(SrGrauSatisfacao grauSatisfacao) {
        this.grauSatisfacao = grauSatisfacao;
    }

    public SrPergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(SrPergunta pergunta) {
        this.pergunta = pergunta;
    }

    public SrMovimentacao getMovimentacao() {
        return movimentacao;
    }

    public void setMovimentacao(SrMovimentacao movimentacao) {
        this.movimentacao = movimentacao;
    }

}
