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
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cd.AssinaturaDigital;
import br.gov.jfrj.siga.cd.Cd;
import br.gov.jfrj.siga.cd.service.CdService;

/**
 * EstÃ¡ classe implementa os mÃ©todos de validaÃ§Ã£o e conversÃ£o de assinaturas
 * digitais. O acesso Ã  esta classe Ã© realizado via web-services, com interfaces
 * definidas no mÃ³dulo siga-ws, conforme o padrÃ£o adotados para o SIGA.
 * 
 * @author tah
 * 
 */
@WebService(serviceName = "CdService", endpointInterface = "br.gov.jfrj.siga.cd.service.CdService", targetNamespace = "http://impl.service.cd.siga.jfrj.gov.br/")
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
		assertAssinatura(assinatura);
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
		assertAssinatura(assinatura);
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
	 * Service.ERRO + "Mime Type nÃ£o pode ser nulo";
	 * 
	 * if (mimeType.equals(CdService.MIME_TYPE_PKCS7)) return
	 * validarAssinaturaPKCS7(digest, digestAlgorithm, assinatura, dtAssinatura,
	 * verificarLCRs);
	 * 
	 * if (mimeType.equals(CdService.MIME_TYPE_CMS)) return
	 * validarAssinaturaCMS(digest, digestAlgorithm, assinatura, dtAssinatura);
	 * 
	 * return Service.ERRO + "Mime Type '" + mimeType + "' invÃ¡lido"; }
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
			//return AssinaturaDigital.recuperarCPF(cms);
			return Cd.getInstance().getAssinaturaDigital().recuperarCPF(cms);
		} catch (Exception e) {
			if (!isHideStackTrace())
				e.printStackTrace(System.out);
			return Service.ERRO + e.getMessage();
		}
	}

	public byte[] validarECompletarAssinatura(byte[] assinatura,
			byte[] documento, boolean politica, Date dtAssinatura)
			throws Exception {
		assertAssinatura(assinatura);
		return Cd
				.getInstance()
				.getAssinaturaDigital()
				.validarECompletarAssinatura(assinatura, documento, politica,
						dtAssinatura);
	}

	public byte[] produzPacoteAssinavel(byte[] certificado,
			byte[] certificadoHash, byte[] documento, boolean politica,
			Date dtAssinatura) throws Exception {
		return Cd
				.getInstance()
				.getAssinaturaDigital()
				.produzPacoteAssinavel(certificado, certificadoHash, documento,
						politica, dtAssinatura);

	};

	public byte[] validarECompletarPacoteAssinavel(byte[] certificado,
			byte[] documento, byte[] assinatura, boolean politica,
			Date dtAssinatura) throws Exception {
		assertAssinatura(assinatura);
		return Cd
				.getInstance()
				.getAssinaturaDigital()
				.validarECompletarPacoteAssinavel(certificado, documento,
						assinatura, politica, dtAssinatura);
	};
	
	private void assertAssinatura(byte[] assinatura) {
		if (assinatura==null){
			throw new AplicacaoException("A assinatura nÃ£o foi enviada para validaÃ§Ã£o! Principais motivos: 1) o usuÃ¡rio cancelou "
					+ "a operaÃ§Ã£o de assinatura; 2) o usuÃ¡rio impediu que o navegador acessasse o certificado.");
		}
	};

}
