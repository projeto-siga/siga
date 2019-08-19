package br.gov.jfrj.siga.base;

import java.util.ResourceBundle;

public class SigaMessages {
	public static ResourceBundle bundle;

//	private static void changeLocale(final String local) {
//		if (SigaBaseProperties.getString("siga.local") == "TRF2") {
//			// TO DO 
//		} else {
//			// TO DO
//		}
//	}

	public static String getMessage(String key) {
		try {
	    	if (bundle == null) {
		    	bundle = getBundle();
	    	}
		    String message = bundle.getString(key);
			return message;
		} catch (Exception e) {
			return "???." + key + ".???";
		}
	}

	public static boolean isSigaSP() {
    	if (SigaBaseProperties.getString("siga.local") != null && "GOVSP".equals(SigaBaseProperties.getString("siga.local"))) {
    		return true;
    	}
    	return false;
    }
	
	public static String getLocalizationContext() {
		String messages = System.getProperty("siga.messages");
		if (messages == null) {
			messages = "messages";
			if (SigaBaseProperties.getString("siga.local") != null) {
	    		messages += "_" + SigaBaseProperties.getString("siga.local");
	    	}
		}
		return messages;
	}
	
	public static ResourceBundle getBundle() {
		return ResourceBundle.getBundle(getLocalizationContext());
    }

}