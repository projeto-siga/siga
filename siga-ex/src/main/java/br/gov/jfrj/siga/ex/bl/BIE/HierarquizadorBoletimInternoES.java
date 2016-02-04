/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.ex.bl.BIE;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.hibernate.ExDao;

public class HierarquizadorBoletimInternoES {

	private class DescricaoNodoComparator implements Comparator<Nodo> {

		public int compare(Nodo o1, Nodo o2) {
			return o1.getDescr().compareTo(o2.getDescr());
		}
	}

	private class CidadeComparator implements Comparator<ExDocumento> {

		public int compare(ExDocumento o1, ExDocumento o2) {
			return o1.getLocalidadeString().compareTo(o2.getLocalidadeString());
		}

	}

	private class TipoDataComparator implements Comparator<ExDocumento> {

		public int compare(ExDocumento o1, ExDocumento o2) {
			if (o1.getExFormaDocumento().getIdFormaDoc() > o2
					.getExFormaDocumento().getIdFormaDoc())
				return 1;
			else if (o1.getExFormaDocumento().getIdFormaDoc() < o2
					.getExFormaDocumento().getIdFormaDoc())
				return -1;
			else if (o1.getDtFinalizacao().after(o2.getDtFinalizacao()))
				return 1;
			else if (o1.getDtFinalizacao().before(o2.getDtFinalizacao()))
				return -1;
			else
				return 0;
		}
	}

	private class DataComparator implements Comparator<ExDocumento> {

		public int compare(ExDocumento o1, ExDocumento o2) {
			if (o1.getDtFinalizacao().after(o2.getDtFinalizacao()))
				return 1;
			else if (o1.getDtFinalizacao().before(o2.getDtFinalizacao()))
				return -1;
			else
				return 0;
		}
	}

	
	

	

	private List<ExDocumento> docsAPublicar;
	private List<NodoMaior> nodosPrincipais;
	private HashMap<Long, Integer> ordemPorModelo;

	public HierarquizadorBoletimInternoES(CpOrgaoUsuario orgao) {
		this(orgao, ExDao.getInstance().consultarPorModeloParaPublicar(orgao));
	}

	public HierarquizadorBoletimInternoES(CpOrgaoUsuario orgao,
			List<ExDocumento> docsAPublicar) {

		setDocsAPublicar(docsAPublicar);

		criaEstruturaNodos();

		for (ExDocumento doc : getDocsAPublicar()) 
			alocaDocumento(doc);
		
		
		for (NodoMaior n : nodosPrincipais) {
			for (NodoMenor n2 : n.nodos) {
				n2.ordena();
				List<ExDocumento> novaLista = new ArrayList<ExDocumento>();
				for (ExDocumento docLista : n2.getExDocumentoSet()) {
					novaLista.add(docLista);
					if(docLista.getExDocumentoFilhoSet() != null) {
						for (ExDocumento docFilho : docLista.getExDocumentoFilhoSet()) {
							if(docFilho.getExFormaDocumento().getIdFormaDoc() == 60 && docFilho.isAssinado())
								novaLista.add(docFilho);
						}
					}
				}
				n2.setExDocumentoSet(novaLista);
			}
			n.ordena();
		}
	}
	
	public static String enxugaHtml(String html){
		int aPartirDe = 0;
		int posIni = html.indexOf("<p");
		int posFim = html.indexOf(">", posIni);
		
		while(posIni!=-1){
			String sub = html.substring(posIni, posFim);
			if (sub.indexOf("style=\"")!=-1)
				sub = sub.replace("style=\"", "style=\"margin-bottom:7px; margin-top:7px;");
			else 
				sub = sub + " style=\"margin-bottom:7px; margin-top:7px;\" ";
			
			String[] atributos = sub.split(";");
			
			for (String atributo : atributos) {
				if(atributo.contains("text-indent:")) {
					sub = sub.replace(atributo + ";", "");
				}
				if(atributo.contains("text-align:left")) {
					sub = sub.replace(atributo, "text-align:justify");
				}
			}
			
			html = html.substring(0, posIni) + sub + html.substring(posFim, html.length());
			aPartirDe = posFim + 1;
			posIni = html.indexOf("<p", aPartirDe);
			posFim = html.indexOf(">", posIni);
		}
		html = html.replace("&nbsp;", " ");
		return html;
		
	}

	private void criaEstruturaNodos() {
		nodosPrincipais = new ArrayList<NodoMaior>();

		ordemPorModelo = new HashMap<Long, Integer>();

		NodoMaior nodoSede = new NodoMaior("SEÇÃO JUDICIÁRIA - SEDE");

		nodoSede.getNodos().add(
				new NodoMenor("NORMAS INTERNAS", new DataComparator()));

		ordemPorModelo.put(145L, 0);

		nodoSede.getNodos().add(
				new NodoMenor("ORDENS DE SERVIÇO", new DataComparator()));

		ordemPorModelo.put(76L, 1);
		ordemPorModelo.put(148L, 1);

		nodoSede.getNodos().add(
				new NodoMenor("PORTARIAS DF (DIREÇÃO DO FORO)", new DataComparator()));

		ordemPorModelo.put(73L, 2);
		ordemPorModelo.put(215L, 2);
		ordemPorModelo.put(216L, 2);
		ordemPorModelo.put(217L, 2);
		ordemPorModelo.put(218L, 2);

		nodoSede.getNodos().add(
				new NodoMenor("PORTARIAS SG (SECRETARIA GERAL)", new DataComparator()));

		ordemPorModelo.put(144L, 3);


		nodoSede.getNodos().add(
				new NodoMenor("PORTARIAS NGP (NÚCLEO DE GESTÃO DE PESSOAS)", new DataComparator()));

		ordemPorModelo.put(85L, 4);

		nodoSede.getNodos().add(
				new NodoMenor("DIÁRIAS", new DataComparator()));


		ordemPorModelo.put(84L, 5);
		ordemPorModelo.put(147L, 5);

		nodoSede.getNodos().add(
				new NodoMenor("SUPRIMENTOS DE FUNDOS", new DataComparator()));

		/*ordemPorModelo.put(233L, 6);

		nodoSede.getNodos().add(
				new NodoMenor("CONCESSÃO DE DIÁRIAS", new DataComparator()));

		ordemPorModelo.put(231L, 7);

		nodoSede.getNodos().add(
				new NodoMenor("TABELA DE CONCESSÃO DE SUPRIMENTO DE FUNDOS",
						new DataComparator()));

		ordemPorModelo.put(232L, 8);

		nodoSede.getNodos().add(
				new NodoMenor("ERRATAS", new TipoDataComparator()));

		nodoSede.getNodos().add(
				new NodoMenor("ANEXOS", new TipoDataComparator()));*/

		nodoSede.getNodos().add(
				new NodoMenor("OUTROS", new TipoDataComparator()));

		nodosPrincipais.add(nodoSede);

		NodoMaior nodoSubsecoes = new NodoMaior(
				"PORTARIAS DAS SUBSEÇÕES JUDICIÁRIAS",
				new DescricaoNodoComparator());
		nodosPrincipais.add(nodoSubsecoes);
	}

	private void alocaDocumento(ExDocumento doc) {

		// Se é matéria do Rio...
		if ((doc.getExModelo().getHisIdIni() != 234 &&  doc.getLocalidadeString().trim().toLowerCase().equals(
				"vitória")) ||
				(doc.getExModelo().getHisIdIni() == 234 &&  !doc.getForm().containsKey("orgOrigem_lotacaoSel.descricao"))) {

			// Se não existe subtópico no boletim pro modelo desse doc...
			if (!ordemPorModelo.containsKey(doc.getExModelo().getHisIdIni())) {

				// Se é matéria livre...
				if (doc.getExModelo().getHisIdIni() == 234) {
					for (String chave : doc.getForm().keySet()) {
						if (chave.contains("tipoMateria")) {
							String tipoMat = doc.getForm().get(chave);
							if (tipoMat != null
									&& tipoMat.equals("Portaria SGP"))
								nodosPrincipais.get(0).getNodos().get(4)
										.getExDocumentoSet().add(doc);
							else if (tipoMat != null
									&& tipoMat.equals("Portaria"))
								nodosPrincipais.get(0).getNodos().get(2)
										.getExDocumentoSet().add(doc);
							else if (tipoMat != null
									&& tipoMat.equals("Errata"))
								nodosPrincipais.get(0).getNodos().get(9)
										.getExDocumentoSet().add(doc);
							else if (tipoMat != null && tipoMat.equals("Anexo"))
								nodosPrincipais.get(0).getNodos().get(10)
										.getExDocumentoSet().add(doc);
							else
								nodosPrincipais.get(0).getNodos().get(11)
										.getExDocumentoSet().add(doc);
						}
					}

					// Se não é matéria livre...
				} else
					nodosPrincipais.get(0).getNodos().get(11)
							.getExDocumentoSet().add(doc);
			}

			// Se existe tópico pra esse modelo...
			else
				nodosPrincipais.get(0).getNodos().get(
						ordemPorModelo.get(doc.getExModelo().getHisIdIni()))
						.getExDocumentoSet().add(doc);

			// Se é do interior...
		} else {

			if (!nodosPrincipais.get(1).contemNodo(
					doc.getLocalidadeStringMaiusculas()))
				nodosPrincipais.get(1).getNodos().add(
						new NodoMenor(doc.getLocalidadeStringMaiusculas(),
								new DataComparator()));

			nodosPrincipais.get(1).getNodos().get(
					nodosPrincipais.get(1).indiceDe(
							doc.getLocalidadeStringMaiusculas()))
					.getExDocumentoSet().add(doc);
		}
	}

	public static int obterIndiceMateriaLivreDoc(NodoMenor nodoMenor, ExDocumento doc,
			Integer i) {
		String tipoMateria = "";
		int contador = 1;
		if (nodoMenor.getDescr().equals("ANEXOS"))
			tipoMateria = "Anexo";
		else if (nodoMenor.getDescr().equals("PORTARIAS"))
			tipoMateria = "Portaria";
		else if (nodoMenor.getDescr().equals("PORTARIAS DA SGP"))
			tipoMateria = "Portaria SGP";
		else if (nodoMenor.getDescr().equals("ERRATAS"))
			tipoMateria = "Errata";
		else if (nodoMenor.getDescr().equals("OUTROS"))
			tipoMateria = "Campo Livre";

		for (String chave : doc.getForm().keySet()) {
			String chave2[] = chave.split("tipoMateria");
			if (chave2.length > 1)
				if (doc.getForm().get(chave).equals(tipoMateria))
					if (contador == i)
						return Integer.parseInt(chave2[1]);
					else
						contador++;
		}
		return 0;
	}

	public static String obterTituloMateriaLivre(NodoMenor nodoMenor,
			ExDocumento doc, Integer i) {
		return doc.getForm().get(
				"tituloMateria" + obterIndiceMateriaLivreDoc(nodoMenor, doc, i));
	}

	public static String obterBlobMateriaLivre(NodoMenor nodoMenor,
			ExDocumento doc, Integer i) {
		return doc.getForm().get(
				"texto_publicacao" + obterIndiceMateriaLivreDoc(nodoMenor, doc, i));
	}

	public List<ExDocumento> getDocsAPublicar() {
		return docsAPublicar;
	}

	public void setDocsAPublicar(List<ExDocumento> docsAPublicar) {
		this.docsAPublicar = docsAPublicar;
	}

	public List<NodoMaior> getNodosPrincipais() {
		return nodosPrincipais;
	}

	public void setNodosPrincipais(List<NodoMaior> nodosPrincipais) {
		this.nodosPrincipais = nodosPrincipais;
	}
}