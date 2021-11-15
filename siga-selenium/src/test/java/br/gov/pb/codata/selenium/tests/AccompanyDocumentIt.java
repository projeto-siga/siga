package br.gov.pb.codata.selenium.tests;

import java.nio.file.Path;
import java.nio.file.Paths;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.PBDocAccompanyDocumentPage;
import br.gov.pb.codata.selenium.util.text.Dictionary;

/**
*
* @author Thomas Ribeiro
*/
public class AccompanyDocumentIt extends DriverBase {

	// Objetivo: Fazer o acompanhamento de um documento
	
	private String responsable = System.getenv("USUARIO");

	public void accompanyDocument() throws Exception {
		PBDocAccompanyDocumentPage accompanyDocumentPage = new PBDocAccompanyDocumentPage();
		accompanyDocumentPage.fillinputDate("01012020");
		accompanyDocumentPage.selectResponsableType(Dictionary.USUARIO);
		accompanyDocumentPage.selectResponsable(responsable);
		accompanyDocumentPage.selectRole(Dictionary.INTERESSADO);
		accompanyDocumentPage.submitForm();
		PBDocSeleniumController.checkNoError("AccompanyDocumentIt.accompanyDocument");
	}
}
