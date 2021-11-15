package br.gov.pb.codata.selenium.page_objects;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

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
public class PBDocAnnotateDocumentPage {

	private final Query inputDate = new Query().defaultLocator(By.xpath("//*[@name='dtMovString']"));
	private final Query buttonSubscritor = new Query().defaultLocator(By.xpath("//*[@id='subscritorSelButton']"));
	private final Query inputSubscritorFunction = new Query().defaultLocator(By.xpath("//*[@name='nmFuncaoSubscritor']"));
	private final Query textareaNoteDescription = new Query().defaultLocator(By.xpath("//*[@name='descrMov']"));
	private final Query submitForm = new Query().defaultLocator(By.xpath("//input[@type='submit']"));

	public PBDocAnnotateDocumentPage() throws Exception {
		initQueryObjects(this, DriverBase.getDriver());
	}

	public PBDocAnnotateDocumentPage fillinputDate(String text) {
		inputDate.findWebElement().sendKeys(text);
		return this;
	}

	public PBDocAnnotateDocumentPage selectSubscritor(String subscritor) throws Exception {
		WebDriver driver = DriverBase.getDriver();
		buttonSubscritor.findWebElement().click();
		{
			WebElement element = buttonSubscritor.findWebElement();
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(0);
		driver.findElement(By.cssSelector("input.form-control")).sendKeys(subscritor);
		Select select = new Select(driver.findElement(By.cssSelector("select.form-control")));
		select.selectByVisibleText(Dictionary.CODATA);
		driver.findElement(By.cssSelector("button.btn.btn-primary")).submit();
		driver.findElement(By.linkText(subscritor)).click();
		driver.switchTo().defaultContent();
		return this;
	}

	public PBDocAnnotateDocumentPage fillinputSubscritorFunction(String text) {
		inputSubscritorFunction.findWebElement().sendKeys(text);
		return this;
	}

	public PBDocAnnotateDocumentPage filltextareaNoteDescription(String text) {
		textareaNoteDescription.findWebElement().sendKeys(text);
		return this;
	}

	public PBDocAnnotateDocumentPage submitForm() {
		submitForm.findWebElement().click();
		return this;
	}

}
