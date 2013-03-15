import br.gov.jfrj.siga.dp.DpPessoa;
import models.GcInformacao;
import models.GcTipoInformacao;
import models.GcTipoMovimentacao;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.vfs.VirtualFile;

@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() throws Exception {

		if (GcTipoMovimentacao.count() == 0) {
			System.out.println("\n\n\n*** INICIALIZANDO BANCO DE DADOS ***");
			new GcTipoMovimentacao(
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO, "Criação")
					.save();

			new GcTipoMovimentacao(
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO,
					"Concluído").save();

			new GcTipoMovimentacao(
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO,
					"Cancelado").save();

			new GcTipoMovimentacao(
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_DE_REVISAO,
					"Solicitação de revisão").save();

			new GcTipoMovimentacao(
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_REVISADO, "Revisado")
					.save();

			new GcTipoMovimentacao(
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICAR,
					"Notificação").save();

			new GcTipoMovimentacao(GcTipoMovimentacao.TIPO_MOVIMENTACAO_CIENTE,
					"Ciente").save();

			new GcTipoMovimentacao(
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_CLASSIFICACAO,
					"Classificação").save();

			new GcTipoMovimentacao(
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_INTERESSADO,
					"Interesse").save();

			new GcTipoMovimentacao(GcTipoMovimentacao.TIPO_MOVIMENTACAO_EDICAO,
					"Edição").save();

			new GcTipoMovimentacao(GcTipoMovimentacao.TIPO_MOVIMENTACAO_VISITA,
					"Visita").save();

			new GcTipoMovimentacao(
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO,
					"Cancelamento de movimentação").save();

			GcTipoInformacao tinf = new GcTipoInformacao();
			tinf.nome = "Registro de Conhecimento";
			tinf.save();
		}

		if (false && !Play.classes.hasClass("br.gov.jfrj.siga.dp.DpPessoa")) {
			System.out
					.println("\n\n\n*********************** INICIALIZANDO CLASSES *****************************");
			System.out.println(Play.usePrecompiled);
			Play.javaPath.add(VirtualFile.fromRelativePath("../siga-cp/src/"));
			System.out.println(Play.javaPath);

			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.cp.CpConfiguracao");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.cp.CpGrupo");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.cp.CpGrupoDeEmail");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.cp.CpIdentidade");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.cp.CpModelo");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.cp.CpPapel");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.cp.CpPerfil");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.cp.CpPerfilJEE");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.cp.CpServico");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.cp.CpSituacaoConfiguracao");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.cp.CpTipoGrupo");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.cp.CpTipoIdentidade");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.cp.CpTipoPapel");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.cp.CpTipoServico");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.dp.CpAplicacaoFeriado");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.dp.CpFeriado");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.dp.CpLocalidade");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.dp.CpMarca");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.dp.CpMarcador");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.dp.CpOcorrenciaFeriado");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.dp.CpOrgao");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.dp.CpPersonalizacao");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.dp.CpTipoLotacao");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.dp.CpTipoMarca");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.dp.CpTipoMarcador");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.dp.CpTipoPessoa");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.cp.CpTipoConfiguracao");
			// Play.classloader.loadApplicationClass("br.gov.jfrj.siga.dp.CpUF");
			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.dp.DpSubstituicao");

			Play.classes
					.getApplicationClass("br.gov.jfrj.siga.dp.CpOrgaoUsuario");
			Play.classes.getApplicationClass("br.gov.jfrj.siga.dp.DpCargo");
			Play.classes
					.getApplicationClass("br.gov.jfrj.siga.dp.DpFuncaoConfianca");
			Play.classes.getApplicationClass("br.gov.jfrj.siga.dp.DpLotacao");
			Play.classes.getApplicationClass("br.gov.jfrj.siga.dp.DpPessoa");

			System.out.println(Play.classes
					.hasClass("br.gov.jfrj.siga.dp.DpPessoa"));
			System.out.println("classes carregadas...");
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