package br.gov.pb.codata.selenium.page_objects;

import br.gov.pb.codata.selenium.DriverBase;
import com.lazerycode.selenium.util.Query;
import org.openqa.selenium.By;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

/**
*
* @author Thomas Ribeiro
*/
public class SigadocHomePage {

    private Query searchBar = new Query().defaultLocator(By.name("q"));
    private Query googleSearch = new Query().defaultLocator(By.name("btnK"));
    private Query imFeelingLucky = new Query().defaultLocator(By.name("btnI"));

    public SigadocHomePage() throws Exception {
        initQueryObjects(this, DriverBase.getDriver());
    }

    public SigadocHomePage enterSearchTerm(String searchTerm) {
        searchBar.findWebElement().clear();
        searchBar.findWebElement().sendKeys(searchTerm);

        return this;
    }

    public SigadocHomePage submitSearch() {
        googleSearch.findWebElement().submit();

        return this;
    }

    public void getLucky() {
        imFeelingLucky.findWebElement().click();
    }

}