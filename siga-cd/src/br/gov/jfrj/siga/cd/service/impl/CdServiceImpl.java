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
package br.gov.jfrj.siga.cd.service.impl;

import java.util.Date;

import javax.jws.WebService;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.cd.AssinaturaDigital;
import br.gov.jfrj.siga.cd.Cd;
import br.gov.jfrj.siga.cd.service.CdService;

/**
 * Está classe implementa os métodos de validação e conversão de assinaturas
 * digitais. O acesso à esta classe é realizado via web-services, com interfaces
 * definidas no módulo siga-ws, conforme o padrão adotados para o SIGA.
 * 
 * @author tah
 * 
 */
@WebService(endpointInterface = "br.gov.jfrj.siga.cd.service.CdService")
public class CdServiceImpl implements CdService {

	private boolean hideStackTrace = false;

	public boolean isHideStackTrace() {
		return hideStackTrace;
	}

	public void setHideStackTrace(boolean hideStackTrace) {
		this.hideStackTrace = hideStackTrace;
	}

	public String validarAssinatura(byte[] assinatura, byte[] documento,
			Date dtAssinatura, boolean verificarLCRs) {
		try {
			return Cd
					.getInstance()
					.getAssinaturaDigital()
					.validarAssinatura(assinatura, documento, dtAssinatura,
							verificarLCRs);
		} catch (Exception e) {
			if (!isHideStackTrace())
				e.printStackTrace(System.out);
			return Service.ERRO + e.getMessage();
		}
	}

	public String validarAssinaturaPKCS7(byte[] digest, String digestAlgorithm,
			byte[] assinatura, Date dtAssinatura, boolean verificarLCRs) {
		try {
			return AssinaturaDigital.validarAssinaturaPKCS7(digest,
					digestAlgorithm, assinatura, dtAssinatura, verificarLCRs);
		} catch (Exception e) {
			if (!isHideStackTrace())
				e.printStackTrace(System.out);
			return Service.ERRO + e.getMessage();
		}
	}

	/*
	 * public String validarAssinaturaCMS(byte[] digest, String digestAlgorithm,
	 * byte[] assinatura, Date dtAssinatura) { try { return
	 * AssinaturaDigital.validarAssinaturaCMS(digest, digestAlgorithm,
	 * assinatura, dtAssinatura); } catch (Exception e) { if
	 * (!isHideStackTrace()) e.printStackTrace(System.out); return Service.ERRO
	 * + e.getMessage(); } }
	 * 
	 * public String validarAssinaturaCMSECarimboDeTempo(byte[] digest, String
	 * digestAlgorithm, byte[] assinatura, Date dtAssinatura) { try { return
	 * AssinaturaDigital.validarAssinaturaCMSeCarimboDeTempo( digest,
	 * digestAlgorithm, assinatura, dtAssinatura); } catch (Exception e) { if
	 * (!isHideStackTrace()) e.printStackTrace(System.out); return Service.ERRO
	 * + e.getMessage(); } }
	 * 
	 * public String validarAssinatura(String mimeType, byte[] digest, String
	 * digestAlgorithm, byte[] assinatura, Date dtAssinatura, boolean
	 * verificarLCRs) throws Exception { if (mimeType == null) return
	 * Service.ERRO + "Mime Type não pode ser nulo";
	 * 
	 * if (mimeType.equals(CdService.MIME_TYPE_PKCS7)) return
	 * validarAssinaturaPKCS7(digest, digestAlgorithm, assinatura, dtAssinatura,
	 * verificarLCRs);
	 * 
	 * if (mimeType.equals(CdService.MIME_TYPE_CMS)) return
	 * validarAssinaturaCMS(digest, digestAlgorithm, assinatura, dtAssinatura);
	 * 
	 * return Service.ERRO + "Mime Type '" + mimeType + "' inválido"; }
	 * 
	 * public byte[] converterPkcs7EmCMSComCertificadosLCRsECarimboDeTempo(
	 * byte[] pkcs7) { try { return AssinaturaDigital
	 * .converterPkcs7EmCMSComCertificadosCRLsECarimboDeTempo(pkcs7); } catch
	 * (Exception e) { if (!isHideStackTrace()) e.printStackTrace(System.out);
	 * try { return (Service.ERRO + e.getMessage()).getBytes("UTF-8"); } catch
	 * (Exception e2) { return null; } } }
	 * 
	 * public byte[] converterPkcs7EmCMSComCertificadosLCRs(byte[] pkcs7) { try
	 * { return AssinaturaDigital
	 * .converterPkcs7EmCMSComCertificadosECRLs(pkcs7); } catch (Exception e) {
	 * if (!isHideStackTrace()) e.printStackTrace(System.out); try { return
	 * (Service.ERRO + e.getMessage()).getBytes("UTF-8"); } catch (Exception e2)
	 * { return null; } } }
	 */

	public String recuperarCPF(byte[] cms) {
		try {
			return AssinaturaDigital.recuperarCPF(cms);
			//return Cd.getInstance().getAssinaturaDigital().recuperarCPF(cms);
		} catch (Exception e) {
			if (!isHideStackTrace())
				e.printStackTrace(System.out);
			return Service.ERRO + e.getMessage();
		}
	}

	public byte[] validarECompletarAssinatura(byte[] assinatura,
			byte[] documento, String sArquivoPolitica, Date dtAssinatura)
			throws Exception {
		return Cd
				.getInstance()
				.getAssinaturaDigital()
				.validarECompletarAssinatura(assinatura, documento,
						sArquivoPolitica, dtAssinatura);
	}
}
