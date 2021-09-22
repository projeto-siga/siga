package br.gov.jfrj.siga.tp.model;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import br.gov.jfrj.siga.feature.converter.entity.vraptor.ConvertableEntity;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.util.PerguntaSimNao;
import br.gov.jfrj.siga.tp.validation.annotation.Data;
import br.gov.jfrj.siga.tp.validation.annotation.UpperCase;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "relatoriodiario", schema = "sigatp")
public class RelatorioDiario extends TpModel implements ConvertableEntity {
	
	public static ActiveRecord<RelatorioDiario> AR = new ActiveRecord<>(RelatorioDiario.class);
	
	@Id
	@GeneratedValue(generator = "hibernate_sequence_generator") @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName="sigatp.hibernate_sequence") 
	private Long id;
	
	@NotNull
	@Data(descricaoCampo="Data", nullable=false)
	private Calendar data;
	
	@ManyToOne
	@NotNull
	private Veiculo veiculo;	
	
	@NotNull
	private Double odometroEmKm;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private NivelDeCombustivel nivelDeCombustivel;
	
	@Enumerated(EnumType.STRING)
	private PerguntaSimNao equipamentoObrigatorio;
	
	@Enumerated(EnumType.STRING)
	private PerguntaSimNao cartoes;
	
	@UpperCase
	private String observacao;
	
	public static List<RelatorioDiario> buscarTodosPorVeiculo(Veiculo veiculo){
		return RelatorioDiario.AR.find("veiculo", veiculo).fetch();
	}
	
	public RelatorioDiario(){
		this.id = new Long(0);
		this.nivelDeCombustivel = NivelDeCombustivel.I;
		this.equipamentoObrigatorio = PerguntaSimNao.SIM;
		this.cartoes = PerguntaSimNao.SIM;
	}
	
	public RelatorioDiario(Long id, Calendar data,
			NivelDeCombustivel nivelDeCombustivel,
			Double odometroEmKm, PerguntaSimNao equipamentoObrigatorio,  PerguntaSimNao cartoes,
			String observacao) {
		super();
		this.id = id;
		this.data = data;
		this.nivelDeCombustivel = nivelDeCombustivel;
		this.odometroEmKm = odometroEmKm;
		this.equipamentoObrigatorio = equipamentoObrigatorio;
		this.cartoes = cartoes;
		this.observacao = observacao;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Double getOdometroEmKm() {
		return odometroEmKm;
	}

	public void setOdometroEmKm(Double odometroEmKm) {
		this.odometroEmKm = odometroEmKm;
	}

	public NivelDeCombustivel getNivelDeCombustivel() {
		return nivelDeCombustivel;
	}

	public void setNivelDeCombustivel(NivelDeCombustivel nivelDeCombustivel) {
		this.nivelDeCombustivel = nivelDeCombustivel;
	}

	public PerguntaSimNao getEquipamentoObrigatorio() {
		return equipamentoObrigatorio;
	}

	public void setEquipamentoObrigatorio(PerguntaSimNao equipamentoObrigatorio) {
		this.equipamentoObrigatorio = equipamentoObrigatorio;
	}

	public PerguntaSimNao getCartoes() {
		return cartoes;
	}

	public void setCartoes(PerguntaSimNao cartoes) {
		this.cartoes = cartoes;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

}
