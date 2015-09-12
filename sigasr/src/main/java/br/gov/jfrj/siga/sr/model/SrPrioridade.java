package br.gov.jfrj.siga.sr.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public enum SrPrioridade {

    IMEDIATO(5, "Imediata"), ALTO(4, "Alta"), MEDIO(3, "Média"), BAIXO(2, "Baixa"), PLANEJADO(1, "Planejada");

    private int idPrioridade;
    private String descPrioridade;

    private SrPrioridade(int idPrioridade, String descPrioridade) {
        this.setIdPrioridade(idPrioridade);
        this.setDescPrioridade(descPrioridade);
    }

    public static List<SrPrioridade> getValoresEmOrdem() {
        List<SrPrioridade> prioridades = Arrays.asList(values());
        Collections.sort(prioridades, new Comparator<SrPrioridade>() {
            @Override
            public int compare(SrPrioridade o1, SrPrioridade o2) {
                return o2.getIdPrioridade() - o1.getIdPrioridade();
            }
        });
        return prioridades;
    }

    public static JsonObject getJSON() {
        Gson gson = new Gson();

        JsonObject obj = new JsonObject();

        for (SrPrioridade srPrioridade : values()) {
            obj.add(srPrioridade.name(), gson.toJsonTree(srPrioridade.getIdPrioridade()));
        }
        return obj;
    }

    public int getIdPrioridade() {
        return idPrioridade;
    }

    public void setIdPrioridade(int idPrioridade) {
        this.idPrioridade = idPrioridade;
    }

    public String getDescPrioridade() {
        return descPrioridade;
    }

    public void setDescPrioridade(String descPrioridade) {
        this.descPrioridade = descPrioridade;
    }
    
    
}
