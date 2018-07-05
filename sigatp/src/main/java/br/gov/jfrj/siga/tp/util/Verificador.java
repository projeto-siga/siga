package br.gov.jfrj.siga.tp.util;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.ioc.spring.VRaptorRequestHolder;

@RequestScoped
@Component
public class Verificador {
	public static boolean estamosExecutandoNoCron() {
		return (VRaptorRequestHolder.currentRequest() == null || VRaptorRequestHolder.currentRequest().getRequest() == null);
	}
}