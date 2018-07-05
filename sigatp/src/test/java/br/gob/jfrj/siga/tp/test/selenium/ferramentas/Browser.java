package br.gob.jfrj.siga.tp.test.selenium.ferramentas;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public enum Browser {

	FIREFOX(0), INTERNETEXPLORER(1), CHROME(2);

	private int tipo;

	public int getTipo() {
		return tipo;
	}

	private Browser(int tipo) {
		this.tipo = tipo;
	}

	public WebDriver getDriver() throws Exception {
		switch (tipo) {
		case 0: // firefox
			FirefoxProfile firefoxProfile = new FirefoxProfile();
			firefoxProfile.setPreference("network.proxy.type", 2);
			firefoxProfile.setPreference("network.proxy.no_proxies_on", "localhost");

			return new FirefoxDriver(firefoxProfile);

		case 1: // IE
			System.setProperty("webdriver.ie.driver", "C:\\Desenvolvimento\\WebDrivers\\IEDriverServer_Win32_2.42.0\\IEDriverServer.exe");
			System.setProperty("webdriver.ie.driver.silent", "false");

			DesiredCapabilities capab = DesiredCapabilities.internetExplorer();
			capab.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

			return new InternetExplorerDriver(capab);

		case 2: // Chrome
			System.setProperty("webdriver.chrome.driver", "C:\\Desenvolvimento\\WebDrivers\\ChromeDriver_Win32_2.9\\chromedriver.exe");
			return new ChromeDriver();

		default:
			return null;
		}
	}
}
