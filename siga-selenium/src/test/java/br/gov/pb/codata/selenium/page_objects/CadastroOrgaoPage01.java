package br.gov.pb.codata.selenium.page_objects;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
/** 
 @author Allysson Fabiano*/

import com.lazerycode.selenium.util.Query;

import br.gov.pb.codata.selenium.DriverBase;

public class CadastroOrgaoPage01 {

	  
	//Dev:Thomas Ribeiro 
	public static final String URL = "https://desenv.pbdigital.pb.gov.br/siga/app/orgaoUsuario/editar";


	private final Query inputId = new Query().defaultLocator(By.id("id"));
	private final Query inputNome = new Query().defaultLocator(By.id("nmOrgaoUsuario"));
	private final Query inputSigla = new Query().defaultLocator(By.name("siglaOrgaoUsuario"));


	
	public CadastroOrgaoPage01() throws Exception{
		initQueryObjects(this, DriverBase.getDriver());
	}


	public CadastroOrgaoPage01 PreecherDados(String descricao, String id, String sigla ) {

		inputId.findWebElement().sendKeys(id);
		inputNome.findWebElement().sendKeys(descricao);
		inputSigla.findWebElement().sendKeys(sigla);
	
		return this;
		
	}

	public CadastroOrgaoPage01 IncluirOk() {
		inputId.findWebElement().submit();	
		return this;
	}

	
	public CadastroOrgaoPage01 IncluirCancelar() {
		inputId.findWebElement().submit();	
		return this;
	}

	
}
