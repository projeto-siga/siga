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
package br.gov.jfrj.siga.ex.service;

import java.util.Date;

import javax.jws.WebService;

import br.gov.jfrj.siga.Remote;

@WebService
public interface ExService extends Remote {

	public Boolean transferir(String codigoDocumentoVia, String siglaDestino,
			String siglaCadastrante, Boolean forcarTransferencia) throws Exception;

	public Boolean isAssinado(String codigoDocumento, String siglaCadastrante)
			throws Exception;
	
	public Boolean isSemEfeito(String codigoDocumento)
			throws Exception;


	public Boolean arquivarCorrente(String codigoDocumentoVia,
			String siglaDestino, String siglaCadastrante) throws Exception;

	public Boolean podeMovimentar(String codigoDocumento,
			String siglaCadastrante) throws Exception;

	public Boolean podeTransferir(String codigoDocumento,
			String siglaCadastrante, Boolean forcarTransferencia) throws Exception;

	public Boolean isAtendente(String codigoDocumento, String siglaCadastrante)
			throws Exception;

	public String getAtendente(String codigoDocumento, String siglaTitular)
			throws Exception;

	public byte[] obterPdfPorNumeroAssinatura(String num) throws Exception;
	
	public String buscarPorCodigo(String codigo) throws Exception;
	
	public String criarVia(String codigoDocumento, String siglaCadastrante) throws Exception;

	public String form(String codigoDocumento, String variavel) throws Exception;
	
	public String toJSON(String codigo) throws Exception;

	Boolean exigirAnexo(String codigoDocumentoVia, String siglaCadastrante,
			String descricaoDoAnexo) throws Exception;
	
	public String criarDocumento(String cadastranteStr, String subscritorStr, String destinatarioStr, String destinatarioCampoExtraStr, String descricaoTipoDeDocumento, String nomeForma ,String nomeModelo, String classificacaoStr, 
			String descricaoStr, Boolean eletronico, String nomeNivelDeAcesso, String conteudo, String siglaMobilPai, Boolean finalizar) throws Exception;

}
