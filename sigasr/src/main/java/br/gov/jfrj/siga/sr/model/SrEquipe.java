package br.gov.jfrj.siga.sr.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.model.vo.SelecionavelVO;
import br.gov.jfrj.siga.sr.util.Util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Entity
@Table(name = "sr_equipe", schema = "sigasr")
public class SrEquipe extends HistoricoSuporte {

    public static final ActiveRecord<SrEquipe> AR = new ActiveRecord<>(SrEquipe.class);

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(sequenceName = "SIGASR" + ".SR_EQUIPE_SEQ", name = "srEquipeSeq")
    @GeneratedValue(generator = "srEquipeSeq")
    @Column(name = "ID_EQUIPE")
    private Long idEquipe;

    @ManyToOne
    @JoinColumn(name = "ID_LOTA_EQUIPE")
    private DpLotacao lotacao;

    @OneToMany(targetEntity = SrExcecaoHorario.class, mappedBy = "equipe", fetch = FetchType.LAZY)
    private List<SrExcecaoHorario> excecaoHorarioSet;

    @Transient
    private DpLotacao lotacaoEquipe;

    @Override
    public Long getId() {
        return this.getIdEquipe();
    }

    @Override
    public void setId(Long id) {
        this.setIdEquipe(id);
    }

    @Override
    public boolean semelhante(Assemelhavel obj, int profundidade) {
        return false;
    }

    public SrEquipe() {
        super();
    }

    // Edson: Nao foi possivel deixar cascade automatico.
    // Isso porque, no primeiro salvamento, o formulario nao consegue
    // fazer automaticamente a conexao abaixo, entre os horarios e a equipe,
    // visto que a equipe nao tem ID
    @Override
    public void salvarComHistorico() throws Exception {
        super.salvarComHistorico();
        if (getExcecaoHorarioSet() != null) {
            for (SrExcecaoHorario eh : getExcecaoHorarioSet()) {
                eh.setEquipe(this);
                eh.save();
            }
            ContextoPersistencia.em().flush();
        }
    }

    public List<SrConfiguracao> getDesignacoes() throws Exception {
        if (getLotacao() == null)
            return null;
        else
            return SrConfiguracao.listarDesignacoes(this);
    }

    public static List<SrEquipe> listar(boolean mostrarDesativados) {
        StringBuffer sb = new StringBuffer();

        if (!mostrarDesativados)
            sb.append(" hisDtFim is null ");
        else {
            sb.append(" idEquipe in (");
            sb.append(" SELECT max(idEquipe) as idEquipe FROM ");
            sb.append(" SrEquipe GROUP BY hisIdIni) ");
        }

        return SrEquipe.AR.find(sb.toString()).fetch();

    }

    public boolean podeEditar(DpLotacao lotaTitular, DpPessoa titular) {
        return lotaTitular.equivale(this.getLotacao());
    }

    public String toJson() {
        Gson gson = Util.createGson("lotacao", "lotacaoEquipe", "excecaoHorarioSet");

        JsonObject jsonObject = (JsonObject) gson.toJsonTree(this);
        jsonObject.add("ativo", gson.toJsonTree(isAtivo()));
        jsonObject.add("excecaoHorarioSet", excecaoHorarioArray());
        jsonObject.add("lotacaoEquipe", gson.toJsonTree(SelecionavelVO.createFrom(this.getLotacao())));
        return jsonObject.toString();
    }

    private JsonArray excecaoHorarioArray() {
        Gson gson = Util.createGson("equipe");
        JsonArray jsonArray = new JsonArray();

        if (this.getExcecaoHorarioSet() != null)
            for (SrExcecaoHorario srExcecaoHorario : this.getExcecaoHorarioSet()) {
                JsonObject jsonObjectExcecao = (JsonObject) gson.toJsonTree(srExcecaoHorario);

                if (srExcecaoHorario.getDiaSemana() != null)
                    jsonObjectExcecao.add("descrDiaSemana", gson.toJsonTree(srExcecaoHorario.getDiaSemana().getDescrDiaSemana()));

                jsonArray.add(jsonObjectExcecao);
            }

        return jsonArray;
    }

    public DpLotacao getLotacaoEquipe() {
        return this.lotacaoEquipe != null ? this.lotacaoEquipe : this.getLotacao();
    }

    public void setLotacaoEquipe(DpLotacao lotacaoEquipe) {
        this.lotacaoEquipe = lotacaoEquipe;
        this.setLotacao(lotacaoEquipe);
    }

    public DpLotacao getLotacao() {
        return lotacao;
    }

    public void setLotacao(DpLotacao lotacao) {
        this.lotacao = lotacao;
    }

    public List<SrExcecaoHorario> getExcecaoHorarioSet() {
        return excecaoHorarioSet;
    }

    public void setExcecaoHorarioSet(List<SrExcecaoHorario> excecaoHorarioSet) {
        this.excecaoHorarioSet = excecaoHorarioSet;
    }

    public Long getIdEquipe() {
        return idEquipe;
    }

    public void setIdEquipe(Long idEquipe) {
        this.idEquipe = idEquipe;
    }
}