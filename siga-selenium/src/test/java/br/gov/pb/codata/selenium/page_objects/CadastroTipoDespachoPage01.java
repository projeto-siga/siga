package br.gov.pb.codata.selenium.page_objects;

import br.gov.pb.codata.selenium.DriverBase;
import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;
import com.lazerycode.selenium.util.Query;
import org.openqa.selenium.By;

/**
 *
 * @author kenneth
 * @author Thomas Ribeiro
 */
public class CadastroTipoDespachoPage01 {
    public static final String URL = "https://desenv.pbdigital.pb.gov.br/sigaex/app/despacho/tipodespacho/editar";
    
      
    private final Query inputDescricao = new Query().defaultLocator(By.name("exTipoDespacho.descTpDespacho"));
    
    public CadastroTipoDespachoPage01() throws Exception {
        initQueryObjects(this, DriverBase.getDriver());
    }

    	
    public CadastroTipoDespachoPage01 digitarDados(String descricao) {
        inputDescricao.findWebElement().clear();
        inputDescricao.findWebElement().sendKeys(descricao);        
        
        return this;
    }

    public CadastroTipoDespachoPage01 enviarForm() {
        inputDescricao.findWebElement().submit();

        return this;
    }
}
