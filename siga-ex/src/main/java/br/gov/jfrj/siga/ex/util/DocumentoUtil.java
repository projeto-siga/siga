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
package br.gov.jfrj.siga.ex.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.gov.jfrj.siga.base.util.Texto;

public class DocumentoUtil {

	public static String obterLetraViaPorCodigo(int codigo) {
		return new Character(((char)(codigo+64))).toString();
	}
	
	public static String obterLetraViaPorCodigo(String codigo){
		return obterLetraViaPorCodigo(Integer.parseInt(codigo));
	}
	
	public static String obterDataExtenso(String localidade, Date dataDoc) {

        // Forçando a ficar em pt_BR, antes a data aparecia na linguagem
        // definida no servidor de aplicação (tomcat, jbos, etc.)
        SimpleDateFormat df1 = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy.",
                                       new Locale("pt", "BR"));
        try {
                       // As linhas abaixo foram comentadas porque o formato já está
                       // definido na declaração da variável df1.
                       //
                       // df1.applyPattern("dd/MM/yyyy");
                       // df1.applyPattern("dd 'de' MMMM 'de' yyyy.");
                       String s;

                       List<String> uf = Arrays.asList("AC","AL","AM","AP","BA","CE","DF","ES","GO","MA","MG","MT","MS","PA","PB","PE","PR","RN","RJ","RO","RR","RS","SE","SP","SC","TO");
                       
                       
                      if (localidade.contains("-") ) {
                       
                              String p[] = localidade.split("-");      

                              if ((p[p.length-1] != null) && (uf.contains(p[p.length-1].toUpperCase()))){                                                                                                                    
                                   s = Texto.maiusculasEMinusculas(localidade.replace("-" + p[p.length-1],"")) + "-" + p[p.length-1].toUpperCase();     

                              } else {               
                                       s = Texto.maiusculasEMinusculas(localidade);  
                              }
                     } else {
                               s = Texto.maiusculasEMinusculas(localidade);
                     }


                       return s + ", " + df1.format(dataDoc).toLowerCase();
        } catch (Exception e) {
                       return null;
        }

	}

}
