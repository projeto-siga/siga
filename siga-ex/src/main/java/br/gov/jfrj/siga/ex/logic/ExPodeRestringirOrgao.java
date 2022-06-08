package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeRestringirOrgao extends CompositeExpressionSupport{

	private DpPessoa pessoa;
	private DpLotacao lotacao;
	private CpOrgaoUsuario orgao;
	
	public ExPodeRestringirOrgao(final DpPessoa pessoa, final DpLotacao lotacao, final CpOrgaoUsuario orgao) {
		this.pessoa = pessoa;
		this.lotacao = lotacao;
		this.orgao = orgao;
	}
	 
	@Override  
	protected Expression create() {
		return new ExPodePorConfiguracao(pessoa, lotacao) 
				.withIdTpConf(ExTipoDeConfiguracao.RESTRINGIR_VINCULACAO_DO_ORGAO_NO_CAMPO_BUSCAR).withPessoaObjeto(pessoa)
				.withLotacaoObjeto(lotacao).withCargoObjeto(pessoa.getCargo())
				.withFuncaoConfiancaObjeto(pessoa.getFuncaoConfianca()).withOrgaoObjeto(orgao);
	} 
	
}
