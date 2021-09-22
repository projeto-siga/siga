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
package br.gov.jfrj.siga.ex.ext;

import br.gov.jfrj.itextpdf.ConversorHtml;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.bl.ExConfiguracaoBL;

/**
 * Classe abstrata que representa uma fábrica (criador) de conversores HTML para
 * PDF.<br/>
 * Esta classe serve como ponto de extensão, permitindo a substituição de
 * tecnologias de conversão de forma isolada do resto do código. <br/>
 * <br/>
 * 
 * Para criar uma nova extensão:<br/>
 * <ol>
 * <li>Crie uma classe que estenda AbstractConversorHTMLFactory;</li>
 * <li>Configure arquivo siga.properties com a nova classe criada <br/>
 * Exemplo: <br/>
 * <br/>
 * conversor.html.factory =
 * br.gov.jfrj.siga.ex.ext.ConversorHTMLFactoryMinhaFabrica</li>
 * <li>Sobrescreva os métodos abstratos</li>
 * </ol>
 * <br/>
 * O SIGA já fornece uma implementação de fábrica padrão chamada
 * br.gov.jfrj.siga.ex.ext.ConversorHTMLFactoryOpenSource
 * 
 * 
 * 
 * 
 * 
 * @author kpf
 * 
 */
public abstract class AbstractConversorHTMLFactory {

	public static final int CONVERSOR_NHEENGATU = 0;
	public static final int CONVERSOR_FOP = 1;
	public static final int CONVERSOR_FLYING_SAUCER = 2;

	private static AbstractConversorHTMLFactory instance;

	protected AbstractConversorHTMLFactory() {

	}

	public static AbstractConversorHTMLFactory getInstance()
			throws AplicacaoException {
		if (instance == null) {
			try {
				instance = (AbstractConversorHTMLFactory) Class.forName(
						Prop.get("conversor.html.factory"))
						.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
				throw new AplicacaoException(e.getMessage());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new AplicacaoException(e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new AplicacaoException(e.getMessage());
			}
		}
		return instance;
	}

	/**
	 * Retorna um conversor baseado na configuração, documento ou texto a ser
	 * convertido.
	 * 
	 * @param conf
	 * @param doc
	 * @param strHtml
	 * @return
	 * @throws Exception
	 */
	public abstract ConversorHtml getConversor(ExConfiguracaoBL conf,
			ExDocumento doc, String strHtml) throws Exception;

	/**
	 * Retorna um conversor específico
	 * 
	 * @param conversor
	 *            - identifcador do conversor
	 * @return
	 * @throws Exception
	 */
	public abstract ConversorHtml getConversor(int conversor) throws Exception;

	/**
	 * Retorna o conversor padrão.
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract ConversorHtml getConversorPadrao() throws Exception;

	/**
	 * Retorna a extensão do conversor HTML definido na propriedade "conversor.html.ext" do arquivo siga.properties
	 * @return
	 * @throws AplicacaoException
	 */
	public ConversorHtml getExtensaoConversorHTML() throws AplicacaoException {
		return null;
	}
}
