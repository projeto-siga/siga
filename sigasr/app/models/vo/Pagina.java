package models.vo;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class Pagina {
	/**
	 * Registros da pagina;
	 */
	private JsonArray registros;
	/**
	 * Quantidade de registros encontradas na consulta
	 */
	private Integer count;
	/**
	 * Indice da pagina
	 */
	private Integer numero;
	/**
	 * Numero de registros por pagina
	 */
	private Integer tamanho;
	/**
	 * Coluna do order by
	 */
	private String orderBy;
	/**
	 * Direção do order by
	 */
	private String direcaoOrdenacao;

	public Pagina() {
		this.registros = new JsonArray();
	}

	public JsonArray getRegistros() {
		return registros;
	}

	public void setRegistros(JsonArray registros) {
		this.registros = registros;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Integer getTamanho() {
		return tamanho;
	}

	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}

	public boolean precisaExecutarCount() {
		return count == null;
	}

	public int getFistResult() {
		return (numero - 1) * tamanho;
	}

	public void addRegistro(JsonElement registro) {
		this.registros.add(registro);
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getDirecaoOrdenacao() {
		return direcaoOrdenacao;
	}

	public void setDirecaoOrdenacao(String direcaoOrdenacao) {
		this.direcaoOrdenacao = direcaoOrdenacao;
	}
}