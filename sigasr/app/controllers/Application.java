package controllers;

import play.*;
import play.db.jpa.JPA;
import play.mvc.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import javax.persistence.Query;

import models.*;

public class Application extends Controller {

	public static void index() {
		List<SrItemConfiguracao> itensConfiguracao = SrItemConfiguracao.all().fetch();
		List<SrFormaAcompanhamento> formasAcompanhamento = SrFormaAcompanhamento.all().fetch();
		render(itensConfiguracao, formasAcompanhamento);
	}
	
	public static void proxy(String url) throws Exception {
		URL u = new URL(url);
		URLConnection conn = u.openConnection();
		for (Http.Header h : request.headers.values())
			conn.setRequestProperty(h.name, h.value());

		// BufferedReader in = new BufferedReader(new InputStreamReader(u
		// .openStream()));
		BufferedReader in = new BufferedReader(new InputStreamReader(conn
				.getInputStream()));
		String inputLine;
		StringBuilder sb = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			sb.append(inputLine);
		}
		in.close();

		renderHtml(sb.toString());

		// byte ba[] = inputLine.getBytes("utf-8");
		// response.contentType = "text/html";
		// response.out.write(ba);
		// response.headers.put("Content-Length", new Http.Header(
		// "Content-Length", Integer.toString(ba.length)));
	}

}