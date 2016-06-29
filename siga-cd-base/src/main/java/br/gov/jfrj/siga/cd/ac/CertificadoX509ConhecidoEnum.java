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
package br.gov.jfrj.siga.cd.ac;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
/**
 * Enumerador dos certificados X509 conhecidos
 * @author aym
 *
 */
public enum CertificadoX509ConhecidoEnum {
	AC_RAIZ_ICPBRASIL(CertificadoACEnum.AC_RAIZ_ICPBRASIL.toX509Certificate()),
	AC_RAIZ_ICPBRASIL_V1(CertificadoACEnum.AC_RAIZ_ICPBRASIL_V1.toX509Certificate()),
	AC_JUS(CertificadoACEnum.AC_JUS.toX509Certificate()),
	AC_CAIXA_JUS(CertificadoACEnum.AC_CAIXA_JUS.toX509Certificate()),
	AC_CAIXA_JUS_V1(CertificadoACEnum.AC_CAIXA_JUS_V1.toX509Certificate()),
	AC_JUS_V3(CertificadoACEnum.AC_JUS_V3.toX509Certificate()),
	AC_CERTISIGN_JUS_G2(CertificadoACEnum.AC_CERTISIGN_JUS_G2.toX509Certificate()),
	AC_CERTISIGN_RFB_G3(CertificadoACEnum.AC_CERTISIGN_RFB_G3.toX509Certificate()),
	AC_RFB_V2(CertificadoACEnum.AC_RFB_V2.toX509Certificate()),

	AC_RAIZ_ICPBRASIL_V2(CertificadoACEnum.AC_RAIZ_ICPBRASIL_V2.toX509Certificate()),
	AC_JUS_V4(CertificadoACEnum.AC_JUS_V4.toX509Certificate()),
	AC_JUS_V4_NOVO(CertificadoACEnum.AC_JUS_V4_NOVO.toX509Certificate()),
	AC_SERPRO_JUS_V4(CertificadoACEnum.AC_SERPRO_JUS_V4.toX509Certificate()),
	AC_SERPRO_JUS_V3(CertificadoACEnum.AC_SERPRO_JUS_V3.toX509Certificate()),
	AC_SERASA_JUS_V2(CertificadoACEnum.AC_SERASA_JUS_V2.toX509Certificate()),
	AC_CERTISIGN_JUS_G3(CertificadoACEnum.AC_CERTISIGN_JUS_G3.toX509Certificate()),
	AC_CERTISIGN_JUS_G3_NOVO(CertificadoACEnum.AC_CERTISIGN_JUS_G3_NOVO.toX509Certificate()),
	AC_CAIXA_JUS_V2(CertificadoACEnum.AC_CAIXA_JUS_V2.toX509Certificate()),
	AC_CERTISIGN_G6(CertificadoACEnum.AC_CERTISIGN_G6.toX509Certificate()),
	AC_RFB_V3(CertificadoACEnum.AC_RFB_V3.toX509Certificate()),
	AC_CERTISIGN_RFB_G4(CertificadoACEnum.AC_CERTISIGN_RFB_G4.toX509Certificate()),
	AC_SERASA_JUS_V1(CertificadoACEnum.AC_SERASA_JUS_V1.toX509Certificate()),
	AC_SERPRO_V3(CertificadoACEnum.AC_SERPRO_V3.toX509Certificate()),
	AC_SERPRO_FINAL_V4(CertificadoACEnum.AC_SERPRO_FINAL_V4.toX509Certificate()),
	AC_SOLUTI_MULTIPLA_V1(CertificadoACEnum.AC_SOLUTI_MULTIPLA_V1.toX509Certificate()),
	AC_SOLUTI_V1(CertificadoACEnum.AC_SOLUTI_V1.toX509Certificate()),
	AC_SOLUTI_JUS_V1(CertificadoACEnum.AC_SOLUTI_JUS_V1.toX509Certificate())
	;
	
	
	X509Certificate certificado;
	
	CertificadoX509ConhecidoEnum(X509Certificate certificado) {
		this.certificado = certificado;
	}
	/**
	 * Obtém todos os certificados X509 conhecidos
	 * @return
	 */
	public static ArrayList<X509Certificate> obterCertificados() {
		ArrayList<X509Certificate> knownCertsList = new ArrayList<X509Certificate>();
		for (CertificadoX509ConhecidoEnum certX509 : CertificadoX509ConhecidoEnum.values()) {
			 knownCertsList.add(certX509.getCertificado());
		 }
		return knownCertsList;
	}
	/**
	 * obtém o certificado X509 correspondente
	 * @return
	 */
	public X509Certificate  getCertificado() {
		return this.certificado;
	}
	
}
