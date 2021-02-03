package br.gov.jfrj.siga.vraptor;

import java.util.List;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.model.DpCargoSelecao;
import br.gov.jfrj.siga.cp.model.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
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
			result.include("idSituacao", c.getCpSituacaoConfiguracao().getIdSitConfiguracao());

		if (c.getCpTipoConfiguracao() != null)
			result.include("idTpConfiguracao", c.getCpTipoConfiguracao().getIdTpConfiguracao());

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

	public static void gravarConfiguracao(Long idTpConfiguracao, Long idSituacao, final CpConfiguracao config,
			CpDao dao, CpIdentidade idc) {
		if (idTpConfiguracao == null || idTpConfiguracao == 0)
			throw new AplicacaoException("Tipo de configuracao não informado");

		if (idSituacao == null || idSituacao == 0)
			throw new AplicacaoException("Situação de Configuracao não informada");

		config.setHisDtIni(dao.consultarDataEHoraDoServidor());
		dao.gravarComHistorico(config, idc);
	}

}
