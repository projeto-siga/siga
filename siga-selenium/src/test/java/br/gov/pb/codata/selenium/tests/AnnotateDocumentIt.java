package br.gov.pb.codata.selenium.tests;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.PBDocAnnotateDocumentPage;

/**
*
* @author Thomas Ribeiro
*/
public class AnnotateDocumentIt extends DriverBase {

	// Objetivo: Fazer anotações em documentos no sistema

	private String subscritor = System.getenv("USUARIO");

	public void annotateDocument() throws Exception {
		PBDocAnnotateDocumentPage annotateDocumentPage = new PBDocAnnotateDocumentPage();
		annotateDocumentPage.fillinputDate("01012020");
		annotateDocumentPage.selectSubscritor(subscritor);
		annotateDocumentPage.fillinputSubscritorFunction("Analista de testes");
		annotateDocumentPage.filltextareaNoteDescription("Descricao da nota no documento");
		annotateDocumentPage.submitForm();
		PBDocSeleniumController.checkNoError("AnnotateDocumentIt.annotateDocument");
	}
}
