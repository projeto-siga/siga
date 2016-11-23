package br.gov.jfrj.siga.sr.util;

import br.gov.jfrj.siga.model.Historico;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrAtributoSolicitacao;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;

public abstract class Filtros {

    private Filtros() {
    }

    public static boolean deveAdicionar(Historico obj) {
        return obj != null && obj.getIdInicial() != null && obj.getIdInicial() > 0L;
    }

    public static boolean deveAdicionar(SrItemConfiguracao obj) {
        return obj != null && obj.getIdItemConfiguracao() != null && obj.getIdItemConfiguracao() > 0L;
    }

    public static boolean deveAdicionar(SrAcao obj) {
        return obj != null && obj.getAcaoInicial() != null && obj.getAcaoInicial().getIdAcao() != null && obj.getAcaoInicial().getIdAcao() > 0L;
    }
    
    public static boolean deveAdicionar(SrAtributoSolicitacao obj) {
    	return obj != null && deveAdicionar(obj.getAtributo());
    }
}