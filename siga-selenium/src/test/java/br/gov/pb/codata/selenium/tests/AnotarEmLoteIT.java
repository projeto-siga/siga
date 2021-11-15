package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class AnotarEmLoteIT extends DriverBase {

	/**
	 * Caminho: Na mesa digital, clica em algum documento onde é direncionado para os campos. Clica em anotar para fazer as anotações.
	 * 
	 * @author Allysson Cruz
	 * @author Thomas Ribeiro
	 */

	//Refazer
	@Test
	public void anotarDocumeto() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/mov/anotar_lote");

		//Insere a Data
		driver.findElement(By.name("dtMovString")).sendKeys("29/03/2020");

		//seleciona o responsavel
		driver.findElement(By.linkText("COD10288")).click();

		//Seleciona o check Substituto, caso tenha
		driver.findElement(By.name("substituicao")).click();

		//Insere funcao confianca
		driver.findElement(By.name("nmFuncaoSubscritor")).sendKeys("Descriscao função");

		//Insere funcao confianca
		driver.findElement(By.name("descrMov")).sendKeys("tesste");

		//clicar no botão ok
		driver.findElement(By.id("ok")).submit();

	}

}
