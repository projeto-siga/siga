/**
 * 
 */
package br.gov.jfrj.siga.vraptor.builder;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.CpConfiguracaoBuilder;
import br.gov.jfrj.siga.vraptor.ExClassificacaoSelecao;

public final class ExConfiguracaoBuilder extends CpConfiguracaoBuilder<ExConfiguracaoBuilder, ExConfiguracao, ExDao> {

	public static final Integer ORGAO_INTEGRADO = 2;
	public static final Integer MATRICULA = 1;

	private Long idTpMov;
	private Long idTpDoc;
	private Long idMod;
	private Long idFormaDoc;
	private Long idTpFormaDoc;
	private Long idNivelAcesso;
	private Long idPapel;
	private ExClassificacaoSelecao classificacaoSel;
	private Integer tipoPublicador;

	public ExConfiguracaoBuilder() {
		super(ExConfiguracao.class, ExDao.getInstance());
		this.classificacaoSel = new ExClassificacaoSelecao();
		this.tipoPublicador = ORGAO_INTEGRADO;
	}

	public ExConfiguracao construir() {
		ExConfiguracao config = instanciarConfiguracao();
		return construir(config);
	}

	@Override
	public ExConfiguracao construir(ExConfiguracao config) {
		super.construir(config);

		ExDao dao = ExDao.getInstance();

		if (idTpMov != null && idTpMov != 0) {
			config.setExTipoMovimentacao(dao.consultar(idTpMov, ExTipoMovimentacao.class, false));
		} else {
			config.setExTipoMovimentacao(null);
		}

		if (idTpDoc != null && idTpDoc != 0) {
			config.setExTipoDocumento(dao.consultar(idTpDoc, ExTipoDocumento.class, false));
		} else {
			config.setExTipoDocumento(null);
		}

		if (idMod != null && idMod != 0) {
			config.setExModelo(dao.consultar(idMod, ExModelo.class, false));
			config.setExFormaDocumento(null);
			config.setExTipoFormaDoc(null);
		} else {
			config.setExModelo(null);
			if (idFormaDoc != null && idFormaDoc != 0) {
				config.setExFormaDocumento(dao.consultar(idFormaDoc, ExFormaDocumento.class, false));
				config.setExTipoFormaDoc(null);
			} else {
				config.setExFormaDocumento(null);
				if (idTpFormaDoc != null && idTpFormaDoc != 0) {
					config.setExTipoFormaDoc(dao.consultar(idTpFormaDoc, ExTipoFormaDoc.class, false));
				} else
					config.setExTipoFormaDoc(null);
			}
		}

		if (idNivelAcesso != null && idNivelAcesso != 0) {
			config.setExNivelAcesso(dao.consultar(idNivelAcesso, ExNivelAcesso.class, false));
		} else {
			config.setExNivelAcesso(null);
		}

		if (idPapel != null && idPapel != 0) {
			config.setExPapel(dao.consultar(idPapel, ExPapel.class, false));
		} else {
			config.setExPapel(null);
		}

		if (classificacaoSel != null && classificacaoSel.getId() != null) {
			config.setExClassificacao(dao.consultar(classificacaoSel.getId(), ExClassificacao.class, false));
		} else {
			config.setExClassificacao(null);
		}

		if (getPessoaSel() != null && getPessoaSel().getId() != null
				&& (tipoPublicador == null || ExConfiguracaoBuilder.isTipoMatricula(tipoPublicador))) {
			config.setDpPessoa(dao.consultar(getPessoaSel().getId(), DpPessoa.class, false));
		} else {
			config.setDpPessoa(null);
		}

		if (getLotacaoSel() != null && getLotacaoSel().getId() != null
				&& (tipoPublicador == null || ExConfiguracaoBuilder.isTipoOrgaoIntegrado(tipoPublicador))) {
			config.setLotacao(dao.consultar(getLotacaoSel().getId(), DpLotacao.class, false));
		} else {
			config.setLotacao(null);
		}

		return config;
	}

	public static boolean isTipoMatricula(Integer tipo) {
		return MATRICULA.equals(tipo);
	}

	public static boolean isTipoOrgaoIntegrado(Integer tipo) {
		return ORGAO_INTEGRADO.equals(tipo);
	}

	public Long getIdTpMov() {
		return idTpMov;
	}

	public Long getIdTpDoc() {
		return idTpDoc;
	}

	public Long getIdMod() {
		return idMod;
	}

	public Long getIdFormaDoc() {
		return idFormaDoc;
	}

	public Long getIdTpFormaDoc() {
		return idTpFormaDoc;
	}

	public Long getIdNivelAcesso() {
		return idNivelAcesso;
	}

	public Long getIdPapel() {
		return idPapel;
	}

	public ExClassificacaoSelecao getClassificacaoSel() {
		return classificacaoSel;
	}

	public ExConfiguracaoBuilder setIdTpMov(Long idTpMov) {
		this.idTpMov = idTpMov;
		return this;
	}

	public ExConfiguracaoBuilder setIdTpDoc(Long idTpDoc) {
		this.idTpDoc = idTpDoc;
		return this;
	}

	public ExConfiguracaoBuilder setIdMod(Long idMod) {
		this.idMod = idMod;
		return this;
	}

	public ExConfiguracaoBuilder setIdFormaDoc(Long idFormaDoc) {
		this.idFormaDoc = idFormaDoc;
		return this;
	}

	public ExConfiguracaoBuilder setIdTpFormaDoc(Long idTpFormaDoc) {
		this.idTpFormaDoc = idTpFormaDoc;
		return this;
	}

	public ExConfiguracaoBuilder setIdNivelAcesso(Long idNivelAcesso) {
		this.idNivelAcesso = idNivelAcesso;
		return this;
	}

	public ExConfiguracaoBuilder setIdPapel(Long idPapel) {
		this.idPapel = idPapel;
		return this;
	}

	public ExConfiguracaoBuilder setClassificacaoSel(ExClassificacaoSelecao classificacaoSel) {
		this.classificacaoSel = classificacaoSel;
		return this;
	}

	public ExConfiguracaoBuilder setTipoPublicador(Integer tipoPublicador) {
		this.tipoPublicador = tipoPublicador;
		return this;
	}
}
