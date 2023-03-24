package br.gov.jfrj.siga.cp.arquivo;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import javax.net.ssl.SSLContext;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.jboss.logging.Logger;
import org.json.JSONObject;
import org.json.XML;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.CpArquivo;

public class ArmazenamentoHCP implements Armazenamento {

	private final static Logger log = Logger.getLogger(ArmazenamentoHCP.class);

	private static ArmazenamentoHCP instance;
	
	private static final String HCP = "HCP ";
	private static final String ERRO_AO_INSTANCIAR_ARMAZENAMENTO_HCP = "Erro ao instanciar ArmazenamentoHCP";
	private static final String ERRO_RECUPERAR_ARQUIVO = "Erro ao recuperar o arquivo";
	private static final String ERRO_GRAVAR_ARQUIVO = "Erro ao gravar o arquivo";
	private static final String ERRO_EXCLUIR_ARQUIVO = "Erro ao excluir o arquivo";
	private static final String ERRO_RECUPERAR_ESTATISTICAS = "Erro ao recuperar as estatísticas de armazenamento do HCP";
	private static final String END_POINT_NAMESPACE_STATISTICS = "proc/statistics";
	
	private static final String AUTHORIZATION = "Authorization";
	private CloseableHttpClient client;
	private String uri = Prop.get("/siga.armazenamento.arquivo.url");
	private String uriRest = uri + "rest/";
	private String usuario = Prop.get("/siga.armazenamento.arquivo.usuario");
	private String senha = Prop.get("/siga.armazenamento.arquivo.senha");
	private String token = null;

	
	private ArmazenamentoHCP() {}
	
	public static ArmazenamentoHCP getInstance() {
		try {
			if (instance == null) {
				synchronized (ArmazenamentoHCP.class) {
					instance = new ArmazenamentoHCP();
					instance.configurar();
				}
			}
			return instance;
		} catch (Exception e) {
			log.error(ERRO_AO_INSTANCIAR_ARMAZENAMENTO_HCP, e);
			throw new AplicacaoException(e.getMessage());
		}
	}

	private void configurar() throws Exception {
		gerarToken();
		TrustStrategy acceptingTrustStrategy = new TrustSelfSignedStrategy();
	    SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
	    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
	    client = HttpClients.custom().setSSLSocketFactory(csf).build();
	}

    @Override
    public void salvar(Long id, String caminho, String tipoDeConteudo, byte[] conteudo) {
		try {
			HttpPut request = new HttpPut(uriRest+caminho);
			request.addHeader(AUTHORIZATION, token);
			ByteArrayEntity requestEntity = new ByteArrayEntity(conteudo);
			request.setEntity(requestEntity);
			try(CloseableHttpResponse response = client.execute(request)){
				if(response.getStatusLine().getStatusCode()!=201)
					throw new Exception(response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			log.error(ERRO_GRAVAR_ARQUIVO, id, e);
			throw new AplicacaoException(ERRO_GRAVAR_ARQUIVO);
		}
	}
		
    @Override
    public void apagar(Long id, String caminho) {
		try {
			HttpDelete request = new HttpDelete(uriRest+caminho);
			request.addHeader(AUTHORIZATION, token);
			try(CloseableHttpResponse response = client.execute(request)){
				if(!(response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()==404))
					throw new Exception(response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			log.error(ERRO_EXCLUIR_ARQUIVO, id, e);
			throw new AplicacaoException(ERRO_EXCLUIR_ARQUIVO);
		}
	}
	
    @Override
    public byte[] recuperar(Long id, String caminho) {
		if(id == null || caminho == null)
			return null;
		try {
			HttpGet httpGet = new HttpGet(uriRest+caminho);
			httpGet.addHeader(AUTHORIZATION, token);
			try(CloseableHttpResponse response = client.execute(httpGet)){
				if (response.getStatusLine().getStatusCode() == 200 ) {
					BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());
					ByteArrayOutputStream bao = new ByteArrayOutputStream();
					byte[] buff = new byte[8000];
					int bytesRead = 0;
					while((bytesRead = bis.read(buff)) != -1) {
						bao.write(buff, 0, bytesRead);
					}
					return bao.toByteArray();
				} else {
					throw new Exception(response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
				}
			}
		} catch (Exception e) {
			log.error(ERRO_RECUPERAR_ARQUIVO, id, e);
			throw new AplicacaoException(ERRO_RECUPERAR_ARQUIVO);
		}
	}
	
	public JSONObject estatistica() {
		try {
			HttpGet httpGet = new HttpGet(uri+END_POINT_NAMESPACE_STATISTICS);
			httpGet.addHeader(AUTHORIZATION, token);
			try(CloseableHttpResponse response = client.execute(httpGet)){
				if (response.getStatusLine().getStatusCode() == 200 ) {
		           String XMLString = EntityUtils.toString(response.getEntity(), "UTF-8");
		           
		           JSONObject hcpStatJson = XML.toJSONObject(XMLString);

		           return hcpStatJson;

				} else {
					throw new Exception(response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
				}
			}
		} catch (Exception e) {
			throw new AplicacaoException(ERRO_RECUPERAR_ESTATISTICAS);
		}
	}

	private void gerarToken() {
		String usuarioBase64 = Base64.getEncoder().encodeToString(usuario.getBytes());
		String senhaMD5 = DigestUtils.md5Hex(senha.getBytes());
		token = HCP + usuarioBase64 + ":" + senhaMD5;
	}

}
