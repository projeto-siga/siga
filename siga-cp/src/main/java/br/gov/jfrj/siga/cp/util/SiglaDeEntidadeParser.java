package br.gov.jfrj.siga.cp.util;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class SiglaDeEntidadeParser {
    boolean valida;
    String orgao;
    String especie;
    String modulo;

    public SiglaDeEntidadeParser(String sigla) {
        sigla = sigla.trim().toUpperCase();

        Map<String, CpOrgaoUsuario> mapAcronimo = new TreeMap<String, CpOrgaoUsuario>();
        for (CpOrgaoUsuario ou : CpDao.getInstance().listarOrgaosUsuariosTodos()) {
            mapAcronimo.put(ou.getAcronimoOrgaoUsu(), ou);
            mapAcronimo.put(ou.getSiglaOrgaoUsu(), ou);
        }

        StringBuilder acronimos = new StringBuilder();
        for (String s : mapAcronimo.keySet()) {
            if (acronimos.length() > 0)
                acronimos.append("|");
            acronimos.append(s);
        }

        final Pattern p1 = Pattern.compile("^(?<orgao>" + acronimos.toString()
                + ")?-?(?:(?<especie>[A-Za-z]{3})|(?<modulo>SR|TMPSR|GC|TMPGC|DP|WF|TP))-?([0-9][0-9A-Za-z\\.\\-\\/]*)$");
        final Matcher m1 = p1.matcher(sigla);

        valida = m1.find();
        if (valida) {
            orgao = m1.group("orgao");
            especie = m1.group("especie");
            modulo = m1.group("modulo");
        }
    }

    public boolean isValida() {
        return valida;
    }

    public String getOrgao() {
        return orgao;
    }

    public String getEspecie() {
        return especie;
    }

    public String getModulo() {
        return modulo;
    }
}
