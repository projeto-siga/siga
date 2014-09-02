package br.gov.jfrj.siga.integration.test.util;

import org.openqa.selenium.WebDriver;

import com.google.common.base.Predicate;

public class PopupExpectedCondition implements Predicate<WebDriver>{

	@Override
	public boolean apply(WebDriver input) {
		return input.getWindowHandles().size() > 1;
	}

}
