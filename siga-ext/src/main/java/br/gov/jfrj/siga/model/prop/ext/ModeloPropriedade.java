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
package br.gov.jfrj.siga.model.prop.ext;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Superclasse abstrata responsável pela infraestrutura de tratamento de
 * propriedades Toda classe que tratar propriedades estáticas deve subclasseá-la
 * e criar um arquivo de propriedades chamado 'app.properties' no mesmo pacote
 * que ela.
 * 
 * @author aym
 * 
 */
public abstract class ModeloPropriedade {
	private Properties propriedades = null;
	protected String[] prefixo;

	/**
	 * Retorna o prefixo do módulo (programa), fazendo uma separação de
	 * namespaces para cada projeto. Esse prefixo é adicionado à propriedade que
	 * o usuário deseja buscar. Por exemplo, se for buscada uma propriedade
	 * "teste" no módulo siga.cd (projeto siga-cd), equivale à solicitar uma
	 * propriedade "siga.cd.teste" <br/>
	 * Veja alguns exemplos:
	 * <table border="1">
	 * <tr>
	 * <th>projeto</th>
	 * <th>prefixo do módulo</th>
	 * </tr>
	 * <tr>
	 * <td>siga-cp</td>
	 * <td>siga.cp</td>
	 * </tr>
	 * <tr>
	 * <td>siga-ex</td>
	 * <td>siga.ex</td>
	 * </tr>
	 * <tr>
	 * <td>siga-ldap</td>
	 * <td>siga.ldap</td>
	 * </tr>
	 * <tr>
	 * <td>siga-base</td>
	 * <td>siga.base</td>
	 * </tr>
	 * <tr>
	 * <td>siga-cd-base</td>
	 * <td>siga.cd.base</td>
	 * </tr>
	 * </table>
	 * 
	 * @return
	 */
	public abstract String getPrefixoModulo();

	public ModeloPropriedade() {
		setPrefixo(getPrefixoModulo());
	}

	/**
	 * Carrega as propriedades
	 * 
	 * @throws Exception
	 */
	private void carregarPropriedades() throws Exception {
		if (propriedades == null) {
			propriedades = CarregadorDeModeloPropriedade.obterTodas(this);
		}
	}

	/**
	 * atribui o prefixo para as propriedades. Note que se um prefixo for, por
	 * exemplo, "a.b.c", então as combinações de busca nos arquivos de
	 * propriedades serão a.b.c.[propriedade], b.c.[propriedade],
	 * c.[propriedade] e [propriedade]
	 * 
	 * @param prefstr
	 *            a string de parâmetros contendo os prefixos separados por
	 *            pontos
	 */
	public void setPrefixo(String prefstr) {
		prefixo = prefstr.split("\\.");
	}

	public String getPrefixo() {
		String pars = new String();
		for (int i = 0; i < prefixo.length; i++) {
			if (i == prefixo.length - 1) {
				pars += prefixo[i];
			} else {
				pars += prefixo[i] + ".";
			}
		}
		return pars;
	}

	/**
	 * Obtém a propriedade referente ao nome (parâmetro) ou null se não existir.
	 * obs-1: Note que o parâmetro tem herança e é procurado da subclasse para
	 * as superclasses, assim com um objeto. obs-2: Note também que se
	 * setPrefixo(prefixo) for executado, a procura do parâmetro será feita pela
	 * combinação dos prefixos gerados por 'prefixo'. Ex: se for executado
	 * setPrefixo("a.b.c"), então quando executado o método
	 * obterPropriedade("um.dois.tres") será procurada propriedade na seguinte
	 * ordem ....: 1) a.b.c.um.dois.tres 2) b.c.um.dois.tres 3) c.um.dois.tres
	 * 4) um.dois.tres
	 * 
	 * @param nome
	 *            nome da propriedade a carregar.
	 * @return propriedade referente ao nome (parâmetro) ou null.
	 * @throws Exception
	 */
	public String obterPropriedade(String nome) throws Exception {
		carregarPropriedades();
		if (prefixo != null) {
			for (int i = 0; i < prefixo.length; i++) {
				String prop = new String();
				for (int j = i; j < prefixo.length; j++) {
					prop += prefixo[j] + ".";
				}
				String value = propriedades.getProperty(prop + nome);
				if (value != null)
					return value.trim();
			}
		}
		if (propriedades.getProperty(nome) != null) {
			return propriedades.getProperty(nome).trim();
		}
		return null;
	}

	/**
	 * obtém uma lista de propriedades que começam com um nome (parâmetro) que é
	 * separado por um ponto e seguido de um numero sequencial. Ex: parâmetro
	 * nome -> uf no arquivo : uf.0=AC uf.1=AL uf.2=AM ... uf.26=TO
	 * 
	 * Obs: Note que os parâmetros em lista também têm herança, da mesma forma
	 * que os parâmetros simples
	 * 
	 * @param nome
	 *            nome do parâmetro a obter. Se atribuído o prefixo, o mesmo não
	 *            deve estar contido.
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> obterPropriedadeLista(String nome)
			throws Exception {
		ArrayList<String> lista = new ArrayList<String>();
		for (int i = 0; true; i++) {
			String prp = obterPropriedade(nome + "." + String.valueOf(i));
			if (prp == null)
				break;
			lista.add(prp.trim());
		}
		return lista;
	}

	/**
	 * obtém uma lista de propriedades que começam com um nome (parâmetro) que é
	 * separado por um ponto e seguido de um numero sequencial. Ex: parâmetro
	 * nome -> uf no arquivo : uf.0=AC uf.1=AL uf.2=AM ... uf.26=TO
	 * 
	 * Obs: Note que os parâmetros em lista também têm herança, da mesma forma
	 * que os parâmetros simples
	 * 
	 * @param nome
	 *            nome do parâmetro a obter. Se atribuído o prefixo, o mesmo não
	 *            deve estar contido.
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> obterPropriedadeListaNaoNumerada(String nome)
			throws Exception {
		carregarPropriedades();
		Map<String, String> map = new TreeMap<String, String>();
		String nomeMaisSeparador = nome + ".";

		for (Object k : propriedades.keySet()) {
			if (!(k instanceof String))
				continue;
			String s = (String) k;
			if (!s.startsWith(nomeMaisSeparador))
				continue;
			map.put(s.substring(nomeMaisSeparador.length()),
					obterPropriedade(s));
		}
		return map;
	}
}
