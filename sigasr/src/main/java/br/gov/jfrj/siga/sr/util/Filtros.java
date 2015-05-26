package br.gov.jfrj.siga.sr.util;

import br.gov.jfrj.siga.model.Historico;

public abstract class Filtros {

    public static boolean deveAdicionar(Historico obj) {
        return (obj != null && obj.getIdInicial() != null && obj.getIdInicial() > 0L);
    }
}