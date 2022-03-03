package br.gov.jfrj.siga.integracao.ws.siafem;

import br.gov.jfrj.siga.base.Prop;

public class PropriedadeSiafem {
	public static final String URL_WSDL = Prop.get("ws.siafem.url.wsdl");
	public static final String URL_NAMESPACE = Prop.get("ws.siafem.url.namespace");
	public static final String NM_MODELO = Prop.get("ws.siafem.nome.modelo");
	public static final String LOCAL_PART = Prop.get("ws.siafem.service.localpart");
	public static final String LOCAL_PART_SOAP = Prop.get("ws.siafem.service.localpartsoap");
}
