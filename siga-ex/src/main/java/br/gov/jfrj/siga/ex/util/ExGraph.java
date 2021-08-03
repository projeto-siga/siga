package br.gov.jfrj.siga.ex.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExGraph {

	public static String ESTILO_PONTILHADO = "dotted";
	public static String ESTILO_TRACEJADO = "dashed";
	public static String ESTILO_NEGRITO = "bold";

	protected class Nodo {

		private String nome, shape, estilo, label, URL, tooltip, color;

		public String getNome() {
			return nome;
		}

		private boolean destacar;

		protected Nodo(String nome) {
			this.nome = nome;
		}

		protected Nodo setShape(String shape) {
			this.shape = shape;
			return this;
		}

		protected Nodo setLabel(String label) {
			this.label = label;
			return this;
		}

		protected Nodo setDestacar(boolean destacar) {
			this.destacar = destacar;
			return this;
		}

		protected Nodo setEstilo(String estilo) {
			this.estilo = estilo;
			return this;
		}

		protected Nodo setCor(String cor) {
			this.color = cor;
			return this;
		}

		protected Nodo setURL(String URL) {
			this.URL = URL;
			return this;
		}

		protected Nodo setTooltip(String tooltip) {
			this.tooltip = tooltip;
			return this;
		}
		
		protected String getEstilo() {
			return this.estilo;
		}

		@Override
		public String toString() {
			String toString = "\"" + nome + "\"";
			if (shape != null)
				toString += "[shape=\"" + shape + "\", fillcolor=\"white\"]";
			if (label != null)
				toString += "[label=\"" + label + "\"]";
			if (destacar)
				toString += "[color=\"red\", fontcolor=\"red\"]";
			if (estilo != null)
				toString += "[style=\"filled," + estilo + "\"]";
			if (color != null)
				toString += "[color=\"" + color + "\"]";
			if (URL != null)
				toString += "[URL=\"" + URL + "\" penwidth=2]";
			if (tooltip != null)
				toString += "[tooltip=\"" + tooltip + "\"]";
			return toString;
		}

	}

	protected class Transicao {

		private String nodo1, nodo2, estilo, tooltip, label, color;
		private boolean directed, aoContrario;
		private boolean constraint = true;

		protected Transicao(String nodo1, String nodo2) {
			this.nodo1 = nodo1;
			this.nodo2 = nodo2;
		}

		protected Transicao setDirected(boolean directed) {
			this.directed = directed;
			return this;
		}

		protected Transicao setEstilo(String estilo) {
			this.estilo = estilo;
			return this;
		}

		protected Transicao setCor(String cor) {
			this.color = cor;
			return this;
		}

		protected Transicao setAoContrario(boolean aoContrario) {
			this.aoContrario = aoContrario;
			return this;
		}

		protected Transicao setTooltip(String tooltip) {
			this.tooltip = tooltip;
			return this;
		}

		protected Transicao setLabel(String label) {
			this.label = label;
			return this;
		}

		@Override
		public String toString() {
			String toString = "\"" + nodo1 + "\" -> \"" + nodo2 + "\"";
			if (estilo != null)
				toString += "[style=\"" + estilo + "\"]";
			if (tooltip != null)
				toString += "[tooltip=\"" + tooltip + "\"]";
			if (color != null)
				toString += "[color=\"" + color + "\"]";
			if (!directed)
				toString += "[dir=none]";
			else if (aoContrario)
				toString += "[dir=back]";
			if (!constraint)
				toString += "[constraint=false]";
			if (label != null)
				toString += "[label=\"" + label + "\"]";
			return toString;
		}

		public boolean isConstraint() {
			return constraint;
		}

		public Transicao setConstraint(boolean constraint) {
			this.constraint = constraint;
			return this;
		}
	}

	public ExGraph() {
	}

	private List<Nodo> nodos = new ArrayList<Nodo>();
	private List<Transicao> trans = new ArrayList<Transicao>();

	@Override
	public String toString() {
		String toString = "";
		for (Nodo nodo : nodos)
			toString += "\n" + nodo + ";";
		for (Transicao t : trans)
			toString += "\n" + t + ";";
		return toString.replace("\n", " ").replace("\r", " ");
	}

	public void adicionar(Nodo nodoAIncluir) {
		Nodo nodoAExcluir = localizarNodoPorNome(nodoAIncluir.nome);
		if (nodoAExcluir != null)
			nodos.remove(nodoAExcluir);
		nodos.add(nodoAIncluir);
	}

	public Nodo localizarNodoPorNome(String nome) {
		Nodo nodoAExcluir = null;
		for (Nodo n : nodos)
			if (n.nome.equals(nome)) {
				nodoAExcluir = n;
				break;
			}
		return nodoAExcluir;
	}

	public void adicionar(Transicao transicao) {
		trans.add(transicao);
	}

	public List<Nodo> getNodos() {
		return this.nodos;
	}

	public List<Transicao> getTransicoes() {
		return this.trans;
	}

	public int getNumNodos() {
		return getNodos().size();
	}

	public boolean misturaDirectedUndirected() {
		if (trans.size() < 1)
			return false;
		boolean dirDefault = trans.get(0).directed;
		for (Transicao t : trans)
			if (t.directed == !dirDefault)
				return true;
		return false;

	}

}
