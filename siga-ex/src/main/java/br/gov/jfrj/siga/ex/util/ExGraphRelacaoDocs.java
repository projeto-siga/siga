package br.gov.jfrj.siga.ex.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExGraphRelacaoDocs extends ExGraph {

	private class NodoMob extends Nodo {

		private ExMobil mob;

		public ExMobil getMob() {
			return this.mob;
		}

		public NodoMob(ExMobil mob, DpPessoa pessoaVisualizando) {
			this(mob, pessoaVisualizando, null);
		}

		public NodoMob(ExMobil mob, DpPessoa pessoaVisualizando,
				ExDocumento docRef) {
			super(mob.getSigla());
			this.mob = mob;
			setShape("rectangle");
			setLabel(mob.getSiglaResumida(pessoaVisualizando.getOrgaoUsuario(),
					docRef));
			setTooltip(mob.getSigla());
			setURL("exibir?sigla=" + mob.getSigla() + "");
		}
	}

	private class TransicaoMob extends Transicao {

		private ExMobil mob1;
		private ExMobil mob2;
		private String tipo;

		public ExMobil getMob1() {
			return this.mob1;
		}

		public ExMobil getMob2() {
			return this.mob2;
		}

		public TransicaoMob(ExMobil mob1, ExMobil mob2, String tipo) {
			super(mob1.getSigla(), mob2.getSigla());
			this.mob1 = mob1;
			this.mob2 = mob2;
			this.tipo = tipo;
			if (tipo.equals("vinculacao")) {
				setTooltip("Vincula&ccedil;&atilde;o");
				setEstilo(ESTILO_TRACEJADO);
				setCor("gray");
				setDirected(false);
			} else if (tipo.equals("juntada")) {
				setTooltip("Juntada");
				setEstilo(ESTILO_TRACEJADO);
				setDirected(true).setAoContrario(true);
			} else if (tipo.equals("apensacao")) {
				setTooltip("Apensa&ccedil;&atilde;o");
				setEstilo(ESTILO_TRACEJADO);
				setDirected(true).setAoContrario(true);
			} else if (tipo.equals("paternidade")) {
				setTooltip(mob2.doc().isProcesso() ? "Subprocesso"
						: "Documento filho");
				setDirected(false);
			}
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
		for (ExMobil apenso : mobBase.getApensos(false, true)) {
			adicionar(new NodoMob(apenso, pessVendo, mobBase.doc()));
			adicionar(new TransicaoMob(mobBase, apenso, "apensacao"));
		}
		ExMobil mestre = mobBase.getMestre();
		if (mestre != null && !mestre.doc().equals(mobBase)) {
			adicionar(new NodoMob(mestre, pessVendo, mobBase.doc()));
			adicionar(new TransicaoMob(mestre, mobBase, "apensacao"));
		}

		// Vinculações
		for (ExMobil vinculado : mobBase.getVinculados()) {
			adicionar(new NodoMob(vinculado, pessVendo, mobBase.doc()));
			adicionar(new TransicaoMob(mobBase, vinculado, "vinculacao"));
		}

		// Juntadas
		ExMobil pai = mobBase.getExMobilPai();
		if (pai != null) {
			adicionar(new NodoMob(pai, pessVendo, mobBase.doc()));
			adicionar(new TransicaoMob(pai, mobBase, "juntada"));
		}

		// Incluir os documentos filhos juntados (ou não) enquanto o principal não estiver assinado
		if (mobBase.doc().isPendenteDeAssinatura()) {
			for (ExMobil m : mobBase.getJuntados()) {
				adicionar(new NodoMob(m, pessVendo, mobBase.doc()));
				adicionar(new TransicaoMob(mobBase, m, "juntada"));
			}
		}

		// Paternidades
		for (ExDocumento sub : mobBase.doc().getMobilGeral()
				.getExDocumentoFilhoSet()) {
			boolean jaTemNodo = false;
			for (Nodo n : getNodos())
				if (((NodoMob) n).getMob().doc().equals(sub)) {
					jaTemNodo = true;
					break;
				}
			if (!jaTemNodo) {
				adicionar(new NodoMob(sub.getMobilGeral(), pessVendo,
						mobBase.doc()));
				adicionar(new TransicaoMob(mobBase, sub.getMobilGeral(),
						"paternidade"));
			}
		}

		if (mobBase.doc().getExMobilPai() != null) {
			boolean jaTemNodo = false;
			for (Nodo n : getNodos())
				if (((NodoMob) n).getMob().doc()
						.equals(mobBase.doc().getExMobilPai().doc())) {
					jaTemNodo = true;
					break;
				}
			if (!jaTemNodo) {
				adicionar(new NodoMob(mobBase.doc().getExMobilPai(), pessVendo,
						mobBase.doc()));
				adicionar(new TransicaoMob(mobBase.doc().getExMobilPai(),
						mobBase, "paternidade"));
			}
		}
	}

	public Map<String, List<ExMobil>> getAsMap() {
		Map<String, List<ExMobil>> mapa = new HashMap<String, List<ExMobil>>();
		String cat = "";
		ExMobil mobilAAdicionar = null;
		for (Transicao t : getTransicoes()) {
			TransicaoMob tMob = (TransicaoMob) t;
			if (tMob.tipo.equals("vinculacao")) {
				cat = "Veja também";
				mobilAAdicionar = tMob.mob2;
			} else if (tMob.tipo.equals("juntada")) {
				cat = "Juntado ao documento";
				mobilAAdicionar = tMob.mob1;
			} else if (tMob.tipo.equals("apensacao"))
				if (tMob.mob1.equals(mobBase)) {
					cat = "Documentos Apensados";
					mobilAAdicionar = tMob.mob2;
				} else {
					cat = "Apensado ao Documento";
					mobilAAdicionar = tMob.mob1;
				}
			else if (tMob.tipo.equals("paternidade"))
				if (tMob.mob1.equals(mobBase)) {
					cat = "Subprocessos";
					mobilAAdicionar = tMob.mob2;
				} else {
					cat = "Documento pai";
					mobilAAdicionar = tMob.mob1;
				}

			if (mapa.get(cat) == null) {
				mapa.put(cat, new ArrayList<ExMobil>());
			}
			mapa.get(cat).add(mobilAAdicionar);
		}
		return mapa;
	}

}
