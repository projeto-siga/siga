package br.gov.jfrj.siga.dp;

import java.util.Objects;

public class NotificarPorEmail {

	private Long id;
	private String nome;
	private int naoConfiguravel;
	private int configuravel;
	
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
	public int getNaoConfiguravel() {
		return naoConfiguravel;
	}
	public void setNaoConfiguravel(int naoConfiguravel) {
		this.naoConfiguravel = naoConfiguravel;
	}
	public int getConfiguravel() {
		return configuravel;
	}
	public void setConfiguravel(int configuravel) {
		this.configuravel = configuravel;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotificarPorEmail other = (NotificarPorEmail) obj;
		return Objects.equals(id, other.id);
	}
	
}
