package models;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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

	public static JsonObject getJSON() {
		Gson gson = new Gson();
		
		JsonObject obj = new JsonObject();

		for (SrPrioridade srPrioridade : values()) {
			obj.add(srPrioridade.name(), gson.toJsonTree(srPrioridade.idPrioridade));
		}
		return obj;
	}
}
