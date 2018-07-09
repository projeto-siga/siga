package br.gov.jfrj.siga.tp.util;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.model.ActiveRecord;

public enum Combo {

	Cor("cores","br.gov.jfrj.siga.tp.model"), 
	Grupo("grupos","br.gov.jfrj.siga.tp.model"), 
	DpLotacao("dpLotacoes","br.gov.jfrj.siga.dp"), 
	CategoriaCNH("categoriasCNH","br.gov.jfrj.siga.tp.model"),
	Fornecedor("fornecedores","br.gov.jfrj.siga.tp.model"), 
	Veiculo("veiculos","br.gov.jfrj.siga.tp.model");
	
	private String descricao;
	private String pacote;
	
	Combo(String descricao, String pacote){
		this.setDescricao(descricao);
		this.setPacote(pacote);
	}

	private void setPacote(String pacote) {
		this.pacote = pacote;
	}
	
	public String getPacote() {
		return this.pacote;
	}

	private void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	@Override
	public String toString() {
		return this.name();
	}
		
	public String nomeCompletoDaClasse() {
		return this.pacote + "." +  this.name();
	}
	
	/**
	 * Metodo responsável por renderizar as listas que serão exibidas aas combos dos forms.
	 * Foi utilizado uma convenção abaixo:
	 *     Passado o parâmetro Combo.Cor , é retornado o atributo cores que representa a 
	 *     lista de cores.
	 * Veja o exemplo da inclusão de um novo enumerator
	 *     Avarias("avarias")
	 * @param parametrosRequest - RenderArgs
	 * @param args - Lista dos enumerators Combo
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Result montar(Result result, Combo... args) throws Exception {
		for (Combo combo : args) {
			Class<?> nomeDaClasse = Class.forName(combo.nomeCompletoDaClasse());
			ActiveRecord<?> activeRecord = new ActiveRecord(nomeDaClasse);
			result.include(combo.getDescricao(), activeRecord.findAll());
		}
		return result;
	}
}
