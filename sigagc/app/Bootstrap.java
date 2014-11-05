import models.GcAcesso;
import models.GcTipoInformacao;
import models.GcTipoMovimentacao;
import models.GcTipoTag;

import org.jboss.logging.Logger;

import play.Play;
import play.db.jpa.JPA;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.vfs.VirtualFile;

@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() throws Exception {

		if (false && !Play.classes.hasClass("br.gov.jfrj.siga.dp.DpPessoa")) {
			System.out.println("\n\n\n*********************** INICIALIZANDO CLASSES *****************************");
			System.out.println(Play.usePrecompiled);
			Play.javaPath.add(VirtualFile.fromRelativePath("/modules/siga-play-module-0.0.1-SNAPSHOT/app"));
			System.out.println(Play.javaPath);

			// Play.classloader
			// .loadApplicationClass("br.gov.jfrj.siga.cp.CpConfiguracao");
			

			System.out.println(Play.classes.getApplicationClass("br.gov.jfrj.siga.dp.CpOrgaoUsuario"));
			System.out.println(Play.classes.getApplicationClass("br.gov.jfrj.siga.dp.DpCargo"));
			System.out.println(Play.classes.getApplicationClass("br.gov.jfrj.siga.dp.DpFuncaoConfianca"));
			System.out.println(Play.classes.getApplicationClass("br.gov.jfrj.siga.dp.DpLotacao"));
			System.out.println(Play.classes.getApplicationClass("br.gov.jfrj.siga.dp.DpPessoa"));

			System.out.println(Play.classes.hasClass("br.gov.jfrj.siga.dp.DpPessoa"));
			System.out.println("classes carregadas...");
		}
		
		if (GcTipoMovimentacao.count() == 0) {
			System.out.println("\n\n\n*** INICIALIZANDO BANCO DE DADOS ***");
			new GcTipoMovimentacao(GcTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO, "Criação").save();
			new GcTipoMovimentacao(GcTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO,"Concluído").save();
			new GcTipoMovimentacao(GcTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO,"Cancelado").save();
			new GcTipoMovimentacao(GcTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_DE_REVISAO,"Solicitação de revisão").save();
			new GcTipoMovimentacao(GcTipoMovimentacao.TIPO_MOVIMENTACAO_REVISADO, "Revisado").save();
			new GcTipoMovimentacao(GcTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICAR,"Notificação").save();
			new GcTipoMovimentacao(GcTipoMovimentacao.TIPO_MOVIMENTACAO_CIENTE,"Ciente").save();
			new GcTipoMovimentacao(GcTipoMovimentacao.TIPO_MOVIMENTACAO_CLASSIFICACAO,"Classificação").save();
			new GcTipoMovimentacao(GcTipoMovimentacao.TIPO_MOVIMENTACAO_INTERESSADO,"Interesse").save();
			new GcTipoMovimentacao(GcTipoMovimentacao.TIPO_MOVIMENTACAO_EDICAO,"Edição").save();
			new GcTipoMovimentacao(GcTipoMovimentacao.TIPO_MOVIMENTACAO_VISITA,"Visita").save();
			new GcTipoMovimentacao(GcTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO,"Cancelamento de movimentação").save();
			new GcTipoMovimentacao(GcTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXAR_ARQUIVO,"Anexação de arquivo").save();
			new GcTipoMovimentacao(GcTipoMovimentacao.TIPO_MOVIMENTACAO_DUPLICAR,"Duplicado").save();
			new GcTipoInformacao(GcTipoInformacao.TIPO_INFORMACAO_REGISTRO_DE_CONHECIMENTO,"Registro de Conhecimento").save();
			new GcTipoTag(GcTipoTag.TIPO_TAG_CLASSIFICACAO, "Classificação").save();
			new GcTipoTag(GcTipoTag.TIPO_TAG_HASHTAG, "Marcador").save();
			new GcTipoTag(GcTipoTag.TIPO_TAG_ANCORA, "Âncora").save();
			new GcAcesso(GcAcesso.ACESSO_PUBLICO, "Público").save();
			new GcAcesso(GcAcesso.ACESSO_ORGAO_USU, "Restrito ao órgão").save();
			new GcAcesso(GcAcesso.ACESSO_LOTACAO_E_SUPERIORES,"Lotação e superiores").save();
			new GcAcesso(GcAcesso.ACESSO_LOTACAO_E_INFERIORES,"Lotação e inferiores").save();
			new GcAcesso(GcAcesso.ACESSO_LOTACAO, "Lotação").save();
			new GcAcesso(GcAcesso.ACESSO_PESSOAL, "Pessoal").save();
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