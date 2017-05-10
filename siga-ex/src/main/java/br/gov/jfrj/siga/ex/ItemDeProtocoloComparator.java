package br.gov.jfrj.siga.ex;

import java.util.Comparator;

import br.gov.jfrj.siga.sinc.lib.Sincronizavel;

public class ItemDeProtocoloComparator implements Comparator<ItemDeProtocolo>{
	
	public int compare(ItemDeProtocolo obj1, ItemDeProtocolo obj2) {
		ExDocumento doc1 = obj1.getDoc();
		ExMobil mob1 = obj1.getMob();
		ExDocumento doc2 = obj2.getDoc();
		ExMobil mob2 = obj2.getMob();

		if (doc1.getAnoEmissao() > doc2.getAnoEmissao())
			return 1;
		else if (doc1.getAnoEmissao() < doc2.getAnoEmissao())
			return -1;
		else if (doc1.getExFormaDocumento().getIdFormaDoc() > doc2
				.getExFormaDocumento().getIdFormaDoc())
			return 1;
		else if (doc1.getExFormaDocumento().getIdFormaDoc() < doc2
				.getExFormaDocumento().getIdFormaDoc())
			return -1;
		else if (doc1.getNumExpediente() > doc2.getNumExpediente())
			return 1;
		else if (doc1.getNumExpediente() < doc2.getNumExpediente())
			return -1;
		else if (mob1.getExTipoMobil().getIdTipoMobil() > mob2
				.getExTipoMobil().getIdTipoMobil())
			return 1;
		else if (mob1.getExTipoMobil().getIdTipoMobil() < mob2
				.getExTipoMobil().getIdTipoMobil())
			return -1;
		else if (mob1.getNumSequencia() > mob2
				.getNumSequencia())
			return 1;
		else if (mob1.getNumSequencia() < mob2
				.getNumSequencia())
			return -1;
		else if (doc1.getIdDoc() > doc2.getIdDoc())
			return 1;
		else if (doc1.getIdDoc() < doc2.getIdDoc())
			return -1;
		else
			return 0;
	}

}
