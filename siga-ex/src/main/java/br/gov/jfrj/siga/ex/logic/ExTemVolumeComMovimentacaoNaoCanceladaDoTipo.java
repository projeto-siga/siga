package br.gov.jfrj.siga.ex.logic;

import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExVia;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import java.util.Set;

public class ExTemVolumeComMovimentacaoNaoCanceladaDoTipo implements Expression {
    private ExDocumento doc;
    private ITipoDeMovimentacao tipo;

    public ExTemVolumeComMovimentacaoNaoCanceladaDoTipo(ExDocumento doc, ITipoDeMovimentacao tipo) {
        this.doc = doc;
        this.tipo = tipo;
    }

    @Override
    public boolean eval() {
        Set<ExMobil> volumes = doc.getVolumes();
        if (volumes.isEmpty())
            return false;

        for (ExMobil volume : volumes)
            for (ExDocumento docFilho : volume.getExDocumentoFilhoSet())
                    if (docFilho.getPrimeiraVia().getMovsNaoCanceladas(tipo).size() > 0)
                        return true;

        return false;
    }

    @Override
    public String explain(boolean result) {
        return JLogic.explain("tem documento juntado com movimentação não cancelada do tipo " + tipo, result);
    }

}
