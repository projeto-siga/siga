package br.gov.jfrj.siga.ex.util;

import java.util.Comparator;

import br.gov.jfrj.siga.ex.ExMovimentacao;

public class AnexoNumeradoComparator implements Comparator<ExMovimentacao> {

	public int compare(ExMovimentacao o1, ExMovimentacao o2) {
		try {
			int i = o1.getDtIniMovParaInsercaoEmDossie().compareTo(
					o2.getDtIniMovParaInsercaoEmDossie());
			if (i != 0)
				return i;
			i = o1.getIdMov().compareTo(o2.getIdMov());
			return i;
		} catch (final Exception ex) {
			return 0;
		}
	}

}
