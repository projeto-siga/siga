/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.ex.bl;

import java.util.Set;
import java.util.SortedSet;

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoBL;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.ExVia;
import br.gov.jfrj.siga.hibernate.ExDao;

/**
 * @author eeh
 * 
 */
public class ExConfiguracaoBL extends CpConfiguracaoBL {

	public static int NIVEL_ACESSO = 1;
	public static int PESSOA = 2;
	public static int LOTACAO = 3;
	public static int FUNCAO = 4;
	public static int ORGAO = 5;
	public static int CARGO = 6;
	public static int TIPO_MOVIMENTACAO = 7;
	public static int VIA = 8;
	public static int CLASSIFICACAO = 9;
	public static int MODELO = 10;
	public static int FORMA = 11;
	public static int TIPO = 12;
	public static int PAPEL = 13;
	public static int TIPO_FORMA_DOC = 14;
	public static int SERVICO = 14;

	/**
	 * Verifica se a configuração é uma configuração válida.
	 */
	@Override
	public boolean atendeExigencias(CpConfiguracao cfgFiltro,
			Set<Integer> atributosDesconsiderados, CpConfiguracao cfg,
			SortedSet<CpPerfil> perfis) {
		if (!super.atendeExigencias(cfgFiltro, atributosDesconsiderados, cfg,
				perfis))
			return false;

		if (cfg instanceof ExConfiguracao
				&& cfgFiltro instanceof ExConfiguracao) {
			ExConfiguracao exCfg = (ExConfiguracao) cfg;
			ExConfiguracao exCfgFiltro = (ExConfiguracao) cfgFiltro;

			if (exCfg.getExNivelAcesso() != null
					&& ((exCfgFiltro.getExNivelAcesso() != null && !exCfg
							.getExNivelAcesso().getGrauNivelAcesso().equals(
									exCfgFiltro.getExNivelAcesso()
											.getGrauNivelAcesso())) || ((exCfgFiltro
							.getExNivelAcesso() == null) && !atributosDesconsiderados
							.contains(NIVEL_ACESSO))))
				return false;

			if (exCfg.getDpPessoa() != null
					&& ((exCfgFiltro.getDpPessoa() != null
							&& !exCfg.getDpPessoa().equivale(
									exCfgFiltro.getDpPessoa()) || ((exCfgFiltro
							.getDpPessoa() == null) && !atributosDesconsiderados
							.contains(PESSOA)))))
				return false;

			if (exCfg.getLotacao() != null
					&& ((exCfgFiltro.getLotacao() != null
							&& !exCfg.getLotacao().equivale(
									exCfgFiltro.getLotacao()) || ((exCfgFiltro
							.getLotacao() == null) && !atributosDesconsiderados
							.contains(LOTACAO)))))
				return false;

			if (exCfg.getFuncaoConfianca() != null
					&& ((exCfgFiltro.getFuncaoConfianca() != null && !exCfg
							.getFuncaoConfianca().getIdFuncao().equals(
									exCfgFiltro.getFuncaoConfianca()
											.getIdFuncao())) || ((exCfgFiltro
							.getFuncaoConfianca() == null) && !atributosDesconsiderados
							.contains(FUNCAO))))
				return false;

			if (exCfg.getOrgaoUsuario() != null
					&& ((exCfgFiltro.getOrgaoUsuario() != null && !exCfg
							.getOrgaoUsuario().getIdOrgaoUsu().equals(
									exCfgFiltro.getOrgaoUsuario()
											.getIdOrgaoUsu())) || ((exCfgFiltro
							.getOrgaoUsuario() == null) && !atributosDesconsiderados
							.contains(ORGAO))))
				return false;

			if (exCfg.getCargo() != null
					&& ((exCfgFiltro.getCargo() != null && !exCfg.getCargo()
							.getIdCargo().equals(
									exCfgFiltro.getCargo().getIdCargo())) || ((exCfgFiltro
							.getCargo() == null) && !atributosDesconsiderados
							.contains(CARGO))))
				return false;

			if (exCfg.getExTipoMovimentacao() != null
					&& ((exCfgFiltro.getExTipoMovimentacao() != null && !exCfg
							.getExTipoMovimentacao().getIdTpMov().equals(
									exCfgFiltro.getExTipoMovimentacao()
											.getIdTpMov())) || ((exCfgFiltro
							.getExTipoMovimentacao() == null) && !atributosDesconsiderados
							.contains(TIPO_MOVIMENTACAO))))
				return false;

			if (exCfg.getExVia() != null
					&& ((exCfgFiltro.getExVia() != null && !exCfg.getExVia()
							.getIdVia().equals(
									exCfgFiltro.getExVia().getIdVia())) || ((exCfgFiltro
							.getExVia() == null) && !atributosDesconsiderados
							.contains(VIA))))
				return false;

			if (exCfg.getExClassificacao() != null
					&& ((exCfgFiltro.getExClassificacao() != null && !exCfg
							.getExClassificacao().getIdClassificacao().equals(
									exCfgFiltro.getExClassificacao()
											.getIdClassificacao())) || ((exCfgFiltro
							.getExClassificacao() == null) && !atributosDesconsiderados
							.contains(CLASSIFICACAO))))
				return false;

			if (exCfg.getExModelo() != null
					&& ((exCfgFiltro.getExModelo() != null && !exCfg
							.getExModelo().getIdMod().equals(
									exCfgFiltro.getExModelo().getIdMod())) || ((exCfgFiltro
							.getExModelo() == null) && !atributosDesconsiderados
							.contains(MODELO))))
				return false;

			if (exCfg.getExFormaDocumento() != null
					&& ((exCfgFiltro.getExFormaDocumento() != null && !exCfg
							.getExFormaDocumento().getIdFormaDoc().equals(
									exCfgFiltro.getExFormaDocumento()
											.getIdFormaDoc())) || ((exCfgFiltro
							.getExFormaDocumento() == null) && !atributosDesconsiderados
							.contains(FORMA))))
				return false;

			if (exCfg.getExTipoDocumento() != null
					&& ((exCfgFiltro.getExTipoDocumento() != null && !exCfg
							.getExTipoDocumento().getIdTpDoc().equals(
									exCfgFiltro.getExTipoDocumento().getId())) || ((exCfgFiltro
							.getExTipoDocumento() == null) && !atributosDesconsiderados
							.contains(TIPO))))
				return false;

			if (exCfg.getExPapel() != null
					&& ((exCfgFiltro.getExPapel() != null && !exCfg
							.getExPapel().getIdPapel().equals(
									exCfgFiltro.getExPapel().getIdPapel())) || ((exCfgFiltro
							.getExPapel() == null) && !atributosDesconsiderados
							.contains(PAPEL))))
				return false;

			if (exCfg.getExTipoFormaDoc() != null
					&& ((exCfgFiltro.getExTipoFormaDoc() != null && !exCfg
							.getExTipoFormaDoc().getId().equals(
									exCfgFiltro.getExTipoFormaDoc().getId())) || ((exCfgFiltro
							.getExTipoFormaDoc() == null) && !atributosDesconsiderados
							.contains(TIPO_FORMA_DOC))))
				return false;

			if (exCfg.getCpServico() != null
					&& ((exCfgFiltro.getCpServico() != null && !exCfg
							.getCpServico().getIdServico().equals(
									exCfgFiltro.getCpServico().getIdServico())) || ((exCfgFiltro
							.getCpServico() == null) && !atributosDesconsiderados
							.contains(SERVICO))))
				return false;
		}
		return true;
	}

	public CpSituacaoConfiguracao buscaSituacao(ExConfiguracao exConfiguracao)
			throws Exception {
		CpConfiguracao cpConfiguracaoResult = buscaConfiguracao(exConfiguracao,
				new int[] { 0 }, ExDao.getInstance()
						.consultarDataEHoraDoServidor());
		if (cpConfiguracaoResult != null)
			return cpConfiguracaoResult.getCpSituacaoConfiguracao();
		else
			return exConfiguracao.getCpTipoConfiguracao().getSituacaoDefault();
	}

	public CpSituacaoConfiguracao buscaSituacao(ExModelo mod, DpPessoa pess,
			DpLotacao lota, long idTpConfig) throws Exception {
		ExConfiguracao exConfig = new ExConfiguracao();
		exConfig.setDpPessoa(pess);
		exConfig.setLotacao(lota);
		exConfig.setExModelo(mod);
		exConfig.setCpTipoConfiguracao(ExDao.getInstance().consultar(
				idTpConfig, CpTipoConfiguracao.class, false));
		return buscaSituacao(exConfig);

	}

	/**
	 * 
	 * Método com implementação completa, chamado pelas outras sobrecargas
	 * 
	 * @param exTpDoc
	 * @param exFormaDoc
	 * @param exMod
	 * @param exClassificacao
	 * @param exVia
	 * @param exTpMov
	 * @param cargo
	 * @param cpOrgaoUsu
	 * @param dpFuncaoConfianca
	 * @param dpLotacao
	 * @param dpPessoa
	 * @param nivelAcesso
	 * @param idTpConf
	 * @throws Exception
	 */
	public boolean podePorConfiguracao(CpServico cpServico,
			ExTipoFormaDoc exTipoFormaDoc, ExPapel exPapel,
			ExTipoDocumento exTpDoc, ExFormaDocumento exFormaDoc,
			ExModelo exMod, ExClassificacao exClassificacao, ExVia exVia,
			ExTipoMovimentacao exTpMov, DpCargo cargo,
			CpOrgaoUsuario cpOrgaoUsu, DpFuncaoConfianca dpFuncaoConfianca,
			DpLotacao dpLotacao, DpPessoa dpPessoa, ExNivelAcesso nivelAcesso,
			long idTpConf) throws Exception {

		ExConfiguracao config = new ExConfiguracao();

		config.setCargo(cargo);
		config.setOrgaoUsuario(cpOrgaoUsu);
		config.setFuncaoConfianca(dpFuncaoConfianca);
		config.setLotacao(dpLotacao);
		config.setDpPessoa(dpPessoa);
		config.setCpTipoConfiguracao(CpDao.getInstance().consultar(idTpConf,
				CpTipoConfiguracao.class, false));

		config.setCpServico(cpServico);
		config.setExTipoFormaDoc(exTipoFormaDoc);
		config.setExPapel(exPapel);
		config.setExTipoDocumento(exTpDoc);
		config.setExFormaDocumento(exFormaDoc);
		config.setExModelo(exMod);
		config.setExClassificacao(exClassificacao);
		config.setExVia(exVia);
		config.setExTipoMovimentacao(exTpMov);
		config.setExNivelAcesso(nivelAcesso);

		CpConfiguracao cfg = (CpConfiguracao) buscaConfiguracao(config,
				new int[] { 0 }, null);

		CpSituacaoConfiguracao situacao;
		if (cfg != null)
			situacao = cfg.getCpSituacaoConfiguracao();
		else
			situacao = config.getCpTipoConfiguracao().getSituacaoDefault();

		if (situacao != null
				&& situacao.getIdSitConfiguracao() == CpSituacaoConfiguracao.SITUACAO_PODE)
			return true;
		return false;
	}

	/**
	 * 
	 * Usado para se verificar se uma pessoa pode realizar uma determinada
	 * operação no documento
	 * 
	 * @param dpPessoa
	 * @param dpLotacao
	 * @param idTpConf
	 * @throws Exception
	 */
	public boolean podePorConfiguracao(DpPessoa dpPessoa, DpLotacao dpLotacao,
			long idTpConf) throws Exception {
		return podePorConfiguracao(null, null, null, null, null, null, null,
				null, null, null, null, null, dpLotacao, dpPessoa, null,
				idTpConf);

	}

	/**
	 * 
	 * Usado para se saber se uma pessoa pode fazer uma certa movimentação
	 * 
	 * @param dpPessoa
	 * @param dpLotacao
	 * @param idTpMov
	 * @param idTpConf
	 * @throws Exception
	 */
	public boolean podePorConfiguracao(DpPessoa dpPessoa, DpLotacao dpLotacao,
			long idTpMov, long idTpConf) throws Exception {
		ExTipoMovimentacao exTpMov = ExDao.getInstance().consultar(idTpMov,
				ExTipoMovimentacao.class, false);
		return podePorConfiguracao(null, null, null, null, null, null, null,
				null, exTpMov, null, null, null, dpLotacao, dpPessoa, null,
				idTpConf);
	}

	public boolean podePorConfiguracao(ExModelo mod, long idTpConf)
			throws Exception {
		return podePorConfiguracao(null, null, null, null, null, mod, null,
				null, null, null, null, null, null, null, null, idTpConf);
	}

	public boolean podePorConfiguracao(ExModelo mod, long idTpMov, long idTpConf)
			throws Exception {
		ExTipoMovimentacao exTpMov = ExDao.getInstance().consultar(idTpMov,
				ExTipoMovimentacao.class, false);
		return podePorConfiguracao(null, null, null, null, null, mod, null,
				null, exTpMov, null, null, null, null, null, null, idTpConf);
	}

	/**
	 * 
	 * Usado para se saber se uma pessoa pode gravar o documento pretendido
	 * 
	 * @param dpPessoa
	 * @param lotacao
	 * @param exTpDoc
	 * @param exFormaDoc
	 * @param exMod
	 * @param exClassif
	 * @param nivelAcesso
	 * @param idTpConf
	 * @throws Exception
	 */
	public boolean podePorConfiguracao(DpPessoa dpPessoa, DpLotacao lotacao,
			ExTipoDocumento exTpDoc, ExFormaDocumento exFormaDoc,
			ExModelo exMod, ExClassificacao exClassif,
			ExNivelAcesso nivelAcesso, Long idTpConf) throws Exception {
		return podePorConfiguracao(null, null, null, exTpDoc, exFormaDoc,
				exMod, exClassif, null, null, null, null, null, lotacao,
				dpPessoa, nivelAcesso, idTpConf);
	}

	/**
	 * 
	 * Usado para se saber se uma determinada pessoa pode criar um documento de
	 * uma certa espécie
	 * 
	 * @param dpPessoa
	 * @param dpLotacao
	 * @param forma
	 * @param idTpConf
	 * @throws Exception
	 */
	public boolean podePorConfiguracao(DpPessoa dpPessoa, DpLotacao dpLotacao,
			ExFormaDocumento forma, long idTpConf) throws Exception {
		return podePorConfiguracao(null, null, null, null, forma, null, null,
				null, null, null, null, null, dpLotacao, dpPessoa, null,
				idTpConf);
	}

	/**
	 * 
	 * Usado para se verificar se um usuário pode criar um documento de um certo
	 * modelo
	 * 
	 * @param dpPessoa
	 * @param dpLotacao
	 * @param mod
	 * @param idTpConf
	 * @throws Exception
	 */
	public boolean podePorConfiguracao(DpPessoa dpPessoa, DpLotacao dpLotacao,
			ExModelo mod, long idTpConf) throws Exception {
		return podePorConfiguracao(null, null, null, null, null, mod, null,
				null, null, null, null, null, dpLotacao, dpPessoa, null,
				idTpConf);
	}

	public boolean podePorConfiguracao(DpPessoa dpPessoa, long idTpConf)
			throws Exception {
		return podePorConfiguracao(null, null, null, null, null, null, null,
				null, null, null, null, null, null, dpPessoa, null, idTpConf);
	}

	public boolean podePorConfiguracao(DpLotacao dpLotacao, long idTpConf)
			throws Exception {
		return podePorConfiguracao(null, null, null, null, null, null, null,
				null, null, null, null, null, dpLotacao, null, null, idTpConf);
	}

	// Pedro : Fim //
	/**
	 * Informa se um certo modelo pode sofrer uma certa movimentação, por uma
	 * certa pessoa
	 * 
	 * @param dpPessoa
	 * @param dpLotacao
	 * @param mod
	 * @param tpMov
	 * @param idTpConf
	 * @throws Exception
	 */
	public boolean podePorConfiguracao(DpPessoa dpPessoa, DpLotacao dpLotacao,
			ExModelo mod, long idTpMov, long idTpConf) throws Exception {
		ExTipoMovimentacao exTpMov = ExDao.getInstance().consultar(idTpMov,
				ExTipoMovimentacao.class, false);
		return podePorConfiguracao(null, null, null, null, null, mod, null,
				null, exTpMov, null, null, null, dpLotacao, dpPessoa, null,
				idTpConf);
	}

	public boolean podePorConfiguracao(ExTipoFormaDoc exTipoFormaDoc,
			ExPapel exPapel, ExTipoMovimentacao exTpMov, long idTpConf)
			throws Exception {
		return podePorConfiguracao(null, exTipoFormaDoc, exPapel, null, null,
				null, null, null, exTpMov, null, null, null, null, null, null,
				idTpConf);
	}
	
	public boolean podePorConfiguracao(ExTipoFormaDoc exTipoFormaDoc,
			ExPapel exPapel, DpPessoa pessoa, ExTipoMovimentacao exTpMov, long idTpConf)
			throws Exception {
		return podePorConfiguracao(null, exTipoFormaDoc, exPapel, null, null,
				null, null, null, exTpMov, null, null, null, null, pessoa, null,
				idTpConf);
	}

	public boolean podePorConfiguracao(DpPessoa dpPessoa, DpLotacao dpLotacao,
			CpServico cpServico, long idTpConf) throws Exception {
		return podePorConfiguracao(cpServico, null, null, null, null, null,
				null, null, null, null, null, null, dpLotacao, dpPessoa, null,
				idTpConf);
	}

	public void deduzFiltro(ExConfiguracao exConfiguracao) {

		if (exConfiguracao.getDpPessoa() != null) {
			if (exConfiguracao.getLotacao() == null)
				exConfiguracao.setLotacao(exConfiguracao.getDpPessoa()
						.getLotacao());
			if (exConfiguracao.getCargo() == null)
				exConfiguracao
						.setCargo(exConfiguracao.getDpPessoa().getCargo());
			if (exConfiguracao.getFuncaoConfianca() == null)
				exConfiguracao.setFuncaoConfianca(exConfiguracao.getDpPessoa()
						.getFuncaoConfianca());
		}

		if (exConfiguracao.getLotacao() != null)
			if (exConfiguracao.getOrgaoUsuario() == null)
				exConfiguracao.setOrgaoUsuario(exConfiguracao.getLotacao()
						.getOrgaoUsuario());

		// *************************************************
		// Ver regrinha pra habilitar e desabilitar esses if's aí, talvez
		// *************************************************

		if (exConfiguracao.getExTipoDocumento() != null)
			if (exConfiguracao.getExFormaDocumento() == null) {
				Set<ExFormaDocumento> formas = exConfiguracao
						.getExTipoDocumento().getExFormaDocumentoSet();
				if (formas.size() == 1)
					exConfiguracao
							.setExFormaDocumento(formas.iterator().next());
			}

		if (exConfiguracao.getExFormaDocumento() != null)
			if (exConfiguracao.getExModelo() == null) {
				Set<ExModelo> modelos = exConfiguracao.getExFormaDocumento()
						.getExModeloSet();
				if (modelos.size() == 1)
					exConfiguracao.setExModelo(modelos.iterator().next());
			}

		if (exConfiguracao.getExModelo() != null) {
			if (exConfiguracao.getExClassificacao() == null)
				exConfiguracao.setExClassificacao(exConfiguracao.getExModelo()
						.getExClassificacao());
			if (exConfiguracao.getExFormaDocumento() == null)
				exConfiguracao.setExFormaDocumento(exConfiguracao.getExModelo()
						.getExFormaDocumento());
		}
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	// public void doFilter(ServletRequest request, ServletResponse response,
	// FilterChain chain) throws IOException, ServletException {
	//
	// hashListas.set(null);
	// chain.doFilter(request, response);
	// hashListas.set(null);
	// }

	// public void init(FilterConfig arg0) throws ServletException {
	// // TODO Auto-generated method stub
	//
	// }

	public DpPessoa getDiretorForo() throws Exception {
		ExConfiguracao c = new ExConfiguracao();
		c.setCpTipoConfiguracao(ExDao.getInstance().consultar(
				CpTipoConfiguracao.TIPO_CONFIG_DIRETORFORO,
				CpTipoConfiguracao.class, false));
		c = ExDao.getInstance().consultar(c).get(0);
		return c.getDpPessoa();
	}

	public boolean isDiretorForo(DpPessoa quem) throws Exception {
		return (quem.equivale(getDiretorForo()));
	}

}
