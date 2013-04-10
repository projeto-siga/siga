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

		try {
			HashMap<String, String> propsPlay = SigaBaseProperties.obterTodas();
			for (String s : propsPlay.keySet()) {
				if (!s.contains("hibernate.transaction"))
					Play.configuration.put(s, propsPlay.get(s));
			}
		} catch (Exception e) {
			Logger.error("Ocorreu um erro ao configurar o Play: "
					+ e.getMessage());
			e.printStackTrace();
		}

	}

}
