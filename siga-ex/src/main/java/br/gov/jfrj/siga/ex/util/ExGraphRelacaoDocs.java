package br.gov.jfrj.siga.ex.util;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExGraphRelacaoDocs extends ExGraph {

	private class NodoMob extends Nodo {

		private ExMobil mob;

		public ExMobil getMob() {
			return this.mob;
		}
		
		public NodoMob(ExMobil mob, DpPessoa pessoaVisualizando){
			this(mob, pessoaVisualizando, null);
		}

		public NodoMob(ExMobil mob, DpPessoa pessoaVisualizando, ExDocumento docRef) {
			super(mob.getSigla());
			this.mob = mob;
			setShape("rectangle");
			setLabel(mob.getSiglaResumida(pessoaVisualizando.getOrgaoUsuario(), docRef));
			setTooltip(mob.getSigla());
			setURL("exibirNovo.action?sigla=" + mob.getSigla() + "");
		}
	}

	private ExMobil mobBase;

	public List<NodoMob> getNodosExcetoMobBase() {
		List<NodoMob> listaFinal = new ArrayList<NodoMob>();
		for (Nodo nodo : getNodos()) {
			NodoMob nodoDoc = (NodoMob) nodo;
			if (!nodoDoc.getMob().equals(mobBase))
				listaFinal.add(nodoDoc);
		}
		return listaFinal;
	}

	public ExGraphRelacaoDocs(ExMobil mobBase, DpPessoa pessVendo) {

		this.mobBase = mobBase;

		adicionar(new NodoMob(mobBase, pessVendo).setDestacar(true));

		// Apensações
		for (ExMobil apenso : mobBase.getApensos(true, false)) {
			adicionar(new NodoMob(apenso, pessVendo, mobBase.doc()));
			adicionar(apensacao(mobBase, apenso));
		}
		ExMobil mestre = mobBase.getMestre();
		if (mestre != null && !mestre.doc().equals(mobBase)) {
			adicionar(new NodoMob(mestre, pessVendo, mobBase.doc()));
			adicionar(apensacao(mestre, mobBase));
		}

		// Vinculações
		for (ExMobil vinculado : mobBase.getVinculados()) {
			adicionar(new NodoMob(vinculado, pessVendo, mobBase.doc()));
			adicionar(vinculacao(mobBase, vinculado));
		}

		// Juntada
		ExMobil pai = mobBase.getExMobilPai();
		if (pai != null) {
			adicionar(new NodoMob(pai, pessVendo, mobBase.doc()));
			adicionar(juntada(pai, mobBase));
		}

		// Paternidades
		for (ExDocumento sub : mobBase.doc().getMobilGeral()
				.getExDocumentoFilhoSet()) {
			boolean jaTemNodo = false;
			for (Nodo n : getNodos())
				if (((NodoMob) n).getMob().doc().equals(sub)){
					jaTemNodo = true;
					break;
				}
			if (!jaTemNodo){
				adicionar(new NodoMob(sub.getMobilGeral(), pessVendo, mobBase.doc()));
				adicionar(paternidade(mobBase, sub.getMobilGeral()));
			}
		}
		if (mobBase.doc().getExMobilPai() != null) {
			boolean jaTemNodo = false;
			for (Nodo n : getNodos())
				if (((NodoMob) n).getMob().doc().equals(mobBase.doc().getExMobilPai().doc())){
					jaTemNodo = true;
					break;
				}
			if (!jaTemNodo){
				adicionar(new NodoMob(mobBase.doc().getExMobilPai(), pessVendo, mobBase.doc()));
				adicionar(paternidade(mobBase.doc().getExMobilPai(), mobBase));
			}
		}

		// Ver se algum dos nodos é filho de outro e conectar um ao outro como
		// paternidade, pra facilitar visualização

		/*for (NodoDoc nodoDocX : getNodosExcetoDocBase()) {
			for (NodoDoc nodoDocY : getNodosExcetoDocBase()) {
				if (nodoDocX.getDoc().getExMobilPai() != null
						&& nodoDocX.getDoc().getExMobilPai().getDoc()
								.equals(nodoDocY.getDoc())) {
					adicionar(paternidade(nodoDocY.getDoc(), nodoDocX.getDoc()));
					nodoDocX.setLabel(nodoDocX.getDoc().getPontoNumSequencia());
				}
			}
		}*/

	}

	private Transicao vinculacao(ExMobil mob1, ExMobil mob2) {
		Transicao t = new Transicao(mob1.getSigla(), mob2.getSigla());
		t.setTooltip("Vinculação: " + mob1.getSigla() + " -> "
				+ mob2.getSigla());
		t.setEstilo(ESTILO_TRACEJADO);
		t.setCor("gray");
		t.setDirected(false);
		return t;
	}

	private Transicao juntada(ExMobil pai, ExMobil filho) {
		Transicao t = new Transicao(pai.getSigla(), filho.getSigla());
		t.setTooltip("Juntada");
		t.setEstilo(ESTILO_TRACEJADO);
		t.setDirected(true).setAoContrario(true);
		return t;
	}

	private Transicao apensacao(ExMobil mestre, ExMobil escravo) {
		Transicao t = new Transicao(mestre.getSigla(), escravo.getSigla());
		t.setTooltip("Apensação");
		t.setEstilo(ESTILO_TRACEJADO);
		t.setDirected(true).setAoContrario(true);
		return t;
	}

	private Transicao paternidade(ExMobil pai, ExMobil filho) {
		Transicao t = new Transicao(pai.getSigla(), filho.getSigla());
		t.setTooltip(filho.doc().isProcesso() ? "Subprocesso" : "Documento filho");
		t.setDirected(false);
		return t;
	}

}
