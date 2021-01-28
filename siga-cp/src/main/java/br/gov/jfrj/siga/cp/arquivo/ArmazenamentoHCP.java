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
import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.CpArquivo;

public class ArmazenamentoHCP {

	private final static Logger log = Logger.getLogger(ArmazenamentoHCP.class);

	private static ArmazenamentoHCP instance;
	
	private static final String HCP = "HCP ";
	private static final String ERRO_AO_INSTANCIAR_ARMAZENAMENTO_HCP = "Erro ao instanciar ArmazenamentoHCP";
	private static final String ERRO_RECUPERAR_ARQUIVO = "Erro ao recuperar o arquivo";
	private static final String ERRO_GRAVAR_ARQUIVO = "Erro ao gravar o arquivo";
	private static final String ERRO_EXCLUIR_ARQUIVO = "Erro ao excluir o arquivo";
	
	private static final String AUTHORIZATION = "Authorization";
	private CloseableHttpClient client;
	private String uri = Prop.get("/siga.armazenamento.arquivo.url");
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

	public void salvar(CpArquivo cpArquivo, byte[] conteudo) {
		try {
			HttpPut request = new HttpPut(uri+cpArquivo.getCaminho());
			request.addHeader(AUTHORIZATION, token);
			ByteArrayEntity requestEntity = new ByteArrayEntity(conteudo);
			request.setEntity(requestEntity);
			try(CloseableHttpResponse response = client.execute(request)){
				if(response.getStatusLine().getStatusCode()!=201)
					throw new Exception(response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			log.error(ERRO_GRAVAR_ARQUIVO, cpArquivo.getIdArq(), e);
			throw new AplicacaoException(ERRO_GRAVAR_ARQUIVO);
		}
	}
		
	public void apagar(CpArquivo cpArquivo) {
		try {
			HttpDelete request = new HttpDelete(uri+cpArquivo.getCaminho());
			request.addHeader(AUTHORIZATION, token);
			try(CloseableHttpResponse response = client.execute(request)){
				if(!(response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()==404))
					throw new Exception(response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			log.error(ERRO_EXCLUIR_ARQUIVO, cpArquivo.getIdArq(), e);
			throw new AplicacaoException(ERRO_EXCLUIR_ARQUIVO);
		}
	}
	
	public byte[] recuperar(CpArquivo cpArquivo) {
		if(cpArquivo.getIdArq() == null || cpArquivo.getCaminho() == null)
			return null;
		try {
			HttpGet httpGet = new HttpGet(uri+cpArquivo.getCaminho());
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
			log.error(ERRO_RECUPERAR_ARQUIVO, cpArquivo.getIdArq(), e);
			throw new AplicacaoException(ERRO_RECUPERAR_ARQUIVO);
		}
	}

	private void gerarToken() {
		String usuarioBase64 = Base64.getEncoder().encodeToString(usuario.getBytes());
		String senhaMD5 = DigestUtils.md5Hex(senha.getBytes());
		token = HCP + usuarioBase64 + ":" + senhaMD5;
	}

}
