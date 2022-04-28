package br.gov.jfrj.siga.integracao.api.v1;

import com.crivano.swaggerservlet.ISwaggerMethod;
import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerResponse;

public interface IIntegracaoApiV1 {
    public interface IEnviarSiafemSiglaPost extends ISwaggerMethod {
        public static class Request implements ISwaggerRequest {
            public String sigla;
            public String usuarioSiafem;
            public String senhaSiafem;
        }

        public static class Response implements ISwaggerResponse {
            public String sigla;
            public String status;
        }

        public void run(Request req, Response resp, IntegracaoApiV1Context ctx) throws Exception;
    }
}
