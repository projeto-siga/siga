package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.crivano.swaggerservlet.ISwaggerMethod;
import com.crivano.swaggerservlet.ISwaggerModel;
import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerResponse;

public interface ISigaApiV1 {
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

	public static class Pessoa implements ISwaggerModel {
		public String idPessoa;
		public String idPessoaIni;
		public String sigla;
		public String nome;
		public String siglaLotacao;
		public String email;
		public Boolean isExternaPessoa;
		public Lotacao lotacao;
		public Cargo cargo;
		public FuncaoConfianca funcaoConfianca;
	}

	public static class Orgao implements ISwaggerModel {
		public String idOrgao;
		public String sigla;
		public String nome;
	}

	public static class Lotacao implements ISwaggerModel {
		public String idLotacao;
		public String idLotacaoIni;
		public String sigla;
		public String siglaLotacao;
		public String nome;
		public Orgao orgao;
	}

	public static class LotacaoAtual implements ISwaggerModel {
		public String idLotacao;
		public String idLotacaoIni;
		public String sigla;
		public String nome;
		public String orgao;
	}

	public static class Cargo implements ISwaggerModel {
		public String idCargo;
		public String idCargoIni;
		public String idExterna;
		public String sigla;
		public String nome;
	}

	public static class FuncaoConfianca implements ISwaggerModel {
		public String idFuncaoConfianca;
		public String idFuncaoConfiancaIni;
		public String idExterna;
		public String sigla;
		public String nome;
		public String idpai;
	}

	public static class Localidade implements ISwaggerModel {
		public String idLocalidade;
		public String nome;
		public Uf uf;
	}

	public static class Uf implements ISwaggerModel {
		public String idUf;
		public String nomeUf;
	}

	public static class AcessoItem implements ISwaggerModel {
		public Date datahora;
		public String ip;
	}

	public static class Notificacao implements ISwaggerModel {
		public String idNotificacao;
		public String titulo;
		public String icone;
		public String resumo;
		public String conteudo;
		public Date dataInicio;
		public Date dataTermino;
		public Boolean sempreMostrar;
	}

	public static class Usuario implements ISwaggerModel {
		public String identidadeId;
		public String cadastranteId;
		public String cadastranteSigla;
		public String cadastranteNome;
		public String lotaCadastranteId;
		public String lotaCadastranteSigla;
		public String lotaCadastranteNome;
		public String titularId;
		public String titularSigla;
		public String titularNome;
		public String lotaTitularId;
		public String lotaTitularSigla;
		public String lotaTitularNome;
		public String substituicaoId;
		public List<SubstituicaoItem> substituicoesPermitidas = new ArrayList<>();
	}

	public static class SubstituicaoItem implements ISwaggerModel {
		public String substituicaoId;
		public Date substituicaoDataFim;
		public Date substituicaoDataInicio;
		public String registroIdInicial;
		public Date registroDataFim;
		public Date registroDataInicio;
		public String substitutoId;
		public String substitutoSigla;
		public String substitutoNome;
		public String lotaSubstitutoId;
		public String lotaSubstitutoSigla;
		public String lotaSubstitutoNome;
		public String titularId;
		public String titularSigla;
		public String titularNome;
		public String lotaTitularId;
		public String lotaTitularSigla;
		public String lotaTitularNome;
	}

	public static class PainelQuadroItem implements ISwaggerModel {
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
		public List<PainelQuadroQtdItem> qtds = new ArrayList<>();
	}

	public static class PainelQuadroQtdItem implements ISwaggerModel {
		public String tipo;
		public String filtro;
		public String qtd;
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

	public static class Acao implements ISwaggerModel {
		public String nome;
		public String icone;
		public Boolean ativa;
	}

	public interface IAutenticarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public String token;
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface IPessoasGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String idPessoaIni;
			public String texto;
			public String cpf;
		}

		public static class Response implements ISwaggerResponse {
			public List<Pessoa> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface IPessoasPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String siglaOrgao;
			public String idCargoIni;
			public String idFuncaoConfiancaIni;
			public String lotacao;
			public String formCpf;
			public String nome;
			public String nomeAbreviado;
			public String dataNascimento;
			public String email;
			public String rg;
			public String rgOrgaoExpedidor;
			public String rgUf;
			public String rgDataExpedicao;
			public Boolean enviarEmailAcesso;
		}

		public static class Response implements ISwaggerResponse {
			public String idPessoa;
			public String siglaPessoa;
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface IPessoasSiglaGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public Pessoa pessoa;
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface IPinPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String pin;
		}

		public static class Response implements ISwaggerResponse {
			public String mensagem;
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface IPinTrocarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String pinAtual;
			public String pin;
		}

		public static class Response implements ISwaggerResponse {
			public String mensagem;
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface IPinResetPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String tokenPin;
			public String pin;
		}

		public static class Response implements ISwaggerResponse {
			public String mensagem;
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface IPinGerarTokenResetPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public String mensagem;
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface ILotacoesGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String siglaOrgaoQuery;
			public String texto;
			public String idLotacaoIni;
		}

		public static class Response implements ISwaggerResponse {
			public List<Lotacao> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface ILotacoesPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String siglaOrgao;
			public String nome;
			public String sigla;
			public String idLotacaoPaiIni;
			public String idLocalidade;
			public Boolean isAcessoExterno;
		}

		public static class Response implements ISwaggerResponse {
			public String idLotacao;
			public String siglaLotacao;
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface ILotacoesSiglaGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public Lotacao lotacao;
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface IOrgaosGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String idOrgao;
			public String texto;
		}

		public static class Response implements ISwaggerResponse {
			public List<Orgao> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface IOrgaosSiglaGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public Orgao orgao;
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface ICargosIdGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
		}

		public static class Response implements ISwaggerResponse {
			public Cargo cargo;
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface ICargosGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String idOrgao;
			public String nomeCargo;
		}

		public static class Response implements ISwaggerResponse {
			public List<Cargo> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface IFuncoesIdGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
		}

		public static class Response implements ISwaggerResponse {
			public FuncaoConfianca funcaoConfianca;
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface IFuncoesGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String idOrgao;
			public String nomeFuncaoConfianca;
		}

		public static class Response implements ISwaggerResponse {
			public List<FuncaoConfianca> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface ILocalidadesIdGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
		}

		public static class Response implements ISwaggerResponse {
			public Localidade localidade;
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface ILocalidadesGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String idUf;
			public String texto;
		}

		public static class Response implements ISwaggerResponse {
			public List<Localidade> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface IUfsGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public List<Uf> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface IPainelQuadroGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String estilo;
			public String tipoMarca;
		}

		public static class Response implements ISwaggerResponse {
			public List<PainelQuadroItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface IPainelListaGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String idMarcadores;
			public String tipoMarca;
			public String filtroPessoaLotacao;
			public String itensPorPagina;
			public String pagina;
		}

		public static class Response implements ISwaggerResponse {
			public List<PainelListaItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface IAcessosGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public List<AcessoItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface INotificacoesGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sigla;
		}

		public static class Response implements ISwaggerResponse {
			public List<Notificacao> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface IUsuarioGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public Usuario usuario;
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

	public interface IUsuarioSubstituirPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String substituicaoId;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception;
	}

}
