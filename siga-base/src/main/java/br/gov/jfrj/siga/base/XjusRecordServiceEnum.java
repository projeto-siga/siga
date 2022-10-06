package br.gov.jfrj.siga.base;

import java.util.List;

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

	public static List<XjusRecordServiceEnum> enabledValues() {
		return DisableableEnum.enabledValues(values());
	}
	
//	public static List<XjusRecordServiceEnum> enabledValues() {
//		List<XjusRecordServiceEnum> l = new ArrayList<>();
//		for (XjusRecordServiceEnum service : values())
//			if (!service.isDisabled())
//				l.add(service);
//		return l;
//	}

//	public Boolean isDisabled() {
//		List<String> list = Prop.getList("/xjus.service.disable");
//		if (list == null)
//			return false;
//		return list.contains(this.name());
//	}

	@Override
	public String getDisablerProperty() {
		return "/xjus.service.disable";
	}
}
