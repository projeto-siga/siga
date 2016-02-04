package br.gov.jfrj.siga.sr.model;

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

            // Quando c1 Ã© mais abstrato, retorna 1.

            if (srC1.getItemConfiguracaoSet() == null) {
                if (srC2.getItemConfiguracaoSet() != null)
                    return 1;
            } else {
                if (srC2.getItemConfiguracaoSet() == null)
                    return -1;
                else {
                    int nivelSrC1 = srC1.getNivelItemParaComparar();
                    int nivelSrC2 = srC2.getNivelItemParaComparar();
                    if (nivelSrC1 < nivelSrC2)
                        return 1;
                    if (nivelSrC1 > nivelSrC2)
                        return -1;
                }
            }

            if (srC1.getAcoesSet() == null) {
                if (srC2.getAcoesSet() != null)
                    return 1;
            } else {
                if (srC2.getAcoesSet() == null)
                    return -1;
                else {
                    int nivelSrC1 = srC1.getNivelAcaoParaComparar();
                    int nivelSrC2 = srC2.getNivelAcaoParaComparar();
                    if (nivelSrC1 < nivelSrC2)
                        return 1;
                    if (nivelSrC1 > nivelSrC2)
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
