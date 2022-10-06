package br.gov.jfrj.siga.xjus;

import com.crivano.swaggerservlet.SwaggerServlet;

public enum RecordServiceEnum {
	DOC("/sigaex/apis/x-jus/doc/v1/"),
	//
	MOV("/sigaex/apis/x-jus/mov/v1/"),
	//
	INF("/sigagc/apis/x-jus/inf/v1/");

	private String path;

	RecordServiceEnum(String path) {
		this.path = path;
	}

	public String buildUrl() {
		return SwaggerServlet.getHttpServletRequest().getRequestURL().toString().replace("siga/apis/x-jus/v1/", this.path);
	}
}
