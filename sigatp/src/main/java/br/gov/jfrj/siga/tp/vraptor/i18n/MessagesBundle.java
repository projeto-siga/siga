package br.gov.jfrj.siga.tp.vraptor.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;


public class MessagesBundle {
    private static final ThreadLocal<ResourceBundle> THREAD_LOCAL = new ThreadLocal<ResourceBundle>();

    public static void set(ResourceBundle localization) {
        THREAD_LOCAL.set(localization);
    }

    @SuppressWarnings("all")
    public static String getMessage(String key, String... args) {
    	ResourceBundle localization = THREAD_LOCAL.get();
    	
        if (localization == null) {
            return "???" + key + "???";
        }
    	
    	if (args == null) {
    		   return localization.getString(key);
    	} else {
    		MessageFormat fmt = new MessageFormat(key);
    		return localization.getString(fmt.format(args));
    	}
      
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}