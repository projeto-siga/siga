package models.vo;

public class ColunasVO {

	private String titulo;
	private String nome;
	private boolean exibir;
	private boolean alteravel;
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public boolean getExibir() {
		return exibir;
	}
	public void setExibir(boolean exibir) {
		this.exibir = exibir;
	}
	public boolean isAlteravel() {
		return alteravel;
	}
	public void setAlteravel(boolean alteravel) {
		this.alteravel = alteravel;
	}
	
}
