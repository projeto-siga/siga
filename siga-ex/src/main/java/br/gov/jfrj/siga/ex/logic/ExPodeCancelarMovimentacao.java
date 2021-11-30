package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.If;
import com.crivano.jlogic.NAnd;
import com.crivano.jlogic.NOr;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;

public class ExPodeCancelarMovimentacao extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private ExMovimentacao exUltMov;
	private ExMovimentacao exUltMovNaoCanc;
	private ExMobil mobRef;
	private ExMovimentacao ultimaMovimentacaoDaReferencia;

	public ExPodeCancelarMovimentacao(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
		this.exUltMov = mob.getUltimaMovimentacao();
		this.exUltMovNaoCanc = mob.getUltimaMovimentacaoNaoCancelada();
		if (exUltMovNaoCanc != null)
			mobRef = exUltMovNaoCanc.getExMobilRef();
		if (mobRef != null)
			ultimaMovimentacaoDaReferencia = exUltMovNaoCanc.getExMobilRef().getUltimaMovimentacao();

	}

	@Override
	protected Expression create() {

		return And.of(
				// Não deixa cancelar movimentação de um mobil diferente de geral quando um
				// documento está sem efeito.
				Not.of(And.of(Not.of(new ExEMobilGeral(mob)), new ExEstaSemEfeito(mob.doc()))),

				Not.of(new ExEstaEliminado(mob)),

				new ExTemMovimentacaoNaoCancelada(mob),

				Not.of(new ExEMobilCancelado(mob)),

				// Só deixa cancelar movimentação de tornar documento sem efeito, se o titular
				// for o subscritor do documento
				// Também não é permitido os cosignatários cancelar essa movimentação
				NAnd.of(

						new ExEMobilGeral(mob),

						new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_TORNAR_SEM_EFEITO),

						Not.of(new ExMovimentacaoESubscritor(exUltMovNaoCanc, titular))),

				NOr.of(

						// Não deixa cancelar apensação ou desapensação
						new ExMovimentacaoEDoTipo(exUltMovNaoCanc, ExTipoMovimentacao.TIPO_MOVIMENTACAO_APENSACAO),

						new ExMovimentacaoEDoTipo(exUltMovNaoCanc, ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESAPENSACAO),

						new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_EM_EDITAL_DE_ELIMINACAO),

						new ExMovimentacaoEDoTipo(exUltMovNaoCanc, ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO),

						new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_PENDENCIA_DE_ANEXACAO),

						new ExMovimentacaoEDoTipo(exUltMovNaoCanc, ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO),

						// Não deixa cancelar assinatura
						new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_REGISTRO_ASSINATURA_DOCUMENTO),

						new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO),

						new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA),

						new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO),

						new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO),

						new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA),

						new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA),

						new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICACAO_PUBL_BI),

						new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_PUBLICACAO_BOLETIM),

						new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_DISPONIBILIZACAO),

						new ExMovimentacaoEDoTipo(exUltMovNaoCanc, ExTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO)),

				// Não deixa cancelar a atualização (por enquanto, só se resultar da assinatura)
				NAnd.of(

						new ExMovimentacaoEDoTipo(exUltMovNaoCanc, ExTipoMovimentacao.TIPO_MOVIMENTACAO_ATUALIZACAO),

						Or.of(

								new ExMovimentacaoRefEDoTipo(exUltMovNaoCanc,
										ExTipoMovimentacao.TIPO_MOVIMENTACAO_REGISTRO_ASSINATURA_DOCUMENTO),

								new ExMovimentacaoRefEDoTipo(exUltMovNaoCanc,
										ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO))),

				// Não deixa cancelar alguns tipo se não for o cadastrante
				If.of(

						Or.of(

								new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
										ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA),

								new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
										ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA),

								new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
										ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO_TRANSFERENCIA),

								new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
										ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA),

								new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
										ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA),

								new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
										ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO_TRANSITORIO),

								new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
										ExTipoMovimentacao.TIPO_MOVIMENTACAO_REGISTRO_ASSINATURA_DOCUMENTO),

								new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
										ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO),

								new ExMovimentacaoEDoTipo(exUltMovNaoCanc,
										ExTipoMovimentacao.TIPO_MOVIMENTACAO_SOLICITACAO_DE_ASSINATURA)),

						new ExMovimentacaoELotaTitular(exUltMovNaoCanc, lotaTitular),

						new ExEstaResponsavel(mob, titular, lotaTitular)),

				// Não deixa cancelar juntada quando o documento está juntado a um
				// expediente/processo que já sofreu outra movimentação
				NAnd.of(

						new ExMovimentacaoEDoTipo(exUltMovNaoCanc, ExTipoMovimentacao.TIPO_MOVIMENTACAO_ATUALIZACAO),

						NOr.of(

								new ExEstaArquivado(mobRef),

								And.of(

										new ExMovimentacaoEDoTipo(ultimaMovimentacaoDaReferencia,
												ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANOTACAO),

										new ExMovimentacaoEPosterior(ultimaMovimentacaoDaReferencia, exUltMovNaoCanc)),

								// Verifica se o mobil de referência já recebeu outras movimentações depois da
								// movimentação que vai ser cancelada.
								new ExMovMobRefRecebeuMovimentacoesPosteriores(exUltMovNaoCanc))),

				Or.of(

						// Verifica se a última movimentação não cancelada é agendamento de publicação
						// no DJE
						And.of(

								new ExMovimentacaoEDoTipo(ultimaMovimentacaoDaReferencia,
										ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO),

								new ExMobPodeAtenderPedidoPublicacao(mob, titular, lotaTitular)),

						And.of(

								Not.of(new ExMobHaCancelamentoDeMovimentacaoAnterior(mob)),

								Not.of(new ExMobTemMovimentacaoPosteriorEmQualquerVia(mob)),

								// Não deixa desfazer os antigos arquivamentos feitos em volume de processo
								NAnd.of(

										new ExMovimentacaoEDoTipo(ultimaMovimentacaoDaReferencia,
												ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE),

										new ExPodeDesarquivarCorrente(mob, titular, lotaTitular)),

								NAnd.of(

										new ExMovimentacaoEDoTipo(ultimaMovimentacaoDaReferencia,
												ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO),

										new ExPodeCancelarAnexo(mob, exUltMovNaoCanc, titular, lotaTitular)),

								new ExPodeMovimentarPorConfiguracao(
										ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO, titular,
										lotaTitular))));

	}
}
