package br.gov.jfrj.siga.ex.util;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public interface EnviarEmail {

	public void enviarAoCossignatario(DpPessoa pessoaDest, DpPessoa titular, String sigla);
	public void enviarAoTramitarDocParaUsuariosDaUnidade(DpLotacao lotaDest, DpPessoa pessoa, String sigla);
	public void enviarAoTramitarDocMarcado(DpPessoa pessoaDest, DpPessoa titular, String sigla, String marcador);
	public void enviarAoResponsavelPelaAssinatura(DpPessoa pessoaDest, DpPessoa titular, String sigla);
	public void enviarAoTramitarDocParaUsuario(DpPessoa pessoaDest, DpPessoa titular, String sigla);
	
}
