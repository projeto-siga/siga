package br.gov.jfrj.siga.ex.bl;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExAssinavelDoc {
	private ExDocumento doc;
	private boolean podeAssinar;
	private boolean podeSenha;
	private List<ExAssinavelMov> movs = new ArrayList<>();

	public ExDocumento getDoc() {
		return doc;
	}

	public List<ExAssinavelMov> getMovs() {
		return movs;
	}

	public boolean isPodeAssinar() {
		return podeAssinar;
	}

	public boolean isPodeSenha() {
		return podeSenha;
	}

	public void setDoc(ExDocumento doc) {
		this.doc = doc;
	}

	public void setMovs(List<ExAssinavelMov> movs) {
		this.movs = movs;
	}

	public void setPodeAssinar(boolean podeAssinar) {
		this.podeAssinar = podeAssinar;
	}

	public void setPodeSenha(boolean podeSenha) {
		this.podeSenha = podeSenha;
	}
}
