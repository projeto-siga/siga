package br.gov.jfrj.siga.integration.test.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MudancaCodigoExpectedCondition implements ExpectedCondition<Boolean> {

	@Override
	public Boolean apply(WebDriver input) {				
		String codigo = input.findElement(By.xpath("/html/body/div[5]/div[1]/div/table/tbody/tr/td/span/table[1]/tbody/tr[1]/td/p")).getText();
		codigo = codigo.substring(codigo.indexOf("TMP"));
		return null;
	}

}
