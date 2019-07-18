package br.gov.jfrj.siga.vraptor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;

import br.com.caelum.vraptor.observer.upload.DefaultMultipartConfig;
import br.gov.jfrj.siga.ex.SigaExProperties;

@Specializes
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