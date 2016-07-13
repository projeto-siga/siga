package br.gov.jfrj.siga.ex.util.BIE;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ConversorMultiplo extends ConversorDeExDocParaMateria{

	public ConversorMultiplo(){
		
	}
	
	@Override
	public boolean canHandle(ExDocumento doc) {
		return doc.getExModelo().getNmMod().equals("Publicação BI: Matéria Livre");
	}

	@Override
	public List<Materia> converter(ExDocumento doc) {
		List<Materia> materias = new ArrayList<Materia>();
		for (String chave : doc.getForm().keySet()){
			final Matcher matcher = Pattern.compile("^tipoMateria(\\d)$").matcher(chave);
			if (!matcher.find())
				continue;
			int i = Integer.valueOf(matcher.group(0));
			
			Materia m = new Materia();
			m.setTipoMateria(getTipoMateria());
			m.setLocalidade(doc.getForm().containsKey("orgOrigem_lotacaoSel.descricao") ? Localidade.OUTRA : Localidade.RIO_DE_JANEIRO);
			m.setNumero(doc.getForm().get("tituloMateria")+i);
			m.setDt(doc.getDtFinalizacao());
			m.setConteudo(doc.getForm().get("texto_publicacao")+i);
			materias.add(m);
		}
		return materias;
	}

}
