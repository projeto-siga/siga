package br.gov.jfrj.siga.cd;

import java.security.MessageDigest;
import java.util.Date;

public class CdAssinaturaDigital implements ICdAssinaturaDigital {
	public byte[] validarECompletarAssinatura(byte[] assinatura,
			byte[] documento, boolean politica, Date dtAssinatura)
			throws Exception {
		validarAssinatura(assinatura, documento, dtAssinatura, false);
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

	public byte[] produzPacoteAssinavel(byte[] certificado,
			byte[] certificadoHash, byte[] documento, boolean politica,
			Date dtAssinatura) throws Exception {
		return documento;
	}

	public byte[] validarECompletarPacoteAssinavel(byte[] certificado,
			byte[] documento, byte[] assinatura, boolean politica,
			Date dtAssinatura) throws Exception {
		return validarECompletarAssinatura(assinatura, documento, politica,
				dtAssinatura);
	}

}
