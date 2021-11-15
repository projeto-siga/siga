package br.gov.pb.codata.selenium.page_objects;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

import org.openqa.selenium.By;

import com.lazerycode.selenium.util.Query;

import br.gov.pb.codata.selenium.DriverBase;

/**
*
* @author Thomas Ribeiro
*/
public class SigadocMesaVirtualPage {

	private final Query criarNovo = new Query().defaultLocator(By.linkText("Criar Novo"));
	private final Query menuPrincipal = new Query().defaultLocator(By.linkText("MENU PRINCIPAL"));
	private final Query quadrosQuantitativos = new Query().defaultLocator(By.linkText("Quadros Quantitativos"));
	private final Query gestaoDeIdentidade = new Query().defaultLocator(By.linkText("Gestão de Identidade"));
	private final Query cadastroDePessoa = new Query().defaultLocator(By.linkText("Cadastro de Pessoa"));

	public SigadocMesaVirtualPage() throws Exception {
		initQueryObjects(this, DriverBase.getDriver());
	}

	public SigadocMesaVirtualPage clicarCriarNovo() {
		criarNovo.findWebElement().click();

		return this;
	}

	public SigadocMesaVirtualPage abrirQuadrosQuantitativos() {
		menuPrincipal.findWebElement().click();
		quadrosQuantitativos.findWebElement().click();

		return this;
	}

	public SigadocMesaVirtualPage abrirCadastroDePessoa() {
		menuPrincipal.findWebElement().click();
		gestaoDeIdentidade.findWebElement().click();
		cadastroDePessoa.findWebElement().click();

		return this;
	}

}