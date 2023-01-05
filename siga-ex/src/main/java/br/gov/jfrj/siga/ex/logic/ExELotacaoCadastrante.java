package br.gov.jfrj.siga.ex.logic;

import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

public class ExELotacaoCadastrante implements Expression {

    private ExDocumento doc;
    private DpLotacao lotaTitular;

    public ExELotacaoCadastrante(ExDocumento doc, DpLotacao lotaTitular) {
        this.doc = doc;
        this.lotaTitular = lotaTitular;
    }

    @Override
    public boolean eval() {
        return Utils.equivale(doc.getLotaCadastrante(), lotaTitular);
    }

    @Override
    public String explain(boolean result) {
        return ( lotaTitular != null ? lotaTitular.getSiglaCompleta() : lotaTitular != null ?
                lotaTitular.getSiglaCompleta() : "" ) + ( result ? "" : JLogic.NOT ) + " é lotação cadastrante de " + doc.getCodigo();
    }
};
