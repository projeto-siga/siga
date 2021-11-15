package br.gov.pb.codata.selenium.page_objects;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.lazerycode.selenium.util.Query;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.util.text.Dictionary;

/**
*
* @author Thomas Ribeiro
*/
public class PBDocNewDocumentPage extends PBDocSeleniumController {

	private final Query buttonUnity = new Query().defaultLocator(By.xpath("//*[@id='lotacaoDestinatarioSelButton']"));
	private final Query buttonClassification = new Query().defaultLocator(By.xpath("//*[@id='classificacaoSelButton']"));
	private final Query buttonHolder = new Query().defaultLocator(By.id("titularSelButton"));
	private final Query buttonOrg = new Query().defaultLocator(By.id("orgaoExternoDestinatarioSelButton"));
	private final Query buttonRecord = new Query().defaultLocator(By.cssSelector(".btn-primary > u"));
	private final Query checkboxSubstitute = new Query().defaultLocator(By.name("exDocumentoDTO.substituicao"));
	private final Query dropdownAccess = new Query().defaultLocator(By.name("exDocumentoDTO.nivelAcesso"));
	private final Query dropdownModel = new Query().defaultLocator(By.cssSelector(".selected-label"));
	private final Query dropdownReceiver = new Query().defaultLocator(By.name("exDocumentoDTO.tipoDestinatario"));
	private final Query inputClassification = new Query().defaultLocator(By.name("exDocumentoDTO.classificacaoSel.sigla"));
	private final Query inputFile = new Query().defaultLocator(By.id("arquivo"));
	//private final Query inputReceiver = new Query().defaultLocator(By.name("exDocumentoDTO.nmDestinatario"));
	private final Query inputReceiver = new Query().defaultLocator(By.id("formulario_exDocumentoDTO.destinatarioSel_sigla"));
	private final Query inputReceiverInitials = new Query().defaultLocator(By.name("exDocumentoDTO.destinatarioSel.sigla"));
	private final Query textareaDescription = new Query().defaultLocator(By.id("descrDocumento"));

	public PBDocNewDocumentPage() throws Exception {
		initQueryObjects(this, DriverBase.getDriver());
	}

	public PBDocNewDocumentPage selectModel(String model) throws Exception {
		WebDriver driver = DriverBase.getDriver();
		dropdownModel.findWebElement().click();
		driver.findElement(By.linkText(model)).click();
		return this;
	}

	public PBDocNewDocumentPage fillDescriptionTextarea(String text) {
		textareaDescription.findWebElement().sendKeys(text);
		return this;
	}

	public PBDocNewDocumentPage recordDocument() {
		buttonRecord.findWebElement().click();
		return this;
	}

	public PBDocNewDocumentPage selectFile(String file) {
		inputFile.findWebElement().sendKeys(file);
		return this;
	}

	public PBDocNewDocumentPage selectClassification(String classificationNumber, Integer frame) throws Exception {
		WebDriver driver = DriverBase.getDriver();
		buttonClassification.findWebElement().click();
		{
			WebElement element = buttonClassification.findWebElement();
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(frame);
		driver.findElement(By.linkText(classificationNumber)).click();
		driver.switchTo().defaultContent();
		return this;
	}

	public PBDocNewDocumentPage selectAccess(String access) {
		Select select = new Select(dropdownAccess.findWebElement());
		select.selectByVisibleText(access);
		return this;
	}

	public PBDocNewDocumentPage selectReceiver(String receiver) {
		Select select = new Select(dropdownReceiver.findWebElement());
		select.selectByVisibleText(receiver);
		return this;
	}

	public PBDocNewDocumentPage informReceiverInitials(String receiver) {
		if (receiver == null || receiver.length() < 1) {
			receiver = System.getenv("USUARIO");
		}
		inputReceiverInitials.findWebElement().sendKeys(receiver);
		return this;
	}

	public PBDocNewDocumentPage informClassification(String classification) {
		inputClassification.findWebElement().sendKeys(classification);
		return this;
	}

	public PBDocNewDocumentPage clickSubstitute() {
		checkboxSubstitute.findWebElement().click();
		return this;
	}

	public PBDocNewDocumentPage selectHolder(String holder) throws Exception {
		WebDriver driver = DriverBase.getDriver();
		buttonHolder.findWebElement().click();
		{
			WebElement element = buttonHolder.findWebElement();
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(0);
		driver.findElement(By.cssSelector("input.form-control")).sendKeys(holder);
		Select select = new Select(driver.findElement(By.cssSelector("select.form-control")));
		select.selectByVisibleText(Dictionary.CODATA);
		driver.findElement(By.cssSelector("button.btn.btn-primary")).submit();
		driver.findElement(By.linkText(holder)).click();
		driver.switchTo().defaultContent();
		return this;
	}

	public PBDocNewDocumentPage selectUnity(String unity) throws Exception {
		WebDriver driver = DriverBase.getDriver();
		buttonUnity.findWebElement().click();
		{
			WebElement element = buttonUnity.findWebElement();
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		Thread.sleep(2000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("input.form-control")).sendKeys(unity);
		Select select = new Select(driver.findElement(By.cssSelector("select.form-control")));
		select.selectByVisibleText(Dictionary.CODATA);
		driver.findElement(By.cssSelector("button.btn.btn-primary")).submit();
		driver.findElement(By.linkText(unity)).click();
		driver.switchTo().defaultContent();
		return this;
	}

	public PBDocNewDocumentPage selectOrg(String org) throws Exception {
		WebDriver driver = DriverBase.getDriver();
		buttonOrg.findWebElement().click();
		{
			WebElement element = buttonOrg.findWebElement();
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		Thread.sleep(2000);
		driver.switchTo().frame(0);
		driver.findElement(By.cssSelector("input.form-control")).sendKeys(org);
		Select select = new Select(driver.findElement(By.cssSelector("select.form-control")));
		select.selectByVisibleText(Dictionary.CODATA);
		driver.findElement(By.cssSelector("button.btn.btn-primary")).submit();
		driver.findElement(By.linkText(org)).click();
		driver.switchTo().defaultContent();
		return this;
	}

	public PBDocNewDocumentPage informReceiver(String receiver) {
		inputReceiver.findWebElement().sendKeys(receiver);
		return this;
	}

}