package br.gov.jfrj.siga.ex.api.v1;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.crivano.swaggerservlet.ISwaggerMethod;
import com.crivano.swaggerservlet.ISwaggerModel;
import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerRequestFile;
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

	public class DocumentoDTO implements ISwaggerModel {
		public String modelo;
		public String siglamobilpai;
		public String subscritor;
		public String titular;
		public String descricaotipodoc;
		public String classificacao;
		public Boolean eletronico;
		public String pessoadestinatario;
		public String lotadestinatario;
		public String orgaoExternoDestinatario;
		public String destinatariocampoextra;
		public String descricaodocumento;
		public String nivelacesso;
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

	public class DocumentosSiglaArquivoGetRequest implements ISwaggerRequest {
		public String contenttype;
		public String sigla;
		public Boolean estampa;
		public Boolean completo;
		public Boolean volumes;
		public Boolean exibirReordenacao;
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

	public class MesaGetRequest implements ISwaggerRequest {
	}

	public class MesaGetResponse implements ISwaggerResponse {
		public List<MesaItem> list;
	}

	public interface IMesaGet extends ISwaggerMethod {
		public void run(MesaGetRequest req, MesaGetResponse resp) throws Exception;
	}

	public class DocumentosSiglaGetRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaGetResponse implements ISwaggerResponse, ISwaggerResponseFile {
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

	public interface IDocumentosSiglaGet extends ISwaggerMethod {
		public void run(DocumentosSiglaGetRequest req, DocumentosSiglaGetResponse resp) throws Exception;
	}

	public class DocumentosSiglaPdfGetRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaPdfGetResponse implements ISwaggerResponse {
		public String jwt;
	}

	public interface IDocumentosSiglaPdfGet extends ISwaggerMethod {
		public void run(DocumentosSiglaPdfGetRequest req, DocumentosSiglaPdfGetResponse resp) throws Exception;
	}

	public class DocumentosSiglaHtmlGetRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaHtmlGetResponse implements ISwaggerResponse {
		public String html;
	}

	public interface IDocumentosSiglaHtmlGet extends ISwaggerMethod {
		public void run(DocumentosSiglaHtmlGetRequest req, DocumentosSiglaHtmlGetResponse resp) throws Exception;
	}

	public class DocumentosSiglaPdfCompletoGetRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaPdfCompletoGetResponse implements ISwaggerResponse {
		public String jwt;
	}

	public interface IDocumentosSiglaPdfCompletoGet extends ISwaggerMethod {
		public void run(DocumentosSiglaPdfCompletoGetRequest req, DocumentosSiglaPdfCompletoGetResponse resp) throws Exception;
	}

	public class DocumentosSiglaMovIdPdfGetRequest implements ISwaggerRequest {
		public String sigla;
		public String id;
	}

	public class DocumentosSiglaMovIdPdfGetResponse implements ISwaggerResponse {
		public String jwt;
	}

	public interface IDocumentosSiglaMovIdPdfGet extends ISwaggerMethod {
		public void run(DocumentosSiglaMovIdPdfGetRequest req, DocumentosSiglaMovIdPdfGetResponse resp) throws Exception;
	}

	public class DocumentosSiglaAssinarComSenhaPostRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaAssinarComSenhaPostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocumentosSiglaAssinarComSenhaPost extends ISwaggerMethod {
		public void run(DocumentosSiglaAssinarComSenhaPostRequest req, DocumentosSiglaAssinarComSenhaPostResponse resp) throws Exception;
	}

	public class DocumentosSiglaAutenticarComSenhaPostRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaAutenticarComSenhaPostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocumentosSiglaAutenticarComSenhaPost extends ISwaggerMethod {
		public void run(DocumentosSiglaAutenticarComSenhaPostRequest req, DocumentosSiglaAutenticarComSenhaPostResponse resp) throws Exception;
	}

	public class DocumentosSiglaTramitarPostRequest implements ISwaggerRequest {
		public String sigla;
		public String lotacao;
		public String matricula;
		public String orgao;
		public String observacao;
		public String dataDevolucao;
	}

	public class DocumentosSiglaTramitarPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IDocumentosSiglaTramitarPost extends ISwaggerMethod {
		public void run(DocumentosSiglaTramitarPostRequest req, DocumentosSiglaTramitarPostResponse resp) throws Exception;
	}

	public class DocumentosSiglaJuntarPostRequest implements ISwaggerRequest {
		public String sigla;
		public String siglapai;
	}

	public class DocumentosSiglaJuntarPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IDocumentosSiglaJuntarPost extends ISwaggerMethod {
		public void run(DocumentosSiglaJuntarPostRequest req, DocumentosSiglaJuntarPostResponse resp) throws Exception;
	}

	public class DocumentosSiglaAnotarPostRequest implements ISwaggerRequest {
		public String sigla;
		public String anotacao;
	}

	public class DocumentosSiglaAnotarPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IDocumentosSiglaAnotarPost extends ISwaggerMethod {
		public void run(DocumentosSiglaAnotarPostRequest req, DocumentosSiglaAnotarPostResponse resp) throws Exception;
	}

	public class DocumentosSiglaPesquisarSiglaGetRequest implements ISwaggerRequest {
		public String sigla;
		public String matricula;
	}

	public class DocumentosSiglaPesquisarSiglaGetResponse implements ISwaggerResponse {
		public String codigo;
		public String sigla;
	}

	public interface IDocumentosSiglaPesquisarSiglaGet extends ISwaggerMethod {
		public void run(DocumentosSiglaPesquisarSiglaGetRequest req, DocumentosSiglaPesquisarSiglaGetResponse resp) throws Exception;
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
	}

	public interface ILotacaoIdLotacaoIniLotacaoAtualGet extends ISwaggerMethod {
		public void run(LotacaoIdLotacaoIniLotacaoAtualGetRequest req, LotacaoIdLotacaoIniLotacaoAtualGetResponse resp) throws Exception;
	}


	/* ESTE CÓDIGO DEVE SER MANTIDO POIS FOI GERADO MANUALMENTE.
	 * 
	 * O swaggerServlet não permite geração automatica de campos de upload de arquivos.
	 */
	public class DocumentosPostRequest implements ISwaggerRequest, ISwaggerRequestFile {	
		public String modelo;
		public String siglamobilpai;
		public String subscritor;
		public String titular;
		public Boolean eletronico;
		public String descricaotipodoc;
		public String classificacao;
		public String pessoadestinatario;
		public String lotadestinatario;
		public String orgaoexternodestinatario;
		public String destinatariocampoextra;
		public String descricaodocumento;
		public String nivelacesso;
		public String entrevista;
		public String arquivo;

		public String filename;
		public String contenttype = "application/pdf";
		public Object content;
		public Map<String, List<String>> headerFields;
		 
		public String getFilename() {
			return filename;
		}
		 
		public void setFilename(String filename) {
			this.filename = filename;
		}
		
		public String getContenttype() {
			return contenttype;
		}
		
		public void setContenttype(String contenttype) {
			this.contenttype = contenttype;
		}
		
		public Object getContent() {
			return content;
		}
		
		public void setContent(Object content) {
			this.content = content;
		}
		
		@Override
		public Map<String, List<String>> getHeaderFields() {
			return headerFields;
		}
		
		@Override
		public void setHeaderFields(Map<String, List<String>> headerFields) {
			this.headerFields = headerFields;
		}		
	}
	public class DocumentosPostResponse implements ISwaggerResponse {
		public String sigladoc;
	}

	public interface IDocumentosPost extends ISwaggerMethod {
		public void run(DocumentosPostRequest req, DocumentosPostResponse resp) throws Exception;
	}

}