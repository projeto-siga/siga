package br.gov.jfrj.siga.sr.model;

import java.util.List;

public interface SrParametroAcordoSolicitacao {

	public SrValor getValorRealizado();

	boolean isAcordoSatisfeito();
	
	public SrSituacaoAcordo getSituacaoAcordo();

	public List<SrParametroAcordo> getParamsAcordo();

	public void setParamsAcordo(List<SrParametroAcordo> paramsAcordo);

}
