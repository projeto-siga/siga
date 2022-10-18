package br.gov.jfrj.siga.cp.arquivo;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.CpArquivo;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;

public class ArmazenamentoS3 {

	private final static Logger log = Logger.getLogger(ArmazenamentoS3.class);

	private static ArmazenamentoS3 instance;
	
	private static final String ERRO_AO_INSTANCIAR_ARMAZENAMENTO_S3 = "Erro ao instanciar ArmazenamentoS3";
	private static final String ERRO_RECUPERAR_ARQUIVO = "Erro ao recuperar o arquivo";
	private static final String ERRO_GRAVAR_ARQUIVO = "Erro ao gravar o arquivo";
	private static final String ERRO_EXCLUIR_ARQUIVO = "Erro ao excluir o arquivo";
	private static final String ERRO_BUCKET_INEXISTENTE_S3 = "Bucket %s não existe";
	
	//private CloseableHttpClient client;
	private MinioClient client;
	private String uri = Prop.get("/siga.armazenamento.arquivo.url");
	private String usuario = Prop.get("/siga.armazenamento.arquivo.usuario");
	private String senha = Prop.get("/siga.armazenamento.arquivo.senha");
	private String bucket = Prop.get("/siga.armazenamento.arquivo.bucket");
	private ArmazenamentoS3() {}
	
	public static ArmazenamentoS3 getInstance() {
		try {
			if (instance == null) {
				synchronized (ArmazenamentoS3.class) {
					instance = new ArmazenamentoS3();
					instance.configurar();
				}
			}
			return instance;
		} catch (Exception e) {
			log.error(ERRO_AO_INSTANCIAR_ARMAZENAMENTO_S3, e);
			throw new AplicacaoException(e.getMessage());
		}
	}

	private void configurar() throws Exception {
		client = MinioClient.builder()
	              .endpoint(uri)
	              .credentials(usuario, senha)
	              .build();
		boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());

		if (!found) {
			String msg = String.format(ERRO_BUCKET_INEXISTENTE_S3,bucket);
			log.error(msg);
			throw new AplicacaoException(msg);
		}
		
	}

	public void salvar(CpArquivo cpArquivo, byte[] conteudo) {
		try {
			client.putObject(
					PutObjectArgs.builder()
					.bucket(bucket)
					.object(cpArquivo.getCaminho())
					.stream(new ByteArrayInputStream(conteudo), -1, 10485760L)
					.contentType(cpArquivo.getConteudoTpArq())
					.build());
			
		} catch (Exception e) {
			log.error(ERRO_GRAVAR_ARQUIVO, cpArquivo.getIdArq(), e);
			throw new AplicacaoException(ERRO_GRAVAR_ARQUIVO);
		}
	}
		
	public void apagar(CpArquivo cpArquivo) {
		try {
			client.removeObject(
				    RemoveObjectArgs.builder().bucket(bucket).object(cpArquivo.getCaminho()).build());
		} catch (Exception e) {
			log.error(ERRO_EXCLUIR_ARQUIVO, cpArquivo.getIdArq(), e);
			throw new AplicacaoException(ERRO_EXCLUIR_ARQUIVO);
		}
	}
	
	public byte[] recuperar(CpArquivo cpArquivo) {
		if(cpArquivo.getIdArq() == null || cpArquivo.getCaminho() == null)
			return null;
		try (InputStream stream = client.getObject(
				  GetObjectArgs.builder()
				  .bucket(bucket)
				  .object(cpArquivo.getCaminho())
				  .build())) {
					BufferedInputStream bis = new BufferedInputStream(stream);
					ByteArrayOutputStream bao = new ByteArrayOutputStream();
					byte[] buff = new byte[8000];
					int bytesRead = 0;
					while((bytesRead = bis.read(buff)) != -1) {
						bao.write(buff, 0, bytesRead);
					}
					
					return bao.toByteArray();
				
		} catch (Exception e) {
			log.error(ERRO_RECUPERAR_ARQUIVO, cpArquivo.getIdArq(), e);
			throw new AplicacaoException(ERRO_RECUPERAR_ARQUIVO);
		}
	}

}
