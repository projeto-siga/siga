package br.gov.pb.codata.selenium.tests;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.PBDocNewDocumentPage;
import br.gov.pb.codata.selenium.page_objects.SigadocMesaVirtualPage;
import br.gov.pb.codata.selenium.util.text.Dictionary;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Thomas Ribeiro
 * Classe para criar novos documentos.
 */
public class NewDocumentIT extends DriverBase {

	private static Path path = Paths.get("src/test/resources/hino.pdf");
	private static String file = path.toAbsolutePath().toString();
	private String usuario2 = System.getenv("USUARIO2");
	private String classificationNumber = "01.01.01.02";

	/**
		Criar documento do modelo:Despacho
		Acesso: Limitado entre pessoas
		Subscritor: USUÁRIO DEFINIDO NO COMANDO
		Destinatário: Lotação
		Tipo Documental: 01.01.01.02
		PBdoc v10.0.8.24
	 */
	@Test
	public void newDispatchDocument() throws Exception {
		String model = "DespachoDespacho";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LIMITADO_PESSOAS);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiverInitials(usuario2);
		newDocumentPage.selectClassification(classificationNumber, 1);
		newDocumentPage.fillDescriptionTextarea("Teste Selenium. Texto de descrição para despacho.");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDispatchDocument");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}

	//@Test
	public void newExternalDocument() throws Exception {
		String model = "Documento Externo CapturadoDocumento Externo Capturado";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.PUBLICO);
		newDocumentPage.clickSubstitute();
		newDocumentPage.selectHolder(usuario2);
		newDocumentPage.selectClassification(classificationNumber, 0);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para documento externo capturado");
		newDocumentPage.selectFile(file);
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newExternalDocument");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}

	/*@Test
	public void newMemoDocument() throws Exception {
		String model = "MemorandoMemorando";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LIMITADO_ORGAO);
		newDocumentPage.selectReceiver(Dictionary.ORGAO_INTEGRADO);
		newDocumentPage.selectUnity(Dictionary.GEDES);
		newDocumentPage.selectClassification(classificationNumber, 1);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para memorando");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newMemoDocument");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}*/

	/*@Test
	public void newLegalDocument() throws Exception {
		String model = "OfícioOfício";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LIMITADO_PESSOA_DIVISAO);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.selectClassification(classificationNumber, 1);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para oficio");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newLegalDocument");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}*/
	
	/*@Test
	public void newLegalDocument2() throws Exception {
		String model = "OfícioOfício";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LIMITADO_PESSOA_DIVISAO);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiverInitials(usuario2);
		newDocumentPage.selectClassification(classificationNumber, 1);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para oficio");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newLegalDocument");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}*/

	@Test
	public void newDocumentAdminProcess() throws Exception {
		String model = "Processo AdministrativoGenérico";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.selectClassification(classificationNumber, 0);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para processo administrativo generico");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentAdminProcess");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newLegalExternalDocument() throws Exception {
		String model = "Ofício ExternoOfício Externo";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LIMITADO_PESSOA_DIVISAO);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiverInitials(usuario2);
		newDocumentPage.selectClassification(classificationNumber, 1);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para oficio externo");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newLegalExternalDocument");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newLegalInternalDocument() throws Exception {
		String model = "Oficio InternoOfício Interno";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LIMITADO_PESSOA_DIVISAO);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiverInitials(usuario2);
		newDocumentPage.selectClassification(classificationNumber, 1);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para oficio interno");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newLegalInternalDocument");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentLicenses() throws Exception {
		String model = "Processo de Afastamentos ou LicençasProcesso de Afastamentos ou Licenças";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para processo de Afastamentos ou Licenças");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentLicenses");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentMaterialAquisition() throws Exception {
		String model = "Processo de Aquisição de Material de ConsumoProcesso de Aquisição de Material de Consumo";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para processo de Aquisição de Material de Consumo");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentMaterialAquisition");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentMaterialAquisitionPermanent() throws Exception {
		String model = "Processo de Aquisição de Material PermanenteProcesso de Aquisição de Material Permanente";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para processo de Aquisição de Material Permanente");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentMaterialAquisitionPermanent");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentWindingProcess() throws Exception {
		String model = "Processo de Arrolamento de Bens Excedentes, Inservíveis ou em DesusoProcesso de Arrolamento de Bens Excedentes, Inservíveis ou em Desuso";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Arrolamento de Bens Excedentes, Inservíveis ou em Desuso");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentWindingProcess");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentHRTraining() throws Exception {
		String model = "Processo de Capacitação de Recursos HumanosProcesso de Capacitação de Recursos Humanos";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Capacitação de Recursos Humanos");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentHRTraining");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentRightsConcession() throws Exception {
		String model = "Processo de Concessão de Direitos e BenefíciosProcesso de Concessão de Direitos e Benefícios";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Concessão de Direitos e Benefícios");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentRightsConcession");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentPecuniaryConcession() throws Exception {
		String model = "Processo de Concessão e Incorporação de Vantagens PecuniáriasProcesso de Concessão e Incorporação de Vantagens Pecuniárias";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Concessão e Incorporação de Vantagens Pecuniárias");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentPecuniaryConcession");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentServicesContract() throws Exception {
		String model = "Processo de Contratação de ServiçosProcesso de Contratação de Serviços";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Contratação de Serviços");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentServicesContract");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentJudicialAction() throws Exception {
		String model = "Processo de Cumprimento de Ação JudicialProcesso de Cumprimento de Ação Judicial";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Cumprimento de Ação Judicial");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentJudicialAction");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentPaymentWho() throws Exception {
		String model = "Processo de Pagamento a Quem de DireitoProcesso de Pagamento a Quem de Direito";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Pagamento a Quem de Direito");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentPaymentWho");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentPaymentPublicBills() throws Exception {
		String model = "Processo de Pagamento de Contas de Utilidade PúblicaProcesso de Pagamento de Contas de Utilidade Pública";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Pagamento de Contas de Utilidade Pública");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentPaymentPublicBills");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentPaymentContract() throws Exception {
		String model = "Processo de Pagamento de ContratoProcesso de Pagamento de Contrato";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Pagamento de Contrato");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentPaymentContract");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentPaymentOfDebt() throws Exception {
		String model = "Processo de Pagamento de Despesas Inscritas em Reconhecimento de DívidaProcesso de Pagamento de Despesas Inscritas em Reconhecimento de Dívida";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Pagamento de Despesas Inscritas em Reconhecimento de Dívida");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentPaymentOfDebt");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentPaymentOfDaylies() throws Exception {
		String model = "Processo de Pagamento de Diárias e Ajuda de CustoProcesso de Pagamento de Diárias e Ajuda de Custo";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Pagamento de Diárias e Ajuda de Custo");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentPaymentOfDaylies");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentPaymentOfCharges() throws Exception {
		String model = "Processo de Pagamento de Encargos da DívidaProcesso de Pagamento de Encargos da Dívida";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Pagamento de Encargos da Dívida");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentPaymentOfCharges");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentPaymentOfVacation() throws Exception {
		String model = "Processo de Pagamento de Férias e Licença-Prêmio em PecúniaProcesso de Pagamento de Férias e Licença-Prêmio em Pecúnia";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Pagamento de Férias e Licença-Prêmio em Pecúnia");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentPaymentOfVacation");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	

	@Test
	public void newDocumentPaymentOfTaxes() throws Exception {
		String model = "Processo de Pagamento de Impostos, Taxas e Tarifas BancáriasProcesso de Pagamento de Impostos, Taxas e Tarifas Bancárias";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Pagamento de Impostos, Taxas e Tarifas Bancárias");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentPaymentOfTaxes");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentPaymentOfIndemnity() throws Exception {
		String model = "Processo de Pagamento de IndenizaçãoProcesso de Pagamento de Indenização";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Pagamento de Indenização");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentPaymentOfIndemnity");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentPaymentOfINSS() throws Exception {
		String model = "Processo de Pagamento de INSSProcesso de Pagamento de INSS";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Pagamento de INSS");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentPaymentOfINSS");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentPaymentOfRestitution() throws Exception {
		String model = "Processo de Pagamento de RestituiçãoProcesso de Pagamento de Restituição";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Pagamento de Restituição");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentPaymentOfRestitution");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentPaymentOfFunds() throws Exception {
		String model = "Processo de Pagamento de Verbas RescisóriasProcesso de Pagamento de Verbas Rescisórias";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Pagamento de Verbas Rescisórias");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentPaymentOfFunds");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentOfFowardAccount() throws Exception {
		String model = "Processo de Prestação de Contas de AdiantamentoProcesso de Prestação de Contas de Adiantamento";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Prestação de Contas de Adiantamento");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentOfFowardAccount");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentOfTitularity() throws Exception {
		String model = "Processo de Progressão por TitularidadeProcesso de Progressão por Titularidade";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Progressão por Titularidade");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentOfTitularity");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentOfTrafficTicket() throws Exception {
		String model = "Processo de Sindicância de Multa de TrânsitoProcesso de Sindicância de Multa de Trânsito";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo de Sindicância de Multa de Trânsito");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentOfTrafficTicket");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentPaymentoOfSalary() throws Exception {
		String model = "Processo para Pagamento de Pessoal e ReflexosProcesso para Pagamento de Pessoal e Reflexos";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo para Pagamento de Pessoal e Reflexos");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentPaymentoOfSalary");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}
	
	@Test
	public void newDocumentComission() throws Exception {
		String model = "Processo Relativo aos Trabalhos de Comissão Permanente ou Especial de LicitaçãoProcesso Relativo aos Trabalhos de Comissão Permanente ou Especial de Licitação";
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();
		PBDocNewDocumentPage newDocumentPage = new PBDocNewDocumentPage();
		newDocumentPage.selectModel(model);
		newDocumentPage.selectAccess(Dictionary.LMITADO_LOTACOES);
		newDocumentPage.selectReceiver(Dictionary.USUARIO);
		newDocumentPage.informReceiver(usuario2);
		newDocumentPage.fillDescriptionTextarea("Texto de descricao para Processo Relativo aos Trabalhos de Comissão Permanente ou Especial de Licitação");
		newDocumentPage.recordDocument();
		PBDocSeleniumController.checkNoError("NewDocumentIT.newDocumentComission");
		EditDocumentIt editDocument = new EditDocumentIt();
		editDocument.edit(Dictionary.FINALIZAR);
		editDocument.edit(Dictionary.ANEXAR);
		editDocument.edit(Dictionary.ASSINAR);
	}

}
