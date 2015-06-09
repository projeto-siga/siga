package br.gov.jfrj.siga.integration.test.util;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class IntegrationTestUtil {

	private String winHandle;

	public void preencheElemento(WebDriver driver, WebElement element, String valor) {
		new WebDriverWait(driver, 30).ignoring(NoSuchElementException.class, StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(element)).click();
		element.clear();
		element.sendKeys(valor);
	}

	public Select getSelect(WebDriver driver, WebElement element) {
		new WebDriverWait(driver, 30).ignoring(NoSuchElementException.class, StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(element)).click();
		return new Select(element);
	}

	public WebElement getContentDiv(WebDriver driver, By option, String content) {
		List<WebElement> elements = new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfAllElementsLocatedBy(option));
		WebElement div = null;
		for (WebElement webElement : elements) {
			if(webElement.getText().contains(content)) {
				div = webElement;
				break;
			}
		}
		return div;
	}

	public WebElement getWebElement(WebDriver driver, By option) {
		WebElement we = null;
		try {
			we = new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(option));
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return we;
	}

	public WebElement checkCampo(WebDriver driver, WebElement element) {
		WebElement we = null;
		try {
			we = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(element));
		} catch (TimeoutException e) {
		}
		return we;
	}

	public WebElement getWebElement(WebDriver driver, WebElement element, By option) {
		WebElement we = null;
		try {
			we = new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOf(element.findElement(option)));
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return we;
	}

	public WebElement getClickableElement(WebDriver driver, By option) {
		WebElement we = null;
		try {
			we = new WebDriverWait(driver, 30).ignoring(NoSuchElementException.class, StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(option));
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return we;
	}

	public WebElement getClickableElement(WebDriver driver, WebElement element) {
		WebElement we = null;
		try {
			we = new WebDriverWait(driver, 30).ignoring(NoSuchElementException.class, StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(element));
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return we;
	}

	public Boolean isElementInvisible(WebDriver driver, By option) {
		Boolean invisible = Boolean.FALSE;

		try {
			invisible = new WebDriverWait(driver, 30).until(ExpectedConditions.invisibilityOfElementLocated(option));
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		return invisible;
	}

	public Boolean isElementVisible(WebDriver driver, WebElement element) {
		WebElement we = null;

		try {
			we = new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOf(element));
		} catch(TimeoutException e) {
			e.printStackTrace();
		}

		return (we != null);
	}

	public WebDriver openPopup(WebDriver driver) {
		winHandle = driver.getWindowHandle();
		new WebDriverWait(driver, 30).until(popupDisponivel());

		Set<String> windowHandles = driver.getWindowHandles();

		for (String handle : windowHandles) {
			if(!handle.contains(winHandle)) {
				driver.switchTo().window(handle);
				System.out.println("Troca de controle para Popup!");
				break;
			}
		}

		new WebDriverWait(driver, 5);
		driver.manage().window().maximize();
		return driver;
	}

	public void closePopup(WebDriver driver) {
		if(driver.getWindowHandles().size() > 1) {
			Set<String> windowHandles = driver.getWindowHandles();
			for (String handle : windowHandles) {
				if(!handle.equals(winHandle)) {
					driver.switchTo().window(handle);
					driver.close();
				}
			}
		}
		driver.switchTo().window(winHandle);
	}

	public String closeAlertAndGetItsText(WebDriver driver) {
		new WebDriverWait(driver, 30).until(ExpectedConditions.alertIsPresent());
	    Alert alert = driver.switchTo().alert();
		String alertText = alert.getText();
		alert.accept();
		return alertText;
	}

	public Boolean isPDF(WebDriver driver) {
		if(driver.getPageSource().equals("")) {
			return true;
		} else {
			return false;
		}
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

	  public ExpectedCondition<Boolean> popupFechada() {
		    return new ExpectedCondition<Boolean>() {
		      @Override
		      public Boolean apply(WebDriver driver) {
		    	  System.out.println("Tamanho do WindowHandle: " + driver.getWindowHandles().size());
		    	  return driver.getWindowHandles().size() == 1;
		      }

		      @Override
		      public String toString() {
		        return new String("Popup fechar automaticamente!");
		      }
		    };
		  }
	  }
