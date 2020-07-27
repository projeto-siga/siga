package br.gov.jfrj.siga.cp.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.idp.jwt.SigaJwtProviderException;

public class SigaUtil {

	public static SigaUtil getInstance() {
		return new SigaUtil();
	}

	/**
	 * Consulta a Identidade do usuário
	 * 
	 * @param matricula
	 *            String
	 * @param senha
	 *            String
	 * @return CpIdentidade
	 * 
	 */
	public CpIdentidade autenticar(String matricula, String senha) {
		CpIdentidade cpIdentidade = null;
		try {
			CpDao dao = CpDao.getInstance();
			DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
			flt.setSigla(matricula);
			cpIdentidade = dao.consultaIdentidadeCadastrante(matricula, true);
		} catch (AplicacaoException e) {
			e.printStackTrace();
		}
		return cpIdentidade;
	}

	/**
	 * Verificar se o usuário possui permissão de acesso ao WS
	 * 
	 * @param dpPessoa
	 *            DpPessoa
	 * @return Boolean
	 */

	public Boolean verificaSePessoTemPermissaoWS(DpPessoa dpPessoa) {

		CpConfiguracao t_cfgConfigExemplo = new CpConfiguracao();
		boolean retorno = false;

		t_cfgConfigExemplo.setDpPessoa(dpPessoa);

		CpServico p_cpsServico = new CpServico();
		p_cpsServico.setSiglaServico(CpServico.ACESSO_WEBSERVICE);
		t_cfgConfigExemplo.setCpServico(p_cpsServico);

		CpTipoConfiguracao cpT = new CpTipoConfiguracao();
		cpT.setIdTpConfiguracao(CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO);
		t_cfgConfigExemplo.setCpTipoConfiguracao(cpT);

		try {
			List<CpConfiguracao> ll = CpDao.getInstance().porLotacaoPessoaServicoTipo(t_cfgConfigExemplo);
			for (CpConfiguracao cpConfiguracao : ll) {
				if (cpConfiguracao.getCpServico().getSiglaServico().equals(CpServico.ACESSO_WEBSERVICE)) {
					retorno = true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return retorno;

	}
	
	/* Token JWT de Usuário Autenticado */
	private SigaJwt inicializarJwtBL(String modulo) throws IOException, ServletException {
		SigaJwt jwtBL = null;

		try {
			jwtBL = SigaJwt.getInstance(modulo);
		} catch (SigaJwtProviderException e) {
			throw new ServletException("Erro ao iniciar o provider", e);
		}

		return jwtBL;
	}

	/**
	 * Verifica se é um TOKEN valido JWT.
	 * 
	 * @param token
	 * @return String
	 * @throws TokenException
	 */
	public String validarToken(String token) throws TokenException {

		SigaJwt jwtBL;
		try {
			jwtBL = inicializarJwtBL(null);
			token = jwtBL.validar(token);

		} catch (IOException e) {
			throw new TokenException(TokenException.MSG_EX_TOKEN_INVALIDO);
		} catch (ServletException e) {
			throw new TokenException(TokenException.MSG_EX_TOKEN_INVALIDO);
		} catch (InvalidKeyException e) {
			throw new TokenException(TokenException.MSG_EX_TOKEN_INVALIDO);
		} catch (NoSuchAlgorithmException e) {
			throw new TokenException(TokenException.MSG_EX_TOKEN_INVALIDO);
		} catch (IllegalStateException e) {
			throw new TokenException(TokenException.MSG_EX_TOKEN_INVALIDO);
		} catch (SignatureException e) {
			throw new TokenException(TokenException.MSG_EX_TOKEN_INVALIDO);
		} catch (JWTVerifyException e) {
			throw new TokenException(TokenException.MSG_EX_TOKEN_INVALIDO);
		}

		return token;
	}
	
	
	public Map<String, Object> parseTokenJwt(String token) throws TokenException {

		SigaJwt jwtBL;
		try {
			jwtBL = inicializarJwtBL(null);
			Map<String, Object> map = jwtBL.validarToken(token);
			return map;

		} catch (IOException e) {
			throw new TokenException(TokenException.MSG_EX_TOKEN_INVALIDO);
		} catch (ServletException e) {
			throw new TokenException(TokenException.MSG_EX_TOKEN_INVALIDO);
		} catch (InvalidKeyException e) {
			throw new TokenException(TokenException.MSG_EX_TOKEN_INVALIDO);
		} catch (NoSuchAlgorithmException e) {
			throw new TokenException(TokenException.MSG_EX_TOKEN_INVALIDO);
		} catch (IllegalStateException e) {
			throw new TokenException(TokenException.MSG_EX_TOKEN_INVALIDO);
		} catch (SignatureException e) {
			throw new TokenException(TokenException.MSG_EX_TOKEN_INVALIDO);
		} catch (JWTVerifyException e) {
			throw new TokenException(TokenException.MSG_EX_TOKEN_INVALIDO);
		}
	}

	public String gerarToken(String matricula) throws IOException, ServletException {
		SigaJwt jwtBL = inicializarJwtBL(null);
		String token = jwtBL.criarToken(matricula, null, null, null);
		return token;
	}
	/***************/

	
	/* JWT de uso Externo - Sem autenticação no Sistema. Ex.: Acesso Arquivo,verificação autenticidade */
	/*TODO: Verificar se não dá para unificar as rotinas JWT */
	public static String buildJwtToken(final String tipoLink, final String token, final String sigla) {
		String jwt;

		final JWTSigner signer = new JWTSigner(getJwtPassword());
		final HashMap<String, Object> claims = new HashMap<String, Object>();

		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		final long exp = iat + 1 * 60 * 60L; // token expires in 1 hours
		claims.put("exp", exp);
		claims.put("iat", iat);

		claims.put("tipoLink", tipoLink);
		claims.put("token", token);
		claims.put("sigla", sigla);
		
		jwt = signer.sign(claims);

		return jwt;
	}
	
	
	public static Map<String, Object> verifyGetJwtToken(String token) {
		final JWTVerifier verifier = new JWTVerifier(getJwtPassword());
		try {
			Map<String, Object> map = verifier.verify(token);
			return map;
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao verificar token JWT", 0, e);
		}
	}
	

	private static String getJwtPassword() {
		String pwd = null;
		try {
			pwd = System.getProperty("siga.ex.autenticacao.pwd");
			if (pwd == null)
				throw new AplicacaoException(
						"Erro obtendo propriedade siga.ex.autenticacao.pwd");
			return pwd;
		} catch (Exception e) {
			throw new AplicacaoException(
					"Erro obtendo propriedade siga.ex.autenticacao.pwd", 0, e);
		}
	}
	
	/*************************************/
	
	/*
	 * Funcao para geracao de codigos alfanumericos randomicos
	 * recebendo apenas a quantidade de caracteres que o codigo deve conter
	 */
	public static String randomAlfanumerico(int contador) {
		final String STRING_ALFANUMERICA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb = new StringBuilder();
		while (contador-- != 0) {	
			int caracteres = (int)(Math.random()*STRING_ALFANUMERICA.length());	
			sb.append(STRING_ALFANUMERICA.charAt(caracteres));	
		}	
		return sb.toString();
	}

}
