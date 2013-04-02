package util;

import java.util.HashMap;

import br.gov.jfrj.siga.base.SigaBaseProperties;
import play.Logger;
import play.Play;
import play.PlayPlugin;

public class SigaSrPlugin extends PlayPlugin {

	@Override
	public void onConfigurationRead() {
		super.onConfigurationRead();

		try {
			HashMap<String, String> propsPlay = SigaSrProperties
					.getPropriedadesPlay();
			for (String s : propsPlay.keySet()) {
				Play.configuration.put(s.substring(s.indexOf("play.") + 5),
						propsPlay.get(s));
			}
		} catch (Exception e) {
			Logger.error("Ocorreu um erro ao configurar o Play: "
					+ e.getMessage());
			e.printStackTrace();
		}
/*
		Play.configuration.put(
				"db.user",
				SigaSrProperties.getString(SigaSrProperties.getAmbiente()
						+ ".play.db.user"));
		Play.configuration.put(
				"db.pass",
				SigaSrProperties.getString(SigaSrProperties.getAmbiente()
						+ ".play.db.pass"));
		Play.configuration.put(
				"db.driver",
				SigaSrProperties.getString(SigaSrProperties.getAmbiente()
						+ ".play.db.driver"));
		Play.configuration.put(
				"db.url",
				SigaSrProperties.getString(SigaSrProperties.getAmbiente()
						+ ".play.db.url"));
		Play.configuration.put(
				"jpa.debugSQL",
				SigaSrProperties.getString(SigaSrProperties.getAmbiente()
						+ ".play.db.url"));
		Play.configuration.put(
				"mail.smtp.host",
				SigaSrProperties.getString(SigaSrProperties.getAmbiente()
						+ ".play.db.url"));
		Play.configuration.put(
				"mail.smtp.user",
				SigaSrProperties.getString(SigaSrProperties.getAmbiente()
						+ ".play.db.url"));
		Play.configuration.put(
				"mail.smtp.pass",
				SigaSrProperties.getString(SigaSrProperties.getAmbiente()
						+ ".play.db.url"));
		Play.configuration.put(
				"mail.debug",
				SigaSrProperties.getString(SigaSrProperties.getAmbiente()
						+ ".play.db.url"));
		Play.configuration.put(
				"mail.smtp.port",
				SigaSrProperties.getString(SigaSrProperties.getAmbiente()
						+ ".play.db.url"));*/
	}

}
