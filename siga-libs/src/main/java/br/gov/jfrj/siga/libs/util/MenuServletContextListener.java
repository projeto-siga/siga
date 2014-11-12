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
 * The contents of this file are subject to the GNU Lesser General Public
 * License Version 2.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/lesser.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Developer:
 * Todd Ditchendorf, todd@ditchnet.org
 *
 */

/**
 *	@author Todd Ditchendorf
 *	@version 0.8
 *	@since 0.8
 */
package br.gov.jfrj.siga.libs.util;

import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import java.io.File;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.URL;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author Todd Ditchendorf
 * @since 0.8
 * 
 *        <p>
 *        <code>ServletContextListener</code> that handles extracting CSS,
 *        JavaScript, and image resource files from this taglib's JAR file, and
 *        places them in a directory called <code>org.ditchnet.taglib/</code> in
 *        this web app's root directory. Why do this? The HTML files in which
 *        the tabs are used must have access to the CSS, JavaScript and image
 *        files that render the tabs correctly. Therefore, they must be placed
 *        in a known location outside of the <code>WEB-INF</code> directory so
 *        that client browsers can access them.
 *        </p>
 *        <p>
 *        By implementing this feature as as <code>ServletContextListener</code>
 *        , These resources are deployed immediately on reloading your web app,
 *        and are therefore available before the first request!
 */
public class MenuServletContextListener implements ServletContextListener {

	private static final String DEST_FOLDER_NAME = "/sigalibs/";

	public static final String SCRIPT_URI = DEST_FOLDER_NAME + "menus.js";

	public static final String STYLE_URI = DEST_FOLDER_NAME + "menusstyle.jsp";

	private static final String IMAGE_RESOURCE_FOLDER = "/lib/img/";

	private static final List IMAGE_RESOURCE_NAMES = Arrays
			.asList(new String[] { "arrow_sub_0.gif",
					"sample3_main_arrow_0.gif", "sample3_sub_arrow_0.gif",
					"toplogo1_70.png", "toplogo2_70.png", "siga.ico"});

	private static final String JS_RESOURCE_FOLDER = "/lib/js/";

	private static final List JS_RESOURCE_NAMES = Arrays.asList(new String[] {
			"staticjsdefmenu0.jsp", "staticjsdefmenu1.jsp",
			"staticjssrcmenu0.jsp", "staticjssrcmenu1.jsp", "ajax.js",
			"static_javascript.js", "mensagensremotas.js", "siga_ckeditor_styles.js" });

	private static final String CSS_RESOURCE_FOLDER = "/lib/css/";

	private static final List CSS_RESOURCE_NAMES = Arrays.asList(new String[] {
			"siga.css", "menu.css" });

	private static final String JSP_RESOURCE_FOLDER = "/lib/jsp/";

	private static final List JSP_RESOURCE_NAMES = Arrays.asList(new String[] {
			"menuprincipal.jsp", "barranav.jsp", "staticjs.jsp",
			"ajax_retorno.jsp", "ajax_vazio.jsp", "cargo_busca.jsp",
			"funcao_busca.jsp", "lotacao_busca.jsp", "orgao_busca.jsp",
			"pessoa_busca.jsp", "erroGeral.jsp", "erro400.jsp", "erro401.jsp", "erro403.jsp",
			"loginInvalido.jsp", "estilos.jsp", "rpc_retorno.jsp", "client_cert_auth_logoff.jsp","ajax_msg_erro.jsp" });

	private static final String XML_RESOURCE_FOLDER = "/lib/jsp/xml/";

	private static final List XML_RESOURCE_NAMES = Arrays.asList(new String[] {
			"barranav.xml", "barranav.xsl" });

	/**
	 * Handles finding all CSS, JavaScript, and image resources from this
	 * taglib's JAR file and copies them to a directory in the web app's root
	 * directory where the tab components can link to them.
	 */
	public void contextInitialized(final ServletContextEvent evt) {
		ServletContext servletContext = evt.getServletContext();
		createDitchnetDir(servletContext);
		String sourcePath, destPath, fileName;
		URL sourceURL;

		for (Iterator iter = IMAGE_RESOURCE_NAMES.iterator(); iter.hasNext();) {
			fileName = (String) iter.next();
			sourcePath = IMAGE_RESOURCE_FOLDER + fileName;
			sourceURL = getClass().getResource(sourcePath);
			destPath = DEST_FOLDER_NAME + fileName;
			destPath = servletContext.getRealPath(destPath);
			writeFile(sourceURL, destPath, servletContext);
		}

		for (Iterator iter = JS_RESOURCE_NAMES.iterator(); iter.hasNext();) {
			fileName = (String) iter.next();
			sourcePath = JS_RESOURCE_FOLDER + fileName;
			sourceURL = getClass().getResource(sourcePath);
			destPath = DEST_FOLDER_NAME + fileName;
			destPath = servletContext.getRealPath(destPath);
			writeFile(sourceURL, destPath, servletContext);
		}

		for (Iterator iter = CSS_RESOURCE_NAMES.iterator(); iter.hasNext();) {
			fileName = (String) iter.next();
			sourcePath = CSS_RESOURCE_FOLDER + fileName;
			sourceURL = getClass().getResource(sourcePath);
			destPath = DEST_FOLDER_NAME + fileName;
			destPath = servletContext.getRealPath(destPath);
			writeFile(sourceURL, destPath, servletContext);
		}

		for (Iterator iter = JSP_RESOURCE_NAMES.iterator(); iter.hasNext();) {
			fileName = (String) iter.next();
			sourcePath = JSP_RESOURCE_FOLDER + fileName;
			sourceURL = getClass().getResource(sourcePath);
			destPath = DEST_FOLDER_NAME + fileName;
			destPath = servletContext.getRealPath(destPath);
			writeFile(sourceURL, destPath, servletContext);
		}

		for (Iterator iter = XML_RESOURCE_NAMES.iterator(); iter.hasNext();) {
			fileName = (String) iter.next();
			sourcePath = XML_RESOURCE_FOLDER + fileName;
			sourceURL = getClass().getResource(sourcePath);
			destPath = DEST_FOLDER_NAME + fileName;
			destPath = servletContext.getRealPath(destPath);
			writeFile(sourceURL, destPath, servletContext);
		}

	}

	public void contextDestroyed(final ServletContextEvent evt) {
	}

	private void createDitchnetDir(final ServletContext servletContext) {
		String dirPath = servletContext.getRealPath(DEST_FOLDER_NAME);
		File dir = null;
		try {
			dir = new File(dirPath);
			dir.mkdir();
		} catch (Exception e) {
			// log.error("Error creating Ditchnet dir");
		}
	}

	private void writeFile(final URL fromURL, final String toPath,
			final ServletContext servletContext) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(fromURL.openStream());
			out = new BufferedOutputStream(new FileOutputStream(toPath));
			int len;
			byte[] buffer = new byte[4096];
			while ((len = in.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush();
		} catch (Exception e) {
			// log.error("Error writing file dude: " + e.getMessage());
		} finally {
			try {
				in.close();
				out.close();
			} catch (Exception e) {
			}
		}
	}

}