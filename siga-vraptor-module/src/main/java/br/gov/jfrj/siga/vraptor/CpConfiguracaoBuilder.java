/**
 * 
 */
package br.gov.jfrj.siga.vraptor;

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.model.DpCargoSelecao;
import br.gov.jfrj.siga.cp.model.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpTipoLotacao;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class CpConfiguracaoBuilder<B extends CpConfiguracaoBuilder, T extends CpConfiguracao, D extends CpDao> {

	public static final Integer ORGAO_INTEGRADO = 2;
	public static final Integer MATRICULA = 1;
	private Long idOrgaoUsu;
	private Integer idSituacao;
	private Integer idTpConfiguracao;
	private DpPessoaSelecao pessoaSel;
	private DpLotacaoSelecao lotacaoSel;
	private DpCargoSelecao cargoSel;
	private DpFuncaoConfiancaSelecao funcaoSel;
	private DpPessoaSelecao pessoaObjetoSel;
	private DpLotacaoSelecao lotacaoObjetoSel;
	private DpCargoSelecao cargoObjetoSel;
	private DpFuncaoConfiancaSelecao funcaoObjetoSel;
	private Long idOrgaoObjeto;
	private Long id;
	private Long idTpLotacao;
	private Class<T> clazz;
	private D dao;

	public CpConfiguracaoBuilder(Class<T> clazz, D dao) {
		this.clazz = clazz;
		this.dao = dao;
		this.pessoaSel = new DpPessoaSelecao();
		this.lotacaoSel = new DpLotacaoSelecao();
		this.cargoSel = new DpCargoSelecao();
		this.funcaoSel = new DpFuncaoConfiancaSelecao();
	}

//	public T construir() {
//		T config = instanciarConfiguracao();
//		return construir(config);
//	}

	public T construir() {
		T config = instanciarConfiguracao();
		return construir(config);
	}

	public T construir(T config) {
		if (idOrgaoUsu != null && idOrgaoUsu != 0) {
			config.setOrgaoUsuario(dao.consultar(idOrgaoUsu, CpOrgaoUsuario.class, false));
		} else
			config.setOrgaoUsuario(null);

		if (idSituacao != null && idSituacao != 0) {
			config.setCpSituacaoConfiguracao(CpSituacaoDeConfiguracaoEnum.getById(idSituacao));
		} else
			config.setCpSituacaoConfiguracao(null);

		if (idTpConfiguracao != null && idTpConfiguracao != 0) {
			config.setCpTipoConfiguracao(CpTipoDeConfiguracao.getById(idTpConfiguracao));
		} else
			config.setCpTipoConfiguracao(null);

		if (pessoaSel != null && pessoaSel.getId() != null) {
			config.setDpPessoa(dao.consultar(pessoaSel.getId(), DpPessoa.class, false));
		} else
			config.setDpPessoa(null);

		if (lotacaoSel != null && lotacaoSel.getId() != null) {
			config.setLotacao(dao.consultar(lotacaoSel.getId(), DpLotacao.class, false));
		} else
			config.setLotacao(null);

		if (idTpLotacao != null && idTpLotacao != 0) {
			config.setCpTipoLotacao(dao.consultar(idTpLotacao, CpTipoLotacao.class, false));
		} else
			config.setCpTipoLotacao(null);

		if (cargoSel != null && cargoSel.getId() != null) {
			config.setCargo(dao.consultar(cargoSel.getId(), DpCargo.class, false));
		} else
			config.setCargo(null);

		if (funcaoSel != null && funcaoSel.getId() != null) {
			config.setFuncaoConfianca(dao.consultar(funcaoSel.getId(), DpFuncaoConfianca.class, false));
		} else
			config.setFuncaoConfianca(null);

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
			config.setFuncaoConfiancaObjeto(dao.consultar(funcaoObjetoSel.getId(), DpFuncaoConfianca.class, false));
		} else
			config.setFuncaoConfiancaObjeto(null);

		if (idOrgaoObjeto != null && idOrgaoObjeto != 0) {
			config.setOrgaoObjeto(dao.consultar(idOrgaoObjeto, CpOrgaoUsuario.class, false));
		} else
			config.setOrgaoObjeto(null);

		return config;
	}

	protected T instanciarConfiguracao() {
		if (id == null)
			try {
				return clazz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		return dao.consultar(id, clazz, false);
	}

	public Long getIdOrgaoUsu() {
		return idOrgaoUsu;
	}

	public B setIdOrgaoUsu(Long idOrgaoUsu) {
		this.idOrgaoUsu = idOrgaoUsu;
		return (B) this;
	}

	public Integer getIdSituacao() {
		return idSituacao;
	}

	public B setIdSituacao(Integer idSituacao) {
		this.idSituacao = idSituacao;
		return (B) this;
	}

	public Integer getIdTpConfiguracao() {
		return idTpConfiguracao;
	}

	public B setIdTpConfiguracao(Integer idTpConfiguracao) {
		this.idTpConfiguracao = idTpConfiguracao;
		return (B) this;
	}

	public DpPessoaSelecao getPessoaSel() {
		return pessoaSel;
	}

	public B setPessoaSel(DpPessoaSelecao pessoaSel) {
		this.pessoaSel = pessoaSel;
		return (B) this;
	}

	public DpLotacaoSelecao getLotacaoSel() {
		return lotacaoSel;
	}

	public B setLotacaoSel(DpLotacaoSelecao lotacaoSel) {
		this.lotacaoSel = lotacaoSel;
		return (B) this;
	}

	public DpCargoSelecao getCargoSel() {
		return cargoSel;
	}

	public B setCargoSel(DpCargoSelecao cargoSel) {
		this.cargoSel = cargoSel;
		return (B) this;
	}

	public DpFuncaoConfiancaSelecao getFuncaoSel() {
		return funcaoSel;
	}

	public B setFuncaoSel(DpFuncaoConfiancaSelecao funcaoSel) {
		this.funcaoSel = funcaoSel;
		return (B) this;
	}

	public DpPessoaSelecao getPessoaObjetoSel() {
		return pessoaObjetoSel;
	}

	public B setPessoaObjetoSel(DpPessoaSelecao pessoaObjetoSel) {
		this.pessoaObjetoSel = pessoaObjetoSel;
		return (B) this;
	}

	public DpLotacaoSelecao getLotacaoObjetoSel() {
		return lotacaoObjetoSel;
	}

	public B setLotacaoObjetoSel(DpLotacaoSelecao lotacaoObjetoSel) {
		this.lotacaoObjetoSel = lotacaoObjetoSel;
		return (B) this;
	}

	public DpCargoSelecao getCargoObjetoSel() {
		return cargoObjetoSel;
	}

	public B setCargoObjetoSel(DpCargoSelecao cargoObjetoSel) {
		this.cargoObjetoSel = cargoObjetoSel;
		return (B) this;
	}

	public DpFuncaoConfiancaSelecao getFuncaoObjetoSel() {
		return funcaoObjetoSel;
	}

	public B setFuncaoObjetoSel(DpFuncaoConfiancaSelecao funcaoObjetoSel) {
		this.funcaoObjetoSel = funcaoObjetoSel;
		return (B) this;
	}

	public Long getIdOrgaoObjeto() {
		return idOrgaoObjeto;
	}

	public B setIdOrgaoObjeto(Long idOrgaoObjeto) {
		this.idOrgaoObjeto = idOrgaoObjeto;
		return (B) this;
	}

	public Long getId() {
		return id;
	}

	public B setId(Long id) {
		this.id = id;
		return (B) this;
	}

	public Long getIdTpLotacao() {
		return idTpLotacao;
	}

	public B setIdTpLotacao(Long idTpLotacao) {
		this.idTpLotacao = idTpLotacao;
		return (B) this;
	}

	public D dao() {
		return dao;
	}
}
