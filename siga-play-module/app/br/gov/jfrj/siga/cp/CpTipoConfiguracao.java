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
package br.gov.jfrj.siga.cp;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "CP_TIPO_CONFIGURACAO", schema="CORPORATIVO")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class CpTipoConfiguracao extends AbstractCpTipoConfiguracao {

	/**
	 * 
	 */
	public static final long serialVersionUID = 3624557793773660738L;

	// SIGA-EX

	public static final long TIPO_CONFIG_MOVIMENTAR = 1;

	public static final long TIPO_CONFIG_CRIAR = 2;

	public static final long TIPO_CONFIG_FINALIZAR = 3;

	public static final long TIPO_CONFIG_ELETRONICO = 4;

	public static final long TIPO_CONFIG_NIVELACESSO = 5;

	public static final long TIPO_CONFIG_ACESSAR = 6;

	public static final long TIPO_CONFIG_DIRETORFORO = 7;

	public static final long TIPO_CONFIG_REFAZER = 8;

	public static final long TIPO_CONFIG_DUPLICAR = 9;

	public static final long TIPO_CONFIG_EDITAR = 10;

	public static final long TIPO_CONFIG_EXCLUIR = 11;

	public static final long TIPO_CONFIG_EXCLUIR_ANEXO = 12;

	public static final long TIPO_CONFIG_CONFIGURAR = 13;

	public static final long TIPO_CONFIG_EXCLUIR_ANOTACAO = 14;

	public static final long TIPO_CONFIG_VISUALIZAR_IMPRESSAO = 15;

	public static final long TIPO_CONFIG_CANCELAR_VIA = 16;

	public static final long TIPO_CONFIG_CRIAR_VIA = 17;

	public static final long TIPO_CONFIG_NIVEL_ACESSO_MAXIMO = 18;

	public static final long TIPO_CONFIG_NIVEL_ACESSO_MINIMO = 19;

	public static final long TIPO_CONFIG_CADASTRAR_QUALQUER_SUBST = 20;

	public static final long TIPO_CONFIG_DEFINIR_PUBLICADORES = 21;

	public static final long TIPO_CONFIG_ATENDER_PEDIDO_PUBLICACAO = 22;

	public static final long TIPO_CONFIG_RECEBER_DOC_NAO_ASSINADO = 23;

	public static final long TIPO_CONFIG_CRIAR_DOC_FILHO = 24;

	public static final long TIPO_CONFIG_GERENCIAR_PUBLICACAO_BOLETIM = 25;

	public static final long TIPO_CONFIG_SIMULAR_USUARIO = 26;

	public static final long TIPO_CONFIG_NOTIFICAR_POR_EMAIL = 27;

	public static final long TIPO_CONFIG_CORRIGIR_ERRO = 28;

	public static final long TIPO_CONFIG_CANCELAR_MOVIMENTACAO = 29;
	
	public static final long TIPO_CONFIG_DESPACHAVEL = 30;
	
	public static final long TIPO_CONFIG_DESTINATARIO = 31;
	
	public static final long TIPO_CONFIG_UTILIZAR_EXTENSAO_EDITOR = 32;
	
	public static final long TIPO_CONFIG_UTILIZAR_EXTENSAO_CONVERSOR_HTML = 33;
	
	public static final long TIPO_CONFIG_SR_DESIGNACAO = 300;
	
	public static final long TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO = 301;

	public static final long TIPO_CONFIG_SR_PERMISSAO_USO_LISTA = 302;
	
	public static final long TIPO_CONFIG_SR_DEFINICAO_INCLUSAO_AUTOMATICA = 303;
	
	public static final long TIPO_CONFIG_SR_ABRANGENCIA_ACORDO = 304;
	
	public static final long TIPO_CONFIG_SR_ASSOCIACAO_PESQUISA = 305;

	public static final long TIPO_CONFIG_REINICIAR_NUMERACAO_TODO_ANO = 34;
	
	public static final long TIPO_CONFIG_AUTUAVEL = 35;
	// SIGA-WF

	public static final long TIPO_CONFIG_INSTANCIAR_PROCEDIMENTO = 100;

	public static final long TIPO_CONFIG_DESIGNAR_TAREFA = 101;

	// SIGA-GI

	public static final long TIPO_CONFIG_UTILIZAR_SERVICO = 200;
	public static final long TIPO_CONFIG_HABILITAR_SERVICO = 201;
	public static final long TIPO_CONFIG_HABILITAR_SERVICO_DE_DIRETORIO = 202;
	public static final long TIPO_CONFIG_PERTENCER = 203;
	public static final long TIPO_CONFIG_FAZER_LOGIN = 204;

	public static final long TIPO_CONFIG_UTILIZAR_SERVICO_OUTRA_LOTACAO = 205;
	public static final long TIPO_CONFIG_GERENCIAR_GRUPO = 206;

	public CpTipoConfiguracao() {
	}
}
