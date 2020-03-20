/*
 * The Nheengatu Project : a free Java library for HTML  abstraction.
 *
 * Project Info:  http://www.aryjr.com/nheengatu/
 * Project Lead:  Ary Rodrigues Ferreira Junior
 *
 * (C) Copyright 2005, 2006 by Ary Junior
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.aryjr.nheengatu.testes;

import com.aryjr.nheengatu.html.HTMLDocument;
import com.aryjr.nheengatu.html.Tag;
import com.aryjr.nheengatu.pdf.PDFDocument;
import com.lowagie.text.Document;

/**
 * 
 * Teste para o pacote val-html-para-pdf.
 * 
 * @version $Id: TesteImpressao.java,v 1.1 2007/12/26 15:57:41 tah Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class TesteImpressao {

	public static void main(final String[] args) {
		new TesteImpressao();
	}

	public TesteImpressao() {
		final Document document = new Document();
		final HTMLDocument html = new HTMLDocument("teste-impressao");
		html.setPath("/home/aryjr/cvs/java/Nheengatu/");
		html.breakLines(true);
		html.getBody().setPropertyValue("bgcolor", "#DDDDDD");
		final PDFDocument pdf = new PDFDocument("teste-impressao");
		pdf.setPath("/home/aryjr/cvs/java/Nheengatu/");
		html.getBody().addTag(new Tag("center", new Tag("h1", "The Nheengatu Project")));
		final Tag text1Font = new Tag(
				"font",
				"O Nheengatu, tamb&eacute;m conhecido como \"l&iacute;ngua geral\", foi desenvolvido pelos jesu&iacute;tas nos s&eacute;culos 16 e17, com base no vocabul&aacute;rio e na pronuncia tupi, que era a l&iacute;ngua das tribos da costa, tendo como referencia a gram&aacute;tica da l&iacute;ngua portuguesa, enriquecida com palavras portuguesas e espanholas. A l&iacute;ngua geral foi usada correntemente pelos brasileiros de origem ib&eacute;rica, como l&iacute;ngua de conversa&ccedil;&atilde;o cotidiana, at&eacute; o s&eacute;culo 18, quando foi proibida pelo rei de Portugal.");
		text1Font.setPropertyValue("color", "#990000");
		html.getBody().addTag(new Tag("blockquote", new Tag("b", text1Font)));
		html.getBody().addTag(new Tag("br"));
		html.getBody().addTag(table());
		html.getBody().addTag(new Tag("br"));
		final Tag text2Font = new Tag("font");
		text2Font.setPropertyValue("face", "Arial, Helvetica, sans-serif");
		text2Font.setPropertyValue("size", "3");
		final Tag img1 = new Tag("img");
		img1.setPropertyValue("src", "image4.jpg");
		img1.setPropertyValue("align", "top");
		text2Font.addTag(img1);
		text2Font
				.addText("Um olhar para o passado...O padre Jos&eacute;, aos sessenta e dois anos, tinha ainda no cora&ccedil;&atilde;o aquela inquietude e sede de conhecimento do menino. Mas, o corpo cansado queria descansar da vida dedicada &agrave; aventura, &agrave; peregrina&ccedil;&atilde;o, ao desbravamento de um mundo misterioso. Seus olhos se enchiam de satisfa&ccedil;&atilde;o ao lembrar como estava contente o menino aventureiro que ainda tinha dentro de si: fora ele quem sonhara a vinda para o Brasil; o trabalho de evangeliza&ccedil;&atilde;o; a funda&ccedil;&atilde;o da vila que se tornaria a metr&oacute;pole de S&atilde;o Paulo; a participa&ccedil;&atilde;o no nascimento da cidade do Rio de Janeiro; o ensino do latim; o aprendizado e a uniformiza&ccedil;&atilde;o da l&iacute;ngua dos &iacute;ndios brasileiros; as agruras dos tempos de ref&eacute;m em Ubatuba; as batalhas; as reconcilia&ccedil;&otilde;es. O menino lan&ccedil;ava para o velho olhares de alegria n&atilde;o s&oacute; pelas grandes realiza&ccedil;&otilde;es, mas tamb&eacute;m pelo dever cumprido da evangeliza&ccedil;&atilde;o.");
		text2Font.addTag(new Tag("br"));
		html.getBody().addTag(text2Font);
		html.getBody().addTag(new Tag("br"));
		final Tag img2 = new Tag("img");
		img2.setPropertyValue("src", "bandeira01.jpg");
		img2.setPropertyValue("width", "50");
		img2.setPropertyValue("height", "50");
		html.getBody().addTag(new Tag("center", img2));
		pdf.setBody(html.getBody());
		// Agora salvando os documentos
		try {
			html.generateFile();
			pdf.generateFile();
			Runtime.getRuntime().exec("acroread /home/aryjr/cvs/java/Nheengatu/teste-impressao.pdf");
		} catch (final Exception e) {
//			System.out.println("Exception ao criar os documentos.");
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	private Tag table() {
		final Tag table = new Tag("table");
		table.setPropertyValue("align", "center");
		table.setPropertyValue("width", "400");
		table.setPropertyValue("bgcolor", "#000000");
		Tag row = new Tag("tr");
		row.setPropertyValue("bgcolor", "#FFFFFF");
		Tag cell = new Tag("td", "Fase");
		cell.setPropertyValue("align", "center");
		cell.setPropertyValue("rowspan", "2");
		row.addTag(cell);
		cell = new Tag("td", "Período");
		cell.setPropertyValue("align", "center");
		cell.setPropertyValue("colspan", "2");
		row.addTag(cell);
		table.addTag(row);
		row = new Tag("tr");
		row.setPropertyValue("bgcolor", "#FFFFFF");
		cell = new Tag("td", "Início");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", "Fim");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		table.addTag(row);
		row = new Tag("tr");
		row.setPropertyValue("bgcolor", "#FFFFFF");
		cell = new Tag("td", "FASE LITORÂNEA");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", "1500");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", "1640");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		table.addTag(row);
		row = new Tag("tr");
		row.setPropertyValue("bgcolor", "#FFFFFF");
		cell = new Tag("td", "FASE BANDEIRISTA");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", "1573");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", "1756");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		table.addTag(row);
		row = new Tag("tr");
		row.setPropertyValue("bgcolor", "#FFFFFF");
		cell = new Tag("td", "FASE AMAZÔNICA");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", "1616");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", "1832");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		table.addTag(row);
		row = new Tag("tr");
		row.setPropertyValue("bgcolor", "#FFFFFF");
		cell = new Tag("td", "FASE SERTANEJA");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", "1624");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", "1758");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		table.addTag(row);
		return table;
	}
}
/**
 * 
 * $Log: TesteImpressao.java,v $
 * Revision 1.1  2007/12/26 15:57:41  tah
 * *** empty log message ***
 *
 * Revision 1.4  2007/07/19 21:18:16  nts
 * Inserindo comentários
 *
 * Revision 1.3  2006/07/05 16:00:48  nts
 * Refatorando para melhorar qualidade do código
 *
 * Revision 1.2  2006/04/11 19:43:47  tah
 * *** empty log message ***
 * Revision 1.1 2006/04/03 21:30:42 tah Utilizando
 * o nheengatu
 * 
 * Revision 1.3 2006/01/01 13:45:32 aryjr Feliz 2006!!!
 * 
 * Revision 1.2 2005/12/16 14:06:30 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.1 2005/11/14 12:17:29 aryjr Renomeando os pacotes.
 * 
 * Revision 1.3 2005/11/11 21:09:52 aryjr Retomando o desenvolvimento e
 * trabalhando no suporte ao CSS2.
 * 
 * Revision 1.2 2005/09/26 19:41:15 aryjr Aproveitando a greve para voltar a
 * atividade.
 * 
 * Revision 1.1 2005/09/10 23:43:37 aryjr Passando para o java.net.
 * 
 * Revision 1.7 2005/09/02 21:51:31 aryjunior Teste
 * 
 * Revision 1.6 2005/07/02 01:18:57 aryjunior Site do projeto.
 * 
 * Revision 1.5 2005/06/04 13:29:25 aryjunior LGPL.
 * 
 * Revision 1.4 2005/05/30 14:31:24 aryjunior Ha uma pendencia com relacao aos
 * tamanhos das fontes.
 * 
 * Revision 1.3 2005/05/30 01:55:57 aryjunior Alguns detalhes no cabecalho dos
 * arquivos e fazendo alguns testes com tabelas ainhadas.
 * 
 * Revision 1.2 2005/05/28 23:21:41 aryjunior Corrigindo o cabecalho.
 * 
 * Revision 1.1.1.1 2005/05/28 21:10:32 aryjunior Initial import.
 * 
 */
