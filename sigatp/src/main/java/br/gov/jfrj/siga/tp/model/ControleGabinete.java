package br.gov.jfrj.siga.tp.model;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.feature.converter.entity.vraptor.ConvertableEntity;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.validation.annotation.Data;

@Entity
@Audited
@Table(name = "controlegabinete", schema = "sigatp")
public class ControleGabinete extends TpModel implements ConvertableEntity {

    private static final long serialVersionUID = 5270396853989326489L;
    public static final ActiveRecord<ControleGabinete> AR = new ActiveRecord<>(ControleGabinete.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(ControleGabinete.class);

    @Id
    @GeneratedValue(generator = "hibernate_sequence_generator")
    @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "sigatp.hibernate_sequence")
    private Long id;

    @Data(descricaoCampo = "Data/Hora")
    private Calendar dataHora;

    @NotNull
    @Data(descricaoCampo = "Data/Hora Saida")
    private Calendar dataHoraSaida;

    @NotNull
    @Data(descricaoCampo = "Data/Hora Retorno")
    private Calendar dataHoraRetorno;

    @NotNull
    private String itinerario;

    @NotNull
    @ManyToOne
    private Veiculo veiculo;

    @NotNull
    @ManyToOne
    private Condutor condutor;

    @NotNull
    private Double odometroEmKmSaida;

    @NotNull
    private Double odometroEmKmRetorno;

    @NotNull
    private String naturezaDoServico;

    private String ocorrencias;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name = "ID_TITULAR")
    private DpPessoa titular;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name = "ID_SOLICITANTE")
    private DpPessoa solicitante;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getDataHora() {
        return dataHora;
    }

    public void setDataHora(Calendar dataHora) {
        this.dataHora = dataHora;
    }

    public Calendar getDataHoraSaida() {
        return dataHoraSaida;
    }

    public void setDataHoraSaida(Calendar dataHoraSaida) {
        this.dataHoraSaida = dataHoraSaida;
    }

    public Calendar getDataHoraRetorno() {
        return dataHoraRetorno;
    }

    public void setDataHoraRetorno(Calendar dataHoraRetorno) {
        this.dataHoraRetorno = dataHoraRetorno;
    }

    public String getItinerario() {
        return itinerario;
    }

    public void setItinerario(String itinerario) {
        this.itinerario = itinerario;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Condutor getCondutor() {
        return condutor;
    }

    public void setCondutor(Condutor condutor) {
        this.condutor = condutor;
    }

    public Double getOdometroEmKmSaida() {
        return odometroEmKmSaida;
    }

    public void setOdometroEmKmSaida(Double odometroEmKmSaida) {
        this.odometroEmKmSaida = odometroEmKmSaida;
    }

    public Double getOdometroEmKmRetorno() {
        return odometroEmKmRetorno;
    }

    public void setOdometroEmKmRetorno(Double odometroEmKmRetorno) {
        this.odometroEmKmRetorno = odometroEmKmRetorno;
    }

    public String getNaturezaDoServico() {
        return naturezaDoServico;
    }

    public void setNaturezaDoServico(String naturezaDoServico) {
        this.naturezaDoServico = naturezaDoServico;
    }

    public String getOcorrencias() {
        return ocorrencias;
    }

    public void setOcorrencias(String ocorrencias) {
        this.ocorrencias = ocorrencias;
    }

    public DpPessoa getTitular() {
        return titular;
    }

    public void setTitular(DpPessoa titular) {
        this.titular = titular;
    }

    public DpPessoa getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(DpPessoa solicitante) {
        this.solicitante = solicitante;
    }

    public static List<ControleGabinete> buscarTodosPorVeiculo(Veiculo veiculo) {
        return ControleGabinete.AR.find("veiculo", veiculo).fetch();
    }

    public static double buscarUltimoOdometroPorVeiculo(Veiculo veiculo, ControleGabinete controleGabinete) {
        double retorno = 0;
		Map<String, Object> parametros = new HashMap<String,Object>();
		parametros.put("veiculo",veiculo);
		parametros.put("id",controleGabinete.id);
        try {
            retorno = ((ControleGabinete) ControleGabinete.AR.find("veiculo = :veiculo and id <> :id order by id desc", parametros).fetch().get(0)).odometroEmKmRetorno;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return retorno;
    }

    public ControleGabinete() {
        this.id = Long.valueOf(0);
    }

    @SuppressWarnings("unchecked")
    public static List<ControleGabinete> listarTodos() {
        return ControleGabinete.AR.findAll();
    }

    public static List<ControleGabinete> listarPorCondutor(Condutor condutor) {
        return ControleGabinete.AR.find("condutor", condutor).fetch();
    }
}
