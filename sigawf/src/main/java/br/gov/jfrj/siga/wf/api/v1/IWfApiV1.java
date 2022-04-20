package br.gov.jfrj.siga.wf.api.v1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.crivano.swaggerservlet.ISwaggerMethod;
import com.crivano.swaggerservlet.ISwaggerModel;
import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerResponse;

public interface IWfApiV1 {
	public static class Procedimento implements ISwaggerModel {
		public String id;
		public String sigla;
		public String principalTipo;
		public String principalSigla;
		public Pessoa atendente;
		public Lotacao lotaAtendente;
		public String prioridadeId;
		public String prioridadeNome;
		public String vizProcedimento;
		public String msgAviso;
		public Boolean formulario;
		public Boolean desabilitarFormulario;
		public String tarefaTitulo;
		public Date tarefaDataDeInicio;
		public String tarefaTempoRelativo;
		public DefinicaoDeTarefa definicaoDeTarefaCorrente;
		public String definicaoDeProcedimentoId;
		public String definicaoDeProcedimentoSigla;
		public String definicaoDeProcedimentoNome;
		public List<Variavel> variaveis = new ArrayList<>();
		public List<Evento> eventos = new ArrayList<>();
		public List<Acao> acoes = new ArrayList<>();
	}

	public static class Acao implements ISwaggerModel {
		public String nome;
		public String icone;
		public String nameSpace;
		public String acao;
		public Boolean pode;
		public String explicacao;
		public String msgConfirmacao;
		public String params;
		public String pre;
		public String pos;
		public String classe;
		public String modal;
		public Boolean post;
	}

	public static class Evento implements ISwaggerModel {
		public String eventoId;
		public Date eventoHora;
		public String eventoTempoRelativo;
		public String eventoTitulo;
		public String eventoDescr;
		public Pessoa responsavel;
		public Lotacao lotaResponsavel;
	}

	public static class Variavel implements ISwaggerModel {
		public String variavelIdentificador;
		public String variavelTipo;
		public String variavelValorString;
		public Date variavelValorDate;
		public Boolean variavelValorBoolean;
		public Double variavelValorNumber;
	}

	public static class DefinicaoDeResponsavel implements ISwaggerModel {
		public String definicaoDeResponsavelId;
		public String definicaoDeResponsavelNome;
		public String definicaoDeResponsavelDescr;
		public String definicaoDeResponsavelTipo;
	}

	public static class Responsavel implements ISwaggerModel {
		public String responsavelId;
		public String orgaoId;
		public String orgaoSigla;
		public Pessoa pessoa;
		public Lotacao lotacao;
	}

	public static class DefinicaoDeProcedimento implements ISwaggerModel {
		public String definicaoDeProcedimentoId;
		public String definicaoDeProcedimentoSigla;
		public String definicaoDeProcedimentoNome;
		public String definicaoDeProcedimentoDescr;
		public String definicaoDeProcedimentoAno;
		public String definicaoDeProcedimentoNumero;
		public String orgaoId;
		public Pessoa responsavel;
		public Lotacao lotaResponsavel;
		public String acessoDeEdicaoId;
		public String acessoDeInicializacaoId;
		public String tipoDePrincipalId;
		public String tipoDeVinculoComPrincipalId;
		public List<DefinicaoDeTarefa> definicoesDeTarefas = new ArrayList<>();
	}

	public static class DefinicaoDeTarefa implements ISwaggerModel {
		public String definicaoDeTarefaId;
		public String definicaoDeTarefaOrdem;
		public String definicaoDeTarefaTipo;
		public String definicaoDeTarefaNome;
		public String definicaoDeTarefaAssunto;
		public String definicaoDeTarefaConteudo;
		public String definicaoDeTarefaSeguinteId;
		public Boolean definicaoDeTarefaUltimo;
		public String tipoDeResponsavel;
		public String definicaoDeResponsavelId;
		public Pessoa responsavel;
		public Lotacao lotaResponsavel;
		public String refId;
		public String refSigla;
		public String refDescr;
		public List<DefinicaoDeVariavel> definicoesDeVariaveis = new ArrayList<>();
		public List<DefinicaoDeDesvio> definicoesDeDesvios = new ArrayList<>();
	}

	public static class DefinicaoDeVariavel implements ISwaggerModel {
		public String definicaoDeVariavelId;
		public String definicaoDeVariavelOrdem;
		public String definicaoDeVariavelIdentificador;
		public String definicaoDeVariavelNome;
		public String definicaoDeVariavelTipo;
		public String definicaoDeVariavelAcesso;
	}

	public static class DefinicaoDeDesvio implements ISwaggerModel {
		public String definicaoDeDesvioId;
		public String definicaoDeDesvioOrdem;
		public String definicaoDeDesvioNome;
		public String definicaoDeDesvioCondicao;
		public String definicaoDeTarefaSeguinteId;
		public Boolean definicaoDeTarefaUltimo;
	}

	public static class Enumeravel implements ISwaggerModel {
		public String id;
		public String descr;
	}

	public static class Pessoa implements ISwaggerModel {
		public String id;
		public String sigla;
		public String descr;
	}

	public static class Lotacao implements ISwaggerModel {
		public String id;
		public String sigla;
		public String descr;
	}

	public static class QuadroItem implements ISwaggerModel {
		public String finalidadeId;
		public String tipoId;
		public String tipoNome;
		public String grupoId;
		public String grupoNome;
		public String marcadorEnum;
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

	public static class PainelListaItem implements ISwaggerModel {
		public String marcaId;
		public String marcaTipo;
		public String marcaTexto;
		public String marcaIcone;
		public Date dataIni;
		public Date dataFim;
		public String moduloId;
		public String refId;
		public String movId;
		public String tipo;
		public String codigo;
		public String sigla;
		public String descricao;
		public String origem;
		public String ultimaAnotacao;
		public List<Marca> marcas = new ArrayList<>();
		public List<Acao> acoes = new ArrayList<>();
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

	public static class Error implements ISwaggerModel {
		public String errormsg;
	}

	public interface IPainelListaGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String idMarcas;
		}

		public static class Response implements ISwaggerResponse {
			public List<PainelListaItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IListaGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String idMarcador;
			public String filtroPessoaLotacao;
		}

		public static class Response implements ISwaggerResponse {
			public List<ListaItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IQuadroGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String estilo;
		}

		public static class Response implements ISwaggerResponse {
			public List<QuadroItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IAtivosGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public List<Procedimento> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IRelacionadosAoPrincipalPrincipalSiglaGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String principalSigla;
		}

		public static class Response implements ISwaggerResponse {
			public List<Procedimento> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IProcedimentosProcedimentoSiglaGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String procedimentoSigla;
		}

		public static class Response implements ISwaggerResponse {
			public Procedimento procedimento;
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IProcedimentosProcedimentoSiglaContinuarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String procedimentoSigla;
			public String variaveis;
			public String indiceDoDesvio;
		}

		public static class Response implements ISwaggerResponse {
			public Procedimento procedimento;
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IProcedimentosProcedimentoSiglaPegarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String procedimentoSigla;
		}

		public static class Response implements ISwaggerResponse {
			public Procedimento procedimento;
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IProcedimentosProcedimentoSiglaRedirecionarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String procedimentoSigla;
			public String definicaoDeTarefaId;
		}

		public static class Response implements ISwaggerResponse {
			public Procedimento procedimento;
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IProcedimentosProcedimentoSiglaTerminarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String procedimentoSigla;
		}

		public static class Response implements ISwaggerResponse {
			public Procedimento procedimento;
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IProcedimentosProcedimentoSiglaAnotarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String procedimentoSigla;
			public String anotacaoTexto;
		}

		public static class Response implements ISwaggerResponse {
			public Procedimento procedimento;
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IProcedimentosProcedimentoSiglaEventosEventoIdDelete extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String procedimentoSigla;
			public String eventoId;
		}

		public static class Response implements ISwaggerResponse {
			public Procedimento procedimento;
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IDefinicoesDeProcedimentosIniciaveisGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public List<Procedimento> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IDefinicoesDeProcedimentosVazioGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String definicaoDeProcedimentoSigla;
		}

		public static class Response implements ISwaggerResponse {
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IDefinicoesDeProcedimentosDefinicaoDeProcedimentoSiglaGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String definicaoDeProcedimentoSigla;
		}

		public static class Response implements ISwaggerResponse {
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IDefinicoesDeProcedimentosDefinicaoDeProcedimentoSiglaPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String definicaoDeProcedimentoSigla;
		}

		public static class Response implements ISwaggerResponse {
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IDefinicoesDeProcedimentosDefinicaoDeProcedimentoSiglaIniciarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String definicaoDeProcedimentoSigla;
			public String definicaoDeTarefaId;
			public String principalTipo;
			public String principalSigla;
			public String variaveis;
		}

		public static class Response implements ISwaggerResponse {
			public List<Procedimento> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IDefinicoesDeProcedimentosDefinicaoDeProcedimentoSiglaDesativarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String definicaoDeProcedimentoSigla;
		}

		public static class Response implements ISwaggerResponse {
			public DefinicaoDeProcedimento definicaoDeProcedimento;
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IDefinicoesDeResponsaveisGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public List<DefinicaoDeResponsavel> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IDefinicoesDeResponsaveisPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String definicaoDeResponsavelNome;
			public String definicaoDeResponsavelDescr;
			public String definicaoDeResponsavelTipo;
		}

		public static class Response implements ISwaggerResponse {
			public DefinicaoDeResponsavel definicaoDeResponsavel;
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IDefinicoesDeResponsaveisDefinicaoDeResponsavelIdGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String definicaoDeResponsavelId;
		}

		public static class Response implements ISwaggerResponse {
			public List<DefinicaoDeResponsavel> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IDefinicoesDeResponsaveisDefinicaoDeResponsavelIdPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String definicaoDeResponsavelId;
			public String definicaoDeResponsavelNome;
			public String definicaoDeResponsavelDescr;
			public String definicaoDeResponsavelTipo;
		}

		public static class Response implements ISwaggerResponse {
			public DefinicaoDeResponsavel definicaoDeResponsavel;
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IDefinicoesDeResponsaveisDefinicaoDeResponsavelIdDelete extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String definicaoDeResponsavelId;
		}

		public static class Response implements ISwaggerResponse {
			public DefinicaoDeResponsavel definicaoDeResponsavel;
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IDefinicoesDeResponsaveisDefinicaoDeResponsavelIdResponsaveisGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String definicaoDeResponsavelId;
		}

		public static class Response implements ISwaggerResponse {
			public List<Responsavel> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IDefinicoesDeResponsaveisDefinicaoDeResponsavelIdResponsaveisPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String definicaoDeResponsavelId;
			public String orgaoId;
			public String lotacaoId;
			public String pessoaId;
		}

		public static class Response implements ISwaggerResponse {
			public Responsavel responsavel;
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IDefinicoesDeResponsaveisDefinicaoDeResponsavelIdResponsaveisResponsavelIdGet
			extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String definicaoDeResponsavelId;
			public String responsavelId;
		}

		public static class Response implements ISwaggerResponse {
			public List<Responsavel> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IDefinicoesDeResponsaveisDefinicaoDeResponsavelIdResponsaveisResponsavelIdPost
			extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String definicaoDeResponsavelId;
			public String responsavelId;
			public String orgaoId;
			public String lotacaoId;
			public String pessoaId;
		}

		public static class Response implements ISwaggerResponse {
			public Responsavel responsavel;
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IDefinicoesDeResponsaveisDefinicaoDeResponsavelIdResponsaveisResponsavelIdDelete
			extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String definicaoDeResponsavelId;
			public String responsavelId;
		}

		public static class Response implements ISwaggerResponse {
			public Responsavel responsavel;
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IAcessosDeEdicaoGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public List<Enumeravel> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IAcessosDeInicializacaoGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public List<Enumeravel> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface ITiposDePrincipalGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public List<Enumeravel> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface ITiposDeVinculoComPrincipalGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public List<Enumeravel> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

	public interface IDefinicoesDeProcedimentosDefinicaoDeProcedimentoSiglaRelatorioRelatorioIdPost
			extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String definicaoDeProcedimentoSigla;
			public String relatorioId;
			public String grupoIni;
			public String grupoFim;
			public Boolean incluirAbertos;
			public Double minMediaTruncada;
			public Double maxMediaTruncada;
		}

		public static class Response implements ISwaggerResponse {
			public DefinicaoDeProcedimento definicaoDeProcedimento;
		}

		public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception;
	}

}
