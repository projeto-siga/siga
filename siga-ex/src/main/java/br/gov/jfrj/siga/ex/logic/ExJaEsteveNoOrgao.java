package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;

public class ExJaEsteveNoOrgao implements Expression {

	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se um documento já esteve num órgão (TRF, JFRJ, JFES), verificando se
	 * alguma movimentação de algum móbil do documento teve lotação atendente
	 * pertencente ao órgão onde está a lotação passada por parâmetro ou se, tendo
	 * sido definida uma pessoa atendente para a movimentação, o órgão a que a
	 * pessoa <i>pertencia*</i> é o órgão da pessoa passada por parâmetro. <br/>
	 * * Rever o restante deste documento, se os verbos estão no tempo correto
	 * 
	 * @param doc
	 * @param titular
	 * @param lotaTitular
	 * @return
	 */
	public ExJaEsteveNoOrgao(ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular) {
		this.doc = doc;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	public boolean eval() {
		for (ExMobil m : doc.getExMobilSet()) {
			for (ExMovimentacao mov : m.getExMovimentacaoSet()) {
				if ((mov.getLotaResp() != null
						&& mov.getLotaResp().getOrgaoUsuario().equivale(lotaTitular.getOrgaoUsuario()))
						|| (mov.getResp() != null
								&& mov.getResp().getOrgaoUsuario().equivale(titular.getOrgaoUsuario())))
					return true;
			}
		}
		return false;
	}

	@Override
	public String explain(boolean result) {
		return doc.getCodigo() + (result ? "" : JLogic.NOT) + " esteve no órgão de "
				+ (titular != null ? titular.getSiglaCompleta() : "")
				+ (titular != null && lotaTitular != null ? "/" : "")
				+ (lotaTitular != null ? lotaTitular.getSiglaCompleta() : "");
	}
};
