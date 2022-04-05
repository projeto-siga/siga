package br.gov.jfrj.siga.ex.logic;

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
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.ExVia;
import br.gov.jfrj.siga.ex.bl.Ex;

public class ExPodePorConfiguracao implements Expression {
	private CpServico cpServico;
	private ExTipoFormaDoc exTipoFormaDoc;
	private ExPapel exPapel;
	private ExTipoDocumento exTpDoc;
	private ExFormaDocumento exFormaDoc;
	private ExModelo exMod;
	private ExClassificacao exClassificacao;
	private ExVia exVia;
	private ITipoDeMovimentacao exTpMov;
	private DpCargo cargo;
	private CpOrgaoUsuario cpOrgaoUsu;
	private DpFuncaoConfianca dpFuncaoConfianca;
	private DpLotacao dpLotacao;
	private DpPessoa dpPessoa;
	private ExNivelAcesso nivelAcesso;
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

	public ExPodePorConfiguracao(DpPessoa titular, DpLotacao lotaTitular) {
		super();
		this.dpPessoa = titular;
		this.dpLotacao = lotaTitular;
	}

	@Override
	public boolean eval() {
		CpSituacaoDeConfiguracaoEnum situacao = Ex.getInstance().getConf().situacaoPorConfiguracao(cpServico,
				exTipoFormaDoc, exPapel, exTpDoc, exFormaDoc, exMod, exClassificacao, exVia, exTpMov, cargo, cpOrgaoUsu,
				dpFuncaoConfianca, dpLotacao, dpPessoa, nivelAcesso, cpTpLotacao, idTpConf, pessoaObjeto, lotacaoObjeto,
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
				"pode" + (idTpConf != null ? " " + idTpConf.getDescr().toLowerCase() : "") 
				+ (exTpMov != null ? " " + exTpMov.getDescr().toLowerCase() : "") + " por configuração",
				result);
	}

	public ExPodePorConfiguracao withCpServico(CpServico cpServico) {
		this.cpServico = cpServico;
		return this;
	}

	public ExPodePorConfiguracao withExTipoFormaDoc(ExTipoFormaDoc exTipoFormaDoc) {
		this.exTipoFormaDoc = exTipoFormaDoc;
		return this;
	}

	public ExPodePorConfiguracao withExPapel(ExPapel exPapel) {
		this.exPapel = exPapel;
		return this;
	}

	public ExPodePorConfiguracao withExTpDoc(ExTipoDocumento exTpDoc) {
		this.exTpDoc = exTpDoc;
		return this;
	}

	public ExPodePorConfiguracao withExFormaDoc(ExFormaDocumento exFormaDoc) {
		this.exFormaDoc = exFormaDoc;
		return this;
	}

	public ExPodePorConfiguracao withExMod(ExModelo exMod) {
		this.exMod = exMod;
		return this;
	}

	public ExPodePorConfiguracao withExClassificacao(ExClassificacao exClassificacao) {
		this.exClassificacao = exClassificacao;
		return this;
	}

	public ExPodePorConfiguracao withExVia(ExVia exVia) {
		this.exVia = exVia;
		return this;
	}

	public ExPodePorConfiguracao withExTpMov(ITipoDeMovimentacao exTpMov) {
		this.exTpMov = exTpMov;
		return this;
	}

	public ExPodePorConfiguracao withCargo(DpCargo cargo) {
		this.cargo = cargo;
		return this;
	}

	public ExPodePorConfiguracao withCpOrgaoUsu(CpOrgaoUsuario cpOrgaoUsu) {
		this.cpOrgaoUsu = cpOrgaoUsu;
		return this;
	}

	public ExPodePorConfiguracao withDpFuncaoConfianca(DpFuncaoConfianca dpFuncaoConfianca) {
		this.dpFuncaoConfianca = dpFuncaoConfianca;
		return this;
	}

	public ExPodePorConfiguracao withDpLotacao(DpLotacao dpLotacao) {
		this.dpLotacao = dpLotacao;
		return this;
	}

	public ExPodePorConfiguracao withDpPessoa(DpPessoa dpPessoa) {
		this.dpPessoa = dpPessoa;
		return this;
	}

	public ExPodePorConfiguracao withNivelAcesso(ExNivelAcesso nivelAcesso) {
		this.nivelAcesso = nivelAcesso;
		return this;
	}

	public ExPodePorConfiguracao withCpTpLotacao(CpTipoLotacao cpTpLotacao) {
		this.cpTpLotacao = cpTpLotacao;
		return this;
	}

	public ExPodePorConfiguracao withIdTpConf(ITipoDeConfiguracao idTpConf) {
		this.idTpConf = idTpConf;
		return this;
	}

	public ExPodePorConfiguracao withPessoaObjeto(DpPessoa pessoaObjeto) {
		this.pessoaObjeto = pessoaObjeto;
		return this;
	}

	public ExPodePorConfiguracao withLotacaoObjeto(DpLotacao lotacaoObjeto) {
		this.lotacaoObjeto = lotacaoObjeto;
		return this;
	}

	public ExPodePorConfiguracao withComplexoObjeto(CpComplexo complexoObjeto) {
		this.complexoObjeto = complexoObjeto;
		return this;
	}

	public ExPodePorConfiguracao withCargoObjeto(DpCargo cargoObjeto) {
		this.cargoObjeto = cargoObjeto;
		return this;
	}

	public ExPodePorConfiguracao withFuncaoConfiancaObjeto(DpFuncaoConfianca funcaoConfiancaObjeto) {
		this.funcaoConfiancaObjeto = funcaoConfiancaObjeto;
		return this;
	}

	public ExPodePorConfiguracao withOrgaoObjeto(CpOrgaoUsuario orgaoObjeto) {
		this.orgaoObjeto = orgaoObjeto;
		return this;
	}

	public ExPodePorConfiguracao withAceitarPode(boolean f) {
		this.aceitarPode = f;
		return this;
	}

	public ExPodePorConfiguracao withAceitarDefault(boolean f) {
		this.aceitarDefault = f;
		return this;
	}

	public ExPodePorConfiguracao withAceitarObrigatorio(boolean f) {
		this.aceitarObrigatorio = f;
		return this;
	}
};
