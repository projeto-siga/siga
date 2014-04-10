package br.gov.jfrj.siga.wf.relatorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.wf.relatorio.RelTempoDocDetalhado.Tarefa;

public class EstatisticaGrupoRel {

	
	private Map<String,List<List<Tarefa>>> mapGrupos = new HashMap<String, List<List<Tarefa>>>();

	public void addGrupo(List<Tarefa> grupoAtual) {
		String numDoc = grupoAtual.get(0).getNumeroDocumento();
		List<List<Tarefa>> gruposDoc = mapGrupos.get(numDoc);
		if(gruposDoc==null){
			gruposDoc = new ArrayList<List<Tarefa>>();
			mapGrupos.put(numDoc, gruposDoc);
		}
		
		
		List<Tarefa> grupoClone =new ArrayList<RelTempoDocDetalhado.Tarefa>(); 
		grupoClone.addAll(grupoAtual);		
		gruposDoc.add(grupoClone);
		
		
		
	}

	public String getMediaGrupo(String numeroDocumento) {
		List<List<Tarefa>> listaGrupos = mapGrupos.get(numeroDocumento);
		long duracaoTotal = 0L; 
		long totalGrupos = 0;
		
		if(listaGrupos!=null){
			totalGrupos+=listaGrupos.size();
			for (List<Tarefa> listaTarefas : listaGrupos) {
				
				for (Tarefa tarefa : listaTarefas) {
					duracaoTotal += tarefa.getDuracaoEmMili();
				}
				
			}
		}
		
		return totalGrupos==0L?"0":SigaCalendar.formatDHM(duracaoTotal/totalGrupos);
	}

}
