package br.gov.jfrj.siga.cd;

import java.util.Date;

public interface ICdAssinaturaDigital {
	byte[] validarECompletarAssinatura(byte[] assinatura, byte[] documento,
			String sArquivoPolitica, Date dtAssinatura) throws Exception;

	String validarAssinatura(byte[] assinatura, byte[] documento,
			Date dtAssinatura, boolean verificarLCRs) throws Exception;

	String recuperarCPF(byte[] cms) throws Exception;
}
