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
 * Criado em  21/12/2005
 *
 */
package br.gov.jfrj.siga.dp;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CP_TIPO_MARCADOR", schema = "CORPORATIVO")
public class CpTipoMarcador extends AbstractCpTipoMarcador {
	public static final long TIPO_MARCADOR_SISTEMA = 1L;
	public static final long TIPO_MARCADOR_GERAL = 2L;
	public static final long TIPO_MARCADOR_LOTACAO_E_SUBLOTACOES = 3L;
	public static final long TIPO_MARCADOR_LOTACAO = 4L;
	public static final long TIPO_MARCADOR_PESSOA = 5L;
}
