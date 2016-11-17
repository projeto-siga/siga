package br.gov.jfrj.siga.gc.vraptor;

import br.com.caelum.vraptor.interceptor.multipart.DefaultMultipartConfig;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component  
@ApplicationScoped  
public class GcMultipartConfig extends DefaultMultipartConfig {

	@Override
    public long getSizeLimit() {  
		return 10 * 1024 * 1024;
    }  
}
