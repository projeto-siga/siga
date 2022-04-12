package br.gov.jfrj.siga.sr.api.v1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.crivano.swaggerservlet.ISwaggerMethod;
import com.crivano.swaggerservlet.ISwaggerModel;
import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerResponse;

public interface ISrApiV1 {
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

	public static class Acao implements ISwaggerModel {
		public String nome;
		public String icone;
		public Boolean ativa;
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

		public void run(Request req, Response resp, SrApiV1Context ctx) throws Exception;
	}

	public interface IListaGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String idMarcador;
			public String filtroPessoaLotacao;
		}

		public static class Response implements ISwaggerResponse {
			public List<ListaItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, SrApiV1Context ctx) throws Exception;
	}

	public interface IQuadroGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String estilo;
		}

		public static class Response implements ISwaggerResponse {
			public List<QuadroItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, SrApiV1Context ctx) throws Exception;
	}

}
