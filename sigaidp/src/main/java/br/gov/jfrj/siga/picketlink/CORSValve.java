package br.gov.jfrj.siga.picketlink;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;


public class CORSValve extends ValveBase{

	@Override
	public void invoke(Request request, Response response) throws IOException,ServletException {
		response.addHeader("Access-Control-Allow-Origin", "http://localhost:8080");
		response.addHeader("Access-Control-Allow-Methods", "POST, GET");
		response.addHeader("Access-Control-Max-Age", "3600");
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type");
		
		getNext().invoke(request, response);
	}

}
