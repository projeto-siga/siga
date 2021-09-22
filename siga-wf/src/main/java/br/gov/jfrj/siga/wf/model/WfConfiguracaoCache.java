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
package br.gov.jfrj.siga.wf.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.cp.converter.LongNonNullConverter;

@Entity
@Table(name = "sigawf.wf_configuracao")
@PrimaryKeyJoinColumn(name = "CONF_ID")
public class WfConfiguracaoCache extends CpConfiguracaoCache {

	@Convert(converter = LongNonNullConverter.class)
	@Column(name = "DEFP_ID")
	public long definicaoDeProcedimento;

	public WfConfiguracaoCache() {
	}

	public WfConfiguracaoCache(WfConfiguracao cfg) {
		super(cfg);
		this.definicaoDeProcedimento = longOrZero(
				cfg.getDefinicaoDeProcedimento() != null ? cfg.getDefinicaoDeProcedimento().getIdInicial() : null);
	}
}
