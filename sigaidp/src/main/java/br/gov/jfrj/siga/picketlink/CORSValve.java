package br.gov.jfrj.siga.picketlink;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;


public class CORSValve extends ValveBase{
	
	@Override
	public void invoke(Request request, Response response) throws IOException,ServletException {
		//int porta = request.getServerPort();
		// Esse 363 é o número da diferença da porta https pra http ex: 8443 - 8080
		// Com isso se a aplicação estiver em uma porta diferente da padrão não precisa modificar esse método.
		//porta = porta - 363;
		String url = System.getProperty("sigaidp.crossdomain.url");
		response.addHeader("Access-Control-Allow-Origin", url);
		response.addHeader("Access-Control-Allow-Methods", "POST, GET");
		response.addHeader("Access-Control-Max-Age", "3600");
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type");
		
		getNext().invoke(request, response);
	}

}
