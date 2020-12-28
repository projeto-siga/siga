package br.gov.jfrj.siga.tp.util;

import br.gov.jfrj.siga.tp.auth.annotation.DadosAuditoria;

public class ContextoRequest
{
    private static final ThreadLocal<DadosAuditoria> dadosAuditoriaByThread;
    
    static {
        dadosAuditoriaByThread = new ThreadLocal<DadosAuditoria>();
    }
    
    public static void setDadosAuditoria(final DadosAuditoria dadosAuditoria) {
        ContextoRequest.dadosAuditoriaByThread.set(dadosAuditoria);
    }
    
    public static DadosAuditoria getDadosAuditoria() {
        return ContextoRequest.dadosAuditoriaByThread.get();
    }
    
    public static void removeDadosAuditoria() {
        ContextoRequest.dadosAuditoriaByThread.remove();
    }
}