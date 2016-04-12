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
/* 
 * Criado em  19/09/2005
 *
 *  To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.ex.sinc;

import java.util.List;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.SigaBaseProperties;

public class ImportarXmlProperties extends SigaBaseProperties {
	/*
	 * 
	 * Antes a classe se chamava Mensagens (foi renomeada - refactor)
	 * 
	 * Atenção: foi renomeada antes do código abaixo ser comentado
	 */
	/*
	 * private static final String BUNDLE_NAME = "application";
	 * 
	 * private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
	 * .getBundle(ImportarXmlProperties.BUNDLE_NAME);
	 * 
	 * public static String getString(final String key) {
	 * 
	 * try { return ImportarXmlProperties.RESOURCE_BUNDLE.getString(key); }
	 * catch (final MissingResourceException e) { return '!' + key + '!'; } }
	 */
	private ImportarXmlProperties() {
		// construtor privado
	}

	@Override
	public String getPrefixoModulo() {
		return "siga.ex.sinc";
	}

	private static SigaBaseProperties instance = new ImportarXmlProperties();

	public static String getString(final String key) {
		try {
			return instance.obterPropriedade(key);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static List<String> getDiretorios() throws AplicacaoException {
		try {
			return instance.obterPropriedadeLista("modelo.diretorio");
		} catch (Exception e) {
			throw new AplicacaoException(
					"Não foi possível encontrar servidores de e-mail no arquivo siga.properties. Ex: servidor.smtp.0 = nome_servidor_email");
		}
	}

}
