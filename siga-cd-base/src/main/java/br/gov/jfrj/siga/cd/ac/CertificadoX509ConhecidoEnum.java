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

    /** Inicio dos certificados Anchors **/

    AC_RAIZ_ICPBRASIL(CertificadoACEnum. AC_RAIZ_ICPBRASIL.toX509Certificate()),
    AC_RAIZ_ICPBRASIL_V1(CertificadoACEnum.AC_RAIZ_ICPBRASIL_V1.toX509Certificate()),
    AC_RAIZ_ICPBRASIL_V2(CertificadoACEnum.AC_RAIZ_ICPBRASIL_V2.toX509Certificate()),
    AC_RAIZ_ICPBRASIL_V2_NOVO(CertificadoACEnum.AC_RAIZ_ICPBRASIL_V2_NOVO.toX509Certificate()),

    /** Final dos certificados Anchors **/

    ACRFBV3(CertificadoACEnum.ACRFBV3.toX509Certificate()),
    ACSINCOR_2005(CertificadoACEnum.ACSINCOR_2005.toX509Certificate()),
    ACIMESP_19052006(CertificadoACEnum.ACIMESP_19052006.toX509Certificate()),
    ACSERASASRF_08102003(CertificadoACEnum.ACSERASASRF_08102003.toX509Certificate()),
    AC_SERASA_JUS_V1(CertificadoACEnum.AC_SERASA_JUS_V1.toX509Certificate()),
    AC_OAB(CertificadoACEnum.AC_OAB.toX509Certificate()),
    AC_PR_12022004(CertificadoACEnum.AC_PR_12022004.toX509Certificate()),
    AC_FENACON_CERTISIGN_RFB_G2(CertificadoACEnum.AC_FENACON_CERTISIGN_RFB_G2.toX509Certificate()),
    AC_SINCOR_RFB_G4(CertificadoACEnum.AC_SINCOR_RFB_G4.toX509Certificate()),
    ACCAIXA(CertificadoACEnum.ACCAIXA.toX509Certificate()),
    SERASAAC_23_04_2004(CertificadoACEnum.SERASAAC_23_04_2004.toX509Certificate()),
    AC_PRODEMGE_RFB_G2(CertificadoACEnum.AC_PRODEMGE_RFB_G2.toX509Certificate()),
    ACBOAVISTA(CertificadoACEnum.ACBOAVISTA.toX509Certificate()),
    AC_IMESP_SRF_PEM(CertificadoACEnum.AC_IMESP_SRF_PEM.toX509Certificate()),
    ACCERTISIGNSPB_29112004(CertificadoACEnum.ACCERTISIGNSPB_29112004.toX509Certificate()),
    AC_DIGITALSIGN_RFB(CertificadoACEnum.AC_DIGITALSIGN_RFB.toX509Certificate()),
    ACSERPROJUS_10032006(CertificadoACEnum.ACSERPROJUS_10032006.toX509Certificate()),
    ACCERTISIGN_2004(CertificadoACEnum.ACCERTISIGN_2004.toX509Certificate()),
    ACCERTISIGNG3(CertificadoACEnum.ACCERTISIGNG3.toX509Certificate()),
    AC_BR_RFB_G3(CertificadoACEnum.AC_BR_RFB_G3.toX509Certificate()),
    ACCERTISIGNG5_V2(CertificadoACEnum.ACCERTISIGNG5_V2.toX509Certificate()),
    ACVALIDRFB(CertificadoACEnum.ACVALIDRFB.toX509Certificate()),
    AC_SERASA_ACP_V1(CertificadoACEnum.AC_SERASA_ACP_V1.toX509Certificate()),
    AC_PR_V3(CertificadoACEnum.AC_PR_V3.toX509Certificate()),
    AC_CAIXA_V1(CertificadoACEnum.AC_CAIXA_V1.toX509Certificate()),
    AC_FENACON_CERTISIGN_RFB_G3(CertificadoACEnum.AC_FENACON_CERTISIGN_RFB_G3.toX509Certificate()),
    AC_PR_20092006(CertificadoACEnum.AC_PR_20092006.toX509Certificate()),
    AC_INSTITUTO_FENACOM_RFB(CertificadoACEnum.AC_INSTITUTO_FENACOM_RFB.toX509Certificate()),
    AC_OAB_G2(CertificadoACEnum.AC_OAB_G2.toX509Certificate()),
    AC_CERTISIGN_MULTIPLA_G3(CertificadoACEnum.AC_CERTISIGN_MULTIPLA_G3.toX509Certificate()),
    AC_INSTITUTO_FENACON(CertificadoACEnum.AC_INSTITUTO_FENACON.toX509Certificate()),
    SERASAACP_22_04_2004(CertificadoACEnum.SERASAACP_22_04_2004.toX509Certificate()),
    SERASAACP(CertificadoACEnum.SERASAACP.toX509Certificate()),
    AC_NOTARIAL_RFB_G2(CertificadoACEnum.AC_NOTARIAL_RFB_G2.toX509Certificate()),
    AC_SOLUTI_MULTIPLA_V1(CertificadoACEnum.AC_SOLUTI_MULTIPLA_V1.toX509Certificate()),
    ACCAIXAPJV1(CertificadoACEnum.ACCAIXAPJV1.toX509Certificate()),
    ACCAIXAPF_2005(CertificadoACEnum.ACCAIXAPF_2005.toX509Certificate()),
    ACSERASA_CD_V1(CertificadoACEnum.ACSERASA_CD_V1.toX509Certificate()),
    SERASACD_2004(CertificadoACEnum.SERASACD_2004.toX509Certificate()),
    ACSERASARFB(CertificadoACEnum.ACSERASARFB.toX509Certificate()),
    AC_CERTISIGN_JUS_G3(CertificadoACEnum.AC_CERTISIGN_JUS_G3.toX509Certificate()),
    ACPRODEMGE_2004(CertificadoACEnum.ACPRODEMGE_2004.toX509Certificate()),
    CERTIFICADOACRAIZ(CertificadoACEnum.CERTIFICADOACRAIZ.toX509Certificate()),
    AC_CERTISIGN_RFB_G3(CertificadoACEnum.AC_CERTISIGN_RFB_G3.toX509Certificate()),
    AC_SERASA_JUS_V2(CertificadoACEnum.AC_SERASA_JUS_V2.toX509Certificate()),
    ICP_BRASIL(CertificadoACEnum.ICP_BRASIL.toX509Certificate()),
    AC_IMPRENSAOFICIALSP_G3_V2(CertificadoACEnum.AC_IMPRENSAOFICIALSP_G3_V2.toX509Certificate()),
    SERASA_CD_V2(CertificadoACEnum.SERASA_CD_V2.toX509Certificate()),
    ACFENACONCERTISIGNSRF(CertificadoACEnum.ACFENACONCERTISIGNSRF.toX509Certificate()),
    AC_PRODEST_RFB(CertificadoACEnum.AC_PRODEST_RFB.toX509Certificate()),
    SERASAAC(CertificadoACEnum.SERASAAC.toX509Certificate()),
    ACCERTISIGNSPBG5(CertificadoACEnum.ACCERTISIGNSPBG5.toX509Certificate()),
    ACSERASAJUSV2(CertificadoACEnum.ACSERASAJUSV2.toX509Certificate()),
    AC_VALID_BRASIL(CertificadoACEnum.AC_VALID_BRASIL.toX509Certificate()),
    SERPROV2(CertificadoACEnum.SERPROV2.toX509Certificate()),
    ACCAIXAPJ(CertificadoACEnum.ACCAIXAPJ.toX509Certificate()),
    SERASA_AC_V2(CertificadoACEnum.SERASA_AC_V2.toX509Certificate()),
    AUTORIDADE_CERTIFICADORA_DA_JUSTICA_V3(CertificadoACEnum.AUTORIDADE_CERTIFICADORA_DA_JUSTICA_V3.toX509Certificate()),
    AC_VALID_RFB(CertificadoACEnum.AC_VALID_RFB.toX509Certificate()),
    AC_IMESP_RFB_G2(CertificadoACEnum.AC_IMESP_RFB_G2.toX509Certificate()),
    AC_SINCOR_RFB_G2(CertificadoACEnum.AC_SINCOR_RFB_G2.toX509Certificate()),
    AC_PRODEMGE_G3(CertificadoACEnum.AC_PRODEMGE_G3.toX509Certificate()),
    ACSERASASRBV1(CertificadoACEnum.ACSERASASRBV1.toX509Certificate()),
    AC_PRODEMGE_RFB_G3(CertificadoACEnum.AC_PRODEMGE_RFB_G3.toX509Certificate()),
    AC_INST_FENACON_RFB(CertificadoACEnum.AC_INST_FENACON_RFB.toX509Certificate()),
    AC_SERASA_RFB_V2(CertificadoACEnum.AC_SERASA_RFB_V2.toX509Certificate()),
    ACSRF_2002(CertificadoACEnum.ACSRF_2002.toX509Certificate()),
    AC_SOLUTI(CertificadoACEnum.AC_SOLUTI.toX509Certificate()),
    AC_PETROBRAS_G3(CertificadoACEnum.AC_PETROBRAS_G3.toX509Certificate()),
    ACPRV4(CertificadoACEnum.ACPRV4.toX509Certificate()),
    AC_DIGITALSIGN_ACP(CertificadoACEnum.AC_DIGITALSIGN_ACP.toX509Certificate()),
    SERASAAC_05082006(CertificadoACEnum.SERASAAC_05082006.toX509Certificate()),
    ACPRV3(CertificadoACEnum.ACPRV3.toX509Certificate()),
    ACCAIXAPJ_2005(CertificadoACEnum.ACCAIXAPJ_2005.toX509Certificate()),
    ACCAIXAPFV1(CertificadoACEnum.ACCAIXAPFV1.toX509Certificate()),
    AC_PRODEMGE_SRF(CertificadoACEnum.AC_PRODEMGE_SRF.toX509Certificate()),
    SERASACD(CertificadoACEnum.SERASACD.toX509Certificate()),
    AC_SINCOR_G3(CertificadoACEnum.AC_SINCOR_G3.toX509Certificate()),
    AC_CERTISIGN_SPB_G3(CertificadoACEnum.AC_CERTISIGN_SPB_G3.toX509Certificate()),
    ACCAIXAJUSV1(CertificadoACEnum.ACCAIXAJUSV1.toX509Certificate()),
    ACFENACOR_V1(CertificadoACEnum.ACFENACOR_V1.toX509Certificate()),
    AC_CAIXA_JUS_V2(CertificadoACEnum.AC_CAIXA_JUS_V2.toX509Certificate()),
    ACDIGITALSIGNRFB(CertificadoACEnum.ACDIGITALSIGNRFB.toX509Certificate()),
    ACFENACOR2006(CertificadoACEnum.ACFENACOR2006.toX509Certificate()),
    AC_CAIXA_PJ_1_V1(CertificadoACEnum.AC_CAIXA_PJ_1_V1.toX509Certificate()),
    AC_JUS_V4(CertificadoACEnum.AC_JUS_V4.toX509Certificate()),
    AC_PETROBRAS(CertificadoACEnum.AC_PETROBRAS.toX509Certificate()),
    ACCERTISIGNMULTIPLAG5(CertificadoACEnum.ACCERTISIGNMULTIPLAG5.toX509Certificate()),
    ACSERPROJUSV4(CertificadoACEnum.ACSERPROJUSV4.toX509Certificate()),
    ACSERPROV3(CertificadoACEnum.ACSERPROV3.toX509Certificate()),
    AC_SERPRO_JUS_V3(CertificadoACEnum.AC_SERPRO_JUS_V3.toX509Certificate()),
    ACCAIXA_2005(CertificadoACEnum.ACCAIXA_2005.toX509Certificate()),
    ACCAIXAJUS_07042006(CertificadoACEnum.ACCAIXAJUS_07042006.toX509Certificate()),
    ACCAIXAPF(CertificadoACEnum.ACCAIXAPF.toX509Certificate()),
    ACJUSV3_PEM(CertificadoACEnum.ACJUSV3_PEM.toX509Certificate()),
    ACSERPROSRF_2005(CertificadoACEnum.ACSERPROSRF_2005.toX509Certificate()),
    AC_CERTISIGN_RFB_G4(CertificadoACEnum.AC_CERTISIGN_RFB_G4.toX509Certificate()),
    AC_SINCOR_G2(CertificadoACEnum.AC_SINCOR_G2.toX509Certificate()),
    ACJUSV4(CertificadoACEnum.ACJUSV4.toX509Certificate()),
    AC_SERASACD_2006(CertificadoACEnum.AC_SERASACD_2006.toX509Certificate()),
    ACCERTISIGNSRF_2003(CertificadoACEnum.ACCERTISIGNSRF_2003.toX509Certificate()),
    ACSERPRO_2005(CertificadoACEnum.ACSERPRO_2005.toX509Certificate()),
    ACIMESP_2004(CertificadoACEnum.ACIMESP_2004.toX509Certificate()),
    ACJUS_19122005(CertificadoACEnum.ACJUS_19122005.toX509Certificate()),
    ACFENACOR(CertificadoACEnum.ACFENACOR.toX509Certificate()),
    AC_VALID(CertificadoACEnum.AC_VALID.toX509Certificate()),
    ACCERTISIGNJUS_19052006(CertificadoACEnum.ACCERTISIGNJUS_19052006.toX509Certificate()),
    ACCERTISIGNSRF_2005(CertificadoACEnum.ACCERTISIGNSRF_2005.toX509Certificate()),
    AC_CMB_V3(CertificadoACEnum.AC_CMB_V3.toX509Certificate()),
    AC_SINCOR_RFB_G3(CertificadoACEnum.AC_SINCOR_RFB_G3.toX509Certificate()),
    SERPROSRF(CertificadoACEnum.SERPROSRF.toX509Certificate()),
    ACCMB(CertificadoACEnum.ACCMB.toX509Certificate()),
    ACFENACOR_05082006(CertificadoACEnum.ACFENACOR_05082006.toX509Certificate()),
    ACSERASAJUS(CertificadoACEnum.ACSERASAJUS.toX509Certificate()),
    ACPRODERJ(CertificadoACEnum.ACPRODERJ.toX509Certificate()),
    AC_CERTISIGN(CertificadoACEnum.AC_CERTISIGN.toX509Certificate()),
    ACSERPRORFBV3(CertificadoACEnum.ACSERPRORFBV3.toX509Certificate()),
    AC_NOTARIAL_RFB_G3(CertificadoACEnum.AC_NOTARIAL_RFB_G3.toX509Certificate()),
    AC_CERTISIGNSPB(CertificadoACEnum.AC_CERTISIGNSPB.toX509Certificate()),
    AC_CERTISIGNMULTIPLA(CertificadoACEnum.AC_CERTISIGNMULTIPLA.toX509Certificate()),
    AC_PRODEST_RFB_V2(CertificadoACEnum.AC_PRODEST_RFB_V2.toX509Certificate()),
    AC_CAIXA_PJ_V2(CertificadoACEnum.AC_CAIXA_PJ_V2.toX509Certificate()),
    AC_PRODEMGE_G2(CertificadoACEnum.AC_PRODEMGE_G2.toX509Certificate()),
    AC_IMESP_RFB_G3(CertificadoACEnum.AC_IMESP_RFB_G3.toX509Certificate()),
    ACSERPRO(CertificadoACEnum.ACSERPRO.toX509Certificate()),
    AC_CAIXA_PF_1_V1(CertificadoACEnum.AC_CAIXA_PF_1_V1.toX509Certificate()),
    AC_VALID_SPB(CertificadoACEnum.AC_VALID_SPB.toX509Certificate()),
    AC_BR_RFB_G2(CertificadoACEnum.AC_BR_RFB_G2.toX509Certificate()),
    ACRFBV2(CertificadoACEnum.ACRFBV2.toX509Certificate()),
    ACSERPRORFB(CertificadoACEnum.ACSERPRORFB.toX509Certificate()),
    AC_SINCOR_SRF(CertificadoACEnum.AC_SINCOR_SRF.toX509Certificate()),
    ICP_BRASILV2(CertificadoACEnum.ICP_BRASILV2.toX509Certificate()),
    ACSERPROACF_04042005(CertificadoACEnum.ACSERPROACF_04042005.toX509Certificate()),
    ACIMPRENSAOFICIAL(CertificadoACEnum.ACIMPRENSAOFICIAL.toX509Certificate()),
    ACFENACOR_03032006(CertificadoACEnum.ACFENACOR_03032006.toX509Certificate()),
    AC_SERASA_ACP_V2(CertificadoACEnum.AC_SERASA_ACP_V2.toX509Certificate()),
    ACSERASA_V1(CertificadoACEnum.ACSERASA_V1.toX509Certificate()),
    AC_CAIXA_V2(CertificadoACEnum.AC_CAIXA_V2.toX509Certificate()),
    ACPR(CertificadoACEnum.ACPR.toX509Certificate()),
    AC_CAIXA_PF_V2(CertificadoACEnum.AC_CAIXA_PF_V2.toX509Certificate()),
    AC_INSTITUTOFENACON_G2(CertificadoACEnum.AC_INSTITUTOFENACON_G2.toX509Certificate()),
    SERASAACP_04082006(CertificadoACEnum.SERASAACP_04082006.toX509Certificate()),
    ACPRV2(CertificadoACEnum.ACPRV2.toX509Certificate()),
    SERPROFINALV2(CertificadoACEnum.SERPROFINALV2.toX509Certificate()),
    ICP_BRASILV3(CertificadoACEnum.ICP_BRASILV3.toX509Certificate()),
    ACRAIZV1(CertificadoACEnum.ACRAIZV1.toX509Certificate()),
    ACCERTISIGNMULTIPLA_2004(CertificadoACEnum.ACCERTISIGNMULTIPLA_2004.toX509Certificate()),
    ACSERASASRF_16022005(CertificadoACEnum.ACSERASASRF_16022005.toX509Certificate()),
    SERPROACFV3(CertificadoACEnum.SERPROACFV3.toX509Certificate()),
    AC_CAIXA_JUSV1(CertificadoACEnum.AC_CAIXA_JUSV1.toX509Certificate()),
    ACBRSRF(CertificadoACEnum.ACBRSRF.toX509Certificate()),
    AC_INSTITUTO_FENACON_RFB_G2(CertificadoACEnum.AC_INSTITUTO_FENACON_RFB_G2.toX509Certificate()),
    AC_CERTISIGN_JUS_G2(CertificadoACEnum.AC_CERTISIGN_JUS_G2.toX509Certificate()),
    AC_CMB_V2(CertificadoACEnum.AC_CMB_V2.toX509Certificate()),
    ACCERTISIGNG6_V2(CertificadoACEnum.ACCERTISIGNG6_V2.toX509Certificate()),
    ACNOTARIALSRF(CertificadoACEnum.ACNOTARIALSRF.toX509Certificate()),
    ACSRF_2005(CertificadoACEnum.ACSRF_2005.toX509Certificate())
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
