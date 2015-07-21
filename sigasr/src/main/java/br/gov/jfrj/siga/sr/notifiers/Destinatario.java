package br.gov.jfrj.siga.sr.notifiers;

import br.gov.jfrj.siga.dp.DpPessoa;

public class Destinatario {

	private DpPessoa dpPessoa;

	public Destinatario(DpPessoa dpPessoa) {
		super();
		this.dpPessoa = dpPessoa;
	}

	public boolean possuiEmailCadastrado() {
		return dpPessoa != null && dpPessoa.getEmailPessoa() != null;
	}

	public String getEnderecoEmail() {
		return dpPessoa.getEmailPessoa();
	}
}
