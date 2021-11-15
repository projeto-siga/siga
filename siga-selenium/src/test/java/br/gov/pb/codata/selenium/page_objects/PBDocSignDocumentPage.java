package br.gov.pb.codata.selenium.page_objects;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.lazerycode.selenium.util.Query;

import br.gov.pb.codata.selenium.DriverBase;

/**
*
* @author Thomas Ribeiro
*/
public class PBDocSignDocumentPage {

	private final Query confirmPassWordButton = new Query().defaultLocator(By.xpath("//*[@id='senhaOk']"));
	private final Query signButton = new Query().defaultLocator(By.xpath("//*[@id='bot-assinar']"));
	private final Query passwordRadio = new Query().defaultLocator(By.xpath("//*[@for='ad_password_0']"));
	private final Query passwordInput = new Query().defaultLocator(By.xpath("//*[@id='senhaUsuarioSubscritor']"));

	public PBDocSignDocumentPage() throws Exception {
		initQueryObjects(this, DriverBase.getDriver());
	}

	private static ExpectedCondition<Boolean> pageTitleNotEquals(final String searchString) {
		return driver -> !driver.getTitle().toLowerCase().equals(searchString.toLowerCase());
	}

	public PBDocSignDocumentPage signDocument() throws Exception {
		WebDriver driver = DriverBase.getDriver();
		passwordRadio.findWebElement().click();//Clicar na opção usando senha no radio (Opção não default)
		signButton.findWebElement().click();
		{
			WebElement element = signButton.findWebElement();
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		Thread.sleep(5000);
		passwordInput.findWebElement().sendKeys(SigadocLoginPage.SENHA);
		confirmPassWordButton.findWebElement().click();
		WebDriverWait wait = new WebDriverWait(driver, 30, 100);
		wait.until(pageTitleNotEquals("PBDoc - Documento"));
		return this;
	}
}
