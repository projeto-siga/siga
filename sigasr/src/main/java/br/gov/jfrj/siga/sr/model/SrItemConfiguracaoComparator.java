package br.gov.jfrj.siga.sr.model;

import java.util.Comparator;

public class SrItemConfiguracaoComparator implements Comparator<SrItemConfiguracao> {

    @Override
    public int compare(SrItemConfiguracao o1, SrItemConfiguracao o2) {
        if (o1 != null && o2 != null && o1.getIdItemConfiguracao() == o2.getIdItemConfiguracao())
            return 0;
        return o1.getSiglaItemConfiguracao().compareTo(o2.getSiglaItemConfiguracao());
    }

}
