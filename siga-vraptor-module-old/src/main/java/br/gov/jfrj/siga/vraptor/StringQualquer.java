package br.gov.jfrj.siga.vraptor;

/* Como estamos alterando o converter de String para eliminar strings vazia, quando precisamos receber uma string independente do conteudo ser vazio, criamos essa classe para enganar o converter */
public class StringQualquer {
	private final String string;

	public StringQualquer(String string) {
		this.string = string;
	}

	@Override
	public String toString() {
		return string;
	}
}
