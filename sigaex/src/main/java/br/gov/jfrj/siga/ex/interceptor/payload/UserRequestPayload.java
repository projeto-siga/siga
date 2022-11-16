package br.gov.jfrj.siga.ex.interceptor.payload;

import br.gov.jfrj.siga.base.HttpRequestUtils;
import br.gov.jfrj.siga.dp.DpPessoa;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.util.UuidUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class UserRequestPayload {

    public static final String REQUEST_ID = "requestId";
    public static final String SESSION_ID = "sessionId";
    public static final String USER_IP_ADDRESS = "userIpAddress";

    public static final String NOME_ACAO = "nomeAcao";
    public static final String REQUEST_URL = "requestURL";
    public static final String REQUEST_PARAMS = "requestParams";

    public static final String MATRICULA = "matricula";
    public static final String LOTACAO = "lotacao";
    public static final String ORGAO = "orgao";

    public static final String NOME_PESSOA = "nomePessoa";
    public static final String SIGLA = "sigla";

    private final HttpServletRequest request;

    private final String nomeAcao;
    private final String requestURL;
    private final String requestParams;
    private final String userIpAddress;
    private final DpPessoa cadastrante;
    private final String matricula;
    private final String lotacao;

    private final String orgao;
    private final String nomePessoa;

    private final String sigla;


    public UserRequestPayload(HttpServletRequest request, String sigla, String nomeAcao, DpPessoa cadastrante) {
        this.request = request;
        this.requestURL = request.getRequestURL().toString();
        this.requestParams = request.getQueryString();

        String sessionId = request.getRequestedSessionId();
        this.userIpAddress = HttpRequestUtils.getIpAudit(request);

        this.cadastrante = cadastrante;
        this.matricula = cadastrante.getMatricula().toString();
        this.lotacao = cadastrante.getLotacao().toString();
        this.orgao = cadastrante.getOrgaoUsuario().getNmOrgaoUsu();
        this.nomePessoa = cadastrante.getNomePessoa();
        this.sigla = sigla;
        this.nomeAcao = nomeAcao;

        String uuidStr = ThreadContext.get(REQUEST_ID);
        if (uuidStr == null) {
            UUID requestId = UuidUtil.getTimeBasedUuid();
            ThreadContext.put(REQUEST_ID, requestId.toString());
        }

        if (sessionId != null) {
            ThreadContext.put(SESSION_ID, sessionId);
        }
        if (userIpAddress != null) {
            ThreadContext.put(USER_IP_ADDRESS, userIpAddress);
        }
        ThreadContext.put(NOME_ACAO, nomeAcao);
        ThreadContext.put(REQUEST_URL, requestURL);
        if (requestParams != null) {
            ThreadContext.put(REQUEST_PARAMS, requestParams);
        }

        ThreadContext.put(MATRICULA, matricula);
        if (lotacao != null) {
            ThreadContext.put(LOTACAO, lotacao);
        }
        if(orgao != null){
            ThreadContext.put(ORGAO, orgao);
        }
        if (nomePessoa != null) {
            ThreadContext.put(NOME_PESSOA, nomePessoa);
        }
        if (sigla != null) {
            ThreadContext.put(SIGLA, sigla);
        }

    }

    public static void clear() {
        ThreadContext.clearMap();
    }

    public static String getRequestId() {
        String uuidStr = ThreadContext.get(REQUEST_ID);
        UUID uuid;
        if (uuidStr == null) {
            uuid = UuidUtil.getTimeBasedUuid();
            ThreadContext.put(REQUEST_ID, uuid.toString());
        }
        return uuidStr;
    }

    public static String getSessionId() {
        return ThreadContext.get(SESSION_ID);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public DpPessoa getCadastrante() {
        return cadastrante;
    }

    public String getNomeAcao() {
        return nomeAcao;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public String getUserIpAddress() {
        return userIpAddress;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getLotacao() {
        return lotacao;
    }

    public String getOrgao() {
        return orgao;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public String getSigla() {
        return sigla;
    }

    @Override
    public String toString() {
        return this.nomeAcao;
    }
}
