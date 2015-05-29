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
package br.gov.jfrj.siga.vraptor.builder;

import java.util.List;

import br.gov.jfrj.siga.ex.ExTipoDespacho;
import br.gov.jfrj.siga.hibernate.ExDao;

public final class ExTipoDespachoBuilder {

	private String descTpDespacho;
	private String fgAtivo;
	private Long id;
	private Long idTpDespacho;
	private List<ExTipoDespacho> tiposDespacho;

	private ExTipoDespachoBuilder() {

	}

	public static ExTipoDespachoBuilder novaInstancia() {
		return new ExTipoDespachoBuilder();
	}

	public ExTipoDespacho construir(ExDao dao) {
		ExTipoDespacho tipoDespacho;
		if (idTpDespacho != null) {
			tipoDespacho = dao.consultar(idTpDespacho, ExTipoDespacho.class, false);
		} else {
			tipoDespacho = new ExTipoDespacho();

		}

		tipoDespacho.setDescTpDespacho(descTpDespacho);
		tipoDespacho.setFgAtivo(fgAtivo);
		// se id for zero então obriga a gravar um novo
		if (idTpDespacho != null && idTpDespacho == 0)
			tipoDespacho.setIdTpDespacho(null);
		else
			tipoDespacho.setIdTpDespacho(idTpDespacho);

		return tipoDespacho;
	}

	public String getDescTpDespacho() {
		return descTpDespacho;
	}

	public Long getId() {
		return id;
	}

	public Long getIdTpDespacho() {
		return idTpDespacho;
	}

	public List<ExTipoDespacho> getTiposDespacho() {
		return tiposDespacho;
	}

	public boolean isAtivo() {
		if (fgAtivo == null) {
			return false;
		}
		return "S".equals(fgAtivo);
	}

	public ExTipoDespachoBuilder setAtivo(final String ativo) {
		fgAtivo = (ativo == null ? false : "on".equals(ativo) || "S".equals(ativo)) ? "S" : "N";
		return this;
	}

	public ExTipoDespachoBuilder setDescTpDespacho(final String descTpDespacho) {
		this.descTpDespacho = descTpDespacho;
		return this;
	}

	public ExTipoDespachoBuilder setFgAtivo(final String fgAtivo) {
		this.fgAtivo = fgAtivo;
		return this;
	}

	public ExTipoDespachoBuilder setId(final Long id) {
		this.id = id;
		return this;
	}

	public ExTipoDespachoBuilder setIdTpDespacho(final Long idTpDespacho) {
		this.idTpDespacho = idTpDespacho;
		return this;
	}

	public ExTipoDespachoBuilder setTiposDespacho(final List<ExTipoDespacho> tiposDespacho) {
		this.tiposDespacho = tiposDespacho;
		return this;
	}
}
