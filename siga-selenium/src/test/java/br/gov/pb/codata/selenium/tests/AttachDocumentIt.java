package br.gov.pb.codata.selenium.tests;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.PBDocAttachDocumentPage;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
*
* @author Thomas Ribeiro
* Classe para controle de fluxo de anexar um arquivo ao documento criado.
*/
public class AttachDocumentIt extends DriverBase {

	private String subscritor = System.getenv("USUARIO");
	private String titular = System.getenv("USUARIO2");
	private static Path path = Paths.get("src/test/resources/hino.pdf");
	private static String file = path.toAbsolutePath().toString();

	/**
		 Anexar aquivo no documento, assina e autentica.
		 Data: 01/01/2020
		 Responsável: USUÁRIO DEFINIDO NO COMANDO
		 Titular: Lotação
		 Ficheiro: siga-selenium/src/test/resources/hino.pdf
		 PBdoc v10.0.8.24
	 */
	public void attachDocument() throws Exception {
		PBDocAttachDocumentPage attachDocumentPage = new PBDocAttachDocumentPage();
		attachDocumentPage.fillinputDate("01012020");
		attachDocumentPage.selectSubscritor(subscritor);
		attachDocumentPage.selectTitular(titular);
		attachDocumentPage.selectFile(file);
		attachDocumentPage.fillinputDescription("Teste Selenium. Descrição de anexo no documento.");
		attachDocumentPage.submitForm();
		attachDocumentPage.authenticateAttachement();
		attachDocumentPage.returnToEditPage();
		PBDocSeleniumController.checkNoError("AttachDocumentIt.attachDocument");
	}
}
