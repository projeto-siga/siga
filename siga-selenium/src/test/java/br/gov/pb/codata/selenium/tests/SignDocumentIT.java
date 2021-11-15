package br.gov.pb.codata.selenium.tests;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.PBDocSignDocumentPage;

public class SignDocumentIT extends DriverBase {

	// Objetivo: Assinar os documentos salvos para tramitarem1

	public void signDocument() throws Exception {
		PBDocSignDocumentPage signDocumentPage = new PBDocSignDocumentPage();
		signDocumentPage.signDocument();
		PBDocSeleniumController.checkNoError("SignDocumentIT.signDocument");
	}
}
