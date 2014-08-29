package br.gov.jfrj.siga.integration.test.util;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class IntegrationTestUtil {
	
	private String winHandle;
	
	public void preencheElemento(WebDriver driver, WebElement element, String valor) {
		//new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOf(element));
		element.click();
		element.clear();
		element.sendKeys(valor);
	}
	
	public Select getSelect(WebDriver driver, WebElement element) {
		new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(element)).click();
		element.click();
		return new Select(element);
	}
		
	public WebDriver openPopup(WebDriver driver) {
		winHandle = driver.getWindowHandle();
		new WebDriverWait(driver, 15).until(popupDisponivel());
		
		Set<String> windowHandles = driver.getWindowHandles();
		
		for (String handle : windowHandles) {
			if(!handle.contains(winHandle)) {
				driver.switchTo().window(handle); 	
			}
		}

		driver.manage().window().maximize();		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;
	}
	
	public WebDriver closePopup(WebDriver driver) {
		driver.close();
		return changeFromPopup(driver);
	}
	
	public WebDriver changeFromPopup(WebDriver driver) {
		driver.switchTo().window(winHandle);
		return driver;
	}
	
	  public ExpectedCondition<Boolean> trocaURL(final String URL) {
		    return new ExpectedCondition<Boolean>() {
		      @Override
		      public Boolean apply(WebDriver driver) {		    	  
		    	  return !URL.equals(driver.getCurrentUrl());
		      }
		      
		      @Override
		      public String toString() {
		        return new String("Mudança de URL!");
		      }
		    };
	  }
	
	  public ExpectedCondition<Boolean> popupDisponivel() {
		    return new ExpectedCondition<Boolean>() {
		      @Override
		      public Boolean apply(WebDriver driver) {
		    	  System.out.println("Tamanho do WindowHandle: " + driver.getWindowHandles().size());
		    	  return driver.getWindowHandles().size() > 1;
		      }
		      
		      @Override
		      public String toString() {
		        return new String("Localizar a popup aberta!");
		      }
		    };
		  }
}
