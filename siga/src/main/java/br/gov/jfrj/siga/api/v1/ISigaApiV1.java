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
		public String idCargoIni;
		public String idExterna;
		public String sigla;
		public String nome;
	}

	public class FuncaoConfianca implements ISwaggerModel {
		public String idFuncaoConfianca;
		public String idFuncaoConfiancaIni;
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

	public class PessoasPostRequest implements ISwaggerRequest {
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

	public class PessoasPostResponse implements ISwaggerResponse {
		public String idPessoa;
		public String siglaPessoa;
	}

	public interface IPessoasPost extends ISwaggerMethod {
		public void run(PessoasPostRequest req, PessoasPostResponse resp) throws Exception;
	}

	public class PessoasSiglaGetRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class PessoasSiglaGetResponse implements ISwaggerResponse {
		public Pessoa pessoa;
	}

	public interface IPessoasSiglaGet extends ISwaggerMethod {
		public void run(PessoasSiglaGetRequest req, PessoasSiglaGetResponse resp) throws Exception;
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

	public class LotacoesSiglaGetRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class LotacoesSiglaGetResponse implements ISwaggerResponse {
		public Lotacao lotacao;
	}

	public interface ILotacoesSiglaGet extends ISwaggerMethod {
		public void run(LotacoesSiglaGetRequest req, LotacoesSiglaGetResponse resp) throws Exception;
	}

	public class OrgaosGetRequest implements ISwaggerRequest {
		public String idOrgao;
		public String texto;
	}

	public class OrgaosGetResponse implements ISwaggerResponse {
		public List<Orgao> list;
	}

	public interface IOrgaosGet extends ISwaggerMethod {
		public void run(OrgaosGetRequest req, OrgaosGetResponse resp) throws Exception;
	}

	public class OrgaosSiglaGetRequest implements ISwaggerRequest {
		public String sigla;
	}

	public class OrgaosSiglaGetResponse implements ISwaggerResponse {
		public Orgao orgao;
	}

	public interface IOrgaosSiglaGet extends ISwaggerMethod {
		public void run(OrgaosSiglaGetRequest req, OrgaosSiglaGetResponse resp) throws Exception;
	}

	public class CargosIdGetRequest implements ISwaggerRequest {
		public String id;
	}

	public class CargosIdGetResponse implements ISwaggerResponse {
		public Cargo cargo;
	}

	public interface ICargosIdGet extends ISwaggerMethod {
		public void run(CargosIdGetRequest req, CargosIdGetResponse resp) throws Exception;
	}

	public class CargosGetRequest implements ISwaggerRequest {
		public String idOrgao;
		public String nomeCargo;
	}

	public class CargosGetResponse implements ISwaggerResponse {
		public List<Cargo> list;
	}

	public interface ICargosGet extends ISwaggerMethod {
		public void run(CargosGetRequest req, CargosGetResponse resp) throws Exception;
	}

	public class FuncoesIdGetRequest implements ISwaggerRequest {
		public String id;
	}

	public class FuncoesIdGetResponse implements ISwaggerResponse {
		public FuncaoConfianca funcaoConfianca;
	}

	public interface IFuncoesIdGet extends ISwaggerMethod {
		public void run(FuncoesIdGetRequest req, FuncoesIdGetResponse resp) throws Exception;
	}

	public class FuncoesGetRequest implements ISwaggerRequest {
		public String idOrgao;
		public String nomeFuncaoConfianca;
	}

	public class FuncoesGetResponse implements ISwaggerResponse {
		public List<FuncaoConfianca> list;
	}

	public interface IFuncoesGet extends ISwaggerMethod {
		public void run(FuncoesGetRequest req, FuncoesGetResponse resp) throws Exception;
	}

	public class AcessosGetRequest implements ISwaggerRequest {
	}

	public class AcessosGetResponse implements ISwaggerResponse {
		public List<AcessoItem> list;
	}

	public interface IAcessosGet extends ISwaggerMethod {
		public void run(AcessosGetRequest req, AcessosGetResponse resp) throws Exception;
	}

	//
	// A PARTIR DAQUI NÃO FOI GERADO PELO SWAGGERSERVLET
	// COPIAR O CÓDIGO DO SWAGGERSERVLET PARA CIMA E MANTER O CODIGO ABAIXO.
	//
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