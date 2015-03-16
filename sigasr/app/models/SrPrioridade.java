package models;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public enum SrPrioridade {

	IMEDIATO(5, "Imediata"), ALTO(4, "Alta"), MEDIO(3, "Média"), 
	BAIXO(2, "Baixa"), PLANEJADO(1, "Planejada");
	
	public int idPrioridade;
	public String descPrioridade;
	
	private SrPrioridade(int idPrioridade, String descPrioridade) {
		this.idPrioridade = idPrioridade;
		this.descPrioridade = descPrioridade;
	}
	
	public static List<SrPrioridade> getValoresEmOrdem() {
		List<SrPrioridade> prioridades = Arrays.asList(values());
		Collections.sort(prioridades, new Comparator<SrPrioridade>() {
			@Override
			public int compare(SrPrioridade o1, SrPrioridade o2) {
				return o2.idPrioridade - o1.idPrioridade;
			}
		});
		return prioridades;
	}
}
