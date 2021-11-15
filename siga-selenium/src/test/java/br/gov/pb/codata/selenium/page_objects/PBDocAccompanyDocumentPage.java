package br.gov.pb.codata.selenium.page_objects;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.lazerycode.selenium.util.Query;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.util.text.Dictionary;

/**
*
* @author Thomas Ribeiro
*/
public class PBDocAccompanyDocumentPage {

	private final Query inputDate = new Query().defaultLocator(By.xpath("//*[@name='dtMovString']"));
	private final Query dropdownResponsable = new Query().defaultLocator(By.xpath("//*[@id='tipoResponsavel']"));
	private final Query buttonResponsable = new Query().defaultLocator(By.xpath("//*[@id='responsavelSelButton']"));
	private final Query dropdownRole = new Query().defaultLocator(By.xpath("//*[@name='idPapel']"));
	private final Query submitForm = new Query().defaultLocator(By.xpath("//input[@type='submit']"));

	public PBDocAccompanyDocumentPage() throws Exception {
		initQueryObjects(this, DriverBase.getDriver());
	}

	public PBDocAccompanyDocumentPage fillinputDate(String text) {
		inputDate.findWebElement().sendKeys(text);
		return this;
	}
	
	public PBDocAccompanyDocumentPage selectResponsableType(String responsableType) {
		Select select = new Select(dropdownResponsable.findWebElement());
		select.selectByVisibleText(responsableType);
		return this;
	}
	
	public PBDocAccompanyDocumentPage selectResponsable(String responsable) throws Exception {
		WebDriver driver = DriverBase.getDriver();
		buttonResponsable.findWebElement().click();
		{
			WebElement element = buttonResponsable.findWebElement();
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(0);
		driver.findElement(By.cssSelector("input.form-control")).sendKeys(responsable);
		Select select = new Select(driver.findElement(By.cssSelector("select.form-control")));
		select.selectByVisibleText(Dictionary.CODATA);
		driver.findElement(By.cssSelector("button.btn.btn-primary")).submit();
		driver.findElement(By.linkText(responsable)).click();
		driver.switchTo().defaultContent();
		return this;
	}
	
	public PBDocAccompanyDocumentPage selectRole(String role) {
		Select select = new Select(dropdownRole.findWebElement());
		select.selectByVisibleText(role);
		return this;
	}
	
	public PBDocAccompanyDocumentPage submitForm() {
		submitForm.findWebElement().click();
		return this;
	}

}
