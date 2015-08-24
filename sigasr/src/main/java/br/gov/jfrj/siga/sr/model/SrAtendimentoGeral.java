package br.gov.jfrj.siga.sr.model;

import java.util.Date;
import java.util.List;


public class SrAtendimentoGeral extends AbstractSrEtapa {

	@Override
	public String getDescricao() {
		return "Atendimento (total)";
	}	
	
	@SuppressWarnings("unchecked")
	public List<SrAtendimento> getAtendimentos(){
		return (List<SrAtendimento>)getIntervalos();
	}
	
}
