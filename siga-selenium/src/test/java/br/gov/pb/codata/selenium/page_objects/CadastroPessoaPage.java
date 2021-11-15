package br.gov.pb.codata.selenium.page_objects;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

import org.openqa.selenium.By;

import com.lazerycode.selenium.util.Query;

import br.gov.pb.codata.selenium.DriverBase;

/**
*
* @author Allysson Cruz
* @author Thomas Ribeiro
*/
public class CadastroPessoaPage {
	
	public static final String URL = "https://desenv.pbdigital.pb.gov.br/sigaex/app/despacho/tipodespacho/editar";
	
    private final Query inputOrgao = new Query().defaultLocator(By.name("idOrgaoUsu"));
    private final Query inputCargoPesquisa = new Query().defaultLocator(By.name("idCargoPesquisa"));
    private final Query inputFuncaoConfianca = new Query().defaultLocator(By.name("idFuncaoPesquisa"));
    private final Query inputLotacao = new Query().defaultLocator(By.name("idLotacao"));
    private final Query inputNome = new Query().defaultLocator(By.id("nome"));
    private final Query inputDtNascimento = new Query().defaultLocator(By.id("dtNascimento"));
    private final Query inputCpf = new Query().defaultLocator(By.id("cpfPesquisa"));
    private final Query inputEmail = new Query().defaultLocator(By.id("email"));
    
    public CadastroPessoaPage() throws Exception {
        initQueryObjects(this, DriverBase.getDriver());
    }
    
    //clicar em incluir primeiro, depois preenche o formulario
    
    public CadastroPessoaPage PreencherCampos (String orgao, String cargo, String funcao, String unidade, String nome, String dtnascimento, String cpf, String email) {
    	inputOrgao.findWebElement().sendKeys(orgao);
    	inputCargoPesquisa.findWebElement().sendKeys(cargo);
    	inputFuncaoConfianca.findWebElement().sendKeys(funcao);
    	inputLotacao.findWebElement().sendKeys(unidade);
    	inputNome.findWebElement().sendKeys(nome);
    	inputDtNascimento.findWebElement().sendKeys(dtnascimento);
    	inputCpf.findWebElement().sendKeys(cpf);
    	inputEmail.findWebElement().sendKeys(email);
    	
       	    	return this;
    	
    }
    public CadastroPessoaPage enviarForm() {
        inputOrgao.findWebElement().submit();

        return this;
    }
    
}
