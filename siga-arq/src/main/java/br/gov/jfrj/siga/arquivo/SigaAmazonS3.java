package br.gov.jfrj.siga.arquivo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SigaAmazonS3 {
	
	private AmazonS3 s3Client;
	static private String bucketName;
	static long DEFAULT_TTL_TOKEN = 3600 * 30 * 100;
	static String NOME_PASTA_UPLOAD = "upload/"; 

	public String upload(InputStream is, String parmsJson) throws Exception {
		AmazonS3 s3Client = conectarS3();
		System.out.println(is.markSupported());
		BufferedInputStream arquivo = new BufferedInputStream(is);
		ObjectMapper objectMapper = new ObjectMapper();
		SigaAmazonS3ParametrosUpload parms = objectMapper.readValue(parmsJson, SigaAmazonS3ParametrosUpload.class);
		
		String msgerro = validaParms(parms, true, false);
		if (msgerro != null)
			throw new Exception(msgerro);

	    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss", Locale.ENGLISH);
	    String fdatetime = fmt.format(LocalDateTime.now());
	    
        parms.setArquivoNomeS3(NOME_PASTA_UPLOAD + fdatetime.replace(":", "-").replace("/", "") + "-" +
        		UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(parms.getArquivoNome()));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(Long.valueOf(parms.getTamanho()));

//        String hSha256 = calculaHashSha256(arquivo, Long.valueOf(parms.getTamanho()));
//        if (!parms.getHash().equals(hSha256))
//        	throw new Exception("O arquivo recebido para upload nao corresponde ao enviado.");
        
//        metadata.setHeader("x-amz-checksum-algorithm", "SHA256");
//        metadata.setHeader("x-amz-checksum-sha256", parms.getHash());
//        metadata.addUserMetadata("nomeArquivo", parms.getArquivoNome());
//        metadata.addUserMetadata("hashSha256", parms.getHash());
//        InputStreamHash arquivoHash = new InputStreamHash (arquivo);

        PutObjectResult res = s3Client.putObject(new PutObjectRequest(
                bucketName, 
                parms.getArquivoNomeS3(),
                arquivo,
                metadata));

//        String hs256 = new String(Hex.encodeHexString(bis.getSha256()));
        String tk = criarToken(bucketName, parms.getArquivoNome(), parms.getArquivoNomeS3(), 
        		Long.valueOf(parms.getTamanho()), parms.getHash());
        parms.setToken(tk);
        String resp = objectMapper.writeValueAsString(parms);
        return resp;
	}
	
	private String criarToken(String nomeBucket, String nomeArquivo, String nomeArquivoS3, Long tamanho, String hash) {
		final JWTSigner signer = new JWTSigner(System.getProperty("siga.jwt.secret"));
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		final long iat = System.currentTimeMillis() / 1000L;
		claims.put("iat", iat);
		claims.put("bucket", nomeBucket);
		claims.put("tamanho", tamanho.toString());
		claims.put("nomeArq", nomeArquivo);
		claims.put("nomeArqS3", nomeArquivoS3);
		claims.put("hash", hash);
		return signer.sign(claims);
	}

	public ResponseEntity<InputStreamResource> download(String tokenArquivo) throws Exception {
		AmazonS3 s3Client = conectarS3();
		
		final JWTVerifier verifier = new JWTVerifier(System.getProperty("siga.jwt.secret"));
 		Map<String, Object> lst = verifier.verify(tokenArquivo);
		String arquivoNomeS3 = (String) lst.get("nomeArqS3");
		String arquivoNome = (String) lst.get("nomeArq");
		S3Object obj;
		
        obj = s3Client.getObject(
                new GetObjectRequest(bucketName, arquivoNomeS3));

        S3ObjectInputStream responseStream = obj.getObjectContent();
        InputStreamResource dataFile = new InputStreamResource(responseStream);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Expose-Headers", "Content-Disposition");

        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(arquivoNome)
                .build();
        headers.setContentDisposition(contentDisposition);        

        String ctype = obj.getObjectMetadata().getContentType();
        if(ctype == null) {
            ctype = "application/octet-stream";
        }

        return ResponseEntity.status(HttpStatus.OK)
        		.headers(headers)
        		.contentType(MediaType.parseMediaType(ctype))
        		.body(dataFile);
	}
	
	public void remover(String tokenArquivo) throws Exception {
		AmazonS3 s3Client = conectarS3();
		
		final JWTVerifier verifier = new JWTVerifier(System.getProperty("siga.jwt.secret"));
 		Map<String, Object> lst = verifier.verify(tokenArquivo);
		String arquivoNomeS3 = (String) lst.get("nomeArqS3");
		
        s3Client.deleteObject(
                new DeleteObjectRequest(bucketName, arquivoNomeS3));
	}
	
	public static String calculaHashSha256(InputStream arq, Long tamanhoArq) throws NoSuchAlgorithmException, IOException {	
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
	    final int TAMANHO_BUFFER = 1048576;
		byte[] buffer = new byte[TAMANHO_BUFFER];
	    byte[] hash = null;
	    long offset = 0; 
	    int tam; 
	    while (offset < tamanhoArq) {
	        tam = (int) (((tamanhoArq - offset) >= TAMANHO_BUFFER) ? TAMANHO_BUFFER : (tamanhoArq - offset));
	        arq.read(buffer, 0, tam);
	        digest.update(buffer, 0, tam);
	        offset += tam;
	    }	
	    arq.close();
	    hash = new byte[digest.getDigestLength()];
	    hash = digest.digest();
		return new String(Hex.encodeHexString(hash));  
	}
	
	private String validaParms(SigaAmazonS3ParametrosUpload parms, boolean validaSequencia, boolean validaUploadId) {
		if (parms.getArquivoNome() == null || "".equals(parms.getArquivoNome()))
			return "Nome do arquivo não foi informado";
		if (parms.getArquivoNome().length() > 150)
			return "Nome do arquivo maior que 150 caracteres";
		return null;
	}

	private AmazonS3 conectarS3() {
		if (s3Client == null) {
			String accessKey = Base64.getEncoder().encodeToString(System.getProperty("siga.armazenamento.arquivo.usuario").getBytes());
			String secretKey = DigestUtils.md5Hex(System.getProperty("siga.armazenamento.arquivo.senha"));
	        System.setProperty("com.amazonaws.sdk.disableCertChecking", "true");
	        bucketName = System.getProperty("siga.armazenamento.arquivo.bucket");

	        s3Client = AmazonS3ClientBuilder
	        	    .standard()
	        	    .withCredentials(new AWSStaticCredentialsProvider(
	        	    		new BasicAWSCredentials(accessKey, secretKey)))
	        	    .withEndpointConfiguration(new EndpointConfiguration(
	        	    		System.getProperty("siga.armazenamento.arquivo.url").replace(bucketName + ".", ""),
	        	    		"us-west-1"))
	        	    .build();
		}
		return s3Client;
	}
}