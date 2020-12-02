package br.gov.jfrj.siga.api.v1;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.crivano.swaggerservlet.ISwaggerMethod;
import com.crivano.swaggerservlet.ISwaggerModel;
import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerResponse;
import com.crivano.swaggerservlet.ISwaggerResponseFile;

public interface ISigaApiV1 {
	public class Status implements ISwaggerModel {
		public String mensagem;
		public Double indice;
		public Double contador;
		public Double bytes;
		public String errormsg;
		public String stacktrace;
	}

	public class Error implements ISwaggerModel {
		public String errormsg;
	}

	public class Pessoa implements ISwaggerModel {
		public String idPessoa;
		public String idPessoaIni;
		public String sigla;
		public String nome;
		public String siglaLotacao;
		public Boolean isExternaPessoa;
		public Lotacao lotacao;
		public Cargo cargo;
		public FuncaoConfianca funcaoConfianca;
	}

	public class Orgao implements ISwaggerModel {
		public String idOrgao;
		public String sigla;
		public String nome;
	}

	public class Lotacao implements ISwaggerModel {
		public String idLotacao;
		public String idLotacaoIni;
		public String sigla;
		public String siglaLotacao;
		public String nome;
		public Orgao orgao;
	}

	public class LotacaoAtual implements ISwaggerModel {
		public String idLotacao;
		public String idLotacaoIni;
		public String sigla;
		public String nome;
		public String orgao;
	}

	public class Cargo implements ISwaggerModel {
		public String idCargo;
		public String idExterna;
		public String sigla;
		public String nome;
	}

	public class FuncaoConfianca implements ISwaggerModel {
		public String idFuncaoConfianca;
		public String idExterna;
		public String sigla;
		public String nome;
		public String idpai;
	}

	public class AutenticarPostRequest implements ISwaggerRequest {
	}

	public class AutenticarPostResponse implements ISwaggerResponse {
		public String token;
	}

	public interface IAutenticarPost extends ISwaggerMethod {
		public void run(AutenticarPostRequest req, AutenticarPostResponse resp) throws Exception;
	}

	public class DocumentosSiglaArquivoGetRequest implements ISwaggerRequest {
		public String contenttype;
		public String sigla;
		public Boolean estampa;
		public Boolean completo;
		public Boolean volumes;
	}

	public class DocumentosSiglaArquivoGetResponse implements ISwaggerResponse {
		public String uuid;
		public String jwt;
	}

	public interface IDocumentosSiglaArquivoGet extends ISwaggerMethod {
		public void run(DocumentosSiglaArquivoGetRequest req, DocumentosSiglaArquivoGetResponse resp) throws Exception;
	}

	public class DownloadJwtFilenameGetRequest implements ISwaggerRequest {
		public String jwt;
		public String filename;
		public String disposition;
	}

	public class DownloadJwtFilenameGetResponse implements ISwaggerResponse, ISwaggerResponseFile {
		public String contenttype = "application/pdf";
		public String contentdisposition = "attachment";
		public Long contentlength;
		public InputStream inputstream;
		public Map<String, List<String>> headerFields;

		public String getContenttype() {
			return contenttype;
		}
		public void setContenttype(String contenttype) {
			this.contenttype = contenttype;
		}
		public String getContentdisposition() {
			return contentdisposition;
		}
		public void setContentdisposition(String contentdisposition) {
			this.contentdisposition = contentdisposition;
		}
		public Long getContentlength() {
			return contentlength;
		}
		public void setContentlength(Long contentlength) {
			this.contentlength = contentlength;
		}
		public InputStream getInputstream() {
			return inputstream;
		}
		public void setInputstream(InputStream inputstream) {
			this.inputstream = inputstream;
		}
		public Map<String, List<String>> getHeaderFields() {
			return headerFields;
		}
		public void setHeaderFields(Map<String, List<String>> headerFields) {
			this.headerFields = headerFields;
		}
	}

	public interface IDownloadJwtFilenameGet extends ISwaggerMethod {
		public void run(DownloadJwtFilenameGetRequest req, DownloadJwtFilenameGetResponse resp) throws Exception;
	}

	public class StatusChaveGetRequest implements ISwaggerRequest {
		public String chave;
	}

	public class StatusChaveGetResponse implements ISwaggerResponse {
		public String mensagem;
		public Double indice;
		public Double contador;
		public Double bytes;
		public String errormsg;
		public String stacktrace;
	}

	public interface IStatusChaveGet extends ISwaggerMethod {
		public void run(StatusChaveGetRequest req, StatusChaveGetResponse resp) throws Exception;
	}

	public class PessoasGetRequest implements ISwaggerRequest {
		public String idPessoaIni;
		public String texto;
		public String cpf;
	}

	public class PessoasGetResponse implements ISwaggerResponse {
		public List<Pessoa> list;
	}

	public interface IPessoasGet extends ISwaggerMethod {
		public void run(PessoasGetRequest req, PessoasGetResponse resp) throws Exception;
	}

	public class LotacoesGetRequest implements ISwaggerRequest {
		public String texto;
		public String idLotacaoIni;
	}

	public class LotacoesGetResponse implements ISwaggerResponse {
		public List<Lotacao> list;
	}

	public interface ILotacoesGet extends ISwaggerMethod {
		public void run(LotacoesGetRequest req, LotacoesGetResponse resp) throws Exception;
	}

}