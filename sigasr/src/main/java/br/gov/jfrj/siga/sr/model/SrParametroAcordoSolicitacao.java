package br.gov.jfrj.siga.sr.model;

import java.util.Set;
import java.util.TreeSet;

public abstract class SrParametroAcordoSolicitacao {

	protected SrParametroAcordo paramAcordo;
	protected String descricao;

	public SrParametroAcordoSolicitacao(SrSolicitacao sol) {

		Set<SrParametroAcordo> set = new TreeSet<SrParametroAcordo>(
				new SrParametroAcordoComparator());
		for (SrAcordo a : sol.getAcordos())
			set.addAll(a.getParametroAcordoSet());

		//Edson: o primeiro que aparece na lista com a classe correta é o mais
		//restritivo, pois a lista está ordenada
		for (SrParametroAcordo par : set) {
			if (par.getClasse().equals(this.getClass())) {
				this.paramAcordo = par;
			}
		}
	}

	public abstract SrValor getValorRealizado();
	
	public SrSituacaoAcordo getSituacao(){
		return paramAcordo.getSituacaoParaOValor(getValorRealizado());
	}
	
	public boolean isAcordoSatisfeito(){
		return paramAcordo.isSatisfeitoPeloValor(getValorRealizado());
	}
	
	public SrSituacaoAcordo getSituacaoAcordo(){
		return paramAcordo.getSituacaoParaOValor(getValorRealizado());
	}

	public SrParametroAcordo getParamAcordo() {
		return paramAcordo;
	}

	public void setParamAcordo(SrParametroAcordo paramAcordo) {
		this.paramAcordo = paramAcordo;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
