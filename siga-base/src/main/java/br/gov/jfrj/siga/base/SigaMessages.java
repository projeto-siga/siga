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
    	if ("GOVSP".equals(Prop.get("/siga.local"))) {
    		return true;
    	}
    	return false;
    }
	
	public static String getLocalizationContext() {
		String messages = Prop.get("/siga.mensagens");
		if (messages == null) {
			messages = "messages";
			if (Prop.get("/siga.local") != null) {
	    		messages += "_" + Prop.get("/siga.local");
	    	} else {
	    		messages += "_SIGA";
	    	}
		}
		return messages;
	}
	
	private static ResourceBundle getBundle() {
		return ResourceBundle.getBundle(getLocalizationContext());
    }

}