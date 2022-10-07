package br.gov.jfrj.siga.base;

public enum XjusRecordServiceEnum implements DisableableEnum {
	EX_DOC("/sigaex/apis/x-jus/doc/v1/"),
	//
	EX_MOV("/sigaex/apis/x-jus/mov/v1/"),
	//
	GC_INF("/sigagc/apis/x-jus/inf/v1/");

	public String path;

	XjusRecordServiceEnum(String path) {
		this.path = path;
	}
}
