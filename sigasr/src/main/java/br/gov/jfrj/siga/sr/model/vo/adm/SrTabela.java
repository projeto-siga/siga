package br.gov.jfrj.siga.sr.model.vo.adm;

import java.util.List;

public class SrTabela<T> {
	
	private List<T> itens;
	
	private String nome;
	
	private List<SrColuna> colunas;
	
	private SrModal modal;
	
	public SrTabela(List<T> itens, String nome){
		this.itens = itens;
		this.nome = nome;
	}
	
	public SrTabela add(String label, String propriedade, SrTipoCampo tipo){
		addColuna(label, propriedade);
		getModal().addCampo(label, propriedade, tipo);
		return this;
	}
	
	public SrTabela addColuna(String label, String propriedade){
		colunas.add(new SrColuna(label, propriedade));
		return this;
	}
	
	private SrModal getModal(){
		if (modal == null)
			modal = new SrModal();
		return modal;
	}

}
