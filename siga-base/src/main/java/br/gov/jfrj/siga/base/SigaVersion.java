package br.gov.jfrj.siga.base;

import javax.inject.Singleton;
import com.jcabi.manifests.Manifests;

@Singleton
public class SigaVersion {

	public static String SIGA_VERSION = "SNAPSHOT";
	public static String SIGA_VERSION_DATA = "NODATA";
	
	public static void loadSigaVersion() {
		try {
			String value = Manifests.read("Build-Label");
			if (value != null) {
				SIGA_VERSION = value;
				SIGA_VERSION_DATA = Manifests.read("Build-Time");
			}
		} catch (Exception e) {
			//reseta default
			SIGA_VERSION = "SNAPSHOT";
			SIGA_VERSION_DATA = "NODATA";
			
			e.printStackTrace();
		}
	}

}
