package model;

import static model.TestUtil.criarSoft;
import static model.TestUtil.limparBase;
import static model.TestUtil.manterSoft;
import static model.TestUtil.prepararSessao;
import static model.TestUtil.tipoConfigDesignacao;
import models.SrAcao;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.UnitTest;

public class SrAcaoTest extends UnitTest {

	@BeforeClass
	public static void criarDadosBasicos() throws Exception{
		manterSoft();
		criarSoft();
		assertEquals(3, SrAcao.count());
		
		tipoConfigDesignacao();
		
		prepararSessao();
	}
	
	@AfterClass
	public static void limparDadosCriados() throws Exception{
		limparBase();
	}
	
	@Test
	public void testarNada(){
		
	}

}
