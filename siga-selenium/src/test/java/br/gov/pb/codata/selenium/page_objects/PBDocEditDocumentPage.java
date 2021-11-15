package br.gov.pb.codata.selenium.page_objects;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.tests.AttachDocumentIt;
import br.gov.pb.codata.selenium.tests.SignDocumentIT;
import br.gov.pb.codata.selenium.tests.exceptions.PBDocGenericError;
import br.gov.pb.codata.selenium.util.text.Dictionary;
import com.lazerycode.selenium.util.Query;
import org.openqa.selenium.By;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

/**
*
* @author Thomas Ribeiro
* Classe de controle para página de edição de documentos criados.
*/
public class PBDocEditDocumentPage {

	private final Query linkSignDocument = new Query().defaultLocator(By.xpath("//*[@id='assinar']"));
	private final Query linkFinishDocument = new Query().defaultLocator(By.xpath("//*[@id='finalizar']"));
	private final Query linkAttachDocument = new Query().defaultLocator(By.xpath("//*[@id='anexar']"));

	public PBDocEditDocumentPage() throws Exception {
		initQueryObjects(this, DriverBase.getDriver());
	}

	public void doAction(String action) throws PBDocGenericError, Exception {
		switch (action) {
		case Dictionary.ASSINAR:
			linkSignDocument.findWebElement().click();
			PBDocSeleniumController.checkNoError("EditDocumentIt.edit:" + action);
			SignDocumentIT signDocumentIT = new SignDocumentIT();
			signDocumentIT.signDocument();
			break;
		case Dictionary.FINALIZAR:
			linkFinishDocument.findWebElement().click();
			PBDocSeleniumController.checkNoError("EditDocumentIt.edit:" + action);
			break;
		case Dictionary.ANEXAR:
			linkAttachDocument.findWebElement().click();
			PBDocSeleniumController.checkNoError("EditDocumentIt.edit:" + action);
			AttachDocumentIt attachDocumentIt = new AttachDocumentIt();
			attachDocumentIt.attachDocument();
			break;
		default:
			break;
		}
	}
}
