package br.gov.jfrj.siga.vraptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.model.DpCargoSelecao;
import br.gov.jfrj.siga.cp.model.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.cp.model.enm.CpParamCfg;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpTipoLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class CpConfiguracaoHelper {
	public static void escreverForm(CpConfiguracao c, Result result) throws Exception {
		DpPessoaSelecao pessoaSelecao = new DpPessoaSelecao();
		DpLotacaoSelecao lotacaoSelecao = new DpLotacaoSelecao();
		DpFuncaoConfiancaSelecao funcaoConfiancaSelecao = new DpFuncaoConfiancaSelecao();
		DpCargoSelecao cargoSelecao = new DpCargoSelecao();
		DpPessoaSelecao pessoaObjetoSelecao = new DpPessoaSelecao();
		DpLotacaoSelecao lotacaoObjetoSelecao = new DpLotacaoSelecao();
		DpFuncaoConfiancaSelecao funcaoConfiancaObjetoSelecao = new DpFuncaoConfiancaSelecao();
		DpCargoSelecao cargoObjetoSelecao = new DpCargoSelecao();

		if (c.getOrgaoUsuario() != null)
			result.include("idOrgaoUsu", c.getOrgaoUsuario().getIdOrgaoUsu());

		if (c.getCpSituacaoConfiguracao() != null)
			result.include("idSituacao", c.getCpSituacaoConfiguracao().getId());

		if (c.getCpTipoConfiguracao() != null)
			result.include("idTpConfiguracao", c.getCpTipoConfiguracao().getId());

		if (c.getCpTipoLotacao() != null)
			result.include("idTpLotacao", c.getCpTipoLotacao().getIdTpLotacao());

		if (c.getDpPessoa() != null)
			pessoaSelecao.buscarPorObjeto(c.getDpPessoa());

		if (c.getLotacao() != null)
			lotacaoSelecao.buscarPorObjeto(c.getLotacao());

		if (c.getCargo() != null)
			cargoSelecao.buscarPorObjeto(c.getCargo());

		if (c.getFuncaoConfianca() != null)
			funcaoConfiancaSelecao.buscarPorObjeto(c.getFuncaoConfianca());

		if (c.getPessoaObjeto() != null)
			pessoaObjetoSelecao.buscarPorObjeto(c.getPessoaObjeto());

		if (c.getLotacaoObjeto() != null)
			lotacaoObjetoSelecao.buscarPorObjeto(c.getLotacaoObjeto());

		if (c.getCargoObjeto() != null)
			cargoObjetoSelecao.buscarPorObjeto(c.getCargoObjeto());

		if (c.getFuncaoConfianca() != null)
			funcaoConfiancaObjetoSelecao.buscarPorObjeto(c.getFuncaoConfiancaObjeto());

		if (c.getOrgaoObjeto() != null)
			result.include("idOrgaoObjeto", c.getOrgaoObjeto().getIdOrgaoUsu());

		result.include("pessoaSel", pessoaSelecao);
		result.include("lotacaoSel", lotacaoSelecao);
		result.include("cargoSel", cargoSelecao);
		result.include("funcaoSel", funcaoConfiancaSelecao);

		result.include("pessoaObjeto_pessoaSel", pessoaObjetoSelecao);
		result.include("lotacaoObjeto_lotacaoSel", lotacaoObjetoSelecao);
		result.include("cargoObjeto_cargoSel", cargoObjetoSelecao);
		result.include("funcaoObjeto_funcaoSel", funcaoConfiancaObjetoSelecao);
	}

	public static List<CpTipoLotacao> getListaTiposLotacao(CpDao dao) throws Exception {
		return dao.listarTiposLotacao();
	}

	public static List<CpOrgaoUsuario> getOrgaosUsu(CpDao dao) throws AplicacaoException {
		return dao.listarOrgaosUsuarios();
	}

	public static void gravarConfiguracao(Integer idTpConfiguracao, Integer idSituacao, final CpConfiguracao config,
			CpDao dao, CpIdentidade idc) {
		if (idTpConfiguracao == null || idTpConfiguracao == 0)
			throw new AplicacaoException("Tipo de configuracao não informado");

		if (idSituacao == null || idSituacao == 0)
			throw new AplicacaoException("Situação de Configuracao não informada");

		config.setHisDtIni(dao.consultarDataEHoraDoServidor());
		dao.gravarComHistorico(config, idc);
	}

	public static void assertConfig(ITipoDeConfiguracao t, CpConfiguracao c, Object o, Enum p) {
		if (o == null || t.ativo(p.name()))
			return;
		throw new RuntimeException("Configuração " + c.getId() + " não poderia conter o parâmetro " + p.name());
	}

	public static void assertConfigSituacao(ITipoDeConfiguracao t, CpConfiguracao c) {
		if (c.getCpSituacaoConfiguracao() == null)
			return;
		if (t.getSituacoes() != null)
			for (CpSituacaoDeConfiguracaoEnum s : t.getSituacoes())
				if (s == c.getCpSituacaoConfiguracao())
					return;
		for (CpSituacaoDeConfiguracaoEnum s : CpSituacaoDeConfiguracaoEnum.values())
			if (s == c.getCpSituacaoConfiguracao())
				throw new RuntimeException("Configuração " + c.getId() + " não poderia conter a situação " + s.name());
		throw new RuntimeException("Configuração " + c.getId() + " não poderia conter a situação "
				+ c.getCpSituacaoConfiguracao().getDescr());
	}

	public static void assertConfig(ITipoDeConfiguracao t, CpConfiguracao c) {
		assertConfig(t, c, c.getDpPessoa(), CpParamCfg.PESSOA);
		assertConfig(t, c, c.getCpTipoLotacao(), CpParamCfg.TIPO_DE_LOTACAO);
		assertConfig(t, c, c.getLotacao(), CpParamCfg.LOTACAO);
		assertConfig(t, c, c.getCargo(), CpParamCfg.CARGO);
		assertConfig(t, c, c.getFuncaoConfianca(), CpParamCfg.FUNCAO);
		assertConfig(t, c, c.getOrgaoUsuario(), CpParamCfg.ORGAO);
		// assertConfig(t, c, c.get), CpParametroDeConfiguracao.PERFIL);
		assertConfig(t, c, c.getCpServico(), CpParamCfg.SERVICO);
		assertConfig(t, c, c.getPessoaObjeto(), CpParamCfg.PESSOA_OBJETO);
		assertConfig(t, c, c.getLotacaoObjeto(), CpParamCfg.LOTACAO_OBJETO);
		assertConfig(t, c, c.getCargoObjeto(), CpParamCfg.CARGO_OBJETO);
		assertConfig(t, c, c.getFuncaoConfiancaObjeto(), CpParamCfg.FUNCAO_OBJETO);
		assertConfig(t, c, c.getOrgaoObjeto(), CpParamCfg.ORGAO_OBJETO);
		assertConfig(t, c, c.getCpSituacaoConfiguracao(), CpParamCfg.SITUACAO);
		assertConfigSituacao(t, c);
	}

	public static void incluirAtributosDeEdicao(Result result, ITipoDeConfiguracao tpconf, CpConfiguracao config) {
		try {
			assertConfig(tpconf, config);
		} catch (RuntimeException ex) {
			result.include("erroEmConfiguracao", ex.getMessage());
		}
		result.include("tipoDeConfiguracao", tpconf);
		result.include("situacoes", tpconf.getSituacoes());
		result.include("config", config);
		result.include("configuracao", config);
	}

	public static void incluirAtributosDeListagem(Result result, ITipoDeConfiguracao tpconf,
			List<CpConfiguracao> listConfig) {
		Map<Long, String> erroEmConfiguracao = new HashMap<>();
		for (CpConfiguracao c : listConfig)
			try {
				assertConfig(tpconf, c);
			} catch (RuntimeException ex) {
				erroEmConfiguracao.put(c.getId(), ex.getMessage());
			}
		result.include("erroEmConfiguracao", erroEmConfiguracao);
		result.include("tipoDeConfiguracao", tpconf);
		result.include("listConfig", listConfig);

	}
}
