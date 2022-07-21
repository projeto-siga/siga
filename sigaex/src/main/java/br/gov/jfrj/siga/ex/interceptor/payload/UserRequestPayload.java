package br.gov.jfrj.siga.ex.interceptor.payload;

import br.gov.jfrj.siga.base.HttpRequestUtils;
import br.gov.jfrj.siga.dp.DpPessoa;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.util.UuidUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class UserRequestPayload {

    public static final String REQUEST_ID = "requestId";
    public static final String SESSION_ID = "sessionId";
    public static final String USER_IP_ADDRESS = "userIpAddress";
    public static final String REQUEST_URL = "requestURL";
    public static final String REQUEST_PARAMS = "requestParams";

    public static final String MATRICULA = "matricula";
    public static final String LOTACAO = "lotacao";
    public static final String NOME = "nome";

    private UUID requestId;
    private HttpServletRequest request;
    private String requestURL;
    private String requestParams;
    private String sessionId;
    private String userIpAddress;

    private DpPessoa cadastrante;
    private String matricula;
    private String lotacao;
    private String nome;


    public UserRequestPayload(HttpServletRequest request, DpPessoa cadastrante) {
        this.request = request;
        this.requestURL = request.getRequestURL().toString();
        this.requestParams = request.getQueryString();
        this.sessionId = request.getRequestedSessionId();
        this.userIpAddress = HttpRequestUtils.getIpAudit(request);

        this.cadastrante = cadastrante;
        this.matricula = cadastrante.getMatricula().toString();
        this.lotacao = cadastrante.getLotacao().toString();
        this.nome = cadastrante.getNomePessoa();

        String uuidStr = ThreadContext.get(REQUEST_ID);
        if (uuidStr == null) {
            requestId = UuidUtil.getTimeBasedUuid();
            ThreadContext.put(REQUEST_ID, requestId.toString());
        }
        
        if (sessionId != null) {
            ThreadContext.put(SESSION_ID, sessionId);
        }
        if (userIpAddress != null) {
            ThreadContext.put(USER_IP_ADDRESS, userIpAddress);
        }
        if (requestURL != null) {
            ThreadContext.put(REQUEST_URL, requestURL);
        }
        if (requestParams != null) {
            ThreadContext.put(REQUEST_PARAMS, requestParams);
        }
        
        if (matricula != null) {
            ThreadContext.put(MATRICULA, matricula);
        }
        if (lotacao != null) {
            ThreadContext.put(LOTACAO, lotacao);
        }
        if (nome != null) {
            ThreadContext.put(NOME, nome);
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

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(request.getRequestURI());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
