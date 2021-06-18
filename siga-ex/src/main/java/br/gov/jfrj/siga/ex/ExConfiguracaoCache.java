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
package br.gov.jfrj.siga.ex;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.dao.DaoFiltro;

public class ExConfiguracaoCache extends CpConfiguracaoCache {

	public long exTipoMovimentacao;
	public long exTipoDocumento;
	public long exTipoFormaDoc;
	public long exFormaDocumento;
	public long exModelo;
	public long exClassificacao;
	public long exVia;
	public long exNivelAcesso;
	public long exPapel;

	public ExConfiguracaoCache() {
	}

	public ExConfiguracaoCache(ExConfiguracao cfg) {
		super(cfg);
		this.exTipoMovimentacao = longOrZero(cfg.getExTipoMovimentacao() != null ? cfg.getExTipoMovimentacao().getId() : null);
		this.exTipoDocumento = longOrZero(cfg.getExTipoDocumento() != null ? cfg.getExTipoDocumento().getId() : null);
		this.exTipoFormaDoc = longOrZero(cfg.getExTipoFormaDoc() != null ? cfg.getExTipoFormaDoc().getId() : null);
		this.exFormaDocumento = longOrZero(cfg.getExFormaDocumento() != null ? cfg.getExFormaDocumento().getId() : null);
		this.exModelo = longOrZero(cfg.getExModelo() != null ? cfg.getExModelo().getIdInicial() : null);
		this.exClassificacao = longOrZero(
				cfg.getExClassificacao() != null ? cfg.getExClassificacao().getIdInicial() : null);
		this.exVia = longOrZero(cfg.getExVia() != null ? cfg.getExVia().getIdInicial() : null);
		this.exNivelAcesso = longOrZero(cfg.getExNivelAcesso() != null ? cfg.getExNivelAcesso().getIdNivelAcesso() : null);
		this.exPapel = longOrZero(cfg.getExPapel() != null ? cfg.getExPapel().getIdPapel() : null);
	}

	public ExConfiguracaoCache(Object[] a) {
		super(a);
		this.exTipoMovimentacao = longOrZero((Long) a[26]);
		this.exTipoDocumento = longOrZero((Long) a[27]);
		this.exTipoFormaDoc = longOrZero((Long) a[28]);
		this.exFormaDocumento = longOrZero((Long) a[29]);
		this.exModelo = longOrZero((Long) a[30]);
		this.exClassificacao = longOrZero((Long) a[31]);
		this.exVia = longOrZero((Long) a[32]);
		this.exNivelAcesso = longOrZero((Long) a[33]);
		this.exPapel = longOrZero((Long) a[34]);
	}
	
	public boolean podeAdicionarComoPublicador(DpPessoa titular,
			DpLotacao lotacaoTitular) {
		return (dpPessoa != 0 && titular != null && ExDao.getInstance().consultar(dpPessoa, DpPessoa.class, false)
				.getOrgaoUsuario().getId()
				.equals(titular.getOrgaoUsuario().getId()))
				|| (lotacao != 0 && lotacaoTitular != null && ExDao.getInstance().consultar(lotacao, DpLotacao.class, false)
						.getOrgaoUsuario().getId()
						.equals(lotacaoTitular.getOrgaoUsuario().getId()));
	}

}
