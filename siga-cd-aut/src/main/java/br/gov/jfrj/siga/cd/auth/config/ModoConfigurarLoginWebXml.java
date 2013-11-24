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
package br.gov.jfrj.siga.cd.auth.config;

/**
 * Enumerador dos modos de configurar os logins no web.xml
 * 
 * @author aym
 * 
 */
public enum ModoConfigurarLoginWebXml {
	CERTIFICADO {
		public String procesarLinha(String linha) {
			// certificado sem comentario
			if (isInicioLoginCertificado(linha)) {
				return getLinhaInicioLoginCertificado();
			}
			;
			if (isFimLoginCertificado(linha)) {
				return getLinhaFimLoginCertificado();
			}
			;
			// formulario comentado
			if (isInicioLoginFormulario(linha)) {
				return getLinhaInicioLoginFormularioComentado();
			}
			;
			if (isFimLoginFormulario(linha)) {
				return getLinhaFimLoginFormularioComentado();
			}
			;
			return linha;
		}

		public String toString() {
			return "CERTIFICADO";
		}
	},
	FORMULARIO {
		public String procesarLinha(String linha) {
			// certificado comentado
			if (isInicioLoginCertificado(linha)) {
				return getLinhaInicioLoginCertificadoComentado();
			}
			;
			if (isFimLoginCertificado(linha)) {
				return getLinhaFimLoginCertificadoComentado();
			}
			;
			// formulario sem comentario
			if (isInicioLoginFormulario(linha)) {
				return getLinhaInicioLoginFormulario();
			}
			;
			if (isFimLoginFormulario(linha)) {
				return getLinhaFimLoginFormulario();
			}
			;
			return linha;
		}

		public String toString() {
			return "FORMULARIO";
		}
	};
	/**
	 * Utilizado nas enumerações para comentar e descomentar os blocos
	 * referentes aos diferentes modos de login
	 * 
	 * @param linha
	 *            linha do arquivo web.xml
	 * @return linha do arquivo web.xml alterada se necessário
	 */
	public abstract String procesarLinha(String linha);

	public abstract String toString();

	/*
	 * INICIO do login por CERTIFICADO
	 */
	private static String getMarcaInicioLoginCertificado() {
		return "_$(iniLogCert)$_";
	}

	private static boolean isInicioLoginCertificado(String linha) {
		return linha.indexOf(getMarcaInicioLoginCertificado()) != -1;
	}

	private static String getLinhaInicioLoginCertificado() {
		return "	<!--  Nao alterar comentario _$(iniLogCert)$_ -->";
	}

	private static String getLinhaInicioLoginCertificadoComentado() {
		return "	<!--  Nao alterar comentario _$(iniLogCert)$_ ";
	}

	/*
	 * FIM do login por CERTIFICADO
	 */
	private static String getMarcaFimLoginCertificado() {
		return "_$(fimLogCert)$_";
	}

	private static boolean isFimLoginCertificado(String linha) {
		return linha.indexOf(getMarcaFimLoginCertificado()) != -1;
	}

	private static String getLinhaFimLoginCertificado() {
		return "	<!-- _$(fimLogCert)$_ Nao alterar comentario -->";
	}

	private static String getLinhaFimLoginCertificadoComentado() {
		return "	     _$(fimLogCert)$_ Nao alterar comentario -->";
	}

	/*
	 * INICIO do Login por FORMULARIO
	 */
	private static String getMarcaInicioLoginFormulario() {
		return "_$(iniLogForm)$_";
	}

	private static boolean isInicioLoginFormulario(String linha) {
		return linha.indexOf(getMarcaInicioLoginFormulario()) != -1;
	}

	private static String getLinhaInicioLoginFormulario() {
		return "	<!-- Nao alterar comentario _$(iniLogForm)$_ -->";
	}

	private static String getLinhaInicioLoginFormularioComentado() {
		return "	<!-- Nao alterar comentario _$(iniLogForm)$_    ";
	}

	/*
	 * FIM do Login por FORMULARIO
	 */
	private static String getMarcaFimLoginFormulario() {
		return "_$(fimLogForm)$_";
	}

	private static boolean isFimLoginFormulario(String linha) {
		return linha.indexOf(getMarcaFimLoginFormulario()) != -1;
	}

	private static String getLinhaFimLoginFormulario() {
		return "	<!-- _$(fimLogForm)$_Nao alterar comentario -->";
	}

	private static String getLinhaFimLoginFormularioComentado() {
		return "	     _$(fimLogForm)$_Nao alterar comentario -->";
	}

	/**
	 * exibe a lista de valores como uma lista de strings separadas por vírgula
	 * 
	 * @return
	 */
	public static String valuesAsString() {
		StringBuffer stb = new StringBuffer();
		boolean inicio = true;
		for (ModoConfigurarLoginWebXml value : ModoConfigurarLoginWebXml
				.values()) {
			if (inicio) {
				inicio = false;
			} else {
				stb.append(", ");
			}
			stb.append(value.toString());
		}
		return stb.toString();
	}
}
