import models.GcTipoInformacao;
import models.GcTipoMovimentacao;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() throws Exception {

		if (GcTipoMovimentacao
				.findById(GcTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO) == null)
			new GcTipoMovimentacao(
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO, "Criação")
					.save();

		if (GcTipoMovimentacao
				.findById(GcTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO) == null)
			new GcTipoMovimentacao(
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO,
					"Concluído").save();

		if (GcTipoMovimentacao
				.findById(GcTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO) == null)
			new GcTipoMovimentacao(
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO,
					"Cancelado").save();

		if (GcTipoMovimentacao
				.findById(GcTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_DE_REVISAO) == null)
			new GcTipoMovimentacao(
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_DE_REVISAO,
					"Solicitação de revisão").save();

		if (GcTipoMovimentacao
				.findById(GcTipoMovimentacao.TIPO_MOVIMENTACAO_REVISADO) == null)
			new GcTipoMovimentacao(
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_REVISADO, "Revisado")
					.save();

		if (GcTipoMovimentacao
				.findById(GcTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_DE_CIENCIA) == null)
			new GcTipoMovimentacao(
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_DE_CIENCIA,
					"Solicitação de ciência").save();

		if (GcTipoMovimentacao
				.findById(GcTipoMovimentacao.TIPO_MOVIMENTACAO_CIENTE) == null)
			new GcTipoMovimentacao(GcTipoMovimentacao.TIPO_MOVIMENTACAO_CIENTE,
					"Ciente").save();

		if (GcTipoMovimentacao
				.findById(GcTipoMovimentacao.TIPO_MOVIMENTACAO_CLASSIFICACAO) == null)
			new GcTipoMovimentacao(
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_CLASSIFICACAO,
					"Classificação").save();

		if (GcTipoMovimentacao
				.findById(GcTipoMovimentacao.TIPO_MOVIMENTACAO_DEFINICAO_DE_PERFIL) == null)
			new GcTipoMovimentacao(
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_DEFINICAO_DE_PERFIL,
					"Definição de perfil").save();

		if (GcTipoMovimentacao
				.findById(GcTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_TITULO) == null)
			new GcTipoMovimentacao(
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_TITULO,
					"Definição de título").save();

		if (GcTipoMovimentacao
				.findById(GcTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_CONTEUDO) == null)
			new GcTipoMovimentacao(
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_CONTEUDO,
					"Definição de conteúdo").save();

		if (GcTipoInformacao.find("nome_tipo_informacao = ?",
				"Registro de Conhecimento").first() == null) {
			GcTipoInformacao tinf = new GcTipoInformacao();
			tinf.nome = "Registro de Conhecimento";
			tinf.save();
		}

		// CpTipoMarcador cpTipoMarcador = CpTipoMarcador.findById(1L);
		// CpTipoMarcador cpTipoMarcador = new CpTipoMarcador();
		// cpTipoMarcador.setIdTpMarcador(1L);
		//
		// if (CpMarcador.findById(CpMarcador.MARCADOR_EM_ELABORACAO) == null)
		// new CpMarcador(CpMarcador.MARCADOR_EM_ELABORACAO, "Em Elaboração",
		// cpTipoMarcador).save();
		//
		// if (CpMarcador.findById(CpMarcador.MARCADOR_CANCELADO) == null)
		// new CpMarcador(CpMarcador.MARCADOR_CANCELADO, "Cancelado",
		// cpTipoMarcador).save();
		//
		// if (CpMarcador.findById(CpMarcador.MARCADOR_COMO_GESTOR) == null)
		// new CpMarcador(CpMarcador.MARCADOR_COMO_GESTOR, "Como Gestor",
		// cpTipoMarcador).save();
		//
		// if (CpMarcador.findById(CpMarcador.MARCADOR_COMO_INTERESSADO) ==
		// null)
		// new CpMarcador(CpMarcador.MARCADOR_COMO_INTERESSADO,
		// "Como Interessado", cpTipoMarcador).save();
		//
		// if (CpMarcador.findById(CpMarcador.MARCADOR_ATIVO) == null)
		// new CpMarcador(CpMarcador.MARCADOR_ATIVO, "Ativo", cpTipoMarcador)
		// .save();
		//
		// if (CpMarcador.findById(CpMarcador.MARCADOR_NOVO) == null)
		// new CpMarcador(CpMarcador.MARCADOR_NOVO, "Novo", cpTipoMarcador)
		// .save();
		//
		// if (CpMarcador.findById(CpMarcador.MARCADOR_POPULAR) == null)
		// new CpMarcador(CpMarcador.MARCADOR_POPULAR, "Popular",
		// cpTipoMarcador).save();
		//
		// if (CpMarcador.findById(CpMarcador.MARCADOR_REVISAR) == null)
		// new CpMarcador(CpMarcador.MARCADOR_REVISAR, "Revisar",
		// cpTipoMarcador).save();
		//
		// if (CpMarcador.findById(CpMarcador.MARCADOR_TOMAR_CIENCIA) == null)
		// new CpMarcador(CpMarcador.MARCADOR_TOMAR_CIENCIA, "Tomar Ciência",
		// cpTipoMarcador).save();
	}
}