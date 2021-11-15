package br.gov.pb.codata.selenium.config;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

//Dev:Thomas Ribeiro
public interface DriverSetup {

	RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities);

}