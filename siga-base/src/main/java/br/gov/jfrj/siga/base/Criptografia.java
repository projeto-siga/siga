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
package br.gov.jfrj.siga.base;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Base64;

/**
 * Classe responsável por criptografar dados .
 * 
 * @author kpf
 * 
 */
public class Criptografia {

	// private static final String ALGORITMO_CRIPTOGRAFICO =
	// "Blowfish/ECB/PKCS5Padding";
	private static final String ALGORITMO_CRIPTOGRAFICO = "AES";

	/**
	 * Transoforma array de bytes em String
	 * 
	 * @param buf
	 * @return
	 */
	public static String asHex(byte buf[]) {
		StringBuffer strbuf = new StringBuffer(buf.length * 2);
		int i;

		for (i = 0; i < buf.length; i++) {
			if (((int) buf[i] & 0xff) < 0x10)
				strbuf.append("0");

			strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
		}

		return strbuf.toString();
	}

	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			throw new Exception(
					"Parâmetros inválidos!\n"
							+ "Use: Criptografia [mensagem que sera criptografada] [chave da criptografia]");
		}

		String mensagem = args[0];
		String chave = new String(Base64.encode(args[1].getBytes()));

		byte[] msgCriptografada = Criptografia.criptografar(mensagem, chave);

		System.out.println("mensagem criptografada com "
				+ ALGORITMO_CRIPTOGRAFICO + " Em Hex: "
				+ asHex(msgCriptografada) + " Em Base64 "
				+ new String(Base64.encode(msgCriptografada)));

		byte[] msgDescriptografada = Criptografia.desCriptografar(
				msgCriptografada, chave);

		String msgOriginal = new String(msgDescriptografada);

		System.out.println("mensagem original: " + msgOriginal + "  Em HEX:"
				+ asHex(msgDescriptografada));
	}

	/**
	 * Criptografa a mensagem com uma chave
	 * 
	 * @param mensagem
	 *            - mensagem a ser criptografada
	 * @param chave
	 *            - chave utilizada para gerar a mensagem criptografada
	 * @return - array de byte com a mensagem criptografada. Para ver a String
	 *         use "new String(byte[] resultado)
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[] criptografar(String mensagem, String chave)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		SecretKey skey = new SecretKeySpec(getHashBytesChave(chave),
				ALGORITMO_CRIPTOGRAFICO);
		byte[] raw = skey.getEncoded();

		SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITMO_CRIPTOGRAFICO);

		Cipher cipher = Cipher.getInstance(ALGORITMO_CRIPTOGRAFICO);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		return cipher.doFinal(mensagem.getBytes());
	}

	/**
	 * Descriptografa a mensagem com um determinada chave
	 * 
	 * @param msgCriptografada
	 *            - mensagem a ser criptorafada
	 * @param chave
	 *            - chave utilizada para criptografar
	 * @return - array de byte com a mensagem descriptografada. Para ver a
	 *         String use "new String(byte[] resultado)"
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 */
	public static byte[] desCriptografar(byte[] msgCriptografada, String chave)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

		SecretKey skey = new SecretKeySpec(getHashBytesChave(chave),
				ALGORITMO_CRIPTOGRAFICO);
		byte[] raw = skey.getEncoded();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITMO_CRIPTOGRAFICO);
		Cipher cipher = Cipher.getInstance(ALGORITMO_CRIPTOGRAFICO);
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		return cipher.doFinal(msgCriptografada);
	}

	/**
	 * Gera um hash com 256 bits a partir da chave fornecida.
	 * 
	 * @param chave
	 *            - chave a ter o hash calculado
	 * @return - array com os bytes correspondente ao hash gerado
	 */
	private static byte[] getHashBytesChave(String chave) {
		return UUID.nameUUIDFromBytes(chave.getBytes()).toString()
				.replace("-", "").getBytes();
	}
}
