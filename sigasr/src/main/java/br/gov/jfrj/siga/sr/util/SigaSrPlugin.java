package br.gov.jfrj.siga.sr.util;

import java.util.HashMap;

import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.sr.model.Sr;
import play.Logger;
import play.Play;
import play.PlayPlugin;

public class SigaSrPlugin extends PlayPlugin {

	@Override
	public void onApplicationStart() {

		// Edson: diferentemente, do que ocorre no Siga-Doc, por exemplo, o
		// Cp.getInstance() eh chamado antes do Sr.getInstance(). Quem faz
		// isso eh um dos plugins do Play, ao executar o CpLocalidade.
		// obterMunicipios(). Sobrescrevendo com a instancia correta:
		if (Cp.isInstantiated())
			Cp.setInstance(null);
		Sr.getInstance();
		super.onApplicationStart();
	}

}
