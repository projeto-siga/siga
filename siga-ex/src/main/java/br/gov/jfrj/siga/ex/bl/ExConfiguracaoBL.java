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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoBL;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpTipoLotacao;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExConfiguracaoCache;
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
	public boolean atendeExigencias(CpConfiguracaoCache filtro,
			Set<Integer> atributosDesconsiderados, CpConfiguracaoCache cfg,
			SortedSet<CpPerfil> perfis) {
		if (!super.atendeExigencias(filtro, atributosDesconsiderados, cfg,
				perfis))
			return false;

		if (cfg instanceof ExConfiguracaoCache
				&& filtro instanceof ExConfiguracaoCache) {
			ExConfiguracaoCache exCfg = (ExConfiguracaoCache) cfg;
			ExConfiguracaoCache exFiltro = (ExConfiguracaoCache) filtro;
			if (filtro == null)
				exFiltro = new ExConfiguracaoCache();

			if (desigual(exCfg.exNivelAcesso, exFiltro.exNivelAcesso, atributosDesconsiderados, NIVEL_ACESSO))
				return false;

			if (desigual(exCfg.exTipoMovimentacao, exFiltro.exTipoMovimentacao, atributosDesconsiderados, TIPO_MOVIMENTACAO))
				return false;

			if (desigual(exCfg.exVia, exFiltro.exVia, atributosDesconsiderados, VIA))
				return false;

			if (desigual(exCfg.exClassificacao, exFiltro.exClassificacao, atributosDesconsiderados, CLASSIFICACAO))
				return false;

			if (desigual(exCfg.exModelo, exFiltro.exModelo, atributosDesconsiderados, MODELO))
				return false;

			if (desigual(exCfg.exFormaDocumento, exFiltro.exFormaDocumento, atributosDesconsiderados, FORMA))
				return false;

			if (desigual(exCfg.exTipoDocumento, exFiltro.exTipoDocumento, atributosDesconsiderados, TIPO))
				return false;

			if (desigual(exCfg.exPapel, exFiltro.exPapel, atributosDesconsiderados, PAPEL))
				return false;

			if (desigual(exCfg.exTipoFormaDoc, exFiltro.exTipoFormaDoc, atributosDesconsiderados, TIPO_FORMA_DOC))
				return false;
		}
		return true;
	}

	public CpSituacaoDeConfiguracaoEnum buscaSituacao(final ExConfiguracao exConfiguracao) {
		final CpConfiguracaoCache cpConfiguracaoResult = buscaConfiguracao(exConfiguracao,
				new int[] { 0 }, ExDao.getInstance()
						.consultarDataEHoraDoServidor());
		if (cpConfiguracaoResult != null) {
			return cpConfiguracaoResult.situacao;
		} else {
			return exConfiguracao.getCpTipoConfiguracao().getSituacaoDefault();

		}
	}

	public CpSituacaoDeConfiguracaoEnum buscaSituacao(ExModelo mod, DpPessoa pess,
			DpLotacao lota, ITipoDeConfiguracao idTpConfig) {
		ExConfiguracao exConfig = new ExConfiguracao();
		exConfig.setDpPessoa(pess);
		exConfig.setLotacao(lota);
		exConfig.setExModelo(mod);
		exConfig.setCpTipoConfiguracao(idTpConfig);
		return buscaSituacao(exConfig);

	}
	public CpSituacaoDeConfiguracaoEnum buscaSituacao(final ExModelo mod, final ExTipoDocumento tipo, final DpPessoa pess,
			final DpLotacao lota, final ITipoDeConfiguracao idTpConfig) {
		final ExConfiguracao exConfig = new ExConfiguracao();
		exConfig.setDpPessoa(pess);
		exConfig.setLotacao(lota);
		exConfig.setExModelo(mod);
		exConfig.setExTipoDocumento(tipo);
		exConfig.setCpTipoConfiguracao(idTpConfig);
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
			DpLotacao dpLotacao, DpPessoa dpPessoa, ExNivelAcesso nivelAcesso, CpTipoLotacao cpTpLotacao,
			ITipoDeConfiguracao idTpConf, DpPessoa pessoaObjeto, 
			DpLotacao lotacaoObjeto, CpComplexo complexoObjeto, DpCargo cargoObjeto, 
			DpFuncaoConfianca funcaoConfiancaObjeto, CpOrgaoUsuario orgaoObjeto) {

			CpSituacaoDeConfiguracaoEnum situacao = situacaoPorConfiguracao(cpServico, exTipoFormaDoc, exPapel, exTpDoc,
							exFormaDoc, exMod, exClassificacao, exVia, exTpMov, cargo, cpOrgaoUsu, dpFuncaoConfianca, dpLotacao,
							dpPessoa, nivelAcesso, cpTpLotacao, idTpConf, pessoaObjeto, lotacaoObjeto, complexoObjeto, cargoObjeto,
							funcaoConfiancaObjeto, orgaoObjeto);
			if (situacao != null && situacao.isPermissiva())			
				return true;
		return false;
	}
	
	public CpSituacaoDeConfiguracaoEnum situacaoPorConfiguracao(CpServico cpServico,
			ExTipoFormaDoc exTipoFormaDoc, ExPapel exPapel,
			ExTipoDocumento exTpDoc, ExFormaDocumento exFormaDoc,
			ExModelo exMod, ExClassificacao exClassificacao, ExVia exVia,
			ExTipoMovimentacao exTpMov, DpCargo cargo,
			CpOrgaoUsuario cpOrgaoUsu, DpFuncaoConfianca dpFuncaoConfianca,
			DpLotacao dpLotacao, DpPessoa dpPessoa, ExNivelAcesso nivelAcesso, CpTipoLotacao cpTpLotacao,
			ITipoDeConfiguracao idTpConf, DpPessoa pessoaObjeto, 
			DpLotacao lotacaoObjeto, CpComplexo complexoObjeto, DpCargo cargoObjeto, 
			DpFuncaoConfianca funcaoConfiancaObjeto, CpOrgaoUsuario orgaoObjeto) {

		ExConfiguracao config = new ExConfiguracao();
		config.setCargo(cargo);
		config.setOrgaoUsuario(cpOrgaoUsu);
		config.setFuncaoConfianca(dpFuncaoConfianca);
		config.setLotacao(dpLotacao);
		config.setDpPessoa(dpPessoa);
		config.setCpTipoConfiguracao(idTpConf);
		config.setCpTipoLotacao(cpTpLotacao);

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
		
		config.setPessoaObjeto(pessoaObjeto);
		config.setLotacaoObjeto(lotacaoObjeto);
		config.setComplexoObjeto(complexoObjeto);
		config.setCargoObjeto(cargoObjeto);
		config.setFuncaoConfiancaObjeto(funcaoConfiancaObjeto);
		config.setOrgaoObjeto(orgaoObjeto);

		CpConfiguracaoCache cfg = (CpConfiguracaoCache) buscaConfiguracao(config,
				new int[] { 0 }, null);

		CpSituacaoDeConfiguracaoEnum situacao = null;
		if (cfg != null)
			situacao = cfg.situacao;
		else
			if (config.getCpTipoConfiguracao() != null)
				situacao = config.getCpTipoConfiguracao().getSituacaoDefault();
		
		return situacao;
	}
	
	public CpSituacaoDeConfiguracaoEnum situacaoPorConfiguracao(CpServico cpServico,
			ExTipoFormaDoc exTipoFormaDoc, ExPapel exPapel,
			ExTipoDocumento exTpDoc, ExFormaDocumento exFormaDoc,
			ExModelo exMod, ExClassificacao exClassificacao, ExVia exVia,
			ExTipoMovimentacao exTpMov, DpCargo cargo,
			CpOrgaoUsuario cpOrgaoUsu, DpFuncaoConfianca dpFuncaoConfianca,
			DpLotacao dpLotacao, DpPessoa dpPessoa, ExNivelAcesso nivelAcesso, CpTipoLotacao cpTpLotacao,
			ITipoDeConfiguracao idTpConf) {
		return situacaoPorConfiguracao(cpServico,
				exTipoFormaDoc, exPapel,
				exTpDoc, exFormaDoc,
				exMod, exClassificacao, exVia,
				exTpMov, cargo,
				cpOrgaoUsu, dpFuncaoConfianca,
				dpLotacao, dpPessoa, nivelAcesso, cpTpLotacao,
				idTpConf, null, null, null, null, null, null);
	}
	
	public boolean podePorConfiguracao(CpServico cpServico,
			ExTipoFormaDoc exTipoFormaDoc, ExPapel exPapel,
			ExTipoDocumento exTpDoc, ExFormaDocumento exFormaDoc,
			ExModelo exMod, ExClassificacao exClassificacao, ExVia exVia,
			ExTipoMovimentacao exTpMov, DpCargo cargo,
			CpOrgaoUsuario cpOrgaoUsu, DpFuncaoConfianca dpFuncaoConfianca,
			DpLotacao dpLotacao, DpPessoa dpPessoa, ExNivelAcesso nivelAcesso, CpTipoLotacao cpTpLotacao,
			ITipoDeConfiguracao idTpConf) {
		return podePorConfiguracao(cpServico,
				exTipoFormaDoc, exPapel,
				exTpDoc, exFormaDoc,
				exMod, exClassificacao, exVia,
				exTpMov, cargo,
				cpOrgaoUsu, dpFuncaoConfianca,
				dpLotacao, dpPessoa, nivelAcesso, cpTpLotacao,
				idTpConf, null, null, null, null, null, null);
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
			ITipoDeConfiguracao idTpConf) {
		return podePorConfiguracao(null, null, null, null, null, null, null,
				null, null, null, null, null, dpLotacao, dpPessoa, null,null,
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
			long idTpMov, ITipoDeConfiguracao idTpConf) {
		ExTipoMovimentacao exTpMov = ExDao.getInstance().consultar(idTpMov,
				ExTipoMovimentacao.class, false);
		return podePorConfiguracao(null, null, null, null, null, null, null,
				null, exTpMov, null, null, null, dpLotacao, dpPessoa, null,null,
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
	public boolean podePorConfiguracao(DpPessoa dpPessoa, DpLotacao dpLotacao, DpCargo cargo, DpFuncaoConfianca funcaoConficanca, ExFormaDocumento tipo, ExModelo modelo,
			long idTpMov, ITipoDeConfiguracao idTpConf) {
		ExTipoMovimentacao exTpMov = ExDao.getInstance().consultar(idTpMov,
				ExTipoMovimentacao.class, false);
		return podePorConfiguracao(null, null, null, null, tipo, modelo, null,
				null, exTpMov, cargo, null, funcaoConficanca, dpLotacao, dpPessoa, null,null,
				idTpConf);
	}	

	public boolean podePorConfiguracao(ExModelo mod, ITipoDeConfiguracao idTpConf) {
		return podePorConfiguracao(null, null, null, null, null, mod, null,
				null, null, null, null, null, null, null, null, null,idTpConf);
	}

	public boolean podePorConfiguracao(ExModelo mod, long idTpMov, ITipoDeConfiguracao idTpConf) {
		ExTipoMovimentacao exTpMov = ExDao.getInstance().consultar(idTpMov,
				ExTipoMovimentacao.class, false);
		return podePorConfiguracao(null, null, null, null, null, mod, null,
				null, exTpMov, null, null, null, null, null, null, null,idTpConf);
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
			ExNivelAcesso nivelAcesso, ITipoDeConfiguracao idTpConf) throws Exception {
		return podePorConfiguracao(null, null, null, exTpDoc, exFormaDoc,
				exMod, exClassif, null, null, null, null, null, lotacao,
				dpPessoa, nivelAcesso, null,idTpConf);
	}
	
	/**
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
			ExModelo exMod, ITipoDeConfiguracao idTpConf) {
		return podePorConfiguracao(null, null, null, exTpDoc, exFormaDoc,
				exMod, null, null, null, null, null, null, lotacao,
				dpPessoa, null, null,idTpConf);
	}
	
	

	/**
	 * 
	 * Usado para se saber se deve reiniciar a numeração de um tipo de documento para um órgão
	 * 
	 * @throws Exception
	 */
	public boolean podePorConfiguracao(CpOrgaoUsuario cpOrgaoUsu, ExFormaDocumento exFormaDoc, ITipoDeConfiguracao idTpConf) throws Exception {
		return podePorConfiguracao(null, null, null, null, exFormaDoc, null, null, null,
				null, null, cpOrgaoUsu, null, null, null, null, null,idTpConf); 
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
			ExFormaDocumento forma, ITipoDeConfiguracao idTpConf) {
		return podePorConfiguracao(null, null, null, null, forma, null, null,
				null, null, null, null, null, dpLotacao, dpPessoa, null,null,
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
			ExModelo mod, ITipoDeConfiguracao idTpConf) {
		return podePorConfiguracao(null, null, null, null, null, mod, null,
				null, null, null, null, null, dpLotacao, dpPessoa, null,null,
				idTpConf);
	}

	public boolean podePorConfiguracao(DpPessoa dpPessoa, ITipoDeConfiguracao idTpConf) {
		return podePorConfiguracao(null, null, null, null, null, null, null,
				null, null, null, null, null, null, dpPessoa, null, null,idTpConf);
	}

	public boolean podePorConfiguracao(DpLotacao dpLotacao, ITipoDeConfiguracao idTpConf)
			throws Exception {
		return podePorConfiguracao(null, null, null, null, null, null, null,
				null, null, null, null, null, dpLotacao, null, null, null,idTpConf);
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
			ExModelo mod, long idTpMov, ITipoDeConfiguracao idTpConf) {
		ExTipoMovimentacao exTpMov = ExDao.getInstance().consultar(idTpMov,
				ExTipoMovimentacao.class, false);
		return podePorConfiguracao(null, null, null, null, null, mod, null,
				null, exTpMov, null, null, null, dpLotacao, dpPessoa, null,null,
				idTpConf);
	}

	public boolean podePorConfiguracao(ExTipoFormaDoc exTipoFormaDoc,
			ExPapel exPapel, ExTipoMovimentacao exTpMov, ITipoDeConfiguracao idTpConf)
			throws Exception {
		return podePorConfiguracao(null, exTipoFormaDoc, exPapel, null, null,
				null, null, null, exTpMov, null, null, null, null, null, null,null,
				idTpConf);
	}

	public boolean podePorConfiguracao(ExTipoFormaDoc exTipoFormaDoc,
			ExPapel exPapel, DpPessoa pessoa, ExTipoMovimentacao exTpMov,
			ITipoDeConfiguracao idTpConf) throws Exception {
		return podePorConfiguracao(null, exTipoFormaDoc, exPapel, null, null,
				null, null, null, exTpMov, null, null, null, null, pessoa,
				null,null, idTpConf);
	}
	
	public boolean podePorConfiguracao(ExTipoFormaDoc exTipoFormaDoc,
			ExPapel exPapel, DpLotacao lotacao, ExTipoMovimentacao exTpMov,
			ITipoDeConfiguracao idTpConf) throws Exception {
		return podePorConfiguracao(null, exTipoFormaDoc, exPapel, null, null,
				null, null, null, exTpMov, null, null, null, lotacao, null,
				null,null, idTpConf);
	}

	public boolean podePorConfiguracao(DpPessoa dpPessoa, DpLotacao dpLotacao,
			CpServico cpServico, ITipoDeConfiguracao idTpConf) throws Exception {
		return podePorConfiguracao(cpServico, null, null, null, null, null,
				null, null, null, null, null, null, dpLotacao, dpPessoa, null,null,
				idTpConf);
	}

	/**
	 * Infere configurações óbvias. Por exemplo, se for informado o tipo de documento e só existir uma forma de documento para este tipo, a forma será preenchida automaticamente.
	 * @param cpConfiguracao
	 */
	@Override
	public void deduzFiltro(CpConfiguracao cpConfiguracao) {

		super.deduzFiltro(cpConfiguracao);

		// *************************************************
		// Ver regrinha pra habilitar e desabilitar esses if's aí, talvez
		// *************************************************

		if (!(cpConfiguracao instanceof ExConfiguracao))
			return;

		ExConfiguracao exConfiguracao = (ExConfiguracao) cpConfiguracao;

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
		
		cpConfiguracao = exConfiguracao;
	}

	/*
	 * public ExConfiguracao buscaConfiguracao( ExConfiguracao
	 * exConfiguracaoFiltro, int[] atributoDesconsideradoFiltro, Date dtEvn)
	 * throws Exception { deduzFiltro(exConfiguracaoFiltro); return
	 * super.buscaConfiguracao(exConfiguracaoFiltro,
	 * atributoDesconsideradoFiltro, dtEvn); }
	 */

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

//	public DpPessoa getDiretorForo() throws Exception {
//		ExConfiguracao c = new ExConfiguracao();
//		c.setCpTipoConfiguracao(ExDao.getInstance().consultar(
//				CpTipoDeConfiguracao.DIRETORFORO,
//				CpTipoConfiguracao.class, false));
//		c = ExDao.getInstance().consultar(c).get(0);
//		return c.getDpPessoa();
//	}
//
//	public boolean isDiretorForo(DpPessoa quem) throws Exception {
//		return (quem.equivale(getDiretorForo()));
//	}
	
	
	/**
	 * 
	 * Retorna uma lista de (ex)configurações vigentes de acordo com um certo tipo
	 * 
	 * @param ExConfiguracao
	 * 
	 */
	
	public List<ExConfiguracao> buscarConfiguracoesVigentes(final ExConfiguracao exemplo) {		
		Date hoje = new Date(); 
		List<ExConfiguracao> todasConfig = ExDao.getInstance().consultar(exemplo);
		List<ExConfiguracao> configVigentes = new ArrayList<ExConfiguracao>();
		
		for (ExConfiguracao cfg : todasConfig ) {
			if (!cfg.ativaNaData(hoje)) 
			   continue;			
			configVigentes.add(cfg);			
		}
		return(configVigentes);		
	}
	
	/**
	 * Varre as entidades definidas na configuração para evitar que o hibernate
	 * guarde versões lazy delas.
	 * 
	 * @param listaCfg - lista de configurações que podem ter objetos lazy
	 */
	@Override
	protected void evitarLazy(List<CpConfiguracao> listaCfg) {
		super.evitarLazy(listaCfg);
		
		for (CpConfiguracao cpCfg : listaCfg) {
			if (cpCfg instanceof ExConfiguracao){
				ExConfiguracao cfg = (ExConfiguracao) cpCfg;
				if (cfg.getExClassificacao() != null)
					cfg.getExClassificacao().getDescrClassificacao();
				if (cfg.getExFormaDocumento() != null)
					cfg.getExFormaDocumento().getDescrFormaDoc();
				if (cfg.getExModelo() != null)
					cfg.getExModelo().getDescMod();
				if (cfg.getExNivelAcesso() != null)
					cfg.getExNivelAcesso().getDscNivelAcesso();
				if (cfg.getExPapel() != null)
					cfg.getExPapel().getDescPapel();
				if (cfg.getExTipoDocumento() != null)
					cfg.getExTipoDocumento().getDescrTipoDocumento();
				if (cfg.getExTipoFormaDoc() != null)
					cfg.getExTipoFormaDoc().getDescTipoFormaDoc();
				if (cfg.getExTipoMovimentacao() != null)
					cfg.getExTipoMovimentacao().getDescrTipoMovimentacao();
				if (cfg.getExVia() != null)
					cfg.getExVia().getObs();
				
				if (cfg.getExModelo() != null) {
					cfg.getExModelo().getId();
					
					if (cfg.getExModelo().getExFormaDocumento() != null) {
						cfg.getExModelo().getExFormaDocumento().getId();
					}
				}

			}
		}
	}
}
