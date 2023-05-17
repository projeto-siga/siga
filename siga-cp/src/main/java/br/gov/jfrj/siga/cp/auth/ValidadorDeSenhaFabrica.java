package br.gov.jfrj.siga.cp.auth;

import java.util.ServiceLoader;

public class ValidadorDeSenhaFabrica {

//    static ServiceLoader<ValidadorDeSenha> loader = ServiceLoader.load(ValidadorDeSenha.class);

    public static ValidadorDeSenha getInstance() {
        ServiceLoader<ValidadorDeSenha> loader = ServiceLoader.load(ValidadorDeSenha.class);
        if (loader != null) {
            for (ValidadorDeSenha instancia : loader) {
                return instancia;
            }
        } else {
            throw new RuntimeException("Não foi possível carregar o validador de senha");
        }
        return null;
    }
}
