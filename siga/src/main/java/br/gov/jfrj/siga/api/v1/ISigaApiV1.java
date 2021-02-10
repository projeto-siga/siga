package br.gov.jfrj.siga.api.v1;

import java.util.Date;
import java.util.List;
import com.crivano.swaggerservlet.ISwaggerMethod;
import com.crivano.swaggerservlet.ISwaggerModel;
import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerResponse;

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

	public class Marcador implements ISwaggerModel {
		public String idMarcador;
		public String descricao;
		public String descricaoDetalhada;
		public Boolean ativo;
		public String tipoAplicacao;
		public String tipoDataPlanejada;
		public String tipoDataLimite;
		public String tipoExibicao;
		public String tipoJustificativa;
		public String tipoInteressado;
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

	public class AcessoItem implements ISwaggerModel {
		public Date datahora;
		public String ip;
	}
	
	public class Notificacao implements ISwaggerModel {
		public String idNotificacao;
		public String titulo;
		public String icone;
		public String resumo;
		public String conteudo;
		public Date dataInicio;
		public Date dataTermino;
		public Boolean sempreMostrar;
	}
	

	public class AutenticarPostRequest implements ISwaggerRequest {
	}

	public class AutenticarPostResponse implements ISwaggerResponse {
		public String token;
	}

	public interface IAutenticarPost extends ISwaggerMethod {
		public void run(AutenticarPostRequest req, AutenticarPostResponse resp) throws Exception;
	}

	public class MarcadoresIdPostRequest implements ISwaggerRequest {
		public String id;
		public String descricao;
		public String descricaoDetalhada;
		public String idGrupoMarcador;
		public String cor;
		public String icone;
		public String tipoAplicacao;
		public String tipoDataPlanejada;
		public String tipoDataLimite;
		public String tipoExibicao;
		public String tipoJustificativa;
		public String tipoInteressado;
	}

	public class MarcadoresIdPostResponse implements ISwaggerResponse {
		public String id;
	}

	public interface IMarcadoresIdPost extends ISwaggerMethod {
		public void run(MarcadoresIdPostRequest req, MarcadoresIdPostResponse resp) throws Exception;
	}

	public class MarcadoresPostRequest implements ISwaggerRequest {
		public String descricao;
		public String descricaoDetalhada;
		public String idGrupoMarcador;
		public String cor;
		public String icone;
		public String tipoAplicacao;
		public String tipoDataPlanejada;
		public String tipoDataLimite;
		public String tipoExibicao;
		public String tipoJustificativa;
		public String tipoInteressado;
	}

	public class MarcadoresPostResponse implements ISwaggerResponse {
		public String id;
	}

	public interface IMarcadoresPost extends ISwaggerMethod {
		public void run(MarcadoresPostRequest req, MarcadoresPostResponse resp) throws Exception;
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

	public class PessoasPinPostRequest implements ISwaggerRequest {
		public String pin;
	}

	public class PessoasPinPostResponse implements ISwaggerResponse {
		public String mensagem;
	}

	public interface IPessoasPinPost extends ISwaggerMethod {
		public void run(PessoasPinPostRequest req, PessoasPinPostResponse resp) throws Exception;
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
	
	public class NotificacoesGetRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class NotificacoesGetResponse implements ISwaggerResponse {
		public List<Notificacao> list;
	}

	public interface INotificacoesGet extends ISwaggerMethod {
		public void run(NotificacoesGetRequest req, NotificacoesGetResponse resp) throws Exception;
	}
}