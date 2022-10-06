package br.gov.jfrj.siga.base;

import java.util.ArrayList;
import java.util.List;

public enum XjusRecordServiceEnum {
	EX_DOC("/sigaex/apis/x-jus/doc/v1/"),
	//
	EX_MOV("/sigaex/apis/x-jus/mov/v1/"),
	//
	GC_INF("/sigagc/apis/x-jus/inf/v1/");

	public String path;

	XjusRecordServiceEnum(String path) {
		this.path = path;
	}

	public static List<XjusRecordServiceEnum> enabledValues() {
		List<XjusRecordServiceEnum> l = new ArrayList<>();
		for (XjusRecordServiceEnum service : values())
			if (service.isEnabled())
				l.add(service);
		return l;
	}

	public Boolean isEnabled() {
		return Prop.getBool("/xjus.enable." + this.name().toLowerCase());
	}
}
