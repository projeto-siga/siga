package br.gov.jfrj.siga.ex.util.BIE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public class ListaDeTopicos {

	private List<Topico> topicos;
	
	private boolean gerarNovostopicos;
	
	@XmlTransient
	private Comparator<Topico> ordenacao = new Comparator<Topico>(){
		public int compare(Topico o1, Topico o2) {
			return o1.getDescr().compareTo(o2.getDescr());
		}
	};
	
	public void addTopico(Topico topico) {
		if (topicos == null)
			topicos = new ArrayList<Topico>();
		topicos.add(topico);
		Collections.sort(topicos, ordenacao);
	}
	
	public boolean isVazio(){
		if (topicos != null)
			for (Topico t : topicos)
				if (!t.isVazio())
					return false;
		return true;
	}
	
	public boolean alocar(Materia m) {
		if (topicos != null){
			for (Topico t : topicos)
				if (t.alocar(m))
					return true;
			if (gerarNovostopicos){
				Topico novoSubtopico = new Topico();
				Topico topicoJaExistente = topicos.get(0);
				if (topicoJaExistente.getLocalidadeDasMaterias() != null)
					novoSubtopico.setLocalidadeDasMaterias(m.getLocalidade());
				if (topicoJaExistente.getTipoDasMaterias() != null)
					novoSubtopico.setTipoDasMaterias(m.getTipoMateria());
				novoSubtopico.addMateria(m);
				addTopico(novoSubtopico);
				return true;
			}
		}
		return false;
	}
	
	@XmlElement(name="topico")
	public List<Topico> getTopicos() {
		return topicos;
	}

	public void setTopicos(List<Topico> subtopicos) {
		this.topicos = subtopicos;
	}

	@XmlAttribute(name="autoIncrementarTopicos")
	public boolean isGerarNovostopicos() {
		return gerarNovostopicos;
	}

	public void setGerarNovostopicos(boolean gerarNovostopicos) {
		this.gerarNovostopicos = gerarNovostopicos;
	}
			
}
