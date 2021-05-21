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

	public class QuadroItem implements ISwaggerModel {
		public String grupoId;
		public String grupoNome;
		public String finalidadeId;
		public String marcadorId;
		public String marcadorNome;
		public String marcadorIcone;
		public String marcadorCor;
		public String qtdAtendente;
		public String qtdLotaAtendente;
	}

	public class ListaItem implements ISwaggerModel {
		public String sigla;
		public Date documentoData;
		public String documentoSubscritor;
		public String documentoLotaSubscritor;
		public String documentoEspecie;
		public String documentoModelo;
		public String documentoDescricao;
		public String mobilUltimaAnotacao;
		public String marcadorId;
		public String marcadorNome;
		public String marcadorIcone;
		public String marcadorCor;
		public Date marcaData;
		public String marcaResponsavel;
		public String marcaLotaResponsavel;
	}

	public class Marca implements ISwaggerModel {
		public String pessoa;
		public String lotacao;
		public String nome;
		public String icone;
		public String titulo;
		public String marcaId;
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
		public String orgao;
		public String nivelacesso;
	}

	public class DocumentoPesq implements ISwaggerModel {
		public String sigla;
		public String dtdoc;
		public String dtregdoc;
		public String dtinimarca;
		public String descricaomarcador;
		public String nomegrupomarcador;
		public String descricaodocumento;
		public String modelo;
		public String siglamobilpai;
		public String cadastrantesigla;
		public String cadastrantenome;
		public String lotacadastrantesigla;
		public String lotacadastrantenome;
		public String subscritorsigla;
		public String subscritornome;
		public String lotasubscritorsigla;
		public String lotasubscritornome;
		public String titular;
	}

	public class Marcador implements ISwaggerModel {
		public String idMarcador;
		public String grupo;
		public String nome;
		public Boolean ativo;
		public String explicacao;
		public String interessado;
		public String planejada;
		public String limite;
		public String texto;
	}

	public class ModeloItem implements ISwaggerModel {
		public String idModelo;
		public String nome;
		public String descr;
	}

	public class ModeloListaHierarquicaItem implements ISwaggerModel {
		public String idModelo;
		public String nome;
		public String descr;
		public int level;
		public String keywords;
		public boolean group;
		public boolean selected;
	}

	public class ClassificacaoItem implements ISwaggerModel {
		public String idClassificacao;
		public String sigla;
		public String nome;
	}

	public class PerfilItem implements ISwaggerModel {
		public String idPerfil;
		public String nome;
	}

	public class AcessoItem implements ISwaggerModel {
		public String idAcesso;
		public String nome;
	}

	public class DossieItem implements ISwaggerModel {
		public String paginaInicial;
		public String paginaFinal;
		public String mobil;
		public String descr;
		public String origem;
		public Date data;
		public String nivel;
		public Boolean copia;
		public String referenciaHtmlCompletoDocPrincipal;
		public String referenciaPDFCompletoDocPrincipal;
	}

	public class ModelosGetRequest implements ISwaggerRequest {
	}

	public class ModelosGetResponse implements ISwaggerResponse {
		public List<ModeloItem> list;
	}

	public interface IModelosGet extends ISwaggerMethod {
		public void run(ModelosGetRequest req, ModelosGetResponse resp) throws Exception;
	}

	public class ModelosListaHierarquicaGetRequest implements ISwaggerRequest {
		public Boolean isEditandoAnexo;
		public Boolean isCriandoSubprocesso;
		public String siglaMobPai;
		public Boolean isAutuando;
	}

	public class ModelosListaHierarquicaGetResponse implements ISwaggerResponse {
		public List<ModeloListaHierarquicaItem> list;
	}

	public interface IModelosListaHierarquicaGet extends ISwaggerMethod {
		public void run(ModelosListaHierarquicaGetRequest req, ModelosListaHierarquicaGetResponse resp) throws Exception;
	}

	public class DocumentosSiglaModelosParaIncluirGetRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaModelosParaIncluirGetResponse implements ISwaggerResponse {
		public List<ModeloItem> list;
	}

	public interface IDocumentosSiglaModelosParaIncluirGet extends ISwaggerMethod {
		public void run(DocumentosSiglaModelosParaIncluirGetRequest req,
				DocumentosSiglaModelosParaIncluirGetResponse resp) throws Exception;
	}

	public class DocumentosSiglaModelosParaAutuarGetRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaModelosParaAutuarGetResponse implements ISwaggerResponse {
		public List<ModeloItem> list;
	}

	public interface IDocumentosSiglaModelosParaAutuarGet extends ISwaggerMethod {
		public void run(DocumentosSiglaModelosParaAutuarGetRequest req,
				DocumentosSiglaModelosParaAutuarGetResponse resp) throws Exception;
	}

	public class ModelosIdGetRequest implements ISwaggerRequest {
		public String id;
	}

	public class ModelosIdGetResponse implements ISwaggerResponse {
		public String idModelo;
		public String nome;
		public String descr;
		public String especie;
		public String nivelDeAcesso;
		public String classificacao;
		public String classificacaoParaCriacaoDeVias;
		public String tipoDeSubscritor;
		public String tipoDeDestinatario;
		public String tipoDeConteudo;
		public String tipoDeDocumento;
		public String conteudo;
	}

	public interface IModelosIdGet extends ISwaggerMethod {
		public void run(ModelosIdGetRequest req, ModelosIdGetResponse resp) throws Exception;
	}

	public class ModelosIdProcessarEntrevistaPostRequest implements ISwaggerRequest {
		public String id;
		public String entrevista;
	}

	public class ModelosIdProcessarEntrevistaPostResponse implements ISwaggerResponse, ISwaggerResponseFile {
		public String contenttype = "text/html";
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

	public interface IModelosIdProcessarEntrevistaPost extends ISwaggerMethod {
		public void run(ModelosIdProcessarEntrevistaPostRequest req, ModelosIdProcessarEntrevistaPostResponse resp)
				throws Exception;
	}

	public class ModelosIdPreverDocumentoHtmlPostRequest implements ISwaggerRequest {
		public String id;
	}

	public class ModelosIdPreverDocumentoHtmlPostResponse implements ISwaggerResponse, ISwaggerResponseFile {
		public String contenttype = "text/html";
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

	public interface IModelosIdPreverDocumentoHtmlPost extends ISwaggerMethod {
		public void run(ModelosIdPreverDocumentoHtmlPostRequest req, ModelosIdPreverDocumentoHtmlPostResponse resp)
				throws Exception;
	}

	public class ModelosIdPreverDocumentoPdfPostRequest implements ISwaggerRequest {
		public String id;
	}

	public class ModelosIdPreverDocumentoPdfPostResponse implements ISwaggerResponse, ISwaggerResponseFile {
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

	public interface IModelosIdPreverDocumentoPdfPost extends ISwaggerMethod {
		public void run(ModelosIdPreverDocumentoPdfPostRequest req, ModelosIdPreverDocumentoPdfPostResponse resp)
				throws Exception;
	}

	public class ClassificacoesGetRequest implements ISwaggerRequest {
		public String idClassificacaoIni;
		public String texto;
	}

	public class ClassificacoesGetResponse implements ISwaggerResponse {
		public List<ClassificacaoItem> list;
	}

	public interface IClassificacoesGet extends ISwaggerMethod {
		public void run(ClassificacoesGetRequest req, ClassificacoesGetResponse resp) throws Exception;
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

	public class DocumentosSiglaArquivoProduzirGetRequest implements ISwaggerRequest {
		public String contenttype;
		public String sigla;
		public Boolean estampa;
		public Boolean completo;
		public Boolean volumes;
		public Boolean exibirReordenacao;
	}

	public class DocumentosSiglaArquivoProduzirGetResponse implements ISwaggerResponse, ISwaggerResponseFile {
		public String contenttype = "text/html";
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

	public interface IDocumentosSiglaArquivoProduzirGet extends ISwaggerMethod {
		public void run(DocumentosSiglaArquivoProduzirGetRequest req, DocumentosSiglaArquivoProduzirGetResponse resp)
				throws Exception;
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
		public String filtroPessoaLotacao;
	}

	public class MesaGetResponse implements ISwaggerResponse {
		public List<MesaItem> list;
	}

	public interface IMesaGet extends ISwaggerMethod {
		public void run(MesaGetRequest req, MesaGetResponse resp) throws Exception;
	}

	public class QuadroGetRequest implements ISwaggerRequest {
		public String filtroExpedienteProcesso;
	}

	public class QuadroGetResponse implements ISwaggerResponse {
		public List<QuadroItem> list;
	}

	public interface IQuadroGet extends ISwaggerMethod {
		public void run(QuadroGetRequest req, QuadroGetResponse resp) throws Exception;
	}

	public class ListaGetRequest implements ISwaggerRequest {
		public String idMarcador;
		public String filtroPessoaLotacao;
		public String filtroExpedienteProcesso;
	}

	public class ListaGetResponse implements ISwaggerResponse {
		public List<ListaItem> list;
	}

	public interface IListaGet extends ISwaggerMethod {
		public void run(ListaGetRequest req, ListaGetResponse resp) throws Exception;
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
		public void run(DocumentosSiglaPdfCompletoGetRequest req, DocumentosSiglaPdfCompletoGetResponse resp)
				throws Exception;
	}

	public class DocumentosSiglaMovIdPdfGetRequest implements ISwaggerRequest {
		public String sigla;
		public String id;
	}

	public class DocumentosSiglaMovIdPdfGetResponse implements ISwaggerResponse {
		public String jwt;
	}

	public interface IDocumentosSiglaMovIdPdfGet extends ISwaggerMethod {
		public void run(DocumentosSiglaMovIdPdfGetRequest req, DocumentosSiglaMovIdPdfGetResponse resp)
				throws Exception;
	}

	public class DocumentosSiglaAssinarComSenhaPostRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaAssinarComSenhaPostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocumentosSiglaAssinarComSenhaPost extends ISwaggerMethod {
		public void run(DocumentosSiglaAssinarComSenhaPostRequest req, DocumentosSiglaAssinarComSenhaPostResponse resp)
				throws Exception;
	}

	public class DocumentosSiglaAutenticarComSenhaPostRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaAutenticarComSenhaPostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocumentosSiglaAutenticarComSenhaPost extends ISwaggerMethod {
		public void run(DocumentosSiglaAutenticarComSenhaPostRequest req,
				DocumentosSiglaAutenticarComSenhaPostResponse resp) throws Exception;
	}

	public class DocumentosSiglaArquivarCorrentePostRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaArquivarCorrentePostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocumentosSiglaArquivarCorrentePost extends ISwaggerMethod {
		public void run(DocumentosSiglaArquivarCorrentePostRequest req,
				DocumentosSiglaArquivarCorrentePostResponse resp) throws Exception;
	}

	public class DocumentosSiglaSobrestarPostRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaSobrestarPostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocumentosSiglaSobrestarPost extends ISwaggerMethod {
		public void run(DocumentosSiglaSobrestarPostRequest req, DocumentosSiglaSobrestarPostResponse resp)
				throws Exception;
	}

	public class DocumentosSiglaFinalizarPostRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaFinalizarPostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocumentosSiglaFinalizarPost extends ISwaggerMethod {
		public void run(DocumentosSiglaFinalizarPostRequest req, DocumentosSiglaFinalizarPostResponse resp)
				throws Exception;
	}

	public class DocumentosSiglaDuplicarPostRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaDuplicarPostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocumentosSiglaDuplicarPost extends ISwaggerMethod {
		public void run(DocumentosSiglaDuplicarPostRequest req, DocumentosSiglaDuplicarPostResponse resp)
				throws Exception;
	}

	public class DocumentosSiglaRefazerPostRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaRefazerPostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocumentosSiglaRefazerPost extends ISwaggerMethod {
		public void run(DocumentosSiglaRefazerPostRequest req, DocumentosSiglaRefazerPostResponse resp)
				throws Exception;
	}

	public class DocumentosSiglaAnexarPostRequest implements ISwaggerRequest, ISwaggerRequestFile {
		public String sigla;
		public String descricaodocumento;
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

	public class DocumentosSiglaAnexarPostResponse implements ISwaggerResponse {
		public String id;
	}

	public interface IDocumentosSiglaAnexarPost extends ISwaggerMethod {
		public void run(DocumentosSiglaAnexarPostRequest req, DocumentosSiglaAnexarPostResponse resp) throws Exception;
	}

	public class DocumentosSiglaMovimentacoesIdAssinarComSenhaPostRequest implements ISwaggerRequest {
		public String sigla;
		public String id;
	}

	public class DocumentosSiglaMovimentacoesIdAssinarComSenhaPostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocumentosSiglaMovimentacoesIdAssinarComSenhaPost extends ISwaggerMethod {
		public void run(DocumentosSiglaMovimentacoesIdAssinarComSenhaPostRequest req,
				DocumentosSiglaMovimentacoesIdAssinarComSenhaPostResponse resp) throws Exception;
	}

	public class DocumentosSiglaMovimentacoesIdAutenticarComSenhaPostRequest implements ISwaggerRequest {
		public String sigla;
		public String id;
	}

	public class DocumentosSiglaMovimentacoesIdAutenticarComSenhaPostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocumentosSiglaMovimentacoesIdAutenticarComSenhaPost extends ISwaggerMethod {
		public void run(DocumentosSiglaMovimentacoesIdAutenticarComSenhaPostRequest req,
				DocumentosSiglaMovimentacoesIdAutenticarComSenhaPostResponse resp) throws Exception;
	}

	public class DocumentosSiglaMovimentacoesIdExcluirPostRequest implements ISwaggerRequest {
		public String sigla;
		public String id;
	}

	public class DocumentosSiglaMovimentacoesIdExcluirPostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocumentosSiglaMovimentacoesIdExcluirPost extends ISwaggerMethod {
		public void run(DocumentosSiglaMovimentacoesIdExcluirPostRequest req,
				DocumentosSiglaMovimentacoesIdExcluirPostResponse resp) throws Exception;
	}

	public class DocumentosSiglaMovimentacaoIdCancelarPostRequest implements ISwaggerRequest {
		public String sigla;
		public String id;
	}

	public class DocumentosSiglaMovimentacaoIdCancelarPostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocumentosSiglaMovimentacaoIdCancelarPost extends ISwaggerMethod {
		public void run(DocumentosSiglaMovimentacaoIdCancelarPostRequest req,
				DocumentosSiglaMovimentacaoIdCancelarPostResponse resp) throws Exception;
	}

	public class DocumentosSiglaTornarSemEfeitoPostRequest implements ISwaggerRequest {
		public String sigla;
		public String motivo;
	}

	public class DocumentosSiglaTornarSemEfeitoPostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocumentosSiglaTornarSemEfeitoPost extends ISwaggerMethod {
		public void run(DocumentosSiglaTornarSemEfeitoPostRequest req, DocumentosSiglaTornarSemEfeitoPostResponse resp)
				throws Exception;
	}

	public class DocumentosSiglaDesentranharPostRequest implements ISwaggerRequest {
		public String sigla;
		public String motivo;
	}

	public class DocumentosSiglaDesentranharPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IDocumentosSiglaDesentranharPost extends ISwaggerMethod {
		public void run(DocumentosSiglaDesentranharPostRequest req, DocumentosSiglaDesentranharPostResponse resp)
				throws Exception;
	}

	public class DocumentosSiglaDesapensarPostRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaDesapensarPostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocumentosSiglaDesapensarPost extends ISwaggerMethod {
		public void run(DocumentosSiglaDesapensarPostRequest req, DocumentosSiglaDesapensarPostResponse resp)
				throws Exception;
	}

	public class DocumentosSiglaDessobrestarPostRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaDessobrestarPostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocumentosSiglaDessobrestarPost extends ISwaggerMethod {
		public void run(DocumentosSiglaDessobrestarPostRequest req, DocumentosSiglaDessobrestarPostResponse resp)
				throws Exception;
	}

	public class DocumentosSiglaDesarquivarCorrentePostRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaDesarquivarCorrentePostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocumentosSiglaDesarquivarCorrentePost extends ISwaggerMethod {
		public void run(DocumentosSiglaDesarquivarCorrentePostRequest req,
				DocumentosSiglaDesarquivarCorrentePostResponse resp) throws Exception;
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
		public void run(DocumentosSiglaTramitarPostRequest req, DocumentosSiglaTramitarPostResponse resp)
				throws Exception;
	}

	public class DocumentosSiglaDefinirPerfilPostRequest implements ISwaggerRequest {
		public String sigla;
		public String idPerfil;
		public String lotacao;
		public String matricula;
	}

	public class DocumentosSiglaDefinirPerfilPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IDocumentosSiglaDefinirPerfilPost extends ISwaggerMethod {
		public void run(DocumentosSiglaDefinirPerfilPostRequest req, DocumentosSiglaDefinirPerfilPostResponse resp)
				throws Exception;
	}

	public class DocumentosSiglaIncluirCossignatarioPostRequest implements ISwaggerRequest {
		public String sigla;
		public String matricula;
		public String funcao;
		public String lotacao;
		public String localidade;
	}

	public class DocumentosSiglaIncluirCossignatarioPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IDocumentosSiglaIncluirCossignatarioPost extends ISwaggerMethod {
		public void run(DocumentosSiglaIncluirCossignatarioPostRequest req,
				DocumentosSiglaIncluirCossignatarioPostResponse resp) throws Exception;
	}

	public class DocumentosSiglaDefinirAcessoPostRequest implements ISwaggerRequest {
		public String sigla;
		public String idAcesso;
	}

	public class DocumentosSiglaDefinirAcessoPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IDocumentosSiglaDefinirAcessoPost extends ISwaggerMethod {
		public void run(DocumentosSiglaDefinirAcessoPostRequest req, DocumentosSiglaDefinirAcessoPostResponse resp)
				throws Exception;
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

	public class DocumentosSiglaVincularPostRequest implements ISwaggerRequest {
		public String sigla;
		public String siglavertambem;
	}

	public class DocumentosSiglaVincularPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IDocumentosSiglaVincularPost extends ISwaggerMethod {
		public void run(DocumentosSiglaVincularPostRequest req, DocumentosSiglaVincularPostResponse resp)
				throws Exception;
	}

	public class DocumentosSiglaApensarPostRequest implements ISwaggerRequest {
		public String sigla;
		public String siglamestre;
	}

	public class DocumentosSiglaApensarPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IDocumentosSiglaApensarPost extends ISwaggerMethod {
		public void run(DocumentosSiglaApensarPostRequest req, DocumentosSiglaApensarPostResponse resp)
				throws Exception;
	}

	public class DocumentosSiglaIncluirCopiaPostRequest implements ISwaggerRequest {
		public String sigla;
		public String siglacopia;
	}

	public class DocumentosSiglaIncluirCopiaPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IDocumentosSiglaIncluirCopiaPost extends ISwaggerMethod {
		public void run(DocumentosSiglaIncluirCopiaPostRequest req, DocumentosSiglaIncluirCopiaPostResponse resp)
				throws Exception;
	}

	public class DocumentosSiglaReceberPostRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaReceberPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IDocumentosSiglaReceberPost extends ISwaggerMethod {
		public void run(DocumentosSiglaReceberPostRequest req, DocumentosSiglaReceberPostResponse resp)
				throws Exception;
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

	public class DocumentosSiglaExcluirPostRequest implements ISwaggerRequest {
		public String sigla;
		public String anotacao;
	}

	public class DocumentosSiglaExcluirPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IDocumentosSiglaExcluirPost extends ISwaggerMethod {
		public void run(DocumentosSiglaExcluirPostRequest req, DocumentosSiglaExcluirPostResponse resp)
				throws Exception;
	}

	public class DocumentosSiglaMarcarPostRequest implements ISwaggerRequest {
		public String sigla;
		public String idMarcador;
		public String lotacao;
		public String matricula;
		public String data1;
		public String data2;
		public String texto;
	}

	public class DocumentosSiglaMarcarPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IDocumentosSiglaMarcarPost extends ISwaggerMethod {
		public void run(DocumentosSiglaMarcarPostRequest req, DocumentosSiglaMarcarPostResponse resp) throws Exception;
	}

	public class DocumentosSiglaCriarViaPostRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaCriarViaPostResponse implements ISwaggerResponse {
		public String sigla;
		public String status;
	}

	public interface IDocumentosSiglaCriarViaPost extends ISwaggerMethod {
		public void run(DocumentosSiglaCriarViaPostRequest req, DocumentosSiglaCriarViaPostResponse resp)
				throws Exception;
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
		public void run(DocumentosSiglaPesquisarSiglaGetRequest req, DocumentosSiglaPesquisarSiglaGetResponse resp)
				throws Exception;
	}

	public class DocumentosSiglaMarcadoresDisponiveisGetRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaMarcadoresDisponiveisGetResponse implements ISwaggerResponse {
		public List<Marcador> list;
	}

	public interface IDocumentosSiglaMarcadoresDisponiveisGet extends ISwaggerMethod {
		public void run(DocumentosSiglaMarcadoresDisponiveisGetRequest req,
				DocumentosSiglaMarcadoresDisponiveisGetResponse resp) throws Exception;
	}

	public class DocumentosSiglaPerfisDisponiveisGetRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaPerfisDisponiveisGetResponse implements ISwaggerResponse {
		public List<PerfilItem> list;
	}

	public interface IDocumentosSiglaPerfisDisponiveisGet extends ISwaggerMethod {
		public void run(DocumentosSiglaPerfisDisponiveisGetRequest req,
				DocumentosSiglaPerfisDisponiveisGetResponse resp) throws Exception;
	}

	public class DocumentosSiglaAcessosDisponiveisGetRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaAcessosDisponiveisGetResponse implements ISwaggerResponse {
		public List<AcessoItem> list;
	}

	public interface IDocumentosSiglaAcessosDisponiveisGet extends ISwaggerMethod {
		public void run(DocumentosSiglaAcessosDisponiveisGetRequest req,
				DocumentosSiglaAcessosDisponiveisGetResponse resp) throws Exception;
	}

	public class DocumentosSiglaGerarProtocoloPostRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaGerarProtocoloPostResponse implements ISwaggerResponse {
		public String numeroProtocolo;
		public String linkProtocolo;
		public String dataHoraEmissaoProtocolo;
	}

	public interface IDocumentosSiglaGerarProtocoloPost extends ISwaggerMethod {
		public void run(DocumentosSiglaGerarProtocoloPostRequest req, DocumentosSiglaGerarProtocoloPostResponse resp)
				throws Exception;
	}

	public class DocumentosSiglaDossieGetRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaDossieGetResponse implements ISwaggerResponse {
		public List<DossieItem> list;
	}

	public interface IDocumentosSiglaDossieGet extends ISwaggerMethod {
		public void run(DocumentosSiglaDossieGetRequest req, DocumentosSiglaDossieGetResponse resp) throws Exception;
	}

	public class DocumentosSiglaConsultarProtocoloGetRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class DocumentosSiglaConsultarProtocoloGetResponse implements ISwaggerResponse {
		public String numeroProtocolo;
		public String linkProtocolo;
		public String dataHoraEmissaoProtocolo;
	}

	public interface IDocumentosSiglaConsultarProtocoloGet extends ISwaggerMethod {
		public void run(DocumentosSiglaConsultarProtocoloGetRequest req,
				DocumentosSiglaConsultarProtocoloGetResponse resp) throws Exception;
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

	public class DocumentosGetRequest implements ISwaggerRequest {
		public Long offset;
		public Long qtdmax;
		public Long ordenacao;
		public String marcador;
		public String grupomarcador;
		public Long idpessoa;
		public Long idlotacao;
		public String dtinicial;
		public String dtfinal;
	}

	public class DocumentosGetResponse implements ISwaggerResponse {
		public List<DocumentoPesq> list;
	}

	public interface IDocumentosGet extends ISwaggerMethod {
		public void run(DocumentosGetRequest req, DocumentosGetResponse resp) throws Exception;
	}

	/*
	 * ESTE CÓDIGO DEVE SER MANTIDO POIS FOI GERADO MANUALMENTE.
	 * 
	 * O swaggerServlet não permite geração automatica de campos de upload de
	 * arquivos.
	 */
	public class DocumentosPostRequest implements ISwaggerRequest, ISwaggerRequestFile {
		public String sigla;
		public String modelo;
		public String siglamobilpai;
		public String siglamobilfilho;
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