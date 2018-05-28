package br.gov.jfrj.siga.tp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.tp.validation.annotation.UpperCase;

@SuppressWarnings("serial")
@Entity
@Table(schema = "SIGATP")
public class UsuarioTeste extends TpModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence_generator")
	@SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "SIGATP.hibernate_sequence")
	public Long id;

	public UsuarioTeste() {
		this.id = new Long(0);
		this.nome = "";
		this.endereco = "";
		this.bairro = "";
		this.numero = 0;
	}

	@UpperCase
	public String nome;

	@UpperCase
	public String endereco;

	public String bairro;

	public int numero;

	@Override
	public Long getId() {
		return id;
	}
}