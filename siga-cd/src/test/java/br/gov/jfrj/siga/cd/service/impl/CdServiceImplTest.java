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

import static br.gov.jfrj.siga.cd.Constants.CMS;
import static br.gov.jfrj.siga.cd.Constants.HASH_SHA1;
import static br.gov.jfrj.siga.cd.Constants.HASH_SHA1_WRONG;
import static br.gov.jfrj.siga.cd.Constants.PKCS7;
import static br.gov.jfrj.siga.cd.Constants.SIGNING_CALENDAR;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cd.ChainValidationException;

/**
 * Testa as funcionalidades de cálculo da ExCalculoPCD.
 * 
 * @author kpf
 * 
 */
public class CdServiceImplTest extends TestCase {

	private static Date SIGNING_DATE;

	private static final String EXPECTED_RESULT = "RENATO DO AMARAL CRIVANO MACHADO:13635";

	private CdServiceImpl c;

	public CdServiceImplTest() {
		c = new CdServiceImpl();
		c.setHideStackTrace(true);
		SIGNING_CALENDAR.clear();
		SIGNING_CALENDAR.set(2010, Calendar.FEBRUARY, 3, 17, 30);
		SIGNING_DATE = SIGNING_CALENDAR.getTime();
	}

	private BouncyCastleProvider bouncyCastleProvider;

	@Override
	protected void setUp() throws Exception {
		this.bouncyCastleProvider = (BouncyCastleProvider) Security
				.getProvider(BouncyCastleProvider.PROVIDER_NAME);
		if (this.bouncyCastleProvider == null) {
			this.bouncyCastleProvider = new BouncyCastleProvider();
			Security.addProvider(this.bouncyCastleProvider);
		}
		super.setUp();
	}

	public void testValidarAssinatura() {
		if (true)
			return;
		
		String s;
		s = c.validarAssinatura(HASH_SHA1, PKCS7, SIGNING_DATE, false);
		assertEquals(EXPECTED_RESULT, s);

		// Erro pois foi informada o hash errado
		s = c.validarAssinatura(HASH_SHA1_WRONG, PKCS7, SIGNING_DATE, false);
		assertTrue(s.startsWith(Service.ERRO) && s.contains("Assinatura inválida"));

		// Data de assinatura é posterior a data atual.
		s = c.validarAssinatura(HASH_SHA1, PKCS7, new Date(new Date().getTime() + 3600), false);
		assertTrue(s.startsWith(Service.ERRO));

		// Não é possível validar CRLs se a assinatura for antiga (anterior ao
		// fim de validade de algum dos certificados) e a CRL da época da
		// assinatura não estiver incluida no CMS.

		// Agora validando as CRLs
		// s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.26", PKCS7,
		// SIGNING_DATE, true);
		// assertEquals(s, EXPECTED_RESULT);

		// Mesmo que a data de assinatura seja bem anterior ao início de
		// vigência das CRLs
		// s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.26", PKCS7,
		// new Date(109, 2, 13), true);
		// assertEquals(s, EXPECTED_RESULT);

		// Os certificados ainda não haviam sido emitidos no ano de 2000
		// s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.26", PKCS7,
		// new Date(100, 0, 1), true);
		// assertTrue(s.startsWith(Service.ERRO) && s.contains("NotBefore"));

		// Os certificados perdem a validade antes de 2012
		// s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.26", PKCS7,
		// new Date(112, 0, 1), true);
		// assertTrue(s.startsWith(Service.ERRO) && s.contains("NotAfter"));

		// Data de assinatura é posterior a data atual
		// s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.26", PKCS7,
		// new Date(new Date().getTime() + 3600), true);
		// assertTrue(s.startsWith(Service.ERRO)
		// && s.contains("Validation time is in future"));
	}

	public void testValidarAssinaturaPKCS7() throws Exception,
			SecurityException, CRLException, CertificateException,
			NoSuchProviderException, NoSuchAlgorithmException,
			SignatureException, AplicacaoException, ChainValidationException,
			IOException, Exception {
		String s;
		s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.26", PKCS7,
				SIGNING_DATE, false);
		assertEquals(s, EXPECTED_RESULT);

		// Erro pois foi informada a string errada no segundo parametro
		s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.2", PKCS7,
				SIGNING_DATE, false);
		assertTrue(s.startsWith(Service.ERRO));

		// Erro pois foi informada o hash errado
		s = c.validarAssinaturaPKCS7(HASH_SHA1_WRONG, "1.3.14.3.2.26", PKCS7,
				SIGNING_DATE, false);
		assertTrue(s.startsWith(Service.ERRO)
				&& s.contains("Assinatura inválida"));

		// Data de assinatura é posterior a data atual.
		s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.26", PKCS7,
				new Date(new Date().getTime() + 3600), false);
		assertTrue(s.startsWith(Service.ERRO));

		// Não é possível validar CRLs se a assinatura for antiga (anterior ao
		// fim de validade de algum dos certificados) e a CRL da época da
		// assinatura não estiver incluida no CMS.

		// Agora validando as CRLs 
//		s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.26", PKCS7,
//				SIGNING_DATE, true);
//		assertEquals(s, EXPECTED_RESULT);

		// Mesmo que a data de assinatura seja bem anterior ao início de
		// vigência das CRLs
//		s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.26", PKCS7,
//				new Date(109, 2, 13), true);
//		assertEquals(s, EXPECTED_RESULT);

		// Os certificados ainda não haviam sido emitidos no ano de 2000 //
//		s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.26", PKCS7,
//				new Date(100, 0, 1), true);
//		assertTrue(s.startsWith(Service.ERRO) && s.contains("NotBefore"));

		// Os certificados perdem a validade antes de 2012
//		s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.26", PKCS7,
//				new Date(112, 0, 1), true);
//		assertTrue(s.startsWith(Service.ERRO) && s.contains("NotAfter"));

		// Data de assinatura é posterior a data atual
//		s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.26", PKCS7,
//				new Date(new Date().getTime() + 3600), true);
//		assertTrue(s.startsWith(Service.ERRO)
//				&& s.contains("Validation time is in future"));
	}

	/*
	public void testValidarAssinaturaPKCS7() throws Exception,
	SecurityException, CRLException, CertificateException,
	NoSuchProviderException, NoSuchAlgorithmException,
	SignatureException, AplicacaoException, ChainValidationException,
	IOException, Exception {
String s;
s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.26", PKCS7,
		SIGNING_DATE, false);
assertEquals(s, EXPECTED_RESULT);

// Erro pois foi informada a string errada no segundo parametro
s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.2", PKCS7,
		SIGNING_DATE, false);
assertTrue(s.startsWith(Service.ERRO));

// Erro pois foi informada o hash errado
s = c.validarAssinaturaPKCS7(HASH_SHA1_WRONG, "1.3.14.3.2.26", PKCS7,
		SIGNING_DATE, false);
assertTrue(s.startsWith(Service.ERRO)
		&& s.contains("Assinatura inválida"));

// Data de assinatura é posterior a data atual.
s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.26", PKCS7,
		new Date(new Date().getTime() + 3600), false);
assertTrue(s.startsWith(Service.ERRO));

// Não é possível validar CRLs se a assinatura for antiga (anterior ao
// fim de validade de algum dos certificados) e a CRL da época da
// assinatura não estiver incluida no CMS.

// Agora validando as CRLs //
s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.26", PKCS7,
		SIGNING_DATE, true);
assertEquals(s, EXPECTED_RESULT);

// Mesmo que a data de assinatura seja bem anterior ao início de
// vigência das CRLs
s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.26", PKCS7,
		new Date(109, 2, 13), true);
assertEquals(s, EXPECTED_RESULT);

// Os certificados ainda não haviam sido emitidos no ano de 2000 //
s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.26", PKCS7,
		new Date(100, 0, 1), true);
assertTrue(s.startsWith(Service.ERRO) && s.contains("NotBefore"));

// Os certificados perdem a validade antes de 2012
s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.26", PKCS7,
		new Date(112, 0, 1), true);
assertTrue(s.startsWith(Service.ERRO) && s.contains("NotAfter"));

// Data de assinatura é posterior a data atual
s = c.validarAssinaturaPKCS7(HASH_SHA1, "1.3.14.3.2.26", PKCS7,
		new Date(new Date().getTime() + 3600), true);
assertTrue(s.startsWith(Service.ERRO)
		&& s.contains("Validation time is in future"));
}
*/
	
	/*
	 * public void testValidarAssinaturaCMS() { testValidarAssinaturaCMS(CMS,
	 * SIGNING_DATE); }
	 * 
	 * public void testValidarAssinaturaCMS(byte[] cms, Date signingDate) {
	 * String s; s = c .validarAssinaturaCMS(HASH_SHA1, "1.3.14.3.2.26", cms,
	 * signingDate); assertEquals(s, EXPECTED_RESULT);
	 * 
	 * // Erro pois foi informada a string errada no segundo parametro s =
	 * c.validarAssinaturaCMS(HASH_SHA1, "1.3.14.3.2.2", cms, signingDate);
	 * assertTrue(s.startsWith(Service.ERRO));
	 * 
	 * // Erro pois foi informada o hash errado s =
	 * c.validarAssinaturaCMS(HASH_SHA1_WRONG, "1.3.14.3.2.26", cms,
	 * signingDate); assertTrue(s.startsWith(Service.ERRO) &&
	 * s.contains("Assinatura inválida"));
	 * 
	 * // Quando a data de assinatura for anterior ao início de // vigência das
	 * CRLs, deve dar erro s = c.validarAssinaturaCMS(HASH_SHA1,
	 * "1.3.14.3.2.26", cms, new Date( 109, 2, 13));
	 * assertTrue(s.startsWith(Service.ERRO) && s.contains("No CRLs found"));
	 * 
	 * // Se for recebido um PKCS7 em vez de um CMS completo, não deve validar
	 * // pois não possui as CRLs s = c.validarAssinaturaCMS(HASH_SHA1,
	 * "1.3.14.3.2.26", PKCS7, signingDate);
	 * assertTrue(s.startsWith(Service.ERRO) && s.contains("No CRLs found"));
	 * 
	 * // Os certificados ainda não haviam sido emitidos no ano de 2000 s =
	 * c.validarAssinaturaCMS(HASH_SHA1, "1.3.14.3.2.26", cms, new Date( 100, 0,
	 * 1)); assertTrue(s.startsWith(Service.ERRO) && s.contains("NotBefore"));
	 * 
	 * // Os certificados perdem a validade antes de 2012 s =
	 * c.validarAssinaturaCMS(HASH_SHA1, "1.3.14.3.2.26", cms, new Date( 112, 0,
	 * 1)); assertTrue(s.startsWith(Service.ERRO) && s.contains("NotAfter"));
	 * 
	 * // Data de assinatura é posterior a data atual s =
	 * c.validarAssinaturaCMS(HASH_SHA1, "1.3.14.3.2.26", cms, new Date( new
	 * Date().getTime() + 3600)); assertTrue(s.startsWith(Service.ERRO) &&
	 * (s.contains("Validation time is in future") || s .contains("NotAfter")));
	 * }
	 * 
	 * 
	 * // Desabilitado porque o carimbador está fora do ar... // public void
	 * testConverterPkcs7EmCMSComCertificadosLCRsECarimboDeTempo() { // byte[]
	 * cms = c // .converterPkcs7EmCMSComCertificadosLCRsECarimboDeTempo(PKCS7);
	 * // // if (Service.isError(cms)) // fail(Service.retrieveError(cms)); //
	 * // testValidarAssinaturaCMSeCarimboDeTempo(cms, new Date()); // } //
	 * public void testValidarAssinaturaCMSeCarimboDeTempo() { // byte[] cms = c
	 * // .converterPkcs7EmCMSComCertificadosLCRsECarimboDeTempo(PKCS7); // //
	 * if (Service.isError(cms)) // assertEquals(Service.retrieveError(cms),
	 * ""); // // testValidarAssinaturaCMSeCarimboDeTempo(cms, new Date()); // }
	 * 
	 * 
	 * 
	 * // Desabilitado porque o exemplo de assinatura que estamos usando possui
	 * um certificado já expirado. Dessa forma, não é possível obter as LCRs
	 * necessárias. // public void testConverterPkcs7EmCMSComCertificadosLCRs()
	 * { // byte[] cms = c.converterPkcs7EmCMSComCertificadosLCRs(PKCS7); // //
	 * if (Service.isError(cms)) // fail(Service.retrieveError(cms)); // //
	 * testValidarAssinaturaCMS(cms, new Date()); // }
	 * 
	 * public void testValidarAssinaturaCMSeCarimboDeTempo(byte[] cms, Date
	 * signingDate) { String s; s =
	 * c.validarAssinaturaCMSECarimboDeTempo(HASH_SHA1, "1.3.14.3.2.26", cms,
	 * signingDate); if (Service.isError(s)) assertEquals(s, ""); }
	 */
	public void testRecuperarCPF() {
		String sCPF = c.recuperarCPF(PKCS7);
		assertEquals(sCPF, "00489623760");

		sCPF = c.recuperarCPF(CMS);
		assertEquals(sCPF, "00489623760");
	}

}
