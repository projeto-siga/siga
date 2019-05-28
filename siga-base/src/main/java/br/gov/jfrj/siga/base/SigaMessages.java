package br.gov.jfrj.siga.base;

import java.util.ResourceBundle;

public class SigaMessages {
	public static ResourceBundle bundle;

	private static void changeLocale(final String local) {
		if (SigaBaseProperties.getString("siga.local") == "TRF2") {
			
		} else {
			
		}
	}

	public static String getMessage(String key) {
		try {
	    	if (bundle == null) {
		    	if (SigaBaseProperties.getString("siga.local") == null) {
		    		bundle = ResourceBundle.getBundle("messages_TRF2");
		    	} else {
		    		bundle = ResourceBundle.getBundle("messages_" + SigaBaseProperties.getString("siga.local"));
		    	}
	    	}
		    String message = bundle.getString(key);
			return message;
		} catch (Exception e) {
			return "???." + key + ".???";
		}
	}
}