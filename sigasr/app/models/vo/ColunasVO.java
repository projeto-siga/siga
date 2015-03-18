package models.vo;


public class ColunasVO {
	private String titulo;
	private String nome;
	private String classe;
	private Long largura;
	private Long colunaAcaoExpandir;
	private boolean exibir;
	private boolean alteravel;
	
	public ColunasVO(String titulo, String nome) {
		super();
		this.titulo = titulo;
		this.nome = nome;
		this.exibir = true;
		this.alteravel = true;
	}

	public ColunasVO(String titulo, String nome, String classe) {
		this(titulo, nome);
		this.classe = classe;
	}

	public ColunasVO(String titulo, String nome, String classe, Long largura, Long colunaAcaoExpandir, boolean exibir,
			boolean alteravel) {
		this(titulo, nome, classe);
		this.largura = largura;
		this.colunaAcaoExpandir = colunaAcaoExpandir;
		this.exibir = exibir;
		this.alteravel = alteravel;
	}

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
	
	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}
	
	public Long getLargura() {
		return largura;
	}

	public void setLargura(Long largura) {
		this.largura = largura;
	}

	public Long getColunaAcaoExpandir() {
		return colunaAcaoExpandir;
	}

	public void setColunaAcaoExpandir(Long colunaAcaoExpandir) {
		this.colunaAcaoExpandir = colunaAcaoExpandir;
	}

	public boolean isExibir() {
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
