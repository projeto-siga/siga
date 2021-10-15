package br.gov.jfrj.siga.ex.api.v1;

import java.io.InputStream;
import java.util.ArrayList;
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
	public static class Status implements ISwaggerModel {
		public String mensagem;
		public Double indice;
		public Double contador;
		public Double bytes;
		public String errormsg;
		public String stacktrace;
	}

	public static class Error implements ISwaggerModel {
		public String errormsg;
	}

	public static class MesaItem implements ISwaggerModel {
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
		public List<Marca> list = new ArrayList<>();
	}

	public static class QuadroItem implements ISwaggerModel {
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

	public static class ListaItem implements ISwaggerModel {
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

	public static class Marca implements ISwaggerModel {
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

	public static class Acao implements ISwaggerModel {
		public String nome;
		public String icone;
		public Boolean ativa;
	}

	public static class Documento implements ISwaggerModel {
		public String sigla;
		public String html;
		public String data;
		public String descr;
		public List<Marca> marca = new ArrayList<>();
		public List<Acao> acao = new ArrayList<>();
	}

	public static class DocumentoDTO implements ISwaggerModel {
		public String modelo;
		public String siglamobilpai;
		public String siglamobilfilho;
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

	public static class DocumentoPesq implements ISwaggerModel {
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

	public static class Marcador implements ISwaggerModel {
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

	public static class ModeloItem implements ISwaggerModel {
		public String idModelo;
		public String nome;
		public String descr;
	}

	public static class ModeloListaHierarquicaItem implements ISwaggerModel {
		public String idModelo;
		public String nome;
		public String descr;
		public Long level;
		public String keywords;
		public Boolean group;
		public Boolean selected;
	}

	public static class ClassificacaoItem implements ISwaggerModel {
		public String idClassificacao;
		public String sigla;
		public String nome;
	}

	public static class PerfilItem implements ISwaggerModel {
		public String idPerfil;
		public String nome;
	}

	public static class AcessoItem implements ISwaggerModel {
		public String idAcesso;
		public String nome;
	}

	public static class DossieItem implements ISwaggerModel {
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

	public interface IModelosGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public List<ModeloItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IModelosListaHierarquicaGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public Boolean isEditandoAnexo;
			public Boolean isCriandoSubprocesso;
			public String siglaMobPai;
			public Boolean isAutuando;
		}

		public static class Response implements ISwaggerResponse {
			public List<ModeloListaHierarquicaItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaModelosParaIncluirGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public List<ModeloItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaModelosParaAutuarGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public List<ModeloItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IModelosIdGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
		}

		public static class Response implements ISwaggerResponse {
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

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IModelosIdProcessarEntrevistaPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
			public String entrevista;
		}

		public static class Response implements ISwaggerResponse, ISwaggerResponseFile {
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

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IModelosIdPreverDocumentoHtmlPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
		}

		public static class Response implements ISwaggerResponse, ISwaggerResponseFile {
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

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IModelosIdPreverDocumentoPdfPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
		}

		public static class Response implements ISwaggerResponse, ISwaggerResponseFile {
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

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IClassificacoesGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String idClassificacaoIni;
			public String texto;
		}

		public static class Response implements ISwaggerResponse {
			public List<ClassificacaoItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaArquivoProduzirGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String contenttype;
			public String sigla;
			public Boolean estampa;
			public Boolean completo;
			public Boolean volumes;
			public Boolean exibirReordenacao;
		}

		public static class Response implements ISwaggerResponse, ISwaggerResponseFile {
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

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaArquivoGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String contenttype;
			public String sigla;
			public Boolean estampa;
			public Boolean completo;
			public Boolean volumes;
			public Boolean exibirReordenacao;
		}

		public static class Response implements ISwaggerResponse {
			public String uuid;
			public String jwt;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDownloadJwtFilenameGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String jwt;
			public String filename;
			public String disposition;
		}

		public static class Response implements ISwaggerResponse, ISwaggerResponseFile {
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

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IStatusChaveGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String chave;
		}

		public static class Response implements ISwaggerResponse {
			public String mensagem;
			public Double indice;
			public Double contador;
			public Double bytes;
			public String errormsg;
			public String stacktrace;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IMesaGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String filtroPessoaLotacao;
		}

		public static class Response implements ISwaggerResponse {
			public List<MesaItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IQuadroGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String filtroExpedienteProcesso;
		}

		public static class Response implements ISwaggerResponse {
			public List<QuadroItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IListaGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String idMarcador;
			public String filtroPessoaLotacao;
			public String filtroExpedienteProcesso;
		}

		public static class Response implements ISwaggerResponse {
			public List<ListaItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse, ISwaggerResponseFile {
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

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaPdfGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public String jwt;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaHtmlGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public String html;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaPdfCompletoGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public String jwt;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaMovIdPdfGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
			public String id;
		}

		public static class Response implements ISwaggerResponse {
			public String jwt;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaAssinarComSenhaPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public String sigla;
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaAutenticarComSenhaPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public String sigla;
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaArquivarCorrentePost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public String sigla;
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaDesarquivarCorrentePost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public String sigla;
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaSobrestarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public String sigla;
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaDessobrestarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public String sigla;
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaFinalizarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public String sigla;
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaDuplicarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public String sigla;
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaRefazerPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public String sigla;
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaTramitarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
			public String lotacao;
			public String matricula;
			public String orgao;
			public String observacao;
			public String dataDevolucao;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaDefinirPerfilPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
			public String idPerfil;
			public String lotacao;
			public String matricula;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaIncluirCossignatarioPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
			public String matricula;
			public String funcao;
			public String lotacao;
			public String localidade;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaDefinirAcessoPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
			public String idAcesso;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaJuntarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
			public String siglapai;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaDesentranharPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
			public String motivo;
		}

		public static class Response implements ISwaggerResponse {
			public String sigla;
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaVincularPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
			public String siglavertambem;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaIncluirCopiaPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
			public String siglacopia;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaApensarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
			public String siglamestre;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaDesapensarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public String sigla;
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaTornarSemEfeitoPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
			public String motivo;
		}

		public static class Response implements ISwaggerResponse {
			public String sigla;
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaCriarViaPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public String sigla;
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaReceberPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaAnotarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
			public String anotacao;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaExcluirPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaMarcarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
			public String idMarcador;
			public String lotacao;
			public String matricula;
			public String data1;
			public String data2;
			public String texto;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaPesquisarSiglaGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
			public String matricula;
		}

		public static class Response implements ISwaggerResponse {
			public String codigo;
			public String sigla;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaMarcadoresDisponiveisGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public List<Marcador> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaPerfisDisponiveisGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public List<PerfilItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaAcessosDisponiveisGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public List<AcessoItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaGerarProtocoloPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public String numeroProtocolo;
			public String linkProtocolo;
			public String dataHoraEmissaoProtocolo;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaConsultarProtocoloGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public String numeroProtocolo;
			public String linkProtocolo;
			public String dataHoraEmissaoProtocolo;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaDossieGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public List<DossieItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaAnexarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest, ISwaggerRequestFile {
			public String sigla;
			public String descricaodocumento;
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

		public static class Response implements ISwaggerResponse {
			public String id;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaMovimentacoesIdAssinarComSenhaPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
			public String id;
		}

		public static class Response implements ISwaggerResponse {
			public String sigla;
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaMovimentacoesIdAutenticarComSenhaPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
			public String id;
		}

		public static class Response implements ISwaggerResponse {
			public String sigla;
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaMovimentacoesIdExcluirPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
			public String id;
		}

		public static class Response implements ISwaggerResponse {
			public String sigla;
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosSiglaMovimentacoesIdCancelarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
			public String id;
		}

		public static class Response implements ISwaggerResponse {
			public String sigla;
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface ISugestaoPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String nome;
			public String email;
			public String mensagem;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
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

		public static class Response implements ISwaggerResponse {
			public List<DocumentoPesq> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

	public interface IDocumentosPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest, ISwaggerRequestFile {
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

		public static class Response implements ISwaggerResponse {
			public String sigladoc;
		}

		public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception;
	}

}
