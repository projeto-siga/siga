package br.gov.jfrj.siga.cp.auth;

import br.gov.jfrj.siga.cp.CpIdentidade;

public interface ValidadorDeSenha {
    boolean validarSenha(CpIdentidade identidade, String senha);
    
    String gerarMessageDigest(String senha);
}
