package br.rj.jfrj.siga.dp;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import junit.framework.TestCase;

public class DpLotacaoTest extends TestCase {
	
	private DpLotacao dpLotacao;
	private CpOrgaoUsuario orgaoUsuario;
	
	@Override
	protected void setUp() throws Exception {
		this.dpLotacao = new DpLotacao();
		this.orgaoUsuario = new CpOrgaoUsuario();
	}
	
	public void testDeveRetornarNuloCasoOOrgaoUsuarioSejaNulo() throws Exception {
		assertNull( this.dpLotacao.getIdOrgaoUsuario() );
	}
	
	public void testDeveRetornarIdDoOrgaoDoUsuario() throws Exception {
		Long idOrgaoUsu = 1L;
		this.orgaoUsuario.setIdOrgaoUsu( idOrgaoUsu );
		this.dpLotacao.setOrgaoUsuario( this.orgaoUsuario );
		
		assertEquals( idOrgaoUsu,  this.dpLotacao.getIdOrgaoUsuario() );
	}

}
