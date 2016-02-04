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
package br.gov.jfrj.siga.cd;

import java.io.IOException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.x509.extension.X509ExtensionUtil;

public class CertificadoUtil {
	
	/**
	 * Interpreta um dado do tipo otherName. Obs. O JDK 5.0 não tem classes que
	 * lidem com um dado do tipo OtherName. É necessário usar o BouncyCastle.
	 * 
	 * @param encoded
	 *            O dado em ASN.1.
	 * @return Um par contendo o OID e o conteúdo.
	 */
	public static Pair<ASN1ObjectIdentifier, String> getOtherName(DLSequence sequence)
			throws IOException {
		
		String conteudo = "";
		@SuppressWarnings("rawtypes")
		Enumeration en = sequence.getObjects();
    	ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) en.nextElement();
    	ASN1TaggedObject taggedObject = (ASN1TaggedObject) en.nextElement();
    	
    	ASN1Primitive obj = taggedObject.getObject();
		if (obj instanceof ASN1String) { // Certificados antigos SERASA -
			// incorretos
			conteudo = ((ASN1String) obj).getString();
		} else if (obj instanceof ASN1OctetString) { // Certificados corretos
			conteudo = new String(((ASN1OctetString) obj).getOctets(),"ISO-8859-1");
		}
		
		return new Pair<ASN1ObjectIdentifier, String>(oid, conteudo);
	}

	/**
	 * Recupera as propriedades ICP/Brasil-Pessoa Física (email e CPF)
	 * 
	 * @param cert
	 * @return
	 * @throws IOException
	 * @throws CertificateParsingException
	 */
	public static Properties recuperarPropriedadesNomesAlteranativos(
			X509Certificate cert) throws IOException,
			CertificateParsingException {
		Properties props = new Properties();
		Pair<ASN1ObjectIdentifier, String> otherName;
		
		
		Iterator<?> subjectAltNamesIt=X509ExtensionUtil.getSubjectAlternativeNames(cert).iterator();
		while (subjectAltNamesIt.hasNext()) {
		    List<?> altName=(List<?>)subjectAltNamesIt.next();
		    int type=((Integer)altName.get(0)).intValue();
		    if (type == GeneralName.rfc822Name) {
		      String email = (String) altName.get(1);
		      props.put("email", email);
		    }else if (type == GeneralName.otherName){
		    	otherName = getOtherName((DLSequence) altName.get(1));
		    	props.put(otherName.first.getId(), otherName.second);
		    }
		  }
		
//		for (List<?> subjectAlternativeName : cert.getSubjectAlternativeNames()) {
//			String email;
//			@SuppressWarnings("unused")
//			int pos;
//
//			// O primeiro elemento é um Integer com o valor 0 = otherName, 1
//			// =
//			// rfc822name etc.
//			// O segundo valor é um byte array ou uma String. Veja o javadoc
//			// de
//			// getSubjectAlternativeNames.
//			switch (((Number) subjectAlternativeName.get(0)).intValue()) {
//			case 0: // OtherName - contém CPF, CNPJ etc.
//				// o OID fica em otherName.first
//				Collection collection = X509ExtensionUtil.getSubjectAlternativeNames(cert);
//				otherName = getOtherName((byte[]) subjectAlternativeName.get(1));
//				props.put(otherName.first.getId(), otherName.second);
//				break;
//			case 1: // rfc822Name - usado para email
//				email = (String) subjectAlternativeName.get(1);
//				props.put("email", email);
//				break;
//			default:
//				break;
//			}
//		}
		return props;
	}

	/**
	 * Recuperar o CPF de um certificado
	 * 
	 * @param cert
	 * @return
	 * @throws Exception
	 */
	public static String recuperarCPF(X509Certificate cert) throws Exception {
		try {
			Properties props = recuperarPropriedadesNomesAlteranativos(cert);
			String sCPF = props.getProperty("2.16.76.1.3.1").substring(8, 19);

			@SuppressWarnings("unused")
			long lCPF = Long.valueOf(sCPF); // usado apenas para verificar se é numérico
			if (!isCPF(sCPF) ) {
				throw new Exception("O CPF encontrado não é válido!");
			}
			return sCPF;
		} catch (Exception e) {
			throw new Exception("Não foi possível obter o CPF :"
					+ e.getMessage());
		}
	}
	/**
	 * Verifica se o modo de autenticação do request é por certificado  
	 * @param request
	 * @return boolean
	 */
	public static boolean isClientCertAuth(HttpServletRequest request) {
		return HttpServletRequest.CLIENT_CERT_AUTH
				.equals(request.getAuthType());
	}

	/**
	 * Retorna o CPF a partir do certificado passado pelo request
	 * 
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public static String recuperarCPF(HttpServletRequest request) throws Exception {
		X509Certificate[] certs = (X509Certificate[]) request
				.getAttribute("javax.servlet.request.X509Certificate");
		if (certs != null) {
			// varre a cadeia de certificados
			for (X509Certificate cert : certs) {
				try {
					String cpf = CertificadoUtil.recuperarCPF(cert);
					return cpf;
				} catch (Exception e) {
					continue;
				}
			}
			throw new Exception(
					"CPF não encontrado na cadeia de certificados fornecida!");
		} else {
			if ("https".equals(request.getScheme())) {
				throw new Exception(
						"Este é um request HTTPS, mas nenhum certificado de cliente está disponível!");
			} else {
				throw new Exception("Esta não é uma requisição HTTPS!");
			}
		}
	}
	/**
	 * Verifica se o parâmetro é um CPF válido
	 * 
	 * @param cpf
	 * @return
	 */
	public static boolean isCPF(String cpf) {
		final int[] pesosCPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };
		if ((cpf == null) || (cpf.length() != 11))
			return false;
		Integer dig1 = obterDigito(cpf.substring(0, 9), pesosCPF);
		Integer dig2 = obterDigito(cpf.substring(0, 9) + dig1, pesosCPF);
		return cpf.equals(cpf.substring(0, 9) + dig1.toString()
				+ dig2.toString());
	}

	/**
	 * Cálculo de dígito verificador auxiliar do cálculo de CPF
	 * 
	 * @param str
	 * @param pesos
	 * @return
	 */
	private static int obterDigito(String str, int[] pesos) {
		int sum = 0;
		for (int ind = str.length() - 1, digito; ind >= 0; ind--) {
			digito = Integer.parseInt(str.substring(ind, ind + 1));
			sum += digito * pesos[pesos.length - str.length() + ind];
		}
		sum = 11 - sum % 11;
		return sum > 9 ? 0 : sum;
	}

}
