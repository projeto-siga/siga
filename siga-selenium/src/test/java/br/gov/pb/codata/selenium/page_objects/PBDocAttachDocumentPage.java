package br.gov.pb.codata.selenium.page_objects;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.util.text.Dictionary;
import com.lazerycode.selenium.util.Query;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.Iterator;
import java.util.Set;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

/**
*
* @author Thomas Ribeiro
*/
public class PBDocAttachDocumentPage {

	private final Query inputDate = new Query().defaultLocator(By.xpath("//*[@name='dtMovString']"));
	private final Query buttonSubscritor = new Query().defaultLocator(By.xpath("//*[@id='subscritorSelButton']"));
	private final Query buttonTitular = new Query().defaultLocator(By.xpath("//*[@id='titularSelButton']"));
	private final Query inputDescription = new Query().defaultLocator(By.xpath("//*[@name='descrMov']"));
	private final Query submitForm = new Query().defaultLocator(By.xpath("//input[@type='submit']"));
	private final Query inputFile = new Query().defaultLocator(By.id("arquivo"));
	private final Query linkAuthenticate = new Query().defaultLocator(By.linkText("Assinar"));
	private final Query buttonCancel = new Query().defaultLocator(By.cssSelector("input[value='Cancela']"));

	public PBDocAttachDocumentPage() throws Exception {
		initQueryObjects(this, DriverBase.getDriver());
	}

	public PBDocAttachDocumentPage fillinputDate(String text) {
		inputDate.findWebElement().sendKeys(text);
		return this;
	}

	public PBDocAttachDocumentPage selectSubscritor(String subscritor) throws Exception {
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
		//select.selectByVisibleText(Dictionary.CODATA);
		select.selectByVisibleText(Dictionary.CODATA);
		driver.findElement(By.cssSelector("button.btn.btn-primary")).submit();
		driver.findElement(By.linkText(subscritor)).click();
		driver.switchTo().defaultContent();
		return this;
	}

	public PBDocAttachDocumentPage selectTitular(String titular) throws Exception {
		WebDriver driver = DriverBase.getDriver();
		buttonTitular.findWebElement().click();
		{
			WebElement element = buttonTitular.findWebElement();
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(0);
		driver.findElement(By.cssSelector("input.form-control")).sendKeys(titular);
		Select select = new Select(driver.findElement(By.cssSelector("select.form-control")));
		//select.selectByVisibleText(Dictionary.CODATA);
		select.selectByVisibleText(Dictionary.CODATA);
		driver.findElement(By.cssSelector("button.btn.btn-primary")).submit();
		driver.findElement(By.linkText(titular)).click();
		driver.switchTo().defaultContent();
		return this;
	}

	public PBDocAttachDocumentPage fillinputDescription(String text) {
		inputDescription.findWebElement().sendKeys(text);
		return this;
	}

	public PBDocAttachDocumentPage submitForm() {
		submitForm.findWebElement().click();
		return this;
	}

	public PBDocAttachDocumentPage selectFile(String file) {
		inputFile.findWebElement().sendKeys(file);
		return this;
	}

	public PBDocAttachDocumentPage authenticateAttachement() throws Exception {
		WebDriver driver = DriverBase.getDriver();
		String parent = driver.getWindowHandle();
		Set<String> s = driver.getWindowHandles();
		Iterator<String> I1 = s.iterator();
		while (I1.hasNext()) {
			String child_window = I1.next();
			if (!parent.equals(child_window)) {
				driver.switchTo().window(child_window);
				PBDocSignDocumentPage signDocumentPage = new PBDocSignDocumentPage();
				signDocumentPage.signDocument();
			}
		}
		driver.switchTo().window(parent);
		System.out.println(driver.getTitle());
		return this;
	}

	public PBDocAttachDocumentPage returnToEditPage() {
		buttonCancel.findWebElement().click();
		return this;
	}

}
