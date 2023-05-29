package br.gov.jfrj.siga.cp.arquivo;

import java.io.InputStream;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaHTTP;
import br.gov.jfrj.siga.cp.CpArquivo;

public class ArmazenamentoS3REST implements Armazenamento {

    private final static Logger log = Logger.getLogger(ArmazenamentoS3REST.class);

    private static ArmazenamentoS3REST instance;

    private static final String ERRO_AO_INSTANCIAR_ARMAZENAMENTO_S3 = "Erro ao instanciar ArmazenamentoS3";
    private static final String ERRO_RECUPERAR_ARQUIVO = "Erro ao recuperar o arquivo";
    private static final String ERRO_GRAVAR_ARQUIVO = "Erro ao gravar o arquivo";
    private static final String ERRO_EXCLUIR_ARQUIVO = "Erro ao excluir o arquivo";
    private static final String ERRO_BUCKET_INEXISTENTE_S3 = "Bucket %s não existe";

    // private CloseableHttpClient client;
    private String uri;
    private String usuario;
    private String senha;
    private String bucket;
    private String awsRegion;
    private String awsService;

    private ArmazenamentoS3REST() {
        uri = Prop.get("/siga.armazenamento.arquivo.url");
        usuario = Prop.get("/siga.armazenamento.arquivo.usuario");
        senha = Prop.get("/siga.armazenamento.arquivo.senha");
        bucket = Prop.get("/siga.armazenamento.arquivo.bucket");
        awsRegion = "br";
        awsService = "s3";
    }

    public static ArmazenamentoS3REST getInstance() {
        try {
            if (instance == null) {
                synchronized (ArmazenamentoS3.class) {
                    instance = new ArmazenamentoS3REST();
                }
            }
            return instance;
        } catch (Exception e) {
            log.error(ERRO_AO_INSTANCIAR_ARMAZENAMENTO_S3, e);
            throw new AplicacaoException(e.getMessage());
        }
    }

    private byte[] fetch(String method, String bucket, String path, String contentType, byte[] body)
            throws RuntimeException {
        String bucketAndPath = "/" + bucket + "/" + path;
        String url = uri + bucketAndPath;
        String query = null;
        String awsIdentity = usuario;
        String awsSecret = senha;

        // optional headers
        Map<String, String> headers = new HashMap<>();

        try {
            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneOffset.UTC);
            String dateHeader = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'UTC'").format(zonedDateTime);
            headers.put("Date", dateHeader);
            String isoDateTime = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'").format(zonedDateTime);

            // this is a linked hashmap and the headers must be in a specific order
            Map<String, String> requestHeaders = AmazonRequestSignatureV4Utils.calculateAuthorizationHeaders(method,
                    uri.replace("http://", "").replace("https://", ""),
                    bucketAndPath, query, headers,
                    body, isoDateTime, awsIdentity, awsSecret, awsRegion, awsService);

            UnsafeSSLHelper unsafeSSLHelper = new UnsafeSSLHelper();

            try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setSslcontext(unsafeSSLHelper.createUnsecureSSLContext())
                    .setHostnameVerifier(unsafeSSLHelper.getPassiveX509HostnameVerifier()).build();) {
                HttpRequestBase request;
                switch (method) {
                    case "GET":
                        request = new HttpGet(url);
                        break;
                    case "POST":
                        request = new HttpPost(url);
                        if (body != null)
                            ((HttpPost) request).setEntity(new ByteArrayEntity(body));
                        break;
                    case "PUT":
                        request = new HttpPut(url);
                        if (body != null)
                            ((HttpPut) request).setEntity(new ByteArrayEntity(body));
                        break;
                    case "DELETE":
                        request = new HttpDelete(url);
                        break;
                    default:
                        throw new RuntimeException("Método desconhecido: " + method);
                }
                if (contentType != null)
                    request.addHeader("Content-Type", contentType);

                List<Header> finalHeaders = new ArrayList<>(requestHeaders.size());
                // these must be in a specific order
                for (Entry<String, String> entry : requestHeaders.entrySet()) {
                    finalHeaders.add(new BasicHeader(entry.getKey(), entry.getValue()));
                }
                // this overrides any previous headers specified
                request.setHeaders(finalHeaders.toArray(new Header[requestHeaders.size()]));

                try (CloseableHttpResponse response = httpClient.execute(request);) {
                    if (response.getStatusLine().getStatusCode() >= 200
                            && response.getStatusLine().getStatusCode() < 300) {
                        HttpEntity entity = response.getEntity();
                        if (entity == null)
                            return null;
                        InputStream content = entity.getContent();
                        if (content == null)
                            return null;
                        return SigaHTTP.convertStreamToByteArray(content, 8192);
                    }
                    throw new RuntimeException("Erro de storage " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void salvar(String caminho, String tipoDeConteudo, byte[] conteudo) {
        // log.info("*** salvar: " + caminho);
        try {
            fetch("PUT", bucket, caminho, tipoDeConteudo, conteudo);
        } catch (Exception e) {
            log.error(ERRO_GRAVAR_ARQUIVO, caminho, e);
            throw new AplicacaoException(ERRO_GRAVAR_ARQUIVO, 0, e);
        }
    }

    @Override
    public void apagar(String caminho) {
        if (!obterTemporalidadePorCaminho(caminho).podeApagar)
            return;
        // log.info("*** apagar: " + caminho);
        try {
            fetch("DELETE", bucket, caminho, null, null);
        } catch (Exception e) {
            log.error(ERRO_EXCLUIR_ARQUIVO, caminho, e);
            throw new AplicacaoException(ERRO_EXCLUIR_ARQUIVO, 0, e);
        }
    }

    @Override
    public byte[] recuperar(String caminho) {
        // log.info("*** recuperar: " + caminho);
        if (caminho == null)
            return null;
        try {
            return fetch("GET", bucket, caminho, null, null);
        } catch (Exception e) {
            log.error(ERRO_RECUPERAR_ARQUIVO, caminho, e);
            throw new AplicacaoException(ERRO_GRAVAR_ARQUIVO, 0, e);
        }

    }

}
