package br.gov.jfrj.siga.tp.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.jboss.logging.Logger;

public class SigaProperties {
	
	private static final Logger LOG = Logger.getLogger(SigaProperties.class); 
	
	private SigaProperties(){
	}

	public static String getValue(String key) {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = SigaProperties.class.getClassLoader().getResourceAsStream(
					"conf/config.properties");
			prop.load(input);
		} catch (IOException e) {
			LOG.error("IOException: ", e);
		}

		return prop.getProperty(key);
	}
}
