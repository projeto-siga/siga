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
package br.gov.jfrj.siga.sinc.lib;

import java.util.Date;

import br.gov.jfrj.siga.model.Assemelhavel;

public interface Sincronizavel extends Assemelhavel {

	public enum EstadoDeSincronismo {
		ALTERADO, DEPENDENTE, REMOVIDO, NOVO
	};

	public Long getId();

	public String getIdExterna();

	public void setIdExterna(String idExterna);

	public Long getIdInicial();

	public void setIdInicial(Long idInicial);

	public Date getDataInicio();

	public void setDataInicio(Date dataInicio);

	public Date getDataFim();

	public void setDataFim(Date dataFim);

	public String getLoteDeImportacao();

	public void setLoteDeImportacao(String loteDeImportacao);

	// Retorna zero se o elemento for independente de qualquer outro, ou 1, 2,
	// 3, etc conforme distância para o elemento independente mais longinquo.
	public int getNivelDeDependencia();

	public String getDescricaoExterna();
}
