package br.gov.jfrj.siga.cp.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

@Singleton
public class SigaVersion {
	

	public static String SIGA_VERSION = "develop";
	
	public static void loadSigaVersion(ClassLoader classLoader) {
		
		Map<String, String> manifest = new HashMap<>();
		try (InputStream is = classLoader.getResourceAsStream("/META-INF/MANIFEST.MF")) {
			String m = convertStreamToString(is); 
			if (m != null) {
				m = m.replaceAll("\r\n", "\n");
				for (String s : m.split("\n")) {
					String a[] = s.split(":", 2);
					if (a.length == 2) {
						manifest.put(a[0].trim(), a[1].trim());
					}
				}
			}
			SIGA_VERSION = manifest.get("sigaVersao");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static String convertStreamToString(java.io.InputStream is) {
		if (is == null)
			return null;
		try (java.util.Scanner s = new java.util.Scanner(is, "UTF-8")) {
			return s.useDelimiter("\\A").hasNext() ? s.next() : "";
		}
	}

}
