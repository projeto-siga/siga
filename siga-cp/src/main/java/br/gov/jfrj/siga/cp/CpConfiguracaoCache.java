/*******************************************************************************
] * Copyright (c) 2006 - 2011 SJRJ.
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
 * Criado em  12/12/2005
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.cp;

import java.util.Date;

import org.hibernate.proxy.HibernateProxy;

public class CpConfiguracaoCache {

	public long idConfiguracao;
//	public String descrConfiguracao;
	public Date hisDtIni;
	public Date hisDtFim;

	public long orgaoUsuario;
	public long lotacao;
	public long complexo;
	public long cargo;
	public long funcaoConfianca;
	public long dpPessoa;
	public long cpTipoConfiguracao;
	public long cpServico;
	public long cpIdentidade;
	public String nmEmail;
	public String dscFormula;
	public long cpTipoLotacao;
	public Date dtIniVigConfiguracao;
	public Date dtFimVigConfiguracao;
	public long configuracaoInicial;
	public long orgaoObjeto;
	public long lotacaoObjeto;
	public long complexoObjeto;
	public long cargoObjeto;
	public long funcaoConfiancaObjeto;
	public long pessoaObjeto;
	public long cpGrupo;
	public int cpGrupoNivel;
	public CpPerfil cpPerfil;

	public long cpSituacaoConfiguracao;
	public Long restritividadeSitConfiguracao;

	public CpConfiguracaoCache() {
	}

	public CpConfiguracaoCache(CpConfiguracao cfg) {
		if (cfg.getId() != null)
			this.idConfiguracao = cfg.getId();
		// public String descrConfiguracao;
		this.nmEmail = cfg.getNmEmail();
		this.dscFormula = cfg.getDscFormula();
		this.hisDtIni = cfg.getHisDtIni();
		this.hisDtFim = cfg.getHisDtFim();
		this.dtIniVigConfiguracao = cfg.getDtIniVigConfiguracao();
		this.dtFimVigConfiguracao = cfg.getDtFimVigConfiguracao();

		if (cfg.getIdInicial() != null)
			this.configuracaoInicial = longOrZero(cfg.getIdInicial());

		this.orgaoUsuario = longOrZero(cfg.getOrgaoUsuario() != null ? cfg.getOrgaoUsuario().getId() : null);
		this.lotacao = longOrZero(cfg.getLotacao() != null ? cfg.getLotacao().getIdInicial() : null);
		this.complexo = longOrZero(cfg.getComplexo() != null ? cfg.getComplexo().getIdComplexo() : null);
		this.cargo = longOrZero(cfg.getCargo() != null ? cfg.getCargo().getIdInicial() : null);
		this.funcaoConfianca = longOrZero(
				cfg.getFuncaoConfianca() != null ? cfg.getFuncaoConfianca().getIdInicial() : null);
		this.dpPessoa = longOrZero(cfg.getDpPessoa() != null ? cfg.getDpPessoa().getIdInicial() : null);
		this.cpTipoConfiguracao = longOrZero(
				cfg.getCpTipoConfiguracao() != null ? cfg.getCpTipoConfiguracao().getIdTpConfiguracao() : null);
		this.cpServico = longOrZero(cfg.getCpServico() != null ? cfg.getCpServico().getId() : null);
		this.cpIdentidade = longOrZero(cfg.getCpIdentidade() != null ? cfg.getCpIdentidade().getIdInicial() : null);
		this.cpTipoLotacao = longOrZero(cfg.getCpGrupo() != null ? cfg.getCpGrupo().getIdInicial() : null);
		this.orgaoObjeto = longOrZero(cfg.getOrgaoObjeto() != null ? cfg.getOrgaoObjeto().getId() : null);
		this.lotacaoObjeto = longOrZero(cfg.getLotacaoObjeto() != null ? cfg.getLotacaoObjeto().getIdInicial() : null);
		this.complexoObjeto = longOrZero(
				cfg.getComplexoObjeto() != null ? cfg.getComplexoObjeto().getIdComplexo() : null);
		this.cargoObjeto = longOrZero(cfg.getCargoObjeto() != null ? cfg.getCargoObjeto().getIdInicial() : null);
		this.funcaoConfiancaObjeto = longOrZero(
				cfg.getFuncaoConfiancaObjeto() != null ? cfg.getFuncaoConfiancaObjeto().getIdInicial() : null);
		this.pessoaObjeto = longOrZero(cfg.getPessoaObjeto() != null ? cfg.getPessoaObjeto().getIdInicial() : null);

		this.cpGrupo = longOrZero(cfg.getCpGrupo() != null ? cfg.getCpGrupo().getIdInicial() : null);

		Object g = cfg.getCpGrupo();
		if (g != null) {
			this.cpGrupoNivel = cfg.getCpGrupo().getNivel();
			if (g instanceof HibernateProxy)
				g = ((HibernateProxy) g).getHibernateLazyInitializer().getImplementation();
			if (g instanceof CpPerfil)
				this.cpPerfil = (CpPerfil) g;
		}

		if (cfg.getCpSituacaoConfiguracao() != null) {
			this.cpSituacaoConfiguracao = longOrZero(cfg.getCpSituacaoConfiguracao().getIdSitConfiguracao());
			this.restritividadeSitConfiguracao = cfg.getCpSituacaoConfiguracao().getRestritividadeSitConfiguracao();
		}
	}

	public CpConfiguracaoCache(Object[] a) {
		if (a[0] != null)
			this.idConfiguracao = (Long) a[0];
		// public String descrConfiguracao;
		this.nmEmail = (String) a[1];
		this.dscFormula = (String) a[2];
		this.hisDtIni = (Date) a[3];
		this.hisDtFim = (Date) a[4];
		this.dtIniVigConfiguracao = (Date) a[5];
		this.dtFimVigConfiguracao = (Date) a[6];

		if (a[7] != null)
			this.configuracaoInicial = longOrZero((Long) a[7]);

		this.orgaoUsuario = longOrZero((Long) a[8]);
		this.lotacao = longOrZero((Long) a[9]);
		this.complexo = longOrZero((Long) a[10]);
		this.cargo = longOrZero((Long) a[11]);
		this.funcaoConfianca = longOrZero((Long) a[12]);
		this.dpPessoa = longOrZero((Long) a[13]);
		this.cpTipoConfiguracao = longOrZero((Long) a[14]);
		this.cpServico = longOrZero((Long) a[15]);
		this.cpIdentidade = longOrZero((Long) a[16]);
		this.cpTipoLotacao = longOrZero((Long) a[17]);
		this.orgaoObjeto = longOrZero((Long) a[18]);
		this.lotacaoObjeto = longOrZero((Long) a[19]);
		this.complexoObjeto = longOrZero((Long) a[20]);
		this.cargoObjeto = longOrZero((Long) a[21]);
		this.funcaoConfiancaObjeto = longOrZero((Long) a[22]);
		this.pessoaObjeto = longOrZero((Long) a[23]);

		if (a[24] != null) {
			CpGrupo grp = (CpGrupo) a[24];
			this.cpGrupo = longOrZero(grp.getIdInicial());
			this.cpGrupoNivel = grp.getNivel();
			if (grp instanceof HibernateProxy)
				grp = (CpGrupo) ((HibernateProxy) grp).getHibernateLazyInitializer().getImplementation();
			if (grp instanceof CpPerfil)
				this.cpPerfil = (CpPerfil) grp;
		}

		if (a[25] != null) {
			CpSituacaoConfiguracao sit = (CpSituacaoConfiguracao) a[25];
			this.cpSituacaoConfiguracao = longOrZero(sit.getIdSitConfiguracao());
			this.restritividadeSitConfiguracao = sit.getRestritividadeSitConfiguracao();
		}
	}

	public long longOrZero(Long l) {
		if (l == null)
			return 0;
		return l;
	}

	public boolean ativaNaData(Date dt) {
		if (dt == null)
			return this.hisDtFim == null;
		if (this.hisDtIni != null && dt.before(this.hisDtIni))
			return false;
		// dt >= DtIni
		if (this.hisDtFim == null)
			return true;
		if (dt.before(this.hisDtFim))
			return true;
		return false;
	}
}
