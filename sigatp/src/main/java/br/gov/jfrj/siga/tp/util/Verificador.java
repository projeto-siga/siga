package br.gov.jfrj.siga.tp.util;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class Verificador {
	public static boolean estamosExecutandoNoCron() {
		try {
			return (ContextoRequest.getDadosAuditoria() == null);
		} catch (Exception Ex ) {
			return true;
		}
	}
}