package br.gov.jfrj.siga.tp.model;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
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

import com.google.gson.Gson;

import br.gov.jfrj.siga.feature.converter.entity.vraptor.ConvertableEntity;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.util.PerguntaSimNao;
import br.gov.jfrj.siga.tp.validation.annotation.Data;
import br.gov.jfrj.siga.tp.validation.annotation.UpperCase;

@Entity
@Audited
@Table(name = "avaria", schema = "sigatp")
public class Avaria extends TpModel implements ConvertableEntity, Comparable<Avaria> {

	private static final long serialVersionUID = 1L;
	public static final ActiveRecord<Avaria> AR = new ActiveRecord<>(Avaria.class);

	@Id
	@GeneratedValue(generator = "hibernate_sequence_generator")
	@SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "sigatp.hibernate_sequence")
	private Long id;

	@ManyToOne
	@NotNull
	private Veiculo veiculo;

	@NotNull(message = "{avaria.data.registro}")
	@Data(descricaoCampo = "Data de Registro", nullable=false)
	private Calendar dataDeRegistro;

	@Data(descricaoCampo = "Data de Solucao")
	private Calendar dataDeSolucao;

	@NotNull(message = "{avaria.descricao}")
	@UpperCase
	private String descricao;

	@NotNull
	@Enumerated(EnumType.STRING)
	private PerguntaSimNao podeCircular;

	public Avaria() {
		this.id = new Long(0);
		this.veiculo = null;
		this.dataDeRegistro = Calendar.getInstance();
		this.dataDeSolucao = null;
		this.descricao = "";
		this.podeCircular = PerguntaSimNao.NAO;
	}

	public Avaria(Veiculo veiculo, Calendar dataDeRegistro, Calendar dataDeSolucao, String descricao, PerguntaSimNao podeCircular) {
		super();
		this.veiculo = veiculo;
		this.dataDeRegistro = dataDeRegistro;
		this.dataDeSolucao = dataDeSolucao;
		this.descricao = descricao;
		this.podeCircular = podeCircular;
	}

	@Override
	public Long getId() {
		return this.id;
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

	public Calendar getDataDeRegistro() {
		return dataDeRegistro;
	}

	public void setDataDeRegistro(Calendar dataDeRegistro) {
		this.dataDeRegistro = dataDeRegistro;
	}

	public Calendar getDataDeSolucao() {
		return dataDeSolucao;
	}

	public void setDataDeSolucao(Calendar dataDeSolucao) {
		this.dataDeSolucao = dataDeSolucao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public PerguntaSimNao getPodeCircular() {
		return podeCircular;
	}

	public void setPodeCircular(PerguntaSimNao podeCircular) {
		this.podeCircular = podeCircular;
	}

	public String toJson() {
		return new Gson().toJson(this);
	}

	public static List<Avaria> buscarTodasPorVeiculo(Veiculo veiculo) {
		List<Avaria> avarias = Avaria.AR.find("veiculo", veiculo).fetch();
		Collections.sort(avarias);
		return avarias;
	}

	public static List<Avaria> buscarPendentesPorVeiculo(Veiculo veiculo) {
        HashMap<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("veiculo",  veiculo);
		List<Avaria> avarias = Avaria.AR.find("veiculo = :veiculo AND dataDeSolucao is null ", parametros).fetch();
		Collections.sort(avarias);
		return avarias;
	}

	@Override
	public int compareTo(Avaria o) {
		return this.veiculo.compareTo(o.veiculo);
	}

	@SuppressWarnings("unchecked")
	public static List<Avaria> listarTodos() {
		List<Avaria> avarias = Avaria.AR.findAll();
		Collections.sort(avarias);
		return avarias;
	}
}
