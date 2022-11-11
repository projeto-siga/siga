package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.cp.logic.CpIgual;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeCapturarPDF extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private Long idTpDoc;

	public ExPodeCapturarPDF(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular, Long idTpDoc) {
		super();
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
		this.idTpDoc = idTpDoc != null ? idTpDoc : mob.doc().getExTipoDocumento().getId();
	}

	public ExPodeCapturarPDF(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this(mob, titular, lotaTitular, null);
	}

	@Override
	protected Expression create() {
		return And.of(

				Or.of(

						new CpIgual(idTpDoc, "tipo do documento", ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_CAPTURADO,
								"interno capturado"),

						new CpIgual(idTpDoc, "tipo do documento", ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_CAPTURADO,
								"externo capturado"),

						new CpIgual(idTpDoc, "tipo do documento", ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_CAPTURADO_FORMATO_LIVRE,
								"interno capturado formato livre"),

						new CpIgual(idTpDoc, "tipo do documento", ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_CAPTURADO_FORMATO_LIVRE,
								"externo capturado formato livre")),

				Or.of(

						Not.of(new ExEstaFinalizado(mob.doc())),

						And.of(

								Not.of(new ExJaTransferido(mob.doc())),

								Not.of(new ExEstaAssinadoPorTodosOsSignatariosComTokenOuSenha(mob.doc())),

								Not.of(new ExEstaJuntado(mob)),

								Not.of(new ExEstaJuntadoExterno(mob)),

								Not.of(new ExEMobilCancelado(mob)),

								Not.of(new ExEstaAutenticadoComTokenOuSenha(mob.doc())),

								new ExPodePorConfiguracao(titular, lotaTitular)
										.withIdTpConf(ExTipoDeConfiguracao.TROCAR_PDF_CAPTURADOS))));
	}
};
