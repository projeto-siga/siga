package br.gov.jfrj.siga.tp.model;

/*@SuppressWarnings("serial")
@Entity
@Table(schema = "SIGATP") */
public class UsuarioTeste extends TpModel {

	//@Id
//	@GeneratedValue( generator = "hibernate_sequence_generator")
//	@SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "sigatp.hibernate_sequence")
	private Long id;

	public UsuarioTeste() {
		this.id = new Long(0);
		this.nome = "";
		this.endereco = "";
		this.bairro = "";
		this.numero = 0;
	}

	//@UpperCase
	private String nome;

	//@UpperCase
	private String endereco;

	private String bairro;

	private int numero;

	//@Override
	public Long getId() {
		return id;
	}
}