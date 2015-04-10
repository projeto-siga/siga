
package com.ittru.ccws.webservice;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import br.gov.jfrj.siga.cd.ext.SigaCdExtProperties;


public class SignServiceImplService
{
	private static SignService getService(Class<SignService> remoteClass,
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
		SignService e = service.getPort(remoteClass);
		
		String enderecoEndpoint = ((javax.xml.ws.BindingProvider)e).getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY).toString();
		enderecoEndpoint = enderecoEndpoint.replace("localhost", wsdlURL.getHost());
		((javax.xml.ws.BindingProvider)e).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, enderecoEndpoint);
		
		return e;
	}

    
	public static SignService getIttruService() {
		SignService	gi = getService(SignService.class,
					SigaCdExtProperties.getString("ittru.endpoint"),
					SigaCdExtProperties.getString( "ittru.qname" ),
					SigaCdExtProperties.getString( "ittru.servicename" ));
		return gi;
	}
}
