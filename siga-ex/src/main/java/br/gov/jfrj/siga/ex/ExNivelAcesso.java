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
/*
 * Created Mon Nov 14 13:33:07 GMT-03:00 2005 by MyEclipse Hibernate Tool.
 */
package br.gov.jfrj.siga.ex;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExConfiguracaoBL;
import br.gov.jfrj.siga.hibernate.ExDao;

/**
 * A class that represents a row in the 'EX_TIPO_DESPACHO' table. This class may
 * be customized as it is never re-generated after being created.
 */
@Entity
@BatchSize(size = 500)
@Immutable
@Cacheable
@Cache(region = CpDao.CACHE_HOURS, usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "siga.ex_nivel_acesso")
public class ExNivelAcesso extends AbstractExNivelAcesso implements
		Serializable {

	private static final long serialVersionUID = 3256722875116761397L;

	public static final long NIVEL_ACESSO_PUBLICO = 10;

	public static final long NIVEL_ACESSO_ENTRE_ORGAOS = 20;

	public static final long NIVEL_RESERVADO_ENTRE_LOTACOES = 25;

	public static final long NIVEL_ACESSO_PESSOA_SUB = 30;

	public static final long NIVEL_ACESSO_SUB_PESSOA = 40;

	public static final long NIVEL_ACESSO_ENTRE_LOTACOES = 60;

	public static final long NIVEL_ACESSO_PESSOAL = 100;

	public static final long ID_LIMITADO_AO_ORGAO = 1;
	public static final long ID_LIMITADO_SUBSEC_PARA_PESSOA = 2;
	public static final long ID_LIMITADO_ENTRE_LOTACOES = 3;
	// ID 4 - NÃO MAPEADO
	public static final long ID_LIMITADO_ENTRE_PESSOAS = 5;
	public static final long ID_PUBLICO = 6;
	public static final long ID_LIMITADO_PESSOA_PARA_SUBSEC = 7;

	public ExNivelAcesso() {
	}

	public static ExNivelAcesso getNivelAcessoDefault(final ExTipoDocumento exTpDoc, final ExFormaDocumento forma, 
			final ExModelo exMod, final ExClassificacao classif, final DpPessoa titular, final DpLotacao lotaTitular) {
		final Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();

		final ExConfiguracao config = new ExConfiguracao();
		final CpTipoConfiguracao exTpConfig = new CpTipoConfiguracao();
		final CpSituacaoConfiguracao exStConfig = new CpSituacaoConfiguracao();
		config.setDpPessoa(titular);
		config.setLotacao(lotaTitular);
		config.setExTipoDocumento(exTpDoc);
		config.setExFormaDocumento(forma);
		config.setExModelo(exMod);
		config.setExClassificacao(classif);
		exTpConfig.setIdTpConfiguracao(CpTipoConfiguracao.TIPO_CONFIG_NIVELACESSO);
		config.setCpTipoConfiguracao(exTpConfig);
		exStConfig.setIdSitConfiguracao(CpSituacaoConfiguracao.SITUACAO_DEFAULT);
		config.setCpSituacaoConfiguracao(exStConfig);
		ExConfiguracao exConfig;

		try {
			exConfig = new ExConfiguracao(Ex.getInstance().getConf()
					.buscaConfiguracao(config, new int[] { ExConfiguracaoBL.NIVEL_ACESSO }, dt));
		} catch (Exception e) {
			exConfig = null;
		}

		if (exConfig != null) {
			return exConfig.getExNivelAcesso();
		}

		return null;
	}
}
