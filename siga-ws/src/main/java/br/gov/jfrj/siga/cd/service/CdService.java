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
package br.gov.jfrj.siga.cd.service;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebService;

import br.gov.jfrj.siga.Remote;

@WebService(targetNamespace = "http://impl.service.cd.siga.jfrj.gov.br/")
public interface CdService extends Remote {

	// public final static String MIME_TYPE_CMS = "application/cms-signature";
	public final static String MIME_TYPE_PKCS7 = "application/pkcs7-signature";

	/**
	 * Verifica a assinatura digital, a partir do hash SHA1 do documento e do
	 * arquivo PKCS7.
	 * 
	 * @param digest
	 *            Hash SHA1 do documento assinado
	 * @param digestAlgorithm
	 *            O identificador do algoritmo utilizada para gerar o hash. Para
	 *            SHA1, deve ser passada a seguinte string: "1.3.14.3.2.26"
	 * @param assinatura
	 *            PKCS7 encoded contendo a assinatura e o certificado do
	 *            assinante
	 * @param fVerificarLCRs
	 *            Faz o download das listas e verifica se os certificados estão
	 *            revogados
	 * @return Retorna Common Name (CN) do assinate se tudo ocorrer
	 *         corretamente. Caso haja algum erro, retorna "ERRO: ", seguido da
	 *         mensagem de erro.
	 * @throws Exception
	 */

	@WebMethod
	byte[] produzPacoteAssinavel(byte[] certificado, byte[] certificadoHash,
			byte[] documento, boolean politica, Date dtAssinatura)
			throws Exception;

	@WebMethod
	byte[] validarECompletarPacoteAssinavel(byte[] certificado,
			byte[] documento, byte[] assinatura, boolean politica,
			Date dtAssinatura) throws Exception;

	@WebMethod
	byte[] validarECompletarAssinatura(byte[] assinatura, byte[] documento,
			boolean politica, Date dtAssinatura) throws Exception;

	@WebMethod
	String validarAssinatura(byte[] assinatura, byte[] documento,
			Date dtAssinatura, boolean verificarLCRs) throws Exception;

	@WebMethod
	String validarAssinaturaPKCS7(byte[] digest, String digestAlgorithm,
			byte[] assinatura, Date dtAssinatura, boolean verificarLCRs)
			throws Exception;

	//
	// String validarAssinaturaCMS(byte[] digest, String digestAlgorithm,
	// byte[] assinatura, Date dtAssinatura) throws Exception;
	//
	// String validarAssinaturaCMSECarimboDeTempo(byte[] digest,
	// String digestAlgorithm, byte[] assinatura, Date dtAssinatura)
	// throws Exception;
	//
	// String validarAssinatura(String mimeType, byte[] digest,
	// String digestAlgorithm, byte[] assinatura, Date dtAssinatura,
	// boolean verificarLCRs) throws Exception;
	//
	// byte[] converterPkcs7EmCMSComCertificadosLCRsECarimboDeTempo(byte[]
	// pkcs7)
	// throws Exception;
	//
	// byte[] converterPkcs7EmCMSComCertificadosLCRs(byte[] pkcs7)
	// throws Exception;

	String recuperarCPF(byte[] cms);

}
