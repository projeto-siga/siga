package br.gov.jfrj.siga.ex.util.notificador.especifico;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public interface ExMontarEmail {

	public String docTramitadoParaUnidade(DpPessoa destinatario, DpLotacao lotacao, String docSigla);
	public String incluirCossignatario(DpPessoa destinatario, DpPessoa cadastrante, String siglaDoc);
	public String responsavelPelaAssinatura(DpPessoa destinatario, DpPessoa cadastrante, String siglaDoc);
	public String docMarcadoTramitadoParaUsuario(DpPessoa destinatario, DpPessoa cadastrante, String docSigla, String marcador);
	public String docTramitadoParaUsuario(DpPessoa destinatario, DpPessoa cadastrante, String siglaDoc);
	
}
