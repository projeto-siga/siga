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
 * Created Mon Nov 14 13:33:08 GMT-03:00 2005 by MyEclipse Hibernate Tool.
 */
package br.gov.jfrj.siga.ex;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Selecionavel;

/**
 * A class that represents a row in the 'EX_TIPO_MOVIMENTACAO' table. This class
 * may be customized as it is never re-generated after being created.
 */
@SuppressWarnings("serial")
@Entity
@BatchSize(size = 500)
@Immutable
@Cacheable
@Cache(region = CpDao.CACHE_HOURS, usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "siga.ex_tipo_movimentacao")
public class ExTipoMovimentacao extends AbstractExTipoMovimentacao implements Serializable, Selecionavel {

	final static public long TIPO_MOVIMENTACAO_CRIACAO = 1;

	final static public long TIPO_MOVIMENTACAO_ANEXACAO = 2;

	final static public long TIPO_MOVIMENTACAO_TRANSFERENCIA = 3;

	final static public long TIPO_MOVIMENTACAO_RECEBIMENTO = 4;

	final static public long TIPO_MOVIMENTACAO_DESPACHO = 5;

	final static public long TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA = 6;

	final static public long TIPO_MOVIMENTACAO_DESPACHO_INTERNO = 7;

	final static public long TIPO_MOVIMENTACAO_DESPACHO_INTERNO_TRANSFERENCIA = 8;

	final static public long TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE = 9;

	// TODO: remover o teste da existencia de tipo de movimentacao de eliminacao
	// na hora da busca, pois isso aumenta muito o tempo do query. Se for o
	// caso, criar um DNM_ELIMINADO.
	final static public long TIPO_MOVIMENTACAO_ELIMINACAO = 10;

	final static public long TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO = 11;

	final static public long TIPO_MOVIMENTACAO_JUNTADA = 12;

	final static public long TIPO_MOVIMENTACAO_JUNTADA_EXTERNO = 31;

	final static public long TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA = 13;

	final static public long TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO = 14;

	final static public long TIPO_MOVIMENTACAO_EXTRAVIO = 15;

	final static public long TIPO_MOVIMENTACAO_REFERENCIA = 16;

	// final static public long TIPO_MOVIMENTACAO_CANCELAMENTO = 17;

	final static public long TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA = 17;

	final static public long TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA = 18;

	final static public long TIPO_MOVIMENTACAO_ARQUIVAMENTO_PERMANENTE = 19;

	final static public long TIPO_MOVIMENTACAO_ARQUIVAMENTO_INTERMEDIARIO = 20;

	final static public long TIPO_MOVIMENTACAO_DESARQUIVAMENTO_CORRENTE = 21;

	final static public long TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO = 22;

	final static public long TIPO_MOVIMENTACAO_RECEBIMENTO_TRANSITORIO = 23;

	final static public long TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO = 24;

	final static public long TIPO_MOVIMENTACAO_REGISTRO_ASSINATURA_DOCUMENTO = 25;

	final static public long TIPO_MOVIMENTACAO_REGISTRO_ASSINATURA_MOVIMENTACAO = 26;

	final static public long TIPO_MOVIMENTACAO_ATUALIZACAO = 27;

	final static public long TIPO_MOVIMENTACAO_ANOTACAO = 28;

	final static public long TIPO_MOVIMENTACAO_REDEFINICAO_NIVEL_ACESSO = 29;

	final static public long TIPO_MOVIMENTACAO_REGISTRO_ACESSO_INDEVIDO = 30;

	final static public long TIPO_MOVIMENTACAO_JUNTADA_A_DOCUMENTO_EXTERNO = 31;

	final static public long TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO = 32;

	final static public long TIPO_MOVIMENTACAO_REMESSA_PARA_PUBLICACAO = 33;

	final static public long TIPO_MOVIMENTACAO_CONFIRMACAO_DE_REMESSA_MANUAL = 34;

	final static public long TIPO_MOVIMENTACAO_DISPONIBILIZACAO = 35;

	final static public long TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO_BOLETIM = 36;

	final static public long TIPO_MOVIMENTACAO_PUBLICACAO_BOLETIM = 37;

	final static public long TIPO_MOVIMENTACAO_PEDIDO_PUBLICACAO = 38;

	// final static public long TIPO_MOVIMENTACAO_DISPONIBILIZACAO = 39;

	final static public long TIPO_MOVIMENTACAO_NOTIFICACAO_PUBL_BI = 40;

	final static public long TIPO_MOVIMENTACAO_APENSACAO = 41;

	final static public long TIPO_MOVIMENTACAO_DESAPENSACAO = 42;

	final static public long TIPO_MOVIMENTACAO_ENCERRAMENTO_DE_VOLUME = 43;

	final static public long TIPO_MOVIMENTACAO_VINCULACAO_PAPEL = 44;

	final static public long TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO = 45;

	final static public long TIPO_MOVIMENTACAO_SOBRESTAR = 46;

	final static public long TIPO_MOVIMENTACAO_DESOBRESTAR = 47;

	final static public long TIPO_MOVIMENTACAO_TORNAR_SEM_EFEITO = 48;

	final static public long TIPO_MOVIMENTACAO_INDICACAO_GUARDA_PERMANENTE = 49;

	final static public long TIPO_MOVIMENTACAO_REVERSAO_INDICACAO_GUARDA_PERMANENTE = 50;

	final static public long TIPO_MOVIMENTACAO_RECLASSIFICACAO = 51;

	final static public long TIPO_MOVIMENTACAO_AVALIACAO = 52;

	final static public long TIPO_MOVIMENTACAO_AVALIACAO_COM_RECLASSIFICACAO = 53;

	final static public long TIPO_MOVIMENTACAO_INCLUSAO_EM_EDITAL_DE_ELIMINACAO = 54;

	final static public long TIPO_MOVIMENTACAO_RETIRADA_DE_EDITAL_DE_ELIMINACAO = 55;

	final static public long TIPO_MOVIMENTACAO_DESARQUIVAMENTO_INTERMEDIARIO = 56;

	final static public long TIPO_MOVIMENTACAO_PENDENCIA_DE_ANEXACAO = 57;

	final static public long TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA = 58;

	final static public long TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA = 59;

	final static public long TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA = 60;

	final static public long TIPO_MOVIMENTACAO_CONTROLE_DE_COLABORACAO = 61;

	final static public long TIPO_MOVIMENTACAO_MARCACAO = 62;

	final static public long TIPO_MOVIMENTACAO_COPIA = 63;

	final static public long TIPO_MOVIMENTACAO_ANEXACAO_DE_ARQUIVO_AUXILIAR = 64;

	final static public long TIPO_MOVIMENTACAO_SOLICITACAO_DE_ASSINATURA = 65;
	
	final static public long TIPO_MOVIMENTACAO_CIENCIA = 66;
	
	final static public long TIPO_MOVIMENTACAO_AUTUAR = 67;
	
	final static public long TIPO_MOVIMENTACAO_RESTRINGIR_ACESSO = 70;
	
	final static public long TIPO_MOVIMENTACAO_REFAZER = 71;
	
	final static public long TIPO_MOVIMENTACAO_ASSINATURA_POR = 72;
	
	final static public long TIPO_MOVIMENTACAO_GERAR_PROTOCOLO = 73;
	
	/*
	 * alteracao para insercao do historico do substituto de assinatura
	 */
	
	final static public long TIPO_MOVIMENTACAO_SUBSTITUICAO_RESPONSAVEL = 74;
	
	final static public long TIPO_MOVIMENTACAO_REORDENACAO_DOCUMENTO = 75;
	
	final static public long TIPO_MOVIMENTACAO_ORDENACAO_ORIGINAL_DOCUMENTO = 76;

	final static public long TIPO_MOVIMENTACAO_PUBLICACAO_PORTAL_TRANSPARENCIA = 77;
	
	final static public long TIPO_MOVIMENTACAO_EXIBIR_NO_ACOMPANHAMENTO_DO_PROTOCOLO = 79;
	
	final static public long TIPO_MOVIMENTACAO_PRAZO_ASSINATURA = 81;
	
	final static public long TIPO_MOVIMENTACAO_TRAMITE_PARALELO = 82;

	final static public long TIPO_MOVIMENTACAO_NOTIFICACAO = 83;

	final static public long TIPO_MOVIMENTACAO_CONCLUSAO = 84;
	
	public static boolean hasDespacho(long id) {
		return id == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO
				|| id == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO
				|| id == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO_TRANSFERENCIA
				|| id == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA
				|| id == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA;
	}

	public static boolean hasDocumento(long id) {
		return id == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO || hasDespacho(id);
	}

	/**
	 * Simple constructor of ExTipoMovimentacao instances.
	 */
	public ExTipoMovimentacao() {
	}

	public Long getId() {
		return this.getIdTpMov();
	}

	public String getSigla() {
		return getDescrTipoMovimentacao();
	}

	public void setSigla(final String sigla) {
	}

	public String getDescricao() {
		return getDescrTipoMovimentacao();
	}

	public static boolean hasApensacao(long id) {
		return id == ExTipoMovimentacao.TIPO_MOVIMENTACAO_APENSACAO
				|| id == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESAPENSACAO;
	}

	public static boolean hasTransferencia(long id) {
		return id == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO_TRANSFERENCIA
				|| id == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA
				|| id == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA
				|| id == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA
				|| id == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA;
	}

	/* Add customized code below */

}