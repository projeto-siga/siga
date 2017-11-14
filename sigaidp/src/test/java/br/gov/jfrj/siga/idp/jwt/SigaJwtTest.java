package br.gov.jfrj.siga.idp.jwt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.auth0.jwt.interfaces.DecodedJWT;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SigaJwtTest {

	private static final String PROVIDER_PASSWORD = "12345";
	private static final long TTL_TOKEN = 1000;
	private static final String MATRICULA = "ZZ99999";
	
	private static String tokenCriado;
	private static DecodedJWT tokenDecodificado;
	private static SigaJwtOptions options = new SigaJwtOptionsBuilder()
		.setTTL(TTL_TOKEN)
		.setPassword(PROVIDER_PASSWORD)
		.build();
		

	@Test
	public void t001_obterProvider() throws SigaJwtProviderException {
		assertNotNull(SigaJwtProvider.getInstance(options));
	}
	
	@Test
	public void t002_obterNovoToken() throws SigaJwtProviderException{
		tokenCriado = SigaJwtProvider.getInstance(options).criarToken(MATRICULA);
		assertNotNull(tokenCriado);
	}
	
	@Test
	public void t003_validarToken() throws SigaJwtProviderException{
		tokenDecodificado = SigaJwtProvider.getInstance(options).validarToken(tokenCriado);
		assertNotNull(tokenDecodificado);
	}
	
	@Test
	public void t004_deveTerEmissorDefinido(){
		assertEquals(SigaJwtProvider.PROVIDER_ISSUER, tokenDecodificado.getIssuer());
	}
	
	@Test
	public void t004_deveTerSubjectDefinido(){
		assertEquals(MATRICULA, tokenDecodificado.getSubject());
	}
	
	
	@Test
	public void t005_deveTerDataDeEmissaoDefinida(){
		assertNotNull(tokenDecodificado.getIssuedAt());
	}
	
	@Test
	public void t006_deveValerApenasAposEmissao(){
		assertEquals(tokenDecodificado.getIssuedAt(),tokenDecodificado.getNotBefore());
	}
	
	@Test
	public void t006_deveTerExpiracaoDefinida(){
		assertNotNull(tokenDecodificado.getExpiresAt());
	}

	@Test(expected=com.auth0.jwt.exceptions.TokenExpiredException.class)
	public void t999_invalidoAposExpiracao() throws InterruptedException, SigaJwtProviderException{
		Thread.sleep(TTL_TOKEN + 1000); //1 segundo depois
		SigaJwtProvider.getInstance(options).validarToken(tokenCriado);
	}



	

}
