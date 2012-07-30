package br.gov.jfrj.siga.cd;

import java.security.MessageDigest;
import java.util.Date;

public class CdAssinaturaDigital implements ICdAssinaturaDigital {
	public byte[] validarECompletarAssinatura(byte[] assinatura,
			byte[] documento, String sArquivoPolitica, Date dtAssinatura)
			throws Exception {
		validarAssinatura(assinatura, documento, dtAssinatura,
				sArquivoPolitica != null);
		return assinatura;
	}

	public String validarAssinatura(byte[] assinatura, byte[] documento,
			Date dtAssinatura, boolean verificarLCRs) throws Exception {
		return AssinaturaDigital.validarAssinaturaPKCS7(MessageDigest
				.getInstance("SHA1").digest(documento), "1.3.14.3.2.26",
				assinatura, dtAssinatura, verificarLCRs);
	}

	public String recuperarCPF(byte[] cms) throws Exception {
		return AssinaturaDigital.recuperarCPF(cms);
	}

}
