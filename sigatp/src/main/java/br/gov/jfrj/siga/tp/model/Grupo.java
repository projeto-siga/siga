package br.gov.jfrj.siga.tp.model;

import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Immutable;
import org.hibernate.envers.Audited;

import br.gov.jfrj.siga.feature.converter.entity.vraptor.ConvertableEntity;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.validation.annotation.UpperCase;

@Entity
@Audited
@Immutable
@Table(name = "grupoveiculo", schema = "sigatp")
public class Grupo extends TpModel implements ConvertableEntity, Comparable<Grupo> {

	private static final long serialVersionUID = -3681022838391034811L;

	public static final ActiveRecord<Grupo> AR = new ActiveRecord<>(Grupo.class);

	@Id
	@GeneratedValue(generator = "hibernate_sequence_generator")
	@SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "sigatp.hibernate_sequence")
	private Long id;

	@NotNull
	@UpperCase
	private String nome;

	@NotNull
	@UpperCase
	private String finalidade;

	@NotNull
	@UpperCase
	private String caracteristicas;

	@NotNull
	@UpperCase
	private String letra;

	public Grupo() {
		this.nome = "";
		this.finalidade = "";
		this.caracteristicas = "";
	}

	public String getDadosParaExibicao() {
		return this.letra.toUpperCase() + "-" + this.nome.toUpperCase();
	}

	public int compareTo(Grupo g) {
		return this.letra.compareTo(g.letra);
	}

	@SuppressWarnings("unchecked")
	public static List<Grupo> listarTodos() throws Exception {
		List<Grupo> grupos = Grupo.AR.findAll();
		Collections.sort(grupos);
		return grupos;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getFinalidade() {
		return finalidade;
	}

	public void setFinalidade(String finalidade) {
		this.finalidade = finalidade;
	}

	public String getCaracteristicas() {
		return caracteristicas;
	}

	public void setCaracteristicas(String caracteristicas) {
		this.caracteristicas = caracteristicas;
	}

	public String getLetra() {
		return letra;
	}

	public void setLetra(String letra) {
		this.letra = letra;
	}
}
