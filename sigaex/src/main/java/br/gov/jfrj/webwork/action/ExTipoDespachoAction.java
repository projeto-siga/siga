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
package br.gov.jfrj.webwork.action;

import java.util.List;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExTipoDespacho;
import br.gov.jfrj.siga.libs.webwork.SigaActionSupport;

public class ExTipoDespachoAction extends ExActionSupport {

	private String descTpDespacho;

	private String fgAtivo;

	private Long id;

	private long idTpDespacho;

	private List<ExTipoDespacho> tiposDespacho;

	public String aEditar() throws Exception {
		assertAcesso("FE:Ferramentas;DESP:Tipos de despacho");

		tiposDespacho = dao().listarExTiposDespacho();
		if (getId() != null) {
			ExTipoDespacho tipo = dao().consultar(getId(),
					ExTipoDespacho.class, false);
			setIdTpDespacho(getId());
			setDescTpDespacho(tipo.getDescTpDespacho());
			setFgAtivo(tipo.getFgAtivo());
		}

		return SUCCESS;

	}

	public String aGravar() throws Exception {
		assertAcesso("FE:Ferramentas;DESP:Tipos de despacho");

		try {
			ExTipoDespacho tipoDespacho;
			dao().iniciarTransacao();
			if (getIdTpDespacho() > 0) {
				tipoDespacho = dao().consultar(getIdTpDespacho(),
						ExTipoDespacho.class, false);
			} else {
				tipoDespacho = new ExTipoDespacho();

			}

			tipoDespacho.setDescTpDespacho(getDescTpDespacho());
			tipoDespacho.setFgAtivo(getFgAtivo());
			// se id for zero então obriga a gravar um novo
			if (getIdTpDespacho() == 0)
				tipoDespacho.setIdTpDespacho(null);
			else
				tipoDespacho.setIdTpDespacho(getIdTpDespacho());

			tipoDespacho = dao().gravar(tipoDespacho);
			dao().commitTransacao();
		} catch (Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Ocorreu um Erro durante a gravação",
					0, e);
		}

		return SUCCESS;

	}

	public String aListar() throws Exception {
		assertAcesso("FE:Ferramentas;DESP:Tipos de despacho");

		tiposDespacho = dao().listarExTiposDespacho();
		return SUCCESS;
	}

	public boolean getAtivo() {
		return (fgAtivo == "S") ? true : false;
	}

	/**
	 * @return Retorna o atributo descTpDespacho.
	 */
	public String getDescTpDespacho() {
		return descTpDespacho;
	}

	/**
	 * @return Retorna o atributo fgAtivo.
	 */
	public String getFgAtivo() {
		return fgAtivo;
	}

	public Long getId() {
		return id;
	}

	/**
	 * @return Retorna o atributo idTpDespacho.
	 */
	public long getIdTpDespacho() {
		return idTpDespacho;
	}

	public List<ExTipoDespacho> getTiposDespacho() {
		return tiposDespacho;
	}

	/**
	 * @return Retorna o atributo ativo.
	 */
	public boolean isAtivo() {
		if (fgAtivo == null) {
			return false;
		}
		return fgAtivo.equals("S") ? true : false;
	}

	/**
	 * @param ativo
	 *            Atribui a ativo o valor.
	 */
	public void setAtivo(boolean ativo) {
		fgAtivo = ativo ? "S" : "N";
	}

	/**
	 * @param descTpDespacho
	 *            Atribui a descTpDespacho o valor.
	 */
	public void setDescTpDespacho(String descTpDespacho) {
		this.descTpDespacho = descTpDespacho;
	}

	/**
	 * @param fgAtivo
	 *            Atribui a fgAtivo o valor.
	 */
	public void setFgAtivo(String fgAtivo) {
		this.fgAtivo = fgAtivo;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param idTpDespacho
	 *            Atribui a idTpDespacho o valor.
	 */
	public void setIdTpDespacho(long idTpDespacho) {
		this.idTpDespacho = idTpDespacho;
	}

	public void setTiposDespacho(List<ExTipoDespacho> tiposDespacho) {
		this.tiposDespacho = tiposDespacho;
	}
}
