package br.gov.jfrj.siga.cp.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Autenticador {
    public static final String SIGA_JWT_AUTH_COOKIE_NAME = "siga-jwt-auth";
    public static final String SIGA_JWT_AUTH_COOKIE_DOMAIN = null;

    public static final int TIME_TO_EXPIRE_IN_S = 60 * 60 * 8; // 8h é o tempo
                                                               // de duração
    public static final int TIME_TO_RENEW_IN_S = 60 * 60 * 7; // renova
                                                              // automaticamente
                                                              // 7h antes de
                                                              // expirar

    static final String PROVIDER_ISSUER = "sigaidp";
    static final String TOKEN_TYPE_KEY = "typ";
    static final String TOKEN_TYPE_VALUE = "auth";
    static long DEFAULT_TTL_TOKEN = 3600; // default 1 hora

    String criarCookie(HttpServletRequest req, HttpServletResponse resp, String principal) throws Exception;

    String obterPrincipal(HttpServletRequest req, HttpServletResponse resp) throws Exception;

    void removerCookie(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
