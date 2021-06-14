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
package br.gov.jfrj.siga.base;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.stripToNull;

import java.util.function.Function;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

public class Contexto {

	private static final Logger log = Logger.getLogger(Contexto.class);
	private static final int DEFAULT_PORT = 80;

	private static final Function<String, ?> PROPERTY_TRANSFORMER = Prop::get;

	private static final Function<String, ?> CONTEXT_TRANSFORMER = name -> {
		try {
			Context initContext = new InitialContext();
			Context envContext = Context.class.cast(initContext.lookup("java:/comp/env"));
			return envContext.lookup(name);
		} catch (Exception e) {
			throw new IllegalStateException("Propriedade \"" + name + "\" não encontrada no contexto do servidor Web");
		}
	};

	private static final Function<String, ?> SYSTEM_PROPERTIES_TRANSFORMER = name -> ofNullable(name)
			.filter(p -> p.startsWith("/"))
			.map(p -> System.getProperty(p.substring(1)))
			.orElseThrow(() -> new IllegalStateException("Propriedade \"" + name + "\" não encontrada no escopo de propriedades do sistema"));

	public static Object resource(String name) {
		try {
			return PROPERTY_TRANSFORMER.apply(name);
		} catch (Exception e1) {
			log.debug(e1);
			try {
				return CONTEXT_TRANSFORMER.apply(name);
			} catch (Exception e2) {
				log.debug(e2);
				try {
					return SYSTEM_PROPERTIES_TRANSFORMER.apply(name);
				} catch (Exception e3) {
					log.debug(e3);
					return null;
				}
			}
		}
	}

	public static String urlBase(HttpServletRequest request) {
		return urlBase(request, true);
	}

	public static String urlBase(HttpServletRequest request, boolean considerarPropriedadeSigaBaseUrl) {
		String baseUrl = stripToNull(Prop.get("/siga.base.url"));
		String visibleSchema = null;
		if (baseUrl != null) {
			if (considerarPropriedadeSigaBaseUrl) {
				return baseUrl;
			}
			visibleSchema = baseUrl.substring(0, baseUrl.indexOf("://"));
		}
		return baseUrlFrom(request, visibleSchema);
	}

	public static String baseUrlFrom(HttpServletRequest request, String visibleScheme) {
		StringBuilder baseUrlBuilder = new StringBuilder()
			.append(isNotEmpty(visibleScheme) ? visibleScheme : request.getScheme())
			.append("://")
			.append(request.getServerName());

		int requestPort = request.getServerPort();
		if (DEFAULT_PORT != requestPort) {
			baseUrlBuilder.append(":").append(requestPort);
		}

		return baseUrlBuilder.toString();
	}

}
