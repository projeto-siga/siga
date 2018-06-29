package br.gov.jfrj.siga.tp.util;

import java.util.logging.Logger;

import br.gov.jfrj.siga.tp.model.Parametro;

public class VerificadorEnvioEmail {

    private static final String CRON_NODEEXECUTAENVIOEMAIL = "cron.nodeQueExecutaEnvioEmail";
    
	public static boolean possoEnviarEmail() {
		String nodeLocal = System.getProperty("jboss.node.name");
		String nodeQueEnviaEmail = Parametro.buscarConfigSistemaEmVigor(CRON_NODEEXECUTAENVIOEMAIL);
		if(nodeQueEnviaEmail == null || (!nodeLocal.equalsIgnoreCase(nodeQueEnviaEmail))) {
			Logger.getLogger("verificador_envioEmail").info("Node \"" + nodeLocal + "\" nao configurado para enviar emails.");
			return false;
		}
		return true;
	}

}