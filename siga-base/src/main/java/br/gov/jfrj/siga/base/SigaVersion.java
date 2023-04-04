package br.gov.jfrj.siga.base;

import javax.inject.Singleton;
import com.jcabi.manifests.Manifests;


/**
 * Classe auxiliar para obtenção e parse dos dados de versionamento do SIGA.
 *
 * @author Dinarde Bezerra @dinarde
 */

@Singleton
public class SigaVersion {

	public static String SIGA_VERSION;
	public static String SIGA_PROJECT_VERSION;
	public static String SIGA_VERSION_DATE;
	
	public static void loadSigaVersion() {
		try {
			SIGA_VERSION = Manifests.read("Build-Label") != null ? Manifests.read("Build-Label")
					.replace("-SNAPSHOT", "")
					.replace("-RELEASE", "") : "SNAPSHOT";
			
			SIGA_PROJECT_VERSION = Manifests.read("Project-Version") != null ? Manifests.read("Project-Version") : "SNAPSHOT";
			
			//Caso release não esteja taggeada, pega do project version
			if (SIGA_VERSION.startsWith("-")) {
				SIGA_VERSION = SIGA_PROJECT_VERSION;
			}
			
			SIGA_VERSION_DATE = Manifests.read("Build-Time") != null ? Manifests.read("Build-Time") : "NODATE";
		} catch (Exception e) {
			//reseta default
			SIGA_VERSION = "SNAPSHOT";
			SIGA_VERSION_DATE = "NODATE";
			
			e.printStackTrace();
		}
	}

}
