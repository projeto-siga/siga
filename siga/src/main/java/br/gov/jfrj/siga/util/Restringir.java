package br.gov.jfrj.siga.util;

import java.util.List;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public interface Restringir {

	public List<CpOrgaoUsuario> orgao(List<CpOrgaoUsuario> orgaos, DpPessoa pessoa, DpLotacao lotacao);
	
}
