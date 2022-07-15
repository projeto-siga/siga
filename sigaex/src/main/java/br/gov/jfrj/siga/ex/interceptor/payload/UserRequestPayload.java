package br.gov.jfrj.siga.ex.interceptor.payload;

public class UserRequestPayload {
    private String requestURL;

    private String requestParams;
    private String matricula;
    private String lotacao;
    private String nome;
    private String dataHora;
    private String ipAudit;

    public UserRequestPayload(String requestURL,
                              String requestParams,
                              String matricula,
                              String lotacao,
                              String nome,
                              String dataHora,
                              String ipAudit) {
        
        this.requestURL = requestURL;
        this.requestParams = requestParams;
        this.matricula = matricula;
        this.lotacao = lotacao;
        this.nome = nome;
        this.dataHora = dataHora;
        this.ipAudit = ipAudit;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getRequestParams() {
        return requestParams;
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

    public String getDataHora() {
        return dataHora;
    }

    public String getIpAudit() {
        return ipAudit;
    }

    @Override
    public String toString() {
        return "UserRequestPayload{" +
                "requestURL='" + requestURL + '\'' +
                ", requestParams='" + requestParams + '\'' +
                ", matricula='" + matricula + '\'' +
                ", lotacao='" + lotacao + '\'' +
                ", nome='" + nome + '\'' +
                ", dataHora='" + dataHora + '\'' +
                ", ipAudit='" + ipAudit + '\'' +
                '}';
    }
}
