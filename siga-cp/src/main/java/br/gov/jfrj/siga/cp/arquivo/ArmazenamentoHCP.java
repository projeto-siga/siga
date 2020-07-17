package br.gov.jfrj.siga.cp.arquivo;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Calendar;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import br.gov.jfrj.siga.cp.CpArquivo;
import br.gov.jfrj.siga.cp.TipoConteudo;

public class ArmazenamentoHCP implements ArmazenamentoBCInterface {

	private static final String AUTHORIZATION = "Authorization";
	private CloseableHttpClient client;
	private String uri = "https://1118-spsempapel-documentos-digitais-desenv.999-prodesp.hcp.prodesp-dc00.sp.gov.br/rest/";
	String username = "usr_sempapel_doc_digitais_desenv";
	String password = "Pu7*95%_af";
	String token = null;

	@PostConstruct
	private void init() {
		gerarToken();
		client = HttpClients.createDefault();
	}

	@Override
	public void salvar(CpArquivo cpArquivo, byte[] conteudo) {
		try {
			init();
			if(cpArquivo.getCaminho()==null)
				cpArquivo.setCaminho(gerarCaminho(cpArquivo));
			else
				apagar(cpArquivo);
			//TODO: K implementar um controle transacional
			HttpPut request = new HttpPut(uri+cpArquivo.getCaminho());
			request.addHeader(AUTHORIZATION, token);
			ByteArrayEntity requestEntity = new ByteArrayEntity(conteudo);
			request.setEntity(requestEntity);
			HttpResponse response = client.execute(request);
			System.out.println("Response Code : " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase()); 
		} catch (Exception e) {
			//TODO: IMPLEMENTAR LOG
			e.printStackTrace();
		}
	}

	@Override
	public void apagar(CpArquivo cpArquivo) {
		try {
			init();
			HttpDelete request = new HttpDelete(uri+cpArquivo.getCaminho());
			request.addHeader(AUTHORIZATION, token);
			HttpResponse response = client.execute(request);
			System.out.println("Response Code : " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase()); 
		} catch (Exception e) {
			//TODO: IMPLEMENTAR LOG
			e.printStackTrace();
		}
	}
	
	@Override
	public byte[] recuperar(CpArquivo cpArquivo) {
		try {
			init();
			HttpGet httpGet = new HttpGet(uri+cpArquivo.getCaminho());
			httpGet.addHeader(AUTHORIZATION, token);
			CloseableHttpResponse response = client.execute(httpGet);
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
				System.out.println("Response Code : " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			//TODO: IMPLEMENTAR LOG
			e.printStackTrace();
		}
		return null;
	}

	private void gerarToken() {
		String usuarioBase64 = Base64.getEncoder().encodeToString(username.getBytes());
		String senhaMD5 = DigestUtils.md5Hex(password.getBytes());
		token = "HCP " + usuarioBase64 + ":" + senhaMD5;
	}

	private String gerarCaminho(CpArquivo cpArquivo) {
		String extensao;
		
		if(TipoConteudo.ZIP.getMimeType().equals(cpArquivo.getConteudoTpArq()))
			extensao = TipoConteudo.ZIP.getExtensao();
		else
			extensao = TipoConteudo.ZIP.getExtensao();
		
		Calendar c = Calendar.getInstance();
		String caminho = c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DATE)+"/"+c.get(Calendar.HOUR)+"/"+c.get(Calendar.MINUTE)+"/"+UUID.randomUUID().toString()+"."+extensao;
		return caminho;
	}

}
