package br.gov.jfrj.siga.ex.util;

import java.util.Comparator;

import br.gov.jfrj.siga.ex.ExMobil;

public class TipoMobilComparatorInverso implements Comparator<ExMobil> {

	public int compare(ExMobil o1, ExMobil o2) {
		if (o1.getExTipoMobil().getIdTipoMobil() > o2.getExTipoMobil()
				.getIdTipoMobil())
			return -1;
		else if (o1.getExTipoMobil().getIdTipoMobil() < o2.getExTipoMobil()
				.getIdTipoMobil())
			return 1;
		else if (o1.getNumSequencia() > o2.getNumSequencia())
			return -1;
		else if (o1.getNumSequencia() < o2.getNumSequencia())
			return 1;
		else
			return 0;
	}
}
