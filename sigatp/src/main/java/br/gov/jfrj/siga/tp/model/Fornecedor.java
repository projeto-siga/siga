package br.gov.jfrj.siga.tp.model;

import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import br.gov.jfrj.siga.feature.converter.entity.vraptor.ConvertableEntity;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.validation.annotation.Unique;
import br.gov.jfrj.siga.tp.validation.annotation.UpperCase;
import br.gov.jfrj.siga.validation.Email;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "fornecedor", schema = "sigatp")
@Unique.List(value = { @Unique(message = "{fornecedor.cnpj.unique}", field = "cnpj"), @Unique(message = "{fornecedor.email.unique}", field = "eMail") })
public class Fornecedor extends TpModel implements ConvertableEntity, Comparable<Fornecedor> {

	public static ActiveRecord<Fornecedor> AR = new ActiveRecord<>(Fornecedor.class);

	@Id
	@GeneratedValue(generator = "hibernate_sequence_generator")
	@SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "sigatp.hibernate_sequence")
	private Long id;

	@Enumerated(EnumType.STRING)
	private RamoDeAtividade ramoDeAtividade;

	@NotNull
	private String cnpj;

	@NotNull
	@UpperCase
	private String razaoSocial;

	@UpperCase
	private String nomeContato;

	private String telefone;

	private String fax;

	@Email(nullable = true)
	private String eMail;

	@UpperCase
	private String logradouro;

	@UpperCase
	private String complemento;

	private String cep;

	@UpperCase
	private String bairro;

	@UpperCase
	private String localidade;

	@UpperCase
	private String uf;

	private String enderecoVirtual;

	public String getRazaoSocialECNPJ() {
		return this.razaoSocial + " - " + this.cnpj;
	}

	public Fornecedor() {
		this.id = new Long(0);
		this.ramoDeAtividade = RamoDeAtividade.COMBUSTIVEL;
	}

	public Fornecedor(long id, RamoDeAtividade ramoDeAtividade, String cnpj, String razaoSocial, String nomeContato, String telefone, String fax, String eMail, String logradouro, String complemento,
			String cep, String bairro, String cidade, String localidade, String uf, String enderecoVirtual) {
		super();
		this.id = id;
		this.ramoDeAtividade = ramoDeAtividade;
		this.cnpj = cnpj;
		this.razaoSocial = razaoSocial;
		this.nomeContato = nomeContato;
		this.telefone = telefone;
		this.fax = fax;
		this.eMail = eMail;
		this.logradouro = logradouro;
		this.complemento = complemento;
		this.cep = cep;
		this.bairro = bairro;
		this.localidade = localidade;
		this.uf = uf;
		this.enderecoVirtual = enderecoVirtual;
	}

	@Override
	public int compareTo(Fornecedor o) {
		return this.razaoSocial.compareTo(o.razaoSocial);
	}

	public static List<Fornecedor> buscarTodosPorUF(String uf) {
		List<Fornecedor> fornecedores = Fornecedor.AR.find("uf", uf).fetch();
		Collections.sort(fornecedores);
		return fornecedores;
	}

	public static List<Fornecedor> buscarTodosPorCep(String cep) {
		List<Fornecedor> fornecedores = Fornecedor.AR.find("cep", cep).fetch();
		Collections.sort(fornecedores);
		return fornecedores;
	}

	@SuppressWarnings("unchecked")
	public static List<Fornecedor> listarTodos() {
		List<Fornecedor> fornecedores = Fornecedor.AR.findAll();
		Collections.sort(fornecedores);
		return fornecedores;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRamoAtividadeDescricao() {
		return ramoDeAtividade.getDescricao();
	}

	public RamoDeAtividade getRamoDeAtividade() {
		return ramoDeAtividade;
	}

	public void setRamoDeAtividade(RamoDeAtividade ramoDeAtividade) {
		this.ramoDeAtividade = ramoDeAtividade;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeContato() {
		return nomeContato;
	}

	public void setNomeContato(String nomeContato) {
		this.nomeContato = nomeContato;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getEnderecoVirtual() {
		return enderecoVirtual;
	}

	public void setEnderecoVirtual(String enderecoVirtual) {
		this.enderecoVirtual = enderecoVirtual;
	}
}
