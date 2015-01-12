package models.vo;

public abstract class AbstractSelecionavel {
	public Long id;
	public String sigla;
	public String descricao;
	
	public AbstractSelecionavel(Long id, String sigla, String descricao) {
		this.id = id;
		this.sigla = sigla;
		this.descricao = descricao;
	}
	
	public AbstractSelecionavel(Long id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}
}
