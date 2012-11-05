package br.gov.jfrj.siga.base.auditoria.hibernate.enums;

public enum SigaLogCategoryEnum {
	
	TRACE("trace"), 
	DEBUG("debug"), 
	INFO("info"), 
	WARN("warn"), 
	ERROR("error"), 
	FATAL("fatal");
	
	private String value;
	
	private SigaLogCategoryEnum(String value) {
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
	
	

}
