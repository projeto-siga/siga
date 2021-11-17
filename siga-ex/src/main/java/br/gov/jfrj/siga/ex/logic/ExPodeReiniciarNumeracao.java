package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.cp.logic.CpNaoENulo;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeReiniciarNumeracao extends CompositeExpressionSupport {

	private ExDocumento doc;
	private CpOrgaoUsuario orgaoUsuario;
	private ExFormaDocumento formaDocumento;

	public ExPodeReiniciarNumeracao(CpOrgaoUsuario orgaoUsuario, ExFormaDocumento formaDocumento) {
		super();
		this.orgaoUsuario = orgaoUsuario;
		this.formaDocumento = formaDocumento;
	}

	public ExPodeReiniciarNumeracao(ExDocumento doc) {
		super();
		this.doc = doc;
		this.orgaoUsuario = doc.getOrgaoUsuario();
		this.formaDocumento = doc.getExFormaDocumento();
	}

	@Override
	protected Expression create() {
		return And.of(

				new CpNaoENulo(orgaoUsuario, "órgão usuário do documento"),

				new CpNaoENulo(formaDocumento, "espécie do documento"),

				new ExPodePorConfiguracao(null, null).withCpOrgaoUsu(orgaoUsuario).withExFormaDoc(formaDocumento)
						.withIdTpConf(ExTipoDeConfiguracao.REINICIAR_NUMERACAO_TODO_ANO));
	}
};
