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
package br.gov.jfrj.itextpdf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import br.gov.jfrj.siga.ex.ExArquivoNumerado;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.util.ProcessadorHtml;

public class DocumentoHtml extends AbstractDocumento {

	@Override
	public String getCharacterEncoding() {
		return "utf-8";
	}

	@Override
	public String getContentType() {
		return "text/html";
	}

	@Override
	protected byte[] getDocumento(ExMobil mob, ExMovimentacao mov,
			HttpServletRequest request) throws Exception {

		List<ExArquivoNumerado> ans = mob.filtrarArquivosNumerados(mov, request
				.getRequestURI().indexOf("/completo/") != -1);

		StringBuilder sb = new StringBuilder();
		boolean fFirst = true;
//TAH: infelizmente o IE não funciona bem com background-color: transparent.		
//		sb.append("<html class=\"fisico\"><body style=\"margin:2px; padding:0pt; background-color: #E2EAEE;overflow:visible;\">");
		sb.append("<html><body style=\"margin:2px; padding:0pt; background-color: " + (mob.getDoc().isEletronico() ? "#E2EAEE" : "#f1e9c6") + ";overflow:visible;\">");
		for (ExArquivoNumerado an : ans) {
			String numeracao = null;
			// if (fFirst)
			// fFirst = false;
			// else
			// sb
			// .append("<div style=\"margin:10px; padding:10px; width:100%;
			// border: medium double green;\" class=\"total\">");

			sb
				.append("<div style=\"margin-bottom:6pt; padding:0pt; width:100%; clear:both; background-color: #fff; border: 1px solid #ccc; border-radius: 5px;\" class=\"documento\">");
			sb
					.append("<table width=\"100%\" style=\"padding:3pt;\" border=0><tr><td>");
			if (an.getPaginaInicial() != null) {
				numeracao = an.getPaginaInicial().toString();
				if (!an.getPaginaFinal().equals(an.getPaginaInicial()))
					numeracao += " - " + an.getPaginaFinal();
				sb
						.append("<div style=\"margin:3pt; padding:3pt; float:right; border: 1px solid #ccc; border-radius: 5px;\" class=\"numeracao\">");
				sb.append(numeracao);
				sb.append("</div>");
			}
			if (an.getArquivo().getHtml() != null) {
				String sHtml = fixHtml(request, an);
				sHtml = ProcessadorHtml.bodyOnly(sHtml);
				// sb
				// .append("<div style=\"margin:3pt; padding:3pt; border: thin
				// solid brown;\" class=\"dochtml\">");
				sb.append(sHtml);
				// sb.append("</div>");
			} else {
				sb
						.append("<div style=\"margin:3pt; padding:3pt;\" class=\"anexo\">");
				sb.append("<a href=\"" + "http://" + request.getServerName()
						+ ":" + request.getServerPort()
						+ request.getContextPath() + "/expediente/doc/"
						+ an.getArquivo().getReferenciaPDF() + "\">");
				sb.append(an.getNome());
				sb.append("</a>");
				if (an.getArquivo() instanceof ExMovimentacao){
					sb.append(": " + ((ExMovimentacao)an.getArquivo()).getDescrMov());
				}
				sb.append("<br/>");
				sb.append("</div>");
			}

			if (an.getArquivo().getMensagem() != null && an.getArquivo().getMensagem().trim().length() > 0) {
				sb.append("</td></tr><tr><td>");
				sb
						.append("<div style=\"margin:3pt; padding:3pt; border: 1px solid #ccc; border-radius: 5px; background-color:lightgreen;\" class=\"anexo\">");
				sb.append(an.getArquivo().getMensagem());
				sb.append("</div>");
			}
			sb.append("</td></tr></table></div>");
		}
		sb.append("</body></html>");

		return sb.toString().getBytes(getCharacterEncoding());
	}

	public static String fixHtml(HttpServletRequest request,
			ExArquivoNumerado an) {
		String sHtml = an.getArquivo().getHtml();
		sHtml = sHtml.replace("<!-- INICIO PRIMEIRO CABECALHO",
				"<!-- INICIO PRIMEIRO CABECALHO -->");
		sHtml = sHtml.replace("FIM PRIMEIRO CABECALHO -->",
				"<!-- FIM PRIMEIRO CABECALHO -->");
		sHtml = sHtml.replace("<!-- INICIO PRIMEIRO RODAPE",
				"<!-- INICIO PRIMEIRO RODAPE -->");
		sHtml = sHtml.replace("FIM PRIMEIRO RODAPE -->",
				"<!-- FIM PRIMEIRO RODAPE-->");
		// s = s.replace("http://localhost:8080/siga/", "/siga/");
		sHtml = sHtml.replace("contextpath", request.getContextPath());
		return sHtml;
	}
}
