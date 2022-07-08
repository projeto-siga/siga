package br.gov.jfrj.siga.cp.logic;

import br.gov.jfrj.siga.cp.CpToken;
import br.gov.jfrj.siga.dp.dao.CpDao;
import com.crivano.jlogic.Expression;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class CpTokenEValido implements Expression {

    private final Long tipoToken;
    private final String token;

    public CpTokenEValido(Long tipoToken, String token) {
        this.tipoToken = tipoToken;
        this.token = token;
    }

    @Override
    public boolean eval() {
        CpToken cpToken = CpDao.getInstance().obterCpTokenPorTipoToken(tipoToken, token);
        
        if (cpToken != null) {
            Date dt = CpDao.getInstance().consultarDataEHoraDoServidor();
            LocalDateTime dtNow = LocalDateTime.ofInstant(dt.toInstant(), ZoneId.systemDefault());
            LocalDateTime dtExp = LocalDateTime.ofInstant(cpToken.getDtExp().toInstant(), ZoneId.systemDefault());

            return dtNow.isBefore(dtExp);
        }

        return false;
    }

    @Override
    public String explain(boolean result) {
        if (result)
            return token + " é válido";
        else
            return token + " não é válido";
    }

}