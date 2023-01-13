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

import junit.framework.TestCase;

public class ModeloMarkdownTest extends TestCase {

    public ModeloMarkdownTest() throws Exception {
    }

    public void testGeraModelo() throws Exception {
        String s = ModeloMarkdown.markdownToFreemarker("Olá [@campo var='nome' /]");
        assertEquals(""
                + "[@entrevista]\n"
                + "  [@campo var='nome' /]\n"
                + "[/@entrevista]\n"
                + "\n"
                + "[@documento]\n"
                + "<p>Olá [@valor var='nome' /]</p>\n"
                + "[/@documento]", s);
    }

    public void testGeraModeloComDefinicaoDeVariavelMinima() throws Exception {
        String s = ModeloMarkdown.markdownToFreemarker("Olá {nome}");
        assertEquals(""
                + "[@entrevista]\n"
                + "  [@campo var='nome' /]\n"
                + "[/@entrevista]\n"
                + "\n"
                + "[@documento]\n"
                + "<p>Olá [@valor var='nome' /]</p>\n"
                + "[/@documento]", s);
    }

    public void testGeraModeloComDefinicaoDeMacro() throws Exception {
        String s = ModeloMarkdown.markdownToFreemarker("Olá {campo var:\"nome\", opcoes:\"Fulano;Beltrano\"}");
        assertEquals(""
                + "[@entrevista]\n"
                + "  [@campo var='nome' opcoes='Fulano;Beltrano' /]\n"
                + "[/@entrevista]\n"
                + "\n"
                + "[@documento]\n"
                + "<p>Olá [@valor var='nome' opcoes='Fulano;Beltrano' /]</p>\n"
                + "[/@documento]", s);
    }

}
