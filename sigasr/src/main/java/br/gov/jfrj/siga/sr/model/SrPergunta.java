package br.gov.jfrj.siga.sr.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "sr_pergunta", schema = "sigasr")
public class SrPergunta extends HistoricoSuporte {

    private static final long serialVersionUID = 8405698996883999900L;
    public static final ActiveRecord<SrPergunta> AR = new ActiveRecord<>(SrPergunta.class);

    @Id
    @SequenceGenerator(sequenceName = "SIGASR" + ".SR_PERGUNTA_SEQ", name = "srPerguntaSeq")
    @GeneratedValue(generator = "srPerguntaSeq")
    @Column(name = "ID_PERGUNTA")
    private Long idPergunta;

    @Column(name = "DESCR_PERGUNTA")
    private String descrPergunta;

    @ManyToOne()
    @JoinColumn(name = "ID_PESQUISA")
    private SrPesquisa pesquisa;

    @ManyToOne()
    @JoinColumn(name = "ID_TIPO_PERGUNTA")
    private SrTipoPergunta tipoPergunta;

    @Column(name = "ORDEM_PERGUNTA")
    private Long ordemPergunta;

    @ManyToOne()
    @JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
    private SrPergunta perguntaInicial;

    @OneToMany(targetEntity = SrPergunta.class, mappedBy = "perguntaInicial")
    private List<SrPergunta> meuPerguntaHistoricoSet;

    public SrPergunta() {
    }

    @Override
    public Long getId() {
        return this.getIdPergunta();
    }

    @Override
    public void setId(Long id) {
        this.setIdPergunta(id);
    }

    @Override
    public boolean semelhante(Assemelhavel obj, int profundidade) {
        return false;
    }

    public List<SrPergunta> getHistoricoPergunta() {
        if (perguntaInicial != null)
            return perguntaInicial.meuPerguntaHistoricoSet;
        return null;
    }

    public SrPergunta getPerguntaAtual() {
        List<SrPergunta> perguntas = getHistoricoPergunta();
        if (perguntas == null)
            return null;
        return perguntas.get(0);
    }

    public Long getIdPergunta() {
        return idPergunta;
    }

    public void setIdPergunta(Long idPergunta) {
        this.idPergunta = idPergunta;
    }

    public String getDescrPergunta() {
        return descrPergunta;
    }

    public void setDescrPergunta(String descrPergunta) {
        this.descrPergunta = descrPergunta;
    }

    public SrPesquisa getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(SrPesquisa pesquisa) {
        this.pesquisa = pesquisa;
    }

    public SrTipoPergunta getTipoPergunta() {
        return tipoPergunta;
    }

    public void setTipoPergunta(SrTipoPergunta tipoPergunta) {
        this.tipoPergunta = tipoPergunta;
    }

    public Long getOrdemPergunta() {
        return ordemPergunta;
    }

    public void setOrdemPergunta(Long ordemPergunta) {
        this.ordemPergunta = ordemPergunta;
    }

    public SrPergunta getPerguntaInicial() {
        return perguntaInicial;
    }

    public void setPerguntaInicial(SrPergunta perguntaInicial) {
        this.perguntaInicial = perguntaInicial;
    }

    public List<SrPergunta> getMeuPerguntaHistoricoSet() {
        return meuPerguntaHistoricoSet;
    }

    public void setMeuPerguntaHistoricoSet(List<SrPergunta> meuPerguntaHistoricoSet) {
        this.meuPerguntaHistoricoSet = meuPerguntaHistoricoSet;
    }

}
