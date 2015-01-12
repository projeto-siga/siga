package models.vo;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;

public class CpOrgaoUsuarioVO extends AbstractSelecionavel {
	
	public CpOrgaoUsuarioVO(Long id, String acronimoOrgaoUsu) {
		super(id, acronimoOrgaoUsu);
	}

	public static CpOrgaoUsuarioVO createFrom(CpOrgaoUsuario orgao) {
		if (orgao != null)
			return new CpOrgaoUsuarioVO(orgao.getId(), orgao.getAcronimoOrgaoUsu());
		else
			return null;
	}
}
