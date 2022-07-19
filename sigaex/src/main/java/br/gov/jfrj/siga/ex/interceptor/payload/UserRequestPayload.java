package br.gov.jfrj.siga.ex.interceptor.payload;

import br.gov.jfrj.siga.base.HttpRequestUtils;
import br.gov.jfrj.siga.dp.DpPessoa;

import javax.servlet.http.HttpServletRequest;

public class UserRequestPayload {
    private HttpServletRequest request;
    private String requestURL;
    private String requestParams;
    private String sessionId;
    private String userIP;

    private DpPessoa cadastrante;
    private String matricula;
    private String lotacao;
    private String nome;
    

    public UserRequestPayload(HttpServletRequest request, DpPessoa cadastrante) {
        this.request = request;
        this.requestURL = request.getRequestURL().toString();
        this.requestParams = request.getQueryString();
        this.sessionId = request.getRequestedSessionId();
        this.userIP = HttpRequestUtils.getIpAudit(request);
        
        this.cadastrante = cadastrante;
        this.matricula = cadastrante.getMatricula().toString();
        this.lotacao = cadastrante.getLotacao().toString();
        this.nome = cadastrante.getNomePessoa();
        
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public DpPessoa getCadastrante() {
        return cadastrante;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUserIP() {
        return userIP;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getLotacao() {
        return lotacao;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "UserRequestPayload{" +
                "requestURL='" + requestURL + '\'' +
                ", requestParams='" + requestParams + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", userIP='" + userIP + '\'' +
                ", matricula='" + matricula + '\'' +
                ", lotacao='" + lotacao + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }
}
