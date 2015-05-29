package br.gov.jfrj.siga.base.util;

import org.apache.commons.lang.StringUtils;

import br.gov.jfrj.siga.base.AplicacaoException;

public class CPFUtils {

	/**
	 * Valida se o CPF é nulo ou em Branco e se os caracteres do CPF são numéricos com exceção do "." e do "-". 
	 * @param cpf
	 * @throws AplicacaoException
	 */
	public static void efetuaValidacaoSimples(final String cpf) throws AplicacaoException {
		validaCpfNuloOuBranco( cpf ); 
		validaQtdCaracteresNumericos( cpf );
	}

	private static boolean validaQtdCaracteresNumericos(final String cpf) throws AplicacaoException {
		
		boolean result = false;
		
		if ( cpf.length() == 11 ) {
			result = validaDigitosNumericos( cpf );
		} else if ( cpf.length() == 14 ) {
			String cpfLimpo = limpaCpf( cpf );
			result = validaDigitosNumericos( cpfLimpo );
		}
		
		if ( !result ) {
			throw new AplicacaoException( "A quantidade de digitos do CPF informado é inválida" );
		}
		
		return result;
	}

	private static void validaCpfNuloOuBranco(final String cpf)
			throws AplicacaoException {
		if ( StringUtils.isBlank( cpf ) ) {
			throw new AplicacaoException( "O CPF informado é nulo ou inválido" );
		}
	}
	
	private static boolean validaDigitosNumericos(String cpfLimpo) throws AplicacaoException {
		if ( !StringUtils.isNumeric( cpfLimpo ) ) {
			throw new AplicacaoException( "O CPF informado possui caracteres inválidos." );
		}
		return true;
	}

	private static String limpaCpf(final String cpf) {
		String cpfLimpo = cpf.trim();
		cpfLimpo = cpfLimpo.replaceAll( "\\.", "" );
		cpfLimpo = cpfLimpo.replaceAll( "\\-", "" );
		return cpfLimpo;
	}

	/**
	 * @param cpf
	 * @return o valor Long do CPF utilizando o método {@link #efetuaValidacaoSimples(String) efetuaValidacaoSimples} <br/>
	 * para validar o CPF antes de efetuar a conversão.
	 * @throws AplicacaoException
	 */
	public static Long getLongValueValidaSimples(String cpf) throws AplicacaoException {
		validaCpfNuloOuBranco( cpf );
		validaQtdCaracteresNumericos( cpf );
		String cpfLimpo = limpaCpf( cpf );
		return Long.parseLong( cpfLimpo );
	}

}
