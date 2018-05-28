package br.gov.jfrj.siga.tp.model;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import br.gov.jfrj.siga.feature.converter.entity.vraptor.ConvertableEntity;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.validation.annotation.Data;
import br.gov.jfrj.siga.tp.validation.annotation.UpperCase;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(schema = "SIGATP")
public class Afastamento extends TpModel implements ConvertableEntity {

    public static final ActiveRecord<Afastamento> AR = new ActiveRecord<>(Afastamento.class);

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence_generator")
    @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "SIGATP.hibernate_sequence")
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "CONDUTOR_ID")
    private Condutor condutor;

    @NotEmpty
    @UpperCase
    @NotNull
    private String descricao;

    @NotNull
    @Data(descricaoCampo = "Data/Hora Inicio")
    private Calendar dataHoraInicio;

    @NotNull
    @Data(descricaoCampo = "Data/Hora Fim")
    private Calendar dataHoraFim;

    public Afastamento() {

    }

    public Afastamento(long id, Condutor condutor, String descricao, Calendar dataHoraInicio, Calendar dataHoraFim) {
        super();
        this.id = id;
        this.condutor = condutor;
        this.descricao = descricao;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Condutor getCondutor() {
        return condutor;
    }

    public void setCondutor(Condutor condutor) {
        this.condutor = condutor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Calendar getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(Calendar dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public Calendar getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(Calendar dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public static List<Afastamento> buscarTodosPorCondutor(Condutor condutor) {
        return Afastamento.AR.find("condutor", condutor).fetch();
    }

    @SuppressWarnings("unchecked")
    private static List<Afastamento> retornarLista(String qrl) {
        List<Afastamento> afastamentos;
        Query qry = AR.em().createQuery(qrl);
        try {
            afastamentos = (List<Afastamento>) qry.getResultList();
        } catch (NoResultException ex) {
            afastamentos = null;
        }
        return afastamentos;
    }

    public static List<Afastamento> buscarPorCondutores(Long idCondutor, String dataHoraInicio) {
        String dataFormatadaOracle = "to_date('" + dataHoraInicio + "', 'DD/MM/YYYY')";
        String filtroCondutor = "";

        if (idCondutor != null) {
            filtroCondutor = "condutor.id = " + idCondutor + " AND ";
        }

        String qrl = "SELECT a FROM Afastamento a " + " WHERE " + filtroCondutor + " trunc(dataHoraInicio) <= trunc(" + dataFormatadaOracle + ")"
                + " AND (dataHoraFim IS NULL OR trunc(dataHoraFim) >= trunc(" + dataFormatadaOracle + "))";

        return retornarLista(qrl);
    }

    public static List<Afastamento> buscarPorCondutores(Long idCondutor, String dataHoraInicio, String dataHoraFim) {
        String dataFormatadaOracleInicio = "to_date('" + dataHoraInicio + "', 'DD/MM/YYYY')";
        String dataFormatadaOracleFim = "to_date('" + dataHoraFim + "', 'DD/MM/YYYY')";
        String filtroCondutor = "";

        if (idCondutor != null) {
            filtroCondutor = "condutor.id = " + idCondutor + " AND ";
        }

        String qrl = "SELECT a FROM Afastamento a " + " WHERE " + filtroCondutor + " ((trunc(dataHoraInicio) <= trunc(" + dataFormatadaOracleInicio + ")"
                + " AND (dataHoraFim IS NULL OR trunc(dataHoraFim) >= trunc(" + dataFormatadaOracleInicio + ")))" + " OR (trunc(dataHoraInicio) <= trunc(" + dataFormatadaOracleFim + ")"
                + " AND (dataHoraFim IS NULL OR trunc(dataHoraFim) >= trunc(" + dataFormatadaOracleFim + "))))";

        return retornarLista(qrl);
    }

    public static List<Afastamento> buscarPorCondutores(Condutor condutor, Calendar dataHoraInicio, Calendar dataHoraFim) {
        List<Afastamento> retorno = null;
        retorno = Afastamento.AR.find(
                "condutor.id = ? " + "and " + "((dataHoraInicio <= ? and (dataHoraFim = null or dataHoraFim >= ?)) " + "or " + "(dataHoraInicio <= ? and (dataHoraFim = null or dataHoraFim >= ?)))",
                condutor.getId(), dataHoraInicio, dataHoraInicio, dataHoraFim, dataHoraFim).fetch();

        return retorno;
    }

    public boolean ordemDeDatasCorreta() {
        return this.dataHoraInicio.before(this.dataHoraFim);
    }

}
