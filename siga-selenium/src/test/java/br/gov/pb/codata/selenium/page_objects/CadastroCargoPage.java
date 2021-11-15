package br.gov.pb.codata.selenium.page_objects;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

import org.openqa.selenium.By;

import com.lazerycode.selenium.util.Query;

import br.gov.pb.codata.selenium.DriverBase;


//Dev:Thomas Ribeiro
	public class CadastroCargoPage {

		public static final String URL = "https://desenv.pbdigital.pb.gov.br/sigaex/app/despacho/tipodespacho/editar";
	
		private final Query inputOrgaoCargo = new Query().defaultLocator(By.name("idOrgaoUsu"));
		private final Query NomeCargo = new Query().defaultLocator(By.name("nmCargo"));
		
		 public CadastroCargoPage() throws Exception {
		        initQueryObjects(this, DriverBase.getDriver());
		    }
		    
		    //clicar em incluir primeiro, depois preenche o formulario
		    
		    public CadastroCargoPage PreencherCampos (String orgaoCargo, String nomeCargo) {
		    	inputOrgaoCargo.findWebElement().sendKeys(orgaoCargo);
		    	NomeCargo.findWebElement().sendKeys(nomeCargo);
		    	return this;
		    }
		    public CadastroCargoPage enviarForm() {
		    	inputOrgaoCargo.findWebElement().submit();

		        return this;
		    }
		    
}