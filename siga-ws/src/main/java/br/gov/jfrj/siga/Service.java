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
package br.gov.jfrj.siga;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import org.codehaus.jackson.JsonProcessingException;

import br.gov.jfrj.siga.bluc.service.BlucService;
import br.gov.jfrj.siga.cd.service.CdService;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.gc.service.GcService;
import br.gov.jfrj.siga.gi.service.GiService;
import br.gov.jfrj.siga.wf.service.WfService;

import com.google.gson.Gson;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;

public abstract class Service {

	public static final String ERRO = "ERRO: ";

	static WfService wf = null;
	static ExService ex = null;
	static CdService cd = null;
	static GiService gi = null;
	static GcService gc = null;
	static BlucService bluc = null;

	static {
		Unirest.setObjectMapper(new ObjectMapper() {
			private Gson gson = new Gson();

			public <T> T readValue(String value, Class<T> valueType) {
				try {
					return gson.fromJson(value, valueType);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			public String writeValue(Object value) {
				try {
					return gson.toJson(value);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	/*
	 * Externalização das informacoes dos servicos
	 * bruno.lacerda@avantiprima.com.br
	 */
	public static WfService getWfService() {
		if (wf == null)
			wf = getService(WfService.class,
					System.getProperty("wfservice.endpoint"),
					System.getProperty("wfservice.qname"),
					System.getProperty("wfservice.servicename"));
		return wf;
	}

	public static ExService getExService() {
		if (ex == null)
			ex = getService(ExService.class,
					System.getProperty("exservice.endpoint"),
					System.getProperty("exservice.qname"),
					System.getProperty("exservice.servicename"));
		return ex;
	}

	public static CdService getCdService() {
		if (cd == null)
			cd = getService(CdService.class,
					System.getProperty("cdservice.endpoint"),
					System.getProperty("cdservice.qname"),
					System.getProperty("cdservice.servicename"));
		return cd;
	}

	public static GiService getGiService() {
		if (gi == null)
			gi = getService(GiService.class,
					System.getProperty("giservice.endpoint"),
					System.getProperty("giservice.qname"),
					System.getProperty("giservice.servicename"));
		return gi;
	}

	public static GcService getGcService() {
		if (gc == null)
			gc = getService(GcService.class,
					System.getProperty("gcservice.endpoint"),
					System.getProperty("gcservice.qname"),
					System.getProperty("gcservice.servicename"));
		return gc;
	}

	public static BlucService getBlucService() {
		if (bluc == null)
			bluc = new BlucService(System.getProperty("blucservice.endpoint"));
		return bluc;
	}

	@SuppressWarnings("unchecked")
	private static <E extends Remote> E getService(Class<E> remoteClass,
			String wsdl, String qname, String serviceName) {
		URL wsdlURL;
		try {
			wsdlURL = new URL(wsdl);
		} catch (MalformedURLException e) {
			throw new Error(e);
		}

		QName SERVICE_NAME = new QName(qname, serviceName);
		javax.xml.ws.Service service = javax.xml.ws.Service.create(wsdlURL,
				SERVICE_NAME);
		E e = service.getPort(remoteClass);

		// Client client = ClientProxy.getClient(e);

		// client.getInInterceptors().add(new LoggingInInterceptor());
		// client.getOutInterceptors().add(new LoggingOutInterceptor());

		// HTTPConduit http = (HTTPConduit) client.getConduit();

		// http.getClient().setProxyServerType(ProxyServerType.HTTP);
		// http.getClient().setProxyServer("127.0.0.1");
		// http.getClient().setProxyServerPort(8087);

		// HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
		// httpClientPolicy.setConnectionTimeout(36000);
		// httpClientPolicy.setAllowChunking(false);
		// httpClientPolicy.setReceiveTimeout(32000);
		// httpClientPolicy.setProxyServerType(ProxyServerType.SOCKS);
		// httpClientPolicy.setProxyServer("127.0.0.1");
		// httpClientPolicy.setProxyServerPort(8085);
		// http.setClient(httpClientPolicy);

		return e;
	}

	// @SuppressWarnings("unchecked")
	// private static <E extends Remote> E getService(Class<E> remoteClass,
	// String wsdl, String qname, String serviceName) {
	//
	// JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
	// factory.getInInterceptors().add(new LoggingInInterceptor());
	// factory.getOutInterceptors().add(new LoggingOutInterceptor());
	// factory.setServiceClass(remoteClass);
	// factory.setAddress("http://localhost:8080/sigacd/servicos/CdService");
	// //factory.setWsdlURL(wsdl);
	// return (E) factory.create();
	// }

	public static boolean isError(String s) {
		return s != null && s.startsWith(ERRO);
	}

	public static boolean isError(byte[] ba) {
		String s;
		try {
			s = new String(ba, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return false;
		}
		if (s == null)
			return false;
		return s.startsWith(ERRO);
	}

	public static String retrieveError(byte[] ba) {
		String s;
		try {
			s = new String(ba, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		if (s == null)
			return null;
		return s;
	}

	public static void throwExceptionIfError(String s) throws Exception {
		if (Service.isError(s))
			throw new Exception(s);
	}

	public static void throwExceptionIfError(byte[] ba) throws Exception {
		if (Service.isError(ba))
			throw new Exception(retrieveError(ba));
	}

	// public static WfService getWfService() {
	// return getService(WfService.class, "wf");
	// }
	//
	// public static ExService getExService() {
	// return getService(ExService.class, "ex");
	// }

	// @SuppressWarnings("unchecked")
	// private static <E extends Remote> E getService(Class<E> remoteClass,
	// String bean) {
	// AbstractApplicationContext context = getContext();
	// return (E) context.getBean(bean);
	// }
	//
	// public static AbstractApplicationContext getContext() {
	// if (context == null) {
	// context = new ClassPathXmlApplicationContext(
	// new String[] { "br/gov/jfrj/siga/beans.xml" });
	// }
	// return context;
	// }

}
