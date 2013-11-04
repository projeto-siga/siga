package models;

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoComparator;

public class SrConfiguracaoComparator extends CpConfiguracaoComparator {

	public int compare(CpConfiguracao c1, CpConfiguracao c2) {

		int i = super.compareSelectedFields(c1, c2);
		if (i != 0)
			return i;

		if (c1 instanceof SrConfiguracao && c2 instanceof SrConfiguracao) {
			SrConfiguracao srC1 = (SrConfiguracao) c1;
			SrConfiguracao srC2 = (SrConfiguracao) c2;

			// Quando c1 é mais abstrato, retorna 1.

			if (srC1.itemConfiguracao == null) {
				if (srC2.itemConfiguracao != null)
					return 1;
			} else {
				if (srC2.itemConfiguracao == null)
					return -1;
				else {
					if (srC1.itemConfiguracao.getNivel() < srC2.itemConfiguracao
							.getNivel())
						return 1;
					if (srC1.itemConfiguracao.getNivel() > srC2.itemConfiguracao
							.getNivel())
						return -1;
				}
			}

			if (srC1.servico == null) {
				if (srC2.servico != null)
					return 1;
			} else {
				if (srC2.servico == null)
					return -1;
				else {
					if (srC1.servico.getNivel() < srC2.servico
							.getNivel())
						return 1;
					if (srC1.servico.getNivel() > srC2.servico
							.getNivel())
						return -1;
				}
			}
			
		}

		i = super.untieSelectedFields(c1, c2);
		if (i != 0)
			return i;

		return c1.getIdConfiguracao().compareTo(c2.getIdConfiguracao());
	}
}
