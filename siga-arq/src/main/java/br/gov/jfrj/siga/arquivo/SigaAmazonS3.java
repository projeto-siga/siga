package br.gov.jfrj.siga.arquivo;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

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

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.model.enm.CpExtensoesDeArquivoEnum;

public class SigaAmazonS3 {
	
	private AmazonS3 s3Client;
	static private String bucketName;
	static long DEFAULT_TTL_TOKEN = 3600 * 30 * 100;
	static String NOME_PASTA_UPLOAD = "upload/"; 

	public String upload(InputStream arquivo, String parmsJson) throws Exception {
		AmazonS3 s3Client = conectarS3();
		ObjectMapper objectMapper = new ObjectMapper();
		SigaAmazonS3ParametrosUpload parms = objectMapper.readValue(parmsJson, SigaAmazonS3ParametrosUpload.class);
		
		String msgerro = validaParms(parms, true, false);
		if (msgerro != null)
			throw new Exception(msgerro);

	    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd/hh/mm/ss", Locale.ENGLISH);
	    String fdatetime = fmt.format(LocalDateTime.now());
	    
        parms.setArquivoNomeS3(NOME_PASTA_UPLOAD + fdatetime + "/" +
        		UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(parms.getArquivoNome()));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(parms.getTamanhoLong());

    	metadata.setHeader("x-amz-checksum-mode", "enabled");
    	metadata.setHeader("x-amz-checksum-algorithm", "SHA256");
    	metadata.setHeader("x-amz-checksum-sha256", parms.getHash());
        
        metadata.setContentDisposition(parms.getArquivoNome());
        metadata.addUserMetadata("nomeArquivo", parms.getArquivoNome());
        metadata.addUserMetadata("hashSha256", parms.getHash());

        PutObjectRequest req = new PutObjectRequest(
                bucketName, 
                parms.getArquivoNomeS3(),
                arquivo,
                metadata);
        
        PutObjectResult res = s3Client.putObject(req);
        
        String tk = criarToken(bucketName, parms.getArquivoNome(), parms.getArquivoNomeS3(), 
        		Long.valueOf(parms.getTamanho()), parms.getHash());
        
        parms.setToken(tk);
        String resp = objectMapper.writeValueAsString(parms);
        return resp;
	}
	
	private String criarToken(String nomeBucket, String nomeArquivo, String nomeArquivoS3, Long tamanho, String hash) {
		final JWTSigner signer = new JWTSigner(Prop.get("/siga.jwt.secret"));
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
		
		final JWTVerifier verifier = new JWTVerifier(Prop.get("/siga.jwt.secret"));
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
		
		final JWTVerifier verifier = new JWTVerifier(Prop.get("/siga.jwt.secret"));
 		Map<String, Object> lst = verifier.verify(tokenArquivo);
		String arquivoNomeS3 = (String) lst.get("nomeArqS3");
		
        s3Client.deleteObject(
                new DeleteObjectRequest(bucketName, arquivoNomeS3));
	}
	
	private String validaParms(SigaAmazonS3ParametrosUpload parms, boolean validaSequencia, boolean validaUploadId) {
		if (parms.getArquivoNome() == null || "".equals(parms.getArquivoNome()))
			return "Nome do arquivo não foi informado";
		if (parms.getArquivoNome().length() > 150)
			return "Nome do arquivo maior que 150 caracteres";
		String extensaoArq = FilenameUtils.getExtension(parms.getArquivoNome());
		if (!CpExtensoesDeArquivoEnum.getList().contains(extensaoArq))
			return "Extensão de arquivo não permitida para upload: ." + extensaoArq;
		return null;
	}

	private AmazonS3 conectarS3() {
		if (s3Client == null) {
			String accessKey = Base64.getEncoder().encodeToString(Prop.get("armazenamento.arquivo.formatolivre.usuario").getBytes());
			String secretKey = DigestUtils.md5Hex(Prop.get("armazenamento.arquivo.formatolivre.senha"));
	        System.setProperty("com.amazonaws.sdk.disableCertChecking", "true");
	        bucketName = Prop.get("armazenamento.arquivo.formatolivre.bucket");

	        s3Client = AmazonS3ClientBuilder
	        	    .standard()
	        	    .withCredentials(new AWSStaticCredentialsProvider(
	        	    		new BasicAWSCredentials(accessKey, secretKey)))
	        	    .withEndpointConfiguration(new EndpointConfiguration(
	        	    		Prop.get("/siga.armazenamento.arquivo.formatolivre.url").replace(bucketName + ".", ""),
	        	    		"us-west-1"))
	        	    .build();
		}
		return s3Client;
	}

	public AmazonS3 conectar() throws Exception {
		return s3Client = conectarS3();
	}
}