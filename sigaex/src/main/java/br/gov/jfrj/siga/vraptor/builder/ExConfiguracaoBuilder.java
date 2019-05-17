/**
 * 
 */
package br.gov.jfrj.siga.vraptor.builder;

import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.model.DpCargoSelecao;
import br.gov.jfrj.siga.cp.model.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpTipoLotacao;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
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
import br.gov.jfrj.siga.vraptor.ExClassificacaoSelecao;

public final class ExConfiguracaoBuilder {

	public static final Integer ORGAO_INTEGRADO = 2;
	public static final Integer MATRICULA = 1;
	private Long idOrgaoUsu;
	private Long idTpMov;
	private Long idTpDoc;
	private Long idMod;
	private Long idFormaDoc; 
	private Long idTpFormaDoc;
	private Long idNivelAcesso; 
	private Long idPapel; 
	private Long idSituacao; 
	private Long idTpConfiguracao;
	private DpPessoaSelecao pessoaSel; 
	private DpLotacaoSelecao lotacaoSel; 
	private DpCargoSelecao cargoSel; 
	private DpFuncaoConfiancaSelecao funcaoSel; 
	private ExClassificacaoSelecao classificacaoSel;
	private DpPessoaSelecao pessoaObjetoSel; 
	private DpLotacaoSelecao lotacaoObjetoSel; 
	private DpCargoSelecao cargoObjetoSel; 
	private DpFuncaoConfiancaSelecao funcaoObjetoSel; 
	private Long idOrgaoObjeto;
	private Long id;
	private Integer tipoPublicador;
	private Long idTpLotacao;

	private ExConfiguracaoBuilder() {
		this.pessoaSel = new DpPessoaSelecao(); 
		this.lotacaoSel = new DpLotacaoSelecao(); 
		this.cargoSel = new DpCargoSelecao(); 
		this.funcaoSel = new DpFuncaoConfiancaSelecao(); 
		this.classificacaoSel = new ExClassificacaoSelecao();
		this.tipoPublicador = ORGAO_INTEGRADO;
	}

	public static ExConfiguracaoBuilder novaInstancia() {
		return new ExConfiguracaoBuilder();
	}

	public ExConfiguracao construir(final ExDao dao) {
		ExConfiguracao config = instanciarExConfiguracao(dao);
		
		if (idOrgaoUsu != null && idOrgaoUsu != 0) {
			config.setOrgaoUsuario(dao.consultar(idOrgaoUsu,
					CpOrgaoUsuario.class, false));
		} else
			config.setOrgaoUsuario(null);

		if (idTpMov != null && idTpMov != 0) {
			config.setExTipoMovimentacao(dao.consultar(
					idTpMov, ExTipoMovimentacao.class, false));
		} else
			config.setExTipoMovimentacao(null);

		if (idTpDoc != null && idTpDoc != 0) {
			config.setExTipoDocumento(dao.consultar(idTpDoc,
					ExTipoDocumento.class, false));
		} else
			config.setExTipoDocumento(null);

		if (idMod != null && idMod != 0) {
			config.setExModelo(dao.consultar(idMod, ExModelo.class, false));
			config.setExFormaDocumento(null);
			config.setExTipoFormaDoc(null);
		} else {
			config.setExModelo(null);
			if (idFormaDoc != null && idFormaDoc != 0) {
				config.setExFormaDocumento(dao.consultar(
						idFormaDoc, ExFormaDocumento.class, false));
				config.setExTipoFormaDoc(null);
			} else {
				config.setExFormaDocumento(null);
				if (idTpFormaDoc != null && idTpFormaDoc != 0) {
					config.setExTipoFormaDoc(dao.consultar(
							idTpFormaDoc, ExTipoFormaDoc.class, false));
				} else
					config.setExTipoFormaDoc(null);
			}
		}

		if (idNivelAcesso != null && idNivelAcesso != 0) {
			config.setExNivelAcesso(dao.consultar(idNivelAcesso,
					ExNivelAcesso.class, false));
		} else
			config.setExNivelAcesso(null);

		if (idPapel != null && idPapel != 0) {
			config.setExPapel(dao.consultar(idPapel,
					ExPapel.class, false));
		} else
			config.setExNivelAcesso(null);

		if (idSituacao != null && idSituacao != 0) {
			config.setCpSituacaoConfiguracao(dao.consultar(idSituacao,
					CpSituacaoConfiguracao.class, false));
		} else
			config.setCpSituacaoConfiguracao(null);

		if (idTpConfiguracao != null && idTpConfiguracao != 0) {
			config.setCpTipoConfiguracao(dao.consultar(idTpConfiguracao,
					CpTipoConfiguracao.class, false));
		} else
			config.setCpTipoConfiguracao(null);

		if (pessoaSel != null && pessoaSel.getId() != null && (tipoPublicador == null || ExConfiguracaoBuilder.isTipoMatricula(tipoPublicador))) {
			config.setDpPessoa(dao.consultar(pessoaSel.getId(), DpPessoa.class, false));
		} else
			config.setDpPessoa(null);

		if (lotacaoSel != null && lotacaoSel.getId() != null && (tipoPublicador == null || ExConfiguracaoBuilder.isTipoOrgaoIntegrado(tipoPublicador))) {
			config.setLotacao(dao.consultar(lotacaoSel.getId(), DpLotacao.class, false));
		} else
			config.setLotacao(null);

		if (idTpLotacao != null && idTpLotacao != 0) {
			config.setCpTipoLotacao(dao.consultar(idTpLotacao,
					CpTipoLotacao.class, false));
		} else
			config.setCpTipoLotacao(null);

		if (cargoSel != null && cargoSel.getId() != null) {
			config.setCargo(dao.consultar(cargoSel.getId(), DpCargo.class, false));
		} else
			config.setCargo(null);

		if (funcaoSel != null && funcaoSel.getId() != null) {
			config.setFuncaoConfianca(dao.consultar(funcaoSel.getId(),
					DpFuncaoConfianca.class, false));
		} else
			config.setFuncaoConfianca(null);

		if (classificacaoSel != null && classificacaoSel.getId() != null) {
			config.setExClassificacao(dao.consultar(classificacaoSel.getId(),
					ExClassificacao.class, false));
		} else
			config.setExClassificacao(null);
		

		if (pessoaObjetoSel != null && pessoaObjetoSel.getId() != null) {
			config.setPessoaObjeto(dao.consultar(pessoaObjetoSel.getId(), DpPessoa.class, false));
		} else
			config.setPessoaObjeto(null);

		if (lotacaoObjetoSel != null && lotacaoObjetoSel.getId() != null) {
			config.setLotacaoObjeto(dao.consultar(lotacaoObjetoSel.getId(), DpLotacao.class, false));
		} else
			config.setLotacaoObjeto(null);

		if (cargoObjetoSel != null && cargoObjetoSel.getId() != null) {
			config.setCargoObjeto(dao.consultar(cargoObjetoSel.getId(), DpCargo.class, false));
		} else
			config.setCargoObjeto(null);

		if (funcaoObjetoSel != null && funcaoObjetoSel.getId() != null) {
			config.setFuncaoConfiancaObjeto(dao.consultar(funcaoObjetoSel.getId(),
					DpFuncaoConfianca.class, false));
		} else
			config.setFuncaoConfiancaObjeto(null);

		if (idOrgaoObjeto != null && idOrgaoObjeto != 0) {
			config.setOrgaoObjeto(dao.consultar(idOrgaoObjeto,
					CpOrgaoUsuario.class, false));
		} else
			config.setOrgaoObjeto(null);
		
		return config;
	}
	
	private ExConfiguracao instanciarExConfiguracao(final ExDao dao) {
		if (id == null)
			return new ExConfiguracao();
		return dao.consultar(id, ExConfiguracao.class, false);
	}
	
	public Long getIdOrgaoUsu() {
		return idOrgaoUsu;
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

	public Long getIdSituacao() {
		return idSituacao;
	}

	public Long getIdTpConfiguracao() {
		return idTpConfiguracao;
	}

	public DpPessoaSelecao getPessoaSel() {
		return pessoaSel;
	}

	public DpLotacaoSelecao getLotacaoSel() {
		return lotacaoSel;
	}

	public DpCargoSelecao getCargoSel() {
		return cargoSel;
	}

	public DpFuncaoConfiancaSelecao getFuncaoSel() {
		return funcaoSel;
	}

	public ExClassificacaoSelecao getClassificacaoSel() {
		return classificacaoSel;
	}


	public DpPessoaSelecao getPessoaObjetoSel() {
		return pessoaObjetoSel;
	}

	public DpLotacaoSelecao getLotacaoObjetoSel() {
		return lotacaoObjetoSel;
	}

	public DpCargoSelecao getCargoObjetoSel() {
		return cargoObjetoSel;
	}

	public DpFuncaoConfiancaSelecao getFuncaoObjetoSel() {
		return funcaoObjetoSel;
	}

	public Long getIdOrgaoObjeto() {
		return idOrgaoObjeto;
	}


	public ExConfiguracaoBuilder setIdOrgaoUsu(Long idOrgaoUsu) {
		this.idOrgaoUsu = idOrgaoUsu;
		return this;
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

	public ExConfiguracaoBuilder setIdSituacao(Long idSituacao) {
		this.idSituacao = idSituacao;
		return this;
	}

	public ExConfiguracaoBuilder setIdTpConfiguracao(Long idTpConfiguracao) {
		this.idTpConfiguracao = idTpConfiguracao;
		return this;
	}

	public ExConfiguracaoBuilder setPessoaSel(DpPessoaSelecao pessoaSel) {
		this.pessoaSel = pessoaSel;
		return this;
	}

	public ExConfiguracaoBuilder setLotacaoSel(DpLotacaoSelecao lotacaoSel) {
		this.lotacaoSel = lotacaoSel;
		return this;
	}

	public ExConfiguracaoBuilder setCargoSel(DpCargoSelecao cargoSel) {
		this.cargoSel = cargoSel;
		return this;
	}

	public ExConfiguracaoBuilder setFuncaoSel(DpFuncaoConfiancaSelecao funcaoSel) {
		this.funcaoSel = funcaoSel;
		return this;
	}

	public ExConfiguracaoBuilder setClassificacaoSel(ExClassificacaoSelecao classificacaoSel) {
		this.classificacaoSel = classificacaoSel;
		return this;
	}

	public ExConfiguracaoBuilder setPessoaObjetoSel(DpPessoaSelecao pessoaObjetoSel) {
		this.pessoaObjetoSel = pessoaObjetoSel;
		return this;
	}

	public ExConfiguracaoBuilder setLotacaoObjetoSel(DpLotacaoSelecao lotacaoObjetoSel) {
		this.lotacaoObjetoSel = lotacaoObjetoSel;
		return this;
	}

	public ExConfiguracaoBuilder setCargoObjetoSel(DpCargoSelecao cargoObjetoSel) {
		this.cargoObjetoSel = cargoObjetoSel;
		return this;
	}

	public ExConfiguracaoBuilder setFuncaoObjetoSel(DpFuncaoConfiancaSelecao funcaoObjetoSel) {
		this.funcaoObjetoSel = funcaoObjetoSel;
		return this;
	}

	public ExConfiguracaoBuilder setIdOrgaoObjeto(Long idOrgaoObjeto) {
		this.idOrgaoObjeto = idOrgaoObjeto;
		return this;
	}

	public ExConfiguracaoBuilder setId(Long id) {
		this.id = id;
		return this;
	}

	public ExConfiguracaoBuilder setTipoPublicador(Integer tipoPublicador) {
		this.tipoPublicador = tipoPublicador;
		return this;
	}
	
	public static boolean isTipoMatricula(Integer tipo) {
		return MATRICULA.equals(tipo);
	}
	
	public static boolean isTipoOrgaoIntegrado(Integer tipo) {
		return ORGAO_INTEGRADO.equals(tipo);
	}

	public ExConfiguracaoBuilder setIdTpLotacao(Long idTpLotacao) {
		this.idTpLotacao = idTpLotacao;
		return this;
	}
}
