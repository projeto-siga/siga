package br.gov.jfrj.siga.tp.model;

import java.util.Calendar;
import java.util.HashMap;
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

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.feature.converter.entity.vraptor.ConvertableEntity;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.util.FormatarDataHora;
import br.gov.jfrj.siga.tp.validation.annotation.Data;
import br.gov.jfrj.siga.tp.validation.annotation.UpperCase;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "afastamento", schema = "sigatp")
public class Afastamento extends TpModel implements ConvertableEntity {

    public static final ActiveRecord<Afastamento> AR = new ActiveRecord<>(Afastamento.class);

    @Id
    @GeneratedValue(generator = "hibernate_sequence_generator")
    @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "sigatp.hibernate_sequence")
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "CONDUTOR_ID")
    private Condutor condutor;

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
     //   String dataFormatadaOracle = "to_date('" + dataHoraInicio + "', 'DD/MM/YYYY')";
        String dataFormatadaOracle = dataHoraInicio;
        String filtroCondutor = "";

        if (idCondutor != null) {
            filtroCondutor = "condutor.id = " + idCondutor + " AND ";
        }

        String qrl = "SELECT a FROM Afastamento a " + " WHERE " + filtroCondutor + FormatarDataHora.recuperaFuncaoTrunc()+"(dataHoraInicio) <= " + FormatarDataHora.recuperaFuncaoTrunc() + "(" + dataFormatadaOracle + ")"
                + " AND (dataHoraFim IS NULL OR "+ FormatarDataHora.recuperaFuncaoTrunc() + "(dataHoraFim) >= " + FormatarDataHora.recuperaFuncaoTrunc() + "(" + dataFormatadaOracle + "))";

        return retornarLista(qrl);
    }

    public static List<Afastamento> buscarPorCondutores(Long idCondutor, String dataHoraInicio, String dataHoraFim) {
//        String dataFormatadaOracleInicio = "to_date('" + dataHoraInicio + "', 'DD/MM/YYYY')";
//        String dataFormatadaOracleFim = "to_date('" + dataHoraFim + "', 'DD/MM/YYYY')";
    	String dataFormatadaOracleInicio = dataHoraInicio;
    	String dataFormatadaOracleFim = dataHoraFim;
        String filtroCondutor = "";

        if (idCondutor != null) {
            filtroCondutor = "condutor.id = " + idCondutor + " AND ";
        }

        String qrl = "SELECT a FROM Afastamento a " + " WHERE " + filtroCondutor + " ((" + FormatarDataHora.recuperaFuncaoTrunc() + "(dataHoraInicio) <= " + FormatarDataHora.recuperaFuncaoTrunc() + "(" + dataFormatadaOracleInicio + ")"
                + " AND (dataHoraFim IS NULL OR " + FormatarDataHora.recuperaFuncaoTrunc() +"(dataHoraFim) >= " + FormatarDataHora.recuperaFuncaoTrunc() + "(" + dataFormatadaOracleInicio + ")))" + " OR (" + FormatarDataHora.recuperaFuncaoTrunc() + "(dataHoraInicio) <= " + FormatarDataHora.recuperaFuncaoTrunc() + "(" + dataFormatadaOracleFim + ")"
                + " AND (dataHoraFim IS NULL OR " + FormatarDataHora.recuperaFuncaoTrunc() + "(dataHoraFim) >= " + FormatarDataHora.recuperaFuncaoTrunc() + "(" + dataFormatadaOracleFim + "))))";

        return retornarLista(qrl);
    }

    public static List<Afastamento> buscarPorCondutores(Condutor condutor, Calendar dataHoraInicio, Calendar dataHoraFim) {
        List<Afastamento> retorno = null;
        HashMap<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("idCondutor",  condutor.getId());
        parametros.put("dataHoraInicio",  dataHoraInicio);
        parametros.put("dataHoraFim",  dataHoraFim);
        
        retorno = Afastamento.AR.find(
                "condutor.id = :idCondutor " + "and " + "((dataHoraInicio <= :dataHoraInicio and (dataHoraFim = null or dataHoraFim >= :dataHoraFim)) " + "or " + "(dataHoraInicio <= :dataHoraInicio and (dataHoraFim = null or dataHoraFim >= :dataHoraFim)))",
               parametros).fetch();

        return retorno;
    }

    public boolean ordemDeDatasCorreta() {
        return this.dataHoraInicio.before(this.dataHoraFim);
    }

}
