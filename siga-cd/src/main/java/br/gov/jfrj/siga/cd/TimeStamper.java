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

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.cms.Attribute;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.SignerId;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.tsp.TSPAlgorithms;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampToken;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * 
 * @author tah
 *
 */
public class TimeStamper {

	private static final Logger log = Logger.getLogger(TimeStamper.class);
	// private static final String SERVIDOR_CARIMBO =
	// "http://201.41.100.134:318";
	private static final String SERVIDOR_CARIMBO = SigaCdProperties.getServidorCarimbo();

	public static TimeStampToken gerarCarimboTempo(byte[] assinatura)
			throws URISyntaxException, IOException, TSPException,
			NoSuchAlgorithmException {
		TimeStampRequestGenerator reqGen = new TimeStampRequestGenerator();
		reqGen.setCertReq(true);
		log.info("Criando requisição para recuperar carimbo");

		MessageDigest md = MessageDigest.getInstance("SHA");
		md.update(assinatura);
		assinatura = md.digest();

		TimeStampRequest request = reqGen.generate(TSPAlgorithms.SHA1,
				assinatura);
		log.info("Enviando requisição para " + SERVIDOR_CARIMBO);
		TimeStampResponse response = sendRequest(request, SERVIDOR_CARIMBO);
		response.validate(request);

		TimeStampToken respToken = response.getTimeStampToken();
		byte[] token = respToken.getEncoded();
		if (token == null) {
			throw new TSPException("Nenhum token retornado");
		}
		log.info("Recebidos " + token.length + " bytes do carimbador");
		return respToken;
	}

	private static TimeStampResponse sendRequest(TimeStampRequest timestampreq,
			String servidor) throws URISyntaxException, IOException,
			TSPException {
		URI uri = new URI(servidor);
		String host = uri.getHost();
		int porta = uri.getPort();

		byte[] token = timestampreq.getEncoded();

		TimeStampResponse tsptcpipresponse = null;
		Socket socket = new Socket();
		log.info("Criando socket em: host=" + host + ", porta=" + porta);
		socket.connect(new InetSocketAddress(host, porta), 15000);
		log.debug("Socket conectada");
		DataInputStream datainputstream = new DataInputStream(socket
				.getInputStream());
		DataOutputStream dataoutputstream = new DataOutputStream(socket
				.getOutputStream());

		log.debug("Escrevendo na socket");
		dataoutputstream.writeInt(token.length + 1); // length (32-bits)
		dataoutputstream.writeByte(0); // flag (8-bits)
		dataoutputstream.write(token); // value (defined below)
		dataoutputstream.flush();
		log.debug("OutputStream atualizada");
		int i = datainputstream.readInt();
		byte byte0 = datainputstream.readByte();
		log.debug("Lendo primeiro byte do inputStream '" + byte0 + "'");

		if (byte0 == 5) {
			byte abyte1[] = new byte[i - 1];
			log.debug("Lendo todo o input stream");
			datainputstream.readFully(abyte1);
			log.debug("Criando novo time stam response: " + abyte1);
			tsptcpipresponse = new TimeStampResponse(abyte1);
			log.debug("Novo TimeStampResponde criado com sucesso: "
					+ tsptcpipresponse);
		} else {
			datainputstream.close();
			dataoutputstream.close();
			socket.close();
			throw new TSPException("Token inválido");
		}

		log.debug("Fechando streams de entrada e saáda");
		datainputstream.close();
		dataoutputstream.close();
		log.info("Fechando conexão socket");
		socket.close();

		return tsptcpipresponse;

	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main_old(String[] args) throws Exception {
		TimeStampRequestGenerator reqGen = new TimeStampRequestGenerator();

		// Dummy request for sha1
		// Sha256 "2.16.840.1.101.3.4.2.1", //
		TimeStampRequest request = reqGen.generate(TSPAlgorithms.SHA1,
				new byte[20], BigInteger.valueOf(100));

		byte[] reqData = request.getEncoded();

		URL url;
		URLConnection urlConn;
		DataOutputStream printout;
		DataInputStream input;

		Properties systemProperties = System.getProperties();
		systemProperties
				.setProperty("http.proxyHost", SigaCdProperties.getProxyHost());
		systemProperties.setProperty("http.proxyPort", SigaCdProperties.getProxyPort());

		// URL of CGI-Bin script.
		// url = new URL("http://www.cryptopro.ru/tsp/tsp.srf");
		url = new URL("http://201.41.100.134:318");
		// URL connection channel.
		urlConn = url.openConnection();
		// Let the run-time system (RTS) know that we want input.
		urlConn.setDoInput(true);
		// Let the RTS know that we want to do output.
		urlConn.setDoOutput(true);
		// No caching, we want the real thing.
		urlConn.setUseCaches(false);
		// Specify the content type.
		urlConn.setRequestProperty("Content-Type",
				"application/timestamp-query");
		urlConn.setRequestProperty("Content-Length", String
				.valueOf(reqData.length));

		// Send POST output.
		printout = new DataOutputStream(urlConn.getOutputStream());
		printout.write(reqData);
		printout.flush();
		printout.close();
		// Get response data.
		input = new DataInputStream(urlConn.getInputStream());
		TimeStampResponse response = new TimeStampResponse(input);
		input.close();

		TimeStampToken tsToken = response.getTimeStampToken();

		// tsToken.validate(cert, "BC");

		//
		// check validation
		//
		response.validate(request);

		return;
	}

	/**
	 * (at) param args (at) throws Exception
	 */
	public static void main(String[] args) throws Exception {

	}

	private static byte[] streamToByteArray(InputStream stream)
			throws Exception {
		if (stream == null) {
			return null;
		} else {
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			byte buffer[] = new byte[1024];
			int c = 0;
			while ((c = stream.read(buffer)) > 0) {
				byteArray.write(buffer, 0, c);
			}
			byteArray.flush();
			return byteArray.toByteArray();
		}
	}

	private static byte[] sign(byte[] data) {
		byte[] res = null;

		// ---- in real implementation, provide some SECURE way to get keystore
		// ---- password from user! -------
		String password = "12345"; // keystore password
		String pfxFileName = "my.pfx";

		KeyStore ks = null;
		PublicKey pub = null;
		PrivateKey priv = null;
		java.security.cert.Certificate storecert = null;
		java.security.cert.Certificate[] certChain = null;
		ArrayList certList = new ArrayList();
		CertStore certs = null;

		try {
			ks = KeyStore.getInstance("pkcs12");
			ks.load(new FileInputStream(pfxFileName), password.toCharArray());
			String ALIAS = (String) ks.aliases().nextElement();

			certChain = ks.getCertificateChain(ALIAS);
			for (int i = 0; i < certChain.length; i++)
				certList.add(certChain[i]);
			certs = CertStore.getInstance("Collection",
					new CollectionCertStoreParameters(certList), "BC");

			priv = (PrivateKey) (ks.getKey(ALIAS, password.toCharArray()));

			storecert = ks.getCertificate(ALIAS);
			pub = ks.getCertificate(ALIAS).getPublicKey();
		} catch (Exception exc) {
			System.out.println("Problem with keystore access: "
					+ exc.toString());
			return null;
		}

		// System.out.println("Public Key Format: " + pub.getFormat());
		// System.out.println("Certificate " + storecert.toString());

		byte[] contentbytes = data;

		try {
			CMSSignedDataGenerator signGen = new CMSSignedDataGenerator();

			signGen.addSigner(priv, (X509Certificate) storecert,
					CMSSignedDataGenerator.DIGEST_SHA1);
			signGen.addCertificatesAndCRLs(certs);
			CMSProcessable content = new CMSProcessableByteArray(contentbytes);

			// generate signed data
			CMSSignedData signedData = signGen.generate(content, "BC");
			// add timestamp to signed data
			signedData = addTimestamp(signedData);
			byte[] signeddata = signedData.getEncoded();
			System.out.println("Created signed message: " + signeddata.length
					+ " bytes");

			res = signeddata;
		} catch (Exception ex) {
			System.out.println("Couldn't generate CMS signed message\n"
					+ ex.toString());
		}
		return res;
	}

	/**
	 * Modyfy PKCS#7 data by adding timestamp
	 * 
	 * (at) param signedData (at) throws Exception
	 */
	public static CMSSignedData addTimestamp(CMSSignedData signedData)
			throws Exception {
		Collection ss = signedData.getSignerInfos().getSigners();
		SignerInformation si = (SignerInformation) ss.iterator().next();
		TimeStampToken tok = getTimeStampToken(si.getSignature());

		CertStore certs = tok.getCertificatesAndCRLs("Collection", "BC");
		CertStore certsAndCrls = AssinaturaDigital
				.buscarCrlParaCadaCertificado(certs);

		CMSSignedData cmssdcrl = CMSSignedData.replaceCertificatesAndCRLs(tok
				.toCMSSignedData(), certsAndCrls);

		tok = new TimeStampToken(cmssdcrl);

		ASN1InputStream asn1InputStream = new ASN1InputStream(tok.getEncoded());
		DERObject tstDER = asn1InputStream.readObject();
		DERSet ds = new DERSet(tstDER);
		Attribute a = new Attribute(
				PKCSObjectIdentifiers.id_aa_signatureTimeStampToken, ds);
		ASN1EncodableVector dv = new ASN1EncodableVector();
		dv.add(a);
		AttributeTable at = new AttributeTable(dv);
		si = SignerInformation.replaceUnsignedAttributes(si, at);
		ss.clear();
		ss.add(si);
		SignerInformationStore sis = new SignerInformationStore(ss);
		signedData = CMSSignedData.replaceSigners(signedData, sis);
		return signedData;
	}

	private static TimeStampToken getTimeStampToken(byte[] content)
			throws Exception {
		TimeStampToken tsToken;

		boolean fSTF = true;

		if (!fSTF) {
			TimeStampRequestGenerator reqGen = new TimeStampRequestGenerator();

			reqGen.setCertReq(true);

			MessageDigest md = MessageDigest.getInstance("SHA1");

			md.update(content);

			byte[] assinatura = md.digest();

			TimeStampRequest request = reqGen.generate(TSPAlgorithms.SHA1,
					assinatura);

			// TimeStampRequestGenerator reqGen = new
			// TimeStampRequestGenerator();
			//
			// // request TSA to return certificate
			// reqGen.setCertReq(true);
			//
			// // Dummy request for sha1
			// // Sha256 "2.16.840.1.101.3.4.2.1", //
			// TimeStampRequest request = reqGen.generate(TSPAlgorithms.SHA1,
			// MessageDigest.getInstance("SHA").digest(content));

			byte[] reqData = request.getEncoded();

			URL url;
			URLConnection urlConn;
			DataOutputStream printout;
			DataInputStream input;

			Properties systemProperties = System.getProperties();
			systemProperties.setProperty("http.proxyHost",
					SigaCdProperties.getProxyHost());
			systemProperties.setProperty("http.proxyPort", SigaCdProperties.getProxyPort());

			// URL of CGI-Bin script.
			//url = new URL("http://www.edelweb.fr/cgi-bin/service-tsp");
			url = new URL(SigaCdProperties.getTSPUrl());
			// url = new URL("http://www.cryptopro.ru/tsp/tsp.srf");
			// url = new URL("http://ns.szikszi.hu:8080/tsa");
			// url = new URL("http://time.certum.pl/");
			// URL connection channel.
			urlConn = url.openConnection();
			// Let the run-time system (RTS) know that we want input.
			urlConn.setDoInput(true);
			// Let the RTS know that we want to do output.
			urlConn.setDoOutput(true);
			// No caching, we want the real thing.
			urlConn.setUseCaches(false);
			// Specify the content type.
			urlConn.setRequestProperty("Content-Type",
					"application/timestamp-query");
			urlConn.setRequestProperty("Content-Length", String
					.valueOf(reqData.length));

			// Send POST output.
			printout = new DataOutputStream(urlConn.getOutputStream());
			printout.write(reqData);
			printout.flush();
			printout.close();
			// Get response data.
			input = new DataInputStream(urlConn.getInputStream());
			// byte[] ba = streamToByteArray(input);
			TimeStampResponse response = new TimeStampResponse(input);
			input.close();

			tsToken = response.getTimeStampToken();
		} else {

			tsToken = gerarCarimboTempo(content);
		}
		SignerId signer_id = tsToken.getSID();
		BigInteger cert_serial_number = signer_id.getSerialNumber();

		System.out.println("Signer ID serial " + signer_id.getSerialNumber());
		System.out.println("Signer ID issuer " + signer_id.getIssuerAsString());

		CertStore cs = tsToken.getCertificatesAndCRLs("Collection", "BC");

		Collection certs = cs.getCertificates(null);

		Iterator iter = certs.iterator();
		X509Certificate certificate = null;
		while (iter.hasNext()) {
			X509Certificate cert = (X509Certificate) iter.next();

			if (cert_serial_number != null) {
				if (cert.getSerialNumber().equals(cert_serial_number)) {
					System.out.println("using certificate with serial: "
							+ cert.getSerialNumber());
					System.out.println("using certificate with base 64: "
							+ Base64.encode(cert.getEncoded()) + "\n\n");

					certificate = cert;
				}
			} else {
				if (certificate == null) {
					certificate = cert;
				}
			}
			System.out.println("Certificate subject dn " + cert.getSubjectDN());
			System.out.println("Certificate serial " + cert.getSerialNumber());
		}

		// Nato: validação do carimbo de tempo está desabilitada porque existe
		// um problema no certificado do STF
		if (!fSTF)
			tsToken.validate(certificate, "BC");
		System.out
				.println("TS info " + tsToken.getTimeStampInfo().getGenTime());
		System.out.println("TS info " + tsToken.getTimeStampInfo());
		System.out.println("TS info "
				+ tsToken.getTimeStampInfo().getAccuracy());
		System.out.println("TS info " + tsToken.getTimeStampInfo().getNonce());
		return tsToken;
	}
}
