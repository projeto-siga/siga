package br.gov.pb.codata.selenium;

import org.openqa.selenium.WebDriver;

import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;
import br.gov.pb.codata.selenium.tests.exceptions.PBDocGenericError;
import br.gov.pb.codata.selenium.util.text.Dictionary;

/**
*
* @author Thomas Ribeiro
*/
public class PBDocSeleniumController extends DriverBase {

	public static WebDriver start() throws Exception {
		return SigadocStartUp.startUp();
	}

	public static void checkNoError(String message) throws Exception {
		WebDriver driver = DriverBase.getDriver();
		if (driver.getTitle().equals(Dictionary.ERRO_GERAL)) {
			throw new PBDocGenericError(message);
		}
	}

}
