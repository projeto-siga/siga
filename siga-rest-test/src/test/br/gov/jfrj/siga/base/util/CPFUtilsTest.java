package br.gov.jfrj.siga.base.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.gov.jfrj.siga.base.AplicacaoException;

public class CPFUtilsTest {
	
	@Test( expected = AplicacaoException.class )
	public void deveLancarExcecaoCasoCPFSejaNulo() throws Exception {
		String cpf = null;
		CPFUtils.efetuaValidacaoSimples( cpf );
	}
	
	@Test( expected = AplicacaoException.class )
	public void deveLancarExcecaoCasoCPFSejaVazio() throws Exception {
		String cpf = "";
		CPFUtils.efetuaValidacaoSimples( cpf );
	}
	
	@Test( expected = AplicacaoException.class )
	public void deveLancarExcecaoCasoCPFSejaEmBranco() throws Exception {
		String cpf = "   ";
		CPFUtils.efetuaValidacaoSimples( cpf );
	}
	
	@Test( expected = AplicacaoException.class )
	public void deveLancarExcecaoCasoCPFNaoPossuaOnzeDigitosNumericos() throws Exception {
		String cpf = "111.111.A11-11";
		CPFUtils.efetuaValidacaoSimples( cpf );
	}
	
	@Test( expected = AplicacaoException.class )
	public void deveLancarExcecaoCasoCPFNaoPossuaOnzeDigitos() throws Exception {
		String cpf = "111111111111";
		CPFUtils.efetuaValidacaoSimples( cpf );
	}
	
	@Test
	public void deveRetornarTrueCasoCPFPossuaOnzeDigitos() throws Exception {
		String cpf = "11111111111";
		CPFUtils.efetuaValidacaoSimples( cpf );
	}
	
	@Test
	public void deveRecuperarLongValueDoCPFInformado() throws Exception {
		String cpf = "111.111.111-11";
		Long cpfEsperado = 11111111111L;
		Long cpfAtual = CPFUtils.getLongValueValidaSimples( cpf );
		assertTrue( cpfEsperado.longValue() == cpfAtual.longValue() );
	}
	

}
