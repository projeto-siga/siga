package br.gov.jfrj.siga.ex.util.BIE;

import java.util.Arrays;
import java.util.List;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ConversorSimples extends ConversorDeExDocParaMateria{

	private List<String> especies;
	
	public ConversorSimples(String... especies){
		this.especies = Arrays.asList(especies);
	}
	
	@Override
	public boolean canHandle(ExDocumento doc) {
		for (String especie : especies)
			if (especie.equals(doc.getExFormaDocumento().getSiglaFormaDoc()))
				return true;
		return false;
	}

	@Override
	public List<Materia> converter(ExDocumento doc) {
		ManipuladorHTML manipuladorHTML = new ManipuladorHTML(doc);
		
		Materia m = new Materia();
		m.setTipoMateria(getTipoMateria());
		m.setLocalidade(Localidade.porDescricao(doc.getLocalidadeString()));
		m.setNumero(manipuladorHTML.getNumero() + " de " + doc.getDtD() + " de " + doc.getDtMMMM() + " de " + doc.getDtYYYY());
		m.setConteudo(manipuladorHTML.getCorpoEnxugado());
		m.setFecho(manipuladorHTML.getFechoEnxugadoComAssinatura());
		m.setDt(doc.getDtFinalizacao());
		return Arrays.asList(m);
	}

}
