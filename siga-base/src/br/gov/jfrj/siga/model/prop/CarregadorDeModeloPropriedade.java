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
package br.gov.jfrj.siga.model.prop;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

/**
 * Classe utilitária para a carga das propriedades
 * 
 * @author aym
 * 
 */
public class CarregadorDeModeloPropriedade {
	private static Hashtable<String, Properties> cache = new Hashtable<String, Properties>();
	private static final String NOME_ARQ_PROPS = "siga.properties";

	/*
	 * private static final String NOME_GERENTE_RAIZ = ModeloPropriedade.class
	 * .getName();
	 */
	/**
	 * Carrega as propriedades do arquivo de propriedades
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Properties obterTodas(ModeloPropriedade obj) throws Exception {
		Properties geral = new Properties();
		Class cls = obj.getClass();
		String clsNme = cls.getName();
		// retorna propriedades da cache, se tem lá
		Properties prpCache = cache.get(clsNme);
		if (prpCache != null)
			return prpCache;
		// 
		while (true) {
			Properties prp = carregarPara(cls);
			Enumeration ks = prp.keys();
			while (ks.hasMoreElements()) {
				Object k = ks.nextElement();
				if (!geral.containsKey(k)) {
					geral.put(k, prp.get(k));
				}
			}
			// verifica a superclasse se é classe PropriedadesGerais (abstrata)
			/*
			 * if (cls.getSuperclass().getName().equals(NOME_GERENTE_RAIZ))
			 * break;
			 */
			// verifica se a classe é abstrata
			if (Modifier.isAbstract(cls.getSuperclass().getModifiers()))
				break;
			cls = cls.getSuperclass();
		}
		cache.put(clsNme, geral);
		return geral;
	}

	/**
	 * retorna as propriedades para o parâmetro obj (Objeto ou classe)
	 * 
	 * @param obj
	 *            uma instancia ou [nome-da-classe].class
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static Properties carregarPara(Object obj) throws Exception {
		Properties p = new Properties();
		String nme = getClassNameOf(obj);
		InputStream is;
		try {
			is = Class.forName(nme).getResourceAsStream(NOME_ARQ_PROPS);
		} catch (ClassNotFoundException e1) {
			throw new Exception("Classe de nome " + nme + "não encontrada !");
		}
		try {
			p.load(is);
		} catch (Exception e) {
			throw new Exception("Arquivo de propriedades chamado '"
					+ NOME_ARQ_PROPS
					+ "' não conseguiu ser lido no pacote da classe " + nme
					+ ": " + e.getMessage());
		} finally {
			is.close();
		}
		return p;
	}

	/**
	 * Obtém o nome (caminho completo) da classe do objeto parâmetro.
	 * 
	 * @param obj
	 *            deve ser uma instancia ou [nome-da-classe].class
	 * @return o nome (caminho completo) da classe do objeto parâmetro.
	 */
	@SuppressWarnings("unchecked")
	private static String getClassNameOf(Object obj) {
		if (obj.getClass().getName().equals("java.lang.Class")) {
			return ((Class) obj).getName();
		} else {
			return obj.getClass().getName();
		}
	}

}
