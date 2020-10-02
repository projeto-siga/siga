package br.gov.jfrj.siga.ex.api.v1;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.crivano.swaggerservlet.ISwaggerMethod;
import com.crivano.swaggerservlet.ISwaggerModel;
import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerResponse;
import com.crivano.swaggerservlet.ISwaggerResponseFile;

public interface IExApiV1 {
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

	public class MesaItem implements ISwaggerModel {
		public String tipo;
		public Date datahora;
		public String tempoRelativo;
		public String codigo;
		public String sigla;
		public String descr;
		public String origem;
		public String grupo;
		public String grupoNome;
		public String grupoOrdem;
		public Boolean podeAnotar;
		public Boolean podeAssinar;
		public Boolean podeAssinarEmLote;
		public Boolean podeTramitar;
		public List<Marca> list;
	}

	public class AcessoItem implements ISwaggerModel {
		public Date datahora;
		public String ip;
	}

	public class Marca implements ISwaggerModel {
		public String pessoa;
		public String lotacao;
		public String nome;
		public String icone;
		public String titulo;
		public Date inicio;
		public Date termino;
		public Boolean daPessoa;
		public Boolean deOutraPessoa;
		public Boolean daLotacao;
	}

	public class Acao implements ISwaggerModel {
		public String nome;
		public String icone;
		public Boolean ativa;
	}

	public class Documento implements ISwaggerModel {
		public String sigla;
		public String html;
		public String data;
		public String descr;
		public List<Marca> marca;
		public List<Acao> acao;
	}

	public class ResultadoDePesquisa implements ISwaggerModel {
		public String sigla;
		public String nome;
		public String siglaLotacao;
	}

	public class Pessoa implements ISwaggerModel {
		public String idPessoa;
		public String idPessoaIni;
		public String sigla;
		public String nome;
		public String siglaLotacao;
	}
	
	public class Lotacao implements ISwaggerModel {
		public String idLotacao;
		public String idLotacaoIni;
		public String sigla;
		public String nome;
		public String orgao;
	}

	public class AutenticarPostRequest implements ISwaggerRequest {
	}

	public class AutenticarPostResponse implements ISwaggerResponse {
		public String token;
	}

	public interface IAutenticarPost extends ISwaggerMethod {
		public void run(AutenticarPostRequest req, AutenticarPostResponse resp) throws Exception;
	}

	public class DocumentoSiglaArquivoGetRequest implements ISwaggerRequest {
		public String contenttype;
		public String sigla;
		public Boolean estampa;
		public Boolean completo;
		public Boolean volumes;
		public Boolean exibirReordenacao;
	}

	public class DocumentoSiglaArquivoGetResponse implements ISwaggerResponse {
		public String uuid;
		public String jwt;
	}

	public interface IDocumentoSiglaArquivoGet extends ISwaggerMethod {
		public void run(DocumentoSiglaArquivoGetRequest req, DocumentoSiglaArquivoGetResponse resp) throws Exception;
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

	public class MesaGetRequest implements ISwaggerRequest {
	}

	public class MesaGetResponse implements ISwaggerResponse {
		public List<MesaItem> list;
	}

	public interface IMesaGet extends ISwaggerMethod {
		public void run(MesaGetRequest req, MesaGetResponse resp) throws Exception;
	}

	public class DocSiglaGetRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocSiglaGetResponse implements ISwaggerResponse, ISwaggerResponseFile {
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

	public interface IDocSiglaGet extends ISwaggerMethod {
		public void run(DocSiglaGetRequest req, DocSiglaGetResponse resp) throws Exception;
	}

	public class DocSiglaPdfGetRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocSiglaPdfGetResponse implements ISwaggerResponse {
		public String jwt;
	}

	public interface IDocSiglaPdfGet extends ISwaggerMethod {
		public void run(DocSiglaPdfGetRequest req, DocSiglaPdfGetResponse resp) throws Exception;
	}

	public class DocSiglaPdfCompletoGetRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocSiglaPdfCompletoGetResponse implements ISwaggerResponse {
		public String jwt;
	}

	public interface IDocSiglaPdfCompletoGet extends ISwaggerMethod {
		public void run(DocSiglaPdfCompletoGetRequest req, DocSiglaPdfCompletoGetResponse resp) throws Exception;
	}

	public class DocSiglaMovIdPdfGetRequest implements ISwaggerRequest {
		public String sigla;
		public String id;
	}

	public class DocSiglaMovIdPdfGetResponse implements ISwaggerResponse {
		public String jwt;
	}

	public interface IDocSiglaMovIdPdfGet extends ISwaggerMethod {
		public void run(DocSiglaMovIdPdfGetRequest req, DocSiglaMovIdPdfGetResponse resp) throws Exception;
	}

	public class DocSiglaAssinarComSenhaPostRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocSiglaAssinarComSenhaPostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocSiglaAssinarComSenhaPost extends ISwaggerMethod {
		public void run(DocSiglaAssinarComSenhaPostRequest req, DocSiglaAssinarComSenhaPostResponse resp) throws Exception;
	}

	public class DocSiglaAutenticarComSenhaPostRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocSiglaAutenticarComSenhaPostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocSiglaAutenticarComSenhaPost extends ISwaggerMethod {
		public void run(DocSiglaAutenticarComSenhaPostRequest req, DocSiglaAutenticarComSenhaPostResponse resp) throws Exception;
	}

	public class DocSiglaTramitarPostRequest implements ISwaggerRequest {
		public String sigla;
		public String lotacao;
		public String matricula;
		public String orgao;
		public String observacao;
		public String dataDevolucao;
	}

	public class DocSiglaTramitarPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IDocSiglaTramitarPost extends ISwaggerMethod {
		public void run(DocSiglaTramitarPostRequest req, DocSiglaTramitarPostResponse resp) throws Exception;
	}

	public class DocSiglaJuntarPostRequest implements ISwaggerRequest {
		public String sigla;
		public String siglapai;
	}

	public class DocSiglaJuntarPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IDocSiglaJuntarPost extends ISwaggerMethod {
		public void run(DocSiglaJuntarPostRequest req, DocSiglaJuntarPostResponse resp) throws Exception;
	}

	public class DocSiglaAnotarPostRequest implements ISwaggerRequest {
		public String sigla;
		public String anotacao;
	}

	public class DocSiglaAnotarPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IDocSiglaAnotarPost extends ISwaggerMethod {
		public void run(DocSiglaAnotarPostRequest req, DocSiglaAnotarPostResponse resp) throws Exception;
	}

	public class DocSiglaPesquisarSiglaGetRequest implements ISwaggerRequest {
		public String sigla;
		public String matricula;
	}

	public class DocSiglaPesquisarSiglaGetResponse implements ISwaggerResponse {
		public String codigo;
		public String sigla;
	}

	public interface IDocSiglaPesquisarSiglaGet extends ISwaggerMethod {
		public void run(DocSiglaPesquisarSiglaGetRequest req, DocSiglaPesquisarSiglaGetResponse resp) throws Exception;
	}

	public class SugestaoPostRequest implements ISwaggerRequest {
		public String nome;
		public String email;
		public String mensagem;
	}

	public class SugestaoPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface ISugestaoPost extends ISwaggerMethod {
		public void run(SugestaoPostRequest req, SugestaoPostResponse resp) throws Exception;
	}

	public class TokenCriarPostRequest implements ISwaggerRequest {
		public String username;
		public String password;
	}

	public class TokenCriarPostResponse implements ISwaggerResponse {
		public String id_token;
	}

	public interface ITokenCriarPost extends ISwaggerMethod {
		public void run(TokenCriarPostRequest req, TokenCriarPostResponse resp) throws Exception;
	}

	public class AcessosGetRequest implements ISwaggerRequest {
	}

	public class AcessosGetResponse implements ISwaggerResponse {
		public List<AcessoItem> list;
	}

	public interface IAcessosGet extends ISwaggerMethod {
		public void run(AcessosGetRequest req, AcessosGetResponse resp) throws Exception;
	}

	public class PessoaTextoPesquisarGetRequest implements ISwaggerRequest {
		public String texto;
	}

	public class PessoaTextoPesquisarGetResponse implements ISwaggerResponse {
		public List<ResultadoDePesquisa> list;
	}

	public interface IPessoaTextoPesquisarGet extends ISwaggerMethod {
		public void run(PessoaTextoPesquisarGetRequest req, PessoaTextoPesquisarGetResponse resp) throws Exception;
	}

	
	public class PessoaIdPessoaIniPessoaAtualGetRequest implements ISwaggerRequest {
		public String idPessoaIni;
	}

	public class PessoaIdPessoaIniPessoaAtualGetResponse implements ISwaggerResponse {
		public Pessoa pessoaAtual;
	}

	public interface IPessoaIdPessoaIniPessoaAtualGet extends ISwaggerMethod {
		public void run(PessoaIdPessoaIniPessoaAtualGetRequest req, PessoaIdPessoaIniPessoaAtualGetResponse resp) throws Exception;
	}


	public class LotacaoTextoPesquisarGetRequest implements ISwaggerRequest {
		public String texto;
	}

	public class LotacaoTextoPesquisarGetResponse implements ISwaggerResponse {
		public List<ResultadoDePesquisa> list;
	}

	public interface ILotacaoTextoPesquisarGet extends ISwaggerMethod {
		public void run(LotacaoTextoPesquisarGetRequest req, LotacaoTextoPesquisarGetResponse resp) throws Exception;
	}
	
	
	public class LotacaoIdLotacaoIniLotacaoAtualGetRequest implements ISwaggerRequest {
		public String idLotacaoIni;
	}

	public class LotacaoIdLotacaoIniLotacaoAtualGetResponse implements ISwaggerResponse {
		public Lotacao lotacaoAtual;
	}

	public interface ILotacaoIdLotacaoIniLotacaoAtualGet extends ISwaggerMethod {
		public void run(LotacaoIdLotacaoIniLotacaoAtualGetRequest req, LotacaoIdLotacaoIniLotacaoAtualGetResponse resp) throws Exception;
	}

}