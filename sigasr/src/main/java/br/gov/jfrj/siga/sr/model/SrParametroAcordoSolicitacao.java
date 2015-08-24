package br.gov.jfrj.siga.sr.model;

import java.util.Set;
import java.util.TreeSet;

public interface SrParametroAcordoSolicitacao {

	public SrValor getValorRealizado();

	boolean isAcordoSatisfeito();
	
	public SrSituacaoAcordo getSituacaoAcordo();

	public SrParametroAcordo getParamAcordo();

	public void setParamAcordo(SrParametroAcordo paramAcordo);

}
