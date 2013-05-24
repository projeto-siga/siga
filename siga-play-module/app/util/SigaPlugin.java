package util;

import java.util.HashMap;

import br.gov.jfrj.siga.base.SigaBaseProperties;
import play.Logger;
import play.Play;
import play.PlayPlugin;

public class SigaPlugin extends PlayPlugin {

	@Override
	public void onConfigurationRead() {
		super.onConfigurationRead();
		String applicationName = Play.configuration
				.getProperty("application.name");
		String applicationMode = SigaBaseProperties.getAmbiente();

		try {
			HashMap<String, String> propsPlay = SigaBaseProperties.obterTodas();
			for (String s : propsPlay.keySet()) {
				if (s.startsWith(applicationName + "." + applicationMode
						+ ".play.")
						|| s.startsWith(applicationMode + ".play."))
					Play.configuration.put(
							s.substring(s.indexOf(".play.") + 6),
							propsPlay.get(s));
			}
			Play.configuration.put("servidor.principal",
					propsPlay.get(applicationMode + ".servidor.principal"));
		} catch (Exception e) {
			Logger.error("Ocorreu um erro ao configurar o Play: "
					+ e.getMessage());
			e.printStackTrace();
		}

	}

}
