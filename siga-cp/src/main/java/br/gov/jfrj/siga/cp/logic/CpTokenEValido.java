package br.gov.jfrj.siga.cp.logic;

import br.gov.jfrj.siga.cp.util.SigaUtil;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import java.util.Map;

public class CpTokenEValido implements Expression {
    String token;

    public CpTokenEValido(String token) {
        this.token = token;
    }

    @Override
    public boolean eval() {
        try {
            SigaUtil.verifyGetJwtToken(token);

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public String explain(boolean result) {
        if (result)
            return token + " é válido";
        else
            return token + " não é válido";
    }

}