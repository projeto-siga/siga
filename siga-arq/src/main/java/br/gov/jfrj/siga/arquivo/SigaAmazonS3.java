package br.gov.jfrj.siga.arquivo;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.jfrj.siga.idp.jwt.SigaJwtOptions;

public class SigaAmazonS3 {
	private AmazonS3 hs3Client;
	static private SigaJwtOptions options;
	static private String bucketName;
	static long DEFAULT_TTL_TOKEN = 3600 * 30 * 100;	
	
	/* 
	 * 	Upload de arquivos
	 */
	public String uploadMultipartIniciar(MultipartFile arquivo, String multipartUploadObj) throws Exception {
		AmazonS3 hs3Client = conectarS3();
		ObjectMapper objectMapper = new ObjectMapper();
		SigaAmazonS3MultipartUpload parms = objectMapper.readValue(multipartUploadObj, SigaAmazonS3MultipartUpload.class);
		
		String msgerro = validaParms(parms, true, false);
		if (msgerro != null)
			throw new Exception(msgerro);

	    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss", Locale.ENGLISH);
	    String fdatetime = fmt.format(LocalDateTime.now());
	    
        parms.setArquivoNomeS3("teste/" + fdatetime.replace(":", "").replace("/", "") + "-" +
        		parms.getArquivoNome());

        ObjectMetadata metadata = new ObjectMetadata();

        // Content-Length deve ser setado para utilizar um InputStream, senão o arquivo será
        // carregado em memória sem necessidade
        metadata.setContentLength(arquivo.getSize());
        String hashSHA256 = calcHashSha256(arquivo.getInputStream(), arquivo.getSize());
        if (!parms.getHash().equals(hashSHA256)) 
        	throw new Exception("O arquivo recebido para upload nao corresponde ao enviado.");

//      metadata.setHeader("x-amz-checksum-algorithm", "SHA256");
        String uId = null;
        
        InitiateMultipartUploadRequest req = new InitiateMultipartUploadRequest(bucketName, parms.getArquivoNomeS3());
        InitiateMultipartUploadResult res = hs3Client.initiateMultipartUpload(req);
        uId = res.getUploadId();
        metadata.setHeader("uploadId", uId);
        metadata.setHeader("Expires", LocalDateTime.now().plusDays(2)
        		.atZone(ZoneId.systemDefault()).toInstant());
        metadata.setHeader("partNumber", parms.getSequencia());
        
//        metadata.setHeader("Etag", parms.getHash());
//      metadata.setHeader("x-amz-checksum-algorithm", "SHA256");
//      metadata.setHeader("x-amz-checksum-sha256", parms.getHash());
//        metadata.setContentMD5(parms.getHash());
        int seq = Integer.parseInt(parms.getSequencia());
        long tam = arquivo.getSize();
        
        UploadPartRequest uploadRequest = new UploadPartRequest()
                .withBucketName(bucketName)
                .withKey(parms.getArquivoNomeS3())
                .withUploadId(res.getUploadId())
                .withPartNumber(seq)
                .withInputStream(arquivo.getInputStream())
                .withPartSize(tam);

        // Envia a parte do arquivo e devolve o response com o uploadId e a ETag dela
        // O uploadId deve ser enviado nos próximos requests para identificar qual é o processo de multipart upload
        // As sequencias (part) e as ETags devem ser armazenadas em um array no javascript cliente e serão 
        // passadas na finalização do multipart upload
        UploadPartResult uploadResult = hs3Client.uploadPart(uploadRequest);
        parms.setPartETag(uploadResult.getPartETag());
        parms.setUploadId(uId);
        String resp = objectMapper.writeValueAsString(parms);
        return resp;
	}

	public String uploadMultipartEnviarParte(MultipartFile arquivo, String multipartUploadObj) throws Exception {
		AmazonS3 hs3Client = conectarS3();
		ObjectMapper objectMapper = new ObjectMapper();
		SigaAmazonS3MultipartUpload parms = objectMapper.readValue(multipartUploadObj, SigaAmazonS3MultipartUpload.class);
		
		String msgerro = validaParms(parms, true, true);
		if (msgerro != null)
			throw new Exception(msgerro);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(arquivo.getSize());
//        metadata.setHeader("Etag", parms.getHash());
//        metadata.setHeader("x-amz-checksum-algorithm", "SHA256");
//        metadata.setHeader("x-amz-checksum-sha256", parms.getHash());
//        metadata.setContentMD5(parms.getHash());
        String hashSHA256 = calcHashSha256(arquivo.getInputStream(), arquivo.getSize());
        if (!parms.getHash().equals(hashSHA256)) 
        	throw new Exception("O arquivo recebido para upload nao corresponde ao enviado.");

        metadata.setHeader("partNumber", parms.getSequencia());
        int seq = Integer.parseInt(parms.getSequencia());
        long tam = arquivo.getSize();
        
        UploadPartRequest uploadRequest = new UploadPartRequest()
                .withBucketName(bucketName)
                .withKey(parms.getArquivoNomeS3())
                .withUploadId(parms.getUploadId())
                .withPartNumber(seq)
                .withInputStream(arquivo.getInputStream())
                .withPartSize(tam);

        UploadPartResult uploadResult = hs3Client.uploadPart(uploadRequest);
        parms.setPartETag(uploadResult.getPartETag());
        String resp = objectMapper.writeValueAsString(parms);
        return resp;
	}

	public String uploadMultipartFinalizar(String multipartUploadObj) throws Exception {
		AmazonS3 hs3Client = conectarS3();
		ObjectMapper objectMapper = new ObjectMapper();
		SigaAmazonS3MultipartUpload parms = objectMapper.readValue(multipartUploadObj, SigaAmazonS3MultipartUpload.class);
		
		String msgerro = validaParms(parms, true, true);
		if (msgerro != null)
			throw new Exception(msgerro);

	    if (parms.getPartETags() == null || "".equals(parms.getPartETags()))
        	throw new Exception("Parâmetro partETags inválido");
        
        List<PartETag> partETags = new ArrayList<PartETag>();
        for (Object pet : parms.getPartETags()) {
        	String p = pet.toString().replace("{partNumber=", "")
        			.replace(" etag=", "").replace("}", "");
        	PartETag petObj = new PartETag(Integer.parseInt(p.split(",")[0]), p.split(",")[1]); 
        	partETags.add(petObj);
        }
        partETags.sort(Comparator.comparing(PartETag :: getPartNumber));
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentMD5(parms.getHash());
        CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, parms.getArquivoNomeS3(),
	            parms.getUploadId(), partETags);
        CompleteMultipartUploadResult res = hs3Client.completeMultipartUpload(compRequest);
		return criarToken(res, parms.getTamanho());
	}

	public String uploadMultipartAbortar(String multipartUploadObj) throws Exception {
		AmazonS3 hs3Client = conectarS3();
		ObjectMapper objectMapper = new ObjectMapper();
		SigaAmazonS3MultipartUpload parms = objectMapper.readValue(multipartUploadObj, SigaAmazonS3MultipartUpload.class);
		
		String msgerro = validaParms(parms, false, false);
		if (msgerro != null)
			throw new Exception(msgerro);
		hs3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
	            bucketName, parms.getArquivoNomeS3(), parms.getUploadId()));
		return "OK";
	}
	/* 
	 * 	Download de Arquivos
	 */
	public ResponseEntity<InputStreamResource> download(String tokenArquivo, Optional<String> range) throws Exception {
		AmazonS3 hs3Client = conectarS3();
		
 		Map<String, Object> lst = decodificarTokenArquivo(tokenArquivo);
		String objName = (String) lst.get("key");
		S3Object obj;
 		String[] rng = null;
 		if (range.isPresent()) {
 			rng = range.orElseGet(() -> "").replace("bytes=","").split("-");
 	        obj = hs3Client.getObject(
 	                new GetObjectRequest(bucketName, objName)
 	                	.withRange(Long.valueOf(rng[0]), Long.valueOf(rng[1])));
		} else {
	        obj = hs3Client.getObject(
	                new GetObjectRequest(bucketName, objName));
		}

        S3ObjectInputStream responseStream = obj.getObjectContent();
        InputStreamResource dataFile = new InputStreamResource(responseStream);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Expose-Headers", "Content-Disposition");
        if (!range.isPresent())
        	headers.add("Content-Length", ((Long) obj.getObjectMetadata().getContentLength()).toString());
        
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + obj.getKey().split("/")[1] + "\"");
        String ctype = obj.getObjectMetadata().getContentType();
        if(ctype == null) {
            ctype = "application/octet-stream";
        }

        return ResponseEntity.status(range.isPresent() ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK)
        		.headers(headers)
        		.contentType(MediaType.parseMediaType(ctype))
        		.body(dataFile);
	}
	
	private String criarToken(CompleteMultipartUploadResult res, String tamanho) {
		final JWTSigner signer = new JWTSigner(System.getProperty("siga.jwt.secret"));
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		final long iat = System.currentTimeMillis() / 1000L;
		claims.put("iat", iat);
		claims.put("bucket", res.getBucketName());
		claims.put("tamanho", tamanho);
		claims.put("key", res.getKey());
        return signer.sign(claims);
	}

	private Map<String, Object> decodificarTokenArquivo(String token) throws InvalidKeyException, NoSuchAlgorithmException, 
			IllegalStateException, SignatureException, IOException, JWTVerifyException {
		final JWTVerifier verifier = new JWTVerifier(getOptions().getPassword());
		return verifier.verify(token);
	}

	public SigaJwtOptions getOptions() {
		if (options == null) {
			String password = System.getProperty("siga.jwt.secret");
			options = new SigaJwtOptions(password, DEFAULT_TTL_TOKEN, null);
		}
		return options;
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

	public static String calcHashSha256(InputStream arq, Long tamanhoArq) throws NoSuchAlgorithmException, IOException {	
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] buffer = new byte[tamanhoArq.intValue()];
        arq.read(buffer, 0, tamanhoArq.intValue());
	    arq.close();
	    byte[] hash = digest.digest(buffer);
		return new String(Hex.encodeHexString(hash));  
	}	

	private String validaParms(SigaAmazonS3MultipartUpload parms, boolean validaSequencia, boolean validaUploadId) {
		if (parms.getArquivoNome() == null || "".equals(parms.getArquivoNome()))
			return "Nome do arquivo não foi informado";
		if (parms.getArquivoNome().length() > 150)
			return "Nome do arquivo maior que 150 caracteres";
		if (validaSequencia) {
			if (parms.getSequencia() == null || "".equals(parms.getSequencia()))
				return "Sequencia não foi informada ou é inválida";
			Integer i;
			try {
		        i = Integer.parseInt(parms.getSequencia());
		    } catch (NumberFormatException nfe) {
		        return "Sequencia não é numérica";
		    }
			if (!(i > 0 && i <= 10000))
				return "Sequencia deve ser numérica de 1 a 10.000";
		}

		if (validaUploadId) {
	        if (parms.getUploadId() == null || "".equals(parms.getUploadId()))
	        	return "UploadId inválido";
		}
		return null;
	}

	private AmazonS3 conectarS3() {
		if (hs3Client == null) {
			String accessKey = Base64.getEncoder().encodeToString(System.getProperty("siga.armazenamento.arquivo.usuario").getBytes());
			String secretKey = DigestUtils.md5Hex(System.getProperty("siga.armazenamento.arquivo.senha"));
	        ClientConfiguration myClientConfig = new ClientConfiguration();
	        myClientConfig.setMaxConnections(200);
	        myClientConfig.setProtocol(Protocol.HTTP);
	        System.setProperty("com.amazonaws.sdk.disableCertChecking", "true");
	
	        hs3Client = new AmazonS3Client(
	                                     new BasicAWSCredentials(accessKey,
	                                           secretKey), myClientConfig);
	
	        hs3Client.setEndpoint(System.getProperty("siga.armazenamento.arquivo.url"));
	        bucketName = System.getProperty("siga.armazenamento.arquivo.bucket");
		}
		return hs3Client;
	}
}
