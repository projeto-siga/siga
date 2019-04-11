package br.gov.jfrj.siga.cp.util;

import org.junit.Test;

import static org.junit.Assert.*;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.util.MatriculaUtils;

public class MatriculaUtilsTest {
	
	@Test( expected = AplicacaoException.class )
	public void deveLancarExcecaoQuandoAMatriculaForNula() throws Exception {
		MatriculaUtils.validaPreenchimentoMatricula( null );
	}
	
	@Test( expected = AplicacaoException.class )
	public void deveLancarExcecaoQuandoAMatriculaEstiverVazia() throws Exception {
		MatriculaUtils.validaPreenchimentoMatricula( "" );
	}
	
	@Test( expected = AplicacaoException.class )
	public void deveLancarExcecaoCasoOTamanhoDaMatriculaSejaMenorOuIgualADois() throws Exception {
		MatriculaUtils.validaPreenchimentoMatricula( "RJ" );
	}
	
	@Test( expected = AplicacaoException.class )
	public void deveLancarExcecaoCasoAParteNumericaDaMatriculaNaoForNumerica() throws Exception {
		MatriculaUtils.getParteNumericaDaMatricula( "RJA" );
	}
	
	@Test
	public void deveRetornarParteNumericaDaMatricula() throws Exception {
		long parteNumericaEsperada = 13286;
		long parteNumericaAtual = MatriculaUtils.getParteNumericaDaMatricula( "RJ13286" );
		assertEquals( parteNumericaEsperada, parteNumericaAtual );
	}
	
	@Test( expected = AplicacaoException.class )
	public void deveLancarExcecaoCasoAParteNaoNumericaDaMatriculaSejaNumerica() throws Exception {
		MatriculaUtils.getSiglaDoOrgaoDaMatricula( "999" );
	}
	
	@Test
	public void deveRetornarSiglaDaMatricula() throws Exception {
		String siglaEsperada = "RJ";
		String siglaAtual = MatriculaUtils.getSiglaDoOrgaoDaMatricula( "RJ13286" );
		assertTrue( siglaEsperada.equals( siglaAtual ) );
	}

}
