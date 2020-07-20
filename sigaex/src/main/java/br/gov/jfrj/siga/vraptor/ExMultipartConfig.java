package br.gov.jfrj.siga.vraptor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;

import br.com.caelum.vraptor.observer.upload.DefaultMultipartConfig;
import br.gov.jfrj.siga.base.Prop;

@Specializes
@ApplicationScoped  
public class ExMultipartConfig extends DefaultMultipartConfig {  
  
    public long getSizeLimit() {  
        return Prop.getInt("pdf.tamanho.maximo");
    }
    
    public long getFileSizeLimit() {
        return Prop.getInt("pdf.tamanho.maximo");
    }
}  