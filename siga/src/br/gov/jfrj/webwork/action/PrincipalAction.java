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
 * Criado em 23/11/2005
 */

package br.gov.jfrj.webwork.action;

import java.io.DataInputStream;
import java.net.URL;
import java.util.List;

import javax.jms.IllegalStateException;

import org.apache.log4j.Logger;

import br.gov.jfrj.siga.base.auditoria.hibernate.util.SigaHibernateAuditorLogUtil;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.libs.webwork.SigaActionSupport;

import com.opensymphony.xwork.Action;

public class PrincipalAction extends SigaActionSupport {
	private static final String OK = "<span style=\"color: green;\">OK</span>";
	private static final String ERRO = "<span style=\"color: red;\">ERRO</span>";
	private static final String SIGA_TESTES_ACTION = "/siga/testes/testes.action";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1630775520737927455L;
	private List listEstados;
	
	@Override
	public String execute() throws Exception {
		// super.getRequest ().setAttribute ( "_cadastrante" , super.getTitular
		// ().getSigla () + "@" + super.getLotaTitular ().getOrgaoUsuario
		// ().getSiglaOrgaoUsu ()+ super.getLotaTitular ().getSigla () );
		
		return Action.SUCCESS;
	}

	public String test() throws Exception {
		DpPessoa pes = daoPes(param("matricula"));

		super.getRequest().setAttribute("siga_test", ERRO);
		if (pes != null)
			super.getRequest().setAttribute("siga_test", OK);

		String url = super.getRequest().getRequestURL().toString()
				+ "?matricula=" + super.getRequest().getParameter("matricula");

		String siga_ex_test_url = url.replace(SIGA_TESTES_ACTION,
				"/sigaex/testes/gadgetTest.action");
		super.getRequest().setAttribute("siga_ex_test", ERRO);
		if (httpTest(siga_ex_test_url, "Atendente"))
			super.getRequest().setAttribute("siga_ex_test", OK);

		String siga_wf_test_url = url.replace(SIGA_TESTES_ACTION,
				"/sigawf/testes/gadgetTest.action");
		super.getRequest().setAttribute("siga_wf_test", ERRO);
		if (httpTest(siga_ex_test_url, "Atendente"))
			super.getRequest().setAttribute("siga_wf_test", OK);

		String siga_cd_test_url = url.replace(SIGA_TESTES_ACTION,
				"/sigacd/testes/testes.action");
		super.getRequest().setAttribute("siga_cd_test", ERRO);
		if (httpTest(siga_ex_test_url, "OK!"))
			super.getRequest().setAttribute("siga_cd_test", OK);

		return Action.SUCCESS;
	}

	private boolean httpTest(String url, String mustHave) {
		try {
			String thisLine;
			URL u = new URL(url);
			DataInputStream theHTML = new DataInputStream(u.openStream());
			boolean fHas = false;
			while ((thisLine = theHTML.readLine()) != null) {
				if (mustHave != null && thisLine.contains(mustHave))
					fHas = true;
			}
			if (mustHave != null && fHas)
				return fHas;
			return true;
		} catch (Exception e) {
			return false;
		}

	}

}