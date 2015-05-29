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

import java.security.cert.TrustAnchor;
import java.util.ArrayList;

/**
 * Eumerador para os Certificados auto assinados confiáveis (Truested Anchors)
 * 
 * Sempre que o sistema necessitar de um novo Truested Anchor, o mesmo deve ser
 * aqui especificado
 * 
 * @author aym
 * 
 */
public enum TrustAnchorEnum {
	AC_RAIZ_ICPBRASIL(CertificadoACEnum.AC_RAIZ_ICPBRASIL.toTrustAnchor()),
	AC_RAIZ_ICPBRASIL_V1(CertificadoACEnum.AC_RAIZ_ICPBRASIL_V1.toTrustAnchor()),
	AC_RAIZ_ICPBRASIL_V2(CertificadoACEnum.AC_RAIZ_ICPBRASIL_V2.toTrustAnchor()),
	AC_CLEPSIDRE_TIME_STAMP(CertificadoACEnum.AC_CLEPSIDRE_TIME_STAMP.toTrustAnchor()),
	AC_SZIKSZI_TIME_STAMP(CertificadoACEnum.AC_SZIKSZI_TIME_STAMP.toTrustAnchor())
	;

	TrustAnchor trustAnchor;

	TrustAnchorEnum(TrustAnchor trustAnchor) {
		this.trustAnchor = trustAnchor;
	}
	
	/**
	 * Obtém todos os trust anchors
	 * @return
	 */
	public static ArrayList<TrustAnchor>obterTrustAnchors() {
		ArrayList<TrustAnchor> trustAnchorList = new ArrayList<TrustAnchor>();
		for (TrustAnchorEnum trustAnchor : TrustAnchorEnum.values()) {
			trustAnchorList.add(trustAnchor.getTrustAnchor());
		 }
		return trustAnchorList;
	}
	
	public TrustAnchor getTrustAnchor() {
		return this.trustAnchor;
	}
}
