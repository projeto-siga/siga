package br.gov.jfrj.siga.cp.auth;

import java.util.ServiceLoader;

public class AutenticadorFabrica {

    static ServiceLoader<Autenticador> loader = ServiceLoader.load(Autenticador.class);

    public static Autenticador getInstance() {
        if (loader != null) {
            for (Autenticador instancia : loader) {
                return instancia;
            }
        } else {
            throw new RuntimeException("Não foi possível carregar o autenticador");
        }
        return null;
    }
}