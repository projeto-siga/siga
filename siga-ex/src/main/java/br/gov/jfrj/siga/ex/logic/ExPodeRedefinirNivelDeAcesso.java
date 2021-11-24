package br.gov.jfrj.siga.ex.logic;

import java.util.ArrayList;
import java.util.List;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.If;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.cp.logic.CpIgual;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeRedefinirNivelDeAcesso extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private List<ExMovimentacao> listMovJuntada;

	public ExPodeRedefinirNivelDeAcesso(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.doc = mob.doc();
		this.titular = titular;
		this.lotaTitular = lotaTitular;

		listMovJuntada = new ArrayList<ExMovimentacao>();
		if (mob.getDoc().getMobilDefaultParaReceberJuntada() != null) {
			listMovJuntada.addAll(mob.getDoc().getMobilDefaultParaReceberJuntada()
					.getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA));
		}
	}

	/**
	 * Retorna se é possível alterar o nível de accesso do documento. É necessário
	 * apenas que o usuário possa acessar o documento e que não haja configuração
	 * impeditiva. <b>Obs.: Não é verificado se <i>podeMovimentar()</i></b>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	@Override
	protected Expression create() {
		return If.of(

				Or.of(

						new ExEstaPublicadoNoBoletim(doc),

						new ExEstaAgendadaPublicacaoNoDiario(doc)),

				Or.of(

						new ExPodeAtenderPedidoPublicacaoNoDiario(mob, titular, lotaTitular),

						new ExPodeGerenciarPublicacaoNoBoletimPorConfiguracao(mob, titular, lotaTitular)),

				And.of(

						/*
						 * Não permite redefinir acesso para documentos que foram publicados no portal
						 * da transparencia
						 */
						Not.of(new ExTemMovimentacaoNaoCanceladaDoTipo(mob,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_PUBLICACAO_PORTAL_TRANSPARENCIA)),

						Not.of(new ExEstaEliminado(mob)),

						new ExPodeAcessarDocumento(mob, titular, lotaTitular),

						new ExPodeMovimentar(mob, titular, lotaTitular),

						new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
								.withExTpMov(ExTipoMovimentacao.TIPO_MOVIMENTACAO_REDEFINICAO_NIVEL_ACESSO)));
	}
}