package br.gov.jfrj.siga.sr.util;

import br.gov.jfrj.siga.model.Historico;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;

public abstract class Filtros {
    
    private Filtros(){
    }

    public static boolean deveAdicionar(Historico obj) {
        return obj != null && obj.getIdInicial() != null && obj.getIdInicial() > 0L;
    }
    
    public static boolean deveAdicionar(SrItemConfiguracao obj) {
        return obj != null && obj.getIdItemConfiguracao() != null && obj.getIdItemConfiguracao() > 0L; 
    }
    
    public static boolean deveAdicionar(SrAcao obj) {
        return obj != null && obj.getIdAcao() != null && obj.getIdAcao() > 0L;
    }
}