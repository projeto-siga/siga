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
package br.gov.jfrj.siga.wf.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.jfrj.siga.cp.AbstractCpConfiguracao;
import br.gov.jfrj.siga.cp.CpConfiguracao;

@Entity
@Table(name = "sigawf.wf_configuracao")
@PrimaryKeyJoinColumn(name = "CONF_ID")
@NamedQueries({
		@NamedQuery(name = "consultarWfConfiguracoes", query = "from WfConfiguracao cfg where (:idTpConfiguracao is null or cfg.cpTipoConfiguracao.idTpConfiguracao = :idTpConfiguracao)"),
		@NamedQuery(name = "consultarCacheDeConfiguracoesWF", query = AbstractCpConfiguracao.CFG_CACHE_SELECT
				+ ", wf.definicaoDeProcedimento.id " + AbstractCpConfiguracao.CFG_CACHE_FROM
				+ " left join WfConfiguracao wf on c.idConfiguracao = wf.idConfiguracao "
				+ AbstractCpConfiguracao.CFG_CACHE_WHERE) })
public class WfConfiguracao extends CpConfiguracao {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFP_ID")
	private WfDefinicaoDeProcedimento definicaoDeProcedimento;

	public WfDefinicaoDeProcedimento getDefinicaoDeProcedimento() {
		return definicaoDeProcedimento;
	}

	public void setDefinicaoDeProcedimento(WfDefinicaoDeProcedimento definicaoDeProcedimento) {
		this.definicaoDeProcedimento = definicaoDeProcedimento;
	}

	@Override
	public void atualizarObjeto() {
		super.atualizarObjeto();
		setDefinicaoDeProcedimento(atual(getDefinicaoDeProcedimento()));
	}
}
