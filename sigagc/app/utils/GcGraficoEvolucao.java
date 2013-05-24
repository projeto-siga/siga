package utils;

import java.util.TreeSet;

public class GcGraficoEvolucao extends TreeSet<GcGraficoEvolucaoItem> {

	@Override
	public boolean add(GcGraficoEvolucaoItem e) {
		if (this.contains(e)) {
			this.floor(e).somar(e);
			return false;
		}
		return super.add(e);
	}

}
