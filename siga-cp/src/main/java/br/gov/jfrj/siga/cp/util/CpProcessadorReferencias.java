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
package br.gov.jfrj.siga.cp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class CpProcessadorReferencias {

	static String acronimos = null;
	static String siglas = null;

	public static String marcarReferenciasParaDocumentos(String sHtml, Set setIgnorar) {
		if (acronimos == null) {
			acronimos = "";
			siglas = "";
			List<CpOrgaoUsuario> lou = CpDao.getInstance().listarOrgaosUsuarios();
			for (CpOrgaoUsuario ou : lou) {
				acronimos += (acronimos.length() > 0 ? "|" : "") + ou.getAcronimoOrgaoUsu();
				siglas += (siglas.length() > 0 ? "|" : "") + ou.getSiglaOrgaoUsu();
			}
		}

		// Substituir elementos HTML por um place holder
		final Pattern p3 = Pattern.compile("<[^>]+?>");
		StringBuffer sb = new StringBuffer();
		final Matcher m3 = p3.matcher(sHtml);
		List<String> placeHolders = new ArrayList<>();
		while (m3.find()) {
			placeHolders.add(m3.group(0));
			m3.appendReplacement(sb, "%ELEMENT%");
		}
		m3.appendTail(sb);
		sHtml = sb.toString();

		final Pattern p2 = Pattern.compile("TMP-([0-9]{1,10})");
		final Pattern p1 = Pattern.compile("(" + acronimos + "|" + siglas
				+ ")-([A-Za-z]{3})-(?:([0-9]{4}))/([0-9]{5,})(\\.[0-9]{1,3})?(?:((?:-?V[0-9]{1,2}))|((?:-?[a-zA-Z]{1})|(?:-[0-9]{1,2})))?");

		sb = new StringBuffer();
		final Matcher m1 = p1.matcher(sHtml);
		while (m1.find()) {
			if (setIgnorar == null || !setIgnorar.contains(m1.group(0)))
				m1.appendReplacement(sb, "<a href=\"/sigaex/app/expediente/doc/exibir?sigla=$0\">$0</a>");
		}
		m1.appendTail(sb);
		sHtml = sb.toString();

		sb = new StringBuffer();
		final Matcher m2 = p2.matcher(sHtml);
		while (m2.find()) {
			if (setIgnorar == null || !setIgnorar.contains(m2.group(0)))
				m2.appendReplacement(sb, "<a href=\"/sigaex/app/expediente/doc/exibir?sigla=$0\">$0</a>");
		}
		m2.appendTail(sb);
		sHtml = sb.toString();

		// Substituir os placeholders pelo elementos HTML
		final Pattern p4 = Pattern.compile("%ELEMENT%");
		sb = new StringBuffer();
		final Matcher m4 = p4.matcher(sHtml);
		int i = 0;
		while (m4.find()) {
			m4.appendReplacement(sb, placeHolders.get(i++));
		}
		m4.appendTail(sb);
		sHtml = sb.toString();

		return sHtml;
	}
}
