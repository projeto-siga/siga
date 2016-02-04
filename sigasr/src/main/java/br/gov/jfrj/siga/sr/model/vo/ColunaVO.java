package br.gov.jfrj.siga.sr.model.vo;

public class ColunaVO {
	private String titulo;
	private String nome;
	private String classe;
	private Long largura;
	private boolean exibirPorDefault;
	private boolean ocultavel;
	private boolean ordenavel;

	public ColunaVO(String titulo, String nome) {
		super();
		this.setTitulo(titulo);
		this.setNome(nome);
		this.setExibirPorDefault(true);
		this.setOcultavel(true);
		this.setOrdenavel(true);
	}

	public ColunaVO(String titulo, String nome, String classe) {
		this(titulo, nome);
		this.setClasse(classe);
	}

	public ColunaVO(String titulo, String nome, String classe, Long largura) {
		this(titulo, nome, classe);
		setLargura(largura);
	}

	public String getTitulo() {
		return titulo;
	}

	public ColunaVO setTitulo(String titulo) {
		this.titulo = titulo;
		return this;
	}

	public String getNome() {
		return nome;
	}

	public ColunaVO setNome(String nome) {
		this.nome = nome;
		return this;
	}

	public String getClasse() {
		return classe;
	}

	public ColunaVO setClasse(String classe) {
		this.classe = classe;
		return this;
	}

	public Long getLargura() {
		return largura;
	}

	public ColunaVO setLargura(Long largura) {
		this.largura = largura;
		return this;
	}

	public boolean isExibirPorDefault() {
		return exibirPorDefault;
	}

	public ColunaVO setExibirPorDefault(boolean exibir) {
		this.exibirPorDefault = exibir;
		return this;
	}

	public boolean isOcultavel() {
		return ocultavel;
	}

	public ColunaVO setOcultavel(boolean alteravel) {
		this.ocultavel = alteravel;
		return this;
	}
	
	public boolean isOrdenavel() {
		return ordenavel;
	}

	public ColunaVO setOrdenavel(boolean ordenavel) {
		this.ordenavel = ordenavel;
		return this;
	}

	public ColunaVO copy() {
		return new ColunaVO(this.getTitulo(), this.getNome(), this.getClasse(),
				this.getLargura()).setExibirPorDefault(
				this.isExibirPorDefault()).setOcultavel(this.isOcultavel());

	}

}
