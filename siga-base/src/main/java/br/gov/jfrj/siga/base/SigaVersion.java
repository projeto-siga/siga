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
	public static String SIGA_PATCH_VERSION;
	public static String SIGA_TAG_VERSION;
	public static String SIGA_VERSION_DATE;
	
	public static void loadSigaVersion() {
		try {
			
			SIGA_VERSION = Manifests.read("Build-Label") != null ? Manifests.read("Build-Label") : "SNAPSHOT";
			SIGA_PROJECT_VERSION = Manifests.read("Project-Version") != null ? Manifests.read("Project-Version") : "SNAPSHOT";
			SIGA_VERSION_DATE = Manifests.read("Build-Time") != null ? Manifests.read("Build-Time") : "NODATE";
			SIGA_TAG_VERSION = Manifests.read("SCM-Tag") != null ? Manifests.read("SCM-Tag") : "NOTAG";
			
		} catch (Exception e) {
			//reseta default
			SIGA_VERSION = "SNAPSHOT";
			SIGA_VERSION_DATE = "NODATE";
			
			e.printStackTrace();
		}
	}

}
