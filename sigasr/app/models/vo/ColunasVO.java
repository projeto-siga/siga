package models.vo;


public class ColunasVO {
	public String titulo;
	public String nome;
	public String classe;
	public Long largura;
	public boolean detalheFormatado;
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

	public ColunasVO(String titulo, String nome, String classe, Long largura) {
		this(titulo, nome, classe);
		this.largura = largura;
	}

	public ColunasVO(String titulo, String nome, String classe, boolean detalheFormatado, boolean exibir, boolean alteravel) {
		this(titulo, nome, classe);
		this.detalheFormatado = detalheFormatado;
		this.exibir = exibir;
		this.alteravel = alteravel;
	}

}
