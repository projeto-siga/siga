package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeAssinar extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeAssinar(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		if (mob.isGeralDeProcesso() && mob.doc().isFinalizado()) 
			mob = mob.doc().getUltimoVolumeOuGeral();
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/*
	 * Retorna se é possível assinar digitalmente o documento a que pertence o móbil
	 * passado por parâmetro, conforme as seguintes condições: <ul> <li>Documento
	 * não pode ser processo interno importado</li> <li>Usuário tem de ser
	 * cossignatário do documento ou subscritor ou [cadastrante, caso o documento
	 * seja externo], ou <i>podeMovimentar()</i> tem de ser verdadeiro para o móbil
	 * / usuário</li> <li>Documento tem de estar finalizado</li> <li>Documento não
	 * pode estar cancelado</li> <li>Documento não pode estar em algum arquivo nem
	 * eliminado</li> <li>Não pode haver configuração impeditiva</li> </ul>
	 * 
	 * @param titular
	 * 
	 * @param lotaTitular
	 * 
	 * @param mob
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	@Override
	protected Expression create() {
		return And.of(

				Not.of(new ExEstaCancelado(mob.doc())),

				Not.of(new ExEstaSemEfeito(mob.doc())),

				Not.of(new ExEExterno(mob.doc())),

				// Edson: este isEletronico() está aqui porque o físico deixa de estar pendente
				// de
				// assinatura logo que a primeira pessoa assina. Porém, isso não significa que
				// os demais
				// cossignatários não podem mais assinar
				Or.of(

						Not.of(new ExEEletronico(mob.doc())),

						new ExEstaPendenteDeAssinatura(mob.doc())),

				Or.of(

						Not.of(new ExEProcesso(mob.doc())),

						Not.of(new ExEInternoFolhaDeRosto(mob.doc())),

						Not.of(new ExEstaComPrazoDeAssinaturaVencido(mob.doc())),

						Not.of(new ExEstaArquivado(mob)),

						Not.of(new ExEstaEliminado(mob)),

						Not.of(new ExEstaPendenteDeColaboracao(mob)),

						new ExTemSubscritor(mob.doc())),

				Not.of(And.of(

						new ExEExternoCapturado(mob.doc()),

						Not.of(new ExEstaAutenticadoComTokenOuSenha(mob.doc())))),

				Or.of(
						And.of(
								new ExEstaOrdenadoAssinatura(mob.doc()),

								new ExEAssinanteAtual(mob.doc(), titular)),
						
						And.of(
								Not.of(new ExEstaOrdenadoAssinatura(mob.doc())),

								new ExEFisico(mob.doc()),

								new ExEstaFinalizado(mob.doc()),

								Or.of(

										new ExESubscritor(mob.doc(), titular),

										new ExECossignatario(mob.doc(), titular))),

						And.of(
								Not.of(new ExEstaOrdenadoAssinatura(mob.doc())),
								
								Or.of(

										new ExESubscritor(mob.doc(), titular),

										And.of(new ExEExterno(mob.doc()), new ExECadastrante(mob.doc(), titular)),

										And.of(

												new ExECossignatario(mob.doc(), titular),

												new ExEstaPendenteDeAssinatura(mob.doc()),

												Or.of(

														new ExEstaAssinadoPeloSubscritorComTokenOuSenha(mob.doc()),

														new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(
																ExTipoDeConfiguracao.COSIGNATARIO_ASSINAR_ANTES_SUBSCRITOR))),

										new ExPodeMovimentar(mob, titular, lotaTitular)),

								Or.of(

										new ExEstaFinalizado(mob.doc()),

										new ExPodeFinalizar(mob.doc(), titular, lotaTitular)),

								new ExPodeMovimentarPorConfiguracao(
										ExTipoDeMovimentacao.ASSINATURA_DIGITAL_DOCUMENTO, titular,
										lotaTitular))));

	}

}