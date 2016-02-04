package br.gov.jfrj.siga.wf.util;

/**
 * Classe que representa o tipo do responsável designado.
 * 
 * @author kpf
 * 
 */
public class WfTipoResponsavel {
	private int id;
	/**
	 * Texto amigável que representa o tipo de responsável.
	 */
	private String texto;

	/**
	 * Texto da expressão ou identificador do tipo de responsável.
	 */
	private String valor;

	/**
	 * Construtor da classe TipoResponsável.
	 * 
	 * @param id
	 * @param texto
	 * @param valor
	 */
	public WfTipoResponsavel(int id, String texto, String valor) {
		this.setId(id);
		this.setTexto(texto);
		this.setValor(valor);
	}

	/**
	 * Retorna o id do tipo de responsável.
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Retorna o texto amigável do Tipo de responsável
	 * 
	 * @return
	 */
	public String getTexto() {
		return texto;
	}

	/**
	 * Retorna o valor do tipo de responsável, por exemplo, a expressão
	 * associada ao tipo de responsável.
	 * 
	 * @return
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * Define o id do tipo de responsável.
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Define o texto amigável do tipo de responsável.
	 * 
	 * @param texto
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}

	/**
	 * Define o valor (por exemplo, a expressão) do tipo de responsável.
	 * 
	 * @param valor
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * Retorna o texto amigável do tipo de responsável.
	 */
	public String toString() {
		return this.getId() + ")" + this.getTexto();
	}
}
