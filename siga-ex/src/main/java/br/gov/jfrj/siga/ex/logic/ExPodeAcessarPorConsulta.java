package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.bl.AcessoConsulta;

public class ExPodeAcessarPorConsulta implements Expression {

	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeAcessarPorConsulta(ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular) {
		this.doc = doc;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	public boolean eval() {
		AcessoConsulta ac = new AcessoConsulta(titular == null ? 0L : titular.getIdInicial(),
				lotaTitular == null ? 0 : lotaTitular.getIdInicial(),
				titular == null ? 0L : titular.getOrgaoUsuario().getId(),
				lotaTitular == null ? 0L : lotaTitular.getOrgaoUsuario().getId());
		return ac.podeAcessar(doc, titular, lotaTitular);
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("acesso permitido", result);
	}
}