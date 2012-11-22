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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CP_MARCADOR", schema = "CORPORATIVO")
public class CpMarcador extends AbstractCpMarcador {

	final static public long MARCADOR_EM_ELABORACAO = 1;

	final static public long MARCADOR_EM_ANDAMENTO = 2;

	final static public long MARCADOR_A_RECEBER = 3;

	final static public long MARCADOR_EXTRAVIADO = 4;

	final static public long MARCADOR_A_ARQUIVAR = 5;

	final static public long MARCADOR_ARQUIVADO_CORRENTE = 6;

	final static public long MARCADOR_A_DESCARTAR = 7;

	final static public long MARCADOR_DESCARTADO = 8;

	final static public long MARCADOR_JUNTADO = 9;

	final static public long MARCADOR_JUNTADO_EXTERNO = 16;

	final static public long MARCADOR_CANCELADO = 10;

	final static public long MARCADOR_TRANSFERIDO_A_ORGAO_EXTERNO = 11;

	final static public long MARCADOR_ARQUIVADO_INTERMEDIARIO = 12;

	final static public long MARCADOR_CAIXA_DE_ENTRADA = 14;

	final static public long MARCADOR_ARQUIVADO_PERMANENTE = 13;

	final static public long MARCADOR_PENDENTE_DE_ASSINATURA = 15;

	final static public long MARCADOR_JUNTADO_A_DOCUMENTO_EXTERNO = 16;

	final static public long MARCADOR_A_REMETER_PARA_PUBLICACAO = 17;

	final static public long MARCADOR_REMETIDO_PARA_PUBLICACAO = 18;

	final static public long MARCADOR_A_REMETER_MANUALMENTE = 19;

	final static public long MARCADOR_PUBLICADO = 20;

	final static public long MARCADOR_PUBLICACAO_SOLICITADA = 21;

	final static public long MARCADOR_DISPONIBILIZADO = 22;

	final static public long MARCADOR_EM_TRANSITO = 23;

	final static public long MARCADOR_EM_TRANSITO_ELETRONICO = 24;

	final static public long MARCADOR_COMO_SUBSCRITOR = 25;

	final static public long MARCADOR_APENSADO = 26;

	public static final long MARCADOR_COMO_GESTOR = 27;

	public static final long MARCADOR_COMO_INTERESSADO = 28;

	final static public long MARCADOR_DESPACHO_PENDENTE_DE_ASSINATURA = 29;

	final static public long MARCADOR_ANEXO_PENDENTE_DE_ASSINATURA = 30;

	final static public long MARCADOR_SOLICITACAO_A_RECEBER = 31;

	final static public long MARCADOR_SOLICITACAO_EM_ANDAMENTO = 32;

	final static public long MARCADOR_SOLICITACAO_FECHADO = 33;

	final static public long MARCADOR_SOLICITACAO_PENDENTE = 34;

	final static public long MARCADOR_SOLICITACAO_CANCELADO = 35;

	final static public long MARCADOR_ATIVO = 36;

	final static public long MARCADOR_NOVO = 37;

	final static public long MARCADOR_POPULAR = 38;

	final static public long MARCADOR_REVISAR = 39;

	final static public long MARCADOR_TOMAR_CIENCIA = 40;
	
	final static public long MARCADOR_SOLICITACAO_PRE_ATENDIMENTO = 41;

	public CpMarcador() {
		super();
	}

	public CpMarcador(Long idMarcador, String descrMarcador,
			CpTipoMarcador cpTipoMarcador) {
		super();
		setIdMarcador(idMarcador);
		setDescrMarcador(descrMarcador);
		setCpTipoMarcador(cpTipoMarcador);
	}

}
