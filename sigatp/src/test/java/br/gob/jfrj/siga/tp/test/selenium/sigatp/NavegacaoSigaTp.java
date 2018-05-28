package br.gob.jfrj.siga.tp.test.selenium.sigatp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.gob.jfrj.siga.tp.test.selenium.ferramentas.Pagina;

public class NavegacaoSigaTp extends NavegacaoSiga {

	private static final int _TEMPO_INTERVALO_CARREGAMENTO_PAGINA_EM_SEGUNDOS = 3;

	private void pausar() {
		try {
			Thread.sleep(_TEMPO_INTERVALO_CARREGAMENTO_PAGINA_EM_SEGUNDOS * 1000);
		} catch (InterruptedException e) {
		}
	}

	public NavegacaoSigaTp(Pagina pagina) {
		super(pagina);
	}

	@Override
	public void logar(String login, String senha) {
		super.acessarSiga();
		super.logar(login, senha);

		/*
		 * ROUBADO:
		 * 
		 * driver.findElement(By.xpath("//a[contains(text(), 'SIGA')]")).click(); driver.findElement(By.xpath("//a[contains(text(), 'Módulos')]")).click();
		 * //driver.findElement(By.cssSelector("a[href*='sigatp']")).click();
		 * 
		 * WebElement botao = driver.findElement(By.cssSelector("a[href*='sigatp']")); botao.click();
		 */

		getPagina().redirecionarViaJavaScript("/sigatp/");

	}

	public void acessarInclusaoCondutor() {
		getPagina().clicarElementoPorId("menuAdm");
		getPagina().clicarElementoPorId("menuAdmCondutores");
		getPagina().clicarElementoPorId("botaoIncluirCondutor");
	}

	public void acessarInclusaoRequisicao() {

		WebElement one = (new WebDriverWait(getPagina().getDriver(), 25)).until(new ExpectedCondition<WebElement>() {
			@Override
			public WebElement apply(WebDriver d) {
				return d.findElement(By.id("menuRequisicoes"));
			}
		});
		Actions builder = new Actions(getPagina().getDriver());
		builder.click(one);
		builder.build().perform();
		WebElement two = (new WebDriverWait(getPagina().getDriver(), 25)).until(new ExpectedCondition<WebElement>() {
			@Override
			public WebElement apply(WebDriver d) {
				return d.findElement(By.id("menuRequisicoesIncluir"));
			}
		});

		builder.click(two);
		builder.build().perform();

		pausar();

		/*
		 * WebElement menu = getPagina().getDriver().findElement(By.id("menuRequisicoes")); pausar(); menu.click();
		 * 
		 * WebElement menuHoverLink = getPagina().getDriver().findElement(By.id("menuRequisicoesIncluir")); Locatable hoverItem = (Locatable) menuHoverLink; Mouse mouse = ((HasInputDevices)
		 * getPagina().getDriver()).getMouse(); mouse.mouseMove(hoverItem.getCoordinates()); mouse.click(hoverItem.getCoordinates());
		 */

		// getPagina().getDriver().findElement(By.id("menuRequisicoesIncluir")).click();

	}

	public void acessarListarRequisicoes() {
		getPagina().clicarElementoPorId("menuRequisicoes");
		getPagina().clicarElementoPorId("menuRequisicoesListar");
	}

	public void acessarMenuTodasListarRequisicoes() {
		getPagina().clicarElementoPorId("menuRequisicoesMostrarTodas");
	}

	public void acessarListarRequisicoesParaAprovacao() {
		pausar();
		getPagina().clicarElementoPorId("menuRequisicoes");
		pausar();
		getPagina().clicarElementoPorId("menuRequisicoesAprovar");
	}

	public void acessarMenuAbertasListarRequisicoesParaAprovacao() {
		pausar();
		getPagina().clicarElementoPorId("menuRequisicoesAprovarMostrarAbertas");
	}
}
