package br.gov.pb.codata.selenium.tests;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.page_objects.PBDocEditDocumentPage;

/**
 *
 * @author Thomas Ribeiro
 * Classe para controle de fluxo para editar documentos no sistema
 */
public class EditDocumentIt extends DriverBase {

	public void edit(String action) throws Exception {
		PBDocEditDocumentPage editDocumentPage = new PBDocEditDocumentPage();
		editDocumentPage.doAction(action);
	}
}
