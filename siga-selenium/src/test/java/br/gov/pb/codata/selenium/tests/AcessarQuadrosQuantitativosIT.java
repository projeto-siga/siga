package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.page_objects.SigadocMesaVirtualPage;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

/**
*
* @author Thomas Ribeiro
*/
public class AcessarQuadrosQuantitativosIT extends DriverBase {

	/*
	 * Caminho: Primeiro acesso na pagina de login
	 * Objetivo: Acessar os itens do menu "Quadros Quantitavos"
	 */

	private ExpectedCondition<Boolean> pageTitleStartsWith(final String searchString) {
		return driver -> driver.getTitle().toLowerCase().startsWith(searchString.toLowerCase());
	}

	@Test //corrigir
	public void acessarQuandrosQuantitativos() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();

		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.abrirQuadrosQuantitativos();

		WebDriverWait wait = new WebDriverWait(driver, 15, 100);
		wait.until(pageTitleStartsWith("PBDoc - Página Inicial"));
	}

}
