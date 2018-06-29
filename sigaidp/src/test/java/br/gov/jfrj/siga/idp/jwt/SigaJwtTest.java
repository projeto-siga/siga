package br.gov.jfrj.siga.idp.jwt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.auth0.jwt.JWTVerifyException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SigaJwtTest {

	private static final String PROVIDER_PASSWORD = "12345";
	private static final long TTL_TOKEN = 60; // 1min
	private static final String MATRICULA = "ZZ99999";

	private static String tokenCriado;
	private static Map<String, Object> tokenDecodificado;
	private static SigaJwtOptions options = new SigaJwtOptionsBuilder()
			.setTTL(TTL_TOKEN).setPassword(PROVIDER_PASSWORD)
			.setModulo("teste").build();

	@Test
	public void t001_obterProvider() throws SigaJwtProviderException {
		assertNotNull(SigaJwtProvider.getInstance(options));
	}

	@Test
	public void t002_obterNovoToken() throws SigaJwtProviderException {
		tokenCriado = SigaJwtProvider.getInstance(options).criarToken(
				MATRICULA, null, null, null);
		assertNotNull(tokenCriado);
	}

	@Test
	public void t003_validarToken() throws SigaJwtProviderException,
			InvalidKeyException, NoSuchAlgorithmException,
			IllegalStateException, SignatureException, IOException,
			JWTVerifyException {
		tokenDecodificado = SigaJwtProvider.getInstance(options).validarToken(
				tokenCriado);
		assertNotNull(tokenDecodificado);
	}

	@Test
	public void t004_deveTerEmissorDefinido() {
		assertEquals(SigaJwtProvider.PROVIDER_ISSUER,
				tokenDecodificado.get("iss"));
	}

	@Test
	public void t004_deveTerSubjectDefinido() {
		assertEquals(MATRICULA, tokenDecodificado.get("sub"));
	}

	@Test
	public void t005_deveTerDataDeEmissaoDefinida() {
		assertNotNull(tokenDecodificado.get("iat"));
	}

	@Test
	public void t006_deveValerApenasAposEmissao() {
		assertEquals(tokenDecodificado.get("iat"), tokenDecodificado.get("nbf"));
	}

	@Test
	public void t006_deveTerExpiracaoDefinida() {
		assertNotNull(tokenDecodificado.get("exp"));
	}

	@Test(expected = JWTVerifyException.class)
	public void t999_invalidoAposExpiracao() throws InterruptedException,
			SigaJwtProviderException, InvalidKeyException,
			NoSuchAlgorithmException, IllegalStateException,
			SignatureException, IOException, JWTVerifyException {
		tokenCriado = SigaJwtProvider.getInstance(options).criarToken(
				MATRICULA, null, null, 0);
		SigaJwtProvider.getInstance(options).validarToken(tokenCriado);
	}

}
