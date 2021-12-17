package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeRestringirCossignatarioSubscritor extends CompositeExpressionSupport {

	private DpPessoa pessoa;
	private DpLotacao lotacao;
	private DpPessoa pessoaObjeto;
	private DpLotacao lotacaoObjeto;
	private DpCargo cargoObjeto;
	private DpFuncaoConfianca funcaoConfiancaObjeto;
	private CpOrgaoUsuario orgaoObjeto;

	public ExPodeRestringirCossignatarioSubscritor(final DpPessoa pessoa, final DpLotacao lotacao,
			final DpPessoa pessoaObjeto, final DpLotacao lotacaoObjeto, final DpCargo cargoObjeto,
			final DpFuncaoConfianca funcaoConfiancaObjeto, final CpOrgaoUsuario orgaoObjeto) {
		this.pessoa = pessoa;
		this.lotacao = lotacao;
		this.pessoaObjeto = pessoaObjeto;
		this.lotacaoObjeto = lotacaoObjeto;
		this.cargoObjeto = cargoObjeto;
		this.funcaoConfiancaObjeto = funcaoConfiancaObjeto;
		this.orgaoObjeto = orgaoObjeto;
	}

	/**
	 * Retorna se é possível incluir o cossignatario ou subscritor ao documento. Basta não estar eliminado
	 * o documento e não haver configuração impeditiva, o que significa que, tendo
	 * acesso a um documento não eliminado, qualquer usuário pode se cadastrar como
	 * cossignatario ou subscritor.
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	@Override
	protected Expression create() {
		return new ExPodePorConfiguracao(pessoa, lotacao)
				.withIdTpConf(ExTipoDeConfiguracao.RESTRINGIR_COSSIGNATARIO_SUBSCRITOR).withPessoaObjeto(pessoaObjeto)
				.withLotacaoObjeto(lotacaoObjeto).withCargoObjeto(cargoObjeto)
				.withFuncaoConfiancaObjeto(funcaoConfiancaObjeto).withOrgaoObjeto(orgaoObjeto);
	}
}
