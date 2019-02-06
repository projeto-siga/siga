package br.gov.jfrj.siga.sr.util; 

import java.util.Date;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class ContadorHorasTrabalhadas {

	private DpPessoa pess;
	
	private DpLotacao lota;
	
	// private DefinicaoHorario h; 
	
	public ContadorHorasTrabalhadas(DpPessoa pess, DpLotacao lota){
		this.pess = pess;
		this.lota = lota;
		//h = CpConfiguracao.buscar()...
	}
	
	public long getTempoTrabalhado(Date dtIni, Date dtFim){
		//Edson: Varrer os horários e também os feriados e 
		//retornar quanto tempo efetivo se passou
		return dtFim.getTime() - dtIni.getTime();
	}
	
	public boolean isTrabalhandoNaData(Date dt){
		//Edson: chamar o mesmo mecanismo acima mas retornar um boolean
		return true;
	}
	
	public Date getDataAPartirDe(Date dtBase, Long millisAdiante){
		//Edson: chamar o mecanismo acima para fazer previsões
		return new Date(dtBase.getTime() + millisAdiante);
	}
	
}
