package models.vo;


public class ColunasVO {
	public String titulo;
	public String nome;
	public String classe;
	public Long largura;
	public boolean temDetalheFormatado;
	public boolean exibir;
	public boolean alteravel;
	
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

	public ColunasVO(String titulo, String nome, String classe, boolean temDetalheFormatado) {
		this(titulo, nome, classe);
		this.temDetalheFormatado = temDetalheFormatado;
	}
	
	public ColunasVO(String titulo, String nome, String classe, Long largura) {
		this(titulo, nome, classe);
		this.largura = largura;
	}

	public ColunasVO(String titulo, String nome, String classe, boolean temDetalheFormatado, boolean exibir,
			boolean alteravel) {
		this(titulo, nome, classe, temDetalheFormatado);
		this.exibir = exibir;
		this.alteravel = alteravel;
	}

}
