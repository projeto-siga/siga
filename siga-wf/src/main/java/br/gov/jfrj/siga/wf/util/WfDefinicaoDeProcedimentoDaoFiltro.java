package br.gov.jfrj.siga.wf.util;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

public class WfDefinicaoDeProcedimentoDaoFiltro extends DaoFiltroSelecionavel {
	public CpOrgaoUsuario ouDefault = null;

	private String nome = null;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
