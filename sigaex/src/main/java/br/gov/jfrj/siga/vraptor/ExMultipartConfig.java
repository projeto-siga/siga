package br.gov.jfrj.siga.vraptor;

import br.com.caelum.vraptor.interceptor.multipart.DefaultMultipartConfig;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component  
@ApplicationScoped  
public class ExMultipartConfig extends DefaultMultipartConfig {  
  
    public long getSizeLimit() {  
        return 100000000L; //10MB  
    }  
}  