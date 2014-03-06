package model;

import static model.TestUtil.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.UnitTest;

public class SrAcaoTest extends UnitTest {

	@BeforeClass
	public static void antesDeTudo() throws Exception{
		criarDadosBasicos();
		prepararSessao();
	}
	
	@AfterClass
	public static void depoisDeTudo() throws Exception{
		limparBase();
	}
	
	@Test
	public void testarNada(){
		
	}

}
