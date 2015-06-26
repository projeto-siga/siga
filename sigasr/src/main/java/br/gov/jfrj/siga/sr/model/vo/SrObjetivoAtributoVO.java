package br.gov.jfrj.siga.sr.model.vo;

import br.gov.jfrj.siga.sr.model.SrObjetivoAtributo;

public class SrObjetivoAtributoVO extends AbstractSelecionavel {

    public SrObjetivoAtributoVO(Long id, String descricao) {
        super(id, descricao);
    }

    public static SrObjetivoAtributoVO createFrom(SrObjetivoAtributo objetivoAtributo) {
        if (objetivoAtributo != null)
            return new SrObjetivoAtributoVO(objetivoAtributo.getIdObjetivo(), objetivoAtributo.getDescrObjetivo());
        else
            return null;
    }

}
