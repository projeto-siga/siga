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
package br.gov.jfrj.siga.util;

/**
 * DAO genéico para acesso às Globais
 *   
 * Classe Padrão para acesso às Globais
 * 
 * @author MQF
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 
 * Classe PSAExtraFolhaDao.
 * 
 * @author SEANS
 * 
 */

public class Mumps {

	String nulo = new String(new char[] { 0 });

	String separador = new String(new char[] { 254 });

	/**
	 * Construtor da classe
	 * 
	 * @throws Erro
	 * 
	 */
	public Mumps() {

	}

	public void close() {

	}

	public static String EnviaComando() {
		Socket socket = null;
		try {
			socket = new Socket("j50", 9718);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			String msg = "";

			// strComando=strComando.replace(" ","%20");
			// String strProcura=new String(new char[] {(char)254});
			// strComando=strComando.replace(strProcura,"%FE");
			// Create a URL for the desired page
			// Authenticator.setDefault(new MyAuthenticator());

			// URL url = new
			// URL("http://intranet/cgi/consulta.exe?MR=COMANDO&OPE="+strComando);
			// URLConnection conn = url.openConnection();
			out.write("MR=EXPXML&\n");

			out.write(getIP() + "^" + getUsuario() + "\n");
			out.flush();

			// Read all the text returned by the server
			// BufferedReader in = new BufferedReader(new
			// InputStreamReader(url.openStream()));
			String str;
			String retorno = "";
			while ((str = in.readLine()) != null) {
				System.out.println(str);
			}

			// System.out.print(retorno);
			socket.close();
			socket = null;

			// url=null;
			return retorno;
		} catch (MalformedURLException e) {
			System.out.print(e.getMessage());
			socket = null;
		} catch (IOException e) {
			System.out.print(e.getMessage());
			socket = null;
		}

		return "";
	}

	private static String getUsuario() {
		// TODO Auto-generated method stub
		return "mqf";
	}

	public static String getIP() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			String ip = "";
			// Get IP Address
			byte[] ipAddr = addr.getAddress();

			for (int i = 0; i < ipAddr.length; i++) {
				if (ipAddr[i] > 0) {
					ip = ip + "." + new Integer(ipAddr[i]).toString();
				} else {
					ip = ip + "." + new Integer(ipAddr[i] + 256).toString();
				}
			}
			return ip.substring(1);
		} catch (UnknownHostException e) {
			return "";
		}
	}

	public static String getHostName() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			// Get hostname
			return addr.getHostName();
		} catch (UnknownHostException e) {
			return "";
		}
	}

	public static void main(String[] args) {
		EnviaComando();
		// System.out.print(EnviaComando("W ^SPASIS04(\"MQF\")"));
	}

}
