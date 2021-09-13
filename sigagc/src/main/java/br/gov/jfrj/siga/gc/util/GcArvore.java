package br.gov.jfrj.siga.gc.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeMap;

import br.gov.jfrj.siga.gc.model.GcInformacao;
import br.gov.jfrj.siga.gc.model.GcTag;

public class GcArvore extends TreeMap<GcTag, GcArvoreNo> {
	final HashMap<GcTag, Long> mapFreq = new HashMap<GcTag, Long>();
	final HashMap<GcInformacao, List<GcTag>> mapInf = new HashMap<GcInformacao, List<GcTag>>();
	final GcArvoreNo raiz = new GcArvoreNo();
	public int getContador() {
		return mapInf.size();
	}
	public void add(GcTag tag, GcInformacao inf) {
		// Find frequency of each individual item (requires 1 table scan)
		if (mapFreq.containsKey(tag)) {
			mapFreq.put(tag, mapFreq.get(tag) + 1);
		} else {
			mapFreq.put(tag, 1L);
		}

		// Store the tuples
		if (!mapInf.containsKey(inf)) {
			mapInf.put(inf, new ArrayList<GcTag>());
		}
		mapInf.get(inf).add(tag);
	}

	public void build() {
		buildSortTags();
		buildFPTree();
		buildSortFPTreeNodes();
	}

	public void build(String classificacao) {
		buildSortTags();
		buildFPTree(classificacao);
		buildSortFPTreeNodes();
	}
	
	// Sort items' tags in descending order of their frequency
	public void buildSortTags() {
		for (List<GcTag> tags : mapInf.values()) {
			Collections.sort(tags, new Comparator<GcTag>() {
				@Override
				public int compare(GcTag o1, GcTag o2) {
					Long long1 = mapFreq.get(o1);
					Long long2 = mapFreq.get(o2);
					if (long1 < long2)
						return 1;
					if (long1 > long2)
						return -1;

					int i = o1.toString().compareTo(o2.toString());
					if (i != 0)
						return i;

					return (o1.getId() < o2.getId() ? 1 : o1.getId() == o1.getId() ? 0 : -1);
				}
			});
		}
	}

	// Build the FP-Tree
	public void buildFPTree() {
		for (GcInformacao inf : mapInf.keySet()) {
			HashMap<GcTag, GcArvoreNo> galho = raiz.nos;
			for (GcTag tag : mapInf.get(inf)) {
				final GcArvoreNo no;
				if (!galho.containsKey(tag)) {
					no = new GcArvoreNo(this, tag, inf);
					galho.put(tag, no);
				} else {
					no = galho.get(tag);
					no.infs.add(inf);
				}
				galho = no.nos;
			}
		}
	}
	
	// Build the FP-Tree
	public void buildFPTree(String classificacao) {
		for (GcInformacao inf : mapInf.keySet()) {
			HashMap<GcTag, GcArvoreNo> galho = raiz.nos;
			for (GcTag tag : mapInf.get(inf)) {
				final GcArvoreNo no;

				if(tag.getTitulo().equals(classificacao)){

					if (!galho.containsKey(tag)) {
						no = new GcArvoreNo(this, tag, inf);
						galho.put(tag, no);
					} else {
						no = galho.get(tag);
						no.infs.add(inf);
					}
					galho = no.nos;

				}
			}
		}
	}

	// Sort FP-Tree
	public void buildSortFPTreeNodes() {
		raiz.build();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		printNos(sb, raiz.nosOrdenados, 0);
		return sb.toString();
	}

	public void printNos(StringBuilder sb, SortedSet<GcArvoreNo> nos, int ident) {
		for (GcArvoreNo no : nos) {
			for (int i = 0; i < ident; i++)
				sb.append("  ");

			sb.append(no.tag.getTitulo());
			for (GcTag tagIrmao : no.tagsIrmaos) {
				sb.append(", ");
				sb.append(tagIrmao.getTitulo());
			}

			sb.append(" (");
			sb.append(no.infs.size());
			sb.append(")\n");
			if (no.nos.size() > 0)
				printNos(sb, no.nosOrdenados, ident + 1);
		}
	}

	public String toHTML(String texto) throws Exception {
		StringBuilder sb = new StringBuilder();
		printNosHTML(sb, raiz.nosOrdenados, 0, texto);
		return sb.toString();
	}

	public void printNosHTML(StringBuilder sb, SortedSet<GcArvoreNo> nos,
			int ident, String texto) throws Exception {
		sb.append("<ul>");
		
		for (GcArvoreNo no : nos) {
			for (int i = 0; i < ident; i++)
				sb.append("  ");
			
			if( (no.tag.getTitulo() == "Conhecimentos_Sem_Classificacao") && (texto== null || texto=="" || texto.isEmpty())){
				sb.append("<li style='display:none'><b>");
				sb.append(no.tag.getTitulo());
				for (GcTag tagIrmao : no.tagsIrmaos) {
					sb.append(", ");
					sb.append(tagIrmao.getTitulo());
				}

				sb.append(" (");
				final int size = no.infs.size();
				sb.append(size);
				sb.append(")</b></li>");

				if (size > 0) {
					for (GcArvoreNo noFilho : no.nosOrdenados) {
						no.infs.removeAll(noFilho.infs);
					}
				}

				if (no.infs.size() > 0) {
					sb.append("<ul>");
					for (GcInformacao inf : no.infs) {
						sb.append("<li style='display:none' class=li"+ no.tag.getTitulo() + "><a href=\"exibir/" + URLEncoder.encode(inf.getSiglaCompacta(), "UTF-8") + "\">");
						sb.append(inf.getArq().getTitulo());
						sb.append("</a></li>");
					}
					sb.append("</ul>");
				}

				if (no.nos.size() > 0)
					printNosHTML(sb, no.nosOrdenados, ident + 1, texto);

			}

			else{
				final int size = no.infs.size();
				//sb.append("<td width=\"2%\">");
				sb.append("<li class=liclassificacao-" + no.tag.getTitulo() +"><b>");
				String imgPlus = "<img style=\"width: 13px;\" id=\"imgMais" + no.tag.getTitulo() + "\" src=\"/siga/css/famfamfam/icons/plus_toggle.png\" alt=\"mais\" title=\"Ver Detalhes\" />";
				String imgMinus = "<img style=\"width: 13px;\" id=\"imgMenos" + no.tag.getTitulo() + "\" src=\"/siga/css/famfamfam/icons/minus_toggle.png\" alt=\"menos\" title=\"Ocultar Detalhes\"/>";

				sb.append("<a class= classificacao-" + no.tag.getTitulo() +">" + no.tag.getTitulo());
				sb.append("</a>");

				for (GcTag tagIrmao : no.tagsIrmaos) {
					sb.append(", "); /////// Tag irmao nao eh tratada como link "a"
					sb.append(tagIrmao.getTitulo());
				}
				
				if (size > 0) {
					for (GcArvoreNo noFilho : no.nosOrdenados) {
						no.infs.removeAll(noFilho.infs);
					}
				}
				
				sb.append(" (");
				sb.append(size);
				sb.append(")</b>");

				if(no.infs.size() > 2){
					sb.append(imgPlus);
					sb.append(" " + imgMinus);
				}
				sb.append("</li>");
						
				if (no.infs.size() < 3) {
					sb.append("<ul>");
					for (GcInformacao inf : no.infs) {
						sb.append("<li class=li"+ no.tag.getTitulo() +"><a href=\"exibir/" + URLEncoder.encode(inf.getSiglaCompacta(), "UTF-8") + "\">");
						sb.append(inf.getArq().getTitulo());
						sb.append("</a></li>");
					}
					sb.append("</ul>");
				}
				else{
					sb.append("<ul>");
					int contador = 0;
					for (GcInformacao inf : no.infs) {
						++contador;
						//sb.append("<li><a href=\"exibir?id=" + inf.id + "\">");
						
						if(contador < 3){
							sb.append("<li class=li" + no.tag.getTitulo() + contador + "><a href=\"exibir/" + URLEncoder.encode(inf.getSiglaCompacta(), "UTF-8") + "\">");
						}
						else{
							sb.append("<li style='display:none' class=li"+ no.tag.getTitulo() +"><a href=\"exibir/" + URLEncoder.encode(inf.getSiglaCompacta(), "UTF-8") + "\">");
						}
						sb.append(inf.getArq().getTitulo());
						sb.append("</a></li>");
					}
					sb.append("</ul>");
				}
				
				if (no.nos.size() > 0){
					printNosHTML(sb, no.nosOrdenados, ident + 1, texto);
				}
			}
		}
		sb.append("</ul>");
	}
}
