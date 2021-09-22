package br.gov.jfrj.siga.tp.model;

import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.feature.converter.entity.vraptor.ConvertableEntity;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.validation.annotation.Data;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;

@Entity
// @Table(name = "LOTACAO_VEICULO_2", schema="SIGAOR")
@Audited
@Table(name = "lotacaoveiculo", schema = "sigatp")
public class LotacaoVeiculo extends TpModel implements ConvertableEntity {

	@Id
	@GeneratedValue(generator = "hibernate_sequence_generator")
	@SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "sigatp.hibernate_sequence")
	private Long id;

	@ManyToOne
	@NotNull
	private Veiculo veiculo;

	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne
	@NotNull
	@JoinColumn(name = "ID_LOTA_SOLICITANTE")
	private DpLotacao lotacao;

	@NotNull
	@Data(descricaoCampo = "Data/Hora Inicio", nullable=false)
	private Calendar dataHoraInicio;

	@Data(descricaoCampo = "Data/Hora Fim")
	private Calendar dataHoraFim;

	private Double odometroEmKm;

	private static final long serialVersionUID = 1912137163976035054L;

	public static final ActiveRecord<LotacaoVeiculo> AR = new ActiveRecord<>(LotacaoVeiculo.class);

	public LotacaoVeiculo() {
	}

	public LotacaoVeiculo(Long id, Veiculo veiculo, DpLotacao lotacao, Calendar dataHoraInicio, Calendar dataHoraFim, double odometroEmKm) {
		super();
		this.id = id;
		this.veiculo = veiculo;
		this.lotacao = lotacao;
		this.dataHoraInicio = dataHoraInicio;
		this.dataHoraFim = dataHoraFim;
		this.odometroEmKm = odometroEmKm;
	}

	/**
	 * Inclui a nova lotacao do veiculo e preenche a data fim da lotacao anterior
	 *
	 * @param veiculo
	 */
	public static String atualizarDataFimLotacaoAnterior(Veiculo veiculo) throws Exception {
		try {
			Map<String, Object> parametros = new HashMap<String,Object>();
			parametros.put("idVeiculo",veiculo.getId());
			List<LotacaoVeiculo> lotacoesVeiculo = LotacaoVeiculo.AR.find("veiculo.id = :idVeiculo and dataHoraFim is null order by dataHoraInicio DESC", parametros).fetch();
			if (lotacoesVeiculo.size() == 1) {
				lotacoesVeiculo.get(0).dataHoraFim = Calendar.getInstance();
				lotacoesVeiculo.get(0).save();
			} else {
				if (lotacoesVeiculo.size() > 1) {
					throw new Exception(MessagesBundle.getMessage("lotacaoVeiculo.lotacoesVeiculo.MaiorQueUm.exception"));
				}
			}
		} catch (Exception e) {
			throw new Exception(MessagesBundle.getMessage("lotacaoVeiculo.lotacoesVeiculo.exception", e.getMessage()));
		}
		return "ok";
	}

	public static List<LotacaoVeiculo> buscarTodosPorVeiculo(Veiculo veiculo) {
		Map<String, Object> parametros = new HashMap<String,Object>();
		parametros.put("veiculo",veiculo);
		return LotacaoVeiculo.AR.find("veiculo = :veiculo order by dataHoraInicio DESC", parametros).fetch();
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public DpLotacao getLotacao() {
		return lotacao;
	}

	public void setLotacao(DpLotacao lotacao) {
		this.lotacao = lotacao;
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

	public Double getOdometroEmKm() {
		return odometroEmKm;
	}

	public void setOdometroEmKm(Double odometroEmKm) {
		this.odometroEmKm = odometroEmKm;
	}

	public static Comparator<LotacaoVeiculo> comparator() {
		return new Comparator<LotacaoVeiculo>() {
			@Override
			public int compare(LotacaoVeiculo o1, LotacaoVeiculo o2) {
				return o2.dataHoraInicio.compareTo(o1.dataHoraInicio);
			}
		};
	}
}
