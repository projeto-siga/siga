package br.gov.jfrj.siga.vraptor;

import br.com.caelum.vraptor.interceptor.multipart.DefaultMultipartConfig;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.gov.jfrj.siga.ex.SigaExProperties;

@Component  
@ApplicationScoped  
public class ExMultipartConfig extends DefaultMultipartConfig {  
  
    public long getSizeLimit() {  
        try {
			return SigaExProperties.getTamanhoMaxPDF();
		} catch (Exception e) {		
		}
		return 0;
    }  
}  