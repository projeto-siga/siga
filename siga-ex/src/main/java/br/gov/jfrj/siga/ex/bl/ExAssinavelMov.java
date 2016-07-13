package br.gov.jfrj.siga.ex.bl;

import br.gov.jfrj.siga.ex.ExMovimentacao;

public class ExAssinavelMov implements Comparable<ExAssinavelMov> {
	private ExMovimentacao mov;
	private boolean podeAutenticar;
	private boolean podeSenha;
	
	public ExMovimentacao getMov() {
		return mov;
	}
	public boolean isPodeAutenticar() {
		return podeAutenticar;
	}
	public boolean isPodeSenha() {
		return podeSenha;
	}
	public void setMov(ExMovimentacao mov) {
		this.mov = mov;
	}
	public void setPodeAutenticar(boolean podeAutenticar) {
		this.podeAutenticar = podeAutenticar;
	}
	public void setPodeSenha(boolean podeSenha) {
		this.podeSenha = podeSenha;
	}
	@Override
	public int compareTo(ExAssinavelMov o) {
		return this.mov.getIdMov().compareTo(o.mov.getIdMov());
	}
}
