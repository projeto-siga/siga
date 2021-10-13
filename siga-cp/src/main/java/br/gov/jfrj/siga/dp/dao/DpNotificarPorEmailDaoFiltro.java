package br.gov.jfrj.siga.dp.dao;

import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

public class DpNotificarPorEmailDaoFiltro extends DaoFiltroSelecionavel{

	private String nomeDaAcao;
	private int naoConfiguravel;
	private int configuravel;
	
	public DpNotificarPorEmailDaoFiltro() { }

	public String getNomeDaAcao() {
		return nomeDaAcao;
	}

	public void setNomeDaAcao(String nome) {
		this.nomeDaAcao = nome;
	}

	public int getNaoConfiguravel() {
		return naoConfiguravel;
	}

	public void setNaoConfiguravel(int naoConfiguravel) {
		this.naoConfiguravel = naoConfiguravel;
	}

	public int getConfiguravel() {
		return configuravel;
	}

	public void setConfiguravel(int configuravel) {
		this.configuravel = configuravel;
	}
	
}
