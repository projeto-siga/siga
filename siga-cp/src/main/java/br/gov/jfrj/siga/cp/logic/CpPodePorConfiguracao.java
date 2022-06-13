package br.gov.jfrj.siga.cp.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpTipoLotacao;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.cp.bl.Cp;

public class CpPodePorConfiguracao implements Expression{

	private CpServico cpServico;
	private DpCargo cargo;
	private CpOrgaoUsuario cpOrgaoUsu;
	private DpFuncaoConfianca dpFuncaoConfianca;
	private DpLotacao dpLotacao;
	private DpPessoa dpPessoa;
	private CpTipoLotacao cpTpLotacao;
	private ITipoDeConfiguracao idTpConf;
	private DpPessoa pessoaObjeto;
	private DpLotacao lotacaoObjeto;
	private CpComplexo complexoObjeto;
	private DpCargo cargoObjeto;
	private DpFuncaoConfianca funcaoConfiancaObjeto;
	private CpOrgaoUsuario orgaoObjeto;
	private boolean aceitarPode = true;
	private boolean aceitarDefault = true;
	private boolean aceitarObrigatorio = true;
	
	public CpPodePorConfiguracao(DpPessoa titular, DpLotacao lotaTitular) {
		super();
		this.dpPessoa = titular;
		this.dpLotacao = lotaTitular;
	}
	
	@Override
	public boolean eval() {
		CpSituacaoDeConfiguracaoEnum situacao = Cp.getInstance().getConf().situacaoPorConfiguracao(cpServico, cargo, cpOrgaoUsu,
				dpFuncaoConfianca, dpLotacao, dpPessoa, cpTpLotacao, idTpConf, pessoaObjeto, lotacaoObjeto,
				complexoObjeto, cargoObjeto, funcaoConfiancaObjeto, orgaoObjeto);

		if (situacao == null)
			return false;
		if (aceitarPode && situacao == CpSituacaoDeConfiguracaoEnum.PODE)
			return true;
		if (aceitarDefault && situacao == CpSituacaoDeConfiguracaoEnum.DEFAULT)
			return true;
		if (aceitarObrigatorio && situacao == CpSituacaoDeConfiguracaoEnum.OBRIGATORIO)
			return true;
		return false;
	}
	
	@Override
	public String explain(boolean result) {
		return JLogic.explain(
				"pode" + (idTpConf != null ? " " + idTpConf.getDescr().toLowerCase() : "") + " por configuração",
				result);
	}
	
	public CpPodePorConfiguracao withPessoaObjeto(DpPessoa pessoaObjeto) {
		this.pessoaObjeto = pessoaObjeto;
		return this;
	}
	
	public CpPodePorConfiguracao withIdTpConf(ITipoDeConfiguracao idTpConf) {
		this.idTpConf = idTpConf;
		return this;
	}
	
	public CpPodePorConfiguracao withLotacaoObjeto(DpLotacao lotacaoObjeto) {
		this.lotacaoObjeto = lotacaoObjeto;
		return this;
	}
	
	public CpPodePorConfiguracao withCargoObjeto(DpCargo cargoObjeto) {
		this.cargoObjeto = cargoObjeto;
		return this;
	}
	
	public CpPodePorConfiguracao withFuncaoConfiancaObjeto(DpFuncaoConfianca funcaoConfiancaObjeto) {
		this.funcaoConfiancaObjeto = funcaoConfiancaObjeto;
		return this;
	}
	
	public CpPodePorConfiguracao withOrgaoObjeto(CpOrgaoUsuario orgaoObjeto) {
		this.orgaoObjeto = orgaoObjeto;
		return this;
	}
	
}
